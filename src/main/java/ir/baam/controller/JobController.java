package ir.baam.controller;


import ir.baam.service.SchedulerJobService;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ir.baam.domain.Message;
import ir.baam.domain.SchedulerJobInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class JobController {

	@Autowired
	private final SchedulerJobService scheduleJobService;

	@RequestMapping(value = "/saveOrUpdate", method = { RequestMethod.GET, RequestMethod.POST })
	public Object saveOrUpdate(@RequestBody SchedulerJobInfo job) {
		log.info("params, job = {}", job);
		Message message = Message.failure();
		try {
			scheduleJobService.saveOrUpdate(job);
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
	public Object getAllJobs()  {
		return scheduleJobService.getAllJobList();
	}

	@RequestMapping(value = "/runJob", method = { RequestMethod.GET, RequestMethod.POST })
	public Object runJob(@RequestBody SchedulerJobInfo job) {
		log.info("params, job = {}", job);
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

	@RequestMapping(value = "/pauseJob", method = { RequestMethod.GET, RequestMethod.POST })
	public Object pauseJob(@RequestBody SchedulerJobInfo job) {
		log.info("params, job = {}", job);
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

	@RequestMapping(value = "/resumeJob", method = { RequestMethod.GET, RequestMethod.POST })
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

	@RequestMapping(value = "/deleteJob", method = { RequestMethod.GET, RequestMethod.POST })
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
