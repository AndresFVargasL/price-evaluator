package com.inditex.jpa.helper;

import com.inditex.jpa.price.PriceEntity;
import com.inditex.jpa.price.PriceJPARepository;
import com.inditex.jpa.price.PriceJPARepositoryAdapter;
import com.inditex.model.price.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Example;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AdapterOperationsTest {

    @Mock
    private PriceJPARepository repository;

    @Mock
    private ObjectMapper objectMapper;

    private PriceJPARepositoryAdapter adapter;

    private Price model;
    private PriceEntity jpaEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        model = Price.builder()
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

        jpaEntity = PriceEntity.builder()
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

        when(objectMapper.map(jpaEntity, Price.class)).thenReturn(model);
        when(objectMapper.map(model, PriceEntity.class)).thenReturn(jpaEntity);

        adapter = new PriceJPARepositoryAdapter(repository, objectMapper);
    }

    @Test
    void testSave() {

        when(repository.save(jpaEntity)).thenReturn(jpaEntity);

        var result = adapter.save(model);

        assertEquals(result.getId(), jpaEntity.getId());
    }

    @Test
    void testSaveAllEntities() {

        var objectValues = List.of(jpaEntity);

        when(repository.saveAll(objectValues)).thenReturn(objectValues);

        var result = adapter.saveAllEntities(List.of(model));

        assertFalse(result.isEmpty());
    }

    @Test
    void testFindById() {

        var id = 1L;
        when(repository.findById(id)).thenReturn(Optional.of(jpaEntity));

        var result = adapter.findById(id);

        assertEquals(result.getId(), jpaEntity.getId());
    }

    @Test
    void testFindAll() {

        when(repository.findAll()).thenReturn(List.of(jpaEntity));

        var result = adapter.findAll();

        assertFalse(result.isEmpty());
    }

    @Test
    void testFindByExample() {

        when(repository.findAll(any(Example.class))).thenReturn(List.of(jpaEntity));

        var result = adapter.findByExample(model);

        assertEquals(result.getFirst().getId(), model.getId());
    }

    @Test
    void shouldReturnMappedPriceFromRepository() {
        // Arrange
        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 16, 0);
        Integer productId = 35455;
        Integer brandId = 1;

        PriceEntity entity = new PriceEntity();
        Price expectedPrice = model;

        when(repository.findApplicablePrices(date, productId, brandId))
            .thenReturn(List.of(entity));
        when(objectMapper.map(entity, Price.class)).thenReturn(expectedPrice);

        // Act & Assert
        StepVerifier.create(adapter.findApplicablePrice(date, productId, brandId))
            .expectNext(expectedPrice)
            .verifyComplete();

        verify(repository).findApplicablePrices(date, productId, brandId);
        verify(objectMapper).map(entity, Price.class);
    }

    @Test
    void shouldReturnEmptyMonoWhenRepositoryReturnsEmptyList() {
        // Arrange
        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 16, 0);
        Integer productId = 35455;
        Integer brandId = 1;

        when(repository.findApplicablePrices(date, productId, brandId))
            .thenReturn(List.of());

        // Act & Assert
        StepVerifier.create(adapter.findApplicablePrice(date, productId, brandId))
            .verifyComplete();

        verify(repository).findApplicablePrices(date, productId, brandId);
    }
}
