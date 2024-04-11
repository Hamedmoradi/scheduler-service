package scheduler.service.controller;

import scheduler.service.domain.Message;
import scheduler.service.domain.SchedulerJobInfo;
import scheduler.service.repository.SchedulerRepository;
import scheduler.service.service.SchedulerJobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/scheduler")
@RequiredArgsConstructor
@Log4j2
public class SchedulerController {

    @Autowired
    private final SchedulerJobService scheduleJobService;

    @Autowired
    private SchedulerRepository schedulerRepository;

    @PostMapping(path = "/reschedule/instruction/failed",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Object reschedulingForFailedInstructions(@RequestBody SchedulerJobInfo req) {
        log.debug("analyst and reschedule for failed instructions again.");
        Message message = Message.failure();
        try {
            SchedulerJobInfo jobInfo = schedulerRepository.findByCommandAndServiceType(req.getCommand(), req.getServiceType());
            scheduleJobService.startJobNow(jobInfo);
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
    public Object reschedulingForFailedTransactions(@RequestBody SchedulerJobInfo req) {
        log.debug("analyst and reschedule for failed transactions again.");
        Message message = Message.failure();
        try {
            SchedulerJobInfo jobInfo = schedulerRepository.findByCommandAndServiceType(req.getCommand(), req.getServiceType());
            scheduleJobService.startJobNow(jobInfo);
            message = Message.success();
            log.debug("scheduler is running and rescheduling for failed transactions again.");
        } catch (Exception e) {
            message.setMsg(e.getMessage());
        }
        return message;
    }

}

