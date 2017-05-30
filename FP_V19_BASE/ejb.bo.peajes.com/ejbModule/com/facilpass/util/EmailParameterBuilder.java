package com.facilpass.util;

import java.util.Map;

import constant.EmailProcess;

public interface EmailParameterBuilder <T> 
{
 
	EmailParameterBuilder<T> buildParameter(int emailPart,Map<String,Object> params,Object... data);
	
	public void setEmailProcess(EmailProcess ep);
	
	public Map<T,Object> getParameters();
	
	public String getTagByEmailParameter(T paramKey);
	
	public EmailProcess getEmailProcess();
}
