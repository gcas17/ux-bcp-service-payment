package bcp.bootcamp.uxbcpservicepayment.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ServicePayment {
    private Integer id;
    private String name;
    private String channel;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate creationDate;
    private String status;
}
