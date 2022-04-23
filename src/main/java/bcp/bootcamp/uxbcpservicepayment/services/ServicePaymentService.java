package bcp.bootcamp.uxbcpservicepayment.services;

import bcp.bootcamp.uxbcpservicepayment.entities.ServicePayment;
import bcp.bootcamp.uxbcpservicepayment.entities.ServicePaymentFavorite;
import bcp.bootcamp.uxbcpservicepayment.entities.ServicePaymentHistory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ServicePaymentService {

    Flux<ServicePayment> servicePaymentFindAll(String channel);

    Flux<ServicePaymentHistory> servicePaymentHistoryFindByClientId(Integer clientId);

    Mono<ServicePaymentHistory> servicePaymentHistorySave(ServicePaymentHistory servicePaymentHistory);

    Flux<ServicePaymentFavorite> servicePaymentFavoriteFindByClientId(Integer clientId);

    Mono<ServicePaymentFavorite> servicePaymentFavoriteSave(ServicePaymentFavorite servicePaymentFavorite);

    Mono<Void> servicePaymentFavoriteDelete(String id);

}
