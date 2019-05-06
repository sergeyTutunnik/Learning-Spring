package com.example.springkafkademo;

import org.springframework.kafka.annotation.KafkaListener;

public class ConsumerExample {

    @KafkaListener(topics = "test", groupId = "listen")
    public void listen(String message) {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Received message in group listen: " + message);
    }
}
