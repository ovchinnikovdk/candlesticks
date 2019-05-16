package com.demo.candlesticks.controller;

import com.demo.candlesticks.model.Candlestick;
import com.demo.candlesticks.service.CandlestickService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
 *
 * Created by mi on 16.05.2019
 *
 */
@RestController
public class CandlestickController {
    @Autowired
    private CandlestickService service;

    @GetMapping(value = "/candlesticks")
    public List<Candlestick> candlesticks(@PathVariable String resolution,
                                          @PathVariable long from,
                                          @PathVariable long to) {
        int period;
        switch (resolution) {
            case "1m":
                period = 60;
                break;
            case "10m":
                period = 600;
                break;
            case "1h":
                period = 3600;
                break;
                default:
                throw new IllegalArgumentException("Resolution has to be one of ['1m', '10m', '1h']");
        }
        return service.aggregate(period, from, to);
    }
}
