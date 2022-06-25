package ir.baam.webClient.standingOrder.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;


@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class SchedulerCommandDto {


    private Date date;
    private String command;
    private Long count;
    private String transactionStatus;
}