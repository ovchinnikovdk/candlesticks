package com.demo.candlesticks.kafka;

import com.demo.candlesticks.model.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;

/*
 *
 * Created by mi on 17.05.2019
 *
 */
public class Consumer {
    @Autowired
    RedisTemplate<String, Trade> tradeRedisTemplate;

    @KafkaListener(topics = "trades")
    public void receive(Trade trade) {
        tradeRedisTemplate.opsForList().leftPush(String.valueOf(trade.getTimestamp()), trade);
    }
}
