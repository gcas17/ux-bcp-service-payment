package bcp.bootcamp.uxbcpservicepayment.handlers;

import bcp.bootcamp.uxbcpservicepayment.core.exceptions.ServicePaymentBaseException;
import bcp.bootcamp.uxbcpservicepayment.entities.ServicePayment;
import bcp.bootcamp.uxbcpservicepayment.entities.ServicePaymentFavorite;
import bcp.bootcamp.uxbcpservicepayment.entities.ServicePaymentHistory;
import bcp.bootcamp.uxbcpservicepayment.services.ServicePaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Slf4j
@Component
public class ServicePaymentHandler {

    @Autowired
    private ServicePaymentService servicePaymentService;

    public Mono<ServerResponse> getServicePayments(ServerRequest request) {
        String token = request.headers().header("Authorization").get(0);
        Integer id = request.queryParam("id").isPresent()? Integer.parseInt(request.queryParam("id").get()):null;
        String channel = request.queryParam("channel").isPresent()? request.queryParam("channel").get():null;
        return this.servicePaymentService.servicePaymentFindAll(id, channel, token)
                .collectList()
                .flatMap(s -> ServerResponse.ok().body(Mono.just(s), ServicePayment.class));
    }

    public Mono<ServerResponse> getServicePaymentHistory(ServerRequest request) {
        String token = request.headers().header("Authorization").get(0);
        if(request.queryParam("clientId").isEmpty()) {
            return Mono.error(new ServicePaymentBaseException("QueryParam: 'ClientId' es obligatorio."));
        }
        Integer clientId = Integer.parseInt(request.queryParam("clientId").get());
        return this.servicePaymentService.servicePaymentHistoryFindByClientId(clientId, token)
                .collectList()
                .flatMap(s -> ServerResponse.ok().body(Mono.just(s), ServicePaymentHistory.class));
    }

    public Mono<ServerResponse> saveServicePaymentHistory(ServerRequest request) {
        String token = request.headers().header("Authorization").get(0);
        return request.bodyToMono(ServicePaymentHistory.class)
                .flatMap(servicePaymentHistory -> {
                    return this.servicePaymentService.servicePaymentFindAll(servicePaymentHistory.getServicePaymentId(), servicePaymentHistory.getChannel(), token)
                        .hasElements()
                        .flatMap(exists -> {
                            if(servicePaymentHistory.getAddToFavorites().equals(true) && (servicePaymentHistory.getFavoriteName() == null || servicePaymentHistory.getFavoriteType() == null)) {
                                return  Mono.error(new ServicePaymentBaseException(HttpStatus.BAD_REQUEST, "Para agregar el servicio a favoritos es necesario enviar nombre y el tipo de favoritos."));
                            }
                            if(exists)  {
                                return this.servicePaymentService.servicePaymentFavoriteSave(
                                    new ServicePaymentFavorite(
                                        null,
                                        servicePaymentHistory.getClientId(),
                                        servicePaymentHistory.getServicePaymentId(),
                                        servicePaymentHistory.getFavoriteName(),
                                        servicePaymentHistory.getFavoriteType(),
                                        LocalDate.now()
                                    ), token)
                                    .hasElement()
                                    .flatMap(existsFav -> existsFav? this.servicePaymentService.servicePaymentHistorySave(servicePaymentHistory, token) : Mono.empty());
                            } else {
                                return Mono.empty();
                            }
                        });
                })
                .switchIfEmpty(Mono.error(new ServicePaymentBaseException(HttpStatus.BAD_REQUEST, "El servicio de pago ingresado no acepta el tipo de canal seleccionado")))
                .flatMap(servicePaymentHistory -> ServerResponse.ok().body(Mono.just(servicePaymentHistory), ServicePaymentHistory.class));
    }

    public Mono<ServerResponse> getServicePaymentFavorites(ServerRequest request) {
        String token = request.headers().header("Authorization").get(0);
        if(request.queryParam("clientId").isEmpty()) {
            return Mono.error(new ServicePaymentBaseException("QueryParam: 'ClientId' es obligatorio."));
        }
        Integer clientId = Integer.parseInt(request.queryParam("clientId").get());
        return this.servicePaymentService.servicePaymentFavoriteFindByClientId(clientId, token)
                .collectList()
                .flatMap(s -> ServerResponse.ok().body(Mono.just(s), ServicePaymentFavorite.class));
    }

    public Mono<ServerResponse> saveServicePaymentFavorite(ServerRequest request) {
        String token = request.headers().header("Authorization").get(0);
        return request.bodyToMono(ServicePaymentFavorite.class)
                .flatMap(servicePaymentFavorite -> this.servicePaymentService.servicePaymentFavoriteSave(servicePaymentFavorite, token))
                .flatMap(servicePaymentFavorite -> ServerResponse.ok().body(Mono.just(servicePaymentFavorite), ServicePaymentFavorite.class));
    }

    public Mono<ServerResponse> deleteServicePaymentFavorite(ServerRequest request) {
        String token = request.headers().header("Authorization").get(0);
        String id = request.pathVariable("id");
        return this.servicePaymentService.servicePaymentFavoriteDelete(id, token)
                .then(ServerResponse.ok().build());
    }

}
