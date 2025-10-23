// sql-java-084: EclipseLinkEntityManagerUsage
// Detects EclipseLink EntityManager usage (JPA implementation)
// This file tests detection of EclipseLink-specific EntityManager patterns

package com.example.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.eclipse.persistence.jpa.JpaEntityManager;
import org.eclipse.persistence.jpa.JpaEntityManagerFactory;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.server.ServerSession;
import org.eclipse.persistence.queries.DatabaseQuery;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class EclipseLinkEntityManagerExample {
    
    // VIOLATION: Creating EclipseLink EntityManagerFactory
    public EntityManagerFactory createEclipseLinkFactory() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(PersistenceUnitProperties.TARGET_DATABASE, "org.eclipse.persistence.platform.database.OraclePlatform");
        properties.put(PersistenceUnitProperties.ECLIPSELINK_PERSISTENCE_XML, "META-INF/persistence.xml");
        return Persistence.createEntityManagerFactory("eclipselink-pu", properties);
    }
    
    // VIOLATION: Using JpaEntityManager (EclipseLink specific)
    public void useJpaEntityManager(EntityManager em) {
        JpaEntityManager eclipseLinkEM = (JpaEntityManager) em;
        Session session = eclipseLinkEM.getActiveSession();
        ServerSession serverSession = eclipseLinkEM.getServerSession();
        eclipseLinkEM.flush();
    }
    
    // VIOLATION: Casting to JpaEntityManager
    public void queryWithEclipseLink(EntityManager entityManager) {
        JpaEntityManager jpaEM = (JpaEntityManager) entityManager;
        Session session = jpaEM.getActiveSession();
        // Execute custom operations
        session.executeQuery("SELECT * FROM Employee");
    }
    
    // VIOLATION: Using EclipseLink Session from EntityManager
    public void accessEclipseLinkSession() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-pu");
        EntityManager em = emf.createEntityManager();
        JpaEntityManager eclipseLinkEM = em.unwrap(JpaEntityManager.class);
        Session session = eclipseLinkEM.getSession();
        session.readAllObjects(Employee.class);
    }
    
    // VIOLATION: EclipseLink-specific EntityManager operations
    public void eclipseLinkSpecificOperations(EntityManager em) {
        JpaEntityManager jpaEntityManager = (JpaEntityManager) em;
        jpaEntityManager.setFlushMode(javax.persistence.FlushModeType.COMMIT);
        DatabaseQuery query = new org.eclipse.persistence.queries.ReadAllQuery(Employee.class);
        jpaEntityManager.getActiveSession().executeQuery(query);
    }
    
    // VIOLATION: Unwrapping to JpaEntityManager
    public void unwrapToEclipseLink(EntityManager entityManager) {
        JpaEntityManager eclipseLinkEntityManager = entityManager.unwrap(JpaEntityManager.class);
        eclipseLinkEntityManager.getActiveSession().log("Custom logging");
    }
    
    // VIOLATION: Creating EntityManager with EclipseLink properties
    public EntityManager createEclipseLinkEntityManager() {
        Map<String, String> props = new HashMap<>();
        props.put("eclipselink.logging.level", "FINE");
        props.put("eclipselink.cache.shared.default", "false");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-unit");
        return emf.createEntityManager(props);
    }
    
    // VIOLATION: Using JpaEntityManagerFactory
    public void useJpaEntityManagerFactory() {
        JpaEntityManagerFactory jpaEMF = (JpaEntityManagerFactory) 
            Persistence.createEntityManagerFactory("eclipselink-unit");
        ServerSession session = jpaEMF.getServerSession();
        session.getLogin().setDriverClassName("oracle.jdbc.OracleDriver");
    }
    
    // VIOLATION: EclipseLink EntityManager with custom session
    public void customSessionAccess(EntityManager em) {
        JpaEntityManager jpaEM = em.unwrap(JpaEntityManager.class);
        Session session = jpaEM.getActiveSession();
        session.getIdentityMapAccessor().initializeAllIdentityMaps();
    }
    
    // NON-VIOLATION: Standard JPA EntityManager usage (no EclipseLink specifics)
    public void standardJpaUsage() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("standard-pu");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Employee emp = em.find(Employee.class, 1L);
        em.getTransaction().commit();
        em.close();
    }
    
    // NON-VIOLATION: Generic EntityManager operations
    public List<Employee> findEmployees(EntityManager entityManager) {
        return entityManager.createQuery("SELECT e FROM Employee e", Employee.class)
                           .getResultList();
    }
    
    // NON-VIOLATION: Standard JPA persist operation
    public void saveEmployee(EntityManager em, Employee employee) {
        em.getTransaction().begin();
        em.persist(employee);
        em.getTransaction().commit();
    }
    
    // NON-VIOLATION: Using standard JPA API only
    public void updateEmployee(EntityManager em, Long id, String name) {
        Employee emp = em.find(Employee.class, id);
        if (emp != null) {
            emp.setName(name);
            em.merge(emp);
        }
    }
}

class Employee {
    private Long id;
    private String name;
    
    public void setName(String name) { this.name = name; }
}

