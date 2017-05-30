package linews;

import com.grupoaval.telepeajes.payments.v1.RechargeNotificationImplService;
import com.grupoaval.telepeajes.payments.v1.RechargeNotificationSoap;

import com.grupoaval.telepeajes.payments.v1.RechargeNotificationAddRqType;

public class RechargeNotificationClient {
	
	public void invokeRechargeNotification(){
		RechargeNotificationImplService ris = new RechargeNotificationImplService();
		RechargeNotificationSoap rs = ris.getRechargeNotificationImplPort();
		RechargeNotificationAddRqType rechargeAddRq = new RechargeNotificationAddRqType();	
		rs.notifyRecharge(rechargeAddRq);
	}

}
