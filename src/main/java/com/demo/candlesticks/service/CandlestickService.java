package com.demo.candlesticks.service;

import com.demo.candlesticks.model.Candlestick;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/*
 *
 * Created by mi on 16.05.2019
 *
 */
@Component
public class CandlestickService {

    @Autowired
    private RedisTemplate<String, Candlestick> tradeRedisTemplate;


    public List<Candlestick> aggregate(int period, long from, long to){
        List<Candlestick> result = new ArrayList<>();
        for (int i = 0; i < (to - from) / period; i++) {
            List<Candlestick> tmpList = new ArrayList<>();
            for (int j = 0; j < period / 60; j++){
                tmpList.add(tradeRedisTemplate.opsForValue().get(String.valueOf((from + i * period + j) / 60)));
            }
            result.add(groupIntoCandlestick(tmpList));
        }
        return result;
    }

    private Candlestick groupIntoCandlestick(List<Candlestick> list) {
        long timestamp = list.get(0).getTimestamp();
        Candlestick first = list.get(0);
        long last = list.get(0).getTimestamp();
        float open = Float.valueOf(first.getOpen());
        float close = Float.valueOf(first.getClose());
        float high  = Float.valueOf(first.getHigh());
        float low  = Float.valueOf(first.getLow());
        float volume  = 0.0f;
        for (Candlestick c : list){
            float vol = Float.valueOf(c.getVolume());
            float curHigh = Float.valueOf(c.getHigh());
            float curLow = Float.valueOf(c.getLow());
            float curOpen = Float.valueOf(c.getOpen());
            float curClose = Float.valueOf(c.getClose());
            if (timestamp > c.getTimestamp()) {
                timestamp = c.getTimestamp();
                open = curOpen;
            }
            if (last < c.getTimestamp()) {
                last = c.getTimestamp();
                close = curClose;
            }
            if (curHigh > high) {
                high = curHigh;
            }
            if (curLow < low) {
                low = curLow;
            }
            volume += vol;
        }
        return new Candlestick(
          timestamp,
          String.valueOf(open),
          String.valueOf(close),
          String.valueOf(high),
          String.valueOf(low),
          String.valueOf(volume)
        );
    }

}
