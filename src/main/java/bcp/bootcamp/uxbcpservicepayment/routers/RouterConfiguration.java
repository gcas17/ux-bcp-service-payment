package bcp.bootcamp.uxbcpservicepayment.routers;

import bcp.bootcamp.uxbcpservicepayment.core.exceptions.GlobalExceptionHandler;
import bcp.bootcamp.uxbcpservicepayment.entities.ServicePayment;
import bcp.bootcamp.uxbcpservicepayment.entities.ServicePaymentFavorite;
import bcp.bootcamp.uxbcpservicepayment.entities.ServicePaymentHistory;
import bcp.bootcamp.uxbcpservicepayment.handlers.ServicePaymentHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class RouterConfiguration {

    @Bean
    @RouterOperations( {
            @RouterOperation (
                path = "/service-payment-favorite",
                produces = {MediaType.APPLICATION_JSON_VALUE},
                method = RequestMethod.GET,
                beanClass = ServicePaymentHandler.class,
                beanMethod = "getServicePaymentFavorites",
                operation = @Operation(
                    summary = "Listar servicios de pago favoritos",
                    description = "Listar servicios de pago favoritos",
                    operationId = "getServicePaymentFavorites",
                    responses = {
                        @ApiResponse(responseCode = "200",
                            description = "Operación exitosa",
                            content = @Content(array=@ArraySchema(schema = @Schema(implementation = ServicePaymentFavorite.class)))),
                        @ApiResponse(
                            responseCode = "404",
                            description = "No se encontró elementos",
                            content = @Content(schema = @Schema(implementation= GlobalExceptionHandler.HttpError.class))
                        )
                    },
                    parameters = {
                            @Parameter(in = ParameterIn.QUERY, name = "clientId", required = true)
                    }
                )
            ),
            @RouterOperation (
                path = "/service-payment-favorite",
                produces = {MediaType.APPLICATION_JSON_VALUE},
                method = RequestMethod.POST,
                beanClass = ServicePaymentHandler.class,
                beanMethod = "saveServicePaymentFavorite",
                operation = @Operation(
                    summary = "Guardar servicio favorito del cliente",
                    description="Guardar servicio favorito del cliente",
                    operationId = "saveServicePaymentFavorite",
                    responses = {
                        @ApiResponse(
                            responseCode = "200",
                            description = "Operación exitosa",
                            content = @Content(schema = @Schema(implementation = ServicePaymentFavorite.class))
                        )
                    },
                    parameters = {},
                    requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = ServicePaymentFavorite.class)))
                )
            ),
            @RouterOperation (
                path = "/service-payment-favorite/{id}",
                produces = {MediaType.APPLICATION_JSON_VALUE},
                method = RequestMethod.DELETE,
                beanClass = ServicePaymentHandler.class,
                beanMethod = "deleteServicePaymentFavorite",
                operation = @Operation(
                    summary = "Eliminar servicio favorito del cliente",
                    description="Eliminar servicio favorito del cliente",
                    operationId = "deleteServicePaymentFavorite",
                    responses = {
                        @ApiResponse(
                            responseCode = "200",
                            description = "Operación exitosa",
                            content = @Content(schema = @Schema(implementation = ServicePaymentFavorite.class))
                        ),
                        @ApiResponse(responseCode = "404", description = "No se encontró el servicio de pago")
                    },
                    parameters = {
                        @Parameter(in = ParameterIn.PATH, name = "id")
                    },
                    requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = ServicePaymentFavorite.class)))
                )
            )
    })
    public RouterFunction<ServerResponse> servicePaymentFavoriteRoutes(ServicePaymentHandler servicePaymentFavoriteHandler) {
        return RouterFunctions.nest(RequestPredicates.path("/service-payment-favorite"),
            RouterFunctions
                .route(GET(""), servicePaymentFavoriteHandler::getServicePaymentFavorites)
                .andRoute(POST("").and(contentType(APPLICATION_JSON)), servicePaymentFavoriteHandler::saveServicePaymentFavorite)
                .andRoute(DELETE("/{id}"), servicePaymentFavoriteHandler::deleteServicePaymentFavorite)
        );
    }

    @Bean
    @RouterOperations( {
        @RouterOperation (
            path = "/service-payment",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            method = RequestMethod.GET,
            beanClass = ServicePaymentHandler.class,
            beanMethod = "getServicePayments",
            operation = @Operation(
                summary = "Listar servicios de pago",
                description = "Listar servicios de pago",
                operationId = "getServicePayments",
                responses = {
                    @ApiResponse(responseCode = "200",
                        description = "Operación exitosa",
                        content = @Content(array=@ArraySchema(schema = @Schema(implementation = ServicePayment.class)))),
                    @ApiResponse(
                        responseCode = "404",
                        description = "No se encontró elementos",
                        content = @Content(schema = @Schema(implementation= GlobalExceptionHandler.HttpError.class))
                    )
                },
                parameters = {
                        @Parameter(in = ParameterIn.QUERY, name = "channel", required = false)
                }
            )
        ),
        @RouterOperation (
            path = "/service-payment/history",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            method = RequestMethod.GET,
            beanClass = ServicePaymentHandler.class,
            beanMethod = "getServicePaymentHistory",
            operation = @Operation(
                summary = "Listar historial de servicios de pago del cliente",
                description = "Listar historial de servicios de pago del cliente",
                operationId = "getServicePaymentHistory",
                responses = {
                    @ApiResponse(responseCode = "200",
                        description = "Operación exitosa",
                        content = @Content(array=@ArraySchema(schema = @Schema(implementation = ServicePaymentHistory.class)))),
                    @ApiResponse(
                        responseCode = "404",
                        description = "No se encontró elementos",
                        content = @Content(schema = @Schema(implementation= GlobalExceptionHandler.HttpError.class))
                    )
                },
                parameters = {
                    @Parameter(in = ParameterIn.QUERY, name = "clientId", required = true)
                }
            )
        ),
        @RouterOperation (
            path = "/service-payment",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            method = RequestMethod.POST,
            beanClass = ServicePaymentHandler.class,
            beanMethod = "saveServicePaymentHistory",
            operation = @Operation(
                summary = "Realizar pago de servicio",
                description="Realizar pago de servicio",
                operationId = "saveServicePaymentHistory",
                responses = {
                    @ApiResponse(
                        responseCode = "200",
                        description = "Operación exitosa",
                        content = @Content(schema = @Schema(implementation = ServicePaymentHistory.class))
                    )
                },
                parameters = {},
                requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = ServicePaymentHistory.class))))
            )
    })
    public RouterFunction<ServerResponse> servicePaymentRoutes(ServicePaymentHandler servicePaymentHandler) {
        return RouterFunctions.nest(RequestPredicates.path("/service-payment"),
            RouterFunctions
                .route(GET(""), servicePaymentHandler::getServicePayments)
                .andRoute(GET("/history"), servicePaymentHandler::getServicePaymentHistory)
                .andRoute(POST("").and(contentType(APPLICATION_JSON)), servicePaymentHandler::saveServicePaymentHistory)
        );
    }
}
