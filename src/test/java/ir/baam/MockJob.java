package ir.baam;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 */
public class MockJob implements Job {

    public MockJob() {

    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        // ok
    }
}