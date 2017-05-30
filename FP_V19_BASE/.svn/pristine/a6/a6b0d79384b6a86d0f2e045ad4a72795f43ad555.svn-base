package ejb.crud;
import java.util.List;

import javax.ejb.Remote;

import util.AllInfoAccount;
import util.ReAccountBank;

import jpa.TbBankAccount;
import jpa.TbProductType;

@Remote
public interface BankAccount {
	
	/**
	 * 
	 * @return {@link TbBankAccount} {@link List}
	 */
	public List<TbBankAccount> getBankAccounts();
	
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
	public Long insertBankAccount(String bankAccountHolder,
			String bankAccountHolderNit, String ip, Long userId,
			String product, String bankAccountNumber, Long bankAccountType);

	public List<jpa.TbBankAccount> getProductsByClient(Long idBank, String codeClient);

	public boolean insertBankAccountAdmin(Long clientId,
			Long bankId, Long typeProductsId, String accountNumber, String ip, Long creationUser, Long initValue);
	
	public List<ReAccountBank> getProductsByClient2(Long idClient);

	public String haveProductAssociation(Long idAccount);
	
	public List<TbProductType> getProductType();
	
	public String getProductNumber(Long idProduct);
	
	public List<TbBankAccount> getProductsByClient2(Long idBank, String userCode, Long codeType);
	
	public boolean isProcessDisociationById(Long bankAccountId);

	public boolean insertBankAccountAdminPSE(Long clientId, String banking, Long idAccount, String ip, Long creationUser);

	public List<AllInfoAccount> getClientAccount(Long idClient);
}

