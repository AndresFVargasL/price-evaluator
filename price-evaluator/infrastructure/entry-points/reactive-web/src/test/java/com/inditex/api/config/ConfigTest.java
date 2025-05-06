package com.inditex.api.config;

import com.inditex.api.Handler;
import com.inditex.api.RouterRest;
import com.inditex.model.price.Price;
import com.inditex.model.price.gateways.PricePersistencePort;
import com.inditex.usecase.price.PriceUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@ContextConfiguration(classes = {RouterRest.class, Handler.class})
@WebFluxTest
@Import({CorsConfig.class, SecurityHeadersConfig.class})
@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = "cors.allowed-origins=http://any-origin.com,http://any-other-origin.com")
class ConfigTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private PriceUseCase useCase;

    @MockitoBean
    private PricePersistencePort port;

    @Test
    void corsConfigurationShouldAllowOrigins() {
        //Arrange
        when(useCase.findPrice(any(), any(), any())).thenReturn(Mono.just(Price.builder().build()));
        //Act & Assert
        webTestClient
            .get()
            .uri(builder -> builder
                .path("/api/prices")
                .queryParam("applicationDate", "2020-06-15T16:00:00")
                .queryParam("productId", "35455")
                .queryParam("brandId", "1")
                .build())
            .exchange()
            .expectStatus().isOk()
            .expectHeader().valueEquals("Content-Security-Policy",
                "default-src 'self'; frame-ancestors 'self'; form-action 'self'")
            .expectHeader().valueEquals("Strict-Transport-Security", "max-age=31536000;")
            .expectHeader().valueEquals("X-Content-Type-Options", "nosniff")
            .expectHeader().doesNotExist("Server")
            .expectHeader().valueEquals("Cache-Control", "no-store")
            .expectHeader().valueEquals("Pragma", "no-cache")
            .expectHeader().valueEquals("Referrer-Policy", "strict-origin-when-cross-origin");
    }

    @Test
    void shouldAllowCorsPreflightRequest() {
        //Arrange, Act & Assert
        webTestClient
            .options()
            .uri("http://localhost/api/prices")
            .header("Origin", "http://any-origin.com")
            .header("Access-Control-Request-Method", "GET")
            .exchange()
            .expectStatus().isOk()
            .expectHeader().valueEquals("Access-Control-Allow-Origin", "http://any-origin.com")
            .expectHeader().valueEquals("Access-Control-Allow-Credentials", "true")
            .expectHeader().valueEquals("Access-Control-Allow-Methods", "GET,OPTIONS");
    }

    @Test
    void shouldApplyCorsToActualGetRequest() {
        //Arrange
        when(useCase.findPrice(any(), any(), any())).thenReturn(Mono.just(Price.builder().build()));
        //Act & Assert
        webTestClient
            .get()
            .uri(builder -> builder
                .scheme("http")
                .host("localhost")
                .path("/api/prices")
                .queryParam("applicationDate", "2020-06-15T16:00:00")
                .queryParam("productId", "35455")
                .queryParam("brandId", "1")
                .build())
            .header("Origin", "http://any-other-origin.com")
            .exchange()
            .expectStatus().isOk()
            .expectHeader().valueEquals("Access-Control-Allow-Origin", "http://any-other-origin.com")
            .expectHeader().valueEquals("Access-Control-Allow-Credentials", "true");
    }

}