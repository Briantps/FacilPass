package ejb.crud;
import java.util.List;

import javax.ejb.Remote;

import jpa.TbWarehouseCardType;

@Remote
public interface WarehouseCardType {
	
	/**
	 * 
	 * @return {@link TbWarehouseCardType} {@link List}
	 */
	public List<TbWarehouseCardType> getWarehouseCardType();
	
	/**
	 * 
	 * @param cardType
	 * @param ip
	 * @param creationUser
	 * @return {@link Long} -1 if the cardType exits, null if error, 0 if successful.
	 */
	public Long insertCardType(String cardType, String ip, Long creationUser);
	
	/**
	 * @param cardTypeId
	 * @param cardType
	 * @param ip
	 * @param creationUser
	 * @return <code>true</code> if successful, <code>false</code> otherwise.
	 */
	public boolean editCardType(Long cardTypeId, String cardType,
			String ip, Long creationUser);
}