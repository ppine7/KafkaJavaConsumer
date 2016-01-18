package com.kafka.consumer.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

import org.boon.Boon;

//import com.yottaa.tacommon.logs.model.LogEvent;

public class TestUtils {

	public static String testRequestURL = "http://f6fd96c0a0594639b58117606941b23a.yottaa.org/jscombine/tc001.html?yocs=_&yoloc=us";
	public static String testResponseStatusCode = "403";
	public static String testUserAgent = "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.2; WOW64; Trident/6.0)";  
	public static String testEventUUID = "test-uuid-1234";
	public static String testClientIp = "207.180.168.68";
	public static String testAdnId = "testAdnId";
	public static String testProfileId = "testProfileId";
	public static String testFwBitmap = "100";
	public static String testObBitmap = "111";
	public static String testFwRuleId = "testRuleId";
	public static String testEventDate = "02/Feb/2016:20:01:08 +0000";
	public static String testImageContentTypeStr = "image/x-icon";

	public static DateTimeFormatter varnishLogDateFormatter = DateTimeFormatter
			.ofPattern("dd/MMM/yyyy:HH:mm:ss Z")
			.withLocale(Locale.US)
			.withZone(ZoneId.of("UTC"));

	public static String getLogFromFile(String file) throws Exception {
		try (InputStream input = TestUtils.class.getResourceAsStream(file);
				InputStreamReader ir = new InputStreamReader(input);
				BufferedReader br = new BufferedReader(ir);) {
			String logmsg = br.readLine();
			return logmsg;
		}
	}

	public static String getTestNonFWLog() {
		return TestUtils.createNonFWLogWithParams(testEventDate, testProfileId, testRequestURL, 
				testResponseStatusCode, testUserAgent, testEventUUID, testClientIp, testAdnId, testObBitmap);
	}

	public static String getTestNonFWLogByUUID(String uuid) {
		return TestUtils.createNonFWLogWithParams(testEventDate, testProfileId, testRequestURL, 
				testResponseStatusCode, testUserAgent, uuid, testClientIp, testAdnId, testObBitmap);
	}

	public static String createNonFWLogWithParams(
			String eventDate, String profileId, String requestURL, String responseStatusCode, 
			String userAgent, String eventUUID, String clientIp, String adnId, String obBitmap) {
		// example format of an event date: 02/Feb/2015:20:01:08 +0000
		String testLogRecord = "99.249.202.250 - [" + eventDate + "] " + 
				"\"GET " + requestURL + " HTTP/1.1\" " + responseStatusCode + 
				" 1621 \"https://us.billabong.com/shop/referer\" \"" + 
				userAgent + "\" \"csi/852552021 cni/012136d1462b ob/" + obBitmap + " si/" + eventUUID + 
				" tts/1392916797730 ti/52d77e728b5f02370e04c000 ai/" + profileId + "\" " + 
				"\"012136d1462b/[1,-,1392916892340] 01116b174ec5/[hit]\" \"image/x-icon\" \"4.28.58.19," + 
				clientIp + "\" hit 0.000079632 - " + eventUUID + " \"-\"";		
		return testLogRecord;
	}

	public static String getTestFWLog(){
		return createFWLogWithParams(testEventDate, testRequestURL, testResponseStatusCode, 
				testUserAgent, testEventUUID, testClientIp, testAdnId, testFwBitmap, testFwRuleId);
	}

	public static String getTestFWLogByUUID(String uuid){
		return createFWLogWithParams(testEventDate, testRequestURL, testResponseStatusCode, 
				testUserAgent, uuid, testClientIp, testAdnId, testFwBitmap, testFwRuleId);
	}
	
	public static String getJsonNonFwEventByUUID(String uuid, String adnId){
		String jsonEvent = "{\"eventUUID\":\"" + uuid + "\",\"adnId\":\"" + adnId + 
				"\",\"profileId\":\"testProfileId\",\"eventTimestampMs\":" + getEventDateInMs(0) + 
				",\"requestInfo\":{\"referer\":\"https://us.billabong.com/shop/referer\",\"version\":\"HTTP/1.1\",\"method\":\"GET\",\"protocol\":\"http\",\"requestURL\":\"http://f6fd96c0a0594639b58117606941b23a.yottaa.org/jscombine/tc001.html?yocs=_&yoloc=us\",\"shortRequestURL\":\"http://f6fd96c0a0594639b58117606941b23a.yottaa.org/jscombine/tc001.html\"},\"responseInfo\":{\"responseCode\":403,\"contentType\":{\"type\":\"image\",\"subType\":\"x-icon\",\"rawRecord\":\"image/x-icon\"},\"responseSizeBytes\":1621,\"responseCodeGroup\":\"4xx\"},\"lb\":{\"role\":\"LB\",\"environment\":\"PRODUCTION\",\"dataCenter\":\"USEAST\",\"publicIP\":\"107.23.78.197\"},\"tpu\":{\"role\":\"TPU\",\"environment\":\"PRODUCTION\",\"dataCenter\":\"USEAST\",\"publicIP\":\"54.209.70.43\"},\"clientInfo\":{\"clientIp\":\"4.28.58.19\",\"location\":{\"countryCode\":\"testCountry\",\"region\":\"testRegion\",\"continent\":\"testContinent\",\"coordinates\":{\"lon\":13.12,\"lat\":10.1}},\"userAgent\":{\"deviceType\":\"COMPUTER\",\"browser\":\"IE\",\"browserVersion\":{\"version\":\"10.0\",\"majorVersion\":\"10\",\"minorVersion\":\"0\"},\"os\":\"WINDOWS\",\"rawRecord\":\"Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.2; WOW64; Trident/6.0)\"}},\"yOptimizations\":{\"profileId\":\"testProfileId\"},\"latencyMetrics\":{\"timeToFirstByteMs\":0.079632},\"varnishCacheHit\":\"HIT\",\"taEventVersion\":\"TA1.1\",\"logsType\":\"VARNISH\",\"rawVarnishLog\":\"test\",\"processingTimes\":{\"Varnish2Json\":1451353674728}}";
		return jsonEvent;
	}

	// example FW log:
	// 207.180.168.68 - [08/Dec/2015:12:38:28 +0000] "GET http://f6fd96c0a0594639b58117606941b23a.yottaa.org/jscombine/tc001.html HTTP/1.1" 403 1729 "-" "python-requests/1.2.3 CPython/2.7.10 Darwin/14.5.0" "-" "041236fb8dc0/[-,0.465]" "text/html; charset=utf-8" "4.28.58.19, 207.180.168.68" - 0.000464678 - 041236fb8dc0-1449516700-936326388 "fb/100 tid/0a0e92e3896a495a8d77accd rid/1 stid/54d082aeface69ae640eae6f"
	public static String createFWLogWithParams(
			String eventDate, String requestURL, String responseStatusCode, 
			String userAgent, String eventUUID, String clientIp, String adnId, String fwBitmap,
			String ruleId) {
		String testLogRecord = "207.180.168.68 - [" + eventDate + "] " +
				"\"GET " + requestURL + " HTTP/1.1\" " + responseStatusCode + 
				" 1729 \"https://us.billabong.com/shop/referer\" \"" + userAgent + 
				"\" \"-\" \"041236fb8dc0/[-,0.465]\" " + 
				"\"text/html; charset=utf-8\" \"4.28.58.19," + clientIp + "\" - 0.000464678 - " + 
				eventUUID + " \"fb/" + fwBitmap + " tid/" + adnId + " rid/" + ruleId + 
				" stid/54d082aeface69ae640eae6f\"";
		return testLogRecord;				
	}
	
	public static String getEventDateAsString(int ageInDays){
		String eventDateStr = "";
		// get a date - 'ageInDays' old
		ZonedDateTime timeNow = ZonedDateTime.now().minusDays(ageInDays);
		eventDateStr = timeNow.format(varnishLogDateFormatter);
		return eventDateStr;
	}

	public static Long getEventDateAsInstantSeconds(int ageInDays){
		// get a date - 'ageInDays' old
		ZonedDateTime timeNow = ZonedDateTime.now().minusDays(ageInDays);
		Instant instantSeconds = Instant.from(timeNow);
		return instantSeconds.getEpochSecond();
	}

	public static Long getEventDateInMs(int ageInDays){
		// get a date - 'ageInDays' old
		ZonedDateTime timeNow = ZonedDateTime.now().minusDays(ageInDays);
		Instant instantSeconds = Instant.from(timeNow);
		return instantSeconds.getEpochSecond()*1000;
	}

	/*
	public static String convertToJson(LogEvent log){
		String resultJsonEvent = Boon.toJson(log);
		System.out.println("--- converted JSON: \n" + resultJsonEvent);
		return resultJsonEvent;
	}

	public static LogEvent convertJsonToLogEvent(String jsonEvent){
		LogEvent log = Boon.fromJson(jsonEvent, LogEvent.class);
		System.out.println("--- converted LogEvent: \n" + log);
		return log;
	}
/*  */
}
