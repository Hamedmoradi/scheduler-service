package ir.baam.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@AllArgsConstructor
@Data
public class SchedulerCommand {
    private Date date;
    private String command;
    private Long count;
    private String transactionStatus;
}
