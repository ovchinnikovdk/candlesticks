package com.demo.candlesticks.grouping;

import com.demo.candlesticks.model.Candlestick;
import com.demo.candlesticks.model.Trade;

import java.util.List;
import java.util.concurrent.Callable;

/*
 *
 * Created by mi on 16.05.2019
 *
 */
public class CalculateStickTask implements Callable<Candlestick> {
    private List<Trade> trades;

    public CalculateStickTask(List<Trade> trades) {
        this.trades = trades;
    }

    @Override
    public Candlestick call() throws Exception {
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
        return new Candlestick(timestamp,
                String.valueOf(open),
                String.valueOf(close),
                String.valueOf(high),
                String.valueOf(low),
                String.valueOf(volume));
    }
}
