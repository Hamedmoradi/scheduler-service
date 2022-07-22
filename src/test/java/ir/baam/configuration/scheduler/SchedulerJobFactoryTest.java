package ir.baam.configuration.scheduler;

import ir.baam.MockJob;
import ir.baam.component.JobScheduleCreator;
import ir.baam.domain.ScheduleIdentifier;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerContext;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.calendar.BaseCalendar;
import org.quartz.spi.OperableTrigger;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//TODO test dont pass
@SpringBootTest
class SchedulerJobFactoryTest {

    @Mock
    SchedulerJobFactory factory;
    @Mock
    ApplicationContext applicationContext;
    @Mock
    TriggerFiredBundle triggerFiredBundle;
    @Mock
    SchedulerContext schedulerContext;

    @Test
    void createJobInstance() {
    }


    @BeforeEach
    @SneakyThrows
    public void setUp() {
        factory.setSchedulerContext(schedulerContext);
        factory.setApplicationContext(applicationContext);
    }

    @Test
    @SneakyThrows
    public void setApplicationContext() {
        JobDetailImpl jobDetail = new JobDetailImpl();
        jobDetail.setJobClass(MockJob.class);
        when(factory.createJobInstance(triggerFiredBundle));
    }

//    @Test
//    @SneakyThrows
//    public void testCreateJobInstance(){
//    }
}