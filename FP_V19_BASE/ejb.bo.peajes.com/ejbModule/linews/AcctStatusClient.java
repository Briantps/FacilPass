package linews;

import com.grupoaval.telepeajes.accounts.v1.AcctStatusImplService;
import com.grupoaval.telepeajes.accounts.v1.AcctStatusModSoap;
import com.grupoaval.telepeajes.accounts.v1.AcctStatusModRqType;


public class AcctStatusClient {
	
public void invokeAcctStatus(){
	AcctStatusImplService asis = new AcctStatusImplService();
	AcctStatusModSoap asms = asis.getAcctStatusImplPort();
	AcctStatusModRqType acctStatusModRq = new AcctStatusModRqType();	
	asms.modAccountStatus(acctStatusModRq);
		
	}

}
