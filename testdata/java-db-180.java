// @testdata/SpringComponentUsageTest.java
package com.example.test;

import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Sample code to test rule sql‑java‑180: SpringComponentUsage
 */
public class SpringComponentUsageTest {

    // Violation: class annotated with @Component
    @Component // Violation
    static class UserService {
        public void serveUser() {
            System.out.println("Serving user");
        }
    }

    // Violation: another component example
    @Component("customComponentName") // Violation
    static class OrderService {
        public void processOrder() {
            System.out.println("Processing order");
        }
    }

    // Non-violation: regular configuration class
    @Configuration // Non-violation
    static class AppConfig {
    }

    // Non-violation: Spring Boot application main class
    @SpringBootApplication // Non-violation
    static class MainApplication {
        public static void main(String[] args) {
            System.out.println("Main application start");
        }
    }

    // Non-violation: plain Java class
    static class PlainClass {
        public void doSomething() {
            System.out.println("Doing something");
        }
    }

    public static void main(String[] args) {
        UserService userService = new UserService();           // Violation
        OrderService orderService = new OrderService();       // Violation
        PlainClass plain = new PlainClass();                  // Non-violation
        AppConfig config = new AppConfig();                  // Non-violation
    }
}
