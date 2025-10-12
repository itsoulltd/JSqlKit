package com.infoworks.entity;

import com.infoworks.connect.JDBCDriverClass;
import com.infoworks.db.SQLConnector;
import com.infoworks.sql.Person;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class EntityMapperTest {

    @Test
    public void basicTest() throws SQLException {
        ResultSet set = null; //Select * From Person;
        List<Person> persons = new PersonMapper().map(set);
        assertTrue(persons.isEmpty());
    }

    private static String[] PersonColNames = {"uuid", "name", "age", "active", "salary"};
    private static String[] PersonInsertQuery = {
            "INSERT INTO Person (uuid,name,age,active,salary) VALUES ('00992334','Cris',23,1, 1400.00);"
            , "INSERT INTO Person (uuid,name,age,active,salary) VALUES ('00342334','Adams',32,1, 3300.00);"
            , "INSERT INTO Person (uuid,name,age,active,salary) VALUES ('00242334','James',21,0, 5400.00);"
            , "INSERT INTO Person (uuid,name,age,active,salary) VALUES ('00122334','Hayes',44,1, 2400.00);"
            , "INSERT INTO Person (uuid,name,age,active,salary) VALUES ('00562334','Andy',36,0, 4800.00);"
    };

    private void insertSeedData() throws SQLException{
        try(Connection connection = SQLConnector.createConnection(JDBCDriverClass.H2_EMBEDDED, "testDB");
            Statement stmt = connection.createStatement()) {
            //
            AtomicInteger insertCount = new AtomicInteger(0);
            Arrays.stream(PersonInsertQuery).forEach(query -> {
                try {
                    int result = stmt.executeUpdate(query);
                    insertCount.incrementAndGet();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            });
            System.out.println("Inserted records into the table, Count: " + insertCount.get());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void map() throws SQLException {
        //First initiate database with tables:
        SQLConnector.executeScripts(JDBCDriverClass.H2_EMBEDDED, "testDB", new File("testDB-v1.4.200.sql"));
        //SQLConnector.executeScripts(JDBCDriverClass.H2_EMBEDDED, "testDB", new File("testDB-v2.2.220.sql"));
        //Now try to connect the database:testDB and insert seed rows:
        insertSeedData();
        //Now begin the actual test:
        try(Connection connection = SQLConnector.createConnection(JDBCDriverClass.H2_EMBEDDED, "testDB");
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT uuid,name,age,active,salary FROM Person")) {
            //Test Mapping:
            List<Person> persons = new PersonMapper().map(rs);
            Assert.assertNotNull(persons);
            Assert.assertFalse(persons.isEmpty());
            persons.forEach(person -> System.out.println(person.marshalling(false)));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}