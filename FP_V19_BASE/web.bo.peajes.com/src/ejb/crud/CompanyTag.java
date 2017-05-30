package ejb.crud;
import java.util.List;

import javax.ejb.Remote;

import jpa.TbCompanyTag;
import jpa.TbCourier;
import jpa.TbRollo;

@Remote
public interface CompanyTag {
	
	public List<TbCompanyTag> getCompanyTag();
	
	public String insertCompanyTag(String companyTagName, String ip, Long creationUser);
	
	public String insertCourier(Long courierId, String courierName, Long companyTagId, String ip, Long userId);
	
	public String insertRoll(String rollName, Long courierId, String ip, Long userId);

	public String existCompanyTag(String companyTagName);

	public String existCourier(String courierName, Long courierIdNew);

	public String existRoll(String rollName, Long rolloId);

	public String getCompanyTagName(Long companyTagId);

    public String getCourierName(Long courierId);

	public List<TbCourier> getCourier(Long companyTagId);

	public List<TbRollo> getRollosByCourier(Long courierId, Long rolloId);
	
	public boolean getpermission(Long userId, String nameperm);

}
