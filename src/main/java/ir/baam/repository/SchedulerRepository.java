package ir.baam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ir.baam.domain.SchedulerJobInfo;

@Repository
public interface SchedulerRepository extends JpaRepository<SchedulerJobInfo, Long> {

	SchedulerJobInfo findByJobName(String jobName);

}
