package com.facilpass.ejb.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jpa.TbAccount;
import jpa.TbAutomaticRecharge;
import jpa.TbBankTransact;
import jpa.TbStateRecharge;
import util.ws.WsResponses;

import com.facilpass.ejb.ProgramaticRechargeEvaluation;
import com.facilpass.util.MessageRegisterBuilder;
import com.facilpass.util.Operation;
import com.facilpass.util.Util;
import com.facilpass.util.classes.ProgramRechargeBuilder;
import com.facilpass.util.classes.ProgramRechargeEmailBuilder;
import com.facilpass.ws.TransactionInqWS;
import com.facilpass.ws.impl.TransactionInqWsImpl;

import constant.ProcessTrackDetailType;
import ejb.Process;
import ejb.email.Outbox;

@Stateless(mappedName = "com/facilpass/ejb/ProgramaticRechargeEvaluation")
public class ProgramaticRechargeEvaluationEJB implements ProgramaticRechargeEvaluation
{
 

	@PersistenceContext(unitName = "bo") 
	EntityManager em;


	@EJB(mappedName="ejb/Process")
	private Process process;

	@EJB(mappedName ="ejb/email/Outbox")
	private Outbox outbox;
 
	@EJB(mappedName ="util/ws/WsResponses")
	private WsResponses wsResponses;
	
	
 /** Returns true if the account has not pending transactions if it has 
	 * 
	 * */
	@SuppressWarnings("rawtypes")
	@Override
	public int processClientProbe(long accountId,String ip,Long creationUser) throws Exception {
		//Injection variables, injection to be implemented 8-04-2017
		Operation wsOperation=new TransactionInqWsImpl();
		TbAccount account=em.find(TbAccount.class, accountId);
		int result = ACCOUNT_EMPTY;
	 
	 
		//check remaining transactions for this account
		Query emQuery=em.createNativeQuery("SELECT tar.automatic_recharge_id FROM tb_automatic_recharge tar WHERE tar.account_id=:account_param AND tar.estado=1 AND tar.proceso=0 FOR UPDATE");
		emQuery.setParameter("account_param", accountId);
		List data=emQuery.getResultList();
	 
		if ( (data != null) && (!data.isEmpty())) {
			result=ACCOUNT_WITH_APPROVED_TRANSACTIONS;
			Map<Integer,Object> processData=new HashMap<Integer,Object>();
			for (int i=0;i<data.size();i++) {
				Long automaticRechargeId = ((Number)data.get(i)).longValue();
				TbAutomaticRecharge automaticRecharge = (TbAutomaticRecharge)em.find(TbAutomaticRecharge.class, automaticRechargeId );
				MessageRegisterBuilder msgCreator = new ProgramRechargeBuilder();
				ProgramRechargeEmailBuilder emailCreator = new ProgramRechargeEmailBuilder();
			 
				int transactionResult = processPendingTransaction(wsOperation,automaticRecharge,processData);

				boolean doRegister=false;
				boolean doEmail=false;
	 		
				emailCreator.buildParameter(ProgramRechargeEmailBuilder.BL_BUILD_BASIC_1, null, em,automaticRecharge,account,account.getTbSystemUser());

//				String errorData;
				switch(transactionResult) {
				case Operation.OPERATION_SUCCESS:
				 int autorizationState=(Integer)processData.get(TransactionInqWS.RET_AUTORIZATION_STATE);
				 emailCreator.buildParameter(ProgramRechargeEmailBuilder.BL_BUILD_TYPE_CLIENT_PROBE , null, transactionResult,autorizationState,automaticRecharge);
				 msgCreator.buildPart(ProgramRechargeBuilder.BL_AUTOMATIC_RECHARGE_BASIC,null, automaticRecharge,em);
	 		  	
				 switch(autorizationState)
				 {
				  case TransactionInqWS.STATE_APPROVED:
					  msgCreator.buildPart(ProgramRechargeBuilder.BL_APPROVATION_STATE ,null, "Autorizó");
					  automaticRecharge.setEstado(0);
					  updateAutorization(automaticRecharge,new Long(1L));
					  em.persist(automaticRecharge);
					  em.flush();
					  doRegister=true;
					  doEmail=true;
					  break;
				  case TransactionInqWS.STATE_REJECTED:
					  msgCreator.buildPart(ProgramRechargeBuilder.BL_APPROVATION_STATE ,null, "Rechazó");
					  String codigoError = (String)processData.get(TransactionInqWS.RET_OPERATION_ERROR);
					  emailCreator.buildParameter(ProgramRechargeEmailBuilder.BL_BUILD_MANUAL, null, ProgramRechargeEmailBuilder.EM_ERROR_CODE, codigoError);
					  automaticRecharge.setEstado(2);
					  updateAutorization(automaticRecharge,new Long(2L));
					  em.persist(automaticRecharge);
					  em.flush();
					  doRegister=true;
					  doEmail=true;
					  if(result==ACCOUNT_WITH_APPROVED_TRANSACTIONS)
						 result=ACCOUNT_WITH_FAILED_TRANSACTIONS;
					  break;
				  case TransactionInqWS.STATE_PENDING:
				 	  result=ACCOUNT_WITH_PENDING_TRANSACTIONS;
				 	  break;	
				 }

				 break;
			 case Operation.OPERATION_FAILED:
				 {
				  String codigoError = (String)processData.get(TransactionInqWS.RET_OPERATION_ERROR);
				  emailCreator.buildParameter(ProgramRechargeEmailBuilder.BL_BUILD_MANUAL, null, ProgramRechargeEmailBuilder.EM_ERROR_CODE, codigoError);
				  emailCreator.buildParameter(ProgramRechargeEmailBuilder.BL_BUILD_TYPE_CLIENT_PROBE , null, transactionResult,null,automaticRecharge);
				  doEmail=true;
				  result=ACCOUNT_WITH_PENDING_TRANSACTIONS;
				 }
	 		 	break;
			 case Operation.OPERATION_ERROR:
			 	 {
				  String codigoError = (String)processData.get(TransactionInqWS.RET_OPERATION_ERROR);
				  emailCreator.buildParameter(ProgramRechargeEmailBuilder.BL_BUILD_MANUAL, null, ProgramRechargeEmailBuilder.EM_ERROR_CODE, codigoError);
				  emailCreator.buildParameter(ProgramRechargeEmailBuilder.BL_BUILD_TYPE_CLIENT_PROBE , null, transactionResult,null,automaticRecharge);
				  doEmail=true;
				  result=ACCOUNT_WITH_PENDING_TRANSACTIONS;
			 	 }
				break;	
	 		}

			 if(doRegister)
			 {
				 switch(automaticRecharge.getTypeProgramming().getTypeProgramminId().intValue()) {
				 case 1:
					 msgCreator.buildPart(ProgramRechargeBuilder.BL_AUTOMATIC_RECHARGE_FREQUENCY ,null, automaticRecharge);
					 break;
				 case 2:
					 msgCreator.buildPart(ProgramRechargeBuilder.BL_AUTOMATIC_RECHARGE_MIN_BALANCE ,null, automaticRecharge,em);
					 break;
				 default: throw new Exception("Error unknown programming type");
				 }

				 msgCreator.buildPart(ProgramRechargeBuilder.NTF_07_152 ,null);
				 String registerMessage=msgCreator.getMessage();
				 Util.doViewProcessAndProcessTrack(process,ProcessTrackDetailType.AUTHOMATHIC_RECHARGE,registerMessage,ip,creationUser);
			 }
	 		
			 if(doEmail)
			 {
			  Util.doTagParserEmail(outbox, creationUser, emailCreator);
			 }
	 		
		 }
		}
	 else
	 {
	  //check if general probe has allocked some transactions for this account.... 
		 Query emQueryGeneral=em.createNativeQuery("SELECT tar.automatic_recharge_id FROM tb_automatic_recharge tar WHERE tar.account_id=:account_param AND tar.estado=1");
		 emQueryGeneral.setParameter("account_param", accountId);
		 List dataGeneral=emQueryGeneral.getResultList();
		 
		 if(dataGeneral!=null && dataGeneral.size()>0)
		 {
		 	result=ProgramaticRechargeEvaluation.ACCOUNT_WITH_PENDING_TRANSACTIONS;
		 }
	 }
	 
		return result;
	}
	
 private void updateAutorization(TbAutomaticRecharge automaticRecharge,Long stateRechargeId)
 {
  if(automaticRecharge!=null)
  {
   Query query=em.createQuery("SELECT tbt FROM TbBankTransact tbt WHERE tbt.automaticRecharge=:auto_rech");
   query.setParameter("auto_rech",automaticRecharge);
   List result=query.getResultList();
   if(result!=null && result.size()>0)
   {
	for(int i=0;i<result.size();i++)
	{
	 TbStateRecharge tsr=(TbStateRecharge)em.find(TbStateRecharge.class, stateRechargeId);
	 TbBankTransact tbt=(TbBankTransact)result.get(i);
	 tbt.setStateCode(tsr.getStateRechargeId());	
	 tbt.setStateDescripcion(tsr.getDescriptionStateRecharge() );
	 em.persist(tbt);
	}
    em.flush();
   }
  }
 }
 

 private int processPendingTransaction(Operation wsOperation,TbAutomaticRecharge automaticRecharge,Map<Integer,Object> outputData)
 {
  Map<Integer,Object> inputData=new HashMap<Integer,Object>();
	 	
  inputData.put(TransactionInqWsImpl.IN_ENTITY_MANAGER, em);
  inputData.put(TransactionInqWsImpl.IN_AUTOMATIC_RECHARGE_ID, automaticRecharge.getAutomaticRechargeId() );
  return wsOperation.executeOperation(inputData, outputData);
 }
	
	
}
