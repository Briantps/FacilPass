package ejb;


import java.util.List;

import javax.ejb.Remote;

import util.BankAccount;

import jpa.ReAccountBank;
import jpa.TbAccount;
import jpa.TbBank;
import jpa.TbBankAccount;
import jpa.TbProductType;


@Remote
public interface AssociationPaymentMethod {
			
	public boolean haveAccountAssociation(Long idProduct);
	
	public String haveProductAssociation(Long idAccount);
	
	//public boolean editPriority(Long accountBankId, Long priority, String ip, Long userId);
	
	public Long associateProductsAccount(Long userId, Long accountId, String codeClient, Long creationUser, 
            String ip, Long idBank, Long idProduct, Long initValue);

	public Long processAssociation(Long userId, String userCode, Long accountId, Long creationUser, 
			String ip, Long idBank, Long idProduct, Long resp, Long lastIdBankAccount);

	public String dissociationBankAccount(Long userId, Long accountBankId, Long bankId, String ip, Long creationUser);
		
	//public List<TbBank> getBanksByClient(String userCode);
	
	public List<TbBank> getBanksByClient(Long userId);

	public List<TbProductType> getProductTypeByClient(Long idBank, Long userId);
	
	public List<TbBank> getBanksByClient2(String codeClient, Long codeType);
	
	/**
	 * Metodo encargado de validar el saldo de la cuenta antes de disociar un producto bancario
	 * @author ablasquez
	 * @return the ReAccountBank
	 */
	public ReAccountBank validateBalanceAccount(Long accountBankId);
	
	/**
	 * Método encargado de validar si el producto bancario a asociar es el mismo anterior producto bancario
	 * @author ablasquez
	 * @return 0 si es el mismo producto bancario, 1 no es el mismo producto bancario, 2 error en el proceso de verificacion
	 */
	public int samePreviousProductAssociation(Long accountId, Long bankAccountId, Long creationUser,String ip,Long userId);
	
	/**
	 * Método encargado de validar si el producto bancario PSE a asociar es el anterior producto bancario
	 * @author psanchez
	 * @return 0 si es el mismo producto bancario, 1 no es el mismo producto bancario, 2 error en el proceso de verificacion
	 */
	public int samePreviousProductAssociationPSE(Long accountId, Long bankAccountId, Long creationUser, String ip, Long userId);
	
	/**
	 * Método encargado de reactivar los tag inactivos por disociacion
	 * @author ablasquez
	 * @return true cuando se reactivan los dispositivos, de los contrario false
	 */
	public boolean activeDeviceByDissociation(Long accountId);
	
	/** Método encargado de devolver el ultimo producto bancario asociado
	 * @author ablasquez
	 * @return the reaccountbankid 
	 */
	public Long getLastBankingProduct(Long accountId);
	
	/** Método encargado de devolver el ultimo producto bancario asociado
	 * @author psanchez
	 * @return the reaccountbankid 
	 */
	public Long getLastBankingProductPSE(Long accountId);

	/** Método encargado de devolver el idAccountbank si existe para la cuenta 
	 * @author ablasquez
	 * @return the reaccountbankid 
	 */
	public ReAccountBank getRelationProduct (Long accountId, Long bankAccountId);
	
	/**
	 * Metodo encargado de validar si el producto bancario a asociar se encuentra en proceso de disociación
	 * @author ablasquez
	 * @return true or false 
	 */	
	public boolean isProcessDisociationById(Long bankAccountId, Long accountId);
	
	/**
	 * Método creado para obtener la lista de producto(s) bancario(s) disponible(s) del cliente en sesión.
	 * @return the list
	 * @author ablasquez
	 */
	public List<TbBankAccount> getProductsByClient(Long idBank,Long idType, Long userId);
	public String getProductBank(Long idBankAccount);
	
	
	/**
	 * Método creado para obtener la lista de cuentas facilpass disponible(s) del cliente en sesión.
	 * @return the list
	 * @author ablasquez
	 */
	public List<TbAccount> getClientAccount(Long userId);
	
	public Long processAssociationPSE(long userId, Long accountId, Long creationUser, 
            String ip, Long idBank, Long idProduct, Long lastIdBankAccount);
	
	public Long getIdPSE(Long userId, Long creationUser);
	
	public List<BankAccount> getAccountBank(long userId);
	
	public Long associateAccountPSE(Long userId, Long accountId, Long creationUser, 
            String ip, Long idBank, Long idProduct, Long initValue);
	
	public Long getIdFreePSE(Long userId, Long accountId, Long idBank);

	public Long insertBankAccountAdminPSE(Long accountId, Long bankId,
			Long bankAccountId, Long clientId, Long creationUser, String ip);

	public boolean getAccountAssociation(Long accountId);

	public Long associateAccountNewPSE(Long userId, Long accountId,
			Long creationUser, String ip, Long idBank, Long idProduct, Long initValue);

	public Long BankAccountNewPSE(Long userId, Long idBank, Long bankAccountNumber);
	
	/** Se agrega mensaje en Seguimiento de procesos y Ver procesos segun FRD064
	* @author Nramirez
	*/	
	public void createProcessAssociationPSE(long userId, Long accountId, String ip);

}
