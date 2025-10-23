// @testdata/JtaTransactionUsageTest.java
package com.example.test;

import javax.naming.InitialContext;
import javax.transaction.UserTransaction;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.TransactionManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * Sample code to test rule sql‑java‑164: JtaTransactionUsage
 */
public class JtaTransactionUsageTest {

    // Violation: using JTA UserTransaction directly
    public void userTransactionViolation() {
        try {
            InitialContext ctx = new InitialContext();
            UserTransaction utx = (UserTransaction) ctx.lookup("java:comp/UserTransaction");

            utx.begin(); // Violation: JTA transaction begins

            // Some JDBC operations
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "dbuser", "dbpassword");
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO users(name) VALUES('Alice')");
            stmt.executeUpdate();

            utx.commit(); // Violation: JTA commit
            System.out.println("Manual JTA transaction used (violation).");

        } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | javax.naming.NamingException | java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    // Violation: using TransactionManager directly
    public void transactionManagerViolation() {
        try {
            InitialContext ctx = new InitialContext();
            TransactionManager tm = (TransactionManager) ctx.lookup("java:comp/TransactionManager");

            tm.begin(); // Violation
            // JDBC operations
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "dbuser", "dbpassword");
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO users(name) VALUES('Bob')");
            stmt.executeUpdate();

            tm.commit(); // Violation
            System.out.println("Manual TransactionManager usage (violation).");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Non-violation: using container-managed transactions or Spring @Transactional
    public void frameworkManagedTransactionNonViolation() {
        // Example placeholder: Spring @Transactional would handle JTA automatically
        System.out.println("Framework-managed transaction (non-violation).");
    }

    public static void main(String[] args) {
        JtaTransactionUsageTest test = new JtaTransactionUsageTest();
        test.userTransactionViolation();          // Violation
        test.transactionManagerViolation();       // Violation
        test.frameworkManagedTransactionNonViolation(); // Non-violation
    }
}
