package com.scheduler.service.configuration.scheduler;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class SchedulerConfig {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Bean
	public SchedulerFactoryBean schedulerFactoryBean() {

		SchedulerJobFactory jobFactory = new SchedulerJobFactory();
		jobFactory.setApplicationContext(applicationContext);

		SchedulerFactoryBean factory = new SchedulerFactoryBean();
		factory.setConfigLocation(new ClassPathResource("quartz.properties"));
		factory.setDataSource(dataSource);
		factory.setTransactionManager(transactionManager);
		factory.setOverwriteExistingJobs(true);
		factory.setJobFactory(jobFactory);
		return factory;
	}
}
