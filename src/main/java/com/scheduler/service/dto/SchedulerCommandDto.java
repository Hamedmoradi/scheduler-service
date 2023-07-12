package com.scheduler.service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class SchedulerCommandDto {
    private String command;
    private Long count;
    private String transactionStatus;
}