package bcp.bootcamp.uxbcpservicepayment.repositories;

import bcp.bootcamp.uxbcpservicepayment.entities.ServicePayment;
import bcp.bootcamp.uxbcpservicepayment.entities.ServicePaymentFavorite;
import bcp.bootcamp.uxbcpservicepayment.entities.ServicePaymentHistory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ServicePaymentRepository {

    Mono<ServicePaymentHistory> servicePaymentHistorySave(ServicePaymentHistory servicePaymentHistory, String token);

    Flux<ServicePaymentHistory> servicePaymentHistoryFindByClientId(Integer clientId, String token);

    Flux<ServicePayment> servicePaymentList(Integer id, String channel, String token);

}
