package com.monitoringService.monitoringService;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.monitoringService.monitoringService.QuartzScheduler;
import com.monitoringService.monitoringService.CreateSyncJobs;
@SpringBootApplication
public class MonitoringServiceApplication {

	public static void main(String[] args) throws SchedulerException {
		SpringApplication.run(MonitoringServiceApplication.class, args);
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.start();
        
        // Define the job detail for the Quartz job

//        JobDetail jobDetail = JobBuilder.newJob(QuartzScheduler.class).withIdentity("hello1", "hello1Group").build();
//
//        // Define the trigger for the Quartz job to run every 20 minutes
//        Trigger trigger = TriggerBuilder.newTrigger()
//            .withIdentity("helloWorldTrigger1", "hello1Group")
//            .startNow()
//            .withSchedule(SimpleScheduleBuilder.simpleSchedule()
//                    .withIntervalInSeconds(5).repeatForever())
//            .build();
//
//        scheduler.scheduleJob(jobDetail, trigger);
        JobDetail jobDetail=JobBuilder.newJob(CreateSyncJobs.class).withIdentity("CreateSyncJobs", "JobCreationGroup").build();
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("CreateSyncJobsTrigger", "JobCreationGroup")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInMinutes(1).repeatForever())
                .build();
        scheduler.scheduleJob(jobDetail, trigger);
	}

}
