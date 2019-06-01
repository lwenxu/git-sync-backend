package com.lwen.gitsync.service;

import com.lwen.gitsync.dao.JobLogRepository;
import com.lwen.gitsync.entry.JobLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class JobLogService {
    @Resource
    JobLogRepository jobLogRepository;

    public Page<JobLog> list() {
        int page = 1;
        return jobLogRepository.findAll(new PageRequest(page, 10, new Sort(new Sort.Order(Sort.Direction.DESC, "id"))));
    }
}
