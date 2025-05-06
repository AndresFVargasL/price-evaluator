package com.inditex.usecase.price;

import com.inditex.model.price.Price;
import com.inditex.model.price.gateways.PricePersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

class PriceUseCaseTest {

    private PricePersistencePort port;
    private PriceUseCase useCase;

    @BeforeEach
    void setUp() {
        port = mock(PricePersistencePort.class);
        useCase = new PriceUseCase(port);
    }

    @Test
    void shouldReturnPriceWhenFound() {
        // Arrange
        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 16, 0);
        Integer productId = 35455;
        Integer brandId = 1;
        Price expectedPrice = Price.builder()
            .id(4L)
            .brandId(1)
            .startDate(LocalDateTime.parse("2020-06-15T16:00:00"))
            .endDate(LocalDateTime.parse("2020-12-31T23:59:59"))
            .priceList(4)
            .productId(35455)
            .priority(1)
            .price(new BigDecimal("38.95"))
            .curr("EUR")
            .build();

        when(port.findApplicablePrice(date, productId, brandId))
            .thenReturn(Mono.just(expectedPrice));

        // Act & Assert
        StepVerifier.create(useCase.findPrice(date, productId, brandId))
            .expectNext(expectedPrice)
            .verifyComplete();

        verify(port).findApplicablePrice(date, productId, brandId);
    }

    @Test
    void shouldReturnEmptyWhenNoPriceFound() {
        // Arrange
        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 16, 0);
        Integer productId = 35455;
        Integer brandId = 1;

        when(port.findApplicablePrice(date, productId, brandId))
            .thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(useCase.findPrice(date, productId, brandId))
            .verifyComplete();

        verify(port).findApplicablePrice(date, productId, brandId);
    }
}
