// @testdata/TransactionManagerUsageTest.java
package com.example.test;

import javax.naming.InitialContext;
import javax.transaction.TransactionManager;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * Sample code to test rule sql‑java‑166: TransactionManagerUsage
 */
public class TransactionManagerUsageTest {

    // Violation: manual usage of TransactionManager
    public void manualTransactionManagerViolation() {
        try {
            InitialContext ctx = new InitialContext();
            TransactionManager tm = (TransactionManager) ctx.lookup("java:comp/TransactionManager");

            tm.begin(); // Violation: manual transaction start

            // JDBC operations
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "dbuser", "dbpassword");
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO users(name) VALUES('Alice')");
            stmt.executeUpdate();

            tm.commit(); // Violation: manual commit
            System.out.println("Manual TransactionManager used (violation).");

        } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | javax.naming.NamingException | java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    // Non-violation: container-managed or framework-managed transactions
    public void frameworkManagedTransactionNonViolation() {
        // Example placeholder: container or Spring-managed transactions
        System.out.println("Framework-managed transaction (non-violation).");
    }

    public static void main(String[] args) {
        TransactionManagerUsageTest test = new TransactionManagerUsageTest();
        test.manualTransactionManagerViolation();         // Violation
        test.frameworkManagedTransactionNonViolation();   // Non-violation
    }
}
