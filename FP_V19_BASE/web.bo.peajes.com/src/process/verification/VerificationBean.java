/**
 * 
 */
package process.verification;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import constant.ProcessTrackDetailType;
import ejb.Role;
import ejb.User;

import sessionVar.SessionUtil;

import jpa.TbCodeType;
import jpa.TbSystemUser;

/**
 * @author tmolina
 *
 */
public class VerificationBean implements Serializable{
	private static final long serialVersionUID = -5113520927747248895L;
	
	@EJB(mappedName="ejb/Process")
	private ejb.Process process;
	
	@EJB(mappedName="ejb/User")
	private User userEJB;
	
	@EJB(mappedName="ejb/Role")
	private Role role;
	
	
	// Attributes-------------------//
	
	private List<TbSystemUser> clients = new ArrayList<TbSystemUser>();
	
	private List<SelectItem> codeTypeList;
	
	private Long codeTypeId;
	
	private String userCode;
	
	private TbSystemUser client;
	
	private String codeClient;
	
	private boolean verfOk;
	
	private boolean verfNo;
	
	private String observation="";
	
	// Control visibility ----//
	
	private boolean showMessage;
	
	private boolean showData;
	
	private boolean showObs;
	
	// Control modal ------//
	
	private boolean showModal;
	
	private String modalMessage;
	
	private Long type=1l;
	
	/**
	 * Constructor.
	 */
	public VerificationBean(){
		this.setType(1L);
		this.type=1l;
		this.setObservation("");
		System.out.println("type en VerificationBean " + type);
	}
	
	/**
	 * 
	 */
	@PostConstruct
	public void init() {
		this.setType(1L);
		System.out.println("type en init " + type);
		codeTypeList = new ArrayList<SelectItem>();
		for(TbCodeType c : userEJB.getCodeTypes()) {
			codeTypeList.add(new SelectItem(c.getCodeTypeId(), c.getCodeTypeDescription()));
		}
	}
	
	/**
	 * @param type the type to set
	 */
	public void setType(Long type) {
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public Long getType() {
		return type;
	}
	
	/**
	 * Searches process track info of a client.
	 * Modificación realizada para que el filtro se realice por medio de
	 * la selección de tipo de documento y número de documento por separado.
	 * Por Andrés Tejedor
	 */
	
	public String search() {
		this.setType(1L);
		this.setShowData(false);
		
		if(userCode!=""){
			client = userEJB.getUserByCode(userCode, codeTypeId);
			if(client!=null){
				if(role.isClient(client.getUserId())){
					if(client.getUserState().getUserStateId()==4){
						System.out.println("Entre a estado 4");
						this.setShowData(true);
						this.setShowModal(false);
					}else if(client.getUserState().getUserStateId()==-1){
						this.setShowModal(true);
						this.setModalMessage("Error: El cliente no ha subido documentos para verificar.");
					}
					else{
						this.setShowModal(true);
						this.setModalMessage("Error: Al cliente ya se le verificaron los documentos.");
					}
				}
				else{
					this.setShowModal(true);
					this.setModalMessage("Error: Este código y tipo de documento no corresponden a un cliente por lo tanto no se le validan documentos.");
				}
			 }
			 else {
				this.setShowModal(true);
				this.setModalMessage("Error: El tipo de documento con el cliente digitado no existe en el sistema.");	
			 }
		}
		else{
			this.setShowModal(true);
			this.setModalMessage("Error: Debe digitar el número de identificación del cliente.");
			this.setShowData(false);
		}
	
		System.out.println("userCode " + userCode);
		System.out.println("client " + client);
		System.out.println("type en search" + type);
		return null;
	}	
	
	public void showObservation(){
		System.out.println("type en showObservation " + type);
		if(type==1L){
			this.setShowObs(false);
		}
		else if(type==2L){
			this.setShowObs(true);
		}
		System.out.println("showObs en showObservation " + showObs);
	}
	
	/**
	 * Saves the transaction.
	 */
	public String save(){
		System.out.println("type en save " + type);
		if(type==null){
			setModalMessage("Seleccione si la verificación fue satisfactoria o no.");
			setShowModal(true);
		} else {
			boolean isVerificationOk;
			System.out.println("type " + type);
			if(type==2L){
				System.out.println("observacion "+ observation);
				//this.setShowObs(true);
				if(observation==""){
					System.out.println("observacion es null");
					setModalMessage("Error: Digite las Observaciones.");
					setShowModal(true);
					setShowData(false);
					return null;
				}
				isVerificationOk = false;
			}else{
				isVerificationOk = true;
				System.out.println(this.client.getUserNames());

				//this.client.setUserState(userEJB.getState(3));

			}
			
			if (process.saveVerification(client.getUserId(), isVerificationOk,
					observation, SessionUtil.sessionUser().getUserId(),
					SessionUtil.ip())) {
				setModalMessage("Transacción Exitosa");
				setShowModal(true);
				setShowData(false);
				setShowMessage(false);
				setCodeClient("");
			} else {
				setModalMessage("Error en Transacción.");
				setShowModal(true);
			}
		}
		return null;
	}
	
	public String hideModal(){
		setModalMessage("");
		setShowModal(false);
		this.setObservation("");
		this.setUserCode("");
		this.setType(1L);
		return null;
	}
	
	// Getters and Setters------------//

	/**
	 * @param clients the clients to set
	 */
	public void setClients(List<TbSystemUser> clients) {
		this.clients = clients;
	}

	/**
	 * @return the clients
	 */
	public List<TbSystemUser> getClients() {
		clients = new ArrayList<TbSystemUser>();
		clients = process.getClientByProcessDetailType(ProcessTrackDetailType.CLIENT_CREATION); // (Type  = Documentation Received)
		return clients;
	}

	/**
	 * @param client the client to set
	 */
	public void setClient(TbSystemUser client) {
		this.client = client;
	}

	/**
	 * @return the client
	 */
	public TbSystemUser getClient() {
		return client;
	}

	/**
	 * @param showMessage the showMessage to set
	 */
	public void setShowMessage(boolean showMessage) {
		this.showMessage = showMessage;
	}

	/**
	 * @return the showMessage
	 */
	public boolean isShowMessage() {
		return showMessage;
	}

	/**
	 * @param showData the showData to set
	 */
	public void setShowData(boolean showData) {
		this.showData = showData;
	}

	/**
	 * @return the showData
	 */
	public boolean isShowData() {
		return showData;
	}

	/**
	 * @param codeClient the codeClient to set
	 */
	public void setCodeClient(String codeClient) {
		this.codeClient = codeClient;
	}

	/**
	 * @return the codeClient
	 */
	public String getCodeClient() {
		return codeClient;
	}

	/**
	 * @param verfOk the verfOk to set
	 */
	public void setVerfOk(boolean verfOk) {
		this.verfOk = verfOk;
	}

	/**
	 * @return the verfOk
	 */
	public boolean isVerfOk() {
		if (verfNo) {
			verfOk = false;
		}
		return verfOk;
	}

	/**
	 * @param verfNo the verfNo to set
	 */
	public void setVerfNo(boolean verfNo) {
		this.verfNo = verfNo;
	}

	/**
	 * @return the verfNo
	 */
	public boolean isVerfNo() {
		if (verfOk) {
			verfNo = false;
		}
		return verfNo;
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
	 * @param observation the observation to set
	 */
	public void setObservation(String observation) {
		this.observation = observation;
	}

	/**
	 * @return the observation
	 */
	public String getObservation() {
		return observation;
	}



	/**
	 * @param showObs the showObs to set
	 */
	public void setShowObs(boolean showObs) {
		this.showObs = showObs;
	}

	/**
	 * @return the showObs
	 */
	public boolean isShowObs() {
		return showObs;
	}
	
	/**
	 * @param codeTypeId the codeTypeId to set
	 */
	public void setCodeTypeId(Long codeTypeId) {
		this.codeTypeId = codeTypeId;
	}

	/**
	 * @return the codeTypeId
	 */
	public Long getCodeTypeId() {
		return codeTypeId;
	}
	
	/**
	 * @param codeTypeList the codeTypeList to set
	 */
	public void setCodeTypeList(List<SelectItem> codeTypeList) {
		this.codeTypeList = codeTypeList;
	}

	/**
	 * @return the codeTypeList
	 */
	public List<SelectItem> getCodeTypeList() {
		return codeTypeList;
	}
	
	/**
	 * @param userCode the userCode to set
	 */
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	/**
	 * @return the userCode
	 */
	public String getUserCode() {
		return userCode;
	}
}