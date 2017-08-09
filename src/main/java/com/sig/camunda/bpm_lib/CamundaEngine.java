package com.sig.camunda.bpm_lib;

/**
 * @author Roycer
 * @version 1.0
 */
import java.util.ArrayList;
import java.util.Date;
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
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.EventSubscription;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.DelegationState;
import org.camunda.bpm.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sig.camunda.bpm_dto.MyEventSubscription;
import com.sig.camunda.bpm_dto.MyProcessInstance;
import com.sig.camunda.bpm_dto.MyTask;

public class CamundaEngine implements Camunda{

	private static final Logger LOG = LoggerFactory.getLogger(CamundaEngine.class);

	private ProcessEngine processEngine;
	private RuntimeService runtimeService;
	private TaskService taskService;
	private FormService formService;
	private RepositoryService repositoryService;
	private HistoryService historyService;
	
	/**
	 * Inicializa los servicios de Camunda
	 */
	public CamundaEngine(){
		this.processEngine = ProcessEngines.getDefaultProcessEngine();
		this.runtimeService = this.processEngine.getRuntimeService();
		this.taskService = this.processEngine.getTaskService();
		this.formService = this.processEngine.getFormService();
		this.repositoryService = this.processEngine.getRepositoryService();
		this.historyService = this.processEngine.getHistoryService();
	}
	
	/**
	 * Inicializa los servicios de Camunda.
	 *
	 * @param processEngine the process engine
	 */
	public CamundaEngine(ProcessEngine processEngine){
		this.processEngine = processEngine;
	}
	
	/**
	 * Inicializa los servicios de Camunda.
	 *
	 * @param processEngineName the process engine name
	 */
	public CamundaEngine(String processEngineName){
		this.processEngine = ProcessEngines.getProcessEngine(processEngineName);
	}
	
	/**
	 * @inheritDoc
	 */
	public List<String> getProcessDefinitions(){
		List<String> strProcessDefinitions = new ArrayList<>();
		List<ProcessDefinition> processDefinitions = this.repositoryService.createProcessDefinitionQuery().active().latestVersion().list();
		for(ProcessDefinition i : processDefinitions)
			strProcessDefinitions.add(i.getKey());
		return strProcessDefinitions;
	}
	
	/**
	 * @inheritDoc
	 */
	public String processCreate(String processDefinitionKey, String businessKey, String description, String person) {
		return processCreate(processDefinitionKey,businessKey,description,person,null);
	}

	/**
	 * @inheritDoc
	 */
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
	
	/**
	 * @inheritDoc
	 */
	public List<MyProcessInstance> getProcessInstances(String proccessDefinitionKey){
		List<MyProcessInstance> myProcessInstances = new ArrayList<>();
		List<ProcessInstance> processInstances = this.runtimeService.createProcessInstanceQuery().processDefinitionKey(proccessDefinitionKey).active().list();
		for(ProcessInstance i: processInstances){
			myProcessInstances.add(convertProcessInstance(i));
		}
		return myProcessInstances;
	}
	
	/**
	 * @inheritDoc
	 */
	public List<MyProcessInstance> getProcessInstances(){
		List<MyProcessInstance> myProcessInstances = new ArrayList<>();
		List<ProcessInstance> processInstances = this.runtimeService.createProcessInstanceQuery().active().list();
		for(ProcessInstance i: processInstances){
			myProcessInstances.add(convertProcessInstance(i));
		}
		return myProcessInstances;
	}

	/**
	 * @inheritDoc
	 */
	public void processDelete(String bpminstanceid) {
		this.runtimeService.deleteProcessInstance(bpminstanceid, null);
	}

	/**
	 * @inheritDoc
	 */
	public void instanceSetVariable(String processInstanceId, String key, Object value){
		Map<String,Object> variables = new HashMap<String, Object>();;
		variables.put(key, value);
		instanceSetVariable(processInstanceId, variables);
	}
	
	/**
	 * @inheritDoc
	 */
	public void instanceSetVariable(String processInstanceId, Map<String, Object> variables){
		this.runtimeService.setVariables(processInstanceId, variables);
	}
	
	/**
	 * @inheritDoc
	 */
	public void instanceSetVariableByTaskId(String bpmtaskid, String key, Object value) {
		Task t = this.taskService.createTaskQuery().taskId(bpmtaskid).singleResult();
		this.runtimeService.setVariable(t.getExecutionId(), key, value);
	}

	/**
	 * @inheritDoc
	 */
	public void instanceSetVariableByTaskId(String bpmtaskid, Map<String, Object> variables) {
		Task t = this.taskService.createTaskQuery().taskId(bpmtaskid).singleResult();
		this.runtimeService.setVariables(t.getExecutionId(), variables);
	}

	/**
	 * @inheritDoc
	 */
	public List<MyTask> taskListByUser(String person) {
		List<Task> tasks = this.taskService.createTaskQuery().taskAssignee(person).active().list();
		List<MyTask> MyTasks = new ArrayList<MyTask>();
		for(Task i:tasks)
			MyTasks.add(convertTask(i));
		return MyTasks;
	}
	
	/**
	 * @inheritDoc
	 */
	public List<MyTask> taskListByUserAndInstanceId(String processInstanceId, String person) {
		List<Task> tasks = this.taskService.createTaskQuery().processInstanceId(processInstanceId).taskAssignee(person).active().list();
		List<MyTask> MyTasks = new ArrayList<MyTask>();
		for(Task i:tasks)
			MyTasks.add(convertTask(i));
		return MyTasks;
	}

	/**
	 * @inheritDoc
	 */
	public List<MyTask> taskListByProcessInstanceId(String processInstanceId) {
		List<Task> tasks = this.taskService.createTaskQuery().processInstanceId(processInstanceId).active().list();
		List<MyTask> MyTasks = new ArrayList<MyTask>();
		for(Task i:tasks)
			MyTasks.add(convertTask(i));
		return MyTasks;
	}

	/**
	 * @inheritDoc
	 */
	public List<MyTask> historyTaskListByUser(String processInstanceId, String person) {
		List<HistoricTaskInstance> historicTaskInstances =	this.historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).taskAssignee(person).list();
		List<MyTask> MyTasks = new ArrayList<MyTask>();
		for(HistoricTaskInstance i:historicTaskInstances){
			MyTasks.add(convetHistoricTask(i));
		}
		return MyTasks;
	}

	/**
	 * @inheritDoc
	 */
	public void taskComplete(String bpmtaskid) {
		taskComplete(bpmtaskid,null);
	}

	/**
	 * @inheritDoc
	 */
	public void taskComplete(String bpmtaskid, String varKey, Object varValue) {
		Map<String,Object> variables = new HashMap<String, Object>();;
		variables.put(varKey, varValue);
		taskComplete(bpmtaskid,variables);
	}

	/**
	 * @inheritDoc
	 */
	public void taskComplete(String bpmtaskid, Map<String, Object> variables) {
		DelegationState state = this.taskService.createTaskQuery().taskId(bpmtaskid).singleResult().getDelegationState();
		if (DelegationState.PENDING.equals(state))
			this.taskService.resolveTask(bpmtaskid, variables);
		else
			this.taskService.complete(bpmtaskid, variables);
	}

	/**
	 * @inheritDoc
	 */
	public JSONObject taskGetForm(String bpmtaskid) {
		JSONObject jsonObject = new JSONObject();
		TaskFormData taskFormData = this.formService.getTaskFormData(bpmtaskid);
		jsonObject.put("form", taskFormData.getFormKey());
		jsonObject.put("fields", taskFormData.getFormFields());
		return jsonObject;
	}

	/**
	 * @inheritDoc
	 */
	public void taskClaim(String bpmtaskid, String person) {
		this.taskService.claim(bpmtaskid, person);
	}

	/**
	 * @inheritDoc
	 */
	public void taskAssignee(String bpmtaskid, String person) {
		this.taskService.setAssignee(bpmtaskid, person);
	}

	/**
	 * @inheritDoc
	 */
	public void taskUpdateDescription(String bpmtaskid, String description) {
		
	}

	/**
	 * @inheritDoc
	 */
	public void taskDelegate(String bpmtaskid, String person) {
		this.taskService.delegateTask(bpmtaskid,person);
	}

	/**
	 * @inheritDoc
	 */
	public boolean updateDescription(String processDefinitionKey, String businessKey, String description) {
		List<Execution> executions = this.runtimeService.createExecutionQuery().processDefinitionKey(processDefinitionKey).processInstanceBusinessKey(businessKey).list();
		if (executions.size() > 0) {
			Execution execution = (Execution) executions.get(0);
			this.runtimeService.setVariable(execution.getId(), "description", description);
			return true;
		}
		return false;
	}

	/**
	 * @inheritDoc
	 */
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

	/**
	 * @inheritDoc
	 */
	public List<MyEventSubscription> getEvents(String processInstanceId) {
		List<EventSubscription> eventSubscriptions = this.runtimeService.createEventSubscriptionQuery().processInstanceId(processInstanceId).eventType("message").list();
		List<MyEventSubscription> myEventSubscriptions = new ArrayList<>();
		for(EventSubscription i : eventSubscriptions)
			myEventSubscriptions.add(convertEventSubscription(i));
		return myEventSubscriptions;
	}
	
	/**
	 * @inheritDoc
	 */
	public void fireEvent(MyEventSubscription myEventSubscription){
		this.runtimeService.messageEventReceived(myEventSubscription.getEventName(), myEventSubscription.getExecutionId());
	}
	
	/**
	 * @inheritDoc
	 */
	public boolean deleteInstance(String processDefinitionKey, String businessKey) {
		List<ProcessInstance> processInstances = this.runtimeService.createProcessInstanceQuery().processDefinitionKey(processDefinitionKey).processInstanceBusinessKey(businessKey).active().list();
		if (processInstances.size() > 0) {
			ProcessInstance pi = (ProcessInstance) processInstances.get(0);
			this.runtimeService.deleteProcessInstance(pi.getProcessInstanceId(), "Eliminado por el usuario");
			return true;
		}
		return false;
	} 

	/**
	 * @inheritDoc
	 */
	public boolean suspendInstance(String processDefinitionKey, String businessKey) {
		List<ProcessInstance> processInstances = this.runtimeService.createProcessInstanceQuery().processDefinitionKey(processDefinitionKey).processInstanceBusinessKey(businessKey).active().list();
		if (processInstances.size() > 0) {
			ProcessInstance pi = (ProcessInstance) processInstances.get(0);
			this.runtimeService.suspendProcessInstanceById(pi.getProcessInstanceId());
			return true;
		}
		return false;
	}

	/**
	 * @inheritDoc
	 */
	public boolean activateInstance(String processDefinitionKey, String businessKey) {
		List<ProcessInstance> processInstances = this.runtimeService.createProcessInstanceQuery().processDefinitionKey(processDefinitionKey).processInstanceBusinessKey(businessKey).active().list();
		if (processInstances.size() > 0) {
			ProcessInstance pi = (ProcessInstance) processInstances.get(0);
			this.runtimeService.activateProcessInstanceById(pi.getProcessInstanceId());
			return true;
		}
		return false;
	}

	/**
	 * @inheritDoc
	 */
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
	
	/**
	 * @inheritDoc
	 */
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
	
	/**
	 * @inheritDoc
	 */
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
	
	/**
	 * @inheritDoc
	 */
	private MyProcessInstance convertProcessInstance(ProcessInstance processInstance){
		MyProcessInstance myProcessInstance = new MyProcessInstance();
		myProcessInstance.setDate(new Date());
		myProcessInstance.setBusinesskey(processInstance.getBusinessKey());
		myProcessInstance.setInstanceid(processInstance.getProcessInstanceId());
		myProcessInstance.setProcessdefinitionid(processInstance.getProcessDefinitionId());
		return myProcessInstance;
	}

	/**
	 * @inheritDoc
	 */
	public Map<String, Object> getVariables(String processInstanceId) {
		return this.runtimeService.getVariables(processInstanceId);
	}
	
	/**
	 * @inheritDoc
	 */
	public Object getVariable(String processInstanceId, String variableName) {
		return this.runtimeService.getVariable(processInstanceId,variableName);
	}

}
