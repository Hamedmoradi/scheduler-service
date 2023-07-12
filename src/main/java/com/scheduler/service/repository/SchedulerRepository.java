package com.scheduler.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scheduler.service.domain.SchedulerJobInfo;

@Repository
public interface SchedulerRepository extends JpaRepository<SchedulerJobInfo, Long> {

	SchedulerJobInfo findByJobName(String jobName);

	SchedulerJobInfo findByCommandAndServiceType(String command,String serviceType);

	SchedulerJobInfo findByJobNameAndServiceType(String jobName,String serviceType);

}
