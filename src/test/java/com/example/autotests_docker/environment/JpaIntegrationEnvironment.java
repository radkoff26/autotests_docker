package com.example.autotests_docker.environment;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = IntegrationEnvironment.TestDataSourceConfiguration.class)
public class JpaIntegrationEnvironment {
}
