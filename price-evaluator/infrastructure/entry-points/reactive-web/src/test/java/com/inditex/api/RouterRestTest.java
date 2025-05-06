package com.inditex.api;

import com.inditex.model.price.Price;
import com.inditex.model.price.gateways.PricePersistencePort;
import com.inditex.usecase.price.PriceUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {RouterRest.class, Handler.class})
@WebFluxTest
@ExtendWith(MockitoExtension.class)
class RouterRestTest {

    @Autowired
    private Handler handler;

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private PriceUseCase useCase;

    @MockitoBean
    private PricePersistencePort port;

    @Test
    @SuppressWarnings("all")
    void testRouterFunctionBean() {
        RouterRest routerRest = new RouterRest();
        RouterFunction<ServerResponse> routerFunction = routerRest.routerFunction(handler);
        assertNotNull(routerFunction, "RouterFunction should not be null");
    }

    @Test
    void testPricesApiGivenSuccessfulRequestShouldAnswerOk() {
        //Arrange
        when(useCase.findPrice(any(), any(), any())).thenReturn(Mono.just(Price.builder()
            .id(4L)
            .brandId(1)
            .startDate(LocalDateTime.parse("2020-06-15T16:00:00"))
            .endDate(LocalDateTime.parse("2020-12-31T23:59:59"))
            .priceList(4)
            .productId(35455)
            .priority(1)
            .price(new BigDecimal("38.95"))
            .curr("EUR")
            .build()));
        //Act & Assert
        webTestClient.get()
            .uri(builder -> builder
                .path("/api/prices")
                .queryParam("applicationDate", "2020-06-15T16:00:00")
                .queryParam("productId", "35455")
                .queryParam("brandId", "1")
                .build())
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.id").isNotEmpty()
            .jsonPath("$.brandId").isNotEmpty()
            .jsonPath("$.startDate").isNotEmpty()
            .jsonPath("$.endDate").isNotEmpty()
            .jsonPath("$.priceList").isNotEmpty()
            .jsonPath("$.productId").isNotEmpty()
            .jsonPath("$.priority").isNotEmpty()
            .jsonPath("$.price").isNotEmpty()
            .jsonPath("$.curr").isNotEmpty();
    }

    @Test
    void testPricesApiGivenInvalidApplicationDateShouldThrowError() {
        //Arrange
        when(useCase.findPrice(any(), any(), any())).thenReturn(Mono.just(Price.builder()
            .id(4L)
            .brandId(1)
            .startDate(LocalDateTime.parse("2020-06-15T16:00:00"))
            .endDate(LocalDateTime.parse("2020-12-31T23:59:59"))
            .priceList(4)
            .productId(35455)
            .priority(1)
            .price(new BigDecimal("38.95"))
            .curr("EUR")
            .build()));
        //Act & Assert
        webTestClient.get()
            .uri(builder -> builder
                .path("/api/prices")
                .queryParam("applicationDate", "Invalid Date")
                .queryParam("productId", "35455")
                .queryParam("brandId", "1")
                .build())
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    void testPricesApiGivenInvalidProductShouldThrowError() {
        //Act & Assert
        webTestClient.get()
            .uri(builder -> builder
                .path("/api/prices")
                .queryParam("applicationDate", "2020-06-15T16:00:00")
                .queryParam("productId", "Error")
                .queryParam("brandId", "1")
                .build())
            .exchange()
            .expectStatus().isBadRequest();
    }
}
