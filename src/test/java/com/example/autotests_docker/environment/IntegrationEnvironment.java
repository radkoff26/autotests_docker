package com.example.autotests_docker.environment;


import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.core.PostgresDatabase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.DirectoryResourceAccessor;
import liquibase.resource.ResourceAccessor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Testcontainers
public abstract class IntegrationEnvironment {
    protected static final PostgreSQLContainer<?> PSQL_CONTAINER;

    private static final String CHANGELOG_FILE = "master.xml";
    private static final Path PATH_TO_CHANGELOG = new File("migrations/changelogs").toPath();

    static {
        PSQL_CONTAINER = new PostgreSQLContainer<>("postgres:15");
        PSQL_CONTAINER.start();
        executeDatabaseMigrations();
    }

    @TestConfiguration
    public static class TestDataSourceConfiguration {

        @Bean
        public DataSource dataSource() {
            return DataSourceBuilder.create()
                    .url(PSQL_CONTAINER.getJdbcUrl())
                    .username(PSQL_CONTAINER.getUsername())
                    .password(PSQL_CONTAINER.getPassword())
                    .build();
        }
    }

    private static void executeDatabaseMigrations() {
        try (Connection connection = DriverManager.getConnection(
                PSQL_CONTAINER.getJdbcUrl(),
                PSQL_CONTAINER.getUsername(),
                PSQL_CONTAINER.getPassword()
        )) {
            ResourceAccessor resourceAccessor = new DirectoryResourceAccessor(PATH_TO_CHANGELOG);
            Database postgres = new PostgresDatabase();
            postgres.setConnection(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase(CHANGELOG_FILE, resourceAccessor, postgres);
            liquibase.update();

        } catch (SQLException | FileNotFoundException | LiquibaseException e) {
            log.error("database migration failed");
            throw new RuntimeException(e);
        }
    }
}
