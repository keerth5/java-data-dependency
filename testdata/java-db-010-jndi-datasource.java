package com.example.database;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Test file for sql-java-010: JndiDataSourceUsage
 * Detects JNDI DataSource lookup usage
 */
public class JndiDataSourceExample {
    
    public void basicJndiDataSourceLookup() throws NamingException, SQLException {
        // Basic JNDI DataSource lookup
        Context ctx = new InitialContext();
        DataSource dataSource = (DataSource) ctx.lookup("java:/comp/env/jdbc/MyDataSource");
        
        Connection conn = dataSource.getConnection();
        conn.close();
    }
    
    public void jndiDataSourceWithContext() throws NamingException, SQLException {
        // JNDI DataSource lookup with context
        Context initialContext = new InitialContext();
        Context envContext = (Context) initialContext.lookup("java:/comp/env");
        DataSource dataSource = (DataSource) envContext.lookup("jdbc/MyDataSource");
        
        Connection conn = dataSource.getConnection();
        conn.close();
    }
    
    public void jndiDataSourceWithSubContext() throws NamingException, SQLException {
        // JNDI DataSource lookup with sub-context
        Context ctx = new InitialContext();
        DataSource dataSource = (DataSource) ctx.lookup("java:/comp/env/jdbc/datasource/MyDB");
        
        Connection conn = dataSource.getConnection();
        conn.close();
    }
    
    public void jndiDataSourceWithDifferentNames() throws NamingException, SQLException {
        // JNDI DataSource lookup with different naming patterns
        
        Context ctx = new InitialContext();
        
        // Standard JNDI names
        DataSource ds1 = (DataSource) ctx.lookup("java:/comp/env/jdbc/DataSource");
        DataSource ds2 = (DataSource) ctx.lookup("java:comp/env/jdbc/MyDataSource");
        DataSource ds3 = (DataSource) ctx.lookup("jdbc/MyDataSource");
        DataSource ds4 = (DataSource) ctx.lookup("java:/MyDataSource");
        
        // Application server specific names
        DataSource ds5 = (DataSource) ctx.lookup("java:jboss/datasources/MyDS");
        DataSource ds6 = (DataSource) ctx.lookup("java:app/jdbc/MyDataSource");
        DataSource ds7 = (DataSource) ctx.lookup("java:module/jdbc/DataSource");
        
        Connection conn1 = ds1.getConnection();
        Connection conn2 = ds2.getConnection();
        Connection conn3 = ds3.getConnection();
        Connection conn4 = ds4.getConnection();
        Connection conn5 = ds5.getConnection();
        Connection conn6 = ds6.getConnection();
        Connection conn7 = ds7.getConnection();
        
        conn1.close();
        conn2.close();
        conn3.close();
        conn4.close();
        conn5.close();
        conn6.close();
        conn7.close();
    }
    
    public void jndiDataSourceWithEnvironment() throws NamingException, SQLException {
        // JNDI DataSource lookup with environment context
        java.util.Hashtable<String, String> env = new java.util.Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
        env.put(Context.PROVIDER_URL, "jnp://localhost:1099");
        
        Context ctx = new InitialContext(env);
        DataSource dataSource = (DataSource) ctx.lookup("java:/comp/env/jdbc/MyDataSource");
        
        Connection conn = dataSource.getConnection();
        conn.close();
    }
    
    public void jndiDataSourceWithProperties() throws NamingException, SQLException {
        // JNDI DataSource lookup with properties
        java.util.Properties props = new java.util.Properties();
        props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.fscontext.RefFSContextFactory");
        props.setProperty(Context.PROVIDER_URL, "file:/tmp/jndi");
        
        Context ctx = new InitialContext(props);
        DataSource dataSource = (DataSource) ctx.lookup("jdbc/MyDataSource");
        
        Connection conn = dataSource.getConnection();
        conn.close();
    }
    
    public void jndiDataSourceWithTryWithResources() {
        // JNDI DataSource lookup with try-with-resources
        try (Connection conn = getJndiConnection()) {
            // Use connection
            System.out.println("JNDI DataSource connection established");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private Connection getJndiConnection() throws NamingException, SQLException {
        Context ctx = new InitialContext();
        DataSource dataSource = (DataSource) ctx.lookup("java:/comp/env/jdbc/MyDataSource");
        return dataSource.getConnection();
    }
    
    public void jndiDataSourceWithMethod() throws NamingException, SQLException {
        // JNDI DataSource lookup in method
        DataSource dataSource = lookupDataSource("java:/comp/env/jdbc/MyDataSource");
        Connection conn = dataSource.getConnection();
        conn.close();
    }
    
    private DataSource lookupDataSource(String jndiName) throws NamingException {
        Context ctx = new InitialContext();
        return (DataSource) ctx.lookup(jndiName);
    }
    
    public void jndiDataSourceWithSpring() throws NamingException, SQLException {
        // JNDI DataSource lookup for Spring framework
        Context ctx = new InitialContext();
        
        // Spring JNDI template style
        DataSource dataSource = (DataSource) ctx.lookup("java:/comp/env/jdbc/SpringDataSource");
        
        // Spring Boot JNDI style
        DataSource springBootDS = (DataSource) ctx.lookup("java:/comp/env/jdbc/SpringBootDataSource");
        
        Connection conn1 = dataSource.getConnection();
        Connection conn2 = springBootDS.getConnection();
        
        conn1.close();
        conn2.close();
    }
    
    public void jndiDataSourceWithEJB() throws NamingException, SQLException {
        // JNDI DataSource lookup for EJB
        Context ctx = new InitialContext();
        
        // EJB JNDI lookup
        DataSource ejbDataSource = (DataSource) ctx.lookup("java:/comp/env/jdbc/EJBDataSource");
        
        // EJB with application context
        DataSource appDataSource = (DataSource) ctx.lookup("java:app/jdbc/AppDataSource");
        
        Connection conn1 = ejbDataSource.getConnection();
        Connection conn2 = appDataSource.getConnection();
        
        conn1.close();
        conn2.close();
    }
    
    public void jndiDataSourceWithWebLogic() throws NamingException, SQLException {
        // JNDI DataSource lookup for WebLogic
        Context ctx = new InitialContext();
        
        // WebLogic specific JNDI names
        DataSource wlDataSource = (DataSource) ctx.lookup("java:/comp/env/jdbc/WebLogicDataSource");
        DataSource wlDS = (DataSource) ctx.lookup("jdbc/WebLogicDS");
        
        Connection conn1 = wlDataSource.getConnection();
        Connection conn2 = wlDS.getConnection();
        
        conn1.close();
        conn2.close();
    }
    
    public void jndiDataSourceWithWebSphere() throws NamingException, SQLException {
        // JNDI DataSource lookup for WebSphere
        Context ctx = new InitialContext();
        
        // WebSphere specific JNDI names
        DataSource wsDataSource = (DataSource) ctx.lookup("java:/comp/env/jdbc/WebSphereDataSource");
        DataSource wsDS = (DataSource) ctx.lookup("jdbc/WebSphereDS");
        
        Connection conn1 = wsDataSource.getConnection();
        Connection conn2 = wsDS.getConnection();
        
        conn1.close();
        conn2.close();
    }
    
    public void jndiDataSourceWithTomcat() throws NamingException, SQLException {
        // JNDI DataSource lookup for Tomcat
        Context ctx = new InitialContext();
        
        // Tomcat specific JNDI names
        DataSource tomcatDataSource = (DataSource) ctx.lookup("java:/comp/env/jdbc/TomcatDataSource");
        DataSource tomcatDS = (DataSource) ctx.lookup("jdbc/TomcatDS");
        
        Connection conn1 = tomcatDataSource.getConnection();
        Connection conn2 = tomcatDS.getConnection();
        
        conn1.close();
        conn2.close();
    }
    
    public void jndiDataSourceWithWildFly() throws NamingException, SQLException {
        // JNDI DataSource lookup for WildFly/JBoss
        Context ctx = new InitialContext();
        
        // WildFly/JBoss specific JNDI names
        DataSource wfDataSource = (DataSource) ctx.lookup("java:/comp/env/jdbc/WildFlyDataSource");
        DataSource jbossDS = (DataSource) ctx.lookup("java:jboss/datasources/JBossDS");
        
        Connection conn1 = wfDataSource.getConnection();
        Connection conn2 = jbossDS.getConnection();
        
        conn1.close();
        conn2.close();
    }
    
    public void jndiDataSourceMethodParameter(DataSource jndiDataSource) throws SQLException {
        // Method accepting DataSource from JNDI as parameter
        Connection conn = jndiDataSource.getConnection();
        conn.close();
    }
    
    public DataSource getJndiDataSource() throws NamingException {
        // Method returning DataSource from JNDI
        Context ctx = new InitialContext();
        return (DataSource) ctx.lookup("java:/comp/env/jdbc/MyDataSource");
    }
}
