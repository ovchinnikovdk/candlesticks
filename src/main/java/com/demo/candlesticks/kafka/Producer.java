package com.demo.candlesticks.kafka;

import com.demo.candlesticks.model.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

/*
 *
 * Created by mi on 17.05.2019
 *
 */
public class Producer {
    @Autowired
    private KafkaTemplate<String, Trade> kafkaTemplate;

    public void send(Trade trade) {
        kafkaTemplate.send("trades", trade);
    }
}
