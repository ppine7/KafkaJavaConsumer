package com.kafka.consumer.service;

// import org.slf4j.Logger;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Created by dhyan on 12/3/15.
 */
@Service
public class KafkaConsumerService {
    private static Logger LOG = Logger.getLogger(KafkaConsumerService.class);
    private Properties consumerProperties;
    private KafkaConsumer<String, String> kafkaConsumer;

    @PostConstruct
    public void init() {
        initializeConsumerProperties();
        initializeConsumer();
    }

    private void initializeConsumer() {
        kafkaConsumer = new KafkaConsumer<String, String>(consumerProperties);
        kafkaConsumer.subscribe(Arrays.asList("javatest"));
    }

    private void initializeConsumerProperties() {
        consumerProperties = new Properties();
        consumerProperties.put("bootstrap.servers", "node-1:9092");
        consumerProperties.put("group.id", "test");
        consumerProperties.put("enable.auto.commit", "true");
        //consumerProperties.put("enable.auto.commit", "false");
        consumerProperties.put("auto.commit.interval.ms", "1000");
        consumerProperties.put("session.timeout.ms", "30000");
        consumerProperties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProperties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    }


    public void process(){
        int commitInterval = 200;
        List<ConsumerRecord<String, String>> buffer = new ArrayList<ConsumerRecord<String, String>>();
        while (true) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                /*buffer.add(record);
                if (buffer.size() >= commitInterval) {
                    //insertIntoDb(buffer);
                    LOG.info("buffer value.."+buffer);
                    kafkaConsumer.commitSync();
                    buffer.clear();
                }*/
                LOG.info("Topic ="+record.topic()+", partition ="+record.partition()+"offset = "+record.offset()+", key = "+record.key()+", value = "+record.value());
                //System.out.printf("offset = %d, key = %s, value = %s", record.offset(), record.key(), record.value());
            }
        }
    }
}
