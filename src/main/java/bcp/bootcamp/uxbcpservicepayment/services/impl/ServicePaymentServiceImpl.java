package bcp.bootcamp.uxbcpservicepayment.services.impl;

import bcp.bootcamp.uxbcpservicepayment.entities.ServicePayment;
import bcp.bootcamp.uxbcpservicepayment.entities.ServicePaymentFavorite;
import bcp.bootcamp.uxbcpservicepayment.entities.ServicePaymentHistory;
import bcp.bootcamp.uxbcpservicepayment.repositories.ServicePaymentFavoriteRepository;
import bcp.bootcamp.uxbcpservicepayment.repositories.ServicePaymentRepository;
import bcp.bootcamp.uxbcpservicepayment.services.ServicePaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Flux<ServicePayment> servicePaymentFindAll(String channel) {
        return this.servicePaymentRepository.servicePaymentList(channel);
    }

    @Override
    public Flux<ServicePaymentHistory> servicePaymentHistoryFindByClientId(Integer clientId) {
        return this.servicePaymentRepository.servicePaymentHistoryFindByClientId(clientId);
    }

    @Override
    public Mono<ServicePaymentHistory> servicePaymentHistorySave(ServicePaymentHistory servicePaymentHistory) {
        return this.servicePaymentRepository.servicePaymentHistorySave(servicePaymentHistory);
    }

    @Override
    public Flux<ServicePaymentFavorite> servicePaymentFavoriteFindByClientId(Integer clientId) {
        return this.servicePaymentFavoriteRepository.servicePaymentFavoriteFindByClientId(clientId);
    }

    @Override
    public Mono<ServicePaymentFavorite> servicePaymentFavoriteSave(ServicePaymentFavorite servicePaymentFavorite) {
        return this.servicePaymentFavoriteRepository.servicePaymentFavoriteSave(servicePaymentFavorite);
    }

    @Override
    public Mono<Void> servicePaymentFavoriteDelete(String id) {
        return this.servicePaymentFavoriteRepository.servicePaymentFavoriteDelete(id);
    }

}
