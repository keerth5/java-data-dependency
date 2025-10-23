// sql-java-188: JpaEntityManagerFactoryBean
// Detects Spring LocalContainerEntityManagerFactoryBean usage
// This file tests detection of Spring JPA EntityManagerFactory configuration

package com.example.spring.jpa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import javax.sql.DataSource;
import javax.persistence.EntityManagerFactory;
import java.util.Properties;

// VIOLATION: Configuration class with LocalContainerEntityManagerFactoryBean
@Configuration
public class JpaConfig {
    
    // VIOLATION: Creating LocalContainerEntityManagerFactoryBean
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("com.example.domain");
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return emf;
    }
    
    // VIOLATION: LocalContainerEntityManagerFactoryBean with properties
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryWithProps(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setPackagesToScan("com.example.entities");
        
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setShowSql(true);
        adapter.setGenerateDdl(true);
        adapter.setDatabasePlatform("org.hibernate.dialect.MySQL8Dialect");
        factory.setJpaVendorAdapter(adapter);
        
        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.hbm2ddl.auto", "update");
        jpaProperties.put("hibernate.show_sql", "true");
        factory.setJpaProperties(jpaProperties);
        
        return factory;
    }
}

// VIOLATION: Another configuration with LocalContainerEntityManagerFactoryBean
@Configuration
public class MultiDatasourceConfig {
    
    // VIOLATION: Primary EntityManagerFactory
    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory(
            @Qualifier("primaryDataSource") DataSource dataSource) {
        
        LocalContainerEntityManagerFactoryBean emfBean = new LocalContainerEntityManagerFactoryBean();
        emfBean.setDataSource(dataSource);
        emfBean.setPackagesToScan("com.example.primary.domain");
        emfBean.setPersistenceUnitName("primaryPU");
        
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        emfBean.setJpaVendorAdapter(vendorAdapter);
        
        return emfBean;
    }
    
    // VIOLATION: Secondary EntityManagerFactory
    @Bean
    public LocalContainerEntityManagerFactoryBean secondaryEntityManagerFactory(
            @Qualifier("secondaryDataSource") DataSource dataSource) {
        
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("com.example.secondary.domain");
        emf.setPersistenceUnitName("secondaryPU");
        
        return emf;
    }
}

// VIOLATION: Using EclipseLink vendor adapter
@Configuration
public class EclipseLinkConfig {
    
    @Bean
    public LocalContainerEntityManagerFactoryBean eclipseLinkEntityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("com.example.model");
        
        EclipseLinkJpaVendorAdapter vendorAdapter = new EclipseLinkJpaVendorAdapter();
        vendorAdapter.setShowSql(true);
        emf.setJpaVendorAdapter(vendorAdapter);
        
        return emf;
    }
}

// VIOLATION: LocalContainerEntityManagerFactoryBean with custom properties
@Configuration
public class CustomJpaConfig {
    
    @Bean
    public LocalContainerEntityManagerFactoryBean customEntityManagerFactory(DataSource ds) {
        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setDataSource(ds);
        bean.setPackagesToScan("com.example.entity");
        bean.setJpaVendorAdapter(jpaVendorAdapter());
        bean.setJpaProperties(hibernateProperties());
        bean.setPersistenceUnitName("customPU");
        bean.setPersistenceXmlLocation("classpath:META-INF/custom-persistence.xml");
        return bean;
    }
    
    private HibernateJpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setShowSql(true);
        adapter.setGenerateDdl(false);
        return adapter;
    }
    
    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL95Dialect");
        properties.setProperty("hibernate.cache.use_second_level_cache", "true");
        return properties;
    }
}

// VIOLATION: LocalContainerEntityManagerFactoryBean in XML-style config
public class XmlStyleConfig {
    
    public void configureEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(createDataSource());
        emf.setPackagesToScan("com.example.domain");
        emf.afterPropertiesSet();
    }
    
    private DataSource createDataSource() {
        return null;
    }
}

// VIOLATION: Using LocalEntityManagerFactoryBean (simpler version)
@Configuration
public class SimpleJpaConfig {
    
    @Bean
    public LocalEntityManagerFactoryBean entityManagerFactory() {
        LocalEntityManagerFactoryBean emf = new LocalEntityManagerFactoryBean();
        emf.setPersistenceUnitName("myPersistenceUnit");
        return emf;
    }
}

// VIOLATION: Transaction manager with LocalContainerEntityManagerFactoryBean
@Configuration
public class TransactionConfig {
    
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("com.example.domain");
        return emf;
    }
    
    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}

// VIOLATION: Programmatic creation
public class ProgrammaticJpaSetup {
    
    public EntityManagerFactory setupEntityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean emfBean = new LocalContainerEntityManagerFactoryBean();
        emfBean.setDataSource(dataSource);
        emfBean.setPackagesToScan("com.example.model");
        emfBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        emfBean.afterPropertiesSet();
        return emfBean.getObject();
    }
}

// NON-VIOLATION: Standard JPA without Spring
public class StandardJpaConfig {
    
    public EntityManagerFactory createEntityManagerFactory() {
        return javax.persistence.Persistence.createEntityManagerFactory("myPU");
    }
}

// NON-VIOLATION: Using EntityManagerFactory directly without LocalContainer
@Configuration
public class DirectEmfConfig {
    
    @Bean
    public EntityManagerFactory entityManagerFactory() {
        return javax.persistence.Persistence.createEntityManagerFactory("directPU");
    }
}

// NON-VIOLATION: Plain JDBC configuration
@Configuration
public class JdbcConfig {
    
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}

// NON-VIOLATION: MyBatis configuration
@Configuration
public class MyBatisConfig {
    
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        return factory.getObject();
    }
}

// Helper classes
class JdbcTemplate { JdbcTemplate(DataSource ds) { } }
class SqlSessionFactory { }
class SqlSessionFactoryBean { 
    void setDataSource(DataSource ds) { }
    SqlSessionFactory getObject() { return null; }
}
@interface Primary { }
@interface Qualifier { String value(); }

