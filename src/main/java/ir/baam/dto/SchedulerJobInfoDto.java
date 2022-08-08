package ir.baam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchedulerJobInfoDto implements Serializable {
    private String jobName;
    private String jobGroup;
    private String jobStatus;
    private String jobClass;
    private String cronExpression;
    private String description;
    private String interfaceName;
    private Long repeatTime;
    private boolean cronJob;
    private String serviceType;
    private String command;
    private String state;
}
