// sql-java-199: SystemFunctionUsage
// Detects system function calls (sp_, xp_ prefixes)
// This file tests detection of system stored procedure and function patterns

package com.example.jdbc;

import java.sql.*;

public class SystemFunctionExample {
    
    // VIOLATION: SQL Server system stored procedure (sp_ prefix)
    public void callSystemStoredProcedure(Connection conn) throws SQLException {
        CallableStatement cstmt = conn.prepareCall("{call sp_helpdb(?)}");
        cstmt.setString(1, "master");
        ResultSet rs = cstmt.executeQuery();
        while (rs.next()) {
            System.out.println("Database: " + rs.getString(1));
        }
        rs.close();
        cstmt.close();
    }
    
    // VIOLATION: SQL Server extended stored procedure (xp_ prefix)
    public void callExtendedStoredProcedure(Connection connection) throws SQLException {
        CallableStatement stmt = connection.prepareCall("{call xp_cmdshell(?)}");
        stmt.setString(1, "dir C:\\");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            System.out.println("Command output: " + rs.getString(1));
        }
        rs.close();
        stmt.close();
    }
    
    // VIOLATION: Multiple sp_ procedures
    public void multipleSystemProcedures(Connection conn) throws SQLException {
        // sp_configure
        CallableStatement cstmt1 = conn.prepareCall("{call sp_configure(?, ?)}");
        cstmt1.setString(1, "show advanced options");
        cstmt1.setInt(2, 1);
        cstmt1.execute();
        cstmt1.close();
        
        // sp_who
        CallableStatement cstmt2 = conn.prepareCall("{call sp_who}");
        ResultSet rs = cstmt2.executeQuery();
        rs.close();
        cstmt2.close();
    }
    
    // VIOLATION: sp_help system procedure
    public void spHelpProcedure(Connection connection) throws SQLException {
        CallableStatement cs = connection.prepareCall("{call sp_help(?)}");
        cs.setString(1, "users");
        ResultSet rs = cs.executeQuery();
        while (rs.next()) {
            System.out.println("Column: " + rs.getString(1));
        }
        rs.close();
        cs.close();
    }
    
    // VIOLATION: sp_spaceused system procedure
    public void spSpaceUsed(Connection conn) throws SQLException {
        CallableStatement cstmt = conn.prepareCall("{call sp_spaceused(?)}");
        cstmt.setString(1, "orders");
        ResultSet rs = cstmt.executeQuery();
        if (rs.next()) {
            System.out.println("Table size: " + rs.getString(2));
        }
        rs.close();
        cstmt.close();
    }
    
    // VIOLATION: sp_executesql system procedure
    public void spExecuteSql(Connection connection) throws SQLException {
        CallableStatement stmt = connection.prepareCall("{call sp_executesql(?, ?, ?)}");
        stmt.setString(1, "SELECT * FROM users WHERE id = @id");
        stmt.setString(2, "@id int");
        stmt.setInt(3, 1001);
        ResultSet rs = stmt.executeQuery();
        rs.close();
        stmt.close();
    }
    
    // VIOLATION: xp_fileexist extended procedure
    public void xpFileExist(Connection conn) throws SQLException {
        CallableStatement cs = conn.prepareCall("{call xp_fileexist(?, ?)}");
        cs.setString(1, "C:\\temp\\file.txt");
        cs.registerOutParameter(2, Types.INTEGER);
        cs.execute();
        int exists = cs.getInt(2);
        cs.close();
    }
    
    // VIOLATION: xp_dirtree extended procedure
    public void xpDirTree(Connection connection) throws SQLException {
        CallableStatement cstmt = connection.prepareCall("{call xp_dirtree(?, ?, ?)}");
        cstmt.setString(1, "C:\\temp");
        cstmt.setInt(2, 1);
        cstmt.setInt(3, 1);
        ResultSet rs = cstmt.executeQuery();
        while (rs.next()) {
            System.out.println("Directory: " + rs.getString(1));
        }
        rs.close();
        cstmt.close();
    }
    
    // VIOLATION: sp_addlogin system procedure
    public void spAddLogin(Connection conn) throws SQLException {
        CallableStatement stmt = conn.prepareCall("{call sp_addlogin(?, ?, ?)}");
        stmt.setString(1, "newuser");
        stmt.setString(2, "password123");
        stmt.setString(3, "master");
        stmt.execute();
        stmt.close();
    }
    
    // VIOLATION: sp_grantdbaccess system procedure
    public void spGrantDbAccess(Connection connection) throws SQLException {
        CallableStatement cs = connection.prepareCall("{call sp_grantdbaccess(?, ?)}");
        cs.setString(1, "newuser");
        cs.setString(2, "dbuser");
        cs.execute();
        cs.close();
    }
    
    // VIOLATION: sp_rename system procedure
    public void spRename(Connection conn) throws SQLException {
        CallableStatement cstmt = conn.prepareCall("{call sp_rename(?, ?, ?)}");
        cstmt.setString(1, "old_table");
        cstmt.setString(2, "new_table");
        cstmt.setString(3, "OBJECT");
        cstmt.execute();
        cstmt.close();
    }
    
    // VIOLATION: sp_helpdb system procedure
    public void spHelpDb(Connection connection) throws SQLException {
        CallableStatement stmt = connection.prepareCall("{call sp_helpdb}");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            System.out.println("Database: " + rs.getString(1));
        }
        rs.close();
        stmt.close();
    }
    
    // VIOLATION: xp_regread extended procedure
    public void xpRegRead(Connection conn) throws SQLException {
        CallableStatement cs = conn.prepareCall("{call xp_regread(?, ?, ?, ?)}");
        cs.setString(1, "HKEY_LOCAL_MACHINE");
        cs.setString(2, "SOFTWARE\\Microsoft\\Windows\\CurrentVersion");
        cs.setString(3, "ProgramFilesDir");
        cs.registerOutParameter(4, Types.VARCHAR);
        cs.execute();
        String value = cs.getString(4);
        cs.close();
    }
    
    // VIOLATION: sp_configure system procedure
    public void spConfigure(Connection connection) throws SQLException {
        CallableStatement cstmt = connection.prepareCall("{call sp_configure(?, ?)}");
        cstmt.setString(1, "max server memory");
        cstmt.setInt(2, 2048);
        cstmt.execute();
        cstmt.close();
    }
    
    // VIOLATION: xp_logevent extended procedure
    public void xpLogEvent(Connection conn) throws SQLException {
        CallableStatement stmt = conn.prepareCall("{call xp_logevent(?, ?, ?)}");
        stmt.setInt(1, 50001);
        stmt.setString(2, "Custom error message");
        stmt.setString(3, "ERROR");
        stmt.execute();
        stmt.close();
    }
    
    // VIOLATION: sp_who2 system procedure
    public void spWho2(Connection connection) throws SQLException {
        CallableStatement cs = connection.prepareCall("{call sp_who2}");
        ResultSet rs = cs.executeQuery();
        while (rs.next()) {
            System.out.println("SPID: " + rs.getInt(1));
        }
        rs.close();
        cs.close();
    }
    
    // VIOLATION: xp_sendmail extended procedure
    public void xpSendMail(Connection conn) throws SQLException {
        CallableStatement cstmt = conn.prepareCall("{call xp_sendmail(?, ?, ?, ?)}");
        cstmt.setString(1, "admin@company.com");
        cstmt.setString(2, "Database Alert");
        cstmt.setString(3, "System is running normally");
        cstmt.setString(4, "text");
        cstmt.execute();
        cstmt.close();
    }
    
    // VIOLATION: sp_attach_db system procedure
    public void spAttachDb(Connection connection) throws SQLException {
        CallableStatement stmt = connection.prepareCall("{call sp_attach_db(?, ?, ?)}");
        stmt.setString(1, "MyDatabase");
        stmt.setString(2, "C:\\Data\\MyDatabase.mdf");
        stmt.setString(3, "C:\\Data\\MyDatabase.ldf");
        stmt.execute();
        stmt.close();
    }
    
    // VIOLATION: xp_cmdshell with parameters
    public void xpCmdShellWithParams(Connection conn) throws SQLException {
        CallableStatement cs = conn.prepareCall("{call xp_cmdshell(?, ?)}");
        cs.setString(1, "netstat -an");
        cs.setInt(2, 1);
        ResultSet rs = cs.executeQuery();
        while (rs.next()) {
            System.out.println("Network: " + rs.getString(1));
        }
        rs.close();
        cs.close();
    }
    
    // NON-VIOLATION: Regular stored procedure (not system)
    public void regularStoredProcedure(Connection connection) throws SQLException {
        CallableStatement cstmt = connection.prepareCall("{call GetUserById(?)}");
        cstmt.setInt(1, 1001);
        ResultSet rs = cstmt.executeQuery();
        rs.close();
        cstmt.close();
    }
    
    // NON-VIOLATION: User-defined function
    public void userDefinedFunction(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT dbo.CalculateAge(birth_date) FROM users");
        rs.close();
        stmt.close();
    }
    
    // NON-VIOLATION: Built-in SQL functions
    public void builtInFunctions(Connection connection) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT UPPER(name), GETDATE() FROM users");
        rs.close();
        stmt.close();
    }
    
    // NON-VIOLATION: Simple query
    public void simpleQuery(Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE id = ?");
        ps.setInt(1, 1001);
        ResultSet rs = ps.executeQuery();
        rs.close();
        ps.close();
    }
    
    // NON-VIOLATION: Regular DML operations
    public void regularDML(Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("INSERT INTO users (name) VALUES (?)");
        ps.setString(1, "John");
        ps.executeUpdate();
        ps.close();
    }
}

// Additional class with system function patterns
class SystemFunctionHelper {
    
    // VIOLATION: Dynamic system procedure call
    public void dynamicSystemProcedure(Connection conn, String procedureName, Object... params) 
            throws SQLException {
        StringBuilder sql = new StringBuilder("{call ").append(procedureName).append("(");
        for (int i = 0; i < params.length; i++) {
            sql.append(i > 0 ? ",?" : "?");
        }
        sql.append(")}");
        
        CallableStatement cs = conn.prepareCall(sql.toString());
        for (int i = 0; i < params.length; i++) {
            cs.setObject(i + 1, params[i]);
        }
        cs.execute();
        cs.close();
    }
    
    // VIOLATION: System procedure with error handling
    public void systemProcedureWithErrorHandling(Connection connection) {
        try {
            CallableStatement cs = connection.prepareCall("{call sp_configure(?, ?)}");
            cs.setString(1, "show advanced options");
            cs.setInt(2, 1);
            cs.execute();
            cs.close();
        } catch (SQLException e) {
            System.err.println("System procedure failed: " + e.getMessage());
        }
    }
}

