/**
 * 
 */
package process.purchasing;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import constant.AccountStateType;

import sessionVar.SessionUtil;

import jpa.TbAccount;
import jpa.TbBank;
import jpa.TbBankAccount;
import jpa.TbPayType;

import ejb.Purchase;
import ejb.User;

/**
 * @author tmolina
 *
 */
public class ConsigBean implements Serializable {
	private static final long serialVersionUID = 1881367628941426545L;
	
	@EJB(mappedName="ejb/Purchase")
	private Purchase purchase;
	
	@EJB(mappedName="ejb/User")
	private User user;
	
	//	 Attributes ----------------------------- //
	
	private List<SelectItem> consigTypesList;
	
	private List<SelectItem> bankList;
	
	private Long consigType;
	
	private String numCheck;
	
	private Long idBank;
	
	private Date consigDate;
	
	private String configOffice;
	
	private String consigNumber;
	
	private Long accountId;
	
	private List<SelectItem> accountList;
	
	private Long consigValue;
	
	private String originAccount;
	
	private List<SelectItem> clients;
	
	private List<SelectItem> clientAccounts;
	
	private List<SelectItem> consignmentConceptList;
	
	// Visibility  ------------------- //
	
	private boolean showFormCheck;
	
	private boolean showTransfer;
	
	private boolean checkTransfer;
	
	private boolean checkEff;
	
	private boolean showCash;
	
	// Control Modal
	
	private boolean showModal;
	
	private String modalMessage;
	
	private boolean confirmationShow;
	
	private String corfirmMessage;
	
	private Long idAccount;
	
	private String consignmentConcept;
	
	/**
	 * Constructor.
	 */
	public ConsigBean() {
		init();
	}
	
	// Actions ----------------//
	
	public void init(){
		setShowFormCheck(false);
		setCheckTransfer(false);
		setCheckEff(false);
		setShowTransfer(false);
		setShowCash(true);
		setClientAccounts(null);
	}
	
	/**
	 * Change form fields according consignment pay type. 
	 */
	public String changeForm(){
		
		setShowTransfer(false);
		setShowFormCheck(false);
		setCheckTransfer(false);
		setCheckEff(false);
		setShowCash(false);
		
		if(consigType.longValue() == 0L){
			setShowCash(true);
		}
		
		if (consigType.longValue() == 1L ||
				consigType.longValue() == 4L) {
			setShowFormCheck(true);		
		} 
		
		if (consigType.longValue() == 1L || consigType.longValue() == 4L
				|| consigType.longValue() == 2L) {
			setCheckTransfer(true);
		}
		
		if(consigType.longValue() == 2L){
			setShowTransfer(true);
		}
		
		if (consigType.longValue() == 1L || consigType.longValue() == 4L
				|| consigType.longValue() == 3L) {
			setCheckEff(true);
		}
		return null;
	}
	
	/**
	 * Shows Confirmation message.
	 */
	public String initConsig()
	{
		setShowModal(false);
		setConfirmationShow(false);
		NumberFormat nf = NumberFormat.getInstance();
		if (idAccount != null && idAccount.longValue() != -1)
		{
			switch(consigType.intValue())
			{
			case 0:
			{
				if(consigValue != null)
				{
					setConfirmationShow(true);
					setCorfirmMessage("¿Está Seguro que desea registrar la consignación " +
							", por valor de $" + nf.format(consigValue) +  "?");
				}
				else
				{
					setModalMessage("El valor de consignación es requerido.");
					setShowModal(true);
				}
				break;
			}
			case 1:
			{
				if(consigDate != null)
				{
					if(consigValue != null)
					{
						if(numCheck.equals("") == false)
						{
							if(configOffice.equals("") == false)
							{
								if(consigNumber.equals("") == false)
								{
									setConfirmationShow(true);
									setCorfirmMessage("¿Está Seguro que desea registrar la consignación " +
											", por valor de $" + nf.format(consigValue) +  "?");
								}
								else
								{
									setModalMessage("El número de la consignación es requerido.");
									setShowModal(true);
								}
							}
							else
							{
								setModalMessage("La oficina de consignación es requerida.");
								setShowModal(true);
							}						
						}
						else
						{
							setModalMessage("El número de cheque es requerido.");
							setShowModal(true);
						}					
					}
					else
					{
						setModalMessage("El valor de consignación es requerido.");
						setShowModal(true);
					}
				}
				else
				{
					setModalMessage("La fecha de consignación es requerida.");
					setShowModal(true);
				}
				break;
			}
			case 2:
			{
				if(consigDate != null)
				{
					if(consigValue != null)
					{
						if(originAccount.equals("") == false)
						{
							setConfirmationShow(true);
							setCorfirmMessage("¿Está Seguro que desea registrar la consignación " +
									", por valor de $" + nf.format(consigValue) +  "?");
						}
						else
						{
							setModalMessage("El número de cuenta de origen es requerido.");
							setShowModal(true);
						}					
					}
					else
					{
						setModalMessage("El valor de consignación es requerido.");
						setShowModal(true);
					}
					
				}
				else
				{
					setModalMessage("La fecha de consignación es requerida.");
					setShowModal(true);
				}
				break;
			}
			case 3:
			{
				if(consigDate != null)
				{
					if(consigValue != null)
					{
						if(configOffice.equals("") == false)
						{
							if(consigNumber.equals("") == false)
							{
								setConfirmationShow(true);
								setCorfirmMessage("¿Está Seguro que desea registrar la consignación " +
										", por valor de $" + nf.format(consigValue) +  "?");
							}
							else
							{
								setModalMessage("El número de la consignación es requerido.");
								setShowModal(true);
							}
						}
						else
						{
							setModalMessage("La oficina de consignación es requerida.");
							setShowModal(true);
						}						
					}
					else
					{
						setModalMessage("El valor de consignación es requerido.");
						setShowModal(true);
					}
					
				}
				else
				{
					setModalMessage("La fecha de consignación es requerida.");
					setShowModal(true);
				}
				break;
			}
			case 4:
			{
				if(consigDate != null)
				{
					if(consigValue != null)
					{
						if(numCheck.equals("") == false)
						{
							if(configOffice.equals("") == false)
							{
								if(consigNumber.equals("") == false)
								{
									setConfirmationShow(true);
									setCorfirmMessage("¿Está Seguro que desea registrar la consignación " +
											", por valor de $" + nf.format(consigValue) +  "?");
								}
								else
								{
									setModalMessage("El número de la consignación es requerido.");
									setShowModal(true);
								}
							}
							else
							{
								setModalMessage("La oficina de consignación es requerida.");
								setShowModal(true);
							}						
						}
						else
						{
							setModalMessage("El número de cheque es requerido.");
							setShowModal(true);
						}					
					}
					else
					{
						setModalMessage("El valor de consignación es requerido.");
						setShowModal(true);
					}
					
				}
				else
				{
					setModalMessage("La fecha de consignación es requerida.");
					setShowModal(true);
				}
				break;
			}
			case 6:
			{
				if(consigDate != null)
				{
					if(consigValue != null)
					{
						setConfirmationShow(true);
						setCorfirmMessage("¿Está Seguro que desea registrar la consignación " +
								", por valor de $" + nf.format(consigValue) +  "?");
					}
					else
					{
						setModalMessage("El valor de consignación es requerido.");
						setShowModal(true);
					}
				}
				else
				{
					setModalMessage("La fecha de consignación es requerida.");
					setShowModal(true);
				}
				break;
			}
			}
		}
		else
		{
			setModalMessage("Debe Seleccionar una Cuenta");
			setShowModal(true);
		}
		return null;
	}
	
	/**
	 * 
	 */
	public String consig() {
		setConfirmationShow(false);
		System.out.println(idAccount);
		if (idAccount != null && idAccount.longValue() != -1) {
			if (purchase.saveConsignment(idAccount, consigValue, consigNumber,
					consigDate, SessionUtil.ip(), consigType, numCheck, idBank,
					configOffice, accountId, originAccount, SessionUtil
							.sessionUser().getUserId(), consignmentConcept) != null) {
				setModalMessage("Se ha registrado la Consignación Satisfactoriamente.");
				clear();
			} else {
				setModalMessage("No se pudo realizar la Transacción, Comuníquese con Servicio al Cliente.");
			}
		}
		else {
			setModalMessage("Debe Seleccionar una Cuenta");
		}
		setShowModal(true);
		return null;
	}
	
	/**
	 * 
	 */
	public String registerConsig() {
		setConfirmationShow(false);
		if (purchase.saveConsignment(idAccount, consigValue,
				consigNumber, consigDate, SessionUtil.ip(), consigType,
				numCheck, idBank, configOffice, accountId, originAccount,
				SessionUtil.sessionUser().getUserId(),consignmentConcept) != null) {
			setModalMessage("Se ha registrado la Consignación Satisfactoriamente.");
			clear();
		} else {
			setModalMessage("No se pudo realizar la Transacción.");
		}
		setShowModal(true);
		return null;
	}
	
	/**
	 * Hides Modal.
	 */
	public String hideModal(){
		setConfirmationShow(false);
		setShowModal(false);
		setCorfirmMessage("");
		setModalMessage("");
		return null;
	}
	
	/**
	 * Clear fields.
	 */
	public void clear(){
		init();
		setConsigValue(null);
		setConsigNumber(null);
		setConsigDate(null);
		setConsigType(0L);
		setNumCheck(null);
		setIdBank(1L);
		setConfigOffice(null);
		setAccountId(1L);
		setOriginAccount(null);		
		setIdAccount(null);
	}

	// Getters and Setters ------------------------ //
	
	/**
	 * @param consigTypesList the consigTypesList to set
	 */
	public void setConsigTypesList(List<SelectItem> consigTypesList) {
		this.consigTypesList = consigTypesList;
	}

	/**
	 * @return the consigTypesList
	 */
	public List<SelectItem> getConsigTypesList() {
		if (consigTypesList == null) {
			
			consigType = 0L;
			
			consigTypesList = new ArrayList<SelectItem>();
			
			// Getting the list of consignment pay types
			List<TbPayType> listCon = purchase.getPayTypes();
			
			for (TbPayType ct : listCon) {
				if(ct.getPayTypeId().longValue() != 5L){
					consigTypesList.add(new SelectItem(
							ct.getPayTypeId(), ct.getPayTypeName()));
				}
			}
		}
		return consigTypesList;
	}

	/**
	 * @param consigType the consigType to set
	 */
	public void setConsigType(Long consigType) {
		this.consigType = consigType;
	}

	/**
	 * @return the consigType
	 */
	public Long getConsigType() {
		return consigType;
	}

	/**
	 * @param numCheck the numCheck to set
	 */
	public void setNumCheck(String numCheck) {
		this.numCheck = numCheck;
	}

	/**
	 * @return the numCheck
	 */
	public String getNumCheck() {
		return numCheck;
	}

	/**
	 * @param showFormCheck the showFormCheck to set
	 */
	public void setShowFormCheck(boolean showFormCheck) {
		this.showFormCheck = showFormCheck;
	}

	/**
	 * @return the showFormCheck
	 */
	public boolean isShowFormCheck() {
		return showFormCheck;
	}

	/**
	 * @param bankList the bankList to set
	 */
	public void setBankList(List<SelectItem> bankList) {
		this.bankList = bankList;
	}

	/**
	 * @return the bankList
	 */
	public List<SelectItem> getBankList() {
		if(bankList == null) {	
			List<TbBank> banks= purchase.getBanks();
			
			bankList = new ArrayList<SelectItem>();
			for (TbBank b : banks) {
				bankList.add(new SelectItem(b.getBankId(), b.getBankName()));
			}
		}
		return bankList;
	}

	/**
	 * @param idBank the idBank to set
	 */
	public void setIdBank(Long idBank) {
		this.idBank = idBank;
	}

	/**
	 * @return the idBank
	 */
	public Long getIdBank() {
		return idBank;
	}

	/**
	 * @param consigDate the consigDate to set
	 */
	public void setConsigDate(Date consigDate) {
		this.consigDate = consigDate;
	}

	/**
	 * @return the consigDate
	 */
	public Date getConsigDate() {
		return consigDate;
	}

	/**
	 * @param configOffice the configOffice to set
	 */
	public void setConfigOffice(String configOffice) {
		this.configOffice = configOffice;
	}

	/**
	 * @return the configOffice
	 */
	public String getConfigOffice() {
		return configOffice;
	}

	/**
	 * @param consigNumber the consigNumber to set
	 */
	public void setConsigNumber(String consigNumber) {
		this.consigNumber = consigNumber;
	}

	/**
	 * @return the consigNumber
	 */
	public String getConsigNumber() {
		return consigNumber;
	}

	/**
	 * @param accountId the accountId to set
	 */
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	/**
	 * @return the accountId
	 */
	public Long getAccountId() {
		return accountId;
	}

	/**
	 * @param accountList the accountList to set
	 */
	public void setAccountList(List<SelectItem> accountList) {
		this.accountList = accountList;
	}

	/**
	 * @return the accountList
	 */
	public List<SelectItem> getAccountList() {
		if(accountList == null){
			accountList = new ArrayList<SelectItem>();
			
			List<TbBankAccount> acc = purchase.getAccounts();
			
			for (TbBankAccount ba : acc) {
				accountList.add(new SelectItem(ba.getBankAccountId(), ba
						.getBankAccountNumber()));
			}
		}
		return accountList;
	}

	/**
	 * @param consigValue the consigValue to set
	 */
	public void setConsigValue(Long consigValue) {
		this.consigValue = consigValue;
	}

	/**
	 * @return the consigValue
	 */
	public Long getConsigValue() {
		return consigValue;
	}

	/**
	 * @param showModal the showModal to set
	 */
	public void setShowModal(boolean showModal) {
		this.showModal = showModal;
	}

	/**
	 * @return the showModal
	 */
	public boolean isShowModal() {
		return showModal;
	}

	/**
	 * @param modalMessage the modalMessage to set
	 */
	public void setModalMessage(String modalMessage) {
		this.modalMessage = modalMessage;
	}

	/**
	 * @return the modalMessage
	 */
	public String getModalMessage() {
		return modalMessage;
	}

	/**
	 * @param showTransfer the showTransfer to set
	 */
	public void setShowTransfer(boolean showTransfer) {
		this.showTransfer = showTransfer;
	}

	/**
	 * @return the showTransfer
	 */
	public boolean isShowTransfer() {
		return showTransfer;
	}

	/**
	 * @param checkTransfer the checkTransfer to set
	 */
	public void setCheckTransfer(boolean checkTransfer) {
		this.checkTransfer = checkTransfer;
	}

	/**
	 * @return the checkTransfer
	 */
	public boolean isCheckTransfer() {
		return checkTransfer;
	}

	/**
	 * @param originAccount the originAccount to set
	 */
	public void setOriginAccount(String originAccount) {
		this.originAccount = originAccount;
	}

	/**
	 * @return the originAccount
	 */
	public String getOriginAccount() {
		return originAccount;
	}

	/**
	 * @param checkEff the checkEff to set
	 */
	public void setCheckEff(boolean checkEff) {
		this.checkEff = checkEff;
	}

	/**
	 * @return the checkEff
	 */
	public boolean isCheckEff() {
		return checkEff;
	}

	/**
	 * @param confirmationShow the confirmationShow to set
	 */
	public void setConfirmationShow(boolean confirmationShow) {
		this.confirmationShow = confirmationShow;
	}

	/**
	 * @return the confirmationShow
	 */
	public boolean isConfirmationShow() {
		return confirmationShow;
	}

	/**
	 * @param corfirmMessage the corfirmMessage to set
	 */
	public void setCorfirmMessage(String corfirmMessage) {
		this.corfirmMessage = corfirmMessage;
	}

	/**
	 * @return the corfirmMessage
	 */
	public String getCorfirmMessage() {
		return corfirmMessage;
	}

	/**
	 * @param showCash the showCash to set
	 */
	public void setShowCash(boolean showCash) {
		this.showCash = showCash;
	}

	/**
	 * @return the showCash
	 */
	public boolean isShowCash() {
		return showCash;
	}

	/**
	 * @param clients the clients to set
	 */
	public void setClients(List<SelectItem> clients) {
		this.clients = clients;
	}

	/**
	 * @return the clients
	 */
	public List<SelectItem> getClients() {
		if(clients == null){
			clients = new ArrayList<SelectItem>();
			clients.add(new SelectItem(-1L, "--Seleccione Una--"));
			for(TbAccount su : user.getClientAccount(null)){
			 if(su.getAccountState().longValue() == AccountStateType.ACTIVE.getId()){	
				String name = su.getTbSystemUser().getUserNames();
				if(su.getTbSystemUser().getTbCodeType().getCodeTypeId().longValue() != 3L){
					name += " " + su.getTbSystemUser().getUserSecondNames();
				}
				clients.add(new SelectItem(su.getAccountId(), name + " - Cuenta No. " + su.getAccountId()));
			 }
			}
		}
		return clients;
	}

	/**
	 * @param idAccount the idAccount to set
	 */
	public void setIdAccount(Long idAccount) {
		this.idAccount = idAccount;
	}

	/**
	 * @return the idAccount
	 */
	public Long getIdAccount() {
		return idAccount;
	}

	/**
	 * @param clientAccounts the clientAccounts to set
	 */
	public void setClientAccounts(List<SelectItem> clientAccounts) {
		this.clientAccounts = clientAccounts;
	}

	/**
	 * @return the clientAccounts
	 */
	public List<SelectItem> getClientAccounts() {
		if(clientAccounts == null){
			clientAccounts = new ArrayList<SelectItem>();
			clientAccounts.add(new SelectItem(-1L, "--Seleccione Una--"));
			for(TbAccount su : user.getClientAccount(SessionUtil.sessionUser().getUserId())){
				clientAccounts.add(new SelectItem(su.getAccountId(), "Cuenta No. " + su.getAccountId()));
			}
		}
		return clientAccounts;
	}

	/**
	 * @param consignmentConcept the consignmentConcept to set
	 */
	public void setConsignmentConcept(String consignmentConcept) {
		this.consignmentConcept = consignmentConcept;
	}

	/**
	 * @return the consignmentConcept
	 */
	public String getConsignmentConcept() {
		return consignmentConcept;
	}

	/**
	 * @param consignmentConceptList the consignmentConceptList to set
	 */
	public void setConsignmentConceptList(List<SelectItem> consignmentConceptList) {
		this.consignmentConceptList = consignmentConceptList;
	}

	/**
	 * @return the consignmentConceptList
	 */
	public List<SelectItem> getConsignmentConceptList() {
		if (consignmentConceptList == null) {
			consignmentConceptList = new ArrayList<SelectItem>();
			consignmentConceptList.add(new SelectItem("RECARGA DE DISPOSITIVO", "RECARGA DE DISPOSITIVO"));
			consignmentConceptList.add(new SelectItem(	"PAGO POR COMPRA DE DISPOSITIVO", "PAGO POR COMPRA DE DISPOSITIVO"));
		}
		return consignmentConceptList;
	}
}