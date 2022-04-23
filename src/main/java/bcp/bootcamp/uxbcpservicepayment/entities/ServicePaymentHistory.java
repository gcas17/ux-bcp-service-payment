package bcp.bootcamp.uxbcpservicepayment.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String channel;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Boolean addToFavorites;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String favoriteName;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String favoriteType;
}
