package ejb;

import java.util.ArrayList;
import javax.ejb.Remote;

import util.ReAlarmBalance;


@Remote
public interface LowBalanceAdmin {
	
	public ArrayList<ReAlarmBalance> getAcountUser (long usrs,Long codeType,String numberDoc,String userName,String secondName,String userEmail,String accountId);
	public Long getValueAval ();
	public String getUpdateDate (ArrayList<ReAlarmBalance> TemUpdatepaccountclient,String ip,long usrs);
	public Long getconfirmAcountUser (long usrs,Long codeType,String numberDoc,String userName,String secondName,String userEmail,String accountId);
}
