package com.lwen.gitsync.controller;

import com.lwen.gitsync.constants.ResultCodeEnum;
import com.lwen.gitsync.entry.AccountInfo;
import com.lwen.gitsync.entry.JobSchedule;
import com.lwen.gitsync.service.JobScheduleService;
import com.lwen.gitsync.service.AccountInfoService;
import com.lwen.gitsync.utils.ResultUtils;
import com.lwen.gitsync.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class HomeController {
    Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Resource
    AccountInfoService accountInfoService;
    @Resource
    JobScheduleService jobScheduleService;

    @PostMapping("/settings")
    public ResultVo saveSettings(@RequestBody AccountInfo params) {
        logger.debug("request params is {}",params);
        AccountInfo accountInfo = accountInfoService.save(params);
        return ResultUtils.success(accountInfo);
    }

    @GetMapping("/settings")
    public ResultVo getSettings() {
        return ResultUtils.ret(ResultCodeEnum.SETTINGS_GET_SUCCESS, accountInfoService.getAccountInfo());
    }

    @PostMapping("/syncDirs")
    public ResultVo saveSyncDirs(@RequestBody JobSchedule params) {
        logger.debug("request params is {}",params);
        JobSchedule schedule = jobScheduleService.save(params);
        return ResultUtils.success(schedule);
    }

    @GetMapping("/syncDirs")
    public ResultVo getSyncDirs() {
        return ResultUtils.ret(ResultCodeEnum.SETTINGS_GET_SUCCESS, jobScheduleService.getJobSchedule());
    }

    @DeleteMapping("/syncDirs/id/{id}")
    public ResultVo deleteSyncDirs(@PathVariable("id") int id) {
        jobScheduleService.delete(id);
        return ResultUtils.ret(ResultCodeEnum.SYNC_DIR_DELETE_SUCCESS, null);
    }

}
