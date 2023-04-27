package com.example.autotests_docker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import com.example.autotests_docker.environment.IntegrationEnvironment;

import static junit.framework.TestCase.assertTrue;

public class DatabaseMigrationTest extends IntegrationEnvironment {

    @Test
    public void applyMigrationsTest() {
        try (Connection connection = DriverManager.getConnection(
                PSQL_CONTAINER.getJdbcUrl(),
                PSQL_CONTAINER.getUsername(),
                PSQL_CONTAINER.getPassword()
        )) {
            assertTrue(tableExist(connection, "link"));
            assertTrue(tableExist(connection, "link_chat"));
            assertTrue(tableExist(connection, "chat"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean tableExist(Connection connection, String tableName) throws SQLException {
        try (ResultSet rs = connection.getMetaData().getTables(null, null, tableName, null)) {
            return rs.next();
        }
    }
}
