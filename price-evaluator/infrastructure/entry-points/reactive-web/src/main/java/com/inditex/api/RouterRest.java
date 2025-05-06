package com.inditex.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.queryParam;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * Defines the routing logic using Spring WebFlux functional style.
 */
@Configuration
public class RouterRest {

    /**
     * Creates the route for handling price evaluation.
     *
     * @param handler the handler that processes the request
     * @return router function for HTTP requests
     */
    @RouterOperations({
        @RouterOperation(
            path = "/api/prices",
            produces = { "application/json" },
            method = RequestMethod.GET,
            operation = @Operation(
                operationId = "getPrice",
                summary = "Get the applicable product price based on date, product ID, and brand ID",
                parameters = {
                    @Parameter(name = "applicationDate", in = ParameterIn.QUERY, required = true, example = "2020-06-14T16:00:00", schema = @Schema(type = "string", defaultValue = "2020-06-14T16:00:00")),
                    @Parameter(name = "productId", in = ParameterIn.QUERY, required = true, example = "35455", schema = @Schema(type = "integer", defaultValue = "35455")),
                    @Parameter(name = "brandId", in = ParameterIn.QUERY, required = true, example = "1", schema = @Schema(type = "integer", defaultValue = "1"))
                },
                responses = {
                    @ApiResponse(responseCode = "200", description = "Price found"),
                    @ApiResponse(responseCode = "400", description = "Missing or invalid parameters"),
                    @ApiResponse(responseCode = "404", description = "Price not found")
                }
            )
        )
    })
    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(GET("/api/prices")
                .and(queryParam("applicationDate", this::isIsoDateTime))
                .and(queryParam("productId", s -> true))
                .and(queryParam("brandId", s -> true)),
            handler::getPrice
        );
    }

    /**
     * Validates that a given query parameter is in ISO 8601 datetime format.
     *
     * @param dateString the date-time string
     * @return true if valid, false otherwise
     */
    private boolean isIsoDateTime(String dateString) {
        try {
            LocalDateTime.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
