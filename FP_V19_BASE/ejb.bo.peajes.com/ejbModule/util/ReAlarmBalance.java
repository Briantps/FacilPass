package util;

import java.io.Serializable;

public class ReAlarmBalance implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String account;
	private String valminimo;
	private String valbajo;
	private String aval;
	private String msn;
	private String usercount;
	private String obser;
	
	public ReAlarmBalance(){
    	//super();
    }

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getValminimo() {
		return valminimo;
	}

	public void setValminimo(String valminimo) {
		this.valminimo = valminimo;
	}

	public String getValbajo() {
		return valbajo;
	}

	public void setValbajo(String valbajo) {
		this.valbajo = valbajo;
	}

	public String getAval() {
		return aval;
	}

	public void setAval(String aval) {
		this.aval = aval;
	}

	public String getMsn() {
		return msn;
	}

	public void setMsn(String msn) {
		this.msn = msn;
	}

	public String getUsercount() {
		return usercount;
	}

	public void setUsercount(String usercount) {
		this.usercount = usercount;
	}

	public String getObser() {
		return obser;
	}

	public void setObser(String obser) {
		this.obser = obser;
	}

	
}
