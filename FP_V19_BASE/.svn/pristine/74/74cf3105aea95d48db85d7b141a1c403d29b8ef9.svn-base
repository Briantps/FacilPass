package crud;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import ejb.crud.Bank;

import sessionVar.SessionUtil;
import util.BankTb;


/**
 * Bean to Manage bank register.
 * @author psanchez
 */
public class BankBean implements Serializable {
	private static final long serialVersionUID = -7148175549095352008L;
	
	@EJB(mappedName="ejb/Bank")
	private Bank bankEJB;

	// Attributes
	private List<BankTb> bankList;
	
	private String bankId;
	private String bankName;
	private String bankLetter;
	private Long bankAval;
	private Long bankAvalCheck;
	private boolean active;
	
	/** Control Modal **/
	private boolean showModalValidateI;
	private boolean showModalValidateE;
	private boolean showConfirmationE;
	private boolean showConfirmationI;
    private boolean showModal;
	private boolean showInsert;
	private boolean showEdit;

	private String modalMessage;

	/**
	 * Constructor.
	 */
	public BankBean() {
		super();
	}
	
	@PostConstruct
	public void init(){
		this.getBankList();
	}
		
	// Actions ------------ //
	
	public void initAddBank(){
		this.setShowModalValidateI(false);
		this.setShowModalValidateE(false);
		this.setShowConfirmationI(false);
		this.setShowConfirmationE(false);
		this.setShowModal(false);
		this.setShowInsert(true);
		this.setShowEdit(false);
		this.setBankId(null);
		this.setBankName(null);
		this.setBankLetter(null);
		this.setActive(false);
	}
	
	
	/**
	 * Método encargado de validar si existe el banco
	 * @author psanchez
	 */
	public void validationAddBank(){
	  this.hideModal();
	  try{
		  if(postValidation()==true){
		      String result = bankEJB.existBank(Long.parseLong(bankId), bankName, bankLetter, 1);
			  if(result != null ){
				this.setModalMessage(result);
				this.setShowModalValidateI(true);
			  }else {
				this.setShowModalValidateI(false);
				this.setShowConfirmationI(true);
				this.setModalMessage("¿Está seguro de crear la entidad?");
			   }
		   }	
		}catch(Exception e){
			this.setModalMessage("El campo código de entidad no puede estar vacío.");
			this.setShowModalValidateI(true);
		}
	  }
	 	
	/**
	 * Método encargado de crear banco
	 * @author psanchez
	 */
	public void insertBank(){
	   this.hideModal();
	   try{
		   if(active==true){
			   bankAval=1L;
			}else{
			   bankAval=0L;
			}
			String result =bankEJB.insertBank(Long.parseLong(bankId), bankName, bankLetter, bankAval, SessionUtil.ip(), SessionUtil.sessionUser().getUserId());
			if(result != null ){
				this.setModalMessage(result);
			} else {
				this.setModalMessage("Error en transacción.");
			}
			this.setShowModal(true);
	   }catch(Exception e){
		   System.out.println(" [ Error en BankBean.insertBank ] " +e.getMessage());
	   }
     } 
	/**
	 * Init Edition of initEditBank.
	 */
	public void initEditBank(){
		for(BankTb bank : bankList){
			if(Long.parseLong(bankId)==bank.getBankId()){
				bankName = bank.getBankName();	
				bankLetter = bank.getBankCode();
				bankAval = bank.getBankAval();
				bankAvalCheck = bank.getBankAvalCheck();
				
				if(bankAval == 1L){
					active = true;
				}else{
					active = false;
				}
				break;
			}
		}
		this.setShowEdit(true);
	}
   
	
	/**
	 * Método encargado de validar el nombre del banco y existencia
	 * @author psanchez
	 */
	 public void validationEditBank(){
		this.hideModal();
		try{
			  if(bankName.equals(null) || bankName.equals("")){
				this.setModalMessage("Ingrese el nombre de la entidad.");
	 			this.setShowModalValidateE(true);
	 		  }else if(bankName.length()> 50L){
				this.setModalMessage("El tamaño de el nombre de la entidad debe ser máximo 50 caracteres. Verifique.");
 			 	this.setShowModalValidateE(true);
			  }else if(bankName!="" && (!bankName.matches("([a-z]|[A-Z]|[áéíóúñ]|[ÁÉÍÓÚÑ]|[-]|\\s)+"))){
				this.setModalMessage("El campo nombre de la entidad contiene caracteres inválidos.");
				this.setShowModalValidateE(true);
			  }else {
				String result = bankEJB.existBank(Long.parseLong(bankId), bankName, bankLetter, 2);
			    if(result != null ){
					this.setModalMessage(result);
					this.setShowModalValidateE(true);
			    } else {
				    if(active==true){
				    	bankAval=1L;
					}else{
						bankAval=0L;
					}
				    this.setShowModalValidateE(false);
					this.setShowConfirmationE(true);
					this.setModalMessage("¿Está seguro de realizar cambios?");
				}
		     }
		 }catch(Exception e){
				System.out.println(" [ Error en BankBean.validationEditBank ] "+e.getMessage());
		 }		
	 }
	
	/**
	 * Método encargado de editar el banco.
	 * @author psanchez
	 */
	 public void editBank(){
	   this.hideModal();
	   try{
		   if(active==true){
			  bankAval=1L;
			}else{
			  bankAval=0L;
			}
			String result =bankEJB.editBank(Long.parseLong(bankId), bankName, bankAval, SessionUtil.ip(), SessionUtil.sessionUser().getUserId());
			if(result != null ){
				this.setModalMessage(result);
			} else {
				this.setModalMessage("Error en transacción.");
			}
			this.setShowModal(true);
	   }catch(Exception e){
		   System.out.println(" [ Error en BankBean.editBank] " +e.getMessage());
	   }
	 } 
	 
	/**
	 * Método encargado de validar los campos código, letra y nombre al crear un banco
	 * @author psanchez
	 */	
	private boolean postValidation(){
		if(bankId.equals(null) || bankId.equals("")){
	    	this.setModalMessage("El campo código de la entidad no puede estar vacío.");
 			this.setShowModalValidateI(true);
 			return false;
 		}
		else if(!bankId.matches("([0-9])+")){
			this.setModalMessage("El campo código de la entidad contiene caracteres inválidos.");
			this.setShowModalValidateI(true);
			return false;
		}
		else if(bankId.substring(0,1).equals("0")){
			this.setModalMessage("El campo código de la entidad no debe iniciar con cero (0).");
			this.setShowModalValidateI(true);
			return false;
		}
		else if(bankId.length()> 3L){
			this.setModalMessage("El tamaño código de la entidad debe ser máximo 3 dígitos.");
		  	this.setShowModalValidateI(true);
		  	return false;
	    }
		else if(bankLetter.equals(null) || bankLetter.equals("")){
	    	this.setModalMessage("El campo letra de la entidad no puede estar vacío.");
	    	this.setShowModalValidateI(true);
	    	return false;
	    }
		else if(bankLetter.length()> 1L){
	    	this.setModalMessage("La letra de la entidad debe ser de 1 caracter.");
	    	this.setShowModalValidateI(true);
	    	return false;
        }
		else if(!bankLetter.toUpperCase().matches("([A-ZÑ]|\\s)+")){
			this.setModalMessage("El campo letra de la entidad contiene caracteres inválidos.");
			this.setShowModalValidateI(true);
			return false;
        }
		else if(bankName.equals(null) || bankName.equals("")){
	    	this.setModalMessage("El campo nombre de la entidad no puede estar vacío.");
	    	this.setShowModalValidateI(true);
	    	return false;
        }
		else if(!bankName.matches("([a-z]|[A-Z]|[áéíóúñ]|[ÁÉÍÓÚÑ]|[-]|\\s)+")){
			this.setModalMessage("El campo nombre de la entidad contiene caracteres inválidos.");
			this.setShowModalValidateI(true);
			return false;
		    }
		else if(bankName.length()> 50L){
	    	this.setModalMessage("El tamaño del nombre de la entidad debe ser máximo 50 caracteres.");
	    	this.setShowModalValidateI(true);
	    	return false;
	    }
		return true;		
	}
	
	/**
	 * Hides modal windows.
	 */
	public void hideModal2(){
		this.setShowModalValidateI(false);
		this.setShowModalValidateE(false);
		this.setShowModal(false);
		this.setShowInsert(false);
		this.setShowEdit(true);
	}
	
	public void hideModal() {
		this.setShowModalValidateI(false);
		this.setShowModalValidateE(false);
		this.setShowConfirmationI(false);
		this.setShowConfirmationE(false);
		this.setShowModal(false);
		this.setShowInsert(false);
		this.setShowEdit(false);
	    this.setModalMessage(null);
	}
	
	
	public void hideModal3(){
		this.setShowModalValidateI(false);
		this.setShowModalValidateE(false);
		this.setShowModal(false);
		this.setShowInsert(true);
		this.setShowEdit(false);
	}

		
	// Getter and Setter ----------------- // 

	/**
	 * @param showModalValidateI the showModalValidateI to set
	 */
	public void setShowModalValidateI(boolean showModalValidateI) {
		this.showModalValidateI = showModalValidateI;
	}

	/**
	 * @return the showModalValidateI
	 */
	public boolean isShowModalValidateI() {
		return showModalValidateI;
	}

	/**
	 * @param showInsert the showInsert to set
	 */
	public void setShowInsert(boolean showInsert) {
		this.showInsert = showInsert;
	}

	/**
	 * @return the showInsert
	 */
	public boolean isShowInsert() {
		return showInsert;
	}

	/**
	 * @param showModalValidateE the showModalValidateE to set
	 */
	public void setShowModalValidateE(boolean showModalValidateE) {
		this.showModalValidateE = showModalValidateE;
	}

	/**
	 * @return the showModalValidateE
	 */
	public boolean isShowModalValidateE() {
		return showModalValidateE;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankName() {
		return bankName;
	}
	
	public void setBankLetter(String bankLetter) {
		this.bankLetter = bankLetter;
	}

	public String getBankLetter() {
		return bankLetter;
	}

	/**
	 * @param bankList the bankList to set
	 */
	public void setBankList(List<BankTb> bankList) {
		this.bankList = bankList;
	}

	/**
	 * @return the bankList
	 */
	public List<BankTb> getBankList() {
		if(bankList == null) {
			bankList = new ArrayList<BankTb>();
		}else{
			bankList.clear();
		}
		bankList = bankEJB.getBankTb();
	  return bankList;
	}

	/**
	 * @param showEdit the showEdit to set
	 */
	public void setShowEdit(boolean showEdit) {
		this.showEdit = showEdit;
	}

	/**
	 * @return the showEdit
	 */
	public boolean isShowEdit() {
		return showEdit;
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

	public void setShowModal(boolean showModal) {
		this.showModal = showModal;
	}

	public boolean isShowModal() {
		return showModal;
	}

	/**
	 * @param bankEJB the bankEJB to set
	 */
	public void setBankEJB(Bank bankEJB) {
		this.bankEJB = bankEJB;
	}

	/**
	 * @return the bankEJB
	 */
	public Bank getBankEJB() {
		return bankEJB;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	public void setShowConfirmationE(boolean showConfirmationE) {
		this.showConfirmationE = showConfirmationE;
	}

	public boolean isShowConfirmationE() {
		return showConfirmationE;
	}

	public void setShowConfirmationI(boolean showConfirmationI) {
		this.showConfirmationI = showConfirmationI;
	}

	public boolean isShowConfirmationI() {
		return showConfirmationI;
	}

	public void setBankAval(Long bankAval) {
		this.bankAval = bankAval;
	}

	public Long getBankAval() {
		return bankAval;
	}

	public void setBankAvalCheck(Long bankAvalCheck) {
		this.bankAvalCheck = bankAvalCheck;
	}

	public Long getBankAvalCheck() {
		return bankAvalCheck;
	}


}