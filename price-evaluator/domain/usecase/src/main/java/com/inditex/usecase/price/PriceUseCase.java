package com.inditex.usecase.price;

import com.inditex.model.price.Price;
import com.inditex.model.price.gateways.PricePersistencePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Business logic for retrieving the applicable price.
 */
@RequiredArgsConstructor
public class PriceUseCase {
    private final PricePersistencePort pricePersistencePort;

    /**
     * Finds the applicable price based on date, product, and brand.
     *
     * @param applicationDate the date of application
     * @param productId the product identifier
     * @param brandId the brand identifier
     * @return a Mono with the price or empty if not found
     */
    public Mono<Price> findPrice(LocalDateTime applicationDate, Integer productId, Integer brandId) {
        return pricePersistencePort.findApplicablePrice(applicationDate, productId, brandId);
    }
}
