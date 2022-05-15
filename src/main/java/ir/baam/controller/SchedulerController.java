package ir.baam.controller;

import ir.baam.domain.Message;
import ir.baam.domain.SchedulerJobInfo;
import ir.baam.service.SchedulerJobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/scheduler")
@RequiredArgsConstructor
@Log4j2
public class SchedulerController {

    private final SchedulerJobService scheduleJobService;

    @PostMapping(path = "/reschedule/instruction/failed",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Object reschedulingForFailedInstructions(@RequestBody SchedulerJobInfo job) {
        log.debug("analyst and reschedule for failed instructions again.");
        Message message = Message.failure();
        try {
            scheduleJobService.startJobNow(job);
            message = Message.success();
            log.debug("scheduler is running and rescheduling for failed instructions again.");
        } catch (Exception e) {
            message.setMsg(e.getMessage());
        }
        return message;
    }

    @PostMapping(path = "/reschedule/transaction/failed",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Object reschedulingForFailedTransactions(@RequestBody SchedulerJobInfo job) {
        log.debug("analyst and reschedule for failed transactions again.");
        Message message = Message.failure();
        try {
            scheduleJobService.startJobNow(job);
            message = Message.success();
            log.debug("scheduler is running and rescheduling for failed transactions again.");
        } catch (Exception e) {
            message.setMsg(e.getMessage());
        }
        return message;
    }

}

