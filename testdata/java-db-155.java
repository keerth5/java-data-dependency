// @testdata/CustomTypeHandlerUsageTest.java
package com.example.test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

/**
 * Sample code to test rule sql‑java‑155: CustomTypeHandlerUsage
 */
public class CustomTypeHandlerUsageTest {

    // Violation: defining a custom TypeHandler
    @MappedTypes(CustomEnum.class)
    public static class CustomEnumTypeHandler implements TypeHandler<CustomEnum> {

        @Override
        public void setParameter(PreparedStatement ps, int i, CustomEnum parameter, JdbcType jdbcType)
                throws SQLException {
            ps.setString(i, parameter.name());
        }

        @Override
        public CustomEnum getResult(ResultSet rs, String columnName) throws SQLException {
            String value = rs.getString(columnName);
            return value == null ? null : CustomEnum.valueOf(value);
        }

        @Override
        public CustomEnum getResult(ResultSet rs, int columnIndex) throws SQLException {
            String value = rs.getString(columnIndex);
            return value == null ? null : CustomEnum.valueOf(value);
        }

        @Override
        public CustomEnum getResult(java.sql.CallableStatement cs, int columnIndex) throws SQLException {
            String value = cs.getString(columnIndex);
            return value == null ? null : CustomEnum.valueOf(value);
        }
    }

    public enum CustomEnum {
        ONE, TWO, THREE
    }

    // Non-violation: using default type mapping
    public void insertWithDefaultMapping(PreparedStatement ps, int id, String name) throws SQLException {
        ps.setInt(1, id);       // default type mapping
        ps.setString(2, name);  // default type mapping
        ps.executeUpdate();
        System.out.println("Inserted record using default type mapping (non-violation).");
    }

    // Violation: registering and using the custom type handler
    public void useCustomTypeHandler() {
        CustomEnum value = CustomEnum.TWO;
        // Simulate using the custom handler
        CustomEnumTypeHandler handler = new CustomEnumTypeHandler();
        try {
            PreparedStatement ps = null; // for demonstration only
            handler.setParameter(ps, 1, value, null); // Violation: custom type handler usage
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Used custom type handler (violation).");
    }
}
