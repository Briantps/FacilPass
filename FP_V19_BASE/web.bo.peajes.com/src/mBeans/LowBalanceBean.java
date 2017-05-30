package mBeans;

import java.io.Serializable;
import java.util.ArrayList;


import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;

import ejb.LowBalance;
import ejb.User;

import security.UserLogged;
import sessionVar.SessionUtil;

import util.ReAlarmBalance;
import validator.Validation;

public class LowBalanceBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@EJB(mappedName="ejb/LowBalance")
	private LowBalance lowBalanceEJB;
	
	@EJB(mappedName="ejb/User")
	private User userEJB;
	
	private ArrayList<ReAlarmBalance> accountclient;
	private ArrayList<ReAlarmBalance> Tempaccountclient;
	private ArrayList<ReAlarmBalance> TemUpdatepaccountclient;
	private UserLogged usrs;
	private String msnModal1;
	private boolean Modal1;
	private boolean ModalConfirm;
	private boolean modalList;
	private boolean modalError;
	private String msnmodalError;
	private String ip;
	private String preaccount;
    private String prevalbajo;


	
	
	/** Constructor **/
	public LowBalanceBean (){
		usrs = (UserLogged) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");	
		ip=SessionUtil.ip();
	}
	
	@PostConstruct
	public void init(){
		if (usrs.getUserId()!=null){
			System.out.println("sesion de Usuario : " + usrs.getUserId());	
			
			Long cont = lowBalanceEJB.getCountAcountUser ((userEJB.getSystemMasterById(usrs.getUserId())));
			
			if (cont == 0){
				this.setModalList(false);
				this.setMsnmodalError("Aún no tiene cuentas asociadas");
				System.out.println("Aún no tiene cuentas asociadas");
				this.setModalError(true);
			}else if (cont == -1L){
				this.setModalList(false);
				this.setMsnmodalError("Error en la Transaccion");
				System.out.println("Error en la Transaccion");
				
			}else{
				this.setModalList(true);
				accountclient = new ArrayList<ReAlarmBalance>();
				Tempaccountclient = new ArrayList<ReAlarmBalance>();
				
				accountclient = lowBalanceEJB.getAcountUser ((userEJB.getSystemMasterById(usrs.getUserId())));
				Tempaccountclient = lowBalanceEJB.getAcountUser ((userEJB.getSystemMasterById(usrs.getUserId())));
				
			}
		}			
		else{
			System.out.println("sesion de Usuario Invalida :" + usrs.getUserId());			
		}
	}
	
	public void savechanges (){
		
		
		TemUpdatepaccountclient = new ArrayList<ReAlarmBalance>();
		int negativeCount = 0;
		int positiveCount = 0;
		int notchagues = 0;
		Long valueList;
		
		if (preValidate()){
			for (int i = 0; i < accountclient.size(); i++){
				util.ReAlarmBalance value = accountclient.get(i);
				
				
				valueList = Long.parseLong(value.getValbajo().replace(".", "").replace(",", ""));
				if(valueList != Long.parseLong((Tempaccountclient.get(i).getValbajo().replace(".", "").replace(",", "")))){
					
						
						
					if (value.getAval().equals("1")){
						Long valaval;
						valaval = lowBalanceEJB.getValueAval ();
						System.out.println("-----------------> Se Recibe Cuenta AVAL 4 " + value.getAccount());
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
						System.out.println("-----------------> Se Recibe Cuenta PSE 4 " + value.getAccount());
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
						System.out.println("No se reconoce el tipo de banco para la Cuenta Facil-Pass : " + value.getAval());
					}
				}else{
					notchagues ++;
					System.out.println("No se encontraron cambios para la cueta " + value.getAccount());
					
				}
				
			}
			if (negativeCount == 0 &&  notchagues < accountclient.size() && positiveCount > 0) {
				this.msnModal1 = lowBalanceEJB.getUpdateDate(TemUpdatepaccountclient,ip,usrs.getUserId());
				this.setModal1(true);
				init();
				}else if (notchagues == accountclient.size()){
					System.out.println("No se Encontraron Cambios");
					this.setMsnModal1("No se Encontraron cambios en las Cuentas FacilPass" );
					this.setModal1(true);
				}else {
					this.setModal1(true);
				}
		}else{
			this.setModal1(true);
		}
		
		
		/*if (negativeCount == 0 && datenull!=(true) && notchagues < accountclient.size() && positiveCount > 0) {
			this.msnModal1 = lowBalanceEJB.getUpdateDate(TemUpdatepaccountclient,ip,usrs.getUserId());
			this.setModal1(true);
			init();
			}else if (negativeCount > 0 && datenull!=(true)){
				System.out.println("Los Valores Recibidos no Cumplen con la condicion minima. Verifique nuevamente");
				this.setMsnModal1("Los valores de las cuentas no cumplen con la condicion minima para ser Actualizadas. Verifique nuevamente " );
				this.setModal1(true);
				}else if (datenull == true){
					System.out.println("Se recibieron Campos Vacios. ¡Proceso detenido!");
					this.setMsnModal1("No se aceptan Valores Vacios. Verifique nuevamente" );
					this.setModal1(true);
				}else {
					System.out.println("No se Encontraron Cambios");
					this.setMsnModal1("No se Encontraron cambios en las Cuentas FacilPass" );
					this.setModal1(true);
				}*/
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
							
			}else if(!Validation.isNumeric(value.getValbajo().replace(".", "").replace(",", ""))){
				System.out.println("Entre a valor no numerico");
				setMsnModal1("El campo saldo bajo de la cuenta FacilPass " + value.getAccount() + " contiene caracteres inválidos" );
				res = false;
				break;
			}else if (value.getValbajo().length()>15){
				System.out.println("Entre a valor no numerico");
				setMsnModal1("El campo saldo bajo de la cuenta FacilPass " + value.getAccount() + " no puede ser mayor a 999.999.999.999" );
				res = false;
				break;
			}
			
		}
		return res;
	}
	
	public String confirm (){
		System.out.println("Ingrese a Confirmar");
		this.setModalConfirm (true);
		return null;
		
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
		return Modal1;
	}

	public void setModal1(boolean modal1) {
		Modal1 = modal1;
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
	

	public void cancelchanges (){
		this.setModalConfirm (false);
		this.setMsnModal1("");
		this.setModal1(false);
		init();
		
	}

	public void ocult (){
		this.setModalError(false);
		this.setModal1(false);
		this.setMsnModal1("");
		
	}
	
	public boolean isModalList() {
		return modalList;
	}

	public void setModalList(boolean modalList) {
		this.modalList = modalList;
	}

	public String getMsnmodalError() {
		return msnmodalError;
	}

	public void setMsnmodalError(String msnmodalError) {
		this.msnmodalError = msnmodalError;
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
	
}
