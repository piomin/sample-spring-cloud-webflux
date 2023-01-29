package pl.piomin.services.gateway;

import io.specto.hoverfly.junit.core.Hoverfly;
import io.specto.hoverfly.junit5.HoverflyExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.TimeUnit;

import static io.specto.hoverfly.junit.core.SimulationSource.dsl;
import static io.specto.hoverfly.junit.dsl.HoverflyDsl.service;
import static io.specto.hoverfly.junit.dsl.ResponseCreators.success;
import static io.specto.hoverfly.junit.dsl.matchers.HoverflyMatchers.startsWith;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(HoverflyExtension.class)
public class GatewayTests {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void findAccounts(Hoverfly hoverfly) {
        hoverfly.simulate(dsl(
            service("account-service:8080")
                .andDelay(200, TimeUnit.MILLISECONDS).forAll()
                .get(startsWith("/1"))
                .willReturn(success("[{\"id\":\"1\",\"number\":\"1234567890\",\"balance\":5000}]", "application/json"))));

        ResponseEntity<String> response = restTemplate
                .getForEntity("/account/1", String.class);
    }
}
