package com.example.database;

import org.hibernate.cfg.Configuration;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.Collection;
import org.hibernate.cfg.Mappings;
import org.hibernate.cfg.Settings;
import java.util.Properties;
import java.io.File;
import java.net.URL;

/**
 * Test file for sql-java-059: HibernateConfigurationUsage
 * Detects Hibernate Configuration class usage
 * Hibernate configuration affects ORM setup complexity
 */
public class HibernateConfigurationExample {
    
    // Field declaration with Configuration
    private Configuration configuration;
    
    private static Configuration staticConfiguration;
    
    // Constructor with Configuration initialization
    public HibernateConfigurationExample() {
        this.configuration = new Configuration();
        this.configuration.configure();
    }
    
    // Constructor with custom config file
    public HibernateConfigurationExample(String configFile) {
        this.configuration = new Configuration();
        this.configuration.configure(configFile);
    }
    
    // Setter for Configuration
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
    
    // Getter returning Configuration
    public Configuration getConfiguration() {
        return configuration;
    }
    
    // Basic Configuration usage
    public void basicConfigurationUsage() {
        // Create new Configuration
        Configuration config = new Configuration();
        
        // Configure from default hibernate.cfg.xml
        config.configure();
        
        // Configure from custom file
        Configuration customConfig = new Configuration();
        customConfig.configure("custom-hibernate.cfg.xml");
        
        // Configure from URL
        Configuration urlConfig = new Configuration();
        try {
            URL configUrl = new URL("file:///path/to/hibernate.cfg.xml");
            urlConfig.configure(configUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Configure from File
        Configuration fileConfig = new Configuration();
        File configFile = new File("hibernate.cfg.xml");
        fileConfig.configure(configFile);
    }
    
    // Configuration with properties
    public void configurationWithProperties() {
        Configuration config = new Configuration();
        
        // Set individual properties
        config.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        config.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/testdb");
        config.setProperty("hibernate.connection.username", "root");
        config.setProperty("hibernate.connection.password", "password");
        config.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        config.setProperty("hibernate.show_sql", "true");
        config.setProperty("hibernate.format_sql", "true");
        config.setProperty("hibernate.hbm2ddl.auto", "update");
        config.setProperty("hibernate.current_session_context_class", "thread");
        
        // Set Properties object
        Properties props = new Properties();
        props.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        props.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/testdb");
        props.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        props.setProperty("hibernate.c3p0.min_size", "5");
        props.setProperty("hibernate.c3p0.max_size", "20");
        props.setProperty("hibernate.c3p0.timeout", "300");
        
        Configuration configWithProps = new Configuration();
        configWithProps.setProperties(props);
        
        // Add properties
        Configuration addPropsConfig = new Configuration();
        addPropsConfig.addProperties(props);
        
        // Get property
        String dialect = config.getProperty("hibernate.dialect");
        
        // Get all properties
        Properties allProps = config.getProperties();
    }
    
    // Configuration with entity mappings
    public void configurationWithMappings() {
        Configuration config = new Configuration();
        
        // Add annotated class
        config.addAnnotatedClass(Employee.class);
        config.addAnnotatedClass(Department.class);
        config.addAnnotatedClass(Project.class);
        
        // Add package (for package-info.java annotations)
        config.addPackage("com.example.entities");
        
        // Add resource (XML mapping file)
        config.addResource("Employee.hbm.xml");
        config.addResource("Department.hbm.xml");
        
        // Add file
        File mappingFile = new File("mappings/Project.hbm.xml");
        config.addFile(mappingFile);
        
        // Add URL
        try {
            URL mappingUrl = new URL("file:///path/to/mapping.hbm.xml");
            config.addURL(mappingUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Add XML string
        String xmlMapping = "<hibernate-mapping>" +
            "<class name=\"com.example.Employee\" table=\"employees\">" +
            "<id name=\"id\" column=\"id\"/>" +
            "<property name=\"name\" column=\"name\"/>" +
            "</class>" +
            "</hibernate-mapping>";
        config.addXML(xmlMapping);
        
        // Add JAR file
        File jarFile = new File("lib/entities.jar");
        config.addJar(jarFile);
        
        // Add directory
        File mappingDir = new File("mappings/");
        config.addDirectory(mappingDir);
    }
    
    // Configuration with naming strategies
    public void configurationWithNamingStrategies() {
        Configuration config = new Configuration();
        
        // Set implicit naming strategy
        config.setImplicitNamingStrategy(
            org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl.INSTANCE
        );
        
        // Set physical naming strategy
        config.setPhysicalNamingStrategy(
            org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl.INSTANCE
        );
        
        // Get naming strategies
        org.hibernate.boot.model.naming.ImplicitNamingStrategy implicitStrategy = 
            config.getImplicitNamingStrategy();
        
        org.hibernate.boot.model.naming.PhysicalNamingStrategy physicalStrategy = 
            config.getPhysicalNamingStrategy();
    }
    
    // Configuration with interceptors and listeners
    public void configurationWithInterceptorsAndListeners() {
        Configuration config = new Configuration();
        
        // Set interceptor
        org.hibernate.Interceptor interceptor = new org.hibernate.EmptyInterceptor();
        config.setInterceptor(interceptor);
        
        // Get interceptor
        org.hibernate.Interceptor currentInterceptor = config.getInterceptor();
        
        // Set entity resolver
        org.xml.sax.EntityResolver resolver = new org.xml.sax.EntityResolver() {
            public org.xml.sax.InputSource resolveEntity(String publicId, String systemId) {
                return null;
            }
        };
        config.setEntityResolver(resolver);
        
        // Set entity not found delegate
        config.setEntityNotFoundDelegate(new org.hibernate.proxy.EntityNotFoundDelegate() {
            public void handleEntityNotFound(String entityName, java.io.Serializable id) {
                throw new IllegalArgumentException("Entity not found: " + entityName);
            }
        });
    }
    
    // Configuration metadata access
    public void configurationMetadataAccess() {
        Configuration config = new Configuration();
        config.configure();
        config.addAnnotatedClass(Employee.class);
        
        // Get class mapping
        PersistentClass persistentClass = config.getClassMapping("com.example.Employee");
        
        // Get collection mapping
        Collection collectionMapping = config.getCollectionMapping("com.example.Employee.departments");
        
        // Get all class mappings
        java.util.Iterator<PersistentClass> classMappings = config.getClassMappings();
        
        // Get table mappings
        java.util.Iterator<org.hibernate.mapping.Table> tableMappings = config.getTableMappings();
        
        // Build mappings
        config.buildMappings();
        
        // Create mappings
        Mappings mappings = config.createMappings();
        
        // Get type resolver
        org.hibernate.type.TypeResolver typeResolver = config.getTypeResolver();
    }
    
    // Building SessionFactory from Configuration
    public void buildSessionFactory() {
        // Basic SessionFactory build
        Configuration config = new Configuration();
        config.configure();
        SessionFactory factory = config.buildSessionFactory();
        
        // SessionFactory with ServiceRegistry
        Configuration configWithRegistry = new Configuration();
        configWithRegistry.configure();
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
            .applySettings(configWithRegistry.getProperties())
            .build();
        SessionFactory factoryWithRegistry = configWithRegistry.buildSessionFactory(serviceRegistry);
        
        // Get settings
        Settings settings = config.buildSettings();
    }
    
    // Configuration with cache settings
    public void configurationWithCache() {
        Configuration config = new Configuration();
        
        // Cache configuration properties
        config.setProperty("hibernate.cache.use_second_level_cache", "true");
        config.setProperty("hibernate.cache.use_query_cache", "true");
        config.setProperty("hibernate.cache.region.factory_class", 
            "org.hibernate.cache.ehcache.EhCacheRegionFactory");
        config.setProperty("hibernate.cache.provider_configuration_file_resource_path", 
            "ehcache.xml");
        
        // Cache concurrency strategy
        config.setCacheConcurrencyStrategy("com.example.Employee", "read-write");
        config.setCacheConcurrencyStrategy("com.example.Department", "read-only");
        
        // Collection cache
        config.setCollectionCacheConcurrencyStrategy("com.example.Employee.projects", "read-write");
        
        // Get cache region prefix
        String cacheRegionPrefix = config.getCacheRegionPrefix();
    }
    
    // Method accepting Configuration as parameter
    public void processWithConfiguration(Configuration cfg) {
        cfg.addAnnotatedClass(Employee.class);
        SessionFactory factory = cfg.buildSessionFactory();
    }
    
    // Method returning Configuration
    public Configuration createConfiguration() {
        return new Configuration().configure();
    }
    
    // Method returning Configuration with custom settings
    public Configuration createCustomConfiguration(String dialect, String url) {
        Configuration config = new Configuration();
        config.setProperty("hibernate.dialect", dialect);
        config.setProperty("hibernate.connection.url", url);
        return config;
    }
    
    // Static method with Configuration
    public static Configuration buildStaticConfiguration() {
        if (staticConfiguration == null) {
            staticConfiguration = new Configuration();
            staticConfiguration.configure();
        }
        return staticConfiguration;
    }
    
    // Configuration builder pattern
    public static class ConfigurationBuilder {
        private Configuration configuration;
        
        public ConfigurationBuilder() {
            this.configuration = new Configuration();
        }
        
        public ConfigurationBuilder withConfigFile(String configFile) {
            configuration.configure(configFile);
            return this;
        }
        
        public ConfigurationBuilder withProperty(String key, String value) {
            configuration.setProperty(key, value);
            return this;
        }
        
        public ConfigurationBuilder withAnnotatedClass(Class<?> clazz) {
            configuration.addAnnotatedClass(clazz);
            return this;
        }
        
        public ConfigurationBuilder withResource(String resource) {
            configuration.addResource(resource);
            return this;
        }
        
        public Configuration build() {
            return configuration;
        }
        
        public SessionFactory buildSessionFactory() {
            return configuration.buildSessionFactory();
        }
    }
    
    // Inner class demonstrating Configuration usage
    class ConfigurationManager {
        private Configuration config;
        
        public ConfigurationManager() {
            this.config = new Configuration();
            this.config.configure();
        }
        
        public Configuration getConfig() {
            return config;
        }
        
        public void addEntity(Class<?> entityClass) {
            config.addAnnotatedClass(entityClass);
        }
        
        public void setProperty(String key, String value) {
            config.setProperty(key, value);
        }
        
        public SessionFactory createSessionFactory() {
            return config.buildSessionFactory();
        }
    }
    
    // Configuration wrapper class
    static class ConfigurationWrapper {
        private final Configuration delegate;
        
        public ConfigurationWrapper() {
            this.delegate = new Configuration();
        }
        
        public ConfigurationWrapper(Configuration delegate) {
            this.delegate = delegate;
        }
        
        public Configuration getDelegate() {
            return delegate;
        }
        
        public ConfigurationWrapper configure() {
            delegate.configure();
            return this;
        }
        
        public ConfigurationWrapper addAnnotatedClass(Class<?> clazz) {
            delegate.addAnnotatedClass(clazz);
            return this;
        }
        
        public SessionFactory buildSessionFactory() {
            return delegate.buildSessionFactory();
        }
    }
    
    // Configuration provider interface
    interface ConfigurationProvider {
        Configuration getConfiguration();
    }
    
    // Configuration provider implementation
    static class DefaultConfigurationProvider implements ConfigurationProvider {
        private Configuration configuration;
        
        public DefaultConfigurationProvider() {
            this.configuration = new Configuration();
            this.configuration.configure();
        }
        
        @Override
        public Configuration getConfiguration() {
            return configuration;
        }
    }
    
    // Employee entity class
    static class Employee {
        private int id;
        private String name;
        private int age;
        
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }
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
    
    // Project entity class
    static class Project {
        private int id;
        private String title;
        
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
    }
}

