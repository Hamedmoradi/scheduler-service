package ir.baam.webClient.standingOrder.dto;


import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class SchedulerCommandDto {
    private Long date;
    private String command;
    private Long count;
    private String transactionStatus;
}
