package scheduler.service.configuration.scheduler;

import scheduler.service.MockJob;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.SchedulerContext;
import org.quartz.impl.JobDetailImpl;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.context.ApplicationContext;

import static org.mockito.Mockito.when;


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
        MockitoAnnotations.openMocks(this);
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

}