package com.facilpass.util;

import java.util.Map;

public interface Operation 
{
	public static final int OPERATION_SUCCESS=1;
	public static final int OPERATION_FAILED=2;
	public static final int OPERATION_PENDING=3;
	public static final int OPERATION_ERROR=4;
	
	
	public int executeOperation(Map<Integer,Object> inputData,Map<Integer,Object> outputData);
	
}
