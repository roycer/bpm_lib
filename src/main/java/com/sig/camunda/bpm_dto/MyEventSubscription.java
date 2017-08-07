package com.sig.camunda.bpm_dto;

import java.util.Date;

public class MyEventSubscription {
	
	String activityId;
	Date created;
	String eventName;
	String eventType;
	String executionId;
	String id;
	String processInstanceID;
	
	public MyEventSubscription() {
	}
	
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getExecutionId() {
		return executionId;
	}
	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProcessInstanceID() {
		return processInstanceID;
	}
	public void setProcessInstanceID(String processInstanceID) {
		this.processInstanceID = processInstanceID;
	}
}
