package com.lwen.gitsync.dao;

import com.lwen.gitsync.entry.JobSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobScheduleRepository extends JpaRepository<JobSchedule,Integer> {
}
