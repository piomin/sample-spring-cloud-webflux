package pl.piomin.services.gateway;

import io.specto.hoverfly.junit.core.Hoverfly;
import io.specto.hoverfly.junit.core.config.LogLevel;
import io.specto.hoverfly.junit5.HoverflyExtension;
import io.specto.hoverfly.junit5.api.HoverflyConfig;
import io.specto.hoverfly.junit5.api.HoverflyCore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.TimeUnit;

import static io.specto.hoverfly.junit.core.SimulationSource.dsl;
import static io.specto.hoverfly.junit.dsl.HoverflyDsl.service;
import static io.specto.hoverfly.junit.dsl.ResponseCreators.success;
import static io.specto.hoverfly.junit.dsl.matchers.HoverflyMatchers.any;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@HoverflyCore(config = @HoverflyConfig(logLevel = LogLevel.DEBUG, webServer = true, proxyPort = 8080))
@ExtendWith(HoverflyExtension.class)
@LoadBalancerClient(name = "account-service", configuration = AccountServiceConf.class)
public class GatewayTests {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void findAccounts(Hoverfly hoverfly) {
        hoverfly.simulate(dsl(
            service("http://localhost:8080")
                .andDelay(200, TimeUnit.MILLISECONDS).forAll()
                .get(any())
                .willReturn(success("[{\"id\":\"1\",\"number\":\"1234567890\",\"balance\":5000}]", "application/json"))));

        ResponseEntity<String> response = restTemplate
                .getForEntity("/account/1", String.class);
        Assertions.assertEquals(200, response.getStatusCodeValue());
        Assertions.assertNotNull(response.getBody());
    }
}
