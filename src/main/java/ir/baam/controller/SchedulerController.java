package ir.baam.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;


@RestController
@RequestMapping("/scheduler")
@RequiredArgsConstructor
@Log4j2
public class SchedulerController {



    @GetMapping(path = "/reschedule/instruction/failed",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String reschedulingForFailedInstructions() throws ParseException {

        log.debug("analyst and reschedule for failed instructions again.");
        //TODO do something......................
        return "scheduler is running and rescheduling for failed instructions again.";
    }

    @GetMapping(path = "/reschedule/transaction/failed",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String reschedulingForFailedTransactions() throws ParseException {

        log.debug("analyst and reschedule for failed transactions again.");
        //TODO do something......................
        return "scheduler is running and rescheduling for failed transactions again.";
    }


 }
