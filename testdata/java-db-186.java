// sql-java-186: EjbUsage
// Detects EJB (Enterprise JavaBeans) usage patterns
// This file tests detection of EJB component usage

package com.example.ejb;

import javax.ejb.*;
import jakarta.ejb.*;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

// VIOLATION: Stateless session bean
@Stateless
public class UserServiceBean {
    
    @PersistenceContext
    private EntityManager em;
    
    public void createUser(String name) {
        // Business logic
    }
}

// VIOLATION: Stateful session bean
@Stateful
public class ShoppingCartBean implements ShoppingCart {
    
    private List<String> items = new ArrayList<>();
    
    public void addItem(String item) {
        items.add(item);
    }
    
    @Remove
    public void checkout() {
        // Process checkout
    }
}

// VIOLATION: Singleton session bean
@Singleton
@Startup
public class ApplicationConfigBean {
    
    @PostConstruct
    public void initialize() {
        // Initialize application
    }
}

// VIOLATION: Message-driven bean
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/jms/queue/OrderQueue")
})
public class OrderProcessorMDB implements MessageListener {
    
    public void onMessage(Message message) {
        // Process message
    }
}

// VIOLATION: Entity bean (legacy, but still EJB)
@Entity
public class CustomerEntity {
    
    @Id
    private Long id;
    private String name;
}

// VIOLATION: EJB with transaction management
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PaymentServiceBean {
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void processPayment(double amount) {
        // Payment processing
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void logTransaction() {
        // Logging
    }
}

// VIOLATION: EJB with security annotations
@Stateless
@DeclareRoles({"admin", "user"})
@RolesAllowed({"admin"})
public class AdminServiceBean {
    
    @PermitAll
    public String getPublicInfo() {
        return "Public information";
    }
    
    @DenyAll
    public void restrictedMethod() {
        // Restricted
    }
}

// VIOLATION: EJB with interceptors
@Stateless
@Interceptors({LoggingInterceptor.class, SecurityInterceptor.class})
public class OrderServiceBean {
    
    public void createOrder(Order order) {
        // Create order
    }
}

// VIOLATION: Using @EJB injection
public class ClientBean {
    
    @EJB
    private UserServiceBean userService;
    
    @EJB(lookup = "java:global/myapp/OrderService")
    private OrderService orderService;
    
    public void doSomething() {
        userService.createUser("John");
    }
}

// VIOLATION: Local business interface
@Local
public interface UserServiceLocal {
    void createUser(String name);
    void deleteUser(Long id);
}

// VIOLATION: Remote business interface
@Remote
public interface UserServiceRemote {
    void createUser(String name);
}

// VIOLATION: Asynchronous EJB method
@Stateless
public class AsyncServiceBean {
    
    @Asynchronous
    public Future<String> processAsync() {
        return new AsyncResult<>("Result");
    }
    
    @Asynchronous
    public void fireAndForget() {
        // Fire and forget
    }
}

// VIOLATION: EJB with schedule
@Singleton
public class ScheduledTaskBean {
    
    @Schedule(hour = "2", minute = "0")
    public void performNightlyMaintenance() {
        // Maintenance task
    }
    
    @Schedules({
        @Schedule(dayOfWeek = "Mon", hour = "9"),
        @Schedule(dayOfWeek = "Fri", hour = "17")
    })
    public void weeklyReport() {
        // Report generation
    }
}

// VIOLATION: EJB with timer service
@Stateless
public class TimerServiceBean {
    
    @Resource
    private TimerService timerService;
    
    public void createTimer() {
        timerService.createTimer(10000, "MyTimer");
    }
    
    @Timeout
    public void handleTimeout(Timer timer) {
        // Handle timeout
    }
}

// NON-VIOLATION: Plain POJO (no EJB annotations)
public class UserService {
    
    private EntityManager em;
    
    public void createUser(String name) {
        // Business logic
    }
}

// NON-VIOLATION: Spring bean
@org.springframework.stereotype.Service
public class OrderService {
    
    @org.springframework.beans.factory.annotation.Autowired
    private OrderRepository repository;
    
    public void createOrder(Order order) {
        repository.save(order);
    }
}

// NON-VIOLATION: CDI bean without EJB
@jakarta.inject.Named
@jakarta.enterprise.context.RequestScoped
public class ProductService {
    
    public void saveProduct(Product product) {
        // Save product
    }
}

// NON-VIOLATION: Regular Java class
public class UtilityClass {
    
    public static String formatDate(Date date) {
        return date.toString();
    }
}

// Helper classes
interface ShoppingCart { }
interface MessageListener { void onMessage(Message message); }
interface Message { }
class LoggingInterceptor { }
class SecurityInterceptor { }
class Order { }
class OrderService { }
class Product { }
class OrderRepository { void save(Order o) { } }

