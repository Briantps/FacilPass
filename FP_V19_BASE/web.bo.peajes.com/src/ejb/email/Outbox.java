package ejb.email;

import java.util.ArrayList;

import javax.ejb.Remote;

import com.facilpass.util.MessageParser;

import constant.EmailProcess;

@Remote
public interface Outbox {
	
	public boolean insertMessageOutbox(Long userid,
			EmailProcess processEmail, Long accountId, Long bankAccountId,
			Long transactionId, Long deviceId, Long vehicleId, Long numberVehicles, 
			Long bankId, Long documentId,Long userCreator, Long codeTypeId, String facialId, String actionType,
			boolean sendImmediate,ArrayList<String> parameters);
	
	public boolean insertMessageOutbox2(Long userid,EmailProcess processEmail,boolean sendInmediate,MessageParser parser); 
	
}
