// sql-java-187: CdiUsage
// Detects CDI (Contexts and Dependency Injection) usage
// This file tests detection of CDI patterns in Java and Jakarta EE

package com.example.cdi;

import javax.inject.*;
import jakarta.inject.*;
import javax.enterprise.context.*;
import jakarta.enterprise.context.*;
import javax.enterprise.inject.*;
import jakarta.enterprise.inject.*;
import javax.enterprise.event.*;
import jakarta.enterprise.event.*;
import javax.decorator.Decorator;
import jakarta.decorator.Decorator;
import javax.interceptor.Interceptor;

// VIOLATION: CDI bean with @Named
@Named
@RequestScoped
public class UserController {
    
    @Inject
    private UserService userService;
    
    public void createUser(String name) {
        userService.save(name);
    }
}

// VIOLATION: CDI bean with @Inject
@ApplicationScoped
public class ProductService {
    
    @Inject
    private ProductRepository repository;
    
    @Inject
    private EntityManager entityManager;
    
    public void saveProduct(Product product) {
        repository.save(product);
    }
}

// VIOLATION: Using @Produces
@ApplicationScoped
public class DatabaseProducer {
    
    @Produces
    @Database
    public DataSource createDataSource() {
        return new DataSource("jdbc:mysql://localhost/db");
    }
    
    @Produces
    @RequestScoped
    public EntityManager createEntityManager(EntityManagerFactory emf) {
        return emf.createEntityManager();
    }
}

// VIOLATION: Qualifier annotation
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
public @interface Database { }

// VIOLATION: Using qualifiers with @Inject
public class OrderService {
    
    @Inject
    @Database
    private DataSource dataSource;
    
    @Inject
    @Named("primary")
    private PaymentProcessor paymentProcessor;
}

// VIOLATION: CDI scopes
@SessionScoped
public class ShoppingCart implements Serializable {
    
    private List<Item> items = new ArrayList<>();
    
    public void addItem(Item item) {
        items.add(item);
    }
}

@ConversationScoped
public class WizardController implements Serializable {
    
    @Inject
    private Conversation conversation;
    
    public void startWizard() {
        conversation.begin();
    }
    
    public void completeWizard() {
        conversation.end();
    }
}

// VIOLATION: Dependent scope
@Dependent
public class TransientService {
    
    public void doSomething() { }
}

// VIOLATION: CDI Events
@ApplicationScoped
public class EventPublisher {
    
    @Inject
    private Event<OrderCreatedEvent> orderCreatedEvent;
    
    public void publishOrderCreated(Order order) {
        orderCreatedEvent.fire(new OrderCreatedEvent(order));
    }
    
    @Inject
    private Event<PaymentProcessedEvent> paymentEvent;
    
    public void notifyPayment() {
        paymentEvent.fireAsync(new PaymentProcessedEvent());
    }
}

// VIOLATION: CDI Event Observer
@ApplicationScoped
public class NotificationService {
    
    public void onOrderCreated(@Observes OrderCreatedEvent event) {
        System.out.println("Order created: " + event.getOrder());
    }
    
    public void onPaymentProcessed(@ObservesAsync PaymentProcessedEvent event) {
        // Async processing
    }
    
    public void onTransactionPhase(@Observes(during = TransactionPhase.AFTER_SUCCESS) DataChangeEvent event) {
        // Handle after transaction success
    }
}

// VIOLATION: CDI Interceptor
@Interceptor
@Logged
@Priority(Interceptor.Priority.APPLICATION)
public class LoggingInterceptor {
    
    @AroundInvoke
    public Object log(InvocationContext ctx) throws Exception {
        System.out.println("Before: " + ctx.getMethod().getName());
        Object result = ctx.proceed();
        System.out.println("After: " + ctx.getMethod().getName());
        return result;
    }
}

// VIOLATION: Interceptor binding
@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Logged { }

// VIOLATION: Using interceptor binding
@ApplicationScoped
@Logged
public class AccountService {
    
    public void deposit(double amount) { }
}

// VIOLATION: CDI Decorator
@Decorator
public abstract class AuditDecorator implements PaymentProcessor {
    
    @Inject
    @Delegate
    private PaymentProcessor delegate;
    
    @Override
    public void processPayment(double amount) {
        logAudit("Payment: " + amount);
        delegate.processPayment(amount);
    }
    
    private void logAudit(String message) { }
}

// VIOLATION: Alternative beans
@Alternative
@Priority(100)
@ApplicationScoped
public class MockUserService implements UserService {
    
    public void save(String name) {
        // Mock implementation
    }
}

// VIOLATION: Stereotype annotation
@Stereotype
@Named
@RequestScoped
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Controller { }

// VIOLATION: Using stereotype
@Controller
public class CustomerController {
    
    @Inject
    private CustomerService service;
}

// VIOLATION: Disposer method
@ApplicationScoped
public class ResourceManager {
    
    @Produces
    @RequestScoped
    public Connection createConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost/db");
    }
    
    public void closeConnection(@Disposes Connection connection) throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}

// VIOLATION: Instance for dynamic lookup
@ApplicationScoped
public class ServiceLocator {
    
    @Inject
    private Instance<PaymentProcessor> processors;
    
    public PaymentProcessor getProcessor(String type) {
        return processors.select(new TypeLiteral(type)).get();
    }
}

// NON-VIOLATION: Plain Java class without CDI
public class SimpleService {
    
    private Repository repository;
    
    public SimpleService(Repository repository) {
        this.repository = repository;
    }
}

// NON-VIOLATION: Spring dependency injection
@org.springframework.stereotype.Component
public class SpringService {
    
    @org.springframework.beans.factory.annotation.Autowired
    private SpringRepository repository;
}

// NON-VIOLATION: Manual object creation
public class ManualService {
    
    private DataSource dataSource = new DataSource("jdbc:mysql://localhost/db");
    
    public void doWork() { }
}

// NON-VIOLATION: Static utility class
public class StringUtils {
    
    public static String trim(String input) {
        return input != null ? input.trim() : null;
    }
}

// Helper classes
interface UserService { void save(String name); }
interface PaymentProcessor { void processPayment(double amount); }
interface Repository { }
class SpringRepository { }
class Product { }
class ProductRepository { void save(Product p) { } }
class DataSource { DataSource(String url) { } }
class EntityManagerFactory { EntityManager createEntityManager() { return null; } }
class EntityManager { }
class Item { }
class Order { }
class OrderCreatedEvent { OrderCreatedEvent(Order o) { } Order getOrder() { return null; } }
class PaymentProcessedEvent { }
class DataChangeEvent { }
class CustomerService { }
class TypeLiteral { TypeLiteral(String s) { } }
class Connection { boolean isClosed() { return false; } void close() { } }
class DriverManager { static Connection getConnection(String url) { return null; } }
class SQLException extends Exception { }

