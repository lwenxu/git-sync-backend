package com.lwen.gitsync.job;

import com.lwen.gitsync.constants.LogConstants;
import com.lwen.gitsync.service.JobScheduleService;
import com.lwen.gitsync.utils.OSUtils;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Data
@DisallowConcurrentExecution
public class GitFileSyncJob implements Job {
    private int id;
    private String repository;
    private String syncPath;
    private String gitPath;
    private String commitMsg;
    private JobScheduleService jobScheduleService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info(LogConstants.LOG_TAG + " starting exec GitFileSyncJob ");
        String bash = OSUtils.WINDOWS ? "cmd " : "/bin/bash";
        String init = gitPath + " init";
        String addChanges = gitPath + " add .";
        String commitChanges = gitPath + " commit -m " + commitMsg;
        String addRemote = gitPath + " remote add origin git@github.com:" + repository;
        String push = gitPath + " push -u  origin master";
        List<String> cmds = Arrays.asList( init, addChanges, commitChanges,addRemote, push);

        // 检查目录是否存在
        if (!(new File(syncPath)).isDirectory()) {
            (new File(syncPath)).mkdirs();
        }
        log.debug(LogConstants.LOG_TAG + "jobScheduleService {} id {}", jobScheduleService, id);
        // 执行命令
        jobScheduleService.startSync(id);
        cmds.forEach(cmd -> {
            try {
                Runtime.getRuntime().exec(cmd,null,new File(syncPath));
                log.info(LogConstants.LOG_TAG + "exec {} success ", cmd);
            } catch (IOException e) {
                log.error(LogConstants.LOG_TAG + "exec {} error ", cmd);
                e.printStackTrace();
            }
        });
        jobScheduleService.endSync(id);
    }

}
