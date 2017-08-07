package com.sig.camunda.bpm_lib;


import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.impl.util.json.JSONArray;
import org.camunda.bpm.engine.impl.util.json.JSONObject;

import com.sig.camunda.bpm_dto.MyEventSubscription;
import com.sig.camunda.bpm_dto.MyProcessInstance;
import com.sig.camunda.bpm_dto.MyTask;



public interface Camunda  {
	
	public String processCreate (String processDefinitionKey,String businessKey
								,String description,String person,Map<String, Object> variables);
//	Crea una instancia de proceso.
	
	public String processCreate (String processDefinitionKey,String businessKey,String description,String person);
//	Crea una instancia de proceso.
	
//	public String processUpdate (MyTask tarea);
//	Actualiza la información de la instancia de proceso.
	
	public List<MyProcessInstance> getProcessInstances(String proccessDefinitionKey);
	// lee todos los procesos instanciados
	
	public List<MyProcessInstance> getProcessInstances();
	// lee todos los procesos instanciados
	
	public void processDelete (String bpminstanceid);
//	Elimina la instancia de proceso.
	
//	public JSONObject inboxGetInfoInit();
//	Lee la configuración inicial del motor de procesos para la bandeja de entrada.
	
	public void instanceSetVariableByTaskId(String bpmtaskid ,String key,Object value);
//	Adiciona o modifica la variable de la instancia de proceso relacionada a la tarea.
	
	public void instanceSetVariableByTaskId(String bpmtaskid, Map<String, Object> variables);
//	Adiciona o modifica las variables de la instancia de proceso relacionada a la tarea.
	
	public List<MyTask> taskListByUser(String person);
//	Lee las tareas asignadas del usuario que inicio sesión en el sistema.
	
	public List<MyTask> taskListByUserAndInstanceId(String processInstanceId, String person);
	
	public List<MyTask> taskListByProcessInstanceId(String processInstanceId);
//	Lee las tareas activas de la instancia de proceso.
	
	public List<MyTask> historyTaskListByUser(String processInstanceId, String person);
//	Lista las tareas históricas del usuario que inicio sesión en el sistema.
	
	public void taskComplete(String bpmtaskid);
//	Completa una tarea.
	
	public void taskComplete(String bpmtaskid,String varKey,Object varValue);
//	Completa una tarea y a la vez establece la variable a la instancia de proceso.
	
	public void taskComplete(String bpmtaskid,Map<String, Object> variables);
//	Completa una tarea y a la vez establece las variables a la instancia de proceso.
	
	public JSONObject taskGetForm(String bpmtaskid);
//	Lee la definición del formulario relacionado a la tarea.
	
//	public JSONArray taskCandidateListByUser();
//	Lee las tareas que el usuario puede reclamar.
	
//	public List<MyTask> getCandidates(String taskid);
//	Lee los usuarios candidatos de una tarea activa.
	
	public void taskClaim(String bpmtaskid, String person);
//	Asigna la tarea al usuario que lo reclama.
	
	public void taskAssignee(String bpmtaskid,String person);
//	Asigna la tarea a un usuario específico.
	
	public void taskUpdateDescription(String bpmtaskid,String description);
//	Actualiza la descripción de la instancia de proceso relacionada a la tarea.
	
	public void taskDelegate(String bpmtaskid,String person);
//	Delega una tarea a un usuario específico.
	
	public boolean updateDescription(String processDefinitionKey,String businessKey,String description);
//	Actualiza la descripción de la instancia de proceso relacionada a la definición de proceso y la clave de negocio.
	
	public boolean updateDescriptionAndPerson(String processDefinitionKey,String businessKey,String description,String person);
//	Actualiza la descripción y la persona/área de la instancia de proceso relacionada a la definición de proceso y la clave de negocio.
	
	public List<MyEventSubscription> getEvents(String processInstanceId);
//	Lee todos los eventos de una instancia de proceso
	
	public void fireEvent(MyEventSubscription myEventSubscription);
//	Dispara un evento al motor de procesos.
	
	public boolean deleteInstance(String processDefinitionKey,String businessKey);
//	Elimina la instancia de proceso relacionado a la clave de negocio.
	
	public boolean suspendInstance(String processDefinitionKey,String businessKey);
//	Suspende la instancia de proceso
	public boolean activateInstance(String processDefinitionKey,String businessKey);
//	Activa la instancia de proceso previamente suspendida.
	
}
