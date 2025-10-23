// @testdata/SpringServiceUsageTest.java
package com.example.test;

import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Sample code to test rule sql‑java‑181: SpringServiceUsage
 */
public class SpringServiceUsageTest {

    // Violation: class annotated with @Service
    @Service // Violation
    static class UserService {
        public void serveUser() {
            System.out.println("Serving user");
        }
    }

    // Violation: another service example
    @Service("orderService") // Violation
    static class OrderService {
        public void processOrder() {
            System.out.println("Processing order");
        }
    }

    // Non-violation: class annotated with @Component
    @Component // Non-violation
    static class NotificationComponent {
        public void sendNotification() {
            System.out.println("Sending notification");
        }
    }

    // Non-violation: configuration class
    @Configuration // Non-violation
    static class AppConfig {
    }

    // Non-violation: Spring Boot main application
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
        NotificationComponent notification = new NotificationComponent(); // Non-violation
        PlainClass plain = new PlainClass();                  // Non-violation
        AppConfig config = new AppConfig();                  // Non-violation
    }
}
