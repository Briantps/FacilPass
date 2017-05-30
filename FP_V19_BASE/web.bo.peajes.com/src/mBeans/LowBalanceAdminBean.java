package mBeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import jpa.TbCodeType;


import ejb.LowBalanceAdmin;
import ejb.User;

import security.UserLogged;
import sessionVar.SessionUtil;

import util.ReAlarmBalance;
import validator.Validation;

public class LowBalanceAdminBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@EJB(mappedName="ejb/User")
	private User userEJB;
	
	@EJB(mappedName="ejb/LowBalanceAdmin")
	private LowBalanceAdmin lowBalanceAdminEJB;
	
	private List<SelectItem> codeTypesList;
	private ArrayList<ReAlarmBalance> accountclient;
	private ArrayList<ReAlarmBalance> Tempaccountclient;
	private ArrayList<ReAlarmBalance> TemUpdatepaccountclient;
	private UserLogged usrs;
	private String msnModal1;
	private String msnmodalError;
	private boolean modal1;
	private boolean modalError;
	private boolean ModalConfirm;
	private boolean modalList;
	private boolean datatable;
	private Long codeType;
	private String ip;
	private String numberDoc;
	private String userName;
	private String secondName;
	private String userEmail;
	private String accountId;
	private String preaccount;
	private String prevalbajo;
	private String preobser;
	
	/** Constructor **/
	public LowBalanceAdminBean (){
		usrs = (UserLogged) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");	
		ip=SessionUtil.ip();
	}
	
	public List<SelectItem> getCodeTypesList() {
		if(codeTypesList == null) {
			codeTypesList = new ArrayList<SelectItem>();			
		}else{
			codeTypesList.clear();
		}
		codeTypesList.add(new SelectItem(-1L,"-- Seleccione Uno --"));
		for(TbCodeType c : userEJB.getCodeTypes()){
			codeTypesList.add(new SelectItem(c.getCodeTypeId(),c.getCodeTypeAbbreviation().toUpperCase()));
		}
		return codeTypesList;
	}
	
	public void filllist () {
		
			if (usrs.getUserId()!=null){
				if ((codeType== -1L)&&
						(numberDoc.equals("")) &&
						(userName.equals("")) &&
						(secondName.equals("")) &&
						(userEmail.equals("")) &&
						(accountId.equals(""))){
					
					this.setMsnmodalError("Debe seleccionar mínimo un filtro");
					this.setModalError(true);
					this.setModalList(false);
				}else if (validateFilters()){
					
					Long cont = lowBalanceAdminEJB.getconfirmAcountUser (usrs.getUserId(),codeType,numberDoc,userName,secondName,userEmail,accountId);
					
					if (cont == 0){
						this.setModalList(false);
						this.setMsnmodalError("No se encontraron resultados para la consulta solicitada");
						System.out.println("No se encontraron resultados para la consulta solicitada");
						this.setModalError(true);
						this.setModalList(false);
					}else if (cont == -1L){
						this.setModalList(false);
						this.setMsnmodalError("Error en la Transaccion");
						System.out.println("Error en la Transaccion");
						this.setModalError(true);
					}else if (cont > 1){
						this.setModalList(false);
						this.setMsnmodalError("Hay más de un resultado en la consulta, por favor agregue otro campo en el filtro.");
						System.out.println("Hay más de un resultado en la consulta, por favor agregue otro campo en el filtro.");
						this.setModalError(true);					
					
					}else{
						System.out.println("sesion de Usuario : " + usrs.getUserId());	
						
						accountclient = new ArrayList<ReAlarmBalance>();
						Tempaccountclient = new ArrayList<ReAlarmBalance>();
						
						accountclient = lowBalanceAdminEJB.getAcountUser (usrs.getUserId(),codeType,numberDoc,userName,secondName,userEmail,accountId);
						Tempaccountclient = lowBalanceAdminEJB.getAcountUser (usrs.getUserId(),codeType,numberDoc,userName,secondName,userEmail,accountId);
						this.setDatatable(true);
						
						
					}
					
				}else{
					this.setModalError(true);
					this.setModalList(false);
				}
			}else{
				System.out.println("sesion de Usuario Invalida :" + usrs.getUserId());			
			}
		
	}
	
	public boolean validateFilters(){
		
		if (!numberDoc.equals("") && !Validation.isNumeric(numberDoc)){
			this.setMsnmodalError("El campo No. Identificación solo permite números");
		}else if (!userName.equals("") && !Validation.isAlphaNumCompany(userName)){
			this.setMsnmodalError("El campo Nombre solo permite valores alfanumericos");
		}else if (!secondName.equals("") && !Validation.apellidoCliente(secondName)){
			this.setMsnmodalError("El campo Apellido solo permite valores de tipo alfabético");
		}else if (!userEmail.equals("") && !Validation.isEmail2(userEmail)){
			this.setMsnmodalError("El campo Usuario solo permite valores de tipo alfabético");
		}else if (!accountId.equals("") && !Validation.isNumeric(accountId)){
			this.setMsnmodalError("El campo Cuenta FacilPass solo permite valores Numéricos");
		}else {
			return true;
		}
				
		return false;
	}
	
	public void savechanges (){
		
		
		TemUpdatepaccountclient = new ArrayList<ReAlarmBalance>();
		int negativeCount = 0;
		int positiveCount = 0;
		int notchagues = 0;
		int obser = 0;
		Long valueList;
		
		if (preValidate()){
			for (int i = 0; i < accountclient.size(); i++){
				util.ReAlarmBalance value = accountclient.get(i);
				
				
				valueList = Long.parseLong(value.getValbajo().replace(".", ""));
				if(valueList != Long.parseLong((Tempaccountclient.get(i).getValbajo().replace(".", "")))){
					if (value.getObser()!=null&&!value.getObser().equals(" ")&& value.getObser().trim().replace(" ", "").length()> 0){
						System.out.println("Observacion Value: " + value.getObser());
						if (Validation.isObservationPSE(value.getObser())){
							if (value.getAval().equals("1")){
								Long valaval;
								valaval = lowBalanceAdminEJB.getValueAval ();
								System.out.println("-----------------> Se Recibe Cuenta AVAL " + value.getAccount());
								System.out.println("-----------------> Se Recibe parametro 4 " + valaval);
								System.out.println("-----------------> Se reciben paramentros para diferenciar " + valueList + " Val Anterior " + Tempaccountclient.get(i).getValbajo());
								if (valueList >= valaval){
									System.out.println("la Cuenta FacilPass del grupo Aval cumple con el parametro 4: " + value.getAccount());
									TemUpdatepaccountclient.add(accountclient.get(i));
									positiveCount ++;
								}else{
									this.setMsnModal1("El campo saldo bajo de la cuenta FacilPass " + value.getAccount() + " no puede ser inferior a $" + valaval);
									negativeCount ++;
									break;
								}
							} else if (value.getAval().equals("0")){
								System.out.println("-----------------> Se Recibe Cuenta PSE " + value.getAccount());
								System.out.println("-----------------> Se reciben paramentros para diferenciar " + valueList + " Val Anterior " + Tempaccountclient.get(i).getValbajo());
								if (valueList/2 >= Long.parseLong(value.getValminimo().replace(".", "").replace(",", ""))) {
									System.out.println("la Cuenta FacilPass del grupo PSE cumple con el doble del valor Minimo: " + value.getAccount());
									TemUpdatepaccountclient.add(accountclient.get(i));
									positiveCount ++;
								}else{
									this.setMsnModal1("El campo saldo bajo de la cuenta FacilPass " + value.getAccount() + " no puede ser inferior a $" + Long.parseLong(value.getValminimo().replace(".", "").replace(",", ""))*2);							
									negativeCount ++;
									break;
								}

							}else{
								System.out.println("No se reconoce el tipo de banco para la Cuenta FacilPass : " + value.getAval());

							}
						}else{
							System.out.println("Entre a observacion Caracteres inválidos");
							this.setMsnModal1("El campo de observación para la Cuenta FacilPass " + value.getAccount() + " contiene caracteres inválidos");
							obser = 1;
							break;
						}
					}else{						
						System.out.println("Entre a observacion Vacia");
						setMsnModal1("El campo de observación para la Cuenta FacilPass " + value.getAccount() + " No puede estar vacío");
						obser = 1;
						break;
					}

				}else{
					notchagues ++;
					System.out.println("No se encontraron cambios para la cueta " + value.getAccount());

				}

			}
			if (obser == 1){
				this.setModal1(true);
			}else if (negativeCount == 0 &&  notchagues < accountclient.size() && positiveCount > 0) {
				this.msnModal1 = lowBalanceAdminEJB.getUpdateDate(TemUpdatepaccountclient,ip,usrs.getUserId());
				this.setModal1(true);
				filllist();
				}else if (notchagues == accountclient.size()){
					System.out.println("No se Encontraron Cambios");
					this.setMsnModal1("No se encontraron cambios en las Cuentas FacilPass" );
					this.setModal1(true);
				}else {
					this.setModal1(true);
				}
		}else{
			this.setModal1(true);
		}		
	}	
	
	public boolean preValidate (){
		boolean res= true;
		for (int i = 0; i < accountclient.size(); i++){
			util.ReAlarmBalance value = accountclient.get(i);
			
			if (value.getValbajo().equals(null) || value.getValbajo().equals("")){
				System.out.println("Entre a valor nulo");
				setMsnModal1("El campo saldo bajo de la cuenta FacilPass " + value.getAccount() + " no puede estar vacío" );
				res = false;
				break;
							
			}else if(!Validation.isNumeric(value.getValbajo().replace(".", ""))){
				System.out.println("Entre a valor no numérico");
				setMsnModal1("El campo saldo bajo de la cuenta FacilPass " + value.getAccount() + " contiene caracteres inválidos" );
				res = false;
				break;
			}else if (value.getValbajo().length()>15){
				System.out.println("Entre a valor mayor a 15 caracteres");
				setMsnModal1("El campo saldo bajo de la cuenta FacilPass " + value.getAccount() + " no puede ser mayor a 999.999.999.999" );
				res = false;
				break;
			}/*else if (value.getObser().equals(null) || value.getObser().trim().length()== 0){
				System.out.println("Entre a observacion Vacia");
				setMsnModal1("El campo de observación para la Cuenta Facil-Pass " + value.getAccount() + " No puede estar vacio");
				res = false;
				break;
			
			}else if (Validation.isObservationPSE(value.getObser())){
				System.out.println("Entre a observacion Caracteres inválidos");
				this.setMsnModal1("El campo de observación para la Cuenta Facil-Pass " + value.getAccount() + " contiene caracteres inválidos");
				res = false;
				break;
			}*/
			
		}
		return res;
	}
	
	public String confirm (){
		System.out.println("Ingrese a Confirmar");
		this.setModalConfirm (true);
		return null;
		
	}
	
	
	public void ocult (){
		this.setModalError(false);
		this.setModal1(false);
		this.setMsnModal1("");
	}
	
	public void ocult2 (){
		System.out.println("Entre a Ocult 2");
		this.setModalError(false);
		this.setModal1(false);
		this.setMsnModal1("");
		this.setModalList(false);
		this.setDatatable(false);
	}
	
	public void clearFilter (){
		codeType = -1L;
		numberDoc = "";
		userName ="";
		secondName ="";
		userEmail="";
		accountId="";
		this.setDatatable(false);
	}
	
	public String hidemodal (){
		this.setModalConfirm (false);
		this.setMsnModal1("");
		this.setModal1(false);
				
		return null;
		
	}
	
	public void preload (){
		
		for (int i = 0; i < accountclient.size(); i++){
			util.ReAlarmBalance value = accountclient.get(i);
			if (value.getAccount().equals(preaccount)){
				value.setValbajo(prevalbajo);
				value.setObser(preobser);
			}
		}
	}

	public String getMsnModal1() {
		return msnModal1;
	}

	public void setMsnModal1(String msnModal1) {
		this.msnModal1 = msnModal1;
	}

	


	public boolean isModal1() {
		return modal1;
	}

	public void setModal1(boolean modal1) {
		this.modal1 = modal1;
	}

	public void setAccountclient(ArrayList<ReAlarmBalance> accountclient) {
		this.accountclient = accountclient;
	}


	public ArrayList<ReAlarmBalance> getAccountclient() {		
		return accountclient;
	}

	public ArrayList<ReAlarmBalance> getTempaccountclient() {
		return Tempaccountclient;
	}

	public void setTempaccountclient(ArrayList<ReAlarmBalance> tempaccountclient) {
		Tempaccountclient = tempaccountclient;
	}
	
	public ArrayList<ReAlarmBalance> getTemUpdatepaccountclient() {
		return TemUpdatepaccountclient;
	}

	public void setTemUpdatepaccountclient(
			ArrayList<ReAlarmBalance> temUpdatepaccountclient) {
		TemUpdatepaccountclient = temUpdatepaccountclient;
	}

	public boolean isModalConfirm() {
		return ModalConfirm;
	}

	public void setModalConfirm(boolean modalConfirm) {
		ModalConfirm = modalConfirm;
	}
	
	
	public Long getCodeType() {
		return codeType;
	}

	public void setCodeType(Long codeType) {
		this.codeType = codeType;
	}

	public String getNumberDoc() {
		return numberDoc;
	}

	public void setNumberDoc(String numberDoc) {
		this.numberDoc = numberDoc;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public void setCodeTypesList(List<SelectItem> codeTypesList) {
		this.codeTypesList = codeTypesList;
	}
	

	public boolean isDatatable() {
		return datatable;
	}

	public void setDatatable(boolean datatable) {
		this.datatable = datatable;
	}

	public void cancelchanges (){
		this.setModalConfirm (false);
		this.setMsnModal1("");
		this.setModalError(false);
		this.setModal1(false);
		filllist ();
		
	}

	

	
	public String getMsnmodalError() {
		return msnmodalError;
	}

	public void setMsnmodalError(String msnmodalError) {
		this.msnmodalError = msnmodalError;
	}

	public boolean isModalList() {
		return modalList;
	}

	public void setModalList(boolean modalList) {
		this.modalList = modalList;
	}

	public boolean isModalError() {
		return modalError;
	}

	public void setModalError(boolean modalError) {
		this.modalError = modalError;
	}

	public String getPreaccount() {
		return preaccount;
	}

	public void setPreaccount(String preaccount) {
		this.preaccount = preaccount;
	}

	public String getPrevalbajo() {
		return prevalbajo;
	}

	public void setPrevalbajo(String prevalbajo) {
		this.prevalbajo = prevalbajo;
	}

	public String getPreobser() {
		return preobser;
	}

	public void setPreobser(String preobser) {
		this.preobser = preobser;
	}
	
	
}
