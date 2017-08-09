package com.sig.camunda.bpm_dto;

/**
 * @author Roycer
 * @version 1.0
 */
import java.util.Date;

/**
 * The Class MyEventSubscription.
 */
public class MyEventSubscription {
	
	/** The activity id. */
	String activityId;
	
	/** The created. */
	Date created;
	
	/** The event name. */
	String eventName;
	
	/** The event type. */
	String eventType;
	
	/** The execution id. */
	String executionId;
	
	/** The id. */
	String id;
	
	/** The process instance ID. */
	String processInstanceID;
	
	/**
	 * Instantiates a new my event subscription.
	 */
	public MyEventSubscription() {
	}

	/**
	 * Gets the activity id.
	 *
	 * @return the activity id
	 */
	public String getActivityId() {
		return activityId;
	}

	/**
	 * Sets the activity id.
	 *
	 * @param activityId the new activity id
	 */
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	/**
	 * Gets the created.
	 *
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * Sets the created.
	 *
	 * @param created the new created
	 */
	public void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * Gets the event name.
	 *
	 * @return the event name
	 */
	public String getEventName() {
		return eventName;
	}

	/**
	 * Sets the event name.
	 *
	 * @param eventName the new event name
	 */
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	/**
	 * Gets the event type.
	 *
	 * @return the event type
	 */
	public String getEventType() {
		return eventType;
	}

	/**
	 * Sets the event type.
	 *
	 * @param eventType the new event type
	 */
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	/**
	 * Gets the execution id.
	 *
	 * @return the execution id
	 */
	public String getExecutionId() {
		return executionId;
	}

	/**
	 * Sets the execution id.
	 *
	 * @param executionId the new execution id
	 */
	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the process instance ID.
	 *
	 * @return the process instance ID
	 */
	public String getProcessInstanceID() {
		return processInstanceID;
	}

	/**
	 * Sets the process instance ID.
	 *
	 * @param processInstanceID the new process instance ID
	 */
	public void setProcessInstanceID(String processInstanceID) {
		this.processInstanceID = processInstanceID;
	}
	
	
}
