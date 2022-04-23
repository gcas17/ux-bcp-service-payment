package bcp.bootcamp.uxbcpservicepayment.core.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    private ObjectMapper objectMapper;

    public GlobalExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();

        if (ex instanceof ServicePaymentBaseException) {
            var internalException= (ServicePaymentBaseException)ex;
            exchange.getResponse().setStatusCode(internalException.getStatus());
            DataBuffer dataBuffer = null;
            try {
                dataBuffer = bufferFactory.wrap(objectMapper.writeValueAsBytes(new HttpError(internalException.getMessage()) ));
            } catch (JsonProcessingException e) {
                dataBuffer = bufferFactory.wrap("".getBytes());
            }
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return exchange.getResponse().writeWith(Mono.just(dataBuffer));

        }

        log.info("ERROR ex=" + ex);
        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        exchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);
        DataBuffer dataBuffer = bufferFactory.wrap("Unknown error".getBytes());
        return exchange.getResponse().writeWith(Mono.just(dataBuffer));
    }

    public class HttpError {

        private String message;

        HttpError(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
