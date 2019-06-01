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

    public void startSync(int id) {
        JobSchedule jobSchedule = updateStatus(id, 0);
        WebSocketService.sendAll(JSON.toJSONString(WsResultUtils.success(jobSchedule, WsResultVO.SYNC_STATUS_CHANGE)));
    }

    public void endSync(int id) {
        JobSchedule jobSchedule = updateStatus(id, 1);
        WebSocketService.sendAll(JSON.toJSONString(WsResultUtils.success(jobSchedule, WsResultVO.SYNC_STATUS_CHANGE)));
    }

    private JobSchedule updateStatus(int id, int status) {
        JobSchedule jobSchedule = jobScheduleRepository.findById(id).get();
        jobSchedule.setStatus(status);
        return jobScheduleRepository.save(jobSchedule);
    }
}
