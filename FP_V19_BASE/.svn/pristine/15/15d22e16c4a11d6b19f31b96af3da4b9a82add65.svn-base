package linews;

import com.grupoaval.telepeajes.payments.v1.RechargeAddRsType;
import com.grupoaval.telepeajes.payments.v1.RechargeImplService;
import com.grupoaval.telepeajes.payments.v1.RechargeSoap;
import com.grupoaval.telepeajes.payments.v1.RechargeAddRqType;


public class RechargeClient {
	
public void invokeRecharge(){
	RechargeImplService ris = new RechargeImplService();
	RechargeSoap rs = ris.getRechargeImplPort();
	RechargeAddRqType rechargeAddRq = new RechargeAddRqType();
	System.out.println("Enviando Solicitud...");
	rechargeAddRq.setRqUID("dato1");
    RechargeAddRsType objeto = rs.addRecharge(rechargeAddRq); //la invocacion como tal es: rs.addRecharge(rechargeAddRq); 
	
	System.out.println("valor final recibido en facil:"+objeto.getRqUID());
	
	}

}
