package bcp.bootcamp.uxbcpservicepayment.repositories.impl;

import bcp.bootcamp.uxbcpservicepayment.core.exceptions.ServicePaymentBaseException;
import bcp.bootcamp.uxbcpservicepayment.entities.ServicePaymentFavorite;
import bcp.bootcamp.uxbcpservicepayment.repositories.ServicePaymentFavoriteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Repository
public class ServicePaymentFavoriteRepositoryImpl implements ServicePaymentFavoriteRepository {

    private final WebClient client;

    public ServicePaymentFavoriteRepositoryImpl(
        WebClient.Builder builder,
        @Value("${application.urlApiBsServicePaymentFavorite}") String urlApiServicePaymentFavorite
    ) {
        //Configurar Response timeout
        HttpClient client = HttpClient.create().responseTimeout(Duration.ofSeconds(5));
        this.client = builder
                .baseUrl(urlApiServicePaymentFavorite)
                .clientConnector(new ReactorClientHttpConnector(client))
                .build();
    }

    @Override
    public Flux<ServicePaymentFavorite> servicePaymentFavoriteFindByClientId(Integer clientId) {
        String queryParam = Optional.ofNullable(clientId).map(id -> "?clientId=" + String.valueOf(id)).orElse("");
        return this.client.get().uri("/"+queryParam).accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new ServicePaymentBaseException("Server error")))
            .bodyToFlux(ServicePaymentFavorite.class)
            .retryWhen(
                Retry.fixedDelay(3, Duration.ofSeconds(2))
                    .doBeforeRetry(x->  log.info("LOG BEFORE RETRY=" + x))
                    .doAfterRetry(x->  log.info("LOG AFTER RETRY=" + x))
            )
            .doOnError(x-> log.info("LOG ERROR"));
    }

    @Override
    public Mono<Void> servicePaymentFavoriteDelete(String id) {
        return this.client.delete().uri(uriBuilder -> uriBuilder.path("/{id}").build(id)).accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is5xxServerError, response-> Mono.error(new ServicePaymentBaseException("Server error")))
                .bodyToMono(Void.class)
                .retryWhen(
                        Retry.fixedDelay(3, Duration.ofSeconds(2))
                                .doBeforeRetry(x->  log.info("LOG BEFORE RETRY=" + x))
                                .doAfterRetry(x->  log.info("LOG AFTER RETRY=" + x))
                )
                .doOnError(x-> log.info("LOG ERROR"))
                .doOnSuccess(x-> log.info("LOG SUCCESS"));
    }

    @Override
    public Mono<ServicePaymentFavorite> servicePaymentFavoriteSave(ServicePaymentFavorite servicePaymentFavorite) {
        return this.client.post().uri("/").accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(servicePaymentFavorite))
                .retrieve()
                .onStatus(HttpStatus::is5xxServerError, response-> Mono.error(new ServicePaymentBaseException("Server error")))
                .bodyToMono(ServicePaymentFavorite.class)
                .retryWhen(
                        Retry.fixedDelay(3, Duration.ofSeconds(2))
                                .doBeforeRetry(x->  log.info("LOG BEFORE RETRY=" + x))
                                .doAfterRetry(x->  log.info("LOG AFTER RETRY=" + x))
                )
                .doOnError(x-> log.info("LOG ERROR"))
                .doOnSuccess(x-> log.info("LOG SUCCESS"));
    }
}
