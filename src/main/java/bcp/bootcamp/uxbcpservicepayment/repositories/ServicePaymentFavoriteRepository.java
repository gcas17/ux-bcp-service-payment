package bcp.bootcamp.uxbcpservicepayment.repositories;

import bcp.bootcamp.uxbcpservicepayment.entities.ServicePaymentFavorite;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ServicePaymentFavoriteRepository {

    Mono<Void> servicePaymentFavoriteDelete(String id);

    Mono<ServicePaymentFavorite> servicePaymentFavoriteSave(ServicePaymentFavorite servicePaymentFavorite);

    Flux<ServicePaymentFavorite> servicePaymentFavoriteFindByClientId(Integer clientId);

}
