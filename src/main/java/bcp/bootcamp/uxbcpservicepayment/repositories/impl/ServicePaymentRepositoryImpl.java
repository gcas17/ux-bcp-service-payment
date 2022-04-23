package bcp.bootcamp.uxbcpservicepayment.repositories.impl;

import bcp.bootcamp.uxbcpservicepayment.core.exceptions.ServicePaymentBaseException;
import bcp.bootcamp.uxbcpservicepayment.entities.ServicePayment;
import bcp.bootcamp.uxbcpservicepayment.entities.ServicePaymentFavorite;
import bcp.bootcamp.uxbcpservicepayment.entities.ServicePaymentHistory;
import bcp.bootcamp.uxbcpservicepayment.repositories.ServicePaymentRepository;
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
public class ServicePaymentRepositoryImpl implements ServicePaymentRepository {

    private final WebClient client;

    public ServicePaymentRepositoryImpl(
        WebClient.Builder builder,
        @Value("${application.urlApiBsServicePayment}") String urlApiServicePayment
    ) {
        //Configurar Response timeout
        HttpClient client = HttpClient.create().responseTimeout(Duration.ofSeconds(5));
        this.client = builder
                .baseUrl(urlApiServicePayment)
                .clientConnector(new ReactorClientHttpConnector(client))
                .build();
    }

    @Override
    public Mono<ServicePaymentHistory> servicePaymentHistorySave(ServicePaymentHistory servicePaymentHistory) {
        return this.client.post().uri("/").accept(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(servicePaymentHistory))
            .retrieve()
            .onStatus(HttpStatus::is5xxServerError, response-> Mono.error(new ServicePaymentBaseException("Server error")))
            .bodyToMono(ServicePaymentHistory.class)
            .retryWhen(
                Retry.fixedDelay(3, Duration.ofSeconds(2))
                    .doBeforeRetry(x->  log.info("LOG BEFORE RETRY=" + x))
                    .doAfterRetry(x->  log.info("LOG AFTER RETRY=" + x))
            )
            .doOnError(x-> log.info("LOG ERROR"))
            .doOnSuccess(x-> log.info("LOG SUCCESS"));
    }

    @Override
    public Flux<ServicePaymentHistory> servicePaymentHistoryFindByClientId(Integer clientId) {
        String queryParam = Optional.ofNullable(clientId).map(id -> "?clientId=" + String.valueOf(id)).orElse("");
        return this.client.get().uri("/history"+queryParam).accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new ServicePaymentBaseException("Server error")))
            .bodyToFlux(ServicePaymentHistory.class)
            .retryWhen(
                Retry.fixedDelay(3, Duration.ofSeconds(2))
                    .doBeforeRetry(x->  log.info("LOG BEFORE RETRY=" + x))
                    .doAfterRetry(x->  log.info("LOG AFTER RETRY=" + x))
            )
            .doOnError(x-> log.info("LOG ERROR"));
    }

    @Override
    public Flux<ServicePayment> servicePaymentList(String channel) {
        String queryParam = Optional.ofNullable(channel).map(s -> "?channel=" + s).orElse("");
        return this.client.get().uri("/" + queryParam).accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new ServicePaymentBaseException("Server error")))
            .bodyToFlux(ServicePayment.class)
            .retryWhen(
                Retry.fixedDelay(3, Duration.ofSeconds(2))
                    .doBeforeRetry(x->  log.info("LOG BEFORE RETRY=" + x))
                    .doAfterRetry(x->  log.info("LOG AFTER RETRY=" + x))
            )
            .doOnError(x-> log.info("LOG ERROR"));
    }
}
