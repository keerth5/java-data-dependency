// @testdata/SpringConfigurationUsageTest.java
package com.example.test;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Sample code to test rule sql‑java‑184: SpringConfigurationUsage
 */
public class SpringConfigurationUsageTest {

    // Violation: class annotated with @Configuration
    @Configuration // Violation
    static class AppConfig {
        // Bean definitions would go here
    }

    // Violation: another configuration class
    @Configuration("customConfig") // Violation
    static class DataConfig {
        // Bean definitions
    }

    // Non-violation: class annotated with @Component
    @Component // Non-violation
    static class NotificationComponent {
        public void sendNotification() {
            System.out.println("Sending notification");
        }
    }

    // Non-violation: class annotated with @Service
    @Service // Non-violation
    static class UserService {
        public void serveUser() {
            System.out.println("Serving user");
        }
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
        AppConfig appConfig = new AppConfig();           // Violation
        DataConfig dataConfig = new DataConfig();       // Violation
        NotificationComponent notification = new NotificationComponent(); // Non-violation
        UserService userService = new UserService();    // Non-violation
        PlainClass plain = new PlainClass();            // Non-violation
    }
}
