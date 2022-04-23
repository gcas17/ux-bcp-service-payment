package bcp.bootcamp.uxbcpservicepayment.services;

import bcp.bootcamp.uxbcpservicepayment.entities.ServicePayment;
import bcp.bootcamp.uxbcpservicepayment.entities.ServicePaymentFavorite;
import bcp.bootcamp.uxbcpservicepayment.entities.ServicePaymentHistory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ServicePaymentService {

    Flux<ServicePayment> servicePaymentFindAll(Integer id, String channel, String token);

    Flux<ServicePaymentHistory> servicePaymentHistoryFindByClientId(Integer clientId, String token);

    Mono<ServicePaymentHistory> servicePaymentHistorySave(ServicePaymentHistory servicePaymentHistory, String token);

    Flux<ServicePaymentFavorite> servicePaymentFavoriteFindByClientId(Integer clientId, String token);

    Mono<ServicePaymentFavorite> servicePaymentFavoriteSave(ServicePaymentFavorite servicePaymentFavorite, String token);

    Mono<Void> servicePaymentFavoriteDelete(String id, String token);

}
