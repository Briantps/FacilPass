package mBeans;

import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;

import security.UserLogged;
import sessionVar.SessionUtil;
import util.BalanceAccount;

import ejb.InformationBalanceAccountI;
import ejb.LowBalance;
import ejb.User;

public class InformationBalanceAccontBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB(mappedName = "ejb/User")
	private User userEJB;

	@EJB(mappedName = "ejb/InformationBalanceAccountI")
	private InformationBalanceAccountI informationBalanceAccountEJB;

	private ArrayList<BalanceAccount> listBalanceAccount;

	public String ip;
	private UserLogged usrs;
	private String txtHtml="";
	private String nameNure="";

	public InformationBalanceAccontBean() {
		usrs = (UserLogged) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("user");
		ip = SessionUtil.ip();
	}

	@PostConstruct
	public void init() {
		// cargar infiormación
		if (usrs.getUserId() != null) {
			System.out.println("Entro!!!!!!!!!!!!!!!!!!!!!!");
			nameNure=informationBalanceAccountEJB.getNameNure();
			txtHtml=informationBalanceAccountEJB.getHtmlEditor();
			listBalanceAccount = new ArrayList<BalanceAccount>();
			listBalanceAccount = informationBalanceAccountEJB
					.getInformationBalanceAccount(userEJB.getSystemMasterById(usrs.getUserId()));
		
			System.out.print("usuario::::: "+userEJB.getSystemMasterById(usrs.getUserId()));
		}

	}

	public ArrayList<BalanceAccount> getListBalanceAccount() {
		return listBalanceAccount;
	}

	public void setListBalanceAccount(ArrayList<BalanceAccount> listBalanceAccount) {
		this.listBalanceAccount = listBalanceAccount;
	}

	public String getTxtHtml() {
		return txtHtml;
	}

	public void setTxtHtml(String txtHtml) {
		this.txtHtml = txtHtml;
	}

	public String getNameNure() {
		return nameNure;
	}

	public void setNameNure(String nameNure) {
		this.nameNure = nameNure;
	}
	
	

}
