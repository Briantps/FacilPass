package ejb.crud;
import java.util.List;

import javax.ejb.Remote;

import jpa.TbSpecialExemptOffice;

@Remote
public interface ExemptOffice {
	
	/**
	 * 
	 * @return {@link TbSpecialExemptOffice} {@link List}
	 */
	public List<TbSpecialExemptOffice> getEspecialExemptOffice();
	
	/**
	 * 
	 * @param officeName
	 * @param ip
	 * @param creationUser
	 * @param exemptTypeId
	 * @param authoreizedBy
	 * @param officeAddress
	 * @param officeEmail
	 * @param officeFax
	 * @param officePhone
	 * @return {@link Long} -1 if the type exits, null if error, 0 if successful.
	 */
	public Long insertTbSpecialExemptOffice(String specialExemptOffice, String ip, Long creationUser, 
			Long exemptTypeId, String authoreizedBy, String officeAddress, String officeEmail,
			String officeFax, String officePhone);
	
	/**
	 * 
	 * @param officeName
	 * @param ip
	 * @param creationUser
	 * @param authoreizedBy
	 * @param officeAddress
	 * @param officeEmail
	 * @param officeFax
	 * @param officePhone
	 * @return <code>true</code> if successful, <code>false</code> otherwise.
	 */
	public boolean editTbSpecialExemptOffice(String officeName, String ip, Long creationUser, 
			String authoreizedBy, String officeAddress, String officeEmail,
			String officeFax, String officePhone, Long officeId);
}
