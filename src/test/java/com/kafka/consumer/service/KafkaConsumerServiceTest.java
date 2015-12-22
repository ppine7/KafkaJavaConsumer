package com.kafka.consumer.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by dhyan on 12/13/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/kafka-consumer-context-test.xml" })
public class KafkaConsumerServiceTest {
    @Autowired
    private KafkaConsumerService kafkaConsumerService ;

    private KafkaTestMessageProducer KafkaTestMessageProducer ;

    @Before
    public void setUp() throws Exception {
        produceMessage();
    }

    private void produceMessage() {
        ExecutorService pool = Executors.newFixedThreadPool(3);
        //Set<Future<Integer>> set = new HashSet<Future≶Integer>>();
        for (int i=0 ;i<=10 ;i++) {
            Callable<Integer> callable = new KafkaTestMessageProducer(10,"javatest", "testMessage");
            pool.submit(callable);
            //set.add(future);
        }
    }
    @Test
    public void consumeMessage(){
        kafkaConsumerService.process();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testProcess() throws Exception {

    }
}