package com.demo.candlesticks.grouping;

import com.demo.candlesticks.model.Candlestick;
import com.demo.candlesticks.model.Trade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;

/*
 *
 * Created by mi on 16.05.2019
 *
 */
public class GroupingAlgorithm {
    private ExecutorService executor;

    public GroupingAlgorithm(int threadNum) {
        executor = Executors.newFixedThreadPool(threadNum);
    }

    public List<Candlestick> group(List<Trade> trades, int period, long start, long stop) {
        List<FutureTask<Candlestick>> result = new ArrayList<>();
        Collections.sort(trades);
        int curIdx = 0;
        for (int i = 0; i < (stop - start) / period; i++) {
            List<Trade> group = new ArrayList<>();
            while (curIdx < trades.size() && trades.get(curIdx).getTimestamp() >= start + i * period &&
            trades.get(curIdx).getTimestamp() < start + (i + 1) * period){
                group.add(trades.get(0));
                curIdx++;
            }
            FutureTask<Candlestick> task = new FutureTask<>(new CalculateStickTask(group));
            result.add(task);
            executor.execute(task);
        }
        boolean isDone = false;
        while (!isDone) {
            isDone = result.get(0).isDone();
            for (FutureTask<Candlestick> task : result) {
                isDone = isDone && task.isDone();
                if (!task.isDone()) break;
            }
        }
         return result.stream().map(candlestickFutureTask -> {
            try {
                return candlestickFutureTask.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
    }
}
