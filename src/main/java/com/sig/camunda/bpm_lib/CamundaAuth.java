package com.sig.camunda.bpm_lib;

/**
 * @author Roycer
 * @version 1.0
 */
import java.util.List;

/**
 * The Interface CamundaAuth.
 */
public interface CamundaAuth {

	/**
	 * Verifica si usuario esta registrado.
	 *
	 * @param user username del usuario
	 * @param password clave del usuario
	 * @return true, si usuario esta registrado
	 */
	public boolean loginUser(String user, String password);
	
	/**
	 * Crea nuevo usuario.
	 *
	 * @param username username del usuario
	 * @param firstName nombre del usuario
	 * @param lastName apellido del usuario
	 * @param password clave del usuario
	 * @param email correo del usuario
	 */
	public void createUser(String username, String firstName, String lastName, String password, String email);
	
	/**
	 * Crea autorización para  crear instancias de procesos por un usuario.
	 *
	 * @param user username del usuario
	 * @param resource identificador del proceso
	 */
	public void userAuthorizationCreate(String user, String resource);
	
	/**
	 * Crea autorización para leer procesos.
	 *
	 * @param user the user
	 * @param resource the resource
	 */
	public void userAuthorizationRead(String user, String resource);
	
	/**
	 * Verifica autorización de un usuario para ver procesos.
	 *
	 * @param user the user
	 * @param processDefinitionKey the process definition key
	 * @return true, if is authorization create process instance
	 */
	public boolean isAuthorizationReadProcessDefinition(String user, String processDefinition);
	
	/**
	 * Verifica autorización de un usuario para crear instancias de proceso.
	 *
	 * @param user the user
	 * @param processDefinitionKey the process definition key
	 * @return true, if is authorization create process instance
	 */
	public boolean isAuthorizationCreateProcessInstance(String user, String processDefinitionKey);
	
	/**
	 * Verifica autorización de un usuario para ver Instancias de procesos.
	 *
	 * @param user the user
	 * @param processDefinitionKey the process definition key
	 * @return true, if is authorization read process instance
	 */
	public boolean isAuthorizationReadProcessInstance(String user, String processDefinitionKey);
	
	/**
	 * Obtiene una lista de usuarios.
	 *
	 * @return the users
	 */
	public List<String> getUsers();
	
}
