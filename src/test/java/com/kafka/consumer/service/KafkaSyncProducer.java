package com.kafka.consumer.service;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;

import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.Callable;

/**
 * Created by dhyan on 12/13/15.
 */
public class KafkaSyncProducer{
    private Producer<byte[], byte[]> producer;
    private String testTopic ;
    private Properties props;
    private Random random = new Random();

    public KafkaSyncProducer(String testTopic) {
        this.testTopic = testTopic ;
        props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "0");
        props.put("retries", 0);
        props.put("batch.size", 100);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 16384);

        producer = new KafkaProducer<byte[], byte[]>(props,
                new ByteArraySerializer(), new ByteArraySerializer());
    }

    public void sendMessage(String eventBody) throws Exception {
        String messageKey = String.valueOf(random.nextInt(80000));
        ProducerRecord<byte[], byte[]> record =
                new ProducerRecord<byte[], byte[]>(testTopic, messageKey.getBytes(), eventBody.getBytes());
        producer.send(record);
        System.out.println("sent message OK: >>>\n " + eventBody);
    }
    
    public void closeProducer() {
    	if (producer != null)
    		producer.close();
    }
}
