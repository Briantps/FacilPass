package mBeans;


import java.io.Serializable;
import java.math.BigDecimal;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import ejb.DataPolicies;
import ejb.User;

import security.UserLogged;
import sessionVar.SessionUtil;

/**
 * @author Jgomez
 */
public class ChangePasswordMessageBean implements Serializable{
	private static final long serialVersionUID = 1L;


	@EJB(mappedName="ejb/DataPolicies")
	private DataPolicies dataPoliciesEJB;
	
	@EJB(mappedName="ejb/User")
	private User userEJB;
	
	private UserLogged usrs;
	private Long userMaster;
	private Long typeUser;
	public Long getTypeUser() {
		return typeUser;
	}
	public void setTypeUser(Long typeUser) {
		this.typeUser = typeUser;
	}


	private BigDecimal typeUserMaster;
	private boolean modalPolitica;
	private String msnPolitica;
	private Long idPolitica;
	

	public ChangePasswordMessageBean (){
		setModalPolitica(false);
		usrs = (UserLogged) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");			
	}
	@PostConstruct
	public void init (){
	try{
		if(usrs.getUserId()!=null){
			System.out.println("Usuario: " + usrs.getUserId());
			
			setTypeUser (userEJB.getReTypeRoleNat(usrs.getUserId()));
			System.out.println("TypePersonNatu: "+ typeUser);
			Boolean userAdmin=userEJB.roleAdmin(usrs.getUserId());
			
			if (typeUser.longValue() == 2L){
				System.out.println("Usuario Persona Natual");
				
				if (dataPoliciesEJB.getNotExistsPermission(usrs.getUserId())){
					
					setIdPolitica(dataPoliciesEJB.getIdHTML(typeUser));
					System.out.println("Id Politica: " + idPolitica);
					
					if(idPolitica>0){
						
						System.out.println("El usuario: " + usrs.getUserId() + ", aun no ha aceptado las Políticas");

						setMsnPolitica(dataPoliciesEJB.getTextHTML(typeUser));
						setModalPolitica(true);
						
					}else {
						System.out.println("Aun no se ha encontrado Politica Configurada o Activa");
						if (!userAdmin.equals(true)){
							
							ExternalContext exC=FacesContext.getCurrentInstance().getExternalContext();
							try {
								exC.redirect("/web.bo.peajes.com/peajes/jsf/bankAccount/informationBalance.jspx");
							} catch (Exception e) {
								// TODO: handle exception
							}
						}
					}
				}else{
					System.out.println("El usuario: " + usrs.getUserId() + ", ya acepto las Políticas");
					if (!userAdmin.equals(true)){
						
						ExternalContext exC=FacesContext.getCurrentInstance().getExternalContext();
						try {
							exC.redirect("/web.bo.peajes.com/peajes/jsf/bankAccount/informationBalance.jspx");
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				}
			}else{
				System.out.println("Usuario No es Persona Natual " + usrs.getUserId());
				if (!userAdmin.equals(true)){
					
					ExternalContext exC=FacesContext.getCurrentInstance().getExternalContext();
					try {
						exC.redirect("/web.bo.peajes.com/peajes/jsf/bankAccount/informationBalance.jspx");
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		}else{
			System.out.println("Usuario no permitido");
		}
	} catch (NullPointerException e) {
		System.out.println("Usuario no permitido");
	}		
	}

	
	public String AceptPolicity (){
		System.out.println("Entre a AceptPolicity");
		dataPoliciesEJB.setCreatesPermission(usrs.getUserId(),idPolitica,1L, SessionUtil.ip(),true,null);
		setModalPolitica(false);
		setMsnPolitica("");
		return "informationBalanceClient";
	}	
	
	public String CancelPolicity(){
		System.out.println("Entre a CancelPolicity");
		dataPoliciesEJB.setCreatesPermission(usrs.getUserId(),idPolitica,2L, SessionUtil.ip(),true,null);
		setModalPolitica(false);
		setMsnPolitica("");
		return "informationBalanceClient";
	}

	
	public Long getUserMaster() {
		return userMaster;
	}


	public void setUserMaster(Long userMaster) {
		this.userMaster = userMaster;
	}



	public BigDecimal getTypeUserMaster() {
		return typeUserMaster;
	}


	public void setTypeUserMaster(BigDecimal typeUserMaster) {
		this.typeUserMaster = typeUserMaster;
	}


	public boolean isModalPolitica() {
		return modalPolitica;
	}


	public void setModalPolitica(boolean modalPolitica) {
		this.modalPolitica = modalPolitica;
	}


	public String getMsnPolitica() {
		return msnPolitica;
	}


	public void setMsnPolitica(String msnPolitica) {
		this.msnPolitica = msnPolitica;
	}


	public Long getIdPolitica() {
		return idPolitica;
	}


	public void setIdPolitica(Long idPolitica) {
		this.idPolitica = idPolitica;
	}
		
		

}
