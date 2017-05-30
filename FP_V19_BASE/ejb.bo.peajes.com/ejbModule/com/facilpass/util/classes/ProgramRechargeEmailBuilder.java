package com.facilpass.util.classes;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import jpa.ReAccountBank;
import jpa.ReAgreementBank;
import jpa.TbAccount;
import jpa.TbAgreement;
import jpa.TbAutomaticRecharge;
import jpa.TbBankTransact;
import jpa.TbState;
import jpa.TbSystemUser;
import jpa.TbWsResponses;

import com.facilpass.util.EmailParameterBuilder;
import com.facilpass.util.Operation;
import com.facilpass.ws.TransactionInqWS;

import constant.EEmailParam;
import constant.EmailProcess;
import constant.TypeProgramming;

/** @author Cristhian Buchely
 *  @deprecated don't comply with interface purpouse
 * */
public class ProgramRechargeEmailBuilder implements EmailParameterBuilder<String> {

	public static final int BL_BUILD_BASIC_1=1;
	public static final int BL_BUILD_TYPE_CLIENT_PROBE=2;
	public static final int BL_BUILD_MANUAL=3;
	public static final int BL_BUILD_TYPE_GENERAL_PROBE=4;
	//public static final int BL_BUILD_TYPE_PREPAID_SCHEDULED=5;
 
//	private static final int EM_EMAIL_PROCESS_ID=1;
 // private static final int EM_DOCUMENT_TYPE=2;
//	private static final int EM_DOCUMENT_NUMBER=3;
//	private static final int EM_CLIENT_NAME=4;
//	private static final int EM_FACILPASS_ACCOUNT=5;
	//private static final int EM_BANK_ENTITY=6;
//	private static final int EM_VALUE=7;
//	private static final int EM_DATE=8;
//	private static final int EM_HOUR=9;
//	private static final int EM_AGREEMENT_NAME=10;
	public static final int EM_ERROR_CODE=11;
//	private static final int EM_PROGRAMMING_TYPE=12;
//	private static final int EM_FREQUENCY_OR_MINIMUM_BALANCE=13;
//	private static final int EM_USER_EMAIL=14;
//	private static final int EM_URL=15;
//	private static final int EM_FACILPASS_ACCOUNT_BALANCE=16;
 
	
	private EmailProcess emailProcess;
	private Map<String,Object> emailData;
	
	// Identificadores de las respuestas del WS
	private Map<Long, TbWsResponses> cods;
	
	/**
	 * Constructor vacío
	 */
	public ProgramRechargeEmailBuilder()
	{
		this.emailData = new HashMap<String,Object>();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public ProgramRechargeEmailBuilder buildParameter(int emailPart, Map params,Object... data)
	{
		TbAutomaticRecharge automaticRecharge;
		int result;
		Integer wsResult;

		switch(emailPart) {
		case BL_BUILD_BASIC_1:
			EntityManager em=(EntityManager)data[0];
			automaticRecharge=(TbAutomaticRecharge)data[1];
			TbAccount account=(TbAccount)data[2];
			TbSystemUser systemUser=(TbSystemUser)data[3];
			buildBasic1(em,automaticRecharge,account,systemUser);
			break;
		case BL_BUILD_TYPE_CLIENT_PROBE:
			result=(Integer)data[0];
			wsResult=(Integer)data[1];
			automaticRecharge=(TbAutomaticRecharge)data[2];
			buildType1(result,wsResult,automaticRecharge);
		 	break;
		case BL_BUILD_TYPE_GENERAL_PROBE:
			result=(Integer)data[0];
			wsResult=(Integer)data[1];
			automaticRecharge=(TbAutomaticRecharge)data[2];
			buildType2(result,wsResult,automaticRecharge);
			break;	
		case BL_BUILD_MANUAL:
			int idParam=(Integer)data[0];
			Object paramValue=data[1];
			
			switch(idParam)
			{
			 case ProgramRechargeEmailBuilder.EM_ERROR_CODE:
			 {
			  Map<String,Object> singleMap=new HashMap<String,Object>();
			  String errorValue=paramValue!=null?paramValue.toString():"";
			  singleMap.put(EEmailParam.ERROR_AVAL.getTag(), errorValue);
			  SimpleTagMessageParser parser=new SimpleTagMessageParser(singleMap);
			  this.emailData.put(EEmailParam.ERROR_AVAL.getTag(), parser); 
			  this.emailData.put(EEmailParam.ERROR_DINAMICO.getTag(), errorValue); 
			 }  
			  break;
			}
		 	break;
		/*case BL_BUILD_TYPE_PREPAID_SCHEDULED:
			result=(Integer)data[0];
			wsResult=(Integer)data[1];
			automaticRecharge=(TbAutomaticRecharge)data[2];
			buildType3(result,wsResult,automaticRecharge);
			break;*/
		}

		return null;
	}

	@Override
	public Map<String,Object> getParameters()
	{
		return this.emailData;
	}
	
	public void setEmailProcess(EmailProcess ep)
	{
		this.emailProcess = ep;
	}
	
	public EmailProcess getEmailProcess()
	{
		return this.emailProcess;
	}

	
	@Override
	public String getTagByEmailParameter(String paramKey)
	{
		return paramKey;
		
//		
//		switch(paramKey)
//		{
//		 case EM_EMAIL_PROCESS_ID: return null; 
//		 case EM_DOCUMENT_TYPE: return "#TPCC";
//		 case EM_DOCUMENT_NUMBER: return "#CC";
//		 case EM_CLIENT_NAME: return "#CLI";
//		 case EM_FACILPASS_ACCOUNT: return "#CFP";
//		 case EM_BANK_ENTITY: return "#PSENB";
//		 case EM_VALUE: return "#VALREC";
//		 case EM_HOUR: return "#HORPROC";
//		 case EM_DATE: return "#FECPROC";
//		 case EM_AGREEMENT_NAME: return "#NOMCO";
//		 case EM_ERROR_CODE: return "#PSEERR";
//		 case EM_PROGRAMMING_TYPE: return "#TIPRG";
//		 case EM_FREQUENCY_OR_MINIMUM_BALANCE: return "#FRESAL";
//		 case EM_USER_EMAIL: return "#CUE";
//		 case EM_URL: return "#ACCURL";
//		 default: throw new IllegalArgumentException();
//		}
	}
	
	private void buildBasic1(EntityManager em,TbAutomaticRecharge automaticRecharge,TbAccount account,TbSystemUser systemUser){
		
		Query query=em.createQuery("SELECT rab FROM ReAccountBank rab WHERE rab.accountId=:account_par");
		query.setParameter("account_par", account);
		ReAccountBank rab=(ReAccountBank)query.getSingleResult();

		TypeProgramming tp = TypeProgramming.getTypePrograming(automaticRecharge.getTypeProgramming().getTypeProgramminId());
		
		Query query1=em.createQuery("SELECT tbt FROM TbBankTransact tbt WHERE tbt.automaticRecharge =:automatic_recharge ");
	   	query1.setParameter("automatic_recharge", automaticRecharge);
	   	TbBankTransact tbt=(TbBankTransact)query1.getSingleResult();
		
		switch (tp)
		{
		case FREQUENCY:
			this.emailData.put(EEmailParam.FRECUENCIA.getTag(),automaticRecharge.getTypeProgramming().getTypeProgramminId());		
			this.emailData.put(EEmailParam.SALDO_BANK_TRANSAC.getTag(), -1);
			break;
		case MINIMUM_BALANCE :	
 	   		
 	   		this.emailData.put(EEmailParam.FRECUENCIA.getTag(), -1);

 	   		this.emailData.put(EEmailParam.SALDO_BANK_TRANSAC.getTag(), tbt.getBankTransactId());
 	   		break;
		}
		
		Date date = new Date();
		
		this.emailData.put(EEmailParam.TIPO_ID.getTag(), systemUser.getTbCodeType().getCodeTypeId());
		this.emailData.put(EEmailParam.NUM_ID.getTag(), systemUser.getUserId());
		this.emailData.put(EEmailParam.NOMB_CLI.getTag(), systemUser.getUserId() );
		this.emailData.put(EEmailParam.CUENTA_FACIL_PASS.getTag(),account.getAccountId());
		this.emailData.put(EEmailParam.NOMB_BANC.getTag(), rab.getBankAccountId().getProduct().getBankId());
		this.emailData.put(EEmailParam.VAL_RECARG.getTag(), automaticRecharge.getAutomaticRechargeValue());

		this.emailData.put(EEmailParam.FECHA_ACCION.getTag(), date);
		this.emailData.put(EEmailParam.HORA_ACCION.getTag(), date);

		Long agrementId=tbt.getAgreementId();
		
		Query query2=em.createQuery("SELECT ta FROM TbAgreement ta WHERE ta.agreementId=:agrement_id");
		query2.setParameter("agrement_id", agrementId);
		List resultList=query2.getResultList();
		 
		if(resultList!=null && resultList.size()>0)
		{
		 TbAgreement ta=(TbAgreement)resultList.get(0);
		 this.emailData.put(EEmailParam.NOMB_CONVENIO.getTag(), ta.getAgreementId());
		}
		else this.emailData.put(EEmailParam.NOMB_CONVENIO.getTag(), null );
		
		 
		//data.put(EM_ERROR_CODE,);
		this.emailData.put(EEmailParam.TIPO_PROGRAMACION.getTag(), automaticRecharge.getTypeProgramming().getTypeProgramminId());
		this.emailData.put(EEmailParam.EMAIL_USU.getTag(), systemUser.getUserId());
		
		this.emailData.put(EEmailParam.URL_ACCION.getTag(),"http://testurl");
	}

	
	/**
	 * Sets the cods or WS errors
	 * @param cods the codes to set
	 */
	public void setCods(Map<Long, TbWsResponses> cods) {
		this.cods = cods;
	}

	private void buildType1(int result,Integer wsResult,TbAutomaticRecharge automaticRecharge)
	{
	  switch(result)
 	  {
 	   case Operation.OPERATION_SUCCESS:
 	   {
 	   	switch(wsResult)
 	   	{
 	   	 case TransactionInqWS.STATE_APPROVED: emailProcess= EmailProcess.REC_FREQ_APROBADA_AUT; break;
 	   	 case TransactionInqWS.STATE_REJECTED: emailProcess= EmailProcess.ERROR_RECARG_FREQ_AUT ; break;
 	   	}
 	   } 
 	   break;
 	   case Operation.OPERATION_FAILED:  
 	   case Operation.OPERATION_ERROR:
 	   	emailProcess=  EmailProcess.ERROR_AUTO_FREQ;
 	   	break;
 	  }
	}
	
	private void buildType2(int result,Integer wsResult,TbAutomaticRecharge automaticRecharge)
	{
	 switch(result)
 	 {
 	  case Operation.OPERATION_SUCCESS:
 	  {
 	   switch(wsResult)
 	   {
 	    case TransactionInqWS.STATE_APPROVED: emailProcess= EmailProcess.REC_FREQ_APROBADA_AUT ; break;
 	    case TransactionInqWS.STATE_REJECTED: emailProcess= EmailProcess.ERROR_RECARG_FREQ_AUT ; break;
 	   }
 	  } 
 	  break;
 	 }
	}
 
	/*private void buildType3(int result,Integer wsResult,TbAutomaticRecharge automaticRecharge)
	{
		switch(automaticRecharge.getTypeProgramming().getTypeProgramminId().intValue())
		{
		 case 1:
		 	 switch(result)
	 	  {
	 	   case Operation.OPERATION_SUCCESS:
	 	   {
	 	   	switch(wsResult)
	 	   	{
	 	   	 case TransactionInqWS.STATE_APPROVED: emailProcess= EmailProcess.FREQUENCY_PREPAID_SCHEDULED_TOPUP_APROVED ; break;
	 	   	 case TransactionInqWS.STATE_REJECTED: emailProcess= EmailProcess.FREQUENCY_PREPAID_SCHEDULED_TOPUP_REJECTED  ; break;
	 	   	}
	 	   } 
	 	   break;
	 	   case Operation.OPERATION_FAILED:  
	 	   case Operation.OPERATION_ERROR:
	 	   	emailProcess=  EmailProcess.FREQUENCY_PREPAID_SCHEDULED_TOPUP_ERROR ; break;
	 	  }
		   break;
		 case 2:
		 	 switch(result)
	 	  {
	 	   case Operation.OPERATION_SUCCESS:
	 	   {
	 	    switch(wsResult)
	 	   	{
	 	   	 case TransactionInqWS.STATE_APPROVED: emailProcess= EmailProcess.MBALACE_PREPAID_SCHEDULED_TOPUP_APROVED; break;
	 	   	 case TransactionInqWS.STATE_REJECTED: emailProcess= EmailProcess.MBALACE_PREPAID_SCHEDULED_TOPUP_REJECTED; break;
	 	   	}
	 	    break;
	 	   }
	 	   case Operation.OPERATION_FAILED:
	 	   case Operation.OPERATION_ERROR:
	 	   	emailProcess= EmailProcess.MBALACE_PREPAID_SCHEDULED_TOPUP_ERROR ;
	 	   	break;
	 	  }
		   break;
		}

	}*/
	
}
