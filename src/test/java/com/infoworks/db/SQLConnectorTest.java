package com.infoworks.db;

import com.infoworks.connect.DriverClass;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLConnectorTest {

    @Test
    public void h2DbInMemConnectionTest() {
        try(Connection connection = SQLConnector.createConnection(DriverClass.H2_EMBEDDED, "testDB")) {
            Assert.assertTrue(connection != null);
            System.out.println("Connection was created.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void h2DbNewOrExistFileConnectionTest() {
        try(Connection connection = SQLConnector.createConnection(DriverClass.H2_FILE, "testDB")) {
            Assert.assertTrue(connection != null);
            System.out.println("Connection was created.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void h2DbNewOrExistFileAsPathConnectionTest() {
        try(Connection connection = SQLConnector.createConnection(DriverClass.H2_FILE, "../../target/testDB")) {
            Assert.assertTrue(connection != null);
            System.out.println("Connection was created.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
