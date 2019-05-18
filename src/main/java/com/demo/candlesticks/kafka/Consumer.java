package com.demo.candlesticks.kafka;

import com.demo.candlesticks.model.Candlestick;
import com.demo.candlesticks.model.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/*
 *
 * Created by mi on 17.05.2019
 *
 */
public class Consumer {
    @Autowired
    private RedisTemplate<String, Candlestick> tradeRedisTemplate;
    private Map<Long, List<Trade>> lastCandleSticks;

    public Consumer() {
        this.lastCandleSticks = new TreeMap<>();
    }

    @KafkaListener(topics = "trades")
    public void receive(Trade trade) {
        long minute = trade.getTimestamp() / 60;
        if (lastCandleSticks.get(minute) != null) {
            lastCandleSticks.get(minute).add(trade);
        }
        else {
            ArrayList<Trade> trades = new ArrayList<>();
            trades.add(trade);
            lastCandleSticks.put(minute, trades);
        }
        if (lastCandleSticks.size() > 3) {
            Long oldKey = lastCandleSticks.keySet().iterator().next();
            dropCandlestickToRedis(oldKey, lastCandleSticks.get(oldKey));
            lastCandleSticks.remove(oldKey);
        }
    }

    private void dropCandlestickToRedis(Long minute, List<Trade> trades) {
        long timestamp = trades.get(0).getTimestamp();
        long last = trades.get(0).getTimestamp();
        float firstPrice = Float.valueOf(trades.get(0).getPrice());
        float open = firstPrice;
        float close = firstPrice;
        float high = firstPrice;
        float low = firstPrice;
        float volume = 0.0f;
        for (Trade trade : trades){
            float cost = Float.valueOf(trade.getCost());
            float price = Float.valueOf(trade.getPrice());
            if (timestamp > trade.getTimestamp()) {
                timestamp = trade.getTimestamp();
                open = price;
            }
            if (last < trade.getTimestamp()) {
                last = trade.getTimestamp();
                close = price;
            }
            if (price > high) {
                high = price;
            }
            if (price < low) {
                low = price;
            }
            volume += cost;
        }
        Candlestick candlestick = new Candlestick(timestamp,
                String.valueOf(open),
                String.valueOf(close),
                String.valueOf(high),
                String.valueOf(low),
                String.valueOf(volume));
        tradeRedisTemplate.opsForValue().set(String.valueOf(minute), candlestick);
    }
}
