package com.sig.camunda.bpm_lib;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.engine.AuthorizationService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.authorization.Permissions;
import org.camunda.bpm.engine.authorization.Resources;
import org.camunda.bpm.engine.identity.Group;

public class CamundaAuthentication {
	ProcessEngine processEngine;
	IdentityService identityService;
	AuthorizationService authorizationService;
	
	public CamundaAuthentication() {
		this.processEngine = ProcessEngines.getDefaultProcessEngine();
		this.identityService = this.processEngine.getIdentityService();
		this.authorizationService = this.processEngine.getAuthorizationService();
	}
	
	public boolean loginUser(String user, String password){
		return this.identityService.checkPassword(user, password);
	}
	
	public boolean isAuthorizationCreateProcessInstance(String user, String processInstanceId){
	    List<Group> groups = processEngine.getIdentityService().createGroupQuery().groupMember(user).list();
	    List<String> groupIds = new ArrayList<String>();
	    for (Group group : groups) 
	    	groupIds.add(group.getId());
	    
		return this.authorizationService.isUserAuthorized(user, groupIds, Permissions.CREATE, Resources.PROCESS_INSTANCE,"idProcess") ;
	}
	
	public boolean isAuthorizationReadProcessInstance(String user, String processInstanceId){
	    List<Group> groups = processEngine.getIdentityService().createGroupQuery().groupMember(user).list();
	    List<String> groupIds = new ArrayList<String>();
	    for (Group group : groups) 
	    	groupIds.add(group.getId());
	    
		return this.authorizationService.isUserAuthorized(user, groupIds, Permissions.READ, Resources.PROCESS_INSTANCE,"idProcess") ;
	}
	
	
	
	
}
