package linews;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import com.grupoaval.telepeajes.payments.v1.RechargeAddRsType;
import com.grupoaval.telepeajes.payments.v1.RechargeImplService;
import com.grupoaval.telepeajes.payments.v1.RechargeSoap;
import com.grupoaval.telepeajes.payments.v1.RechargeAddRqType;
import com.grupoaval.telepeajes.xsd.ifx.BankInfoType;
import com.grupoaval.telepeajes.xsd.ifx.BillPmtInfoType;
import com.grupoaval.telepeajes.xsd.ifx.CurrencyAmountType;
import com.grupoaval.telepeajes.xsd.ifx.CustIdType;
import com.grupoaval.telepeajes.xsd.ifx.DepAcctIdFromType;
import com.grupoaval.telepeajes.xsd.ifx.GovIssueIdentType;
import com.grupoaval.telepeajes.xsd.ifx.MsgRqHdrType;


public class RechargeClient implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public RechargeClient(){
		super();
	}
	
public Long invokeRecharge(Object[] obj){
	try{
		
		String bosId = null;
		String bankId = null;
		String messageType = null;
		Long itemId = null;
		Long documentType = null;
		Long clientId = null;
		Timestamp startDate = null;
		String bankNumber = null;
		String bankType = null;
		Long amount = null;
		boolean revert = false;
		
		Long statusCode = null;
	    @SuppressWarnings("unused")
		String statusDescription = null;
		
		
		
		bosId = (obj[0]!=null?obj[0].toString():"");
		bankId = (obj[1]!=null?obj[1].toString():"");
		messageType = (obj[2]!=null?obj[2].toString():"");
		itemId = (obj[3]!=null?new Long(obj[3].toString()):0);
		documentType = (obj[4]!=null?new Long(obj[4].toString()):0);
		clientId = (obj[5]!=null?new Long(obj[5].toString()):0);
		startDate = (obj[6]!=null?Timestamp.valueOf(obj[6].toString()):null);
		bankNumber = (obj[7]!=null?obj[7].toString():"");
		bankType = (obj[8]!=null?obj[8].toString():"");
		amount = (obj[9]!=null?new Long(obj[9].toString()):0);
		
		System.out.println("bosId: "+bosId);
		System.out.println("bankId: "+bankId);
		System.out.println("messageType: "+messageType);
		System.out.println("itemId: "+itemId);
		System.out.println("documentType: "+documentType);
		System.out.println("clientId: "+clientId);
		System.out.println("startDate: "+startDate);
		System.out.println("bankNumber: "+bankNumber);
		System.out.println("bankType: "+bankType);
		System.out.println("amount: "+amount);
		System.out.println("revert: "+revert);
		
			
		RechargeImplService ris = new RechargeImplService();
		RechargeSoap rs = ris.getRechargeImplPort();
		RechargeAddRqType rechargeAddRq = new RechargeAddRqType();
		
		MsgRqHdrType msg= new MsgRqHdrType();
		BankInfoType bank=new BankInfoType();	    
	    GovIssueIdentType gov=new GovIssueIdentType();
	    CurrencyAmountType cur =new CurrencyAmountType();
	    DepAcctIdFromType dep=new DepAcctIdFromType();
	    BillPmtInfoType bill = new BillPmtInfoType();
	    CustIdType cus =new CustIdType();
	    
		System.out.println("Enviando Solicitud...");
		
		//set Parameters from WebServices
		
		msg.setChannel(bosId);
		bank.setBankId(bankId);
		msg.setSessKey(messageType);
		rechargeAddRq.setRqUID(itemId.toString());
		gov.setGovIssueIdentType(documentType.toString());
		gov.setIdentSerialNum(clientId.toString());
		msg.setClientDt(startDate);
		dep.setAcctId(bankNumber);
		dep.setAcctType(bankType);
		cur.setAmt(new BigDecimal(amount));
		msg.setReverse(revert);
		
		rechargeAddRq.setBillPmtInfo(bill);
		rechargeAddRq.getBillPmtInfo().setCurAmt(cur);
		rechargeAddRq.setCustId(cus);
		rechargeAddRq.setMsgRqHdr(msg);
		rechargeAddRq.setRqUID(itemId.toString());
		rechargeAddRq.getBillPmtInfo().setDepAcctIdFrom(dep);
		
	    RechargeAddRsType objeto = rs.addRecharge(rechargeAddRq); //la invocacion como tal es: rs.addRecharge(rechargeAddRq); 
		
		
		
		if(objeto!=null){
			System.out.println("valor final recibido en facil (Recarga):"+objeto.getRqUID());
			statusCode= objeto.getStatus().getStatusCode();
	    	statusDescription =objeto.getStatus().getStatusDesc();    	
	    	
			return statusCode;
		}
		else{
			return statusCode;
		}
	}catch(Exception ex){
		return null;
	}
	
	
	}

}
