package com.lwen.gitsync.service;

import com.lwen.gitsync.dao.AccountInfoRepository;
import com.lwen.gitsync.entry.AccountInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.logging.XMLFormatter;

@Service
public class AccountInfoService {
    @Resource
    AccountInfoRepository accountInfoRepository;

    public AccountInfo save(AccountInfo accountInfo) {
        return accountInfoRepository.save(accountInfo);
    }

    public AccountInfo getAccountInfo() {
        return accountInfoRepository.findAll().stream().sorted(Comparator.comparingInt(AccountInfo::getId)).limit(1).findFirst().orElse(AccountInfo.builder().build());
    }

}
