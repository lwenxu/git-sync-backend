package com.lwen.gitsync.dao;

import com.lwen.gitsync.entry.AccountInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountInfoRepository extends JpaRepository<AccountInfo, Integer> {
}
