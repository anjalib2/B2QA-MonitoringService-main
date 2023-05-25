package com.monitoringService.monitoringService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.model.Filters;
import com.monitoringService.mongoDB.MongoFunctions;
import com.montioringService.utilities.Reader;

public class FetchAccountIds {
	
	public static void main(String[] args){
		fetchAccountIdsFromMongo("b43c5202-8785-5543-86a1-562497150c0f", 10, 100);
		
	}
	
	
	
	
	public static String fetchAccountIdsFromMongo(String tenantId, int startIndex ,int endIndex)
	{
		
		 String idStart = String.format("%016d", startIndex);
	        String idEnd = String.format("%016d", endIndex);
		ArrayList<String> results = new ArrayList<>();
	        
	   
		Bson filter = new Document("$and", Arrays.asList(new Document("tenantId", "b43c5202-8785-5543-86a1-562497150c0f")
	            .append("Id", 
	        new Document("$lte", idEnd))
	            .append("Id", 
	        new Document("$gte", idStart))));
	    	
	    	results = MongoFunctions.mongoFetchAllID("CDP", "salesforce.accounts", filter, Reader.getData("common", "mongoUrl"));
	        System.out.println(results);
	   
	    return null;
		   
		    
	}

}
