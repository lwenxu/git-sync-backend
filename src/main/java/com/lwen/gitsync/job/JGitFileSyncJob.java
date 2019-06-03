package com.lwen.gitsync.job;

import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import com.lwen.gitsync.constants.LogConstants;
import com.lwen.gitsync.service.JobScheduleService;
import com.lwen.gitsync.service.StatisticsService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.realm.NestedCredentialHandler;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.transport.ChainingCredentialsProvider;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Set;

@Slf4j
@Data
@DisallowConcurrentExecution
public class JGitFileSyncJob implements Job {
    private int id;
    private String repository;
    private String syncPath;
    private String gitPath;
    private String commitMsg;
    private JobScheduleService jobScheduleService;
    private StatisticsService statisticsService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            File gitDir = new File(syncPath);
            if (!gitDir.isDirectory()) {
                gitDir.mkdirs();
            }
            TextProgressMonitor progressMonitor = new TextProgressMonitor(new OutputStreamWriter(System.out));
            Git.init().setDirectory(gitDir).call();
            Git git= Git.open(gitDir);
            Status status = git.status().call();
            if (status.getConflicting().size() != 0) {
                jobScheduleService.syncConflict(id);
                statisticsService.failed();
                log.warn(LogConstants.LOG_TAG + "sync conflict ");
                return;
            }
            if (!status.isClean()) {
                log.info(LogConstants.LOG_TAG + " starting exec GitFileSyncJob ");
                jobScheduleService.startSync(id);
                notifySyncingFiles(status);
                ChainingCredentialsProvider credentialsProvider = new ChainingCredentialsProvider(new UsernamePasswordCredentialsProvider("lwenxu", "sh19970618"));
                git.add().addFilepattern(".").call();
                git.commit().setAll(true).setAuthor("git-sync", "xpf199741@outlook.com").setMessage(commitMsg).call();
                git.remoteAdd().setUri(new URIish("https://github.com/" + repository)).setName("origin").call();
                git.push().setCredentialsProvider(credentialsProvider).setRemote("origin").setProgressMonitor(progressMonitor).call();
                progressMonitor.beginTask("aa", 1);
                progressMonitor.update(1);
                log.info(LogConstants.LOG_TAG + "exec  success ");
                statisticsService.success();
                jobScheduleService.endSync(id);
            }
        } catch (IOException | GitAPIException | URISyntaxException e) {
            statisticsService.failed();
            log.error(LogConstants.LOG_TAG + "exec {} error ", e.getMessage());
            e.printStackTrace();
        }
    }

    private void notifySyncingFiles(Status status) {
        Set<String> add = status.getAdded();
        Set<String> cha = status.getChanged();
        Set<String> mod = status.getModified();
        Set<String> del = status.getRemoved();
        Set<String> all = Sets.newHashSet();
        all.addAll(add);
        all.addAll(mod);
        all.addAll(del);
        all.addAll(cha);
        log.info(LogConstants.LOG_TAG + "change list is :{}", all);
        log.info(LogConstants.LOG_TAG + "uncommited list is :{}", status.getUncommittedChanges());
        jobScheduleService.syncingFiles(all);
    }

}
