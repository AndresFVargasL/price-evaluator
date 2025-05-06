package com.inditex.jpa.price;

import com.inditex.jpa.helper.AdapterOperations;
import com.inditex.model.price.Price;
import com.inditex.model.price.gateways.PricePersistencePort;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * JPA adapter that implements the PricePersistencePort interface.
 */
@Repository
public class PriceJPARepositoryAdapter extends AdapterOperations<Price, PriceEntity, Long, PriceJPARepository>
        implements PricePersistencePort {

    public PriceJPARepositoryAdapter(PriceJPARepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Price.class));
    }

    /**
     * Retrieves matching prices from the JPA repository.
     *
     * @param applicationDate the date of application
     * @param productId the product identifier
     * @param brandId the brand identifier
     * @return a Mono with the price or empty if not found
     */
    @Override
    @Cacheable(value = "prices" )
    public Mono<Price> findApplicablePrice(LocalDateTime applicationDate, Integer productId, Integer brandId) {
        return Mono.fromCallable(() -> repository.findApplicablePrices(applicationDate, productId, brandId))
                .flatMapMany(Flux::fromIterable)
                .next()
                .map(this::toEntity);
    }
}
