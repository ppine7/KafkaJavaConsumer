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
public class KafkaTestMessageProducer implements Callable {
    private Producer<byte[], byte[]> producer;
    private int totalEvents;
    private String testTopic ;
    private Properties props;
    private Random random = new Random();

    public KafkaTestMessageProducer(int totalEvents,String testTopic) {
        this.totalEvents = totalEvents;
        this.testTopic = testTopic ;
        props = new Properties();
        props.put("bootstrap.servers", "node-1:9092");
        props.put("acks", "0");
        props.put("retries", 0);
        props.put("batch.size", 100);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 16384);

        producer = new KafkaProducer<byte[], byte[]>(props,
                new ByteArraySerializer(), new ByteArraySerializer());
    }

    public KafkaTestMessageProducer(int totalEvents ,Properties props,String testTopic) {
        this.totalEvents = totalEvents;
        this.testTopic = testTopic ;
        this.props = props ;
        producer = new KafkaProducer<byte[], byte[]>(this.props,
                new ByteArraySerializer(), new ByteArraySerializer());
    }


    /*public void run() {

        for(int i=0; i<totalEvents; i++) {
            long timeInMs = new Date().getTime();

            String eventNum = String.valueOf(random.nextInt(80000));
            String msg = "timestamp:" + timeInMs + ", eventType: " + random.nextInt(5);

            ProducerRecord<byte[], byte[]> record =
                    new ProducerRecord<byte[], byte[]>("events", eventNum.getBytes(), msg.getBytes());
            System.out.println("sending " + i + " msg >>> " + msg);
            producer.send(record);
        }

        producer.close();
    }*/

    @Override
    public Object call() throws Exception {
        long timeInMs = new Date().getTime();

        String eventNum = String.valueOf(random.nextInt(80000));
        String msg = "timestamp:" + timeInMs + ", eventType: " + random.nextInt(5);

        ProducerRecord<byte[], byte[]> record =
                new ProducerRecord<byte[], byte[]>(testTopic, eventNum.getBytes(), msg.getBytes());
        System.out.println("sending " + " msg >>> " + msg);
        producer.send(record);
        //producer.close();
        return null;
    }
}
