package com.lwen.gitsync.service;

import com.alibaba.fastjson.JSON;
import com.lwen.gitsync.dao.JobScheduleRepository;
import com.lwen.gitsync.entry.JobSchedule;
import com.lwen.gitsync.task.SyncFilesTask;
import com.lwen.gitsync.utils.WsResultUtils;
import com.lwen.gitsync.vo.WsResultVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@Service
public class JobScheduleService {
    @Resource
    SyncFilesTask syncFilesTask;
    @Resource
    WebSocketService webSocketService;

    @Resource
    JobScheduleRepository jobScheduleRepository;

    public JobSchedule save(JobSchedule jobSchedule) {
        JobSchedule schedule = jobScheduleRepository.save(jobSchedule);
        syncFilesTask.start();
        return schedule;
    }

    public List<JobSchedule> getJobSchedule() {
        return jobScheduleRepository.findAll();
    }

    public void delete(int id) {
        jobScheduleRepository.deleteById(id);
    }

    public List<JobSchedule> all() {
        return jobScheduleRepository.findAll();
    }

    /**
     * 正在同步 status 为 0
     * @param id
     */
    public void startSync(int id) {
        updateStatus(id, 0);
    }

    /**
     * 同步完成  status 1
     * @param id
     */
    public void endSync(int id) {
        updateStatus(id, 1);
    }

    /**
     * 出现冲突 status 为 2
     * @param id
     */
    public void syncConflict(int id) {
        updateStatus(id,2);
    }

    private void updateStatus(int id, int status) {
        JobSchedule jobSchedule = jobScheduleRepository.findById(id).get();
        jobSchedule.setStatus(status);
        JobSchedule schedule = jobScheduleRepository.save(jobSchedule);
        WebSocketService.sendAll(JSON.toJSONString(WsResultUtils.success(schedule, WsResultVO.SYNC_STATUS_CHANGE)));
    }

    public void syncingFiles(Set files) {
        WebSocketService.sendAll(JSON.toJSONString(WsResultUtils.success(files, WsResultVO.SYNCING_FILES)));
    }


}
