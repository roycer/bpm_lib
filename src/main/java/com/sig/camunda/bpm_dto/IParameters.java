package com.sig.camunda.bpm_dto;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSession;

public abstract interface IParameters {
	public abstract Map<?, ?> getMap();

	public abstract Object getParameter(String paramString);

	public abstract String getParameterString(String paramString);

	public abstract Integer getParameterInteger(String paramString);

	public abstract HttpServletRequest getRequest();

	public abstract HttpServletResponse getResponse();

	public abstract SqlSession getSession();

	public abstract <T> T getMapper(Class<T> paramClass);

	public abstract String getRootPath();

	public abstract void end();

	public abstract String abort();

	public abstract Integer getIdpersona();

	public abstract Integer getIduserauth();

	public abstract void setIdpersona(Integer paramInteger);

	public abstract String getUsername();

	public abstract void addHeader(String paramString, Object paramObject);

	public abstract boolean isBackground();

	public abstract String getCommand();

	public abstract String getSubcommand();
}