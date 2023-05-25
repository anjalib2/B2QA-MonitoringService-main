package com.monitoringService.monitoringService;

import java.util.concurrent.ConcurrentHashMap;

public class AccountData {
	public String id;
    public ConcurrentHashMap<String, Object> properties;
    public String createdAt;
    public String updatedAt;
    public boolean archived;

    public AccountData(String id, ConcurrentHashMap<String, Object> properties, String createdAt,
                  String updatedAt, boolean archived) {
        this.id = id;
        this.properties = properties;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.archived = archived;
    }
}
