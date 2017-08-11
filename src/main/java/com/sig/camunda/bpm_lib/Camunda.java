package com.sig.camunda.bpm_lib;

/**
 * @author Roycer
 * @version 1.0
 */
import java.util.List;
import java.util.Map;
import org.camunda.bpm.engine.impl.util.json.JSONObject;
import com.sig.camunda.bpm_dto.MyEventSubscription;
import com.sig.camunda.bpm_dto.MyProcessInstance;
import com.sig.camunda.bpm_dto.MyTask;

/**
 * The Interface Camunda.
 */
public interface Camunda  {
	
	/**
	 * Crea una instancia de proceso.
	 *
	 * @param processDefinitionKey Id del proceso BPMN
	 * @param businessKey Clave del proceso BPMN
	 * @param description Descripción del proceso
	 * @param person Username del del usuario
	 * @param variables Variables de inicialización del proceso
	 * @return Id de la instancia del proceso
	 */
	public String processCreate (String processDefinitionKey,String businessKey, String description,String person,Map<String, Object> variables);
	
	/**
	 * Crea una instancia de proceso.
	 *
	 * @param processDefinitionKey the process definition key
	 * @param businessKey the business key
	 * @param description the description
	 * @param person the person
	 * @return id de la instancia del proceso
	 */
	public String processCreate (String processDefinitionKey,String businessKey,String description,String person);
	
	/**
	 * Lee todos los procesos instanciados con respecto a una definición de proceso.
	 *
	 * @return the process instances
	 */
	public List<String> getProcessDefinitions();
	
	/**
	 * Lee todos los procesos instanciados.
	 *
	 * @param proccessDefinitionKey the proccess definition key
	 * @return the process instances
	 */
	public List<MyProcessInstance> getProcessInstances(String proccessDefinitionKey);
	
	/**
	 * Lee todos los procesos instanciados.
	 *
	 * @return the process instances
	 */
	public List<MyProcessInstance> getProcessInstances();
		
	/**
	 * Adiciona o modifica la variable de la instancia de proceso relacionada a la tarea.
	 *
	 * @param processInstanceId the process instance id
	 * @param variables the variables
	 */
	public void instanceSetVariable(String processInstanceId, Map<String, Object> variables);
	
	/**
	 * Adiciona o modifica la variable de la instancia de proceso relacionada a la tarea.
	 *
	 * @param bpmtaskid the bpmtaskid
	 * @param key the key
	 * @param value the value
	 */
	public void instanceSetVariableByTaskId(String bpmtaskid ,String key,Object value);
	
	/**
	 * Adiciona o modifica las variables de la instancia de proceso relacionada a la tarea.
	 *
	 * @param bpmtaskid the bpmtaskid
	 * @param variables the variables
	 */
	public void instanceSetVariableByTaskId(String bpmtaskid, Map<String, Object> variables);
	
	/**
	 * Lee las tareas asignadas del usuario que inicio sesión en el sistema.
	 *
	 * @param person the person
	 * @return the list
	 */
	public List<MyTask> taskListByUser(String person);
	
	/**
	 * Lee las tareas asignadas del usuario que inicio sesión en el sistema en una instancia especifica.
	 *
	 * @param processInstanceId the process instance id
	 * @param person the person
	 * @return the list
	 */
	public List<MyTask> taskListByUserAndInstanceId(String processInstanceId, String person);
	
	/**
	 * Lee las tareas activas de la instancia de proceso.
	 *
	 * @param processInstanceId the process instance id
	 * @return the list
	 */
	public List<MyTask> taskListByProcessInstanceId(String processInstanceId);
	
	/**
	 * Lista las tareas históricas del usuario que inicio sesión en el sistema.
	 *
	 * @param processInstanceId the process instance id
	 * @param person the person
	 * @return the list
	 */
	public List<MyTask> historyTaskListByUser(String processInstanceId, String person);
	
	/**
	 * Completa una tarea.
	 *
	 * @param bpmtaskid the bpmtaskid
	 */
	public void taskComplete(String bpmtaskid);
	
	/**
	 * Completa una tarea y a la vez establece la variable a la instancia de proceso.
	 *
	 * @param bpmtaskid the bpmtaskid
	 * @param varKey the var key
	 * @param varValue the var value
	 */
	public void taskComplete(String bpmtaskid,String varKey,Object varValue);
	
	/**
	 * Completa una tarea y a la vez establece las variables a la instancia de proceso.
	 *
	 * @param bpmtaskid the bpmtaskid
	 * @param variables the variables
	 */
	public void taskComplete(String bpmtaskid,Map<String, Object> variables);
	
	/**
	 * Lee la definición del formulario relacionado a la tarea.
	 *
	 * @param bpmtaskid the bpmtaskid
	 * @return the JSON object
	 */
	public JSONObject taskGetForm(String bpmtaskid);
	
	/**
	 * Asigna la tarea al usuario que lo reclama.
	 *
	 * @param bpmtaskid the bpmtaskid
	 * @param person the person
	 */
	public void taskClaim(String bpmtaskid, String person);
	
	/**
	 * Asigna la tarea a un usuario específico.
	 *
	 * @param bpmtaskid the bpmtaskid
	 * @param person the person
	 */
	public void taskAssignee(String bpmtaskid,String person);
	
	/**
	 * Actualiza la descripción de la instancia de proceso relacionada a la tarea.
	 *
	 * @param bpmtaskid the bpmtaskid
	 * @param description the description
	 */
	public void taskUpdateDescription(String bpmtaskid,String description);
	
	/**
	 * Delega una tarea a un usuario específico.
	 *
	 * @param bpmtaskid the bpmtaskid
	 * @param person the person
	 */
	public void taskDelegate(String bpmtaskid,String person);
	
	/**
	 * Actualiza la descripción de la instancia de proceso relacionada a la definición de proceso y la clave de negocio.
	 *
	 * @param processDefinitionKey the process definition key
	 * @param businessKey the business key
	 * @param description the description
	 * @return true, if successful
	 */
	public boolean updateDescription(String processDefinitionKey,String businessKey,String description);
	
	/**
	 * Actualiza la descripción y la persona/área de la instancia de proceso relacionada a la definición de proceso y la clave de negocio.
	 *
	 * @param processDefinitionKey the process definition key
	 * @param businessKey the business key
	 * @param description the description
	 * @param person the person
	 * @return true, if successful
	 */
	public boolean updateDescriptionAndPerson(String processDefinitionKey,String businessKey,String description,String person);
	
	/**
	 * Lee todos los eventos de una instancia de proceso.
	 *
	 * @param processInstanceId the process instance id
	 * @return the events
	 */
	public List<MyEventSubscription> getEvents(String processInstanceId);
	
	/**
	 * Dispara un evento al motor de procesos.
	 *
	 * @param myEventSubscription the my event subscription
	 */
	public void fireEvent(MyEventSubscription myEventSubscription);
	
	/**
	 * Elimina una instancia de proceso.
	 *
	 * @param processInstanceId una instancia de proceso
	 */
	public void deleteInstance(String processInstanceId);
	
	/**
	 * Suspende la instancia de proceso.
	 *
	 * @param processInstanceId instancia de proceso
	 */
	public void suspendInstance(String processInstanceId);
	
	/**
	 * Activa la instancia de proceso previamente suspendida.
	 *
	 * @param processInstanceId the process instance id
	 */
	public void activateInstance(String processInstanceId);
	
	/**
	 * Gets the variables.
	 *
	 * @param processInstanceId the process instance id
	 * @return the variables
	 */
	public Map<String,Object> getVariables(String processInstanceId);
	
	
	/**
	 * Gets the variable.
	 *
	 * @param executionId the execution id
	 * @param variableName the variable name
	 * @return the variable
	 */
	public Object getVariable(String executionId, String variableName);
	
}
