package ejb.crud;
import java.util.List;

import javax.ejb.Remote;

import jpa.TbCompany;
import jpa.TbCompanyType;

@Remote
public interface Company {
	
	/**
	 * 
	 * @return {@link TbCompany} {@link List}
	 */
	public List<TbCompany> getCompany();
	
	/**
	 * 
	 * @param company
	 * @param companyName 
	 * @param companyTypeId
	 * @param ip
	 * @param creationUser
	 * @param nroContrato 
	 * @param nitFideicomiso 
	 * @param fideicomiso 
	 * @return {@link Long} -1 if the company exits, null if error, 0 if successful.
	 */
	public Long insertCompany(String companyNit,String companyName, Long companyTypeId, String ip,
			Long creationUser, String fideicomiso, String nitFideicomiso, String nroContrato, String disclaimer);
	
	/**
	 * 
	 * @return
	 */
	public List<TbCompany> getConcession();
	
	/**
	 * 
	 * @return
	 */
	public List<TbCompanyType> getCompanytype();
	
	public Long updateCompany(Long companyId,String newCompanyNit, String newCompanyName,
			Long newCompanyTypeId, String ip, Long userId,
			String newFideicomiso, String newNitFideicomiso,
			String newNroContrato, String newDisclaimer);
}
