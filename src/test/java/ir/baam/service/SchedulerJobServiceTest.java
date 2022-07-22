package ir.baam.service;


import ir.baam.MockJob;
import ir.baam.component.JobScheduleCreator;
import ir.baam.domain.ScheduleIdentifier;
import ir.baam.domain.SchedulerJobInfo;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.*;

import static org.mockito.Mockito.*;

@SpringBootTest
class SchedulerJobServiceTest {

    @Mock
    ScheduleIdentifier jobIdentifier;
    @Mock
    SchedulerFactoryBean schedulerFactoryBean;
    @Mock
    Scheduler scheduler;
    @Mock
    JobScheduleCreator jobScheduleCreator;
    @Mock
    JobDetail jobDetail;


    @BeforeEach
    @SneakyThrows
    public void before() {
        MockitoAnnotations.initMocks(this);
        when(schedulerFactoryBean.getScheduler()).thenReturn(scheduler);
        mockJobIdentifier("job-name", "group");
    }


    private void mockJobIdentifier(String jobName, String groupName) {
        when(jobIdentifier.getName()).thenReturn(jobName);
        when(jobIdentifier.getGroup()).thenReturn(groupName);
    }

    @Test
    public void controlTests() throws Exception {
        scheduler.pauseAll();
        scheduler.resumeAll();
        scheduler.getMetaData();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void addMockTrigger(final List list, String triggerKeyName, String triggerKeyGroup) {
        final Trigger trigger = mock(Trigger.class);
        final TriggerKey triggerKey = new TriggerKey(triggerKeyName, triggerKeyGroup);
        when(trigger.getKey()).thenReturn(triggerKey);
        list.add(trigger);
    }

    @Test
    @SneakyThrows
    void getMetaData() {
        Scheduler scheduler = Mockito.mock(Scheduler.class);
        Mockito.when(scheduler.getMetaData()).thenReturn(any());
    }

    @Test
    void getAllJobList() throws SchedulerException {
        Vector<String> jobGroupNames = new Vector<>();
        jobGroupNames.add("group");
        Mockito.when(scheduler.getJobGroupNames()).thenReturn(jobGroupNames);
        Set<JobKey> set = new HashSet<>();
        set.add(new JobKey("name", "group"));
        Mockito.when(scheduler.getJobKeys((GroupMatcher) anyObject())).thenReturn(set);
        Mockito.when(scheduler.getJobDetail((JobKey) anyObject())).thenReturn(new JobDetailImpl("name", "group", MockJob.class));
        List v = new Vector<>();
        addMockTrigger(v, "name", "group");
        Mockito.when(scheduler.getTriggerState((TriggerKey) Mockito.anyObject())).thenReturn(Trigger.TriggerState.BLOCKED);
        Mockito.when(scheduler.getTriggersOfJob((JobKey) anyObject())).thenReturn(v);
    }

    @Test
    @SneakyThrows
    void pauseJob() {
        mockJobIdentifier("job-name", "group");
        final List list = new ArrayList<Trigger>();
        addMockTrigger(list, "trigger-key-name", "trigger-key-name");
        when(scheduler.getTriggersOfJob(new JobKey("job-name", "group"))).thenReturn(list);
        scheduler.pauseTrigger(new TriggerKey("trigger-key-name", "trigger-key-name"));
        verify(scheduler).pauseTrigger(new TriggerKey("trigger-key-name", "trigger-key-name"));
        doThrow(new SchedulerException()).when(scheduler).getTriggersOfJob((JobKey) any());
        try {
            scheduler.pauseTrigger(new TriggerKey("trigger-key-name", "trigger-key-name"));
        } catch (final RuntimeException e) {
            return;
        }
    }

    @Test
    @SneakyThrows
    void resumeJob() {
        mockJobIdentifier("job-name", "group");
        final List list = new ArrayList<Trigger>();
        addMockTrigger(list, "trigger-key-name", "trigger-key-name");
        when(scheduler.getTriggersOfJob(new JobKey("job-name", "group"))).thenReturn(list);
        scheduler.resumeTrigger(new TriggerKey("trigger-key-name", "trigger-key-name"));
        verify(scheduler).resumeTrigger(new TriggerKey("trigger-key-name", "trigger-key-name"));
        doThrow(new SchedulerException()).when(scheduler).getTriggersOfJob((JobKey) any());
        try {
            scheduler.resumeTrigger(new TriggerKey("trigger-key-name", "trigger-key-name"));
        } catch (final RuntimeException e) {
            return;
        }
    }

    @Test
    @SneakyThrows
    void startJobNow() {
        scheduler.start();
        verify(scheduler).start();
        doThrow(new SchedulerException()).when(scheduler).start();
    }

    @Test
    void saveOrUpdate() {
    }

    @Test
    @SneakyThrows
    void scheduleNewJob() {
        SchedulerFactoryBean schedulerFactoryBean = Mockito.mock(SchedulerFactoryBean.class);
        Mockito.when(schedulerFactoryBean.getScheduler()).thenReturn(scheduler);
        final List list = new ArrayList<Trigger>();
        addMockTrigger(list, "trigger-key-name", "trigger-key-name");
        jobScheduleCreator.createCronTrigger("trigger-key-name", new Date(), "0/30 * * * * ?", SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        jobScheduleCreator.createSimpleTrigger("job-simple-trigger", new Date(), 5L, SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        final Trigger trigger = mock(Trigger.class);
        scheduler.scheduleJob(jobDetail, trigger);
    }

    @Test
    void deleteJob() throws SchedulerException {
        mockJobIdentifier("job-name", "group");
        JobKey jobKey=new JobKey("job-name", "group");
        scheduler.deleteJob(jobKey);
        doThrow(new SchedulerException()).when(scheduler).deleteJob(jobKey);
    }


    @Test
    @SneakyThrows
    public void updateTriggerForCronTypeTest() {

        scheduler.start();
        final List list = new ArrayList<Trigger>();
        addMockTrigger(list, "trigger-key-name", "trigger-key-name");
        TriggerKey newTriggerKey=new TriggerKey("new-trigger-key-name", "new-trigger-key-name");
        jobScheduleCreator.createCronTrigger("trigger-key-name", new Date(), "0/30 * * * * ?", SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        final Trigger trigger = mock(Trigger.class);
        scheduler.rescheduleJob(newTriggerKey,trigger);
        doThrow(new SchedulerException()).when(scheduler).rescheduleJob((TriggerKey) any(), (Trigger) any());

    }


    @Test
    public void buildTriggerInfo() throws Exception {
        JobDetailImpl detail = new JobDetailImpl();
        detail.setName("job-name");
        SchedulerJobInfo info = new SchedulerJobInfo();
        Assertions.assertNotNull(info);
    }

}