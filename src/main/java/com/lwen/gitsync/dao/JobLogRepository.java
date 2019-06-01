package com.lwen.gitsync.dao;

import com.lwen.gitsync.entry.JobLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobLogRepository extends JpaRepository<JobLog,Integer> {

}
