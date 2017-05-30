package ejb.crud;
import java.util.List;

import javax.ejb.Remote;

import jpa.TbSpecialExemptType;

@Remote
public interface SpecialExemptType {
	
	/**
	 * 
	 * @return {@link TbSpecialExemptType} {@link List}
	 */
	public List<TbSpecialExemptType> getEspecialExemptTypes();
	
	/**
	 * 
	 * @param specialExemptType
	 * @param ip
	 * @param creationUser
	 * @param deviceTypeId
	 * @return {@link Long} -1 if the type exits, null if error, 0 if successful.
	 */
	public Long insertTbSpecialExemptType(String specialExemptType, String ip, Long creationUser, Long deviceTypeId);
	
	/**
	 * 
	 * @return {@link TbSpecialExemptType} {@link List}
	 */
	public List<TbSpecialExemptType> getExemptTypes();
}
