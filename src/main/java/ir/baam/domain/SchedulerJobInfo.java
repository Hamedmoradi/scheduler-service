package ir.baam.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;



@ToString
@Getter
@Setter
@Entity
@Table(name = "scheduler_job_info")
public class SchedulerJobInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = true, updatable = true)
    private Long id;
    private String jobName;
    private String jobGroup;
    private String jobStatus;
    private String jobClass;
    private String cronExpression;
    private String description;
    private String interfaceName;
    private Long repeatTime;
    private Boolean cronJob;
    private String serviceType;
    private String command;
    private String nextFireTimeString;
    private Date nextFireTime;
    private String state;
    private boolean isRunning;
    private boolean isPaused;
    private boolean isScheduled;


    public void setState() {
        String state = "";
        if (isRunning()) {
            state = "RUNNING";
        } else if (isPaused()) {
            state = "PAUSED";
        } else if (isScheduled()) {
            state = "SCHEDULED";
        } else {
            state = "UNKNOWN";
        }
        this.state = state;
    }


    public boolean isRunning() {
        return isRunning;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public boolean isScheduled() {
        return isScheduled;
    }
}
