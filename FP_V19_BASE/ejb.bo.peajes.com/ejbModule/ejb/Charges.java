package ejb;

import java.util.List;
import javax.ejb.Remote;

import jpa.TbAgreement;
import jpa.TbChanel;
import jpa.TbCharges;
import jpa.TbPaymentMethod;
import jpa.TbStateRecharge;


@Remote
public interface Charges {
	
public List<TbCharges> getAllCharges();
	
	public boolean setCharge(String nameCharge, int typeCharge, Long valueCharge, String ip, Long user);
	
	public String removeCharge(Long chargeId, String ip, Long user);
	
	public boolean editCharge(Long chargeId,String nameCharge, int typeCharge, Long valueCharge, String ip, Long user);
	
	public TbCharges getChargeById(Long chargeId);

	public boolean existChargeI(String nameCharge);

	public boolean existChargeU(Long ChargeId, String nameCharge);
	
	public String nameById(Long chargeId);

	public List<TbPaymentMethod> getPaymentMethod();

	public List<TbChanel> getChanel();

	public List<TbAgreement> getAgreement();
	
	public List<TbStateRecharge> getStateRecharge();
}
