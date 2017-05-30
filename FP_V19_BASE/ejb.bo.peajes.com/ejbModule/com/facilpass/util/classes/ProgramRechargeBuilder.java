package com.facilpass.util.classes;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import jpa.ReAccountBank;
import jpa.TbAccount;
import jpa.TbArMinBalance;
import jpa.TbAutomaticRecharge;

import com.facilpass.util.MessageRegisterBuilder;

/** @author Cristhian Buchely
 * 
 * */
public class ProgramRechargeBuilder implements MessageRegisterBuilder
{
 
	public static final int NTF_07_152=1000;
	public static final int NTF_17_152=1001;
	public static final int NTF_26_152=1002;
	
	
	public static final int BL_APPROVATION_STATE=1;
	public static final int BL_AUTOMATIC_RECHARGE_BASIC=2;
	public static final int BL_AUTOMATIC_RECHARGE_FREQUENCY=3;
	public static final int BL_AUTOMATIC_RECHARGE_MIN_BALANCE=4;
	public static final int BL_TRANSACTION_APROVATION_STATE=5;
	public static final int BL_TRANSACTION_STATE_BASIC=6;
	
	
	
	private static final int PR_AAAA=1;
	private static final int PR_YYYY=2;
	private static final int PR_XXXX=3;
	private static final int PR_EE=4;
	private static final int PR_FF=5;
	private static final int PR_PP=6;
	private static final int PR_TT=7;
	private static final int PR_FI=8;
	private static final int PR_SS=9;
	private static final int PR_ZZZZ=10;
	private static final int PR_EEEE=11;
	
 private int idMessage=-1;
 private Map<Integer,Object> msgData;
 
 public ProgramRechargeBuilder()
 {
 	idMessage=-1;
 	msgData=new HashMap<Integer,Object>();
 }
 
 @Override
	public MessageRegisterBuilder buildPart(int idPart, Map<Integer, Object> params,Object... data)
 {
 	switch(idPart)
		{
 	 case ProgramRechargeBuilder.NTF_26_152:
 	 	  idMessage=idPart;
 	 	  break;
		 case ProgramRechargeBuilder.NTF_07_152:
		 	  msgData.put(ProgramRechargeBuilder.PR_TT,"Iniciando en la siguiente fecha");
		 case ProgramRechargeBuilder.NTF_17_152:
		 	  idMessage=idPart;
		    break;
		 case BL_APPROVATION_STATE:
		 	  msgData.put(PR_AAAA, data[0].toString());
		 	  break;
		 case BL_TRANSACTION_APROVATION_STATE:
 	    msgData.put(PR_EEEE, data[0].toString());
 	    break;
		 case BL_AUTOMATIC_RECHARGE_BASIC:
		    {
		 	   TbAutomaticRecharge automaticRecharge=(TbAutomaticRecharge)data[0];
		 	   EntityManager em=(EntityManager)data[1];
		 	   
		 	   TbAccount account=automaticRecharge.getAccountId();
				   Query query=em.createQuery("SELECT rab FROM ReAccountBank rab WHERE rab.accountId=:account_par");
				   query.setParameter("account_par", account);
				   ReAccountBank rab=(ReAccountBank)query.getSingleResult();
		 	   
				   msgData.put(ProgramRechargeBuilder.PR_EE, rab.getBankAccountId().getProduct().getBankName());
				   msgData.put(ProgramRechargeBuilder.PR_XXXX,  automaticRecharge.getAutomaticRechargeValue());
				   msgData.put(ProgramRechargeBuilder.PR_FF, automaticRecharge.getTypeProgramming().getTypeProgrammingDesc() );
				   msgData.put(ProgramRechargeBuilder.PR_YYYY, account.getAccountId().toString());
		    }
		 	  break;
		 case BL_AUTOMATIC_RECHARGE_FREQUENCY:
		    {
		    	SimpleDateFormat df=new SimpleDateFormat("yyyy\\MM\\dd HH:mm:ss");
		    	TbAutomaticRecharge automaticRecharge=(TbAutomaticRecharge)data[0];
		    	msgData.put(ProgramRechargeBuilder.PR_PP, automaticRecharge.getFrequencyId().getFrequencyDescript());
		    	msgData.put(ProgramRechargeBuilder.PR_FI, df.format(automaticRecharge.getCreationDate()));
		    }
		 	  break;
		 case BL_AUTOMATIC_RECHARGE_MIN_BALANCE:
		 			{
		 				SimpleDateFormat df=new SimpleDateFormat("yyyy\\MM\\dd HH:mm:ss");
		 				TbAutomaticRecharge automaticRecharge=(TbAutomaticRecharge)data[0];
		 				EntityManager em=(EntityManager)data[1];
		 				
		 				Query query=em.createQuery("SELECT tarmb FROM TbArMinBalance tarmb WHERE tarmb.tbAutomaticRecharge=:aut_param");
				 	 query.setParameter("aut_param", automaticRecharge);
				 	 TbArMinBalance balanceObject=(TbArMinBalance)query.getSingleResult();
				 	 msgData.put(ProgramRechargeBuilder.PR_PP, "");
				 	 msgData.put(ProgramRechargeBuilder.PR_FI, df.format(automaticRecharge.getCreationDate() ));
				 	 msgData.put(ProgramRechargeBuilder.PR_SS, balanceObject.getMinBalance()); 
		 			}
		 case BL_TRANSACTION_STATE_BASIC:
		 		 {
		 		 	TbAutomaticRecharge automaticRecharge=(TbAutomaticRecharge)data[0];
		 		 	EntityManager em=(EntityManager)data[1];
		 		 	
		 		 	msgData.put(PR_XXXX, automaticRecharge.getAutomaticRechargeValue());
		 		 	msgData.put(PR_YYYY, automaticRecharge.getAccountId().getAccountId());
		 		 	
		 		 	TbAccount account=automaticRecharge.getAccountId();
				   Query query=em.createQuery("SELECT rab FROM ReAccountBank rab WHERE rab.accountId=:account_par");
				   query.setParameter("account_par", account);
				   ReAccountBank rab=(ReAccountBank)query.getSingleResult();
		 	   
				   msgData.put(ProgramRechargeBuilder.PR_ZZZZ, rab.getBankAccountId().getProduct().getBankName());
		 		 }
		 	  break;	  		 	
		}
 	
 	return this;
	}
 
	
	@Override
	public String getMessage()
	{
		if(idMessage==-1) throw new IllegalStateException("Cannot get message from empty stuff");
  StringBuilder builder=new StringBuilder();
		
		switch(idMessage)
		{
		 case NTF_07_152:
		  {
		  	String AAAA=msgData.get(PR_AAAA).toString();
		  	String YYYY=msgData.get(PR_YYYY).toString();
		  	String XXXX=ic(msgData.get(PR_XXXX)).toString();
		  	String EE=msgData.get(PR_EE).toString();
		  	String FF=msgData.get(PR_FF).toString();
		  	String PP=msgData.get(PR_PP).toString();
		  	String TT=msgData.get(PR_TT).toString();
		  	String FI=msgData.get(PR_FI).toString();
		  	
		 	 builder.append("La entidad ").append(EE).append(" ").append(AAAA).append(" la recarga de recursos programados a su Cuenta FacilPass No");
		 	 builder.append(YYYY).append(" por Valor de $").append(XXXX).append(" con ").append(FF).append(" ").append(PP).append(" ").append(TT).append(" ").append(FI);
		  }
		 	break;
		 case NTF_17_152:
	   {
	  	 String AAAA=msgData.get(PR_AAAA).toString();
	  	 String YYYY=msgData.get(PR_YYYY).toString();
	  	 String XXXX=ic(msgData.get(PR_XXXX)).toString();
	  	 String EE=msgData.get(PR_EE).toString();
	  	 String FF=msgData.get(PR_FF).toString();
	  	 String SS=ic(msgData.get(PR_SS)).toString();
	  	
	 	  builder.append("La entidad ").append(EE).append(" ").append(AAAA).append(" la recarga de recursos programados a su Cuenta FacilPass No ").append(YYYY);
	 	  builder.append(" por valor de $").append(XXXX).append(" con ").append(FF).append(", será efectiva cada vez que el saldo de la cuenta FacilPass llegue a $").append(SS);
	   }
	 	 break;
		 case NTF_26_152:
		  {
     String XXXX=ic(msgData.get(PR_XXXX)).toString();
     String YYYY=msgData.get(PR_YYYY).toString();
     String EEEE=msgData.get(PR_EEEE).toString();
     String ZZZZ=msgData.get(PR_ZZZZ).toString();
		  	
		  	builder.append("La recarga programada por frecuencia y valor de $").append(XXXX).append(" para la Cuenta FacilPass No. ").append(YYYY).append(" fue ").append(EEEE);
		  	builder.append(" por la entidad ").append(ZZZZ).append(". Para ver el comprobante de la transacción de clic aquí");
		  }
		 	break;
		 default:
		 	throw new IllegalArgumentException();
		}
		
		return builder.toString();
	}
	
	private Object ic(Object input)
	{
		Object result=input;
	 if(input instanceof Number)
	 {
	 	DecimalFormat df=new DecimalFormat();
	 	df.setMaximumFractionDigits(0);
	 	result=df.format(input);
	 }
		return result;
	}

}
