package com.monitoringService.mongoDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;

public class MongoFunctions {

	  public static String mongoFind(String databaseName, String collectionName, Bson filter, String element, String mongoURL)
	     {
	    	 String result=null;
	    	 try (
	 	        	MongoClient mongoClient = MongoClients.create(mongoURL)) 
	    	 {
	 	             MongoDatabase database = mongoClient.getDatabase(databaseName);
	 	             MongoCollection<Document> collection = database.getCollection(collectionName);
	 	            FindIterable<Document> iterDoc = collection.find(filter);
	            	 if (iterDoc == null) {
	            	     System.out.println("empty!!");
	            	     return result;
	            	 }
	            	 else 
	            	 {
	            	 for(Document doc : iterDoc)
	            	 {
	            		result = doc.get(element).toString();
	                    System.out.println(result);
	            		return result;
	                	 }
	            	 return result;
	            	 }
	 	         }
	 	             catch (MongoException me) {
	 	                 System.err.println("An error occurred while attempting to run a command: " + me);
	 	                 return null;
	 	             }
	         }
	  
//	   public static ArrayList<String> mongoFindID(String databaseName, String collectionName, Bson filter, String element, String mongoURL) {
//	        ArrayList<String> results = new ArrayList<>();
//	        try
//	        (MongoClient mongoClient = MongoClients.create(mongoURL)) {
//	            MongoDatabase database = mongoClient.getDatabase(databaseName);
//	            MongoCollection<Document> collection = database.getCollection(collectionName);
//	            System.out.println(filter);
//	            FindIterable<Document> iterDoc = collection.find(filter);
//	            if (iterDoc == null) {
//	                System.out.println("Empty!!");
//	                return results;
//	            } else {
//	                for (Document doc : iterDoc) {
//	                    String result = doc.get(element).toString();
//	                    results.add(result);
//	                    System.out.println(result);
//	                }
//	                return results;
//	            }
//	        } catch (MongoException me) {
//	            System.err.println("An error occurred while attempting to run a command: " + me);
//	            return null;
//	        }
//	    }
	  
	  
	  
	  public static ArrayList<String> mongoFetchAllID(String databaseName, String collectionName, Bson filter, String mongoURL)
	     {
	    	 HashMap<String,HashMap<String,String>> resultMain=new HashMap<>();
	    	 HashMap <String,String> result ;
	    	 String tenantId;
	    	 ArrayList <String> allIDs = new ArrayList<>();
	    	 try (
	 	        	MongoClient mongoClient = MongoClients.create(mongoURL)) 
	    	 {
	 	             MongoDatabase database = mongoClient.getDatabase(databaseName);
	 	             MongoCollection<Document> collection = database.getCollection(collectionName);
	 	            FindIterable<Document> iterDoc = collection.find(filter);
	            	 if (iterDoc == null) {
	            	     System.out.println("empty!!");
	            	     return null;
	            	 }
	            	 else 
	            	 {
	            	 for(Document doc : iterDoc)
	            	 {
	            		 for(String eachKey:doc.keySet())
	            		 {
	            				 if(eachKey.equalsIgnoreCase("id"))
	            				 {
	            					 allIDs.add(doc.get(eachKey).toString());
	            				 }
	            		 }

	            	 }
	            	 return allIDs;
	            	 }
	 	         }
	 	             catch (MongoException me) {
	 	                 System.err.println("An error occurred while attempting to run a command: " + me);
	 	                 return null;
	 	             }
	         }

	  
	  public static HashMap<String,HashMap<String,String>> mongoFetchAll(String databaseName, String collectionName, Bson filter, String mongoURL)
	     {
	    	 HashMap<String,HashMap<String,String>> resultMain=new HashMap<>();
	    	 HashMap <String,String> result ;
	    	 String tenantId;
	    	 try (
	 	        	MongoClient mongoClient = MongoClients.create(mongoURL)) 
	    	 {
	 	             MongoDatabase database = mongoClient.getDatabase(databaseName);
	 	             MongoCollection<Document> collection = database.getCollection(collectionName);
	 	            FindIterable<Document> iterDoc = collection.find(filter);
	            	 if (iterDoc == null) {
	            	     System.out.println("empty!!");
	            	     return null;
	            	 }
	            	 else 
	            	 {
	            	 for(Document doc : iterDoc)
	            	 {
	            		 result  = new HashMap<>();
	            		 tenantId=doc.getString("Tenant Id").toString();
	            		 for(String eachKey:doc.keySet())
	            		 {
	            			 String value = null;
	            			 try {
	            			 value = doc.getString(eachKey).toString();
	            			 }
	            			 catch(Exception e)
	            			 {
	            				 value = "";
	            			 }
	            			 result.put(eachKey, value);
	            		 }
	            		 resultMain.put(tenantId, result);
	            		 System.out.println(resultMain);
	            	 }
	            	 return resultMain;
	            	 }
	 	         }
	 	             catch (MongoException me) {
	 	                 System.err.println("An error occurred while attempting to run a command: " + me);
	 	                 return null;
	 	             }
	         }
	  public static HashMap<String,String> mongoFetchAllSingleDoc(String databaseName, String collectionName, Bson filter, String mongoURL)
	     {
	    	 HashMap <String,String> result = new HashMap<>();
	    	 try (
	 	        	MongoClient mongoClient = MongoClients.create(mongoURL)) 
	    	 {
	 	             MongoDatabase database = mongoClient.getDatabase(databaseName);
	 	             MongoCollection<Document> collection = database.getCollection(collectionName);
	 	            FindIterable<Document> iterDoc = collection.find(filter);
	            	 if (iterDoc == null) {
	            	     System.out.println("empty!!");
	            	     return null;
	            	 }
	            	 else 
	            	 {
	            	 for(Document doc : iterDoc)
	            	 {
	            		 for(String eachKey:doc.keySet())
	            		 {
	            			 String value = null;
	            			 try {
	            			 value = doc.getString(eachKey).toString();
	            			 }
	            			 catch(Exception e)
	            			 {
	            				 value = "";
	            			 }
	            			 result.put(eachKey, value);
	            		 }
	            		 return result;
	            	 }
	            	return null;
	            	 }
	 	         }
	 	             catch (MongoException me) {
	 	                 System.err.println("An error occurred while attempting to run a command: " + me);
	 	                 return null;
	 	             }
	         }
	  public static void insertDocumentIntoMongoDB(String databaseName, String collectionName, HashMap<String, String> fields,String mongoURL) {

		  try (
	 	        	MongoClient mongoClient = MongoClients.create(mongoURL)) 
	    	 {
	 	             MongoDatabase database = mongoClient.getDatabase(databaseName);
		    // Get the specified MongoDB collection
		    MongoCollection<Document> collection = database.getCollection(collectionName);

		    // Create a new document to store the field-value pairs
		    Document doc = new Document();

		    // Insert each field-value pair into the document
		    for (String fieldName : fields.keySet()) {
		        Object fieldValue = fields.get(fieldName);
		        doc.append(fieldName, fieldValue);
		    }

		    // Insert the document into the collection
		    collection.insertOne(doc);
	    	   }
           catch (MongoException me) {
               System.err.println("An error occurred while attempting to run a command: " + me);
           }
		}
	  public static void mongoUpdate(String databaseName, String collectionName, Map<String, String> query,String mongoURL)
	     {
		  try (
	 	        	MongoClient mongoClient = MongoClients.create(mongoURL)) {

	             MongoDatabase database = mongoClient.getDatabase(databaseName);
	             MongoCollection<Document> collection = database.getCollection(collectionName);
         	 Document mquery = new Document().append(query.get("filterAttribute"), query.get("filterValue"));
         	 String attributeresult = query.get("value").replaceAll("^\"|\"$", "");
         	 Bson updates;
					updates = Updates.combine(Updates.set(query.get("attribute"), attributeresult));
              UpdateOptions options = new UpdateOptions().upsert(true);
              try {
                  UpdateResult result = collection.updateOne(mquery, updates, options);
                  System.out.println("Modified document count: " + result.getModifiedCount());
                  System.out.println("Mongo Update completed!!");
                                   } catch (MongoException me) {
                  System.err.println("Unable to update due to an error: " + me);
              }
          }
	    	 catch (MongoException me) {
              System.err.println("Unable to connect due to an error: " + me);
          }
	     }

	
}
