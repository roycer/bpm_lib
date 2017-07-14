package com.sig.camunda.bpm_dto;

import java.util.Date;

public class TaskDTO {

	private String id;
	private String name;
	private String description;
	private int priority;
	private Date duedate;
	private Date createtime;
	private String processname;
	private String taskuniquename;
	private String businesskey;
	private String person;
	private String assignee;
	private String status;
	private String closeby;
	private Date starttime;
	private Date endtime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public Date getDuedate() {
		return duedate;
	}
	public void setDuedate(Date duedate) {
		this.duedate = duedate;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public String getProcessname() {
		return processname;
	}
	public void setProcessname(String processname) {
		this.processname = processname;
	}
	public String getTaskuniquename() {
		return taskuniquename;
	}
	public void setTaskuniquename(String taskuniquename) {
		this.taskuniquename = taskuniquename;
	}
	public String getBusinesskey() {
		return businesskey;
	}
	public void setBusinesskey(String businesskey) {
		this.businesskey = businesskey;
	}
	public String getPerson() {
		return person;
	}
	public void setPerson(String person) {
		this.person = person;
	}
	public String getAssignee() {
		return assignee;
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCloseby() {
		return closeby;
	}
	public void setCloseby(String closeby) {
		this.closeby = closeby;
	}
	public Date getStarttime() {
		return starttime;
	}
	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}
	public Date getEndtime() {
		return endtime;
	}
	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}
	
	
}
