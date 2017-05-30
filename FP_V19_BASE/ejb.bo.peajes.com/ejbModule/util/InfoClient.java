package util;

import java.io.Serializable;


public class InfoClient implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long idClient;
	
	private String type="";
	
	private String cod="";
	
	private String nom="";
	
	private String ape="";
	
	private Long cuenta=0L;
	
	
	public InfoClient(){

	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param cod the cod to set
	 */
	public void setCod(String cod) {
		this.cod = cod;
	}

	/**
	 * @return the cod
	 */
	public String getCod() {
		return cod;
	}

	/**
	 * @param nom the nom to set
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * @param ape the ape to set
	 */
	public void setApe(String ape) {
		this.ape = ape;
	}

	/**
	 * @return the ape
	 */
	public String getApe() {
		return ape;
	}

	/**
	 * @param cuenta the cuenta to set
	 */
	public void setCuenta(Long cuenta) {
		this.cuenta = cuenta;
	}

	/**
	 * @return the cuenta
	 */
	public Long getCuenta() {
		return cuenta;
	}


	/**
	 * @param idClient the idClient to set
	 */
	public void setIdClient(Long idClient) {
		this.idClient = idClient;
	}

	/**
	 * @return the idClient
	 */
	public Long getIdClient() {
		return idClient;
	}	
}
