package com.inditex.model.price.gateways;

import com.inditex.model.price.Price;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Port interface for retrieving prices from the persistence layer.
 */
public interface PricePersistencePort {

    /**
     * Finds all prices matching the given product and brand.
     *
     * @param applicationDate the date of application
     * @param productId the product identifier
     * @param brandId the brand identifier
     * @return list of matching prices
     */
    Mono<Price> findApplicablePrice(LocalDateTime applicationDate, Integer productId, Integer brandId);
}
