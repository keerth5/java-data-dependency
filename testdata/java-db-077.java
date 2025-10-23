// @testdata/MyBatisXmlMappingUsageTest.java
package com.example.test;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import java.util.List;

/**
 * Sample code to test rule sql-java-077: MyBatisXmlMappingUsage
 */
public class MyBatisXmlMappingUsageTest {

    private final SqlSessionFactory sessionFactory;

    public MyBatisXmlMappingUsageTest(SqlSessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // Non-violation: method uses inline SQL via annotation (or manual call) rather than external XML mapping
    public User findUserInline(int id) {
        try (SqlSession session = sessionFactory.openSession()) {
            return session.selectOne("com.example.test.MyMapper.findUserInline", id);
        }
    }

    // Violation: method uses an interface that is bound to an external XML mapper file (MyMapper.xml)
    public User findUserByXml(int id) {
        try (SqlSession session = sessionFactory.openSession()) {
            MyMapper mapper = session.getMapper(MyMapper.class);
            // The method below is defined in the interface MyMapper and SQL is in MyMapper.xml
            return mapper.selectUserById(id);
        }
    }

    // Non-violation: simple JDBC style or manual SQL inside code
    public int updateStatusInline(int id, String status) {
        try (SqlSession session = sessionFactory.openSession()) {
            return session.update("com.example.test.MyMapper.updateStatusInline", status);
        }
    }

    // Violation: uses external XML mapping for deletion
    public int deleteUserByXml(int id) {
        try (SqlSession session = sessionFactory.openSession()) {
            MyMapper mapper = session.getMapper(MyMapper.class);
            // SQL for this method is defined in MyMapper.xml
            return mapper.deleteUser(id);
        }
    }

    // Interface for illustration (in same file for brevity)
    public interface MyMapper {
        User selectUserById(int id);
        int deleteUser(int id);
        // This method uses annotation rather than XML
        @org.apache.ibatis.annotations.Select("SELECT * FROM user WHERE id = #{id}")
        User selectUserInline(int id);
    }

    // Example User class stub
    public static class User {
        private int id;
        private String name;
        // getters/setters omitted for brevity
    }
}
