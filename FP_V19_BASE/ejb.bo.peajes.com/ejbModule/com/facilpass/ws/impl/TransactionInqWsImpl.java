package com.facilpass.ws.impl;

import java.net.URL;
import java.util.Date;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.corficolombiana.facilpass.ws.connector.transactioninq.delegate.WSDelegate;
import com.corficolombiana.facilpass.ws.connector.transactioninq.dto.RequestDTO;
import com.corficolombiana.facilpass.ws.connector.transactioninq.dto.ResponseDTO;
import com.facilpass.util.Operation;
import com.facilpass.util.Util;
import com.facilpass.ws.TransactionInqWS;

public class TransactionInqWsImpl implements TransactionInqWS
{
 private static final long wsid=2L;
	
	private static class OperationQueryParameters
 {
		Long rqUID;
		String idType;
		String idNum;
		String acctId;
 }
	
	@Override
	public int executeOperation(Map<Integer, Object> inputData,Map<Integer, Object> outputData)
	{
		int result=0;
		Long autoRechId=(Long)inputData.get(TransactionInqWS.IN_AUTOMATIC_RECHARGE_ID);
		EntityManager em=(EntityManager)inputData.get(TransactionInqWS.IN_ENTITY_MANAGER);
		
				
		try
		{
		 Long rqUID;
		 String channel="1"; // constant
		 Date clientDt=new Date(); // this moment
		 String ipAddr=Util.getMostNearIp();
		 String idType;
		 String idNum;
		 String bankId="6"; //constant
		 String acctId;
		
		 OperationQueryParameters queryParams=queryParameters(em,autoRechId);
		 
		 rqUID=queryParams.rqUID;
		 idType=queryParams.idType;
		 idNum=queryParams.idNum;
		 acctId=queryParams.acctId;
		 
		 String serviceUrl=Util.getWsUrl(em, wsid);
		 RequestDTO request=new RequestDTO(rqUID,channel,clientDt,ipAddr,idType,idNum,bankId,acctId);
		 
		 WSDelegate delegate=new WSDelegate(new URL(serviceUrl));
		 ResponseDTO response=delegate.invokeWS(request);
		 
		 //ResponseDTO response=dummyCall(request);
		 
		 switch((int)response.getStatus().getStatusCode())
			{
			 case 0: // Operación exitosa
			 	{
			 		int approvalState;
			 		
			 		String approvalData=response.getApprovalId();
			 		
			 		if(approvalData.trim().startsWith("16"))
			 		{
			 		 approvalState=TransactionInqWS.STATE_APPROVED;
			 		}
			 		else if(approvalData.trim().startsWith("17"))
			 		{
			 		 approvalState=TransactionInqWS.STATE_PENDING;
			 		}
			 		else
			 		{
			 		 String errorDescription=response.getStatus().getStatusDesc()!=null?response.getStatus().getStatusDesc():"";
			 		 outputData.put(TransactionInqWS.RET_OPERATION_ERROR , errorDescription);
			 		 approvalState=TransactionInqWS.STATE_REJECTED;
			 		}
			 		
			 		outputData.put(TransactionInqWS.RET_AUTORIZATION_STATE , approvalState);
			 		result=Operation.OPERATION_SUCCESS;
			 	}
			 	break;	
			 case 100: // Error general
			   {
			   	String errorDescription=(response.getStatus().getStatusDesc()!=null?response.getStatus().getStatusDesc():Util.getWsMessage(em, wsid, response.getStatus().getStatusCode(),"Error general") );
			   	outputData.put(TransactionInqWS.RET_OPERATION_ERROR , errorDescription);
		  	  result=Operation.OPERATION_FAILED;
		    }
		 	  break;
			 case 200: // Error de datos
			   {
			   	String errorDescription=(response.getStatus().getStatusDesc()!=null?response.getStatus().getStatusDesc():Util.getWsMessage(em, wsid, response.getStatus().getStatusCode(),"Error de datos"));
			   	outputData.put(TransactionInqWS.RET_OPERATION_ERROR ,errorDescription);
		  	  result=Operation.OPERATION_FAILED;
		    }
		 	  break;
			 case 300: // Sistema no disponible
			   {
			   	String errorDescription=(response.getStatus().getStatusDesc()!=null?response.getStatus().getStatusDesc():Util.getWsMessage(em, wsid, response.getStatus().getStatusCode(),"Sistema no disponible"));
			   	outputData.put(TransactionInqWS.RET_OPERATION_ERROR ,errorDescription);
			   	result=Operation.OPERATION_FAILED;
			   }
			 	 break;
			 case 600: // Error por trama inválida	
			   {
			   	String errorDescription=(response.getStatus().getStatusDesc()!=null?response.getStatus().getStatusDesc():Util.getWsMessage(em, wsid, response.getStatus().getStatusCode(),"Error por trama inválida"));
			   	outputData.put(TransactionInqWS.RET_OPERATION_ERROR ,errorDescription);
			  	 result=Operation.OPERATION_FAILED;
			   }
			 	break;
			 case 700: // Error de procesamiento
			   {
			   	String errorDescription=(response.getStatus().getStatusDesc()!=null?response.getStatus().getStatusDesc():Util.getWsMessage(em, wsid, response.getStatus().getStatusCode(),"Error de procesamiento"));
			   	outputData.put(TransactionInqWS.RET_OPERATION_ERROR ,errorDescription);
			   	result=Operation.OPERATION_FAILED;
			   }
			 	break;
			 default:
			   {
			   	String errorDescription=(response.getStatus().getStatusDesc()!=null?response.getStatus().getStatusDesc():Util.getWsMessage(em, wsid, response.getStatus().getStatusCode(),"Error desconocido"));
			   	outputData.put(TransactionInqWS.RET_OPERATION_ERROR ,errorDescription);
	  	   result=Operation.OPERATION_FAILED;
	     }
			 	 break;
			}
		}
		catch(Exception e)
		{
			System.err.println("Error in TransactionInqOperation : ");
			e.printStackTrace();
			outputData.put(TransactionInqWS.RET_OPERATION_ERROR ,"Error desconocido");
   result=Operation.OPERATION_FAILED;
		}
		
		System.out.println("Ws result : " + result + " " + outputData );
		
		return result;
	}
	
	private OperationQueryParameters queryParameters(EntityManager em,Long autoRech)
	{
		OperationQueryParameters queryParams=new OperationQueryParameters();
		
		StringBuilder builder=new StringBuilder();
		
		builder.append("SELECT tbt.tb_bank_transact_id,tct.code_type_abbreviation,tsu.user_code,ta.account_id ");
		builder.append("FROM tb_automatic_recharge tar ");
		builder.append("INNER JOIN tb_account ta ON ta.account_id=tar.account_id ");
		builder.append("INNER JOIN tb_system_user tsu ON tsu.user_id=ta.user_id ");
		builder.append("INNER JOIN tb_code_type tct ON tct.code_type_id=tsu.code_type_id ");
		builder.append("INNER JOIN tb_bank_transact tbt ON tbt.automatic_recharge_id=tar.automatic_recharge_id ");
		builder.append("WHERE tar.automatic_recharge_id=:auto_rech");
		
		Query q1=em.createNativeQuery(builder.toString());
		q1.setParameter("auto_rech", autoRech);
		Object[] row=(Object[])q1.getSingleResult();
		
		queryParams.rqUID=((Number)row[0]).longValue();
		queryParams.idType=formatIdType(((String)row[1]));
		queryParams.idNum=((String)row[2]);
		queryParams.acctId=(row[3]).toString();
		
		return queryParams;
	}
	
	private String formatIdType(String type)
	{
		if(type.equals("CC")) return "C";
		else if(type.equals("CE")) return "E";
		else if(type.equals("NIT"))return "N";
		else if(type.equals("TI")) return "T";
		else if(type.equals("PP")) return "P";
		else return type;
	}
	

}
