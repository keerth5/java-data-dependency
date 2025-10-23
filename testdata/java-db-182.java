// @testdata/SpringRepositoryUsageTest.java
package com.example.test;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Sample code to test rule sql‑java‑182: SpringRepositoryUsage
 */
public class SpringRepositoryUsageTest {

    // Violation: class annotated with @Repository
    @Repository // Violation
    static class UserRepository {
        public void saveUser(String name) {
            System.out.println("Saving user: " + name);
        }
    }

    // Violation: another repository example
    @Repository("orderRepository") // Violation
    static class OrderRepository {
        public void saveOrder(String order) {
            System.out.println("Saving order: " + order);
        }
    }

    // Non-violation: class annotated with @Service
    @Service // Non-violation
    static class UserService {
        public void serveUser() {
            System.out.println("Serving user");
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

    public static void main(String[] args) {
        UserRepository userRepo = new UserRepository();      // Violation
        OrderRepository orderRepo = new OrderRepository();  // Violation
        UserService userService = new UserService();        // Non-violation
        NotificationComponent notification = new NotificationComponent(); // Non-violation
        AppConfig config = new AppConfig();                // Non-violation
    }
}
