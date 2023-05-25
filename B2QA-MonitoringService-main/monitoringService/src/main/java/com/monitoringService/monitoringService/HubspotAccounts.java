package com.monitoringService.monitoringService;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import okhttp3.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.codec.binary.Base64;

@RestController
public class HubspotAccounts implements Job{
	public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    public void postRequest(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            System.out.println(response.body().string());
        }
    }
    public static String generateUnixTimestamp() {
        Instant now = Instant.now();
        return Long.toString(now.toEpochMilli());
    }
    public static void callApi(String urlStr, String requestBody, String... headers) throws Exception {
        URL url = new URL(urlStr);
        
        // Open a connection to the URL
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        
        // Set headers for the request
        if(headers.length > 0){
            for(int i=0; i<headers.length-1; i+=2){
                conn.setRequestProperty(headers[i], headers[i+1]);
            }
        }
        
        // Convert the request body to bytes
        byte[] requestBodyBytes = requestBody.getBytes(StandardCharsets.UTF_8);

        // Enable output on the connection
        conn.setDoOutput(true);

        // Write the request body to the connection's output stream
        try (OutputStream os = conn.getOutputStream()) {
            os.write(requestBodyBytes);
            os.flush();
        }

        // Get the response code from the connection
        int statusCode = conn.getResponseCode();

        // Print the response code to the console
        System.out.println("Response code: " + statusCode+" "+conn.getResponseMessage());
        
        // Close the connection
        conn.disconnect();
    }
	Faker faker;
    ConcurrentHashMap<String, AccountData> accountObjects = new ConcurrentHashMap<>();
	 @Override
	    public void execute(JobExecutionContext context) throws JobExecutionException {
		 //create
		 faker = new Faker();
		 ZoneId z = ZoneId.of( "Asia/Kolkata" ) ;  
	     ZonedDateTime date = ZonedDateTime.now( z ) ;
		 String accountId=generateRandomNumber();
		 String eventId=generateRandomNumber();
		 System.out.println("eventId"+eventId);
		 String accountName = faker.company().name().replaceAll("&", "").replaceAll("-", "")
					.replaceAll("@", "").replaceAll("/", "").replaceAll(" ", "").replaceAll(",", "")+"(AUTO)";
		 String accountWebsite = accountName.replaceAll("\\(","").replaceAll("\\)","").toLowerCase() + ".com";
		 String body1 = "[{\"eventId\": <eventId>,\"subscriptionId\": 1850151,\"portalId\": 22122214,\"appId\": 1266333,\"occurredAt\": <occurredAt>,\"subscriptionType\": \"company.propertyChange\",\"attemptNumber\": 0,\"objectId\": <accountId>,\"propertyName\": \"greenfield_account_segment\",\"propertyValue\": \"Strategic Account\",\"changeSource\": \"CRM_UI\",\"sourceId\": \"userId:45427659\"},{\"eventId\": <eventId>,\"subscriptionId\": 1850138,\"portalId\": 22122214,\"appId\": 1266333,\"occurredAt\": <occurredAt>,\"subscriptionType\": \"company.creation\",\"attemptNumber\": 0,\"objectId\": <accountId>,\"changeFlag\": \"CREATED\",\"changeSource\": \"CRM_UI\",\"sourceId\": \"userId:45427659\"},{\"eventId\": <eventId>,\"subscriptionId\": 1850147,\"portalId\": 22122214,\"appId\": 1266333,\"occurredAt\": <occurredAt>,\"subscriptionType\": \"company.propertyChange\",\"attemptNumber\": 0,\"objectId\": <accountId>,\"propertyName\": \"website\",\"propertyValue\": \"<accountWebsite>\",\"changeSource\": \"CALCULATED\"},{\"eventId\": <eventId>,\"subscriptionId\": 1850140,\"portalId\": 22122214,\"appId\": 1266333,\"occurredAt\": <occurredAt>,\"subscriptionType\": \"company.propertyChange\",\"attemptNumber\": 0,\"objectId\": <accountId>,\"propertyName\": \"name\",\"propertyValue\": \"<accountName>\",\"changeSource\": \"CRM_UI\",\"sourceId\": \"userId:45427659\"},{\"eventId\": <eventId>,\"subscriptionId\": 1850148,\"portalId\": 22122214,\"appId\": 1266333,\"occurredAt\": <occurredAt>,\"subscriptionType\": \"company.propertyChange\",\"attemptNumber\": 0,\"objectId\": <accountId>,\"propertyName\": \"domain\",\"propertyValue\": \"<accountWebsite>\",\"changeSource\": \"CRM_UI\",\"sourceId\": \"userId:45427659\"},{\"eventId\": <eventId>,\"subscriptionId\": 2031882,\"portalId\": 22122214,\"appId\": 1266333,\"occurredAt\": <occurredAt>,\"subscriptionType\": \"company.propertyChange\",\"attemptNumber\": 0,\"objectId\": <accountId>,\"propertyName\": \"hubspot_owner_id\",\"propertyValue\": \"190610862\",\"changeSource\": \"CRM_UI\",\"sourceId\": \"userId:45427659\"},{\"eventId\": <eventId>,\"subscriptionId\": 2031866,\"portalId\": 22122214,\"appId\": 1266333,\"occurredAt\": <occurredAt>,\"subscriptionType\": \"company.propertyChange\",\"attemptNumber\": 0,\"objectId\": <accountId>,\"propertyName\": \"createdate\",\"propertyValue\": \"1682422454288\",\"changeSource\": \"CRM_UI\",\"sourceId\": \"userId:45427659\"},{\"eventId\": <eventId>,\"subscriptionId\": 2031881,\"portalId\": 22122214,\"appId\": 1266333,\"occurredAt\": <occurredAt>,\"subscriptionType\": \"company.propertyChange\",\"attemptNumber\": 0,\"objectId\": <accountId>,\"propertyName\": \"hs_object_id\",\"propertyValue\": \"<accountId>\",\"changeSource\": \"CRM_UI\",\"sourceId\": \"userId:45427659\"}]";
		 String body2 = "[\r\n    {\r\n        \"eventId\": <eventId>,\r\n        \"subscriptionId\": 2031883,\r\n        \"portalId\": 22122214,\r\n        \"appId\": 1266333,\r\n        \"occurredAt\": <occurredAt>,\r\n        \"subscriptionType\": \"company.propertyChange\",\r\n        \"attemptNumber\": 0,\r\n        \"objectId\": <accountId>,\r\n        \"propertyName\": \"num_associated_contacts\",\r\n        \"propertyValue\": \"0\",\r\n        \"changeSource\": \"CALCULATED\",\r\n        \"sourceId\": \"RollupProperties\"\r\n    }\r\n]";
	        ConcurrentHashMap<String, Object> properties = new ConcurrentHashMap<>();
	        properties.put("about_us", "null");
	        properties.put("annualrevenue", "null");
	        properties.put("b2sync", true);
	        properties.put("city", "null");
	        properties.put("client_subtype__c", "null");
	        properties.put("closedate", "null");
	        properties.put("company_segment", "null");
	        properties.put("company_type__c", "null");
	        properties.put("country", "null");
	        properties.put("createdate", date.toString());
	        properties.put("description", "null");
	        properties.put("domain", accountWebsite);
	        properties.put("facebook_company_page", "null");
	        properties.put("greenfield_account_segment", "Strategic Account");
	        properties.put("hs_lastmodifieddate", "date");
	        properties.put("hs_object_id", accountId);
	        properties.put("hs_parent_company_id", "null");
	        properties.put("hubspot_owner_id", "190610862");
	        properties.put("industry", "null");
	        properties.put("lifecyclestage", "lead");
	        properties.put("linkedin_company_page", "null");
	        properties.put("name", accountName);
	        properties.put("numberofemployees", "null");
	        properties.put("phone", "null");
	        properties.put("region__c", "null");
	        properties.put("state", "null");
	        properties.put("twitterhandle", "null");
	        properties.put("web_technologies", "null");
	        AccountData accountData=new AccountData(accountId,properties,date.toString(),date.toString(),false); 
	        accountObjects.put(accountId, accountData);
	        while(body1.contains("<eventId>"))
	        {
	        	eventId=generateRandomNumber();
	        body1=body1.replaceFirst("<eventId>", eventId);
	        }
	        body1=body1.replaceAll("<accountId>", accountId);
	        body1=body1.replaceAll("<occurredAt>", generateUnixTimestamp());
	        body1=body1.replaceAll("<accountName>",accountName);
	        body1=body1.replaceAll("<accountWebsite>",accountWebsite);
	        body2=body2.replaceAll("<eventId>", eventId);
	        body2=body2.replaceAll("<accountId>", accountId);
	        body2=body2.replaceAll("<occurredAt>", generateUnixTimestamp());

		 String secret = "9a255f78-c0dc-4945-b38c-d9ad8097a920";
		 String uri="https://b2synchubspotcrmapi.bamboobox.net/incremental-sync";
		 String timeStamp=generateUnixTimestamp();
		 String v3=verifyRequest("POST",uri,body1,timeStamp,secret);
		 System.out.println(calculateSHA256Hash(secret+body1));
		 try {
			callApi(uri, body1,
						"content-type",  "application/json",
						"x-forwarded-host", "b2synchubspotcrmapi.bamboobox.net",
				        "x-forwarded-proto", "https",
				        "x-hubspot-request-timestamp", timeStamp,
				        "x-hubspot-signature", calculateSHA256Hash(secret+body1),
				        "x-hubspot-signature-v3", v3,
				        "x-hubspot-signature-version", "v1",
				        "x-hubspot-timeout-millis", "10000",
				        "x-trace", "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 timeStamp=generateUnixTimestamp();
		 v3=verifyRequest("POST",uri,body2,timeStamp,secret);
		 try {
			 Thread.sleep(100);
			callApi(uri, body2,
						"content-type",  "application/json",
						"x-forwarded-host", "b2synchubspotcrmapi.bamboobox.net",
				        "x-forwarded-proto", "https",
				        "x-hubspot-request-timestamp", timeStamp,
				        "x-hubspot-signature", calculateSHA256Hash(secret+body2),
				        "x-hubspot-signature-v3", v3,
				        "x-hubspot-signature-version", "v1",
				        "x-hubspot-timeout-millis", "10000",
				        "x-trace", "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    }	
	    public static String calculateSHA256Hash(String sourceString) {
	        try {
	            MessageDigest digest = MessageDigest.getInstance("SHA-256");
	            byte[] encodedhash = digest.digest(sourceString.getBytes(StandardCharsets.UTF_8));
	            return bytesToHex(encodedhash);
	        } catch (NoSuchAlgorithmException e) {
	            throw new RuntimeException("Failed to calculate SHA-256 hash", e);
	        }
	    }

	    private static String bytesToHex(byte[] hash) {
	        StringBuilder hexString = new StringBuilder(2 * hash.length);
	        for (byte b : hash) {
	            String hex = Integer.toHexString(0xff & b);
	            if (hex.length() == 1) {
	                hexString.append('0');
	            }
	            hexString.append(hex);
	        }
	        return hexString.toString();
	    }
	    public static String generateRandomNumber() {
	        Random random = new Random();
	        Set<Long> set = new HashSet<>();
	        long number = 0;
	        do {
	            // Generate a random number between 1000000000000000 and 9999999999999999 (inclusive)
	            number = random.nextLong();
	            if (number < 0) {
	                number *= -1; // make the number positive
	            }
	            number %= 9000000000000000L;
	            number += 1000000000000000L;
	        } while (!set.add(number)); // loop until a unique number is generated

	        return Long.toString(number);
	    }

	
		public static  String verifyRequest(String requestMethod, String requestUri, String requestBody, String requestTimestamp, String applicationSecret) {
		    try {
		        // Decode URL-encoded characters in the request URI
		        requestUri = decodeUrlEncodedCharacters(requestUri);
		        
		        // Concatenate the components
		        String concatenated = requestMethod + requestUri + requestBody + requestTimestamp;
		        
		        // Create an HMAC SHA-256 hash of the concatenated string using the application secret
		        byte[] hmac = calculateHmacSHA256(concatenated, applicationSecret);
		        
		        // Base64 encode the result of the HMAC function
		        String calculatedSignature = Base64.encodeBase64String(hmac);
		        
		        // Compare the hash value to the provided signature
		        return calculatedSignature;
		    } catch (UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeyException ex) {
		        throw new RuntimeException("Failed to verify request", ex);
		    }
		}

		private static String decodeUrlEncodedCharacters(String uri) throws UnsupportedEncodingException {
		    return java.net.URLDecoder.decode(uri.replaceAll("\\+", "%20"), "UTF-8");
		}

		private static byte[] calculateHmacSHA256(String data, String key) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
		    Mac mac = Mac.getInstance("HmacSHA256");
		    SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
		    mac.init(keySpec);
		    return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
		}
		
			@PostMapping("/crm/v3/objects/{objectName}/{objectId}")
			public ResponseEntity<?> handledeleteRequest(@PathVariable("objectName") String objectName,
					@PathVariable("objectId") String objectId,
					@RequestParam(value="archived",required = false) String archived,
					@RequestParam(value="properties",required = false) String properties) 
			{
				JsonNode jsonNode = null;
				AccountData obj=accountObjects.get(objectId);
				try {
					ObjectMapper objectMapper = new ObjectMapper();
					// convert JSON string to JsonNode object
					jsonNode = objectMapper
							.readTree(obj.id+obj.properties+obj.createdAt+obj.updatedAt+obj.archived);
				} catch (Exception ex) {
					System.out.println(obj.id+obj.properties+obj.createdAt+obj.updatedAt+obj.archived);
					return ResponseEntity.ok("Failed!");
				}
				System.out.println(obj.id+obj.properties+obj.createdAt+obj.updatedAt+obj.archived);
				return ResponseEntity.ok(jsonNode);
				
			}
		
}
