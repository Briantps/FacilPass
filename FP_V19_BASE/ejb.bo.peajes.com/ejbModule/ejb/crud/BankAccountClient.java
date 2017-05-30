package ejb.crud;
import java.util.List;

import javax.ejb.Remote;

import util.ReAccountBank;

import jpa.TbBank;
import jpa.TbBankAccount;
import jpa.TbProductType;

@Remote
public interface BankAccountClient {
	
	/**
	 * 
	 * @return {@link TbBankAccount} {@link List}
	 */
	public List<ReAccountBank> getBankAccounts(Long userId);
	
	/**
	 * 
	 * @param bankAccountHolder
	 * @param bankAccountHolderNit
	 * @param ip
	 * @param creationUser
	 * @param product
	 * @param bankAccountNumber
	 * @param bankAccountType
	 * @return  {@link Long} -1 if the bank exits, null if error, 0 if successful.
	 */
	public boolean insertBankAccountClient(Long UserId, Long idBank, String bankAccountNumber, 
			Long bankAccountType, Long initValue, String ip, Long creationUser);
	
	public boolean existBankAccountClient(Long userId, Long idBank, 
					    String bankAccountNumber, Long bankAccountType);

	/**
	 * 
	 * @return {@link TbBank} {@link List}
	 */
	public List<TbBank> getBanks();
	
	public List<TbProductType> getProductType();

}
