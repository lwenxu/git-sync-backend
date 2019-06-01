package com.lwen.gitsync.config;

import com.lwen.gitsync.constants.LogConstants;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class QuartzConfiguration {

    @Bean
    public Scheduler scheduler() {
        SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
        try {
            return schedFact.getScheduler();
        } catch (SchedulerException e) {
            log.error(LogConstants.LOG_TAG + "create scheduler failed ");
            e.printStackTrace();
        }
        return null;
    }


}
