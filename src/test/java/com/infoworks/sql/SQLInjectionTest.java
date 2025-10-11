package com.infoworks.sql;

import com.infoworks.connect.JDBCDriverClass;
import com.infoworks.script.SQLScriptExecutor;
import com.infoworks.entity.Entity;
import com.infoworks.sql.executor.SQLExecutor;
import com.infoworks.sql.query.QueryType;
import com.infoworks.sql.query.SQLInsertQuery;
import com.infoworks.sql.query.SQLQuery;
import com.infoworks.sql.query.SQLSelectQuery;
import com.infoworks.orm.DataType;
import com.infoworks.orm.Property;
import com.infoworks.orm.Row;
import com.infoworks.sql.query.models.Where;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class SQLInjectionTest {

    String sql_file_name = "testDB.sql";
    String sql_file_name_v14 = "testDB-v1.4.200.sql";
    String sql_file_name_v22 = "testDB-v2.2.220.sql";
    private SQLExecutor exe;

    @Before
    public void setUp() throws Exception {
        exe = new SQLExecutor.Builder(JDBCDriverClass.H2_EMBEDDED)
                .database("testH2DB")
                .credential("sa", "").build();
        //
        SQLScriptExecutor runner = new SQLScriptExecutor();
        File file = new File(sql_file_name_v14);
        String[] cmds = runner.commands(runner.createStream(file));
        for (String cmd:cmds) {
            try {
                exe.executeDDLQuery(cmd);
            } catch (SQLException throwables) {}
        }
        //
    }

    @After
    public void tearDown() throws Exception {
        exe.close();
        exe = null;
    }

    @Test
    public void selectQuery() throws SQLException, InstantiationException, IllegalAccessException {
        //
        insert();
        //
        SQLSelectQuery query = new SQLQuery.Builder(QueryType.SELECT)
                .columns()
                .from("Passenger")
                .where(new Where("name").isEqualTo("MyName-A")).build();
        System.out.println(query.toString());
        List<Passenger> results = exe.executeSelect(query, Passenger.class, Entity.mapColumnsToProperties(Passenger.class));
        Assert.assertEquals(results.size(), 1);
        results.forEach(pass -> System.out.println(pass.marshalling(true)));

        //AVOID: 'OR'/'or'/'AND'/'and' in the value. e.g. SELECT * FROM Users WHERE UserId = 105 OR 1=1;
        query = new SQLQuery.Builder(QueryType.SELECT)
                .columns()
                .from("Passenger")
                .where(new Where("name").isEqualTo("MyName-A OR 1=1")).build();
        System.out.println(query.toString());
        results = exe.executeSelect(query, Passenger.class, Entity.mapColumnsToProperties(Passenger.class));
        Assert.assertEquals(results.size(), 0);

        //AVOID: ""="" e.g.  SELECT * FROM Users WHERE Name ="" or ""="" AND Pass ="" or ""="";
        query = new SQLQuery.Builder(QueryType.SELECT)
                .columns()
                .from("Passenger")
                .where(new Where("name").isEqualTo("MyName-A OR \"\"=\"\"")).build();
        System.out.println(query.toString());
        results = exe.executeSelect(query, Passenger.class, Entity.mapColumnsToProperties(Passenger.class));
        Assert.assertEquals(results.size(), 0);

        //AVOID: Batched SQL Statements. e.g.
        //txtUserId = getRequestString("UserId");
        //txtSQL = "SELECT * FROM Users WHERE UserId = " + txtUserId;
        //User input =>  txtUserId = 105; DROP TABLE Suppliers
        //results in=> SELECT * FROM Users WHERE UserId = 105; DROP TABLE Suppliers;
        query = new SQLQuery.Builder(QueryType.SELECT)
                .columns()
                .from("Passenger")
                .where(new Where("name").isEqualTo("MyName-A ; DROP TABLE Passenger;")).build();
        System.out.println(query.toString());
        results = exe.executeSelect(query, Passenger.class, Entity.mapColumnsToProperties(Passenger.class));
        Assert.assertEquals(results.size(), 0);
    }

    @Test
    public void selectQueryBindToValue() throws SQLException, InstantiationException, IllegalAccessException {
        //
        insert();
        //
        SQLSelectQuery query = new SQLQuery.Builder(QueryType.SELECT)
                .columns()
                .from("Passenger")
                .where(new Where("name").isLike("%Na%")).build();
        System.out.println(query.bindValueToString());
        List<Passenger> results = exe.executeSelect(query.bindValueToString(), Passenger.class, Entity.mapColumnsToProperties(Passenger.class));
        Assert.assertEquals(results.size(), 1);
        results.forEach(pass -> System.out.println(pass.marshalling(true)));

        //AVOID: 'OR'/'or'/'AND'/'and' in the value. e.g. SELECT * FROM Users WHERE UserId = 105 OR 1=1;
        query = new SQLQuery.Builder(QueryType.SELECT)
                .columns()
                .from("Passenger")
                .where(new Where("name").isEqualTo("MyName-A OR 1=1")).build();
        System.out.println(query.bindValueToString());
        results = exe.executeSelect(query.bindValueToString(), Passenger.class, Entity.mapColumnsToProperties(Passenger.class));
        Assert.assertEquals(results.size(), 0);

        //AVOID: ""="" e.g.  SELECT * FROM Users WHERE Name ="" or ""="" AND Pass ="" or ""="";
        query = new SQLQuery.Builder(QueryType.SELECT)
                .columns()
                .from("Passenger")
                .where(new Where("name").isEqualTo("MyName-A OR \"\"=\"\"")).build();
        System.out.println(query.bindValueToString());
        results = exe.executeSelect(query.bindValueToString(), Passenger.class, Entity.mapColumnsToProperties(Passenger.class));
        Assert.assertEquals(results.size(), 0);

        //AVOID: ""="" e.g.  SELECT * FROM Users WHERE Name ="" or ""="" AND Pass ="" or ""="";
        query = new SQLQuery.Builder(QueryType.SELECT)
                .columns()
                .from("Passenger")
                .where(new Where("name").isLike("%ame% OR \"\"=\"\"")).build();
        System.out.println(query.bindValueToString());
        results = exe.executeSelect(query.bindValueToString(), Passenger.class, Entity.mapColumnsToProperties(Passenger.class));
        Assert.assertEquals(results.size(), 0);

        //AVOID: Batched SQL Statements. e.g.
        //txtUserId = getRequestString("UserId");
        //txtSQL = "SELECT * FROM Users WHERE UserId = " + txtUserId;
        //User input =>  txtUserId = 105; DROP TABLE Suppliers
        //results in=> SELECT * FROM Users WHERE UserId = 105; DROP TABLE Suppliers;
        query = new SQLQuery.Builder(QueryType.SELECT)
                .columns()
                .from("Passenger")
                .where(new Where("name").isEqualTo("MyName-A ; DROP TABLE Passenger;")).build();
        System.out.println(query.bindValueToString());
        results = exe.executeSelect(query.bindValueToString(), Passenger.class, Entity.mapColumnsToProperties(Passenger.class));
        Assert.assertEquals(results.size(), 0);
    }

    @Test
    public void insert() throws SQLException {
        SQLInsertQuery query = new SQLQuery.Builder(QueryType.INSERT)
                .into("Passenger")
                .values(new Row()
                        .add("name","MyName-A")
                        .add(new Property("age", 18, DataType.INT))
                        .add("sex", null)
                        .add("dob", new Timestamp(new java.util.Date().getTime()))
                        .getProperties().toArray(new Property[0]))
                .build();
        System.out.println(query.bindValueToString());
        int res = exe.executeInsert(true, query);
        Assert.assertTrue(res > 0);
    }

}
