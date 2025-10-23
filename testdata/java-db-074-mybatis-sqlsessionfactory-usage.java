package com.example.database;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import javax.sql.DataSource;
import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

/**
 * Test file for sql-java-074: MyBatisSqlSessionFactoryUsage
 * Detects MyBatis SqlSessionFactory usage
 * SqlSessionFactory affects MyBatis configuration
 */
public class MyBatisSqlSessionFactoryExample {
    
    private SqlSessionFactory sqlSessionFactory;
    private static SqlSessionFactory staticSqlSessionFactory;
    
    // Constructor with SqlSessionFactory initialization
    public MyBatisSqlSessionFactoryExample() {
        this.sqlSessionFactory = createSqlSessionFactory();
    }
    
    // Constructor with custom configuration
    public MyBatisSqlSessionFactoryExample(String configFile) {
        this.sqlSessionFactory = createSqlSessionFactoryFromFile(configFile);
    }
    
    // Setter for SqlSessionFactory
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }
    
    // Getter returning SqlSessionFactory
    public SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }
    
    // Creating SqlSessionFactory from XML configuration
    public void createSqlSessionFactoryFromXml() {
        try {
            // From classpath resource
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory factory1 = new SqlSessionFactoryBuilder().build(inputStream);
            
            // From file path
            Reader reader = Resources.getResourceAsReader("mybatis-config.xml");
            SqlSessionFactory factory2 = new SqlSessionFactoryBuilder().build(reader);
            
            // With environment
            SqlSessionFactory factory3 = new SqlSessionFactoryBuilder().build(reader, "development");
            
            // With properties
            Properties props = new Properties();
            props.setProperty("username", "myuser");
            props.setProperty("password", "mypassword");
            SqlSessionFactory factory4 = new SqlSessionFactoryBuilder().build(reader, "development", props);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Creating SqlSessionFactory programmatically
    public void createSqlSessionFactoryProgrammatically() {
        // DataSource configuration
        DataSource dataSource = new UnpooledDataSource(
            "com.mysql.cj.jdbc.Driver",
            "jdbc:mysql://localhost:3306/testdb",
            "username",
            "password"
        );
        
        // Transaction factory
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        
        // Environment
        Environment environment = new Environment("development", transactionFactory, dataSource);
        
        // Configuration
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(EmployeeMapper.class);
        
        // Build SqlSessionFactory
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(configuration);
    }
    
    // Creating SqlSessionFactory with PooledDataSource
    public void createSqlSessionFactoryWithPooledDataSource() {
        PooledDataSource dataSource = new PooledDataSource();
        dataSource.setDriver("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/testdb");
        dataSource.setUsername("username");
        dataSource.setPassword("password");
        dataSource.setPoolMaximumActiveConnections(20);
        dataSource.setPoolMaximumIdleConnections(5);
        dataSource.setPoolMaximumCheckoutTime(20000);
        dataSource.setPoolTimeToWait(20000);
        
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(configuration);
    }
    
    // SqlSessionFactory operations
    public void sqlSessionFactoryOperations() {
        SqlSessionFactory factory = createSqlSessionFactory();
        
        // Open session
        SqlSession session1 = factory.openSession();
        
        // Open session with auto-commit
        SqlSession session2 = factory.openSession(true);
        
        // Open session with executor type
        SqlSession session3 = factory.openSession(ExecutorType.BATCH);
        
        // Open session with executor type and auto-commit
        SqlSession session4 = factory.openSession(ExecutorType.REUSE, true);
        
        // Open session with connection
        java.sql.Connection connection = null; // Assume connection is provided
        SqlSession session5 = factory.openSession(connection);
        
        // Get configuration
        Configuration config = factory.getConfiguration();
        
        // Close sessions
        session1.close();
        session2.close();
        session3.close();
        session4.close();
        session5.close();
    }
    
    // Configuration operations
    public void configurationOperations() {
        SqlSessionFactory factory = createSqlSessionFactory();
        Configuration config = factory.getConfiguration();
        
        // Check if lazy loading is enabled
        boolean lazyLoadingEnabled = config.isLazyLoadingEnabled();
        
        // Check if aggressive lazy loading is enabled
        boolean aggressiveLazyLoading = config.isAggressiveLazyLoading();
        
        // Check if multiple result sets are enabled
        boolean multipleResultSetsEnabled = config.isMultipleResultSetsEnabled();
        
        // Check if use column label is enabled
        boolean useColumnLabel = config.isUseColumnLabel();
        
        // Check if use generated keys is enabled
        boolean useGeneratedKeys = config.isUseGeneratedKeys();
        
        // Check if auto mapping behavior
        org.apache.ibatis.session.AutoMappingBehavior autoMappingBehavior = config.getAutoMappingBehavior();
        
        // Check if auto mapping unknown column behavior
        org.apache.ibatis.session.AutoMappingUnknownColumnBehavior unknownColumnBehavior = 
            config.getAutoMappingUnknownColumnBehavior();
        
        // Check if default executor type
        ExecutorType defaultExecutorType = config.getDefaultExecutorType();
        
        // Check if default statement timeout
        Integer defaultStatementTimeout = config.getDefaultStatementTimeout();
        
        // Check if default fetch size
        Integer defaultFetchSize = config.getDefaultFetchSize();
        
        // Check if safe row bounds enabled
        boolean safeRowBoundsEnabled = config.isSafeRowBoundsEnabled();
        
        // Check if safe result handler enabled
        boolean safeResultHandlerEnabled = config.isSafeResultHandlerEnabled();
        
        // Check if map underscore to camel case
        boolean mapUnderscoreToCamelCase = config.isMapUnderscoreToCamelCase();
        
        // Check if local cache scope
        org.apache.ibatis.session.LocalCacheScope localCacheScope = config.getLocalCacheScope();
        
        // Check if jdbc type for null
        org.apache.ibatis.type.JdbcType jdbcTypeForNull = config.getJdbcTypeForNull();
        
        // Check if lazy load trigger methods
        java.util.Set<String> lazyLoadTriggerMethods = config.getLazyLoadTriggerMethods();
        
        // Check if default scripting language
        Class<?> defaultScriptingLanguage = config.getDefaultScriptingLanguage();
        
        // Check if default enum type handler
        Class<?> defaultEnumTypeHandler = config.getDefaultEnumTypeHandler();
        
        // Check if call setters on nulls
        boolean callSettersOnNulls = config.isCallSettersOnNulls();
        
        // Check if return instance for empty row
        boolean returnInstanceForEmptyRow = config.isReturnInstanceForEmptyRow();
        
        // Check if shrink whitespaces in sql
        boolean shrinkWhitespacesInSql = config.isShrinkWhitespacesInSql();
        
        // Check if nullable on for each
        boolean nullableOnForEach = config.isNullableOnForEach();
        
        // Check if arg name based constructor auto mapping
        boolean argNameBasedConstructorAutoMapping = config.isArgNameBasedConstructorAutoMapping();
    }
    
    // Environment operations
    public void environmentOperations() {
        SqlSessionFactory factory = createSqlSessionFactory();
        Configuration config = factory.getConfiguration();
        
        // Get environment
        Environment environment = config.getEnvironment();
        
        // Get environment id
        String environmentId = environment.getId();
        
        // Get transaction factory
        TransactionFactory transactionFactory = environment.getTransactionFactory();
        
        // Get data source
        DataSource dataSource = environment.getDataSource();
    }
    
    // Mapper operations
    public void mapperOperations() {
        SqlSessionFactory factory = createSqlSessionFactory();
        Configuration config = factory.getConfiguration();
        
        // Check if mapper is registered
        boolean hasMapper = config.hasMapper(EmployeeMapper.class);
        
        // Get mapper
        EmployeeMapper mapper = factory.openSession().getMapper(EmployeeMapper.class);
        
        // Add mapper
        config.addMapper(EmployeeMapper.class);
        
        // Add mappers from package
        config.addMappers("com.example.mapper");
        
        // Add mappers from package with super type
        config.addMappers("com.example.mapper", EmployeeMapper.class);
    }
    
    // Type handler operations
    public void typeHandlerOperations() {
        SqlSessionFactory factory = createSqlSessionFactory();
        Configuration config = factory.getConfiguration();
        
        // Register type handler
        config.getTypeHandlerRegistry().register(String.class, org.apache.ibatis.type.StringTypeHandler.class);
        
        // Register type handler with jdbc type
        config.getTypeHandlerRegistry().register(String.class, 
            org.apache.ibatis.type.JdbcType.VARCHAR, 
            org.apache.ibatis.type.StringTypeHandler.class);
        
        // Check if type handler is registered
        boolean hasTypeHandler = config.getTypeHandlerRegistry().hasTypeHandler(String.class);
        
        // Get type handler
        org.apache.ibatis.type.TypeHandler<?> typeHandler = 
            config.getTypeHandlerRegistry().getTypeHandler(String.class);
    }
    
    // Cache operations
    public void cacheOperations() {
        SqlSessionFactory factory = createSqlSessionFactory();
        Configuration config = factory.getConfiguration();
        
        // Add cache
        config.addCache(org.apache.ibatis.cache.impl.PerpetualCache.class);
        
        // Add cache with properties
        Properties cacheProps = new Properties();
        cacheProps.setProperty("size", "1024");
        config.addCache(org.apache.ibatis.cache.impl.PerpetualCache.class, cacheProps);
        
        // Get cache
        org.apache.ibatis.cache.Cache cache = config.getCache("com.example.mapper.EmployeeMapper");
    }
    
    // Singleton pattern with SqlSessionFactory
    public static class SqlSessionFactorySingleton {
        private static SqlSessionFactory instance;
        
        private SqlSessionFactorySingleton() {}
        
        public static SqlSessionFactory getInstance() {
            if (instance == null) {
                synchronized (SqlSessionFactorySingleton.class) {
                    if (instance == null) {
                        instance = createSqlSessionFactory();
                    }
                }
            }
            return instance;
        }
        
        public static void close() {
            if (instance != null) {
                instance = null;
            }
        }
    }
    
    // Method accepting SqlSessionFactory as parameter
    public void processWithSqlSessionFactory(SqlSessionFactory factory) {
        SqlSession session = factory.openSession();
        try {
            // Process with session
        } finally {
            session.close();
        }
    }
    
    // Method returning SqlSessionFactory
    public SqlSessionFactory createSqlSessionFactory() {
        try {
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            return new SqlSessionFactoryBuilder().build(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("Error creating SqlSessionFactory", e);
        }
    }
    
    // Method returning SqlSessionFactory with custom config
    public SqlSessionFactory createSqlSessionFactoryFromFile(String configFile) {
        try {
            InputStream inputStream = Resources.getResourceAsStream(configFile);
            return new SqlSessionFactoryBuilder().build(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("Error creating SqlSessionFactory from " + configFile, e);
        }
    }
    
    // Static method with SqlSessionFactory
    public static SqlSessionFactory buildSqlSessionFactory() {
        if (staticSqlSessionFactory == null) {
            staticSqlSessionFactory = new MyBatisSqlSessionFactoryExample().createSqlSessionFactory();
        }
        return staticSqlSessionFactory;
    }
    
    // Builder pattern for SqlSessionFactory
    public static class SqlSessionFactoryBuilder {
        private String configFile;
        private String environment;
        private Properties properties;
        private DataSource dataSource;
        
        public SqlSessionFactoryBuilder withConfigFile(String configFile) {
            this.configFile = configFile;
            return this;
        }
        
        public SqlSessionFactoryBuilder withEnvironment(String environment) {
            this.environment = environment;
            return this;
        }
        
        public SqlSessionFactoryBuilder withProperties(Properties properties) {
            this.properties = properties;
            return this;
        }
        
        public SqlSessionFactoryBuilder withDataSource(DataSource dataSource) {
            this.dataSource = dataSource;
            return this;
        }
        
        public SqlSessionFactory build() {
            try {
                if (configFile != null) {
                    InputStream inputStream = Resources.getResourceAsStream(configFile);
                    if (environment != null && properties != null) {
                        return new org.apache.ibatis.session.SqlSessionFactoryBuilder()
                            .build(inputStream, environment, properties);
                    } else if (environment != null) {
                        return new org.apache.ibatis.session.SqlSessionFactoryBuilder()
                            .build(inputStream, environment);
                    } else {
                        return new org.apache.ibatis.session.SqlSessionFactoryBuilder()
                            .build(inputStream);
                    }
                } else {
                    // Build programmatically
                    TransactionFactory transactionFactory = new JdbcTransactionFactory();
                    Environment env = new Environment("development", transactionFactory, dataSource);
                    Configuration configuration = new Configuration(env);
                    return new org.apache.ibatis.session.SqlSessionFactoryBuilder()
                        .build(configuration);
                }
            } catch (Exception e) {
                throw new RuntimeException("Error building SqlSessionFactory", e);
            }
        }
    }
    
    // Inner class demonstrating SqlSessionFactory usage
    class MyBatisUtil {
        private SqlSessionFactory factory;
        
        public MyBatisUtil() {
            this.factory = createSqlSessionFactory();
        }
        
        public SqlSessionFactory getFactory() {
            return factory;
        }
        
        public SqlSession openSession() {
            return factory.openSession();
        }
        
        public SqlSession openSession(boolean autoCommit) {
            return factory.openSession(autoCommit);
        }
        
        public SqlSession openSession(ExecutorType execType) {
            return factory.openSession(execType);
        }
        
        public void shutdown() {
            // SqlSessionFactory doesn't have a close method
            // Resources are managed by the application
        }
    }
    
    // SqlSessionFactory wrapper class
    static class SqlSessionFactoryWrapper {
        private final SqlSessionFactory delegate;
        
        public SqlSessionFactoryWrapper(SqlSessionFactory delegate) {
            this.delegate = delegate;
        }
        
        public SqlSessionFactory getDelegate() {
            return delegate;
        }
        
        public SqlSession openSession() {
            return delegate.openSession();
        }
        
        public SqlSession openSession(boolean autoCommit) {
            return delegate.openSession(autoCommit);
        }
        
        public SqlSession openSession(ExecutorType execType) {
            return delegate.openSession(execType);
        }
        
        public Configuration getConfiguration() {
            return delegate.getConfiguration();
        }
    }
    
    // SqlSessionFactory provider interface
    interface SqlSessionFactoryProvider {
        SqlSessionFactory getSqlSessionFactory();
    }
    
    // SqlSessionFactory provider implementation
    static class DefaultSqlSessionFactoryProvider implements SqlSessionFactoryProvider {
        private SqlSessionFactory factory;
        
        public DefaultSqlSessionFactoryProvider() {
            this.factory = new MyBatisSqlSessionFactoryExample().createSqlSessionFactory();
        }
        
        @Override
        public SqlSessionFactory getSqlSessionFactory() {
            return factory;
        }
    }
    
    // SqlSessionFactory registry
    static class SqlSessionFactoryRegistry {
        private java.util.Map<String, SqlSessionFactory> factories = new java.util.HashMap<>();
        
        public void register(String name, SqlSessionFactory factory) {
            factories.put(name, factory);
        }
        
        public SqlSessionFactory get(String name) {
            return factories.get(name);
        }
        
        public void clear() {
            factories.clear();
        }
    }
    
    // Employee mapper interface
    interface EmployeeMapper {
        Employee findById(Long id);
        List<Employee> findAll();
        void insert(Employee employee);
        void update(Employee employee);
        void delete(Long id);
    }
    
    // Employee entity class
    static class Employee {
        private Long id;
        private String name;
        private String department;
        
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDepartment() { return department; }
        public void setDepartment(String department) { this.department = department; }
    }
}
