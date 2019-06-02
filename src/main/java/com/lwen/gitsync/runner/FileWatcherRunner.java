package com.lwen.gitsync.runner;

import com.lwen.gitsync.entry.JobSchedule;
import com.lwen.gitsync.service.JobScheduleService;
import com.lwen.gitsync.watcher.FileWatcher;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class FileWatcherRunner implements ApplicationRunner {
    @Resource
    JobScheduleService jobScheduleService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        long interval = TimeUnit.SECONDS.toMillis(1);

        List<JobSchedule> jobSchedules = jobScheduleService.all();
        jobSchedules.forEach(jobSchedule -> {
            try {
                // exclude hidden files and .git files
                IOFileFilter filter = FileFilterUtils.and(HiddenFileFilter.VISIBLE,FileFilterUtils.nameFileFilter(".git"));
                // observer dir
                FileAlterationObserver observer = new FileAlterationObserver(new File(jobSchedule.getSyncPath()),filter);
                observer.addListener(new FileWatcher());
                // create monitor
                FileAlterationMonitor monitor = new FileAlterationMonitor(interval, observer);
                monitor.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }
}
