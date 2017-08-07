package com.sig.camunda.bpm_lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.impl.util.json.JSONObject;
import org.camunda.bpm.engine.runtime.EventSubscription;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.DelegationState;
import org.camunda.bpm.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sig.camunda.bpm_dto.InternalException;
import com.sig.camunda.bpm_dto.MyEventSubscription;
import com.sig.camunda.bpm_dto.MyTask;

public class CamundaEngine implements Camunda{

	private static final Logger LOG = LoggerFactory.getLogger(CamundaEngine.class);

	private ProcessEngine processEngine;
	private RuntimeService runtimeService;
	private TaskService taskService;
	private FormService formService;
	private RepositoryService repositoryService;
	private HistoryService historyService;
	
	public CamundaEngine(){
		this.processEngine = ProcessEngines.getDefaultProcessEngine();
		if (this.processEngine == null) {
			throw new InternalException("DefaultProcessEngine no puede ser NULL");
		}
		this.runtimeService = this.processEngine.getRuntimeService();
		this.taskService = this.processEngine.getTaskService();

		this.formService = this.processEngine.getFormService();
		this.repositoryService = this.processEngine.getRepositoryService();
		this.historyService = this.processEngine.getHistoryService();
		
	}
	
	public CamundaEngine(ProcessEngine processEngine){
		this.processEngine = processEngine;
	}
	
	public CamundaEngine(String processEngineName){
		this.processEngine = ProcessEngines.getProcessEngine(processEngineName);
	}
	
	public String processCreate(String processDefinitionKey, String businessKey, String description, String person) {
		return processCreate(processDefinitionKey,businessKey,description,person,null);
	}

	public String processCreate(String processDefinitionKey, String businessKey, String description, String person, Map<String, Object> variables) {
		
		if (variables == null) 
			variables = new HashMap();	
		if ((!(variables.containsKey("description"))) && (description != null))
			variables.put("description", description);
		if ((!(variables.containsKey("person"))) && (person != null))
			variables.put("person", person);
		
		ProcessInstance processInstance = this.runtimeService.startProcessInstanceByKey(processDefinitionKey,businessKey,variables);
		return processInstance.getId();
	}
	
	public List<String> getProcessInstances(String proccessDefinitionKey){
		List<String> strProcessInstances = new ArrayList<>();
		List<ProcessInstance> processInstances = this.runtimeService.createProcessInstanceQuery().processDefinitionKey(proccessDefinitionKey).list();
		for(ProcessInstance i: processInstances){
			strProcessInstances.add(i.getProcessInstanceId());
		}
		return strProcessInstances;
	}
	
	public List<String> getProcessInstances(){
		List<String> strProcessInstances = new ArrayList<>();
		List<ProcessInstance> processInstances = this.runtimeService.createProcessInstanceQuery().list();
		for(ProcessInstance i: processInstances){
			strProcessInstances.add(i.getProcessInstanceId());
		}
		return strProcessInstances;
	}

	public void processDelete(String bpminstanceid) {
		this.runtimeService.deleteProcessInstance(bpminstanceid, null);
	}

	public void instanceSetVariableByTaskId(String bpmtaskid, String key, Object value) {
		Task t = this.taskService.createTaskQuery().taskId(bpmtaskid).singleResult();
		this.runtimeService.setVariable(t.getExecutionId(), key, value);
	}

	public void instanceSetVariableByTaskId(String bpmtaskid, Map<String, Object> variables) {
		Task t = this.taskService.createTaskQuery().taskId(bpmtaskid).singleResult();
		this.runtimeService.setVariables(t.getExecutionId(), variables);
	}

	public List<MyTask> taskListByUser(String person) {
		List<Task> tasks = this.taskService.createTaskQuery().taskAssignee(person).list();
		List<MyTask> MyTasks = new ArrayList<MyTask>();
		for(Task i:tasks)
			MyTasks.add(convertTask(i));
		return MyTasks;
	}
	
	public List<MyTask> taskListByUserAndInstanceId(String processInstanceId, String person) {
		List<Task> tasks = this.taskService.createTaskQuery().processInstanceId(processInstanceId).taskAssignee(person).list();
		List<MyTask> MyTasks = new ArrayList<MyTask>();
		for(Task i:tasks)
			MyTasks.add(convertTask(i));
		return MyTasks;
	}

	public List<MyTask> taskListByProcessInstanceId(String processInstanceId) {
		List<Task> tasks = this.taskService.createTaskQuery().processInstanceId(processInstanceId).list();
		List<MyTask> MyTasks = new ArrayList<MyTask>();
		for(Task i:tasks)
			MyTasks.add(convertTask(i));
		return MyTasks;
	}

	public List<MyTask> historyTaskListByUser(String processInstanceId, String person) {
		List<HistoricTaskInstance> historicTaskInstances =	this.historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).taskAssignee(person).list();
		List<MyTask> MyTasks = new ArrayList<MyTask>();
		for(HistoricTaskInstance i:historicTaskInstances){
			MyTasks.add(convetHistoricTask(i));
		}
		return MyTasks;
	}

	public void taskComplete(String bpmtaskid) {
		taskComplete(bpmtaskid,null);
	}

	public void taskComplete(String bpmtaskid, String varKey, Object varValue) {
		Map<String,Object> variables = new HashMap<String, Object>();;
		variables.put(varKey, varValue);
		taskComplete(bpmtaskid,variables);
	}

	public void taskComplete(String bpmtaskid, Map<String, Object> variables) {
		DelegationState state = this.taskService.createTaskQuery().taskId(bpmtaskid).singleResult().getDelegationState();
		if (DelegationState.PENDING.equals(state))
			this.taskService.resolveTask(bpmtaskid, variables);
		else
			this.taskService.complete(bpmtaskid, variables);
	}

	public JSONObject taskGetForm(String bpmtaskid) {
		JSONObject jsonObject = new JSONObject();
		TaskFormData taskFormData = this.formService.getTaskFormData(bpmtaskid);
		jsonObject.put("form", taskFormData.getFormKey());
		jsonObject.put("fields", taskFormData.getFormFields());
		return jsonObject;
	}

	public void taskClaim(String bpmtaskid, String person) {
		this.taskService.claim(bpmtaskid, person);
	}

	public void taskAssignee(String bpmtaskid, String person) {
		this.taskService.setAssignee(bpmtaskid, person);
	}

	public void taskUpdateDescription(String bpmtaskid, String description) {
		
	}

	public void taskDelegate(String bpmtaskid, String person) {
		this.taskService.delegateTask(bpmtaskid,person);
	}

	public boolean updateDescription(String processDefinitionKey, String businessKey, String description) {
		List<Execution> executions = this.runtimeService.createExecutionQuery().processDefinitionKey(processDefinitionKey).processInstanceBusinessKey(businessKey).list();
		if (executions.size() > 0) {
			Execution execution = (Execution) executions.get(0);
			this.runtimeService.setVariable(execution.getId(), "description", description);
			return true;
		}
		return false;
	}

	public boolean updateDescriptionAndPerson(String processDefinitionKey, String businessKey, String description, String person) {
		List<Execution> executions = this.runtimeService.createExecutionQuery().processDefinitionKey(processDefinitionKey).processInstanceBusinessKey(businessKey).list();
		if (executions.size() > 0) {
			Execution execution = (Execution) executions.get(0);
			this.runtimeService.setVariable(execution.getId(), "description", description);
			this.runtimeService.setVariable(execution.getId(), "person", person);
			return true;
		}
		return false;
	}

	
	public List<MyEventSubscription> getEvents(String processInstanceId) {
		List<EventSubscription> eventSubscriptions = this.runtimeService.createEventSubscriptionQuery().processInstanceId(processInstanceId).eventType("message").list();
		List<MyEventSubscription> myEventSubscriptions = new ArrayList<>();
		for(EventSubscription i : eventSubscriptions)
			myEventSubscriptions.add(convertEventSubscription(i));
		return myEventSubscriptions;
	}
	
	public void fireEvent(MyEventSubscription myEventSubscription){
		this.runtimeService.messageEventReceived(myEventSubscription.getEventName(), myEventSubscription.getExecutionId());
	}
	
	public boolean deleteInstance(String processDefinitionKey, String businessKey) {
		List<ProcessInstance> processInstances = this.runtimeService.createProcessInstanceQuery().processDefinitionKey(processDefinitionKey).processInstanceBusinessKey(businessKey).list();
		if (processInstances.size() > 0) {
			ProcessInstance pi = (ProcessInstance) processInstances.get(0);
			this.runtimeService.deleteProcessInstance(pi.getProcessInstanceId(), "Eliminado por el usuario");
			return true;
		}
		return false;
	} 

	public boolean suspendInstance(String processDefinitionKey, String businessKey) {
		List<ProcessInstance> processInstances = this.runtimeService.createProcessInstanceQuery().processDefinitionKey(processDefinitionKey).processInstanceBusinessKey(businessKey).list();
		if (processInstances.size() > 0) {
			ProcessInstance pi = (ProcessInstance) processInstances.get(0);
			this.runtimeService.suspendProcessInstanceById(pi.getProcessInstanceId());
			return true;
		}
		return false;
	}

	public boolean activateInstance(String processDefinitionKey, String businessKey) {
		List<ProcessInstance> processInstances = this.runtimeService.createProcessInstanceQuery().processDefinitionKey(processDefinitionKey).processInstanceBusinessKey(businessKey).list();
		if (processInstances.size() > 0) {
			ProcessInstance pi = (ProcessInstance) processInstances.get(0);
			this.runtimeService.activateProcessInstanceById(pi.getProcessInstanceId());
			return true;
		}
		return false;
	}

	private MyTask convertTask(Task task){		
		MyTask myTask = new MyTask();
		myTask.setId(task.getId());
		myTask.setName(task.getName());
		myTask.setDescription(task.getDescription());
		myTask.setPriority(task.getPriority());
		myTask.setDuedate(task.getDueDate());
		myTask.setCreatetime(task.getCreateTime());
		myTask.setProcessname(task.getProcessDefinitionId());
		myTask.setTaskuniquename(task.getTaskDefinitionKey());
		myTask.setAssignee(task.getAssignee());
		return myTask;
	}
	
	private MyTask convetHistoricTask(HistoricTaskInstance historicTaskInstance){
		MyTask myTask = new MyTask();
		myTask.setId(historicTaskInstance.getId());
		myTask.setName(historicTaskInstance.getName());
		myTask.setDescription(historicTaskInstance.getDescription());
		myTask.setPriority(historicTaskInstance.getPriority());
		myTask.setDuedate(historicTaskInstance.getDueDate());
		myTask.setProcessname(historicTaskInstance.getProcessDefinitionId());
		myTask.setTaskuniquename(historicTaskInstance.getTaskDefinitionKey());
		myTask.setAssignee(historicTaskInstance.getAssignee());
		myTask.setStarttime(historicTaskInstance.getStartTime());
		myTask.setEndtime(historicTaskInstance.getEndTime());
		return myTask;
	}
	
	private MyEventSubscription convertEventSubscription(EventSubscription eventSubscription){
		MyEventSubscription myEventSubscription = new MyEventSubscription();
		myEventSubscription.setId(eventSubscription.getId());
		myEventSubscription.setActivityId(eventSubscription.getActivityId());
		myEventSubscription.setCreated(eventSubscription.getCreated());
		myEventSubscription.setEventName(eventSubscription.getEventName());
		myEventSubscription.setEventType(eventSubscription.getEventType());
		myEventSubscription.setExecutionId(eventSubscription.getExecutionId());
		myEventSubscription.setProcessInstanceID(eventSubscription.getProcessInstanceId());
		return myEventSubscription;
	}

}
