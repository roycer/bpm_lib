package com.sig.camunda.bpm_lib;

import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.impl.util.json.JSONArray;
import org.camunda.bpm.engine.impl.util.json.JSONObject;

import com.sig.camunda.bpm_dto.TaskDTO;

public class CamundaEngineLib implements CamundaEngine{

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
		// TODO Auto-generated method stub
		return null;
	}

	public String processCreate(String processDefinitionKey, String businessKey, String description, String person) {
		// TODO Auto-generated method stub
		return null;
	}

	public String processUpdate(TaskDTO tarea) {
		// TODO Auto-generated method stub
		return null;
	}

	public String processDelete(String bpminstanceid) {
		// TODO Auto-generated method stub
		return null;
	}

	public JSONObject inboxGetInfoInit() {
		// TODO Auto-generated method stub
		return null;
	}

	public void instanceSetVariableByTaskId(String bpmtaskid, String key, Object value) {
		// TODO Auto-generated method stub
		
	}

	public void instanceSetVariableByTaskId(String bpmtaskid, String key, Map<String, Object> variables) {
		// TODO Auto-generated method stub
		
	}

	public List<TaskDTO> taskListByUser() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<TaskDTO> taskListByProcessInstanceId() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<TaskDTO> historyTaskListByUser() {
		// TODO Auto-generated method stub
		return null;
	}

	public void taskComplete(String bpmtaskid) {
		// TODO Auto-generated method stub
		
	}

	public void taskComplete(String bpmtaskid, String varKey, Object varValue) {
		// TODO Auto-generated method stub
		
	}

	public void taskComplete(String bpmtaskid, Map<String, Object> variables) {
		// TODO Auto-generated method stub
		
	}

	public JSONObject taskGetForm(String bpmtaskid) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		
	}

	public void taskUpdateDescription(String bpmtaskid, String description) {
		// TODO Auto-generated method stub
		
	}

	public void taskDelegate(String bpmtaskid, Integer idpersona) {
		// TODO Auto-generated method stub
		
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

}
