package com.scheduler.service.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@ToString
@Getter
@Setter
@Entity
@Table(name = "scheduler_job_info")
public class SchedulerJobInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @NotNull
    private String jobName;
    @NotNull
    private String jobGroup;
    private String jobStatus;
    @NotNull
    private String jobClass;
    @NotNull
    private String cronExpression;
    private String description;
    private String interfaceName;
    private Long repeatTime;
    private boolean cronJob;
    @NotNull
    private String serviceType;
    @NotNull
    private String command;
}
