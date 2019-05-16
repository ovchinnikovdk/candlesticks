package com.demo.candlesticks.service;

import com.demo.candlesticks.grouping.GroupingAlgorithm;
import com.demo.candlesticks.model.Candlestick;
import com.demo.candlesticks.model.Trade;
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

    public List<Candlestick> aggregate(int period, long from, long to){
        //TODO: Implement Redis Repository
        List<Trade> trades = new ArrayList<>();
        GroupingAlgorithm algorithm = new GroupingAlgorithm(8);
        return algorithm.group(trades, period, from, to);
    }
}
