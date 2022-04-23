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

@Slf4j
@Component
public class ServicePaymentHandler {

    @Autowired
    private ServicePaymentService servicePaymentService;

    public Mono<ServerResponse> getServicePayments(ServerRequest request) {
        String channel = request.queryParam("channel").isPresent()? request.queryParam("channel").get():null;
        return this.servicePaymentService.servicePaymentFindAll(channel)
                .collectList()
                .flatMap(s -> ServerResponse.ok().body(Mono.just(s), ServicePayment.class));
    }

    public Mono<ServerResponse> getServicePaymentHistory(ServerRequest request) {
        if(request.queryParam("clientId").isEmpty()) {
            return Mono.error(new ServicePaymentBaseException("QueryParam: 'ClientId' es obligatorio."));
        }
        Integer clientId = Integer.parseInt(request.queryParam("clientId").get());
        return this.servicePaymentService.servicePaymentHistoryFindByClientId(clientId)
                .collectList()
                .flatMap(s -> ServerResponse.ok().body(Mono.just(s), ServicePaymentHistory.class));
    }

    public Mono<ServerResponse> saveServicePaymentHistory(ServerRequest request) {
        return request.bodyToMono(ServicePaymentHistory.class)
                .flatMap(servicePaymentHistory -> this.servicePaymentService.servicePaymentHistorySave(servicePaymentHistory))
                .flatMap(servicePaymentHistory -> ServerResponse.ok().body(Mono.just(servicePaymentHistory), ServicePaymentHistory.class));
    }

    public Mono<ServerResponse> getServicePaymentFavorites(ServerRequest request) {
        if(request.queryParam("clientId").isEmpty()) {
            return Mono.error(new ServicePaymentBaseException("QueryParam: 'ClientId' es obligatorio."));
        }
        Integer clientId = Integer.parseInt(request.queryParam("clientId").get());
        return this.servicePaymentService.servicePaymentFavoriteFindByClientId(clientId)
                .collectList()
                .flatMap(s -> ServerResponse.ok().body(Mono.just(s), ServicePaymentFavorite.class));
    }

    public Mono<ServerResponse> saveServicePaymentFavorite(ServerRequest request) {
        return request.bodyToMono(ServicePaymentFavorite.class)
                .flatMap(servicePaymentFavorite -> this.servicePaymentService.servicePaymentFavoriteSave(servicePaymentFavorite))
                .flatMap(servicePaymentFavorite -> ServerResponse.ok().body(Mono.just(servicePaymentFavorite), ServicePaymentFavorite.class));
    }

    public Mono<ServerResponse> deleteServicePaymentFavorite(ServerRequest request) {
        String id = request.pathVariable("id");
        return this.servicePaymentService.servicePaymentFavoriteDelete(id)
                .then(ServerResponse.ok().build());
    }

}
