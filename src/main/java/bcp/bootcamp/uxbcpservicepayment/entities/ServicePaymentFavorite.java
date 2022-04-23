package bcp.bootcamp.uxbcpservicepayment.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ServicePaymentFavorite {
    private String id;
    private Integer clientId;
    private Integer servicePaymentId;
    private String favoriteName;
    private String favoriteType;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate creationDate;
}
