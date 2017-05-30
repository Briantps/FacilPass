package process.account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import sessionVar.SessionUtil;
import validator.Validation;

import ejb.User;

import jpa.TbAccount;
import jpa.TbCodeType;
import jpa.TbSystemUser;


/**
 * Bean to manage note account creation.
 * @author ablanco
 *
 */
public class CreateNote implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext(unitName = "bo") 
	EntityManager em;

	@EJB(mappedName="ejb/User")
	private User user;
	
	// Attributes ------------- //
	
	private Long idAccount;
	
	private List<TbCodeType> codeTypes;
	
	private List<SelectItem> codeTypesList;
	
	private Long codeType;
	
	private String codeClient;
	
	private String userNames;
	
	private String userSecondNames;
	
	private List<SelectItem> clientNames;
	
	private Long idClient;
	
	private List<TbSystemUser> clients; 
	
	private TbSystemUser client;
	
    // Control Visibility -----//
	
	private boolean showData;
		
	// Control Modal ------//
	
	private boolean showModal;
	
	private String modalMessage;
	
	private boolean showConfirmation;
	
	private boolean showType;
	
	private String corfirmMessage;
	
	private List<TbAccount> accountList;
	
	private boolean showNoteAccount;
	
	private String note="";
	
	private Long type2=2L;

	/**
	 * Constructor.
	 */
	public CreateNote() {
		setNote("");
		setShowNoteAccount(false);
		setType2(2l);
	}

	
	public void init() {
		setCodeTypesList(null);
		setClientNames(null);
		setClients(null);
		setShowData(false);
		setCodeClient("");
		setUserNames("");
		setUserSecondNames("");
		setNote("");	
	}
	
	// Actions ------------------- //
	public void clearFilter(){
		this.setShowModal(false);
		this.setShowData(false);
		this.setShowNoteAccount(false);	
		this.setModalMessage("");
		this.setCodeClient("");
		this.setUserNames("");
		this.setUserSecondNames("");
		this.setIdClient(null);
		this.setCodeType(1L);
	}
	
	/**
	 * Searches a client by code and type code.
	 */
	public void search() {
		setShowModal(false);
		setShowData(false);
		idClient = -1L;
		if (codeClient != "") {
			client = user.getUserByCode(codeClient, codeType);
			if (client != null) {
				idClient = client.getUserId();
				this.setIdClient(client.getUserId());
				accountList = user.getClientAccount(idClient);
				setShowData(true);
			} else {
				setShowModal(true);
				setModalMessage("Error: No hay información para el cliente digitado." );
				setShowData(false);
				setShowNoteAccount(false);				
			}
		} else {
			setModalMessage("Error: Debe digitar el número de identificación del cliente." );
			setShowModal(true);
			setShowData(false);
			setShowNoteAccount(false);
		}
	}
	
	/** Método encargado de realizar búsqueda por código, nombre o apellido del cliente
	 * @author psanchez
	 */	
	public void searchClient() {
		this.setShowModal(false);
		this.setShowData(false);
		this.setShowNoteAccount(false);
		this.setModalMessage("");

		if (codeClient.equals("") && userNames.equals("") && userSecondNames.equals("")) {
			this.setModalMessage("Ingrese filtro de búsqueda." );
			this.setShowModal(true);
			this.setShowData(false);
			this.setShowNoteAccount(false);	
		}else if(!codeClient.equals("") && !Validation.isNumeric(codeClient)){
			this.setModalMessage("El campo No. Identificación contiene caracteres inválidos.");
			this.setShowModal(true);
			this.setShowData(false);
			this.setShowNoteAccount(false);			
    	}else if(userNames!="" && !userNames.matches("([a-z]|[A-Z]|[áéíóú]|[ÁÉÍÓÚ]|[0-9]|[&/]|[_-]|[ñÑ]|[.]|\\s)+")){
	    	this.setModalMessage("El campo Nombre contiene caracteres inválidos.");
	    	this.setShowModal(true);
			this.setShowData(false);
			this.setShowNoteAccount(false);			
    	 }else if(userSecondNames!="" && !userSecondNames.matches("([a-z]|[A-Z]|[áéíóú]|[ÁÉÍÓÚ]|[ñÑ]|\\s)+")){
	    	this.setModalMessage("El campo Apellidos contiene caracteres inválidos.");
	    	this.setShowModal(true);
			this.setShowData(false);
			this.setShowNoteAccount(false);			
    	  }
			Long userId = user.getUserFromFiltersCreateNote(codeClient, codeType, userNames, userSecondNames);
			if (userId.equals(-2L)) {
				setShowModal(true);
				setModalMessage("Hay más de un resultado en la consulta, por favor agregue otro campo en el filtro.");
				setShowData(false);
				setShowNoteAccount(false);			
			}		
			else if (userId.equals(-1L)) {
				this.setModalMessage("Ocurrió un error al momento de realizar la búsqueda.");
				this.setShowModal(true);
				this.setShowData(false);
				this.setShowNoteAccount(false);		
			}
			else if (userId.equals(0L)) {
				this.setModalMessage("No hay información para el cliente digitado.");
				this.setShowModal(true);
				this.setShowData(false);
				this.setShowNoteAccount(false);		
			}
			else if (userId>-1L) {
				client = em.find(TbSystemUser.class, userId);
				codeClient = client.getUserCode();
				userNames = client.getUserNames();
				userSecondNames = client.getUserSecondNames();
				accountList = user.getClientAccount(client.getUserId());
				this.setShowData(true);
		    }
	}

	
	/*
	 * Change the data of client.
	 */
	public void changeClient() {
		this.setShowNoteAccount(false);
		setShowModal(false);
		if (idClient.longValue() == -1) {
			showData = false;
			this.codeType = null;
			this.codeClient = "";
		} else {
			this.showData = false;
			for(TbSystemUser u : getClients()){
				if(idClient.longValue() == u.getUserId().longValue()){
					accountList = user.getClientAccount(idClient);
					this.client = u;
					this.codeType = u.getTbCodeType().getCodeTypeId();
					this.codeClient = u.getUserCode();
					this.showData = true;
					break;
				}
			}
		}
	}
	
	/**
	 * Clean Modal Panel.
	 */
	public void hideModal(){
		setShowModal(false);
		setModalMessage("");
		setShowConfirmation(false);
		setShowType(false);
		setShowNoteAccount(false);
		setNote("");
		setType2(2l);
	}
	
	public void hideModal2(){
		setShowModal(false);
		if(modalMessage=="Se crea nota a cuenta."){
			setNote("");
			setModalMessage("");
			setShowConfirmation(false);
			setType2(2l);
		}		
	}
	
	public void createNoteAccount(){
		TbAccount account= em.find(TbAccount.class, idAccount);
		setShowConfirmation(false);
		if(note!=null && !note.equals("")){
				if(note.length()<2000){
					if(user.createNoteAccount(account.getTbSystemUser().getUserId(), idAccount, note,type2, SessionUtil.ip(), SessionUtil.sessionUser().getUserId())){
						setModalMessage("Se crea nota a cuenta.");
						setShowModal(true);
					}
					else{
						setModalMessage("Error: La nota no pudo ser creada.");
						setShowModal(true);
					}
				}
				else{
					setModalMessage("Error: El campo Descripción Nota no debe ser mayor a 2000 caracteres.");
					setShowModal(true);
				}	
		}
		else{
			setModalMessage("Error: El campo Descripción Nota no puede estar vacío");
			setShowModal(true);
		}
	}
	
	// Getters and setters -------------- //
	public void setCodeTypesList(List<SelectItem> codeTypesList) {
		this.codeTypesList = codeTypesList;
	}

	public List<SelectItem> getCodeTypesList() {
		if(codeTypesList==null){
			codeTypesList = new ArrayList<SelectItem>();
		} else {
			codeTypesList.clear();
		}
		for (TbCodeType type : getCodeTypes()) {
			codeTypesList.add(new SelectItem(type.getCodeTypeId(), type
					.getCodeTypeAbbreviation()));
		}
		return codeTypesList;
	}

	public void setCodeType(Long codeType) {
		this.codeType = codeType;
	}

	public Long getCodeType() {
		return codeType;
	}

	public void setCodeClient(String codeClient) {
		this.codeClient = codeClient;
	}

	public String getCodeClient() {
		return codeClient;
	}

	public void setClientNames(List<SelectItem> clientNames) {
		this.clientNames = clientNames;
	}

	public List<SelectItem> getClientNames() {
		if (clientNames == null) {
			clientNames = new ArrayList<SelectItem>();
			
			clientNames.add(new SelectItem(-1, " "));
			for(TbSystemUser tu : getClients()){
				String name = tu.getUserNames();
				if(tu.getTbCodeType().getCodeTypeId().longValue() != 3){
					name = name + " " + tu.getUserSecondNames();
				}
				clientNames.add(new SelectItem(tu.getUserId(), name));
			}
		}		
		return clientNames;
	}

	public void setIdClient(Long idClient) {
		this.idClient = idClient;
	}

	public Long getIdClient() {
		return idClient;
	}

	public void setClients(List<TbSystemUser> clients) {
		this.clients = clients;
	}

	public List<TbSystemUser> getClients() {
		if (clients == null) {
			clients = new ArrayList<TbSystemUser>();
			clients = user.getAllActiveClient();
		}
		return clients;
	}

	public void setShowData(boolean showData) {
		this.showData = showData;
	}

	public boolean isShowData() {
		return showData;
	}

	public void setShowModal(boolean showModal) {
		this.showModal = showModal;
	}

	public boolean isShowModal() {
		return showModal;
	}

	public void setModalMessage(String modalMessage) {
		this.modalMessage = modalMessage;
	}

	public String getModalMessage() {
		return modalMessage;
	}

	public void setClient(TbSystemUser client) {
		this.client = client;
	}

	public TbSystemUser getClient() {
		return client;
	}

	public void setCodeTypes(List<TbCodeType> codeTypes) {
		this.codeTypes = codeTypes;
	}

	public List<TbCodeType> getCodeTypes() {
		if(codeTypes == null){
			codeTypes = new ArrayList<TbCodeType>();
		}
		codeTypes = user.getCodeTypes();
		return codeTypes;
	}

	public void setShowConfirmation(boolean showConfirmation) {
		this.showConfirmation = showConfirmation;
	}

	public boolean isShowConfirmation() {
		return showConfirmation;
	}

	public void setCorfirmMessage(String corfirmMessage) {
		this.corfirmMessage = corfirmMessage;
	}

	public String getCorfirmMessage() {
		return corfirmMessage;
	}

	public void setAccountList(List<TbAccount> accountList) {
		this.accountList = accountList;
	}

	public List<TbAccount> getAccountList() {
		return accountList;
	}

	public void setShowType(boolean showType) {
		this.showType = showType;
	}

	public boolean isShowType() {
		return showType;
	}

	public void setIdAccount(Long idAccount) {
		this.idAccount = idAccount;
	}

	public Long getIdAccount() {
		return idAccount;
	}
	
	public void showNote(){
		this.setShowNoteAccount(true);
	}

	public void setShowNoteAccount(boolean showNoteAccount) {
		this.showNoteAccount = showNoteAccount;
	}

	public boolean isShowNoteAccount() {
		return showNoteAccount;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getNote() {
		return note;
	}
	public void showConfirm(){
		setShowConfirmation(true);
		setCorfirmMessage("¿Está seguro que desea realizar la transacción?");
	}

	public void setType2(Long type2) {
		this.type2 = type2;
	}

	public Long getType2() {
		return type2;
	}


	public void setUserNames(String userNames) {
		this.userNames = userNames;
	}


	public String getUserNames() {
		return userNames;
	}


	public void setUserSecondNames(String userSecondNames) {
		this.userSecondNames = userSecondNames;
	}


	public String getUserSecondNames() {
		return userSecondNames;
	}
}