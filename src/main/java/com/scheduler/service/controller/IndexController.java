package com.scheduler.service.controller;

import com.scheduler.service.domain.SchedulerJobInfo;
import com.scheduler.service.service.SchedulerJobService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class IndexController {

	private final SchedulerJobService scheduleJobService;

	public IndexController(SchedulerJobService scheduleJobService) {
		this.scheduleJobService = scheduleJobService;
	}

	@GetMapping("/index")
	public String index(Model model){
		List<SchedulerJobInfo> jobList = scheduleJobService.getAllJobList();
		model.addAttribute("jobs", jobList);
		return "index";
	}
	
}
