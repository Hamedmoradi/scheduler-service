package ir.baam.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;


@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class SchedulerCommandDto {
    private String command;
    private Long count;
    private String transactionStatus;
}