package ir.baam.controller;


import ir.baam.domain.Message;
import ir.baam.domain.SchedulerJobInfo;
import ir.baam.dto.SchedulerJobInfoDto;
import ir.baam.service.SchedulerJobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class SchedulerJobsAPIs {

    @Autowired
    private final SchedulerJobService scheduleJobService;


    @PostMapping(value = "/save")
    public Object save(@RequestBody SchedulerJobInfoDto schedulerJobInfoDto) {
        Message message = Message.failure();
        try {
            scheduleJobService.saveScheduleNewJob(schedulerJobInfoDto);
            message = Message.success();
        } catch (Exception e) {
            message.setMsg(e.getMessage());
            log.error("updateCron ex:", e);
        }
        return message;
    }

    @PostMapping(value = "/update/{id}")
    public Object update(@RequestBody SchedulerJobInfoDto schedulerJobInfoDto, @PathVariable Long id) {
        Message message = Message.failure();
        try {
            scheduleJobService.updateScheduleJob(schedulerJobInfoDto, id);
            message = Message.success();
        } catch (Exception e) {
            message.setMsg(e.getMessage());
            log.error("updateCron ex:", e);
        }
        return message;
    }

    @RequestMapping("/metaData")
    public Object metaData() throws SchedulerException {
        return scheduleJobService.getMetaData();
    }

    @RequestMapping("/getAllJobs")
    public Object getAllJobs() {
        return scheduleJobService.getAllJobList();
    }

    @PostMapping(value = "/runJob")
    public Object runJob(@RequestBody SchedulerJobInfo job) {
        Message message = Message.failure();
        try {
            scheduleJobService.startJobNow(job);
            message = Message.success();
        } catch (Exception e) {
            message.setMsg(e.getMessage());
            log.error("runJob ex:", e);
        }
        return message;
    }

    @PostMapping(value = "/pauseJob")
    public Object pauseJob(@RequestBody SchedulerJobInfo job) {
        Message message = Message.failure();
        try {
            scheduleJobService.pauseJob(job);
            message = Message.success();
        } catch (Exception e) {
            message.setMsg(e.getMessage());
            log.error("pauseJob ex:", e);
        }
        return message;
    }

    @PostMapping(value = "/resumeJob")
    public Object resumeJob(@RequestBody SchedulerJobInfo job) {
        log.info("params, job = {}", job);
        Message message = Message.failure();
        try {
            scheduleJobService.resumeJob(job);
            message = Message.success();
        } catch (Exception e) {
            message.setMsg(e.getMessage());
            log.error("resumeJob ex:", e);
        }
        return message;
    }

    @DeleteMapping(value = "/deleteJob")
    public Object deleteJob(@RequestBody SchedulerJobInfo job) {
        log.info("params, job = {}", job);
        Message message = Message.failure();
        try {
            scheduleJobService.deleteJob(job);
            message = Message.success();
        } catch (Exception e) {
            message.setMsg(e.getMessage());
            log.error("deleteJob ex:", e);
        }
        return message;
    }
}
