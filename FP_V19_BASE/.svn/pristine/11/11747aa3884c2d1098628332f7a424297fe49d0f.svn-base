/**
 * 
 */
package jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the TB_COMPANY database table.
 * @author tmolina
 *
 */
@Entity
@Table(name = "TB_COMPANY")
public class TbCompany implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_COMPANY_COMPANYID_GENERATOR")
	@SequenceGenerator(name="TB_COMPANY_COMPANYID_GENERATOR", sequenceName = "TB_COMPANY_SEQ",allocationSize=1)
	@Column(name="COMPANY_ID")
	private Long companyId;
	
	@Column(name="COMPANY_NAME")
	private String companyName;
	
	@Column(name="COMPANY_NIT")
	private String companyNIT;
	
	@ManyToOne
	@JoinColumn(name="COMPANY_TYPE_ID")
	private TbCompanyType tbCompanyType;
	
	@Column(name="FIDEICOMISO")
	private String fideicomiso;
	
	@Column(name="NIT_FIDEICOMISO")
	private String nitFideicomiso;
	
	@Column(name="NRO_CONTRATO")
	private String nroContrato;
	
	@Column(name="DISCLAIMER")
	private String disclaimer;
	
	/**
	 * Constructor
	 */
	public TbCompany(){
		super();
	}

	/**
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	/**
	 * @return the companyId
	 */
	public Long getCompanyId() {
		return companyId;
	}

	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @param tbCompanyType the tbCompanyType to set
	 */
	public void setTbCompanyType(TbCompanyType tbCompanyType) {
		this.tbCompanyType = tbCompanyType;
	}

	/**
	 * @return the tbCompanyType
	 */
	public TbCompanyType getTbCompanyType() {
		return tbCompanyType;
	}

	/**
	 * @param fideicomiso the fideicomiso to set
	 */
	public void setFideicomiso(String fideicomiso) {
		this.fideicomiso = fideicomiso;
	}

	/**
	 * @return the fideicomiso
	 */
	public String getFideicomiso() {
		return fideicomiso;
	}

	/**
	 * @param nitFideicomiso the nitFideicomiso to set
	 */
	public void setNitFideicomiso(String nitFideicomiso) {
		this.nitFideicomiso = nitFideicomiso;
	}

	/**
	 * @return the nitFideicomiso
	 */
	public String getNitFideicomiso() {
		return nitFideicomiso;
	}

	/**
	 * @param nroContrato the nroContrato to set
	 */
	public void setNroContrato(String nroContrato) {
		this.nroContrato = nroContrato;
	}

	/**
	 * @return the nroContrato
	 */
	public String getNroContrato() {
		return nroContrato;
	}

	/**
	 * @param companyNIT the companyNIT to set
	 */
	public void setCompanyNIT(String companyNIT) {
		this.companyNIT = companyNIT;
	}

	/**
	 * @return the companyNIT
	 */
	public String getCompanyNIT() {
		return companyNIT;
	}

	/**
	 * @param disclaimer the disclaimer to set
	 */
	public void setDisclaimer(String disclaimer) {
		this.disclaimer = disclaimer;
	}

	/**
	 * @return the disclaimer
	 */
	public String getDisclaimer() {
		return disclaimer;
	}
}