package util.ws;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.ejb.Stateless;
import javax.persistence.*;

import util.ObjectRechage;
import constant.LogAction;
import constant.LogReference;

import jpa.ReAccountBank;
import jpa.TbAccount;
import jpa.TbLog;
import jpa.TbResponseType;
import jpa.TbUmbral;
import linews.InvokeSqdm;


import crud.ObjectEM;

@Stateless(mappedName = "util/ws/WSRecharge")
public class WSRechargeEJB implements WSRecharge {
	
	@PersistenceContext(unitName = "bo") 
	EntityManager em;

	
	
	//private Query userQuery;
	private ObjectEM objectEM;
	
	
	@Override
	public ObjectRechage createListObj(Long idToken) {
     try{
		System.out.println("umbral: "+idToken);
		
		ObjectRechage o = null;
		
		TbUmbral tu = em.find(TbUmbral.class, idToken);
		if(tu == null){
			System.out.println("No se encontro umbral "+idToken);
		}else{
			Query q = em.createNativeQuery("Select * from re_account_bank where account_id=?1 and state_account_bank =1", ReAccountBank.class);
			q.setParameter(1, tu.getTbAccount().getAccountId());
			System.out.println(tu.getTbAccount().getAccountId());			
			
			ReAccountBank rab = (ReAccountBank) q.getSingleResult();
			
			if (rab != null){
				o = new ObjectRechage();
				o.setBosId("BOS");
				o.setBankId(rab.getBankAccountId().getProduct().getBankId().toString());
				o.setMessageType(String.valueOf(tu.getTypeMessageId()));
				o.setItemId(tu.getUmbralId().toString());
				o.setDocumentType(tu.getTbAccount().getTbSystemUser().getTbCodeType().getCodeTypeId().toString());
				o.setClientId(tu.getTbAccount().getTbSystemUser().getUserCode());
				
				SimpleDateFormat formatoDeFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Timestamp fecha = tu.getUmbralTime();
				
				o.setStartDate(formatoDeFecha.format(fecha));
				o.setBankNumber(rab.getBankAccountId().getBankAccountNumber().toString());
				o.setBankType(rab.getBankAccountId().getBankAccountType().getProductTypeId().toString());
				o.setAmount(tu.getUmbralValue());
				o.setRevert(false);
			}
		}
		
    	
		
		return o;
     }catch(NoResultException ex){
    	 ex.printStackTrace();
    	 return null;
     }
	}
	

	public void setObjectEM(ObjectEM objectEM) {
		this.objectEM = objectEM;
	}


	public ObjectEM getObjectEM() {
		return objectEM;
	}

	@Override
	public Long createUmbral(Long account, BigDecimal average, BigDecimal value, Timestamp dateTransaction,Long state, Long message, String ip,
			Long user) {
		try {	
		
		
		
		Long result = null;
		TbAccount a = em.find(TbAccount.class, account);
		if(a != null){
			Query query= em.createNativeQuery("select ACCOUNT_BANK_ID from VW_LAST_PRODUCT_BANK where ACCOUNT_ID=?1");
			query.setParameter(1, account);
			
		  TbUmbral tu = new TbUmbral();
		  tu.setAverage(average);
		  tu.setTbAccount(a);
		  tu.setTypeMessageId(message);
		  tu.setUmbralState(state);
		  tu.setUmbralTime(dateTransaction);
		  tu.setUmbralValue(value);
		  tu.setUmbralChannel(0L);
		  tu.setAccountBankId(((BigDecimal)query.getSingleResult()).longValue());
		  tu.setDescriptionPse(null);
		  
		  em.persist(tu);
		  em.flush();
		  
		  result = tu.getUmbralId();
		  
		  System.out.println("IdUmbral Creado en createUmbral con envio al banco: "+result);
		  
		}
				
		Thread.sleep(500);
		
		
		if(result != null ){
			
			InvokeSqdm rechargeWS= new InvokeSqdm();
			
			ObjectRechage obj = this.createListObj(result);
			if(obj != null){
			String data = rechargeWS.InvokeRecharge(obj);
			 String array[] = data.split(";");
			   Long rs=Long.valueOf(array[0]);
			System.out.println("respuesta: " +  rs);
			
			if(rs != null){
				TbLog log = new TbLog();
				TbResponseType resp = em.find(TbResponseType.class, rs);
				if(resp != null){
					log.setLogMessage(resp.getResponseDescrip());
					log.setLogReference(LogReference.RECHARGESTATION.toString());
					log.setLogAction(LogAction.RECHARGE_MESSAGE.toString());
					log.setLogDate(new Timestamp(System.currentTimeMillis()));
					log.setLogIp(ip);
					log.setUserId(user);
					
					em.persist(log);
					em.flush();
				} else {
					log.setLogMessage("Codigo de Repuesta: "+rs+" invalido");
					log.setLogReference(LogReference.RECHARGESTATION.toString());
					log.setLogAction(LogAction.RECHARGE_MESSAGE.toString());
					log.setLogDate(new Timestamp(System.currentTimeMillis()));
					log.setLogIp(ip);
					log.setUserId(user);
					
					em.persist(log);
					em.flush();
				}
				
			}
			
			return rs;
		} else{
			 return null;
		}
		} else {
		  return null;
		}
	
	
	} catch (Exception e){
		e.printStackTrace();
		return null;
	}
 
	}
	
	

	@Override
	public Long createUmbral(Long account, BigDecimal average, BigDecimal value, Timestamp dateTransaction,Long state, Long message) {
	   try {
		   Long result = null;
		   System.out.println("-------------> createUmbral: " + account);
		   TbAccount a = em.find(TbAccount.class, account);
		   if(a != null){
			   Query query= em.createNativeQuery("select ACCOUNT_BANK_ID from VW_LAST_PRODUCT_BANK where ACCOUNT_ID=?1");
				query.setParameter(1, account);			   

			   TbUmbral tu = new TbUmbral();
			   tu.setAverage(average);
			   tu.setTbAccount(a);
			   tu.setTypeMessageId(message);
			   tu.setUmbralState(state);
			   tu.setUmbralTime(dateTransaction);
			   tu.setUmbralValue(value);
			   tu.setUmbralChannel(0L);
			   tu.setAccountBankId(((BigDecimal)query.getSingleResult()).longValue());
			   tu.setDescriptionPse(null);
			   
			   em.persist(tu);
			   em.flush();
			   
			   result = tu.getUmbralId();
			   
			   System.out.println("IdUmbral Creado: "+result);
			   
		   }	
		return result != null ? result : null;
	
	} catch (Exception e){
		e.printStackTrace();
		return null;
	}
 
	}


	@Override
	public String getBankNameByAccount(Long accountId) {
		try{
			String result = "";
			Query q = em.createNativeQuery("select b.bank_name "+
						"from re_account_bank rab "+
						"inner join tb_bank_account ba on rab.bank_account_id=ba.bank_account_id "+
						"inner join tb_bank b on ba.product=b.bank_id "+
						"where rab.account_id=?1 "+
						"and rab.state_account_bank =1 "); 
			q.setParameter(1, accountId);
			
			result = (String)q.getSingleResult();
			
			if(result == null){
				result = "Banco no reconocido";
			}			
			return result;
		}catch(Exception e){
			e.printStackTrace();
			return "Banco no reconocido";
		}
	}


	@Override
	public Long getTransactionByUmbral(Long umbralId) {
		try{
			Query q = em.createNativeQuery("Select transaction_id from tb_umbral where umbral_id=?1");
			q.setParameter(1, umbralId);
			
			BigDecimal result = ((BigDecimal) q.getSingleResult());
			if(result != null){
				return result.longValue();
			}else{
				return 0L;
			}
		}catch(NoResultException e){
			e.printStackTrace();
			return 0L;
		}
	}
	
	
}
