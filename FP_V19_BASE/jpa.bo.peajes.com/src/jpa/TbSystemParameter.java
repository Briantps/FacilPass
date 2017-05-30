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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the TB_SYSTEM_PARAMETER database table.
 * @author tmolina
 *
 */
@Entity
@Table(name="TB_SYSTEM_PARAMETER")
public class TbSystemParameter implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator= "TBSYSTEMPARAMETER_SYSTEMPARAMETERID_GENERATOR")
	@SequenceGenerator(name="TBSYSTEMPARAMETER_SYSTEMPARAMETERID_GENERATOR", sequenceName="TB_SYSTEM_PARAMETER_SEQ",allocationSize=1)
	@Column(name="SYSTEM_PARAMETER_ID")
	private Long systemParameterId;
	
	@Column(name="SYSTEM_PARAMETER_NAME")
	private String systemParameterName;
	
	@Column(name="SYSTEM_PARAMETER_VALUE")
	private String systemParameterValue;
	
	@Column(name="SYSTEM_PARAMETER_DESCRIPTION")
	private String systemParameterDescription;
	
	@Column(name="CLASIFICATION")
	private String clasification;

	/**
	 * Constructor.
	 */
	public TbSystemParameter() {
		super();
	}

	/**
	 * @param systemParameterId the systemParameterId to set
	 */
	public void setSystemParameterId(Long systemParameterId) {
		this.systemParameterId = systemParameterId;
	}

	/**
	 * @return the systemParameterId
	 */
	public Long getSystemParameterId() {
		return systemParameterId;
	}

	/**
	 * @param systemParameterName the systemParameterName to set
	 */
	public void setSystemParameterName(String systemParameterName) {
		this.systemParameterName = systemParameterName;
	}

	/**
	 * @return the systemParameterName
	 */
	public String getSystemParameterName() {
		return systemParameterName;
	}

	/**
	 * @param systemParameterValue the systemParameterValue to set
	 */
	public void setSystemParameterValue(String systemParameterValue) {
		this.systemParameterValue = systemParameterValue;
	}

	/**
	 * @return the systemParameterValue
	 */
	public String getSystemParameterValue() {
		return systemParameterValue;
	}

	/**
	 * @param systemParameterDescription the systemParameterDescription to set
	 */
	public void setSystemParameterDescription(String systemParameterDescription) {
		this.systemParameterDescription = systemParameterDescription;
	}

	/**
	 * @return the systemParameterDescription
	 */
	public String getSystemParameterDescription() {
		return systemParameterDescription;
	}

	/**
	 * @param clasification the clasification to set
	 */
	public void setClasification(String clasification) {
		this.clasification = clasification;
	}

	/**
	 * @return the clasification
	 */
	public String getClasification() {
		return clasification;
	}
}