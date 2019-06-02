package com.lwen.gitsync.job;

import com.lwen.gitsync.constants.LogConstants;
import com.lwen.gitsync.service.JobScheduleService;
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

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            if (!(new File(syncPath)).isDirectory()) {
                (new File(syncPath)).mkdirs();
            }
            TextProgressMonitor progressMonitor = new TextProgressMonitor(new OutputStreamWriter(System.out));
            Git.init().setDirectory(new File(syncPath)).call();
            Git git= Git.open(new File(syncPath));
            Status status = git.status().call();
            if (status.getConflicting().size() != 0) {
                jobScheduleService.syncConflict(id);
                log.warn(LogConstants.LOG_TAG + "sync conflict ");
                return;
            }
            if (!status.isClean()) {
                jobScheduleService.startSync(id);
                log.info(LogConstants.LOG_TAG + " starting exec GitFileSyncJob ");
                ChainingCredentialsProvider credentialsProvider = new ChainingCredentialsProvider(new UsernamePasswordCredentialsProvider("lwenxu", "sh19970618"));
                git.add().addFilepattern(".").call();
                git.commit().setAll(true).setAuthor("git-sync", "xpf199741@outlook.com").setMessage(commitMsg).call();
                git.remoteAdd().setUri(new URIish("https://github.com/" + repository)).setName("origin").call();
                git.push().setCredentialsProvider(credentialsProvider).setRemote("origin").setProgressMonitor(progressMonitor).call();
                progressMonitor.beginTask("aa", 1);
                progressMonitor.update(1);
                log.info(LogConstants.LOG_TAG + "exec  success ");
                jobScheduleService.endSync(id);
            }
        } catch (IOException | GitAPIException | URISyntaxException e) {
            log.error(LogConstants.LOG_TAG + "exec {} error ", e.getMessage());
            e.printStackTrace();
        }
    }

}
