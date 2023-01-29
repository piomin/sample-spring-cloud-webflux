package pl.piomin.services.discovery;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class DiscoveryServerTests {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void findAccounts() {
        ResponseEntity<String> response = restTemplate
                .getForEntity("/", String.class);
        assertEquals(200, response.getStatusCodeValue());
    }
}
