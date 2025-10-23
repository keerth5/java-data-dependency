// @testdata/SpringBootApplicationPropertiesUsageTest.java
package com.example.test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.core.env.Environment;

/**
 * Sample code to test rule sql‑java‑178: SpringBootApplicationPropertiesUsage
 */
public class SpringBootApplicationPropertiesUsageTest {

    // Violation: class annotated with @SpringBootApplication using @Value
    @SpringBootApplication
    static class PropertiesUsageApplication {

        @Value("${database.url}") // Violation
        private String dbUrl;

        private final Environment env;

        public PropertiesUsageApplication(Environment env) {
            this.env = env;
        }

        public void printProperties() {
            String username = env.getProperty("database.username"); // Violation
            System.out.println("DB URL: " + dbUrl + ", username: " + username);
        }
    }

    // Violation: another property injection example
    @Component
    static class AnotherPropertiesComponent {
        @Value("${server.port}") // Violation
        private int serverPort;

        public void printPort() {
            System.out.println("Server port: " + serverPort);
        }
    }

    // Non-violation: regular configuration class without property usage
    @Configuration // Non-violation
    static class RegularConfiguration {
    }

    // Non-violation: plain component without property usage
    @Component // Non-violation
    static class PlainComponent {
        public void sayHello() {
            System.out.println("Hello World");
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(PropertiesUsageApplication.class, args); // Violation
    }
}
