package scheduler.service.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchedulerJobInfoDto implements Serializable {
    @NotNull
    private String jobName;

    @NotNull
    private String jobGroup;

    private String jobStatus;

    @NotNull
    private String jobClass;

    private String cronExpression;

    @NotNull
    private String description;

    private String interfaceName;

    private Long repeatTime;

    @NotNull
    private boolean cronJob;

    @NotNull
    private String serviceType;

    @NotNull
    private String command;

    private String state;
}
