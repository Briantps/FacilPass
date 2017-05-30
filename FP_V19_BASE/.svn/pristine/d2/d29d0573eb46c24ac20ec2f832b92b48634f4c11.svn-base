package ejb;

import java.util.List;

import javax.ejb.Remote;


import util.AllInfoAccount;


@Remote
public interface AccountClose {
	
	/**
	 * Método encargado de listar las cuentas pendientes de cierre (account_state_type_id=2 tabla tb-account) 
	 * y Pendiente de aprobación de disociación (state_account_bank=4 tabla re-account_bank)
	 * @return the accountCloseList
	 * @author psanchez
	 */
	public List<AllInfoAccount> getAllAccountClose();
	
	public boolean closeAccount(Long account, Long accountBankId, String ip, Long creationUser);
	
}