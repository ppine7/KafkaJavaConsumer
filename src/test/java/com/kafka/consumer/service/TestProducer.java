package com.kafka.consumer.service;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author marinapopova
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/kafka-consumer-context-test.xml" })
public class TestProducer {

    private KafkaSyncProducer kafkaProducer ;

    @Before
    public void setUp() throws Exception {
    	kafkaProducer = new KafkaSyncProducer("varnish_json_topic");
    }

    @Test
    public void produceOldMessages() {
    	String testAdnId = "55d34bd4f21345415a000fdb";
        for (int i=0 ;i<=10 ;i++) {
            long instantSeconds = this.getEventDateAsInstantSeconds(1);
            String testEvent = this.getTestVarnishJsonRecordV1(
            		instantSeconds, testAdnId, "testProfileId-"+i, "10.10.10.10");
            try {
				kafkaProducer.sendMessage(testEvent);
			} catch (Exception e) {
				fail("Failed to send message #" + i + ": " + e.getMessage());
			}
        }       
    }

    @Test
    public void produceNewMessages() {
    	String adnId = "55d34e63f21345415a00d4ba";
        for (int i=0 ;i<=10 ;i++) {
             try {
				kafkaProducer.sendMessage(TestUtils.getJsonNonFwEventByUUID("testUUID_"+i, adnId));
			} catch (Exception e) {
				fail("Failed to send message #" + i + ": " + e.getMessage());
			}
        }
    }
    
    @After
    public void tearDown() throws Exception {
    	kafkaProducer.closeProducer();
    }

	public Long getEventDateAsInstantSeconds(int ageInDays){
		// get a date - 'ageInDays' old
		ZonedDateTime timeNow = ZonedDateTime.now().minusDays(ageInDays);
		Instant instantSeconds = Instant.from(timeNow);
		return instantSeconds.getEpochSecond();
	}

	public String getTestVarnishJsonRecordV1(long instantSeconds, String adnId, String profileId, String ip) {
		String testLogDomainName = "test.yottaa.com";
		String testLogRecord = "{\"_instantseconds\":" + instantSeconds + ",\"_clientInfo\":{\"_ip\":\"" + ip + "\"," +
			"\"_userAgent\":{\"_type\":\"COMPUTER\",\"_browser\":\"IE\",\"_browserVersion\":" +
			"{\"version\":\"10.0\",\"majorVersion\":\"10\",\"minorVersion\":\"0\"},\"_os\":\"WINDOWS\"," + 
			"\"rawRecord\":\"Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.2; WOW64; Trident/6.0)\"," + 
			"\"valide\":true,\"type\":\"COMPUTER\",\"browser\":\"IE\",\"oS\":\"WINDOWS\"}," + 
			"\"ip\":\"" + ip + "\",\"userAgent\":{\"_type\":\"COMPUTER\",\"_browser\":\"IE\"," + 
			"\"_browserVersion\":{\"version\":\"10.0\",\"majorVersion\":\"10\",\"minorVersion\":\"0\"}," + 
			"\"_os\":\"WINDOWS\",\"rawRecord\":\"Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.2; " + 
			"WOW64; Trident/6.0)\",\"valide\":true,\"type\":\"COMPUTER\",\"browser\":\"IE\"," + 
			"\"oS\":\"WINDOWS\"}},\"_optimizations\":{\"profileId\":\"" + adnId + "\"," + 
			"\"rawRecord\":\"csi/852552021 cni/012136d1462b ob/111 si/849549542 tts/1392916797730 " + 
			"ti/52d77e728b5f02370e04c000 ai/" + profileId + "\",\"valide\":true}," + 
			"\"_metrics\":{\"_lb\":{\"_role\":\"LB\",\"_environmenttype\":\"PRODUCT\",\"_datacenter\":" + 
			"\"USEAST\",\"_publicip\":\"107.23.78.197\",\"rawRecord\":\"01116b174ec5\",\"valide\":true}," + 
			"\"_tpu\":{\"_role\":\"TPU\",\"_environmenttype\":\"PRODUCT\",\"_datacenter\":\"USEAST\"," +
			"\"_publicip\":\"54.209.70.43\",\"rawRecord\":\"012136d1462b\",\"valide\":true},\"rawRecord\":" + 
			"\"012136d1462b/[1,-,1392916892340] 01116b174ec5/[hit]\",\"valide\":true},\"_lb\":{\"_request\":" + 
			"{\"_refer\":\"-\",\"version\":\"HTTP/1.1\",\"method\":\"GET\",\"requestedURL\":" + 
			"\"http://" + testLogDomainName + "/favicon.ico\",\"rawRecord\":\"GET http://" + 
			testLogDomainName + "/favicon.ico HTTP/1.1\"," + 
			"\"valide\":true,\"refer\":\"-\",\"protocol\":\"http\",\"requestMethod\":\"GET\"," + 
			"\"requestedHost\":\"" + testLogDomainName + "\",\"requestedFile\":\"/favicon.ico\",\"requestedPort\":80," + 
			"\"requestVersion\":\"HTTP/1.1\"},\"_response\":{\"_statusCode\":{\"rawRecord\":\"200\"," + 
			"\"valide\":true},\"_contentType\":{\"rawRecord\":\"image/x-icon\",\"valide\":true}," + 
			"\"_transferbytes\":{\"transferBytes\":1621,\"rawRecord\":\"1621\",\"valide\":true}}}," + 
			"\"_id\":\"850920126\",\"_xforwardFor\":{\"addressOfClient\":\"" + ip + "\",\"rawRecord\":" + 
			"\"" + ip + "\",\"valide\":true},\"_firstByteElaspe\":{\"firstByte\":7.9632E-5,\"rawRecord\":" + 
			"\"0.000079632\",\"valide\":true},\"adnId\":\"" + adnId + "\"}";
		return testLogRecord;
	}

}