package bcp.bootcamp.uxbcpservicepayment.repositories;

import bcp.bootcamp.uxbcpservicepayment.entities.ServicePaymentFavorite;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ServicePaymentFavoriteRepository {

    Mono<Void> servicePaymentFavoriteDelete(String id, String token);

    Mono<ServicePaymentFavorite> servicePaymentFavoriteSave(ServicePaymentFavorite servicePaymentFavorite, String token);

    Flux<ServicePaymentFavorite> servicePaymentFavoriteFindByClientId(Integer clientId, String token);

}
