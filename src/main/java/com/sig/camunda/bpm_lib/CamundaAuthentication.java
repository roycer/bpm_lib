package com.sig.camunda.bpm_lib;

/**
 * @author Roycer
 * @version 1.0
 */
import java.util.ArrayList;
import java.util.List;
import org.camunda.bpm.engine.AuthorizationService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.authorization.Authorization;
import org.camunda.bpm.engine.authorization.Permissions;
import org.camunda.bpm.engine.authorization.Resources;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;

public class CamundaAuthentication implements CamundaAuth{
	ProcessEngine processEngine;
	IdentityService identityService;
	AuthorizationService authorizationService;
	private static int AUTH_TYPE_GLOBAL = 0;	//Global Authorization 
	private static int AUTH_TYPE_GRANT = 1;		//Grant Authorization
	private static int AUTH_TYPE_REVOKE = 2;	//Revoke Authorization 
	
	/**
	 * Inicializa servicios de autorizacion de camunda
	 */
	public CamundaAuthentication() {
		this.processEngine = ProcessEngines.getDefaultProcessEngine();
		this.identityService = this.processEngine.getIdentityService();
		this.authorizationService = this.processEngine.getAuthorizationService();
	}
	
	/**
	 * @see
	 */
	public boolean loginUser(String user, String password){
		return this.identityService.checkPassword(user, password);
	}
	
	/**
	 * @inheritDoc
	 */
	public void createUser(String username, String firstName, String lastName, String password, String email){
		User user = this.identityService.newUser(username);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		user.setPassword(password);
		this.identityService.saveUser(user);
	}
	
	/**
	 * @inheritDoc
	 */
	public void userAuthorizationCreate(String user, String resource){
		Authorization auth = this.authorizationService.createNewAuthorization(AUTH_TYPE_GRANT);
		auth.setUserId(user);
		auth.setResource(Resources.PROCESS_INSTANCE);
		auth.setResourceId(resource);
		auth.addPermission(Permissions.READ);
		auth.addPermission(Permissions.CREATE);
		this.authorizationService.saveAuthorization(auth);
	}
	
	/**
	 * @inheritDoc
	 */
	public void userAuthorizationRead(String user, String resource){
		Authorization auth = this.authorizationService.createNewAuthorization(AUTH_TYPE_GRANT);
		auth.setUserId(user);
		auth.setResource(Resources.PROCESS_INSTANCE);
		auth.setResourceId(resource);
		auth.addPermission(Permissions.READ);
		this.authorizationService.saveAuthorization(auth);
	}
	
	/**
	 * @inheritDoc
	 */
	public boolean isAuthorizationCreateProcessInstance(String user, String processDefinitionKey){
	    List<Group> groups = processEngine.getIdentityService().createGroupQuery().groupMember(user).list();
	    List<String> groupIds = new ArrayList<String>();
	    for (Group group : groups) 
	    	groupIds.add(group.getId());
	    
		return this.authorizationService.isUserAuthorized(user, groupIds, Permissions.CREATE, Resources.PROCESS_INSTANCE,processDefinitionKey) ;
	}
	
	/**
	 * @inheritDoc
	 */
	public boolean isAuthorizationReadProcessInstance(String user, String processDefinitionKey){
	    List<Group> groups = processEngine.getIdentityService().createGroupQuery().groupMember(user).list();
	    List<String> groupIds = new ArrayList<String>();
	    for (Group group : groups) 
	    	groupIds.add(group.getId());
	    
		return this.authorizationService.isUserAuthorized(user, groupIds, Permissions.READ, Resources.PROCESS_INSTANCE,processDefinitionKey) ;
	}
	
	/**
	 * @inheritDoc
	 */
	public List<String> getUsers(){
	    List<String> strUsers = new ArrayList<String>();
		List<User> users = this.identityService.createUserQuery().list();
		for(User i : users)
			strUsers.add(i.getId());
		
		return strUsers;
	}
	
	
	
	
}
