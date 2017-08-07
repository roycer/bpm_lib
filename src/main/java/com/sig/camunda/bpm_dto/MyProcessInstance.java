package com.sig.camunda.bpm_dto;

import java.util.Date;

public class MyProcessInstance {
	Date date;
	String instanceid;
	String businesskey;
	String processdefinitionid;
	
	public MyProcessInstance() {

	}
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getInstanceid() {
		return instanceid;
	}
	public void setInstanceid(String instanceid) {
		this.instanceid = instanceid;
	}
	public String getBusinesskey() {
		return businesskey;
	}
	public void setBusinesskey(String businesskey) {
		this.businesskey = businesskey;
	}
	public String getProcessdefinitionid() {
		return processdefinitionid;
	}
	public void setProcessdefinitionid(String processdefinitionid) {
		this.processdefinitionid = processdefinitionid;
	}
	
}
