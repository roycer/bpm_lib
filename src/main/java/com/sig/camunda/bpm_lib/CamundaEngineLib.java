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
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.impl.util.json.JSONArray;
import org.camunda.bpm.engine.impl.util.json.JSONObject;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.DelegationState;
import org.camunda.bpm.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sig.camunda.bpm_dto.InternalException;
import com.sig.camunda.bpm_dto.TaskDTO;

public class CamundaEngineLib implements CamundaEngine{

	private static final Logger LOG;
	private static final String USER_CREATOR = "usercreator";
	private static final String CASE_PERSON = "case.person";
	private static final String CASE_DESCRIPTION = "case.description";
	private static final String USER_AUTHORIZATION = "user.authorization";
	
	private ProcessEngine processEngine;
	private RuntimeService runtimeService;
	private TaskService taskService;
	private FormService formService;
	private RepositoryService repositoryService;
	private HistoryService historyService;
	
	static {
		LOG = LoggerFactory.getLogger(CamundaEngineLib.class);
	}
	
	public CamundaEngineLib(){
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
	
	public CamundaEngineLib(ProcessEngine processEngine){
		this.processEngine = processEngine;
	}
	
	public CamundaEngineLib(String processEngineName){
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

	public List<TaskDTO> taskListByUser(String person) {
		List<Task> tasks = this.taskService.createTaskQuery().taskAssignee(person).list();
		List<TaskDTO> taskDTOs = new ArrayList<TaskDTO>();
		for(Task i:tasks)
			taskDTOs.add(convertTask(i));
		return taskDTOs;
	}
	
	public List<TaskDTO> taskListByUserAndInstanceId(String processInstanceId, String person) {
		List<Task> tasks = this.taskService.createTaskQuery().processInstanceId(processInstanceId).taskAssignee(person).list();
		List<TaskDTO> taskDTOs = new ArrayList<TaskDTO>();
		for(Task i:tasks)
			taskDTOs.add(convertTask(i));
		return taskDTOs;
	}

	public List<TaskDTO> taskListByProcessInstanceId(String processInstanceId) {
		List<Task> tasks = this.taskService.createTaskQuery().processInstanceId(processInstanceId).list();
		List<TaskDTO> taskDTOs = new ArrayList<TaskDTO>();
		for(Task i:tasks)
			taskDTOs.add(convertTask(i));
		return taskDTOs;
	}

	public List<TaskDTO> historyTaskListByUser(String processInstanceId, String person) {
		List<HistoricTaskInstance> historicTaskInstances =	this.historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).taskAssignee(person).list();
		List<TaskDTO> taskDTOs = new ArrayList<TaskDTO>();
		for(HistoricTaskInstance i:historicTaskInstances){
			taskDTOs.add(convetHistoricTask(i));
		}
		return taskDTOs;
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

	public void fireEvent(String eventName, String businessKey) {
		// TODO Auto-generated method stub
		
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

	private TaskDTO convertTask(Task task){		
		TaskDTO taskdto = new TaskDTO();
		taskdto.setId(task.getId());
		taskdto.setName(task.getName());
		taskdto.setDescription(task.getDescription());
		taskdto.setPriority(task.getPriority());
		taskdto.setDuedate(task.getDueDate());
		taskdto.setCreatetime(task.getCreateTime());
		taskdto.setProcessname(task.getProcessDefinitionId());
		taskdto.setTaskuniquename(task.getTaskDefinitionKey());
		taskdto.setAssignee(task.getAssignee());
		return taskdto;
	}
	
	private TaskDTO convetHistoricTask(HistoricTaskInstance historicTaskInstance){
		TaskDTO taskdto = new TaskDTO();
		taskdto.setId(historicTaskInstance.getId());
		taskdto.setName(historicTaskInstance.getName());
		taskdto.setDescription(historicTaskInstance.getDescription());
		taskdto.setPriority(historicTaskInstance.getPriority());
		taskdto.setDuedate(historicTaskInstance.getDueDate());
		taskdto.setProcessname(historicTaskInstance.getProcessDefinitionId());
		taskdto.setTaskuniquename(historicTaskInstance.getTaskDefinitionKey());
		taskdto.setAssignee(historicTaskInstance.getAssignee());
		taskdto.setStarttime(historicTaskInstance.getStartTime());
		taskdto.setEndtime(historicTaskInstance.getEndTime());
		return taskdto;
	}
	

}
