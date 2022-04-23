package bcp.bootcamp.uxbcpservicepayment.services.impl;

import bcp.bootcamp.uxbcpservicepayment.core.exceptions.ServicePaymentBaseException;
import bcp.bootcamp.uxbcpservicepayment.entities.ServicePayment;
import bcp.bootcamp.uxbcpservicepayment.entities.ServicePaymentFavorite;
import bcp.bootcamp.uxbcpservicepayment.entities.ServicePaymentHistory;
import bcp.bootcamp.uxbcpservicepayment.repositories.ServicePaymentFavoriteRepository;
import bcp.bootcamp.uxbcpservicepayment.repositories.ServicePaymentRepository;
import bcp.bootcamp.uxbcpservicepayment.services.ServicePaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class ServicePaymentServiceImpl implements ServicePaymentService {

    @Autowired
    private ServicePaymentRepository servicePaymentRepository;

    @Autowired
    private ServicePaymentFavoriteRepository servicePaymentFavoriteRepository;

    @Override
    public Flux<ServicePayment> servicePaymentFindAll(Integer id, String channel, String token) {
        return this.servicePaymentRepository.servicePaymentList(id, channel, token);
    }

    @Override
    public Flux<ServicePaymentHistory> servicePaymentHistoryFindByClientId(Integer clientId, String token) {
        return this.servicePaymentRepository.servicePaymentHistoryFindByClientId(clientId, token);
    }

    @Override
    public Mono<ServicePaymentHistory> servicePaymentHistorySave(ServicePaymentHistory servicePaymentHistory, String token) {
        return this.servicePaymentRepository.servicePaymentHistorySave(servicePaymentHistory, token);
    }

    @Override
    public Flux<ServicePaymentFavorite> servicePaymentFavoriteFindByClientId(Integer clientId, String token) {
        return this.servicePaymentFavoriteRepository.servicePaymentFavoriteFindByClientId(clientId, token);
    }

    @Override
    public Mono<ServicePaymentFavorite> servicePaymentFavoriteSave(ServicePaymentFavorite servicePaymentFavorite, String token) {
        return this.servicePaymentFavoriteRepository.servicePaymentFavoriteSave(servicePaymentFavorite, token);
    }

    @Override
    public Mono<Void> servicePaymentFavoriteDelete(String id, String token) {
        return this.servicePaymentFavoriteRepository.servicePaymentFavoriteDelete(id, token);
    }

}
