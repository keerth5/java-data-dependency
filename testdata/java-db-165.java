// @testdata/UserTransactionUsageTest.java
package com.example.test;

import javax.naming.InitialContext;
import javax.transaction.UserTransaction;
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
 * Sample code to test rule sql‑java‑165: UserTransactionUsage
 */
public class UserTransactionUsageTest {

    // Violation: manual usage of UserTransaction
    public void manualUserTransactionViolation() {
        try {
            InitialContext ctx = new InitialContext();
            UserTransaction utx = (UserTransaction) ctx.lookup("java:comp/UserTransaction");

            utx.begin(); // Violation

            // JDBC operations
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "dbuser", "dbpassword");
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO users(name) VALUES('Alice')");
            stmt.executeUpdate();

            utx.commit(); // Violation
            System.out.println("Manual UserTransaction used (violation).");

        } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | javax.naming.NamingException | java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    // Non-violation: relying on container-managed transaction or Spring @Transactional
    public void frameworkManagedTransactionNonViolation() {
        // Example placeholder: framework-managed JTA transaction handled automatically
        System.out.println("Framework-managed transaction (non-violation).");
    }

    public static void main(String[] args) {
        UserTransactionUsageTest test = new UserTransactionUsageTest();
        test.manualUserTransactionViolation();         // Violation
        test.frameworkManagedTransactionNonViolation(); // Non-violation
    }
}
