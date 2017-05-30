package com.facilpass.util;

import java.util.Map;

public interface MessageRegisterBuilder
{
 
	public MessageRegisterBuilder buildPart(int idPart,Map<Integer,Object> params,Object... data);
	
	public String getMessage();
	
}
