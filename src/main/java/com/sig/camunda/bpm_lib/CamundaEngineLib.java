package com.sig.camunda.bpm_lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.impl.util.json.JSONArray;
import org.camunda.bpm.engine.impl.util.json.JSONObject;
import org.camunda.bpm.engine.task.Task;

import com.sig.camunda.bpm_dto.TaskDTO;

public class CamundaEngineLib implements CamundaEngine{

	private ProcessEngine processEngine;
	private String person;
	private String description;
	
	public CamundaEngineLib(){
		this.processEngine = ProcessEngines.getDefaultProcessEngine();
	}
	
	public CamundaEngineLib(ProcessEngine processEngine){
		this.processEngine = processEngine;
	}
	
	public CamundaEngineLib(String processEngineName){
		this.processEngine = ProcessEngines.getProcessEngine(processEngineName);
	}
	
	public String processCreate(TaskDTO tarea, Map<String, Object> variables) {
		// TODO Auto-generated method stub
		return null;
	}

	public String processCreate(TaskDTO tarea) {
		// TODO Auto-generated method stub
		return null;
	}

	public String processCreate(String processDefinitionKey, String businessKey, String description, String person,
		Map<String, Object> variables) {
		this.description = description;
		this.person = person;
		String InstanciaId = processEngine.getRuntimeService().startProcessInstanceByKey(processDefinitionKey,businessKey,variables).getId();
		return InstanciaId;
	}

	public String processCreate(String processDefinitionKey, String businessKey, String description, String person) {
		this.description = description;
		this.person = person;
		String InstanciaId = processEngine.getRuntimeService().startProcessInstanceByKey(processDefinitionKey,businessKey).getId();
		return InstanciaId;
	}

	public String processUpdate(TaskDTO tarea) {
		// TODO Auto-generated method stub
		return null;
	}

	public String processDelete(String bpminstanceid) {
		// TODO Auto-generated method stub
		processEngine.getRuntimeService().deleteProcessInstance(bpminstanceid, "");
		return null;
	}

	public JSONObject inboxGetInfoInit() {
		// TODO Auto-generated method stub
		return null;
	}

	public void instanceSetVariableByTaskId(String bpmtaskid, String key, Object value) {
		processEngine.getTaskService().setVariableLocal(bpmtaskid, key, value);
	}

	public void instanceSetVariableByTaskId(String bpmtaskid, String key, Map<String, Object> variables) {
		processEngine.getTaskService().setVariableLocal(bpmtaskid, key, variables);
	}

	public List<TaskDTO> taskListByUser() {
		List<org.camunda.bpm.engine.task.Task> tasks = processEngine.getTaskService().createTaskQuery().taskAssignee(person).list();
		List<TaskDTO> taskDTOs = new ArrayList<TaskDTO>();
		for(org.camunda.bpm.engine.task.Task i:tasks){
			taskDTOs.add(convertTask(i));
		}
		return taskDTOs;
	}

	public List<TaskDTO> taskListByProcessInstanceId(String processInstanceId) {
		List<Task> tasks = processEngine.getTaskService().createTaskQuery().processDefinitionId(processInstanceId).list();
		List<TaskDTO> taskDTOs = new ArrayList<TaskDTO>();
		for(Task i:tasks){
			taskDTOs.add(convertTask(i));
		}
		return taskDTOs;
	}

	public List<TaskDTO> historyTaskListByUser() {
		List<HistoricTaskInstance> historicTaskInstances =	processEngine.getHistoryService().createHistoricTaskInstanceQuery().taskAssignee(person).list();
		List<TaskDTO> taskDTOs = new ArrayList<TaskDTO>();
		for(HistoricTaskInstance i:historicTaskInstances){
			taskDTOs.add(convetHistoricTask(i));
		}
		return taskDTOs;
	}

	public void taskComplete(String bpmtaskid) {
		processEngine.getTaskService().complete(bpmtaskid);
	}

	public void taskComplete(String bpmtaskid, String varKey, Object varValue) {
		Map<String,Object> variables = new HashMap<String, Object>();;
		variables.put(varKey, varValue);
		processEngine.getTaskService().complete(bpmtaskid);
	}

	public void taskComplete(String bpmtaskid, Map<String, Object> variables) {
		processEngine.getTaskService().complete(bpmtaskid, variables);
	}

	public JSONObject taskGetForm(String bpmtaskid) {
		return null;
	}

	public JSONArray taskCandidateListByUser() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<TaskDTO> getCandidates(String taskid) {
		// TODO Auto-generated method stub
		return null;
	}

	public void taskClaim(String bpmtaskid) {
		// TODO Auto-generated method stub
		
	}

	public void taskAssignee(String bpmtaskid, Integer idpersona) {
		processEngine.getTaskService().setAssignee(bpmtaskid, idpersona.toString());
	}

	public void taskUpdateDescription(String bpmtaskid, String description) {
		// TODO Auto-generated method stub
	}

	public void taskDelegate(String bpmtaskid, Integer idpersona) {
		processEngine.getTaskService().delegateTask(bpmtaskid, idpersona.toString());
	}

	public void updateDescription(String processDefinitionKey, String businessKey, String description) {
		// TODO Auto-generated method stub
		
	}

	public void updateDescriptionAndPerson(String processDefinitionKey, String businessKey, String description,
			String person) {
		// TODO Auto-generated method stub
		
	}

	public void fireEvent(String eventName, String businessKey) {
		// TODO Auto-generated method stub
		
	}

	public void deleteInstance(String processDefinitionKey, String businessKey) {
		// TODO Auto-generated method stub
		
	} 

	public void suspendInstance(String processDefinitionKey, String businessKey) {
		// TODO Auto-generated method stub
		
	}

	public void activateInstance(String processDefinitionKey, String businessKey) {
		// TODO Auto-generated method stub
		
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
