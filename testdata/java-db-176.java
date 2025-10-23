// @testdata/SpringBootAutoConfigurationUsageTest.java
package com.example.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Sample code to test rule sql‑java‑176: SpringBootAutoConfigurationUsage
 */
public class SpringBootAutoConfigurationUsageTest {

    // Violation: main class annotated with @SpringBootApplication (auto-configuration enabled)
    @SpringBootApplication // Violation
    static class AutoConfiguredApplication {
        public static void main(String[] args) {
            SpringApplication.run(AutoConfiguredApplication.class, args);
            System.out.println("Spring Boot auto-configuration (violation).");
        }
    }

    // Violation: another class using @SpringBootApplication (auto-configuration)
    @SpringBootApplication(scanBasePackages = "com.example.services") // Violation
    static class AnotherAutoConfiguredApp {
    }

    // Non-violation: regular configuration class without auto-configuration
    @Configuration // Non-violation
    static class RegularConfiguration {
    }

    // Non-violation: plain component class
    @Component // Non-violation
    static class PlainComponent {
    }

    public static void main(String[] args) {
        SpringApplication.run(AutoConfiguredApplication.class, args); // Violation
    }
}
