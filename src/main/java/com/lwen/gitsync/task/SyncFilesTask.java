package com.lwen.gitsync.task;

import com.lwen.gitsync.constants.LogConstants;
import com.lwen.gitsync.entry.AccountInfo;
import com.lwen.gitsync.entry.JobSchedule;
import com.lwen.gitsync.job.JGitFileSyncJob;
import com.lwen.gitsync.service.AccountInfoService;
import com.lwen.gitsync.service.JobScheduleService;
import com.lwen.gitsync.service.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class SyncFilesTask {
    @Resource
    Scheduler scheduler;
    @Resource
    JobScheduleService jobScheduleService;
    @Resource
    AccountInfoService accountInfoService;
    @Resource
    StatisticsService statisticsService;

    Map<JobDetail, Trigger> jtMap = new HashMap<>();

    private void initJtMap() {
        List<JobSchedule> jobSchedules = jobScheduleService.all();
        AccountInfo accountInfo = accountInfoService.getAccountInfo();
        jobSchedules.forEach(jobSchedule -> {
            JobDataMap dataMap = new JobDataMap();
            dataMap.put("jobScheduleService", jobScheduleService);
            dataMap.put("statisticsService", statisticsService);
            dataMap.put("accountInfo", accountInfo);
            JobDetail jobDetail = JobBuilder
                    .newJob(JGitFileSyncJob.class)
                    .withIdentity(jobSchedule.getRepository())
                    .usingJobData("repository", jobSchedule.getRepository())
                    .usingJobData("syncPath", jobSchedule.getSyncPath())
                    .usingJobData("username", accountInfo.getUsername())
                    .usingJobData("commitMsg", "\""+new Date() + "autoCommit\"")
                    .usingJobData("id", jobSchedule.getId())
                    .usingJobData(dataMap)
                    .build();
            CronTrigger cronTrigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity(jobSchedule.getRepository() + " trigger")
                    .startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule(jobSchedule.getSyncRule()))
                    .build();
            jtMap.put(jobDetail, cronTrigger);
        });
    }

    private void schedule() {
        jtMap.forEach((jobDetail, cronTrigger) -> {
            try {
                if (scheduler.checkExists(jobDetail.getKey())) {
                    scheduler.deleteJob(jobDetail.getKey());
                }
                scheduler.scheduleJob(jobDetail, cronTrigger);
                log.info(LogConstants.LOG_TAG + "scheduleJob {} success", jobDetail.getKey());
            } catch (SchedulerException e) {
                log.error(LogConstants.LOG_TAG + "scheduleJob error ");
                e.printStackTrace();
            }
        });
    }

    public void start() {
        try {
            if (!scheduler.isStarted()) {
                scheduler.start();
            }
            log.info(LogConstants.LOG_TAG + "init schedule jobs");
            initJtMap();
            log.info(LogConstants.LOG_TAG + "starting schedule jobs");
            schedule();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

    }
}
