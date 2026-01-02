package pl.piomin.services.account;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.piomin.services.account.model.Account;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"spring.cloud.discovery.enabled=false"})
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountControllerTests {

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
        Account account = new Account("123456", "1", 10000);
        webTestClient.post().uri("/")
                .bodyValue(account)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Account.class)
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
                .expectBody(Account.class)
                .value(account -> {
                    assertNotNull(account);
                    assertNotNull(account.getId());
                    assertEquals(id, account.getId());
                });
    }

    @Test
    @Order(2)
    void findAll() {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Account.class)
                .value(accounts -> {
                    assert accounts != null;
                    assertFalse(accounts.isEmpty());
                });
    }
}