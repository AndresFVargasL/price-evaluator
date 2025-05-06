package com.inditex.jpa.price;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.time.LocalDateTime;
import java.util.List;

public interface PriceJPARepository
    extends CrudRepository<PriceEntity, Long>, QueryByExampleExecutor<PriceEntity> {
    @Query("""
        SELECT p FROM PriceEntity p
        WHERE p.productId = :productId
          AND p.brandId = :brandId
          AND :applicationDate BETWEEN p.startDate AND p.endDate
        ORDER BY p.priority DESC
        """)
    List<PriceEntity> findApplicablePrices(
        @Param("applicationDate") LocalDateTime applicationDate,
        @Param("productId") Integer productId,
        @Param("brandId") Integer brandId
    );
}
