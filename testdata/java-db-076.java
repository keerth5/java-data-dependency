// @testdata/MyBatisAnnotationUsageTest.java
package com.example.test;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import java.util.List;

/**
 * Sample mapper interface to test rule sql-java-076: MyBatisAnnotationUsage
 */
@Mapper
public interface MyBatisAnnotationUsageTest {

    // Violation: using @Select annotation inline
    @Select("SELECT id, name FROM user WHERE id = #{id}")
    User findUserById(int id);

    // Violation: using @Insert annotation inline
    @Insert("INSERT INTO user(name, email) VALUES (#{name}, #{email})")
    int insertUser(User user);

    // Violation: using @Update annotation inline
    @Update("UPDATE user SET name = #{name} WHERE id = #{id}")
    int updateUserName(int id, String name);

    // Non-violation: no MyBatis annotation here
    List<User> findAllUsers();

    // Non-violation: method in a class without annotations
    default void someUtilityMethod() {
        System.out.println("This is not a MyBatis call.");
    }

    // Violation: another MyBatis annotation usage
    @Select("DELETE FROM user WHERE id = #{id}")
    int deleteUserById(int id);
}
