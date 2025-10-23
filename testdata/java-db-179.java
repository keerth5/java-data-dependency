// @testdata/SpringBootYamlConfigurationUsageTest.java
package com.example.test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.core.env.Environment;

/**
 * Sample code to test rule sql‑java‑179: SpringBootYamlConfigurationUsage
 */
public class SpringBootYamlConfigurationUsageTest {

    // Violation: class annotated with @SpringBootApplication using @Value from YAML
    @SpringBootApplication
    static class YamlUsageApplication {

        @Value("${server.port}") // Violation
        private int serverPort;

        private final Environment env;

        public YamlUsageApplication(Environment env) {
            this.env = env;
        }

        public void printYamlProperties() {
            String datasourceUrl = env.getProperty("spring.datasource.url"); // Violation
            System.out.println("Server port: " + serverPort + ", datasource URL: " + datasourceUrl);
        }
    }

    // Violation: another component using YAML property
    @Component
    static class AnotherYamlComponent {
        @Value("${spring.datasource.username}") // Violation
        private String dbUser;

        public void printDbUser() {
            System.out.println("Database User: " + dbUser);
        }
    }

    // Non-violation: regular configuration class without YAML usage
    @Configuration // Non-violation
    static class RegularConfiguration {
    }

    // Non-violation: plain component without YAML usage
    @Component // Non-violation
    static class PlainComponent {
        public void sayHello() {
            System.out.println("Hello World");
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(YamlUsageApplication.class, args); // Violation
    }
}
