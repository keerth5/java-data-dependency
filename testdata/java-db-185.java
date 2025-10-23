// @testdata/JakartaEeAnnotationUsageTest.java
package com.example.test;

import jakarta.ejb.Stateless;
import jakarta.ejb.Singleton;
import jakarta.inject.Inject;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.stereotype.Service;

/**
 * Sample code to test rule sql‑java‑185: JakartaEeAnnotationUsage
 */
public class JakartaEeAnnotationUsageTest {

    // Violation: class annotated with @Stateless
    @Stateless // Violation
    static class UserBean {
        public void serveUser() {
            System.out.println("Serving user");
        }
    }

    // Violation: class annotated with @Singleton
    @Singleton // Violation
    static class OrderBean {
        public void processOrder() {
            System.out.println("Processing order");
        }
    }

    // Violation: field injection using Jakarta @Inject
    static class PaymentService {
        @Inject // Violation
        private OrderBean orderBean;

        public void makePayment() {
            System.out.println("Making payment");
        }
    }

    // Violation: class annotated with @Entity
    @Entity // Violation
    static class UserEntity {
        @Id
        private Long id;
        private String name;
    }

    // Non-violation: Spring Service
    @Service // Non-violation
    static class UserService {
        public void serveUser() {
            System.out.println("Spring user service");
        }
    }

    // Non-violation: plain Java class
    static class PlainClass {
        public void doSomething() {
            System.out.println("Doing something");
        }
    }

    public static void main(String[] args) {
        UserBean userBean = new UserBean();       // Violation
        OrderBean orderBean = new OrderBean();    // Violation
        PaymentService paymentService = new PaymentService(); // Violation
        UserEntity userEntity = new UserEntity(); // Violation
        UserService userService = new UserService(); // Non-violation
        PlainClass plain = new PlainClass();     // Non-violation
    }
}
