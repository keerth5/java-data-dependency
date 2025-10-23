// @testdata/SpringAutowiredUsageTest.java
package com.example.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Sample code to test rule sql‑java‑183: SpringAutowiredUsage
 */
public class SpringAutowiredUsageTest {

    // Violation: field injection using @Autowired
    @Component
    static class UserComponent {
        @Autowired // Violation
        private UserService userService;

        public void serve() {
            userService.serveUser();
        }
    }

    // Violation: constructor injection using @Autowired
    @Service
    static class OrderService {
        private final OrderRepository orderRepository;

        @Autowired // Violation
        public OrderService(OrderRepository orderRepository) {
            this.orderRepository = orderRepository;
        }

        public void processOrder() {
            System.out.println("Processing order");
        }
    }

    // Non-violation: plain Java class without @Autowired
    static class PlainClass {
        private String name;

        public void sayHello() {
            System.out.println("Hello " + name);
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
        UserComponent userComponent = new UserComponent(); // Violation
        OrderService orderService = new OrderService(new OrderRepository()); // Violation
        PlainClass plain = new PlainClass(); // Non-violation
        AppConfig config = new AppConfig(); // Non-violation
    }

    // Support classes
    static class UserService {
        public void serveUser() {
            System.out.println("Serving user");
        }
    }

    static class OrderRepository {
        public void saveOrder(String order) {
            System.out.println("Saving order: " + order);
        }
    }
}
