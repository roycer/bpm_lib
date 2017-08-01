package com.sig.camunda.bpm_dto;

import java.util.Date;

public class TaskDTO {
	private String id;
	private String name;
	private String description;
	private int priority;
	private Date duedate;
	private Date createtime;
	private String processkey;
	private String processname;
	private String taskuniquename;
	private String businesskey;
	private String person;
	private String assignee;
	private String status;
	private String closeby;
	private Date starttime;
	private Date endtime;
	private Integer value;
	private String processid;
	private String usercreator;
	private String userid;
	private String instanceid;
	private String taskid;
	private String executionid;

	public TaskDTO() {
	}

	public TaskDTO(IParameters p) {
		setId(p.getParameterString("bpmtaskid"));
		setUserid((p.getIdpersona() == null) ? null : String.valueOf(p.getIdpersona()));
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPriority() {
		return this.priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public Date getDuedate() {
		return ((Date) ((this.duedate == null) ? null : this.duedate.clone()));
	}

	public void setDuedate(Date duedate) {
		this.duedate = ((Date) ((duedate == null) ? null : duedate.clone()));
	}

	public Date getCreatetime() {
		return ((Date) ((this.createtime == null) ? null : this.createtime.clone()));
	}

	public void setCreatetime(Date createtime) {
		this.createtime = ((Date) ((createtime == null) ? null : createtime.clone()));
	}

	public String getProcesskey() {
		return this.processkey;
	}

	public void setProcesskey(String processkey) {
		this.processkey = processkey;
	}

	public String getProcessname() {
		return this.processname;
	}

	public void setProcessname(String processname) {
		this.processname = processname;
	}

	public String getTaskuniquename() {
		return this.taskuniquename;
	}

	public void setTaskuniquename(String taskuniquename) {
		this.taskuniquename = taskuniquename;
	}

	public String getBusinesskey() {
		return this.businesskey;
	}

	public void setBusinesskey(String businesskey) {
		this.businesskey = businesskey;
	}

	public String getPerson() {
		return this.person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public String getAssignee() {
		return this.assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCloseby() {
		return this.closeby;
	}

	public void setCloseby(String closeby) {
		this.closeby = closeby;
	}

	public Date getStarttime() {
		return this.starttime;
	}

	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}

	public Date getEndtime() {
		return this.endtime;
	}

	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}

	public Integer getValue() {
		return this.value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public String getProcessid() {
		return this.processid;
	}

	public void setProcessid(String processid) {
		this.processid = processid;
	}

	public String getUsercreator() {
		return this.usercreator;
	}

	public void setUsercreator(String usercreator) {
		this.usercreator = usercreator;
	}

	public String getUserid() {
		return this.userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getInstanceid() {
		return this.instanceid;
	}

	public void setInstanceid(String instanceid) {
		this.instanceid = instanceid;
	}

	public String getTaskid() {
		return this.taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getExecutionid() {
		return this.executionid;
	}

	public void setExecutionid(String executionid) {
		this.executionid = executionid;
	}
}
