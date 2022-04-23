package bcp.bootcamp.uxbcpservicepayment.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ServicePaymentHistory {
    private Integer id;
    private Integer clientId;
    private Integer servicePaymentId;
    private String servicePaymentName;
    private String supplyNumber;
    private BigDecimal amount;
    private String currency;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime transactionDate;
}
