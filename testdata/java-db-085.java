// sql-java-085: EclipseLinkQueryUsage
// Detects EclipseLink specific query features
// This file tests detection of EclipseLink-specific query patterns

package com.example.persistence;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.eclipse.persistence.queries.*;
import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.eclipse.persistence.expressions.Expression;
import org.eclipse.persistence.config.QueryHints;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.jpa.JpaQuery;
import org.eclipse.persistence.sessions.Session;
import java.util.List;

public class EclipseLinkQueryExample {
    
    // VIOLATION: Using EclipseLink ReadAllQuery
    public List<Customer> useReadAllQuery(Session session) {
        ReadAllQuery query = new ReadAllQuery(Customer.class);
        ExpressionBuilder builder = new ExpressionBuilder();
        Expression expr = builder.get("status").equal("ACTIVE");
        query.setSelectionCriteria(expr);
        return (List<Customer>) session.executeQuery(query);
    }
    
    // VIOLATION: Using EclipseLink ReadObjectQuery
    public Customer useReadObjectQuery(Session session, Long id) {
        ReadObjectQuery query = new ReadObjectQuery(Customer.class);
        ExpressionBuilder builder = new ExpressionBuilder();
        query.setSelectionCriteria(builder.get("id").equal(id));
        return (Customer) session.executeQuery(query);
    }
    
    // VIOLATION: Using EclipseLink UpdateObjectQuery
    public void useUpdateObjectQuery(Session session, Customer customer) {
        UpdateObjectQuery query = new UpdateObjectQuery(customer);
        session.executeQuery(query);
    }
    
    // VIOLATION: Using EclipseLink DeleteObjectQuery
    public void useDeleteObjectQuery(Session session, Customer customer) {
        DeleteObjectQuery query = new DeleteObjectQuery(customer);
        session.executeQuery(query);
    }
    
    // VIOLATION: Using EclipseLink DataModifyQuery
    public void useDataModifyQuery(Session session) {
        DataModifyQuery query = new DataModifyQuery();
        query.setSQLString("UPDATE customers SET status = 'INACTIVE' WHERE last_login < SYSDATE - 365");
        session.executeQuery(query);
    }
    
    // VIOLATION: Using EclipseLink DataReadQuery
    public List useDataReadQuery(Session session) {
        DataReadQuery query = new DataReadQuery();
        query.setSQLString("SELECT * FROM customers WHERE country = 'USA'");
        return (List) session.executeQuery(query);
    }
    
    // VIOLATION: Using EclipseLink query hints
    public List<Customer> useEclipseLinkQueryHints(EntityManager em) {
        return em.createQuery("SELECT c FROM Customer c", Customer.class)
                .setHint(QueryHints.READ_ONLY, HintValues.TRUE)
                .setHint(QueryHints.CACHE_USAGE, HintValues.DoNotCheckCache)
                .setHint(QueryHints.JDBC_FETCH_SIZE, 100)
                .getResultList();
    }
    
    // VIOLATION: Using EclipseLink query with batch fetch hint
    public List<Customer> useBatchFetchHint(EntityManager em) {
        return em.createQuery("SELECT c FROM Customer c JOIN FETCH c.orders", Customer.class)
                .setHint(QueryHints.BATCH, "c.orders")
                .setHint(QueryHints.BATCH_TYPE, "IN")
                .getResultList();
    }
    
    // VIOLATION: Using EclipseLink SQLCall
    public void useSQLCall(Session session) {
        SQLCall sqlCall = new SQLCall("SELECT * FROM customers WHERE status = 'ACTIVE'");
        DataReadQuery query = new DataReadQuery();
        query.setCall(sqlCall);
        session.executeQuery(query);
    }
    
    // VIOLATION: Using JpaQuery (EclipseLink specific)
    public void useJpaQuery(EntityManager em) {
        Query query = em.createQuery("SELECT c FROM Customer c");
        JpaQuery jpaQuery = (JpaQuery) query;
        DatabaseQuery databaseQuery = jpaQuery.getDatabaseQuery();
        databaseQuery.setCascadePolicy(DatabaseQuery.CascadeAllParts);
    }
    
    // VIOLATION: Using EclipseLink ReportQuery
    public List useReportQuery(Session session) {
        ReportQuery query = new ReportQuery(Customer.class, new ExpressionBuilder());
        query.addAttribute("name");
        query.addAttribute("email");
        query.addAverage("orderTotal");
        return (List) session.executeQuery(query);
    }
    
    // VIOLATION: Using EclipseLink query with refresh hint
    public Customer useRefreshHint(EntityManager em, Long id) {
        return em.createQuery("SELECT c FROM Customer c WHERE c.id = :id", Customer.class)
                .setParameter("id", id)
                .setHint(QueryHints.REFRESH, HintValues.TRUE)
                .setHint(QueryHints.REFRESH_CASCADE, "ALL")
                .getSingleResult();
    }
    
    // VIOLATION: Using ExpressionBuilder for complex queries
    public List<Customer> useExpressionBuilder(Session session) {
        ReadAllQuery query = new ReadAllQuery(Customer.class);
        ExpressionBuilder builder = query.getExpressionBuilder();
        Expression criteria = builder.get("age").greaterThan(18)
                                    .and(builder.get("country").equal("USA"));
        query.setSelectionCriteria(criteria);
        return (List<Customer>) session.executeQuery(query);
    }
    
    // NON-VIOLATION: Standard JPQL query
    public List<Customer> standardJpqlQuery(EntityManager em) {
        return em.createQuery("SELECT c FROM Customer c WHERE c.status = :status", Customer.class)
                .setParameter("status", "ACTIVE")
                .getResultList();
    }
    
    // NON-VIOLATION: Native SQL query without EclipseLink specifics
    public List<Customer> nativeQuery(EntityManager em) {
        return em.createNativeQuery("SELECT * FROM customers WHERE active = 1", Customer.class)
                .getResultList();
    }
    
    // NON-VIOLATION: Standard JPA Criteria API
    public List<Customer> useCriteriaAPI(EntityManager em) {
        javax.persistence.criteria.CriteriaBuilder cb = em.getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
        javax.persistence.criteria.Root<Customer> root = cq.from(Customer.class);
        cq.select(root).where(cb.equal(root.get("status"), "ACTIVE"));
        return em.createQuery(cq).getResultList();
    }
    
    // NON-VIOLATION: Named query execution
    public List<Customer> executeNamedQuery(EntityManager em) {
        return em.createNamedQuery("Customer.findAll", Customer.class)
                .getResultList();
    }
}

class Customer {
    private Long id;
    private String name;
    private String email;
    private String status;
    private int age;
    private String country;
}

