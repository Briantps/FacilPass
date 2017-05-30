package mBeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import security.UserLogged;
import sessionVar.SessionUtil;

import jpa.TbBank;
import jpa.TbPaymentMethod;
import util.BankTb;
import ejb.Charges;
import ejb.User;
import ejb.crud.Bank;
import validator.Validation;

/**
 * Clase que realiza la administración de convenios
 * 
 * @author psanchez
 */
public class AgreementsManagementBean implements Serializable {	
	private static final long serialVersionUID = 1L;
		
	@EJB(mappedName="ejb/Bank")
	private Bank bankEJB;
	
	@EJB(mappedName="ejb/Charges")
	private Charges chargeEJB;
	
	@EJB(mappedName = "ejb/User")
	private User userEJB;

	private List<SelectItem> bankList;
	private Long bankId;
	
	private List<BankTb> agreementList;
	private Long idAgreementBank;
	
	private List<SelectItem> paymentMethodList;
	private Long paymentMethodId;
	
	private String codeAgreement;
	private String nameAgreement;
	private String descriptionAgreement;
    private String textRechargeValue;
	private String message;
	private Long idAgreement;
	private Long deleteAgreement;
	private Long idState;
	private Long bankIdTemp;
	private Long rechargeValue;	
	private int numero=0;

	private boolean showUpdateAgreement;
	private boolean showCreateAgreement;
	private boolean showModalValidate;
	private boolean showModalConfirmationRecharge;
	private boolean showModalConfirmationCreate;
	private boolean showModalConfirmationDelete;
	private boolean showModalConfirmationUpdate;
	private boolean showModalConfirmationUpdateEC;
	private boolean viewPanelCreateAgreement;
	private boolean permCreate;
	private boolean permUpdate;
	private boolean permRemove;
	private boolean permActivateInactivate;
	private boolean permActiveRecharge;
	private boolean disableField=false;
	private boolean active=true;

	private UserLogged usrs;
	
	public AgreementsManagementBean() {
		setUsrs((UserLogged) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user"));
	}
	
	@PostConstruct
	public void init() { 		
		permCreate = userEJB.getPermission(usrs.getUserId(),"agreementsManagementCreate");

		permUpdate = userEJB.getPermission(usrs.getUserId(),"agreementsManagementUpdate");
		
		permRemove = userEJB.getPermission(usrs.getUserId(),"agreementsManagementRemove");
		
		permActivateInactivate = userEJB.getPermission(usrs.getUserId(),"agreementsManagementActiveInactive");
		
		permActiveRecharge = userEJB.getPermission(usrs.getUserId(),"agreementsManagementMinimalAssignment");
		
		getAgreementList();
	}	
	
	/**
	 * Método confirma eliminación del convenio
	 * @author psanchez
	 */
	public void showConfirmDelete(){
		hideModalPanel();
		setShowModalConfirmationDelete(true);
		setMessage("¿Está seguro de eliminar el convenio "+nameAgreement+" con el código "+codeAgreement+"?");
	}


	/**
	 * Método elimina el convenio
	 * @author psanchez
	 */
	public void deleteAgreement() {
		hideModalPanel();
		try {
			if(bankEJB.deleteAgreement(idAgreement, idAgreementBank, SessionUtil.ip(), SessionUtil.sessionUser().getUserId())){
				setMessage("Se ha eliminado el convenio exitosamente.");
			} else {
				setMessage("Se ha presentado un error al momento de eliminar el convenio, por favor intente nuevamente.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			setMessage("Se ha presentado un error al momento de eliminar el convenio, por favor intente nuevamente.");
		}
		setShowModalValidate(true);	
	}
	
	/**
	 * Método carga formulario con datos a modificar
	 * @author psanchez
	 */
	public void panelUpdateAgreement(){
		setShowUpdateAgreement(true);
		setShowCreateAgreement(false);
		setNumero(0);
		bankIdTemp = bankId;
		for(BankTb agreement : agreementList){
			if(Long.valueOf(idAgreement)==agreement.getIdAgreement()){
				Long activeRecharge = agreement.getActiveRecharge();
				if(activeRecharge == 1L){
					setActive(true);
					setDisableField(false);
				}else{
					setActive(false);
					setDisableField(true);
				}
				break;
			}
		}
	}
	
	
	/**
	 * Método confirma cambio de estado del convenio
	 * @author psanchez
	 */
	public void updateStateAgreement(){
		hideModalPanel();
		if(idState!=1){
			setMessage("¿Está seguro de Activar el convenio "+nameAgreement+" con el código "+codeAgreement+"?"+
			           ".\n Recuerde que al realizar esta operación también se activarán las relaciones.");
		}
		else{
			setMessage("¿Está seguro de Inactivar el convenio "+nameAgreement+" con el código "+codeAgreement+"?"+
					   ".\n Recuerde que al realizar esta operación también se inactivarán las relaciones.");
		}
		setShowModalConfirmationUpdateEC(true);		
	}


	/**
	 * Método valida el convenio modificado
	 * @author psanchez
	 */
	public void validateUpdateAgreement() {
		if(validateAgreement()==false){
			setShowModalValidate(true);	
		}else if(bankEJB.existAgreement(idAgreementBank, Long.valueOf(codeAgreement), 2) != -1L) {
			Long existAgreement = bankEJB.existAgreement(idAgreementBank, Long.valueOf(codeAgreement), 2);
			if (existAgreement ==1) {
				setMessage("El código del convenio ya existe.");
				setShowModalValidate(true);
	        }else {	        	
	        	setMessage("Error al validar el convenio.");
				setShowModalValidate(true);
	        }
	     }else{
			setMessage("¿Está seguro de modificar el convenio "+nameAgreement+" con el código "+codeAgreement+"?");
			setShowModalConfirmationUpdate(true);
		}
	}
	
	/**
	 * Método modifica el convenio
	 * @author psanchez
	 */
	public void updateAgreement() {
		try {
			Long activeRecharge = null;
			if(active==false){
				activeRecharge=0L;
				rechargeValue = null;
			}
			if(active==true){
				activeRecharge=1L;
				rechargeValue = Long.parseLong(textRechargeValue.replace(".", "").replace(",", "").replace("$", ""));
			}
			if (!bankId.equals(bankIdTemp)) {//modificacion del convenio cuando la entidad es diferente a la existente en bd
				bankEJB.updateAgreement(idAgreement, idAgreementBank, Long.valueOf(codeAgreement), nameAgreement.toUpperCase().trim(), 
						paymentMethodId, bankIdTemp, descriptionAgreement.trim(), 2L, activeRecharge, 
						rechargeValue, SessionUtil.ip(), SessionUtil.sessionUser().getUserId());
				bankEJB.validateRelationAgreementBank(idAgreement, Long.valueOf(codeAgreement), bankId);
				hideModalPanel();
				setMessage("Se ha modificado el convenio exitosamente.");
				setShowModalValidate(true);	
			} else if (bankEJB.updateAgreement(idAgreement, idAgreementBank, Long.valueOf(codeAgreement), 
					nameAgreement.toUpperCase().trim(), paymentMethodId, bankId, descriptionAgreement.trim(), 
					1L, activeRecharge, rechargeValue, SessionUtil.ip(), SessionUtil.sessionUser().getUserId())){
				hideModalPanel();
				setMessage("Se ha modificado el convenio exitosamente.");
			} else {
				setMessage("Se ha presentado un error al momento de modificar el convenio, por favor intente nuevamente.");
			}
	   }  catch (Exception e) {
			e.printStackTrace();
			setMessage("Se ha presentado un error al momento de modificar el convenio, por favor intente nuevamente.");
	   }
		setShowModalValidate(true);	
	}
	
	/**
	 * Método valida el estado del convenio (activo=1, inactivo=0)
	 * @author psanchez
	 */
	public void updateEstateAgreement() {
	  try {	
			if (bankEJB.updateEstateAgreement(idAgreement, idAgreementBank, idState, SessionUtil.ip(), SessionUtil.sessionUser().getUserId())){
				setMessage("El estado del convenio ha sido modificado exitosamente.");
			} else {
				setMessage("Se ha presentado un error al momento de modificar el estado del convenio, por favor intente nuevamente.");
			}
	   }  catch (Exception e) {
			e.printStackTrace();
			setMessage("Se ha presentado un error al momento de modificar el estado del convenio, por favor intente nuevamente.");
	   }
		setShowModalValidate(true);	
	}
	
	/**
	 * Método carga formulario para crear el convenio
	 * @author psanchez
	 */
	public void panelCreateAgreement() {
		setViewPanelCreateAgreement(true);
		setShowCreateAgreement(true);
		setShowUpdateAgreement(false);
		setCodeAgreement("");
		setNameAgreement("");
		setPaymentMethodId(-1L);
		setBankId(-1L);
		setActive(true);
		setDisableField(false);
		setTextRechargeValue("");
		setDescriptionAgreement("");
		getAgreementList();
	}
	
	
	
	/**
	 * Método encargado de validar la creación del convenio
	 * @author psanchez
	 */
	public boolean validateAgreement() {
		if (codeAgreement.trim().length() == 0) {
			setMessage("El campo Código es requerido.");
			return false;
		}
		else if (nameAgreement.trim().length() == 0) {
			setMessage("El campo Nombre es requerido.");
			return false;
		}
		else if (paymentMethodId == null || paymentMethodId == -1L) {
			setMessage("El campo Modalidad es requerido.");
			return false;
		}
		else if (bankId == null || bankId == -1L) {
			setMessage("El campo Entidad es requerido.");
			return false;
		}
		else if (active==true && textRechargeValue.trim().length() == 0) {
			setMessage("El campo Recarga/asignación mínima es requerido.");
			return false;
		}
		else if (descriptionAgreement.trim().length() == 0) {
			setMessage("El campo Descripción es requerido.");
			return false;
		}
		else if (!Validation.isNumeric(codeAgreement.toString())) {
			setMessage("El campo Código contiene caracteres inválidos.");
			return false;
	    }
		else if (!Validation.nameAgreement(nameAgreement)) {
			setMessage("El campo Nombre contiene caracteres inválidos.");
			return false;
	    }
		else if (active==true && (textRechargeValue!="" && !textRechargeValue.matches("([0-9.$])+"))) {
			setMessage("El campo Recarga/asignación contiene caracteres inválidos.");
			return false;
		}
		else if(active==true && textRechargeValue!=""){
			if(Long.parseLong(textRechargeValue.replace(".", "").replace(",", "").replace("$", ""))<0){
				setMessage("El campo Recarga/asignación debe ser mayor o igual a cero $0 pesos.");
				return false;
			}
		}
		else if (!Validation.descriptionAgreement(descriptionAgreement)) {
			setMessage("El campo Descripción contiene caracteres inválidos.");
			return false;
	    }
		else if(codeAgreement.toString().length()>6){
			 setMessage("El campo Código no debe contener más de 6 caracteres.");
			 return false;
		}
		else if(nameAgreement.length()>100){
			setMessage("El campo Nombre no debe contener más de 100 caracteres.");
			return false;
		}
		else if(descriptionAgreement.length()>200){
			setMessage("El campo Descripción no debe contener más de 200 caracteres.");
			return false;
		}
		return true;
	}
	
	/**
	 * Método encargado de validar la creación del convenio
	 * @author psanchez
	 */
	public void validateCreateAgreement() {
		if(validateAgreement()==false){
			setShowModalValidate(true);	
		}else if(bankEJB.existAgreement(idAgreementBank, Long.valueOf(codeAgreement), 1) != -1L) {
			Long existAgreement = bankEJB.existAgreement(idAgreementBank, Long.valueOf(codeAgreement), 1);
			if (existAgreement ==1) {
				setMessage("El código del convenio ya existe.");
				setShowModalValidate(true);
	        }else {	        	
	        	setMessage("Error al validar el convenio.");
				setShowModalValidate(true);
	        }
		}else {
			setMessage("¿Está seguro de crear el convenio "+ nameAgreement+" con el código "+codeAgreement+"?");
			setShowModalConfirmationCreate(true);
		}
	}
	
	/**
	 * Método crea el convenio 
	 * @author psanchez
	 */
	public void insertAgreement() {
		try {
			Long activeRecharge = null;
			if(active==false){
				activeRecharge=0L;
				rechargeValue = null;
			}
			if(active==true){
				activeRecharge=1L;
				rechargeValue = Long.parseLong(textRechargeValue.replace(".", "").replace(",", "").replace("$", ""));
			}
			if (bankEJB.insertAgreement(Long.valueOf(codeAgreement), nameAgreement.trim(), paymentMethodId, descriptionAgreement.trim(), 
					bankId, activeRecharge, rechargeValue, SessionUtil.ip(), SessionUtil.sessionUser().getUserId())){
				hideModalPanel();
				setMessage("Se ha registrado el convenio exitosamente.");
			} else {
				setMessage("Se ha presentado un error al momento de registrar el convenio, por favor intente nuevamente.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			setMessage("Se ha presentado un error al momento de registrar el convenio, por favor intente nuevamente.");
		}
		setShowModalValidate(true);	
	}
	
	
	/**
	 * Método valida la asignación mínima
	 * @author psanchez
	 */
	public void validateMinimumAssignment(ValueChangeEvent event) {
		setNumero(0);

		if((Boolean) event.getNewValue()){
			setActive(true);
			setDisableField(false);
			setNumero(1);
		}else{
			setMessage("¿Está seguro que el Convenio no valide la Recarga/asignación mínima?");
			setShowModalConfirmationRecharge(true);
		}
	}
	
	public void hideModal(){
		hideModal1();
		setActive(true);
        setDisableField(false);
	}
	
	public void hideModal1(){
		setShowModalValidate(false);
		setShowModalConfirmationRecharge(false);
		setShowModalConfirmationCreate(false);
		setShowModalConfirmationDelete(false);
		setShowModalConfirmationUpdate(false);
		setShowModalConfirmationUpdateEC(false);
		setMessage(null);
	}
	
	public void hideModal2(){
		setActive(false);
        setDisableField(true);
		setTextRechargeValue("");
		setShowModalConfirmationRecharge(false);
	}
	
	public void hideModalPanel(){
		setViewPanelCreateAgreement(false);
		setShowUpdateAgreement(false);
		setShowCreateAgreement(false);
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setAgreementList(List<BankTb> agreementList) {
		this.agreementList = agreementList;
	}

	public List<BankTb> getAgreementList() {
		if(agreementList == null) {
			agreementList = new ArrayList<BankTb>();
		}else{
			agreementList.clear();
		}
		agreementList = bankEJB.getListAllAgreement();
		return agreementList;
	}

	public void setUsrs(UserLogged usrs) {
		this.usrs = usrs;
	}

	public UserLogged getUsrs() {
		return usrs;
	}

	public void setViewPanelCreateAgreement(boolean viewPanelCreateAgreement) {
		this.viewPanelCreateAgreement = viewPanelCreateAgreement;
	}

	public boolean isViewPanelCreateAgreement() {
		return viewPanelCreateAgreement;
	}

	public void setDisableField(boolean disableField) {
		this.disableField = disableField;
	}

	public boolean isDisableField() {
		return disableField;
	}

	public void setShowModalConfirmationDelete(boolean showModalConfirmationDelete) {
		this.showModalConfirmationDelete = showModalConfirmationDelete;
	}

	public boolean isShowModalConfirmationDelete() {
		return showModalConfirmationDelete;
	}

	public void setShowModalConfirmationCreate(boolean showModalConfirmationCreate) {
		this.showModalConfirmationCreate = showModalConfirmationCreate;
	}

	public boolean isShowModalConfirmationCreate() {
		return showModalConfirmationCreate;
	}

	public void setShowModalConfirmationUpdate(boolean showModalConfirmationUpdate) {
		this.showModalConfirmationUpdate = showModalConfirmationUpdate;
	}

	public boolean isShowModalConfirmationUpdate() {
		return showModalConfirmationUpdate;
	}
	
	public void setBankList(List<SelectItem> bankList) {
		this.bankList = bankList;
	}

	public List<SelectItem> getBankList() {
		if(bankList == null){
			bankList = new ArrayList<SelectItem>();
		} else {
			bankList.clear();
		}
		bankList.add(new SelectItem(-1L, "--Seleccione Uno--"));
		for(TbBank c : bankEJB.getBanks()) {
			bankList.add(new SelectItem(c.getBankId(),c.getBankName()));
		}
		return bankList;
	}

	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}

	public Long getBankId() {
		return bankId;
	}

	public void setNameAgreement(String nameAgreement) {
		this.nameAgreement = nameAgreement;
	}

	public String getNameAgreement() {
		return nameAgreement;
	}

	public void setDescriptionAgreement(String descriptionAgreement) {
		this.descriptionAgreement = descriptionAgreement;
	}

	public String getDescriptionAgreement() {
		return descriptionAgreement;
	}

	public void setShowUpdateAgreement(boolean showUpdateAgreement) {
		this.showUpdateAgreement = showUpdateAgreement;
	}

	public boolean isShowUpdateAgreement() {
		return showUpdateAgreement;
	}

	public void setShowCreateAgreement(boolean showCreateAgreement) {
		this.showCreateAgreement = showCreateAgreement;
	}

	public boolean isShowCreateAgreement() {
		if(permCreate==true && showUpdateAgreement==false){
			return showCreateAgreement=true;
		}
		return showCreateAgreement;
	}

	public void setIdAgreementBank(Long idAgreementBank) {
		this.idAgreementBank = idAgreementBank;
	}

	public Long getIdAgreementBank() {
		return idAgreementBank;
	}

	public void setCodeAgreement(String codeAgreement) {
		this.codeAgreement = codeAgreement;
	}

	public String getCodeAgreement() {
		return codeAgreement;
	}

	public void setIdState(Long idState) {
		this.idState = idState;
	}

	public Long getIdState() {
		return idState;
	}

	public void setIdAgreement(Long idAgreement) {
		this.idAgreement = idAgreement;
	}

	public Long getIdAgreement() {
		return idAgreement;
	}

	public void setShowModalConfirmationUpdateEC(boolean showModalConfirmationUpdateEC) {
		this.showModalConfirmationUpdateEC = showModalConfirmationUpdateEC;
	}

	public boolean isShowModalConfirmationUpdateEC() {
		return showModalConfirmationUpdateEC;
	}

	public void setPaymentMethodList(List<SelectItem> paymentMethodList) {
		this.paymentMethodList = paymentMethodList;
	}

	public List<SelectItem> getPaymentMethodList() {
		if(paymentMethodList == null){
			paymentMethodList = new ArrayList<SelectItem>();
		} else {
			paymentMethodList.clear();
		}
		paymentMethodList.add(new SelectItem(-1L, "--Seleccione Uno--"));
		for(TbPaymentMethod c : chargeEJB.getPaymentMethod()) {
			paymentMethodList.add(new SelectItem(c.getPaymentMethodId(), c.getPaymentMethod()));
		}
		return paymentMethodList;
	}

	public void setPaymentMethodId(Long paymentMethodId) {
		this.paymentMethodId = paymentMethodId;
	}

	public Long getPaymentMethodId() {
		return paymentMethodId;
	}

	public void setPermCreate(boolean permCreate) {
		this.permCreate = permCreate;
	}

	public boolean isPermCreate() {
		return permCreate;
	}

	public void setPermRemove(boolean permRemove) {
		this.permRemove = permRemove;
	}

	public boolean isPermRemove() {
		return permRemove;
	}

	public void setPermActivateInactivate(boolean permActivateInactivate) {
		this.permActivateInactivate = permActivateInactivate;
	}

	public boolean isPermActivateInactivate() {
		return permActivateInactivate;
	}

	public void setPermUpdate(boolean permUpdate) {
		this.permUpdate = permUpdate;
	}

	public boolean isPermUpdate() {
		return permUpdate;
	}
	
	public void setPermActiveRecharge(boolean permActiveRecharge) {
		this.permActiveRecharge = permActiveRecharge;
	}

	public boolean isPermActiveRecharge() {
		return permActiveRecharge;
	}

	public void setDeleteAgreement(Long deleteAgreement) {
		this.deleteAgreement = deleteAgreement;
	}

	public Long getDeleteAgreement() {
		return deleteAgreement;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isActive() {
		return active;
	}

	public void setRechargeValue(Long rechargeValue) {
		this.rechargeValue = rechargeValue;
	}

	public Long getRechargeValue() {
		return rechargeValue;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public int getNumero() {
		return numero;
	}

	public void setTextRechargeValue(String textRechargeValue) {
		this.textRechargeValue = textRechargeValue;
	}

	public String getTextRechargeValue() {
		return textRechargeValue;
	}

	public void setShowModalConfirmationRecharge(boolean showModalConfirmationRecharge) {
		this.showModalConfirmationRecharge = showModalConfirmationRecharge;
	}

	public boolean isShowModalConfirmationRecharge() {
		return showModalConfirmationRecharge;
	}

	public void setUserEJB(User userEJB) {
		this.userEJB = userEJB;
	}

	public User getUserEJB() {
		return userEJB;
	}

	public void setShowModalValidate(boolean showModalValidate) {
		this.showModalValidate = showModalValidate;
	}

	public boolean isShowModalValidate() {
		return showModalValidate;
	}

	
}