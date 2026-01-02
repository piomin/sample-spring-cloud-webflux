package pl.piomin.service.customer;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.ApplicationContext;
import org.springframework.test.web.reactive.server.WebTestClient;
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
    @ServiceConnection
    static MongoDBContainer mongodb = new MongoDBContainer("mongo:6.0");

    WebTestClient webTestClient;

    @BeforeEach
    void setUp(ApplicationContext context) {
        webTestClient = WebTestClient.bindToApplicationContext(context).build();
    }

    @Test
    @Order(1)
    void add() {
        Customer customer = new Customer("Test1", "Test2", 10);
        webTestClient.post().uri("/")
                .bodyValue(customer)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Customer.class)
                .value(response -> {
                    assertNotNull(response);
                    assertNotNull(response.getId());
                    id = response.getId();
                });
    }

    @Test
    @Order(2)
    void findById() {
        webTestClient.get().uri("/{id}", id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Customer.class)
                .value(customer -> {
                    assertNotNull(customer);
                    assertNotNull(customer.getId());
                    assertEquals(id, customer.getId());
                });
    }

    @Test
    @Order(2)
    void findAll() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Customer.class)
                .value(customers -> assertFalse(customers.isEmpty()));
    }
}