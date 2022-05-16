package ir.baam.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.Date;

@AllArgsConstructor
@Data
public class SchedulerCommand {
    private Instant date;
    private String command;
    private Long count;
    private String transactionStatus;
}
