package com.example.database;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.metamodel.Metamodel;
import java.util.Map;
import java.util.HashMap;

/**
 * Test file for sql-java-064: JpaEntityManagerFactoryUsage
 * Detects JPA EntityManagerFactory usage
 * EntityManagerFactory affects JPA configuration
 */
public class JpaEntityManagerFactoryExample {
    
    // Field declaration with EntityManagerFactory
    private EntityManagerFactory entityManagerFactory;
    
    private static EntityManagerFactory staticEntityManagerFactory;
    
    // Constructor with EntityManagerFactory initialization
    public JpaEntityManagerFactoryExample() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("myPersistenceUnit");
    }
    
    // Constructor with custom configuration
    public JpaEntityManagerFactoryExample(String persistenceUnitName) {
        this.entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
    }
    
    // Constructor with properties
    public JpaEntityManagerFactoryExample(String persistenceUnitName, Map<String, Object> properties) {
        this.entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName, properties);
    }
    
    // Setter for EntityManagerFactory
    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.entityManagerFactory = emf;
    }
    
    // Getter returning EntityManagerFactory
    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }
    
    // Creating EntityManagerFactory with Persistence
    public void createEntityManagerFactory() {
        // Basic creation
        EntityManagerFactory emf1 = Persistence.createEntityManagerFactory("defaultPU");
        
        // Creation with properties
        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.jdbc.url", "jdbc:mysql://localhost:3306/testdb");
        properties.put("javax.persistence.jdbc.user", "root");
        properties.put("javax.persistence.jdbc.password", "password");
        properties.put("javax.persistence.jdbc.driver", "com.mysql.jdbc.Driver");
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.hbm2ddl.auto", "update");
        
        EntityManagerFactory emf2 = Persistence.createEntityManagerFactory("customPU", properties);
        
        // Creation with cache settings
        Map<String, Object> cacheProps = new HashMap<>();
        cacheProps.put("javax.persistence.sharedCache.mode", "ENABLE_SELECTIVE");
        cacheProps.put("hibernate.cache.use_second_level_cache", "true");
        cacheProps.put("hibernate.cache.use_query_cache", "true");
        cacheProps.put("hibernate.cache.region.factory_class", 
            "org.hibernate.cache.ehcache.EhCacheRegionFactory");
        
        EntityManagerFactory emf3 = Persistence.createEntityManagerFactory("cachedPU", cacheProps);
        
        // Close factories
        emf1.close();
        emf2.close();
        emf3.close();
    }
    
    // EntityManager creation from EntityManagerFactory
    public void createEntityManagers() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPU");
        
        // Create entity manager
        EntityManager em1 = emf.createEntityManager();
        
        // Create entity manager with synchronization type
        EntityManager em2 = emf.createEntityManager(SynchronizationType.SYNCHRONIZED);
        EntityManager em3 = emf.createEntityManager(SynchronizationType.UNSYNCHRONIZED);
        
        // Create entity manager with properties
        Map<String, Object> emProperties = new HashMap<>();
        emProperties.put("hibernate.show_sql", "true");
        emProperties.put("javax.persistence.query.timeout", 5000);
        EntityManager em4 = emf.createEntityManager(emProperties);
        
        // Create entity manager with synchronization and properties
        EntityManager em5 = emf.createEntityManager(SynchronizationType.SYNCHRONIZED, emProperties);
        
        // Close entity managers
        em1.close();
        em2.close();
        em3.close();
        em4.close();
        em5.close();
        
        emf.close();
    }
    
    // CriteriaBuilder operations from EntityManagerFactory
    public void criteriaBuilderOperations() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPU");
        
        // Get CriteriaBuilder
        CriteriaBuilder criteriaBuilder = emf.getCriteriaBuilder();
        
        // Create criteria query
        javax.persistence.criteria.CriteriaQuery<Employee> criteriaQuery = 
            criteriaBuilder.createQuery(Employee.class);
        
        javax.persistence.criteria.Root<Employee> root = criteriaQuery.from(Employee.class);
        criteriaQuery.select(root);
        
        // Create tuple criteria query
        javax.persistence.criteria.CriteriaQuery<javax.persistence.Tuple> tupleQuery = 
            criteriaBuilder.createTupleQuery();
        
        // Create criteria update
        javax.persistence.criteria.CriteriaUpdate<Employee> criteriaUpdate = 
            criteriaBuilder.createCriteriaUpdate(Employee.class);
        
        // Create criteria delete
        javax.persistence.criteria.CriteriaDelete<Employee> criteriaDelete = 
            criteriaBuilder.createCriteriaDelete(Employee.class);
        
        emf.close();
    }
    
    // Metamodel operations from EntityManagerFactory
    public void metamodelOperations() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPU");
        
        // Get metamodel
        Metamodel metamodel = emf.getMetamodel();
        
        // Get entity types
        java.util.Set<javax.persistence.metamodel.EntityType<?>> entityTypes = 
            metamodel.getEntities();
        
        // Get entity type by class
        javax.persistence.metamodel.EntityType<Employee> employeeType = 
            metamodel.entity(Employee.class);
        
        // Get managed types
        java.util.Set<javax.persistence.metamodel.ManagedType<?>> managedTypes = 
            metamodel.getManagedTypes();
        
        // Get embeddable types
        java.util.Set<javax.persistence.metamodel.EmbeddableType<?>> embeddableTypes = 
            metamodel.getEmbeddables();
        
        emf.close();
    }
    
    // Cache operations from EntityManagerFactory
    public void cacheOperations() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPU");
        
        // Get cache
        Cache cache = emf.getCache();
        
        // Check if entity is in cache
        boolean inCache = cache.contains(Employee.class, 1L);
        
        // Evict entity from cache
        cache.evict(Employee.class, 1L);
        
        // Evict all instances of entity class
        cache.evict(Employee.class);
        
        // Evict all entities from cache
        cache.evictAll();
        
        // Unwrap cache
        Object unwrapped = cache.unwrap(Object.class);
        
        emf.close();
    }
    
    // PersistenceUnitUtil operations from EntityManagerFactory
    public void persistenceUnitUtilOperations() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPU");
        
        // Get PersistenceUnitUtil
        PersistenceUnitUtil persistenceUnitUtil = emf.getPersistenceUnitUtil();
        
        Employee emp = new Employee();
        emp.setId(1L);
        
        // Get identifier
        Object id = persistenceUnitUtil.getIdentifier(emp);
        
        // Check if loaded
        boolean isLoaded = persistenceUnitUtil.isLoaded(emp);
        
        // Check if attribute is loaded
        boolean isNameLoaded = persistenceUnitUtil.isLoaded(emp, "name");
        
        emf.close();
    }
    
    // EntityManagerFactory properties operations
    public void propertiesOperations() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPU");
        
        // Get properties
        Map<String, Object> properties = emf.getProperties();
        
        // Iterate over properties
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
        
        emf.close();
    }
    
    // EntityManagerFactory state operations
    public void stateOperations() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPU");
        
        // Check if open
        boolean isOpen = emf.isOpen();
        
        if (isOpen) {
            // Perform operations
            EntityManager em = emf.createEntityManager();
            em.close();
        }
        
        // Close factory
        emf.close();
        
        // Check if open after close
        boolean stillOpen = emf.isOpen();
    }
    
    // Unwrap operations
    public void unwrapOperations() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPU");
        
        // Unwrap to specific type
        org.hibernate.SessionFactory sessionFactory = emf.unwrap(org.hibernate.SessionFactory.class);
        
        // Unwrap to Object
        Object unwrapped = emf.unwrap(Object.class);
        
        emf.close();
    }
    
    // Add named query
    public void addNamedQueryOperations() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPU");
        
        // Add named query (JPA 2.1+)
        emf.addNamedQuery("Employee.dynamicFindAll", 
            emf.getCriteriaBuilder().createQuery(Employee.class));
        
        // Add named entity graph
        emf.addNamedEntityGraph("Employee.detail", 
            emf.createEntityManager().getEntityGraph("Employee.detail"));
        
        emf.close();
    }
    
    // Singleton pattern with EntityManagerFactory
    public static class EntityManagerFactorySingleton {
        private static EntityManagerFactory instance;
        
        private EntityManagerFactorySingleton() {}
        
        public static EntityManagerFactory getInstance() {
            if (instance == null) {
                synchronized (EntityManagerFactorySingleton.class) {
                    if (instance == null) {
                        instance = Persistence.createEntityManagerFactory("defaultPU");
                    }
                }
            }
            return instance;
        }
        
        public static void close() {
            if (instance != null && instance.isOpen()) {
                instance.close();
                instance = null;
            }
        }
    }
    
    // Method accepting EntityManagerFactory as parameter
    public void processWithEntityManagerFactory(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        // Perform operations
        em.getTransaction().commit();
        em.close();
    }
    
    // Method returning EntityManagerFactory
    public EntityManagerFactory createFactory() {
        return Persistence.createEntityManagerFactory("myPU");
    }
    
    // Method returning EntityManagerFactory with custom config
    public EntityManagerFactory createCustomFactory(Map<String, Object> props) {
        return Persistence.createEntityManagerFactory("customPU", props);
    }
    
    // Static method with EntityManagerFactory
    public static EntityManagerFactory buildFactory(String persistenceUnitName) {
        if (staticEntityManagerFactory == null || !staticEntityManagerFactory.isOpen()) {
            staticEntityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
        }
        return staticEntityManagerFactory;
    }
    
    // Builder pattern for EntityManagerFactory
    public static class EntityManagerFactoryBuilder {
        private String persistenceUnitName;
        private Map<String, Object> properties = new HashMap<>();
        
        public EntityManagerFactoryBuilder(String persistenceUnitName) {
            this.persistenceUnitName = persistenceUnitName;
        }
        
        public EntityManagerFactoryBuilder withProperty(String key, Object value) {
            properties.put(key, value);
            return this;
        }
        
        public EntityManagerFactoryBuilder withJdbcUrl(String url) {
            properties.put("javax.persistence.jdbc.url", url);
            return this;
        }
        
        public EntityManagerFactoryBuilder withJdbcUser(String user) {
            properties.put("javax.persistence.jdbc.user", user);
            return this;
        }
        
        public EntityManagerFactoryBuilder withJdbcPassword(String password) {
            properties.put("javax.persistence.jdbc.password", password);
            return this;
        }
        
        public EntityManagerFactoryBuilder withDialect(String dialect) {
            properties.put("hibernate.dialect", dialect);
            return this;
        }
        
        public EntityManagerFactoryBuilder withShowSql(boolean showSql) {
            properties.put("hibernate.show_sql", String.valueOf(showSql));
            return this;
        }
        
        public EntityManagerFactory build() {
            return Persistence.createEntityManagerFactory(persistenceUnitName, properties);
        }
    }
    
    // Inner class demonstrating EntityManagerFactory usage
    class JpaUtil {
        private EntityManagerFactory factory;
        
        public JpaUtil(String persistenceUnitName) {
            this.factory = Persistence.createEntityManagerFactory(persistenceUnitName);
        }
        
        public EntityManagerFactory getFactory() {
            return factory;
        }
        
        public EntityManager getEntityManager() {
            return factory.createEntityManager();
        }
        
        public void shutdown() {
            if (factory != null && factory.isOpen()) {
                factory.close();
            }
        }
    }
    
    // EntityManagerFactory wrapper class
    static class EntityManagerFactoryWrapper {
        private final EntityManagerFactory delegate;
        
        public EntityManagerFactoryWrapper(EntityManagerFactory delegate) {
            this.delegate = delegate;
        }
        
        public EntityManagerFactory getDelegate() {
            return delegate;
        }
        
        public EntityManager createEntityManager() {
            return delegate.createEntityManager();
        }
        
        public CriteriaBuilder getCriteriaBuilder() {
            return delegate.getCriteriaBuilder();
        }
        
        public Metamodel getMetamodel() {
            return delegate.getMetamodel();
        }
        
        public void close() {
            if (delegate.isOpen()) {
                delegate.close();
            }
        }
    }
    
    // EntityManagerFactory provider interface
    interface EntityManagerFactoryProvider {
        EntityManagerFactory getEntityManagerFactory();
    }
    
    // EntityManagerFactory provider implementation
    static class DefaultEntityManagerFactoryProvider implements EntityManagerFactoryProvider {
        private EntityManagerFactory emf;
        
        public DefaultEntityManagerFactoryProvider(String persistenceUnitName) {
            this.emf = Persistence.createEntityManagerFactory(persistenceUnitName);
        }
        
        @Override
        public EntityManagerFactory getEntityManagerFactory() {
            return emf;
        }
        
        public void close() {
            if (emf != null && emf.isOpen()) {
                emf.close();
            }
        }
    }
    
    // EntityManagerFactory registry
    static class EntityManagerFactoryRegistry {
        private Map<String, EntityManagerFactory> factories = new HashMap<>();
        
        public void register(String name, EntityManagerFactory factory) {
            factories.put(name, factory);
        }
        
        public EntityManagerFactory get(String name) {
            return factories.get(name);
        }
        
        public void closeAll() {
            factories.values().forEach(emf -> {
                if (emf.isOpen()) {
                    emf.close();
                }
            });
            factories.clear();
        }
    }
    
    // Employee entity class
    @javax.persistence.Entity
    @javax.persistence.Table(name = "employees")
    static class Employee {
        @javax.persistence.Id
        @javax.persistence.GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        
        @javax.persistence.Column(name = "name")
        private String name;
        
        @javax.persistence.Column(name = "age")
        private Integer age;
        
        public Employee() {}
        
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Integer getAge() { return age; }
        public void setAge(Integer age) { this.age = age; }
    }
}

