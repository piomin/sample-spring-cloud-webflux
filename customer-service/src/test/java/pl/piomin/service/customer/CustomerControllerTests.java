package pl.piomin.service.customer;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.piomin.service.customer.model.Customer;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
   properties = {"spring.cloud.discovery.enabled=false"})
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerControllerTests {

    static String id;

    @Container
    static MongoDBContainer mongodb = new MongoDBContainer("mongo:4.4");

    @DynamicPropertySource
    static void registerMongoProperties(DynamicPropertyRegistry registry) {
        String uri = mongodb.getConnectionString() + "/test";
        registry.add("spring.data.mongodb.uri", () -> uri);
    }

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    @Order(1)
    void add() {
        Customer customer = new Customer("Test1", "Test2", 10);
        customer = restTemplate.postForObject("/", customer, Customer.class);
        assertNotNull(customer);
        assertNotNull(customer.getId());
        id = customer.getId();
    }

    @Test
    @Order(2)
    void findById() {
        Customer customer = restTemplate.getForObject("/{id}", Customer.class, id);
        assertNotNull(customer);
        assertNotNull(customer.getId());
        assertEquals(id, customer.getId());
    }

    @Test
    @Order(2)
    void findAll() {
        Customer[] customers = restTemplate.getForObject("/", Customer[].class);
        assertTrue(customers.length > 0);
    }
}
