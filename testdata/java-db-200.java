// sql-java-200: StoredProcedureMetadataUsage
// Detects stored procedure metadata access
// This file tests detection of DatabaseMetaData usage for stored procedures

package com.example.jdbc;

import java.sql.*;

public class StoredProcedureMetadataExample {
    
    // VIOLATION: Getting stored procedure metadata
    public void getStoredProcedureMetadata(Connection conn) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet rs = metaData.getProcedures(null, null, null);
        while (rs.next()) {
            System.out.println("Procedure: " + rs.getString("PROCEDURE_NAME"));
            System.out.println("Type: " + rs.getString("PROCEDURE_TYPE"));
        }
        rs.close();
    }
    
    // VIOLATION: Getting procedure columns metadata
    public void getProcedureColumnsMetadata(Connection connection) throws SQLException {
        DatabaseMetaData dbMetaData = connection.getMetaData();
        ResultSet rs = dbMetaData.getProcedureColumns(null, null, "GetUserById", null);
        while (rs.next()) {
            System.out.println("Column: " + rs.getString("COLUMN_NAME"));
            System.out.println("Type: " + rs.getInt("DATA_TYPE"));
            System.out.println("Nullable: " + rs.getInt("NULLABLE"));
        }
        rs.close();
    }
    
    // VIOLATION: Getting procedures with specific catalog
    public void getProceduresByCatalog(Connection conn, String catalog) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet rs = metaData.getProcedures(catalog, null, null);
        while (rs.next()) {
            System.out.println("Catalog: " + rs.getString("PROCEDURE_CAT"));
            System.out.println("Schema: " + rs.getString("PROCEDURE_SCHEM"));
            System.out.println("Name: " + rs.getString("PROCEDURE_NAME"));
        }
        rs.close();
    }
    
    // VIOLATION: Getting procedures with schema pattern
    public void getProceduresBySchema(Connection connection, String schemaPattern) throws SQLException {
        DatabaseMetaData dbMetaData = connection.getMetaData();
        ResultSet rs = dbMetaData.getProcedures(null, schemaPattern, null);
        while (rs.next()) {
            System.out.println("Schema: " + rs.getString("PROCEDURE_SCHEM"));
            System.out.println("Procedure: " + rs.getString("PROCEDURE_NAME"));
        }
        rs.close();
    }
    
    // VIOLATION: Getting procedures with name pattern
    public void getProceduresByNamePattern(Connection conn, String namePattern) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet rs = metaData.getProcedures(null, null, namePattern);
        while (rs.next()) {
            System.out.println("Procedure: " + rs.getString("PROCEDURE_NAME"));
            System.out.println("Remarks: " + rs.getString("REMARKS"));
        }
        rs.close();
    }
    
    // VIOLATION: Getting procedure columns with all parameters
    public void getAllProcedureColumns(Connection connection) throws SQLException {
        DatabaseMetaData dbMetaData = connection.getMetaData();
        ResultSet rs = dbMetaData.getProcedureColumns(null, null, null, null);
        while (rs.next()) {
            System.out.println("Procedure: " + rs.getString("PROCEDURE_NAME"));
            System.out.println("Column: " + rs.getString("COLUMN_NAME"));
            System.out.println("Column Type: " + rs.getShort("COLUMN_TYPE"));
        }
        rs.close();
    }
    
    // VIOLATION: Getting procedure columns with specific types
    public void getProcedureColumnsByType(Connection conn, String procedureName) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet rs = metaData.getProcedureColumns(null, null, procedureName, null);
        while (rs.next()) {
            short columnType = rs.getShort("COLUMN_TYPE");
            if (columnType == DatabaseMetaData.procedureColumnIn) {
                System.out.println("Input parameter: " + rs.getString("COLUMN_NAME"));
            } else if (columnType == DatabaseMetaData.procedureColumnOut) {
                System.out.println("Output parameter: " + rs.getString("COLUMN_NAME"));
            } else if (columnType == DatabaseMetaData.procedureColumnInOut) {
                System.out.println("InOut parameter: " + rs.getString("COLUMN_NAME"));
            }
        }
        rs.close();
    }
    
    // VIOLATION: Checking procedure support
    public void checkProcedureSupport(Connection connection) throws SQLException {
        DatabaseMetaData dbMetaData = connection.getMetaData();
        boolean supportsStoredProcedures = dbMetaData.supportsStoredProcedures();
        System.out.println("Supports stored procedures: " + supportsStoredProcedures);
        
        if (supportsStoredProcedures) {
            ResultSet rs = dbMetaData.getProcedures(null, null, null);
            int count = 0;
            while (rs.next()) {
                count++;
            }
            System.out.println("Total procedures: " + count);
            rs.close();
        }
    }
    
    // VIOLATION: Getting procedure information with specific criteria
    public void getProcedureInfo(Connection conn, String catalog, String schema, String procedure) 
            throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet rs = metaData.getProcedures(catalog, schema, procedure);
        if (rs.next()) {
            System.out.println("Procedure Name: " + rs.getString("PROCEDURE_NAME"));
            System.out.println("Procedure Type: " + rs.getShort("PROCEDURE_TYPE"));
            System.out.println("Remarks: " + rs.getString("REMARKS"));
        }
        rs.close();
    }
    
    // VIOLATION: Getting procedure columns with data type information
    public void getProcedureColumnTypes(Connection connection, String procedureName) throws SQLException {
        DatabaseMetaData dbMetaData = connection.getMetaData();
        ResultSet rs = dbMetaData.getProcedureColumns(null, null, procedureName, null);
        while (rs.next()) {
            System.out.println("Column: " + rs.getString("COLUMN_NAME"));
            System.out.println("Data Type: " + rs.getInt("DATA_TYPE"));
            System.out.println("Type Name: " + rs.getString("TYPE_NAME"));
            System.out.println("Precision: " + rs.getInt("PRECISION"));
            System.out.println("Length: " + rs.getInt("LENGTH"));
            System.out.println("Scale: " + rs.getShort("SCALE"));
        }
        rs.close();
    }
    
    // VIOLATION: Iterating through all procedures
    public void iterateAllProcedures(Connection conn) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet rs = metaData.getProcedures(null, null, null);
        while (rs.next()) {
            String procedureName = rs.getString("PROCEDURE_NAME");
            System.out.println("Processing procedure: " + procedureName);
            
            // Get columns for this procedure
            ResultSet columns = metaData.getProcedureColumns(null, null, procedureName, null);
            while (columns.next()) {
                System.out.println("  Column: " + columns.getString("COLUMN_NAME"));
            }
            columns.close();
        }
        rs.close();
    }
    
    // VIOLATION: Getting procedure metadata with try-with-resources
    public void getProcedureMetadataWithResources(Connection connection) throws SQLException {
        DatabaseMetaData dbMetaData = connection.getMetaData();
        try (ResultSet rs = dbMetaData.getProcedures(null, null, null)) {
            while (rs.next()) {
                System.out.println("Procedure: " + rs.getString("PROCEDURE_NAME"));
            }
        }
    }
    
    // VIOLATION: Checking procedure column nullable information
    public void checkProcedureColumnNullable(Connection conn, String procedureName) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet rs = metaData.getProcedureColumns(null, null, procedureName, null);
        while (rs.next()) {
            int nullable = rs.getInt("NULLABLE");
            String columnName = rs.getString("COLUMN_NAME");
            if (nullable == DatabaseMetaData.columnNullable) {
                System.out.println("Column " + columnName + " is nullable");
            } else if (nullable == DatabaseMetaData.columnNoNulls) {
                System.out.println("Column " + columnName + " is not nullable");
            }
        }
        rs.close();
    }
    
    // VIOLATION: Getting procedure metadata with error handling
    public void getProcedureMetadataWithErrorHandling(Connection connection) {
        try {
            DatabaseMetaData dbMetaData = connection.getMetaData();
            ResultSet rs = dbMetaData.getProcedures(null, null, null);
            while (rs.next()) {
                System.out.println("Procedure: " + rs.getString("PROCEDURE_NAME"));
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("Failed to get procedure metadata: " + e.getMessage());
        }
    }
    
    // VIOLATION: Getting procedure columns with specific column name pattern
    public void getProcedureColumnsByNamePattern(Connection conn, String procedureName, String columnPattern) 
            throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet rs = metaData.getProcedureColumns(null, null, procedureName, columnPattern);
        while (rs.next()) {
            System.out.println("Column: " + rs.getString("COLUMN_NAME"));
            System.out.println("Position: " + rs.getInt("ORDINAL_POSITION"));
        }
        rs.close();
    }
    
    // NON-VIOLATION: Getting table metadata (not procedure)
    public void getTableMetadata(Connection connection) throws SQLException {
        DatabaseMetaData dbMetaData = connection.getMetaData();
        ResultSet rs = dbMetaData.getTables(null, null, null, null);
        while (rs.next()) {
            System.out.println("Table: " + rs.getString("TABLE_NAME"));
        }
        rs.close();
    }
    
    // NON-VIOLATION: Getting column metadata (not procedure)
    public void getColumnMetadata(Connection conn, String tableName) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet rs = metaData.getColumns(null, null, tableName, null);
        while (rs.next()) {
            System.out.println("Column: " + rs.getString("COLUMN_NAME"));
        }
        rs.close();
    }
    
    // NON-VIOLATION: Getting index metadata
    public void getIndexMetadata(Connection connection, String tableName) throws SQLException {
        DatabaseMetaData dbMetaData = connection.getMetaData();
        ResultSet rs = dbMetaData.getIndexInfo(null, null, tableName, false, false);
        while (rs.next()) {
            System.out.println("Index: " + rs.getString("INDEX_NAME"));
        }
        rs.close();
    }
    
    // NON-VIOLATION: Getting primary key metadata
    public void getPrimaryKeyMetadata(Connection conn, String tableName) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet rs = metaData.getPrimaryKeys(null, null, tableName);
        while (rs.next()) {
            System.out.println("PK Column: " + rs.getString("COLUMN_NAME"));
        }
        rs.close();
    }
    
    // NON-VIOLATION: Getting foreign key metadata
    public void getForeignKeyMetadata(Connection connection, String tableName) throws SQLException {
        DatabaseMetaData dbMetaData = connection.getMetaData();
        ResultSet rs = dbMetaData.getImportedKeys(null, null, tableName);
        while (rs.next()) {
            System.out.println("FK: " + rs.getString("FKCOLUMN_NAME"));
        }
        rs.close();
    }
    
    // NON-VIOLATION: Simple query execution
    public void executeSimpleQuery(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM users");
        rs.close();
        stmt.close();
    }
}

// Additional class with procedure metadata patterns
class ProcedureMetadataHelper {
    
    // VIOLATION: Generic procedure metadata getter
    public void getProcedureMetadata(Connection conn, String catalog, String schema, String procedure) 
            throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet rs = metaData.getProcedures(catalog, schema, procedure);
        processProcedureMetadata(rs);
        rs.close();
    }
    
    // VIOLATION: Generic procedure columns getter
    public void getProcedureColumns(Connection connection, String procedureName) throws SQLException {
        DatabaseMetaData dbMetaData = connection.getMetaData();
        ResultSet rs = dbMetaData.getProcedureColumns(null, null, procedureName, null);
        processProcedureColumns(rs);
        rs.close();
    }
    
    private void processProcedureMetadata(ResultSet rs) throws SQLException {
        while (rs.next()) {
            System.out.println("Procedure: " + rs.getString("PROCEDURE_NAME"));
        }
    }
    
    private void processProcedureColumns(ResultSet rs) throws SQLException {
        while (rs.next()) {
            System.out.println("Column: " + rs.getString("COLUMN_NAME"));
        }
    }
}

