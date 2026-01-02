package pl.piomin.services.gateway;

import io.specto.hoverfly.junit.core.Hoverfly;
import io.specto.hoverfly.junit.core.config.LogLevel;
import io.specto.hoverfly.junit5.HoverflyExtension;
import io.specto.hoverfly.junit5.api.HoverflyConfig;
import io.specto.hoverfly.junit5.api.HoverflyCore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.ApplicationContext;
import org.springframework.test.web.reactive.server.WebTestClient;

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

    WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:8080/")
                .build();
    }

//    @Test
    public void findAccounts(Hoverfly hoverfly) {
        hoverfly.simulate(dsl(
                service("http://localhost:8080")
                        .andDelay(200, TimeUnit.MILLISECONDS).forAll()
                        .get(any())
                        .willReturn(success("[{\"id\":\"1\",\"number\":\"1234567890\",\"balance\":5000}]", "application/json"))));

        webTestClient.get().uri("/account/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo("1")
                .jsonPath("$.number").isEqualTo("1234567890")
                .jsonPath("$.balance").isEqualTo(5000);
    }
}
