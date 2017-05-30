package com.facilpass.ejb;

import javax.ejb.Remote;

@Remote
public interface ProgramaticRechargeEvaluation 
{
	int ACCOUNT_EMPTY=1;
	int ACCOUNT_WITH_ERROR=2;
	int ACCOUNT_WITH_PENDING_TRANSACTIONS=3;
	int ACCOUNT_WITH_FAILED_TRANSACTIONS=4;
	int ACCOUNT_WITH_APPROVED_TRANSACTIONS=5;
	
 public int processClientProbe(long accountId,String ip,Long creationUser) throws Exception;
 
}
