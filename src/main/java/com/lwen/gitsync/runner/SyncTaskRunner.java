package com.lwen.gitsync.runner;

import com.lwen.gitsync.task.SyncFilesTask;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class SyncTaskRunner implements ApplicationRunner {
    @Resource
    SyncFilesTask syncFilesTask;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        syncFilesTask.start();
    }
}
