package com.sig.camunda.bpm_dto;

/**
 * @author Roycer
 * @version 1.0
 */
import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * The Class MyProcessInstance.
 */
public class MyProcessInstance {
	
	/** The date. */
	Date date;
	
	/** The instanceid. */
	String instanceid;
	
	/** The businesskey. */
	String businesskey;
	
	/** The processdefinitionid. */
	String processdefinitionid;
	
	/** The activo. */
	Boolean active;
	
	/**
	 * Gets the active.
	 *
	 * @return the active
	 */
	public Boolean getActive() {
		return active;
	}

	/**
	 * Sets the active.
	 *
	 * @param active the new active
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}

	/** The suspend. */
	Boolean suspend;
	
	/**
	 * Gets the suspend.
	 *
	 * @return the suspend
	 */
	public Boolean getSuspend() {
		return suspend;
	}

	/**
	 * Sets the suspend.
	 *
	 * @param suspend the new suspend
	 */
	public void setSuspend(Boolean suspend) {
		this.suspend = suspend;
	}


	/**
	 * Instantiates a new my process instance.
	 */
	public MyProcessInstance() {

	}
	
	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}
	
	/**
	 * Sets the date.
	 *
	 * @param date the new date
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	
	/**
	 * Gets the instanceid.
	 *
	 * @return the instanceid
	 */
	public String getInstanceid() {
		return instanceid;
	}
	
	/**
	 * Sets the instanceid.
	 *
	 * @param instanceid the new instanceid
	 */
	public void setInstanceid(String instanceid) {
		this.instanceid = instanceid;
	}
	
	/**
	 * Gets the businesskey.
	 *
	 * @return the businesskey
	 */
	public String getBusinesskey() {
		return businesskey;
	}
	
	/**
	 * Sets the businesskey.
	 *
	 * @param businesskey the new businesskey
	 */
	public void setBusinesskey(String businesskey) {
		this.businesskey = businesskey;
	}
	
	/**
	 * Gets the processdefinitionid.
	 *
	 * @return the processdefinitionid
	 */
	public String getProcessdefinitionid() {
		return processdefinitionid;
	}
	
	/**
	 * Sets the processdefinitionid.
	 *
	 * @param processdefinitionid the new processdefinitionid
	 */
	public void setProcessdefinitionid(String processdefinitionid) {
		this.processdefinitionid = processdefinitionid;
	}
	
}
