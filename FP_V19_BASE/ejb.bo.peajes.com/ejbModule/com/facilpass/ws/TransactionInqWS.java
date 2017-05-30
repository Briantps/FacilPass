package com.facilpass.ws;

import com.facilpass.util.Operation;

public interface TransactionInqWS extends Operation
{
 int IN_AUTOMATIC_RECHARGE_ID=1;
 int IN_ENTITY_MANAGER=2;
		
 int RET_AUTORIZATION_STATE=1;
  int STATE_APPROVED=1;
  int STATE_REJECTED=2;
  int STATE_PENDING=3;
		
 int RET_OPERATION_ERROR=2;	 
}
