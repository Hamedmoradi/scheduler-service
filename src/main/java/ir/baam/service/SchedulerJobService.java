package ir.baam.service;

import ir.baam.component.JobScheduleCreator;
import ir.baam.domain.SchedulerJobInfo;
import ir.baam.dto.SchedulerJobInfoDto;
import ir.baam.enumeration.JobStatusEnum;
import ir.baam.repository.SchedulerRepository;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional
@Service
public class SchedulerJobService {
    @Autowired
    private Scheduler scheduler;
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;
    @Autowired
    private SchedulerRepository schedulerRepository;
    @Autowired
    private ApplicationContext context;
    @Autowired
    private JobScheduleCreator scheduleCreator;

    public SchedulerMetaData getMetaData() throws SchedulerException {
        return scheduler.getMetaData();
    }

    public List<SchedulerJobInfo> getAllJobList() {
        return schedulerRepository.findAll();
    }

    public boolean deleteJob(SchedulerJobInfo jobInfo) {
        try {
            SchedulerJobInfo schedulerJobInfo = schedulerRepository.findByJobName(jobInfo.getJobName());
            scheduler.unscheduleJob(TriggerKey.triggerKey(schedulerJobInfo.getJobName()));
            schedulerRepository.delete(schedulerJobInfo);
            return schedulerFactoryBean.getScheduler().deleteJob(new JobKey(schedulerJobInfo.getJobName(), schedulerJobInfo.getJobGroup()));
        } catch (SchedulerException e) {
            log.error("Failed to delete job - {}", jobInfo.getJobName(), e);
            return false;
        }
    }

    public boolean pauseJob(SchedulerJobInfo jobInfo) {
        try {
            SchedulerJobInfo getJobInfo = schedulerRepository.findByJobName(jobInfo.getJobName());
            getJobInfo.setJobStatus(JobStatusEnum.PAUSED.getValue());
            schedulerRepository.save(getJobInfo);
            schedulerFactoryBean.getScheduler().pauseJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
            return true;
        } catch (SchedulerException e) {
            log.error("Failed to pause job - {}", jobInfo.getJobName(), e);
            return false;
        }
    }

    public boolean resumeJob(SchedulerJobInfo jobInfo) {
        try {
            SchedulerJobInfo getJobInfo = schedulerRepository.findByJobName(jobInfo.getJobName());
            getJobInfo.setJobStatus(JobStatusEnum.RESUMED.getValue());
            schedulerRepository.save(getJobInfo);
            schedulerFactoryBean.getScheduler().resumeJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
            return true;
        } catch (SchedulerException e) {
            log.error("Failed to resume job - {}", jobInfo.getJobName(), e);
            return false;
        }
    }

    public boolean startJobNow(SchedulerJobInfo jobInfo) {
        try {
            SchedulerJobInfo getJobInfo = schedulerRepository.findByJobName(jobInfo.getJobName());
            getJobInfo.setJobStatus(JobStatusEnum.SCHEDULED_AND_STARTED.getValue());
            schedulerRepository.save(getJobInfo);
            schedulerFactoryBean.getScheduler().triggerJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
            return true;
        } catch (SchedulerException e) {
            log.error("Failed to start new job - {}", jobInfo.getJobName(), e);
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public void saveScheduleNewJob(SchedulerJobInfoDto jobInfoDto) {
        try {
            SchedulerJobInfo jobInfo = initJobScheduler(jobInfoDto);
            Scheduler schedulerFromFactoryBeanScheduler = schedulerFactoryBean.getScheduler();
            JobDetail jobDetail = createJobDetail(jobInfoDto);
            if (!schedulerFromFactoryBeanScheduler.checkExists(jobDetail.getKey())) {
                createAndSaveJob(jobInfoDto, jobInfo, schedulerFromFactoryBeanScheduler);
            } else {
                log.error("scheduleNewJobRequest.jobAlreadyExist");
            }
        } catch (ClassNotFoundException e) {
            log.error("Class Not Found - {}", jobInfoDto.getJobClass(), e);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }

    private SchedulerJobInfo initJobScheduler(SchedulerJobInfoDto jobInfoDto) {
        SchedulerJobInfo jobInfo = new SchedulerJobInfo();
        IsCronJob(jobInfoDto, jobInfo);
        jobInfo.setDescription(jobInfoDto.getDescription());
        jobInfo.setInterfaceName(jobInfoDto.getInterfaceName());
        jobInfo.setServiceType(jobInfoDto.getServiceType());
        jobInfo.setCronExpression(jobInfoDto.getCronExpression());
        jobInfo.setJobName(jobInfoDto.getJobName());
        jobInfo.setJobGroup(jobInfoDto.getJobGroup());
        jobInfo.setCommand(jobInfoDto.getCommand());
        jobInfo.setRepeatTime(jobInfoDto.getRepeatTime());
        return jobInfo;
    }

    private void createAndSaveJob(SchedulerJobInfoDto jobInfoDto, SchedulerJobInfo jobInfo, Scheduler schedulerFromFactoryBeanScheduler) throws ClassNotFoundException, SchedulerException {
        JobDetail jobDetail;
        jobDetail = createJobWithJobDetail(jobInfoDto);
        Trigger trigger = createTriggerForJob(jobInfoDto.isCronJob(), jobInfoDto.getJobName(), jobInfoDto.getCronExpression(), jobInfoDto.getRepeatTime());
        schedulerFromFactoryBeanScheduler.scheduleJob(jobDetail, trigger);
        jobInfo.setJobStatus(JobStatusEnum.SCHEDULED.getValue());
        schedulerRepository.save(jobInfo);
        log.info(">>>>> jobName = [" + jobInfoDto.getJobName() + "]" + " scheduled.");
    }

    private JobDetail createJobWithJobDetail(SchedulerJobInfoDto jobInfoDto) throws ClassNotFoundException {
        JobDetail jobDetail;
        jobDetail = scheduleCreator.createJob((Class<? extends QuartzJobBean>) Class.forName(jobInfoDto.getJobClass()),
                false, context, jobInfoDto.getJobName(), jobInfoDto.getJobGroup());
        return jobDetail;
    }

    private JobDetail createJobDetail(SchedulerJobInfoDto jobInfoDto) throws ClassNotFoundException {
        JobDetail jobDetail = JobBuilder
                .newJob((Class<? extends QuartzJobBean>) Class.forName(jobInfoDto.getJobClass()))
                .withIdentity(jobInfoDto.getJobName(), jobInfoDto.getJobGroup()).build();
        return jobDetail;
    }

    private void IsCronJob(SchedulerJobInfoDto jobInfoDto, SchedulerJobInfo jobInfo) {
        if (jobInfoDto.getCronExpression().length() > 0) {
            jobInfo.setJobClass(jobInfoDto.getJobClass());
            jobInfo.setCronJob(true);
        } else {
            jobInfo.setJobClass(jobInfoDto.getJobClass());
            jobInfo.setCronJob(false);
            jobInfo.setRepeatTime((long) 1);
        }
    }

    private Trigger createTriggerForJob(boolean cronJob, String triggerName, String cronExpression, Long repeat) {
        Trigger trigger;
        if (cronJob) {
            trigger = scheduleCreator.createCronTrigger(triggerName, new Date(),
                    cronExpression, CronTrigger.MISFIRE_INSTRUCTION_FIRE_ONCE_NOW);
        } else {
            trigger = scheduleCreator.createSimpleTrigger(triggerName, new Date(),
                    repeat, SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        }
        return trigger;
    }

    public void updateScheduleJob(SchedulerJobInfoDto schedulerJobInfoDto, Long id) {
        Optional<SchedulerJobInfo> jobInfo = schedulerRepository.findById(id);
        updateJobInfoFields(schedulerJobInfoDto, jobInfo);
        Trigger newTrigger = createTriggerForJob(jobInfo.get().isCronJob(), jobInfo.get().getJobName(), jobInfo.get().getCronExpression(), jobInfo.get().getRepeatTime());
        try {
            schedulerFactoryBean.getScheduler().rescheduleJob(TriggerKey.triggerKey(jobInfo.get().getJobName()), newTrigger);
            jobInfo.get().setJobStatus(JobStatusEnum.EDITED_AND_SCHEDULED.getValue());
            schedulerRepository.save(jobInfo.get());
            log.info(">>>>> jobName = [" + schedulerJobInfoDto.getJobName() + "]" + " updated and scheduled.");
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void updateJobInfoFields(SchedulerJobInfoDto schedulerJobInfoDto, Optional<SchedulerJobInfo> jobInfo) {
        if (schedulerJobInfoDto.getCommand() != null) {
            jobInfo.get().setCommand(schedulerJobInfoDto.getCommand());
        }
        if (schedulerJobInfoDto.getCronExpression() != null) {
            jobInfo.get().setJobName(schedulerJobInfoDto.getJobName());
        }
        if (schedulerJobInfoDto.getCronExpression() != null) {
            jobInfo.get().setCronExpression(schedulerJobInfoDto.getCronExpression());
        }
        if (schedulerJobInfoDto.getDescription() != null) {
            jobInfo.get().setDescription(schedulerJobInfoDto.getDescription());
        }
        if (schedulerJobInfoDto.getInterfaceName() != null) {
            jobInfo.get().setInterfaceName(schedulerJobInfoDto.getInterfaceName());
        }
        if (schedulerJobInfoDto.getJobGroup() != null) {
            jobInfo.get().setJobGroup(schedulerJobInfoDto.getJobGroup());
        }
        if (schedulerJobInfoDto.getRepeatTime() != null) {
            jobInfo.get().setRepeatTime(schedulerJobInfoDto.getRepeatTime());
        }
        if (schedulerJobInfoDto.getServiceType() != null) {
            jobInfo.get().setServiceType(schedulerJobInfoDto.getServiceType());
        }
    }
}
