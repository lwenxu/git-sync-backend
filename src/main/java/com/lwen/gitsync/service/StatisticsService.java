package com.lwen.gitsync.service;

import com.alibaba.fastjson.JSON;
import com.lwen.gitsync.dao.StatisticsRepository;
import com.lwen.gitsync.entry.Statistics;
import com.lwen.gitsync.utils.WsResultUtils;
import com.lwen.gitsync.vo.WsResultVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.XMLFormatter;

@Service
public class StatisticsService {
    @Resource
    StatisticsRepository statisticsRepository;

    public Statistics getStatistic() {
        List<Statistics> all = statisticsRepository.findAll();
        Statistics statistics;
        if (all.size() == 0) {
            statistics = statisticsRepository.save(Statistics.builder().failedCount(0).successCount(0).build());
        } else {
            statistics = all.stream().min(Comparator.comparingInt(Statistics::getId)).get();
        }
        return statistics;
    }

    public void success() {
        Statistics statistic = getStatistic();
        statisticsRepository.save(addSuccess(statistic));
        WebSocketService.sendAll(JSON.toJSONString(WsResultUtils.success(statistic.getSuccessCount(), WsResultVO.SYNC_SUCCESS)));
    }

    public void failed() {
        Statistics statistics = getStatistic();
        statisticsRepository.save(addFailed(statistics));
        WebSocketService.sendAll(JSON.toJSONString(WsResultUtils.success(statistics.getFailedCount(), WsResultVO.SYNC_FAILED)));
    }

    private Statistics addFailed(Statistics statistics) {
        statistics.setFailedCount(statistics.getFailedCount() + 1);
        return statistics;
    }

    private Statistics addSuccess(Statistics statistic) {
        statistic.setSuccessCount(statistic.getSuccessCount() + 1);
        return statistic;
    }
}
