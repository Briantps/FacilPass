package linews;

import java.io.Serializable;
import java.sql.Timestamp;


import com.grupoaval.telepeajes.accounts.v1.AccountAdminImplService;
import com.grupoaval.telepeajes.accounts.v1.AccountAdminSoap;
import com.grupoaval.telepeajes.accounts.v1.AcctAddRqType;
import com.grupoaval.telepeajes.accounts.v1.AcctAddRsType;
import com.grupoaval.telepeajes.xsd.ifx.BankInfoType;
import com.grupoaval.telepeajes.xsd.ifx.CustIdType;
import com.grupoaval.telepeajes.xsd.ifx.DepAcctIdType;
import com.grupoaval.telepeajes.xsd.ifx.GovIssueIdentType;
import com.grupoaval.telepeajes.xsd.ifx.MsgRqHdrType;


public class AccountAdminClient implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public AccountAdminClient(){
		super();
	}
	
	public String invokeAccountAdmin(Object[] ob){
	    String bosId = null, bankId = null, messageType = null, clientId = null, accountType = null, bankNumber = null,itemId = null, documentType = null;
	    Timestamp startDate = null;
	    Long statusCode = null;
	    String statusDescription = null;
	    
	    	bosId= (ob[0]!=null?ob[0].toString():"");
	    	bankId= (ob[1]!=null?ob[1].toString():"");
	    	messageType= (ob[2]!=null?ob[2].toString():"");
	    	itemId= (ob[3]!=null?ob[3].toString():"");
	    	documentType= (ob[4]!=null?ob[4].toString():"");
	    	clientId= (ob[5]!=null?ob[5].toString():"");
	    	System.out.println("fecha " + ob[6].toString());
	    	startDate= (ob[6]!=null?Timestamp.valueOf(ob[6].toString()):null);
	    	accountType=(ob[7]!=null?ob[7].toString():"");
	    	bankNumber=(ob[8]!=null?ob[8].toString():"");
	    	
	    	System.out.println("bosId" + bosId);
	    	System.out.println("bankId" + bankId);
	    	System.out.println("messageType" + messageType);
	    	System.out.println("itemId" + itemId);
	    	System.out.println("documentType" + documentType);
	    	System.out.println("clientId" + clientId);
	    	System.out.println("startDate" + startDate);
	    	System.out.println("accountType" + accountType);
	    	System.out.println("bankNumber" + bankNumber);
	    
	    AccountAdminImplService adis = new AccountAdminImplService();
	    AccountAdminSoap aas = adis.getAccountAdminImplPort();
	    AcctAddRqType acctAddRq = new AcctAddRqType();
	   
	    BankInfoType bank=new BankInfoType();
	    MsgRqHdrType msg= new MsgRqHdrType();
	    GovIssueIdentType gov=new GovIssueIdentType();
	    CustIdType cus =new CustIdType();
	    DepAcctIdType dep=new DepAcctIdType();
	    
	    //setiando parametros al webservices
	    acctAddRq.setRqUID(itemId);
	    
		bank.setBankId(bankId);
		
		msg.setChannel(bosId);
	    msg.setClientDt(startDate);
		msg.setSessKey(messageType);
		msg.setBankInfo(bank);
		
		gov.setGovIssueIdentType(documentType);
		gov.setIdentSerialNum(clientId);
		
		cus.setGovIssueIdent(gov);
		
		dep.setAcctType(accountType);
		dep.setAcctId(bankNumber);

		acctAddRq.setMsgRqHdr(msg);
		acctAddRq.setCustId(cus);
		acctAddRq.setDepAcctId(dep);
		
		
	    AcctAddRsType res=aas.addAccount(acctAddRq);
	    
	    if(res!=null){
	    	statusCode= res.getStatus().getStatusCode();
	    	statusDescription =res.getStatus().getStatusDesc();
	    	
	    	if(statusCode == 0L){
	    		System.out.println("Listo para hacer proceso de recarga.");
	    	}
	    }
	    System.out.println("Respuesta de solicitud asociación cuenta codigo: " + statusCode + " Descripcion: " + statusDescription); 
	    
	    return "ok";
	}



}
