package com.monitoringService.monitoringService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import org.bson.conversions.Bson;
import org.json.simple.parser.ParseException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.mongodb.client.model.Filters;
import com.monitoringService.mongoDB.MongoFunctions;
import com.montioringService.utilities.Reader;

public class CreateSyncJobs implements Job {
	public void execute(JobExecutionContext context) throws JobExecutionException {
		Bson filter = Filters.or(Filters.regex("Status", "PENDING"), Filters.regex("Status", "IN PROGRESS"));
		HashMap<String, HashMap<String, String>> tenantJobArray = new HashMap<>();
		HashMap<String, String> tenantJob = new HashMap<>();
		LocalDateTime date = LocalDateTime.now();
		String createdAtString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'00:00:00.000'Z'"));
		tenantJobArray = MongoFunctions.mongoFetchAll("Automation", "tenantJobQueue", filter,
				Reader.getData("common", "mongoUrl"));
		for (String eachKey : tenantJobArray.keySet()) {
			tenantJob = tenantJobArray.get(eachKey);
			System.out.println(tenantJob);
			String fileStatus = tenantJob.get("Status");
			String updatedAt = tenantJob.get("updatedAt");
			if (updatedAt == null || !updatedAt.equals(createdAtString)) {
				String crmName = tenantJob.get("CRM");
				System.out.println(crmName);
				if (crmName != null) {
					createSyncJobs(crmName, tenantJob);
				}
			}
		}
		System.out.println("all jobs created!!");

	}

	void createSyncJobs(String crmName, HashMap<String, String> tenantJob) {
		HashMap<String, String> syncJob = new HashMap<>();
		String[] objects = Reader.getData("common", crmName + "Objects").split(",");
		String[] flows = Reader.getData("common", crmName + "Flows").split(",");
		System.out.println(objects);
		System.out.println(flows);
		String createdAt = tenantJob.get("createdAt");
		System.out.println();
		// int diffInUpdatedAt = (Math.abs(epochTime1 - updatedAt));
		int previousOffset = 0;
		for (String eachFlow : flows) {
			for (String eachObject : objects) {
				if ((createdAt == null && !eachFlow.trim().equalsIgnoreCase("incremental sync"))
						|| createdAt != null && !eachFlow.trim().equalsIgnoreCase("bulk sync")) {
					LocalDateTime date = LocalDateTime.now();
					String createdAtString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'00:00:00.000'Z'"));
					HashMap<String, String> tempSyncJob = new HashMap<>();
					Bson filter = Filters.and(Filters.regex("Sync Type", eachFlow.toUpperCase()),
							Filters.regex("Object", eachObject.toUpperCase()),
							Filters.eq("Tenant Id", tenantJob.get("Tenant Id")), Filters.eq("Platform", crmName),
							Filters.eq("createdAt", createdAtString));

					tempSyncJob = MongoFunctions.mongoFetchAllSingleDoc("Automation", "syncJobQueue", filter,
							Reader.getData("common", "mongoUrl"));
					if (tempSyncJob == null) {
						syncJob.put("Status", "PENDING");
						syncJob.put("Tenant Size", tenantJob.get("Tenant Size"));
						syncJob.put("Tenant Id", tenantJob.get("Tenant Id"));
						syncJob.put("Platform", crmName);
						syncJob.put("CDP Count", "0");
						syncJob.put("Response Time", "60");
						syncJob.put("Completed At", "");
						syncJob.put("Sync Type", eachFlow.toUpperCase());
						syncJob.put("Object", eachObject.toUpperCase());
						syncJob.put("Started At", "");
						syncJob.put("GC Count", "");
						syncJob.put("createdAt", createdAtString);
						syncJob.put("Actual Count", LogicEngine.fetchTotalCountFor(eachFlow,eachObject,crmName,
								tenantJob.get("Tenant Size"),tenantJob.get("Tenant Id"),tenantJob.get("Offset"),tenantJob.get("Growth Stage"),Integer.toString(previousOffset)));
						MongoFunctions.insertDocumentIntoMongoDB("Automation", "syncJobQueue", syncJob,
								Reader.getData("common", "mongoUrl"));
						if ((eachObject.equalsIgnoreCase("accounts") && eachFlow.equalsIgnoreCase("incremental sync"))){
							HashMap<String, String> filterForMongoUpdate = new HashMap<>();
							filterForMongoUpdate.put("filterAttribute", "Tenant Id");
							filterForMongoUpdate.put("filterValue", tenantJob.get("Tenant Id"));
							filterForMongoUpdate.put("attribute", "Offset");
							filterForMongoUpdate.put("value", Integer.toString(Integer.parseInt(tenantJob.get("Offset")) + Integer.parseInt(syncJob.get("Actual Count"))));
							MongoFunctions.mongoUpdate("Automation", "tenantJobQueue", filterForMongoUpdate,
									Reader.getData("common", "mongoUrl"));
							previousOffset = Integer.parseInt( tenantJob.get("Offset") );
							tenantJob.remove("Offset");
							tenantJob.put("Offset", filterForMongoUpdate.get("value"));
						}
					} else
						break;
				}
			}
		}
		if (createdAt == null) {
			LocalDateTime date = LocalDateTime.now();
			String createdAtString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'00:00:00.000'Z'"));
			HashMap<String, String> filter = new HashMap<>();
			filter.put("filterAttribute", "Tenant Id");
			filter.put("filterValue", tenantJob.get("Tenant Id"));
			filter.put("attribute", "createdAt");
			filter.put("value", createdAtString);
			MongoFunctions.mongoUpdate("Automation", "tenantJobQueue", filter, Reader.getData("common", "mongoUrl"));
			filter.clear();
			filter.put("filterAttribute", "Tenant Id");
			filter.put("filterValue", tenantJob.get("Tenant Id"));
			filter.put("attribute", "Status");
			filter.put("value", "IN PROGRESS");
			MongoFunctions.mongoUpdate("Automation", "tenantJobQueue", filter, Reader.getData("common", "mongoUrl"));
			filter.clear();
			filter.put("filterAttribute", "Tenant Id");
			filter.put("filterValue", tenantJob.get("Tenant Id"));
			filter.put("attribute", "Offset");
			filter.put("value", Reader.getData("tenantSize_" + tenantJob.get("Tenant Size"), "salesforce_bulk sync_accounts"));
			MongoFunctions.mongoUpdate("Automation", "tenantJobQueue", filter,
					Reader.getData("common", "mongoUrl"));
		} else {
			LocalDateTime date = LocalDateTime.now();
			String createdAtString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'00:00:00.000'Z'"));
			HashMap<String, String> filter = new HashMap<>();
			filter.put("filterAttribute", "Tenant Id");
			filter.put("filterValue", tenantJob.get("Tenant Id"));
			filter.put("attribute", "updatedAt");
			filter.put("value", createdAtString);
			MongoFunctions.mongoUpdate("Automation", "tenantJobQueue", filter, Reader.getData("common", "mongoUrl"));
		}

	}
}
