package linews;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import accountAdminPkg.AccountAdminServices;
import rechargePkg.RechargeServices;
import util.ObjectRechage;

public class InvokeSqdm {

	public String InvokeRecharge(ObjectRechage obj){
		if(obj !=null){
			String bosId = null;
			String bankId = null;
			String messageType = null;
			String itemId = null;
			String documentType = null;
			String clientId = null;
			String startDate = null;
			
			String bankNumber = null;
			String bankType = null;
			BigDecimal amount = null;
			boolean revert = false;
			
			
			RechargeServices recharge=new RechargeServices();
			
			try {
				Context context = new InitialContext();
				DataSource dataSource = (DataSource) context.lookup("jdbc/bo");
				Connection connection = dataSource.getConnection();
				System.out.println(connection.getMetaData().getURL());
				recharge.setConnection(connection);
			} catch (NamingException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			//Long recarga=recharge.createRecharge("bosId", "bankId","messageType", "itemId", "documentType","clientId", "startDt", "bankNumber","accType",new BigDecimal(12000), false);
			
			bosId = obj.getBosId();
			bankId = obj.getBankId();
			messageType = obj.getMessageType();
			itemId = obj.getItemId();
			documentType = obj.getDocumentType();
			clientId = obj.getClientId();
			startDate = obj.getStartDate();
			bankNumber = obj.getBankNumber();
			bankType = obj.getBankType();
			amount = obj.getAmount();
			
			
			
			
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
			
			
			String recarga=recharge.createRecharge(
					bosId ,
					bankId,
					messageType ,
					itemId ,
					documentType ,
					clientId ,
					startDate ,
					bankNumber ,
					bankType ,
					amount ,
					revert
					);
			
			
			System.out.println("recharge: "+recarga);
			
			return recarga;
		}
		else{
			return "300L;null;null";	
		}
		
	}
	
	
	public String InvokeAccountAdmin(Object ob[]){
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		String bosId = null, bankId = null, messageType = null, clientId = null, accountType = null, bankNumber = null,itemId = null, documentType = null;
	    String startDate = null;
	    
	    bosId= (ob[0]!=null?ob[0].toString():"");
	    bankId= (ob[1]!=null?ob[1].toString():"");
	    messageType= (ob[2]!=null?ob[2].toString():"");
	    itemId= (ob[3]!=null?ob[3].toString():"");
	    documentType= (ob[4]!=null?ob[4].toString():"");
	    clientId= (ob[5]!=null?ob[5].toString():"");
	    System.out.println("fecha " + ob[6].toString());
	    	
	    String fec1= ob[6]!=null?ob[6].toString():null;
	    if(fec1!=null){
	    	Timestamp t=Timestamp.valueOf(ob[6]!=null?ob[6].toString():null);
	    	startDate= sdf.format(t);
		    System.out.println("t " + t); 
	    }
	    else{
	    	startDate= null;
	    }
	    System.out.println("startDate " + startDate);
	    	
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
	    	
		AccountAdminServices accountAdmin = new AccountAdminServices();
		
		try {
			Context context = new InitialContext();
			DataSource dataSource = (DataSource) context.lookup("jdbc/bo");
			Connection connection = dataSource.getConnection();
			accountAdmin.setConnection(connection);
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String account=accountAdmin.createAccountAdmin(bosId, bankId,messageType, itemId, documentType,clientId, startDate,accountType, bankNumber);
		System.out.println("accountAdmin: "+account);		
		return account;
	}

}
