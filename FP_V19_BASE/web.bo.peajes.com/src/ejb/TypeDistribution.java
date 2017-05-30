package ejb;

import java.util.List;
import javax.ejb.Remote;

import jpa.TbAccount;
import jpa.TbDistributionType;

@Remote
public interface TypeDistribution {
  
	/**
	 * @return allTypes
	 */
	public List<TbDistributionType> getDistributionType();
	
	public boolean saveTypeDistribution(Long accountId, Long typeId, String ip, Long creationUser);
	
	public TbAccount getAccount(Long idAccount);
	
	public boolean applyChageInDevice(Long accountId, Long typeId, String ip, Long creationUser);
	
	public String getDescriptionType(Long type);
}
