package com.example.database;

import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import javax.persistence.EntityManagerFactory;
import java.util.Properties;

/**
 * Test file for sql-java-058: HibernateSessionFactoryUsage
 * Detects Hibernate SessionFactory usage
 * SessionFactory affects Hibernate configuration complexity
 */
public class HibernateSessionFactoryExample {
    
    // Direct field declaration with SessionFactory
    private SessionFactory sessionFactory;
    
    private static SessionFactory staticSessionFactory;
    
    // Constructor with SessionFactory initialization
    public HibernateSessionFactoryExample() {
        this.sessionFactory = new Configuration().configure().buildSessionFactory();
    }
    
    // Constructor with custom configuration
    public HibernateSessionFactoryExample(String configFile) {
        Configuration configuration = new Configuration();
        configuration.configure(configFile);
        this.sessionFactory = configuration.buildSessionFactory();
    }
    
    // Setter for SessionFactory
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    // Getter returning SessionFactory
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    // Building SessionFactory with Configuration
    public void buildSessionFactoryWithConfiguration() {
        // Basic Configuration approach
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        configuration.addAnnotatedClass(Employee.class);
        configuration.addAnnotatedClass(Department.class);
        SessionFactory factory = configuration.buildSessionFactory();
        
        // Configuration with properties
        Configuration config = new Configuration();
        Properties properties = new Properties();
        properties.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        properties.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/testdb");
        properties.setProperty("hibernate.connection.username", "root");
        properties.setProperty("hibernate.connection.password", "password");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        config.setProperties(properties);
        SessionFactory factoryWithProps = config.buildSessionFactory();
        
        // Configuration with programmatic setup
        Configuration programmaticConfig = new Configuration();
        programmaticConfig.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        programmaticConfig.setProperty("hibernate.show_sql", "true");
        programmaticConfig.addAnnotatedClass(Employee.class);
        SessionFactory programmaticFactory = programmaticConfig.buildSessionFactory();
    }
    
    // Building SessionFactory with ServiceRegistry
    public void buildSessionFactoryWithServiceRegistry() {
        // Using StandardServiceRegistryBuilder
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure()
            .build();
        
        try {
            SessionFactory factory = new MetadataSources(registry)
                .addAnnotatedClass(Employee.class)
                .addAnnotatedClass(Department.class)
                .buildMetadata()
                .buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
        
        // Using custom properties
        StandardServiceRegistry customRegistry = new StandardServiceRegistryBuilder()
            .configure("custom-hibernate.cfg.xml")
            .build();
        
        Metadata metadata = new MetadataSources(customRegistry)
            .addAnnotatedClass(Employee.class)
            .getMetadataBuilder()
            .build();
        
        SessionFactory customFactory = metadata.getSessionFactoryBuilder().build();
    }
    
    // Singleton pattern with SessionFactory
    public static class SessionFactorySingleton {
        private static SessionFactory sessionFactory;
        
        private SessionFactorySingleton() {}
        
        public static SessionFactory getSessionFactory() {
            if (sessionFactory == null) {
                synchronized (SessionFactorySingleton.class) {
                    if (sessionFactory == null) {
                        Configuration configuration = new Configuration();
                        configuration.configure();
                        sessionFactory = configuration.buildSessionFactory();
                    }
                }
            }
            return sessionFactory;
        }
        
        public static void shutdown() {
            if (sessionFactory != null) {
                sessionFactory.close();
            }
        }
    }
    
    // SessionFactory operations
    public void sessionFactoryOperations() {
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        
        // Open new session
        Session session = factory.openSession();
        
        // Get current session
        Session currentSession = factory.getCurrentSession();
        
        // Open stateless session
        org.hibernate.StatelessSession statelessSession = factory.openStatelessSession();
        
        // Get metadata
        org.hibernate.metadata.ClassMetadata classMetadata = 
            factory.getClassMetadata(Employee.class);
        
        org.hibernate.metadata.CollectionMetadata collectionMetadata = 
            factory.getCollectionMetadata("Employee.departments");
        
        // Get all class metadata
        java.util.Map<String, org.hibernate.metadata.ClassMetadata> allMetadata = 
            factory.getAllClassMetadata();
        
        // Get all collection metadata
        java.util.Map<String, org.hibernate.metadata.CollectionMetadata> allCollectionMetadata = 
            factory.getAllCollectionMetadata();
        
        // Statistics
        org.hibernate.stat.Statistics statistics = factory.getStatistics();
        statistics.setStatisticsEnabled(true);
        
        // Check if closed
        boolean isClosed = factory.isClosed();
        
        // Close factory
        factory.close();
    }
    
    // SessionFactory with cache operations
    public void cacheOperations() {
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        
        // Get cache
        org.hibernate.Cache cache = factory.getCache();
        
        // Evict entity from second level cache
        cache.evictEntityRegion(Employee.class);
        
        // Evict collection from cache
        cache.evictCollectionRegion("Employee.departments");
        
        // Evict all regions
        cache.evictAllRegions();
        
        // Check if entity is in cache
        boolean containsEntity = cache.containsEntity(Employee.class, 1);
        
        factory.close();
    }
    
    // SessionFactory with filters
    public void filterOperations() {
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        
        // Get filter definition
        org.hibernate.engine.spi.FilterDefinition filterDef = 
            factory.getFilterDefinition("activeEmployees");
        
        // Get all filter definitions
        java.util.Map<String, org.hibernate.engine.spi.FilterDefinition> allFilters = 
            factory.getDefinedFilterNames()
                .stream()
                .collect(java.util.stream.Collectors.toMap(
                    name -> name,
                    name -> factory.getFilterDefinition(name)
                ));
        
        factory.close();
    }
    
    // Method accepting SessionFactory as parameter
    public void processWithSessionFactory(SessionFactory factory) {
        Session session = factory.openSession();
        session.beginTransaction();
        // Perform operations
        session.getTransaction().commit();
        session.close();
    }
    
    // Method returning SessionFactory
    public SessionFactory createSessionFactory() {
        return new Configuration().configure().buildSessionFactory();
    }
    
    // Method returning SessionFactory with custom config
    public SessionFactory createCustomSessionFactory(Properties props) {
        Configuration config = new Configuration();
        config.setProperties(props);
        return config.buildSessionFactory();
    }
    
    // Static method with SessionFactory
    public static SessionFactory buildStaticSessionFactory() {
        if (staticSessionFactory == null) {
            staticSessionFactory = new Configuration()
                .configure()
                .buildSessionFactory();
        }
        return staticSessionFactory;
    }
    
    // SessionFactory builder pattern
    public static class SessionFactoryBuilder {
        private Configuration configuration;
        private Properties properties;
        
        public SessionFactoryBuilder() {
            this.configuration = new Configuration();
            this.properties = new Properties();
        }
        
        public SessionFactoryBuilder withConfigFile(String configFile) {
            configuration.configure(configFile);
            return this;
        }
        
        public SessionFactoryBuilder withProperty(String key, String value) {
            properties.setProperty(key, value);
            return this;
        }
        
        public SessionFactoryBuilder withAnnotatedClass(Class<?> clazz) {
            configuration.addAnnotatedClass(clazz);
            return this;
        }
        
        public SessionFactory build() {
            configuration.setProperties(properties);
            return configuration.buildSessionFactory();
        }
    }
    
    // Inner class demonstrating SessionFactory usage
    class HibernateUtil {
        private SessionFactory factory;
        
        public HibernateUtil() {
            try {
                factory = new Configuration()
                    .configure()
                    .addAnnotatedClass(Employee.class)
                    .buildSessionFactory();
            } catch (Throwable ex) {
                throw new ExceptionInInitializerError(ex);
            }
        }
        
        public SessionFactory getFactory() {
            return factory;
        }
        
        public Session openSession() {
            return factory.openSession();
        }
        
        public void shutdown() {
            if (factory != null && !factory.isClosed()) {
                factory.close();
            }
        }
    }
    
    // SessionFactory wrapper class
    static class SessionFactoryWrapper {
        private final SessionFactory delegate;
        
        public SessionFactoryWrapper(SessionFactory delegate) {
            this.delegate = delegate;
        }
        
        public SessionFactory getDelegate() {
            return delegate;
        }
        
        public Session openSession() {
            return delegate.openSession();
        }
        
        public void close() {
            delegate.close();
        }
    }
    
    // SessionFactory provider interface
    interface SessionFactoryProvider {
        SessionFactory getSessionFactory();
    }
    
    // SessionFactory provider implementation
    static class DefaultSessionFactoryProvider implements SessionFactoryProvider {
        private SessionFactory sessionFactory;
        
        public DefaultSessionFactoryProvider() {
            this.sessionFactory = new Configuration()
                .configure()
                .buildSessionFactory();
        }
        
        @Override
        public SessionFactory getSessionFactory() {
            return sessionFactory;
        }
    }
    
    // SessionFactory registry
    static class SessionFactoryRegistry {
        private java.util.Map<String, SessionFactory> factories = new java.util.HashMap<>();
        
        public void register(String name, SessionFactory factory) {
            factories.put(name, factory);
        }
        
        public SessionFactory get(String name) {
            return factories.get(name);
        }
        
        public void closeAll() {
            factories.values().forEach(SessionFactory::close);
            factories.clear();
        }
    }
    
    // Employee entity class
    static class Employee {
        private int id;
        private String name;
        private int age;
        private String department;
        
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }
        public String getDepartment() { return department; }
        public void setDepartment(String department) { this.department = department; }
    }
    
    // Department entity class
    static class Department {
        private int id;
        private String name;
        
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
}

