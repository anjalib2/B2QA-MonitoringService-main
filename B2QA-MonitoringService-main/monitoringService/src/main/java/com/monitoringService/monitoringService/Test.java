package com.monitoringService.monitoringService;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
public class Test {

	public static  String verifyRequest(String requestMethod, String requestUri, String requestBody, String requestTimestamp, String signature, String applicationSecret) {
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
	
	public static void main (String[] args)
	{
		String requestMethod = "POST";
		String requestUri = "https://hubspotforb2.free.beeceptor.com";
		String requestBody = "[{\"eventId\":1760994327,\"subscriptionId\":2031893,\"portalId\":7688272,\"appId\":1266333,\"occurredAt\":1682423360952,\"subscriptionType\":\"contact.propertyChange\",\"attemptNumber\":0,\"objectId\":4748351,\"propertyName\":\"hubspot_owner_id\",\"propertyValue\":\"275395421\",\"changeSource\":\"CRM_UI_BULK_ACTION\",\"sourceId\":\"userId:48456752\"},{\"eventId\":2936173115,\"subscriptionId\":2031893,\"portalId\":7688272,\"appId\":1266333,\"occurredAt\":1682423360952,\"subscriptionType\":\"contact.propertyChange\",\"attemptNumber\":0,\"objectId\":4747756,\"propertyName\":\"hubspot_owner_id\",\"propertyValue\":\"275395421\",\"changeSource\":\"CRM_UI_BULK_ACTION\",\"sourceId\":\"userId:48456752\"}]";
		String timeStamp = "1682423363002";
		String signature = "Lmt/f/AyMQ6K1iBLrreJHg/Eiqvv1vtWjFmyqDdcd3A=";
		String secret = "9a255f78-c0dc-4945-b38c-d9ad8097a920";
		System.out.println(verifyRequest(requestMethod,requestUri,requestBody,timeStamp,signature,secret));
	}
}
