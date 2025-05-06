package com.inditex.api;

import com.inditex.usecase.price.PriceUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Handles incoming requests and delegates to the use case.
 */
@Component
@RequiredArgsConstructor
public class Handler {

    private final PriceUseCase useCase;

    /**
     * Handles GET requests for applicable product prices.
     *
     * @param serverRequest the request containing query parameters
     * @return a response containing the applicable price or an error
     */
    @Operation(
        operationId = "getPrice",
        summary = "Get the applicable product price based on date, product ID, and brand ID",
        parameters = {
            @Parameter(name = "applicationDate", in = ParameterIn.QUERY, required = true, example = "2020-06-14T16:00:00"),
            @Parameter(name = "productId", in = ParameterIn.QUERY, required = true, example = "35455"),
            @Parameter(name = "brandId", in = ParameterIn.QUERY, required = true, example = "1")
        },
        responses = {
            @ApiResponse(responseCode = "200", description = "Price found"),
            @ApiResponse(responseCode = "400", description = "Missing or invalid parameters"),
            @ApiResponse(responseCode = "404", description = "Price not found")
        }
    )
    public Mono<ServerResponse> getPrice(ServerRequest serverRequest) {
        var dateParam = serverRequest.queryParam("applicationDate");
        var productIdParam = serverRequest.queryParam("productId");
        var brandIdParam = serverRequest.queryParam("brandId");

        LocalDateTime applicationDate = null;
        int productId = 0;
        int brandId = 0;
        try {
            applicationDate = LocalDateTime.parse(dateParam.get());
            productId = Integer.parseInt(productIdParam.get());
            brandId = Integer.parseInt(brandIdParam.get());
        } catch (NumberFormatException e) {
            return ServerResponse.badRequest()
                .bodyValue("Invalid value in one or more required query parameters.");
        }

        return useCase.findPrice(applicationDate, productId, brandId)
                .flatMap(priceResponse -> ServerResponse.ok().bodyValue(priceResponse))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
