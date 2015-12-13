package com.kafka.consumer.process;

import com.kafka.consumer.service.KafkaConsumerService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by dhyan on 12/3/15.
 */
public class StartKafkaConsumer {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/kafka-consumer-context.xml");
        KafkaConsumerService kafkaConsumerService =context.getBean(KafkaConsumerService.class);
        kafkaConsumerService.process();
    }
}
