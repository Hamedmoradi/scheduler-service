package scheduler.service.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import scheduler.service.enumeration.TriggerStateEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

/**
 * Trigger Info user interface object
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class TriggerInfo {
    private TriggerStateEnum state;
    private ScheduleIdentifier triggerIdentifier;
    private ScheduleIdentifier jobIdentifier;
    private Date nextFireTime;
    private Date previousFireTime;
    private Date startTime;
    private Date endTime;
    private String cronExpression;
    private String description;
    private Class triggerClass;
    private String cronExpressionSummary;

    public TriggerInfo(@JsonProperty("jobIdentifier") ScheduleIdentifier jobIdentifier,
                       @JsonProperty("triggerIdentifier") ScheduleIdentifier triggerIdentifier) {
        this.jobIdentifier = jobIdentifier;
        this.triggerIdentifier = triggerIdentifier;
    }

}