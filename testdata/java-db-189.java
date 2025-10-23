// sql-java-189: HibernateSessionFactoryBean
// Detects Spring LocalSessionFactoryBean usage
// This file tests detection of Spring Hibernate SessionFactory configuration

package com.example.spring.hibernate;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.hibernate.SessionFactory;
import javax.sql.DataSource;
import java.util.Properties;

// VIOLATION: Configuration with LocalSessionFactoryBean
@Configuration
public class HibernateConfig {
    
    // VIOLATION: Creating LocalSessionFactoryBean (Hibernate 5)
    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan("com.example.model");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }
    
    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.hbm2ddl.auto", "update");
        return properties;
    }
}

// VIOLATION: LocalSessionFactoryBean with mapping resources
@Configuration
public class HibernateMappingConfig {
    
    @Bean
    public LocalSessionFactoryBean hibernateSessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean factory = new LocalSessionFactoryBean();
        factory.setDataSource(dataSource);
        factory.setMappingResources("hibernate/User.hbm.xml", "hibernate/Order.hbm.xml");
        
        Properties props = new Properties();
        props.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL95Dialect");
        props.setProperty("hibernate.cache.use_second_level_cache", "true");
        props.setProperty("hibernate.cache.region.factory_class", 
                         "org.hibernate.cache.ehcache.EhCacheRegionFactory");
        factory.setHibernateProperties(props);
        
        return factory;
    }
}

// VIOLATION: Multiple SessionFactory configuration
@Configuration
public class MultipleSessionFactoryConfig {
    
    // VIOLATION: Primary SessionFactory
    @Bean
    @Primary
    public LocalSessionFactoryBean primarySessionFactory(
            @Qualifier("primaryDataSource") DataSource dataSource) {
        
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan("com.example.primary.entity");
        sessionFactory.setHibernateProperties(primaryHibernateProperties());
        return sessionFactory;
    }
    
    // VIOLATION: Secondary SessionFactory
    @Bean
    public LocalSessionFactoryBean secondarySessionFactory(
            @Qualifier("secondaryDataSource") DataSource dataSource) {
        
        LocalSessionFactoryBean factory = new LocalSessionFactoryBean();
        factory.setDataSource(dataSource);
        factory.setPackagesToScan("com.example.secondary.entity");
        factory.setHibernateProperties(secondaryHibernateProperties());
        return factory;
    }
    
    private Properties primaryHibernateProperties() {
        Properties props = new Properties();
        props.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        return props;
    }
    
    private Properties secondaryHibernateProperties() {
        Properties props = new Properties();
        props.put("hibernate.dialect", "org.hibernate.dialect.OracleDialect");
        return props;
    }
}

// VIOLATION: LocalSessionFactoryBean with annotated classes
@Configuration
public class AnnotationBasedConfig {
    
    @Bean
    public LocalSessionFactoryBean sessionFactoryWithAnnotations(DataSource dataSource) {
        LocalSessionFactoryBean bean = new LocalSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setAnnotatedClasses(User.class, Order.class, Product.class);
        bean.setHibernateProperties(getHibernateProperties());
        return bean;
    }
    
    private Properties getHibernateProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        properties.setProperty("hibernate.format_sql", "true");
        return properties;
    }
}

// VIOLATION: LocalSessionFactoryBean with transaction manager
@Configuration
public class HibernateTransactionConfig {
    
    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean factory = new LocalSessionFactoryBean();
        factory.setDataSource(dataSource);
        factory.setPackagesToScan("com.example.domain");
        return factory;
    }
    
    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        return new HibernateTransactionManager(sessionFactory);
    }
}

// VIOLATION: Hibernate 4 LocalSessionFactoryBean
@Configuration
public class Hibernate4Config {
    
    @Bean
    public org.springframework.orm.hibernate4.LocalSessionFactoryBean hibernate4SessionFactory(DataSource ds) {
        org.springframework.orm.hibernate4.LocalSessionFactoryBean sessionFactory = 
            new org.springframework.orm.hibernate4.LocalSessionFactoryBean();
        sessionFactory.setDataSource(ds);
        sessionFactory.setPackagesToScan("com.example.legacy.model");
        return sessionFactory;
    }
}

// VIOLATION: LocalSessionFactoryBean with configuration locations
@Configuration
public class XmlBasedHibernateConfig {
    
    @Bean
    public LocalSessionFactoryBean xmlSessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean factory = new LocalSessionFactoryBean();
        factory.setDataSource(dataSource);
        factory.setConfigLocation(new ClassPathResource("hibernate.cfg.xml"));
        factory.setMappingLocations(
            new ClassPathResource("mappings/User.hbm.xml"),
            new ClassPathResource("mappings/Product.hbm.xml")
        );
        return factory;
    }
}

// VIOLATION: Programmatic SessionFactory creation
public class ProgrammaticHibernateSetup {
    
    public SessionFactory createSessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setPackagesToScan("com.example.entity");
        
        Properties props = new Properties();
        props.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        factoryBean.setHibernateProperties(props);
        
        try {
            factoryBean.afterPropertiesSet();
            return factoryBean.getObject();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create SessionFactory", e);
        }
    }
}

// VIOLATION: Custom configuration method
public class CustomHibernateConfig {
    
    public LocalSessionFactoryBean configureHibernate(DataSource ds, String[] packagesToScan) {
        LocalSessionFactoryBean bean = new LocalSessionFactoryBean();
        bean.setDataSource(ds);
        bean.setPackagesToScan(packagesToScan);
        bean.setHibernateProperties(buildProperties());
        return bean;
    }
    
    private Properties buildProperties() {
        Properties p = new Properties();
        p.setProperty("hibernate.show_sql", "true");
        return p;
    }
}

// NON-VIOLATION: Standard Hibernate Configuration without Spring
public class StandardHibernateConfig {
    
    public SessionFactory createSessionFactory() {
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
        configuration.configure("hibernate.cfg.xml");
        return configuration.buildSessionFactory();
    }
}

// NON-VIOLATION: Spring JPA EntityManagerFactory (not Hibernate SessionFactory)
@Configuration
public class JpaConfig {
    
    @Bean
    public org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource ds) {
        org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean emf = 
            new org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(ds);
        return emf;
    }
}

// NON-VIOLATION: JDBC Template
@Configuration
public class JdbcConfiguration {
    
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}

// NON-VIOLATION: MyBatis SqlSessionFactory
@Configuration
public class MyBatisConfiguration {
    
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        return factory.getObject();
    }
}

// Helper classes
class User { }
class Order { }
class Product { }
class ClassPathResource { ClassPathResource(String path) { } }
class JdbcTemplate { JdbcTemplate(DataSource ds) { } }
class SqlSessionFactoryBean {
    void setDataSource(DataSource ds) { }
    SqlSessionFactory getObject() { return null; }
}
interface SqlSessionFactory { }
@interface Primary { }
@interface Qualifier { String value(); }

