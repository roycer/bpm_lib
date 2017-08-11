package com.sig.camunda.bpm_lib;

/**
 * @author Roycer
 * @version 1.0
 */
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
import org.camunda.bpm.engine.history.HistoricProcessInstance;
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
	private static final String DESCRIPTION = "descripcion";
	private static final String USER = "user";
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
	 * @see
	 */
	public List<String> getProcessDefinitions(){
		List<String> strProcessDefinitions = new ArrayList<>();
		List<ProcessDefinition> processDefinitions = this.repositoryService.createProcessDefinitionQuery().active().latestVersion().list();
		for(ProcessDefinition i : processDefinitions)
			strProcessDefinitions.add(i.getKey());
		return strProcessDefinitions;
	}
	
	/**
	 * @see
	 */
	public String processCreate(String processDefinitionKey, String businessKey, String description, String person) {
		return processCreate(processDefinitionKey,businessKey,description,person,null);
	}

	/**
	 * @see
	 */
	public String processCreate(String processDefinitionKey, String businessKey, String description, String person, Map<String, Object> variables) {
		
		if (variables == null) 
			variables = new HashMap();	
		if ((!(variables.containsKey(DESCRIPTION))) && (description != null))
			variables.put(DESCRIPTION, description);
		if ((!(variables.containsKey(USER))) && (person != null))
			variables.put(USER, person);
		
		ProcessInstance processInstance = this.runtimeService.startProcessInstanceByKey(processDefinitionKey,businessKey,variables);
		return processInstance.getId();
	}
	
	/**
	 * @see
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
	 * @see
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
	 * @see
	 */
	public void instanceSetVariable(String processInstanceId, String key, Object value){
		Map<String,Object> variables = new HashMap<String, Object>();;
		variables.put(key, value);
		instanceSetVariable(processInstanceId, variables);
	}
	
	/**
	 * @see
	 */
	public void instanceSetVariable(String processInstanceId, Map<String, Object> variables){
		this.runtimeService.setVariables(processInstanceId, variables);
	}
	
	/**
	 * @see
	 */
	public void instanceSetVariableByTaskId(String bpmtaskid, String key, Object value) {
		Task t = this.taskService.createTaskQuery().taskId(bpmtaskid).singleResult();
		this.runtimeService.setVariable(t.getExecutionId(), key, value);
	}

	/**
	 * @see
	 */
	public void instanceSetVariableByTaskId(String bpmtaskid, Map<String, Object> variables) {
		Task t = this.taskService.createTaskQuery().taskId(bpmtaskid).singleResult();
		this.runtimeService.setVariables(t.getExecutionId(), variables);
	}

	/**
	 * @see
	 */
	public List<MyTask> taskListByUser(String person) {
		List<Task> tasks = this.taskService.createTaskQuery().taskAssignee(person).active().list();
		List<MyTask> MyTasks = new ArrayList<MyTask>();
		for(Task i:tasks)
			MyTasks.add(convertTask(i));
		return MyTasks;
	}
	
	/**
	 * @see
	 */
	public List<MyTask> taskListByUserAndInstanceId(String processInstanceId, String person) {
		List<Task> tasks = this.taskService.createTaskQuery().processInstanceId(processInstanceId).taskAssignee(person).active().list();
		List<MyTask> MyTasks = new ArrayList<MyTask>();
		for(Task i:tasks)
			MyTasks.add(convertTask(i));
		return MyTasks;
	}

	/**
	 * @see
	 */
	public List<MyTask> taskListByProcessInstanceId(String processInstanceId) {
		List<Task> tasks = this.taskService.createTaskQuery().processInstanceId(processInstanceId).active().list();
		List<MyTask> MyTasks = new ArrayList<MyTask>();
		for(Task i:tasks)
			MyTasks.add(convertTask(i));
		return MyTasks;
	}

	/**
	 * @see
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
	 * @see
	 */
	public void taskComplete(String bpmtaskid) {
		taskComplete(bpmtaskid,null);
	}

	/**
	 * @see
	 */
	public void taskComplete(String bpmtaskid, String varKey, Object varValue) {
		Map<String,Object> variables = new HashMap<String, Object>();;
		variables.put(varKey, varValue);
		taskComplete(bpmtaskid,variables);
	}

	/**
	 * @see
	 */
	public void taskComplete(String bpmtaskid, Map<String, Object> variables) {
		DelegationState state = this.taskService.createTaskQuery().taskId(bpmtaskid).singleResult().getDelegationState();
		if (DelegationState.PENDING.equals(state))
			this.taskService.resolveTask(bpmtaskid, variables);
		else
			this.taskService.complete(bpmtaskid, variables);
	}

	/**
	 * @see
	 */
	public JSONObject taskGetForm(String bpmtaskid) {
		JSONObject jsonObject = new JSONObject();
		TaskFormData taskFormData = this.formService.getTaskFormData(bpmtaskid);
		jsonObject.put("form", taskFormData.getFormKey());
		jsonObject.put("fields", taskFormData.getFormFields());
		return jsonObject;
	}

	/**
	 * @see
	 */
	public void taskClaim(String bpmtaskid, String person) {
		this.taskService.claim(bpmtaskid, person);
	}

	/**
	 * @see
	 */
	public void taskAssignee(String bpmtaskid, String person) {
		this.taskService.setAssignee(bpmtaskid, person);
	}

	/**
	 * @see
	 */
	public void taskUpdateDescription(String bpmtaskid, String description) {
		this.taskService.setVariable(bpmtaskid, DESCRIPTION, description);
	}

	/**
	 * @see
	 */
	public void taskDelegate(String bpmtaskid, String person) {
		this.taskService.delegateTask(bpmtaskid,person);
	}

	/**
	 * @see
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
	 * @see
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
	 * @see
	 */
	public List<MyEventSubscription> getEvents(String processInstanceId) {
		List<EventSubscription> eventSubscriptions = this.runtimeService.createEventSubscriptionQuery().processInstanceId(processInstanceId).eventType("message").list();
		List<MyEventSubscription> myEventSubscriptions = new ArrayList<>();
		for(EventSubscription i : eventSubscriptions)
			myEventSubscriptions.add(convertEventSubscription(i));
		return myEventSubscriptions;
	}
	
	/**
	 * @see
	 */
	public void fireEvent(MyEventSubscription myEventSubscription){
		this.runtimeService.messageEventReceived(myEventSubscription.getEventName(), myEventSubscription.getExecutionId());
	}
	
	/**
	 * @see
	 */
	public void deleteInstance(String processInstanceId) {
		this.runtimeService.deleteProcessInstance(processInstanceId, "Eliminado por el usuario");
	} 

	/**
	 * @see
	 */
	public void suspendInstance(String processInstanceId) {
		this.runtimeService.suspendProcessInstanceById(processInstanceId);
	}

	/**
	 * @see
	 */
	public void activateInstance(String processInstanceId) {
		this.runtimeService.activateProcessInstanceById(processInstanceId);
	}

	/**
	 * @see
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
	 * @see
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
	 * @see
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
	 * @see
	 */
	private MyProcessInstance convertProcessInstance(ProcessInstance processInstance){
		HistoricProcessInstance historicProcessInstance = this.historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstance.getProcessInstanceId()).singleResult();
		
		MyProcessInstance myProcessInstance = new MyProcessInstance();
		myProcessInstance.setDate(historicProcessInstance.getStartTime());
		myProcessInstance.setBusinesskey(processInstance.getBusinessKey());
		myProcessInstance.setInstanceid(processInstance.getProcessInstanceId());
		myProcessInstance.setProcessdefinitionid(processInstance.getProcessDefinitionId());
		return myProcessInstance;
	}

	/**
	 * @see
	 */
	public Map<String, Object> getVariables(String processInstanceId) {
		
		Map<String,Object> allVariables = this.runtimeService.getVariables(processInstanceId);
		Map<String, Object> myVariables = new HashMap<String,Object>();
		
		
		    for (Map.Entry<String, Object> i : allVariables.entrySet()) {
		    	
		        String key = i.getKey();
		        Object value = i.getValue();
		        if(key.equals(DESCRIPTION) || key.equals(USER)) continue;
		        myVariables.put(key, value);
		    }
		
		return myVariables;
	}
	
	/**
	 * @see
	 */
	public Object getVariable(String processInstanceId, String variableName) {
		return this.runtimeService.getVariable(processInstanceId,variableName);
	}

}
