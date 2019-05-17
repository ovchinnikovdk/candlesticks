package com.demo.candlesticks.service;

import com.demo.candlesticks.grouping.GroupingAlgorithm;
import com.demo.candlesticks.model.Candlestick;
import com.demo.candlesticks.model.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/*
 *
 * Created by mi on 16.05.2019
 *
 */
@Component
public class CandlestickService {

    @Autowired
    private RedisTemplate<String, Trade> tradeRedisTemplate;


    public List<Candlestick> aggregate(int period, long from, long to){
        Set<String> keys = tradeRedisTemplate.keys("*");
        List<String> filteredKeys = keys.stream()
                .map(key -> (Long.valueOf(key)))
                .filter(key -> key >= from && key <= to).map(String::valueOf).collect(Collectors.toList());
        List<Trade> trades = new ArrayList<>();
        for (String key : filteredKeys) {
            Long size = tradeRedisTemplate.opsForList().size(key);
            if (size != null) {
                List<Trade> range = tradeRedisTemplate.opsForList().range(key, 0, size);
                if (range != null)
                    trades.addAll(range);
            }
        }
        GroupingAlgorithm algorithm = new GroupingAlgorithm(8);
        return algorithm.group(trades, period, from, to);
    }
}
