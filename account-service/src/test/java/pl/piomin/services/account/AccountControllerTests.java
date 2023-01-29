package pl.piomin.services.account;

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
import pl.piomin.services.account.model.Account;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountControllerTests {

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
        Account account = new Account("123456", "1", 10000);
        account = restTemplate.postForObject("/", account, Account.class);
        assertNotNull(account);
        assertNotNull(account.getId());
        id = account.getId();
    }

    @Test
    @Order(2)
    void findById() {
        Account account = restTemplate.getForObject("/{id}", Account.class, id);
        assertNotNull(account);
        assertNotNull(account.getId());
        assertEquals(id, account.getId());
    }

    @Test
    @Order(2)
    void findAll() {
        Account[] accounts = restTemplate.getForObject("/", Account[].class);
        assertTrue(accounts.length > 0);
    }
}
