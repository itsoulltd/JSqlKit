package com.infoworks.sql;

import com.infoworks.PLogger;
import com.infoworks.connect.JDBCDriverClass;
import com.infoworks.script.SQLScriptExecutor;
import com.infoworks.entity.Entity;
import com.infoworks.sql.executor.SQLExecutor;
import com.infoworks.sql.query.QueryType;
import com.infoworks.sql.query.SQLQuery;
import com.infoworks.sql.query.SQLScalarQuery;
import com.infoworks.sql.query.models.Expression;
import com.infoworks.sql.query.models.Predicate;
import com.infoworks.orm.Row;
import com.infoworks.sql.query.models.Where;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EntityCRUDTest {

    private SQLExecutor exe;

    @Before
    public void setUp() throws Exception {
        exe = new SQLExecutor.Builder(JDBCDriverClass.H2_EMBEDDED)
                .database("testH2DB")
                .credential("sa", "").build();
        //
        SQLScriptExecutor runner = new SQLScriptExecutor();
        File file = new File("testDB.sql");
        String[] cmds = runner.commands(runner.createStream(file));
        for (String cmd:cmds) {
            try {
                exe.executeDDLQuery(cmd);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        //
    }

    @After
    public void tearDown() throws Exception {
        exe.close();
        exe = null;
    }

    public enum Gender {
        MALE,
        FEMALE,
        TRANSGENDER,
        NONE
    }

    private void insertSeedPassenger(SQLExecutor executor) {
        try {
            new Passenger("Towhid Islam", Gender.MALE.name(), 37).insert(executor);
            new Passenger("Sohana Islam Khan", Gender.FEMALE.name(), 27).insert(executor);
            new Passenger("Tanvir Islam", Gender.MALE.name(), 33).insert(executor);
            new Passenger("Sumaiya Islam", Gender.FEMALE.name(), 24).insert(executor);
            new Passenger("Tasnim Islam Khan", Gender.FEMALE.name(), 19).insert(executor);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void insertSeedPassengerInBatch(SQLExecutor executor) {
        try {
            List<Row> batch = new ArrayList<>();
            batch.add(new Passenger("Towhid Islam", Gender.MALE.name(), 37).getRow());
            batch.add(new Passenger("Sohana Islam ", Gender.FEMALE.name(), 27).getRow());
            batch.add(new Passenger("Tanvir Islam", Gender.MALE.name(), 33).getRow());
            batch.add(new Passenger("Sumaiya Islam", Gender.FEMALE.name(), 24).getRow());
            batch.add(new Passenger("Tasnim Islam Khan", Gender.FEMALE.name(), 19).getRow());
            batch.add(new Passenger("Tanvir Islam", Gender.MALE.name(), 33).getRow());
            batch.add(new Passenger("Sumaiya Islam", Gender.FEMALE.name(), 24).getRow());
            batch.add(new Passenger("Sohana Islam Khan", Gender.FEMALE.name(), 27).getRow());
            batch.add(new Passenger("Tanvir Islam Akand", Gender.MALE.name(), 33).getRow());
            batch.add(new Passenger("Sumaiya Islam", Gender.FEMALE.name(), 24).getRow());
            batch.add(new Passenger("Tasnim Islam Khan", Gender.FEMALE.name(), 19).getRow());
            batch.add(new Passenger("Tanvir Islam", Gender.MALE.name(), 33).getRow());
            //Insert In Batch:
            Entity.insert(Passenger.class, executor, 5, batch);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private int getCount(Class<? extends Entity> entityType, SQLExecutor executor, Expression expression) throws SQLException {
        SQLScalarQuery query = (expression == null) ? new SQLQuery.Builder(QueryType.COUNT).columns().from(entityType).build()
                : new SQLQuery.Builder(QueryType.COUNT).columns().from(entityType).where(expression).build();
        int totalFound = executor.getScalarValue(query);
        return totalFound;
    }

    @Test
    public void CreateInBatchTest() throws SQLException {
        int count = Entity.count(Passenger.class, exe, null);
        Assert.assertTrue(count == 0);
        System.out.println("Before COUNT: " + count);
        //
        PLogger logger = new PLogger();
        insertSeedPassengerInBatch(exe);
        logger.printMillis("InsertInBatch: ");
        //
        count = Entity.count(Passenger.class, exe, null);
        Assert.assertTrue(count > 0);
        System.out.println("After COUNT: " + count);
    }

    @Test
    public void ReadAsyncTest() {
        //Seeding:
        insertSeedPassengerInBatch(exe);
        //
        PLogger logger = new PLogger();
        Predicate clause = new Where("name").isLike("%Islam%");
        Entity.read(Passenger.class, exe, 5, clause, (pass) -> {
            //Printing: Page By Page
            System.out.println("==============================================");
            pass.stream().map(Passenger::getName).forEach(System.out::println);
        });
        logger.printMillis("Read-Async: ");
    }

    @Test
    public void ReadTest() throws Exception {
        //Seeding:
        insertSeedPassengerInBatch(exe);
        //
        PLogger logger = new PLogger();
        Predicate clause = new Where("name").isLike("%Islam%");
        List<Passenger> pass = Entity.read(Passenger.class, exe, clause);
        logger.printMillis("Read-Sync: ");
        //
        Assert.assertTrue(!pass.isEmpty());
        pass.stream().map(Passenger::getName).forEach(System.out::println);
    }

    @Test
    public void UpdateTest() throws Exception {
        //Seeding
        insertSeedPassenger(exe);
        List<Passenger> pass = Entity.read(Passenger.class, exe
                , new Where("name").isLike("%Tow%"));
        pass.stream()
                .map(passenger -> passenger.getName() + ":" + passenger.getAge())
                .forEach(System.out::println);
        //
        PLogger logger = new PLogger();
        Entity.update(Passenger.class, exe
                , new Where("name").isLike("%Tow%")
                , new Row().add("name", "MD.Towhid Islam")
                        .add("age", 42));
        logger.printMillis("Update: ");
        //
        pass = Entity.read(Passenger.class, exe
                , new Where("name").isLike("%Tow%"));
        Assert.assertTrue(!pass.isEmpty());
        Assert.assertTrue(pass.get(0).getName().equalsIgnoreCase("MD.Towhid Islam"));
        Assert.assertTrue(pass.get(0).getAge().equals(42));
        pass.stream()
                .map(passenger -> passenger.getName() + ":" + passenger.getAge())
                .forEach(System.out::println);
    }

    @Test
    public void DeleteTest() throws Exception {
        //Seeding
        insertSeedPassenger(exe);
        int count = Entity.count(Passenger.class, exe, new Where("name").isLike("%Tow%"));
        Assert.assertTrue(count > 0);
        System.out.println("Before Count: " + count);
        //
        PLogger logger = new PLogger();
        Entity.delete(Passenger.class, exe, new Where("name").isLike("%Tow%"));
        logger.printMillis("Delete: ");
        //
        count = Entity.count(Passenger.class, exe, new Where("name").isLike("%Tow%"));
        Assert.assertTrue(count == 0);
        System.out.println("After Count: " + count);
    }

    @Test
    public void CountTest() throws SQLException {
        //Seeding
        insertSeedPassenger(exe);
        //
        PLogger logger = new PLogger();
        int count = Entity.count(Passenger.class, exe, null);
        Assert.assertTrue(count > 0);
        logger.printMillis("Scalar: ");
    }

}
