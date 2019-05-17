package com.demo.candlesticks;

import com.demo.candlesticks.kafka.Producer;
import com.demo.candlesticks.model.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;
import java.util.Random;

@SpringBootApplication
public class CandlesticksApplication {


	public static void main(String[] args) {

		//SEND RAND INFO TO SERVICE
		new Thread(() -> {
			Producer producer = new Producer();
			for (int i = 0; i < 1000000; i++) {
				if (i % 100 == 0) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				long timestamp = new Date().getTime();
				float price = new Random().nextFloat() * 1000;
				float q = new Random().nextFloat() * 100;
				producer.send(new Trade(String.valueOf(timestamp),
						timestamp,
						String.valueOf(price),
						String.valueOf(q),
						String.valueOf(q * price)));
			}
		}).start();


		//RUN SERVICE
		SpringApplication.run(CandlesticksApplication.class, args);
	}

}
