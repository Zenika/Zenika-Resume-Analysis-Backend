package fr.zenika.search.zenikaresume.backend.job;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobScheduleConfig {

    @Bean
    public JobDetail sampleJobDetail() {
        return JobBuilder.newJob(ImportUsersJob.class).withIdentity("importJob")
                .usingJobData("name", "importData").storeDurably().build();
    }

    @Bean
    public Trigger sampleJobTrigger() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInHours(3).repeatForever();

        return TriggerBuilder.newTrigger().forJob(sampleJobDetail())
                .withIdentity("importTrigger").withSchedule(scheduleBuilder).build();


    }
}
