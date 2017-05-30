package jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="TB_VALIDATE_DEVICE_STATE")
public class TbValidateDeviceState implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "TB_VALIDATE_DEVICE_STATE_SEQ", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "TB_VALIDATE_DEVICE_STATE_SEQ", sequenceName = "TB_VALIDATE_DEVICE_STATE_SEQ")
	
	@Column(name="VALIDATE_DEVICE_STATE_ID")
	private Long validateDeviceStateId;
	
	@Column (name="VALIDATE_DEVICE_STATE_NAME")
	private String validateDeviceStateName;
	
	@Column (name="VALIDATE_DEVICE_STATE_DESC")
	private String validateDeviceStateDesc;
		
	public TbValidateDeviceState(){
		super();
	}

	public void setValidateDeviceStateId(Long validateDeviceStateId) {
		this.validateDeviceStateId = validateDeviceStateId;
	}

	public Long getValidateDeviceStateId() {
		return validateDeviceStateId;
	}

	public void setValidateDeviceStateName(String validateDeviceStateName) {
		this.validateDeviceStateName = validateDeviceStateName;
	}

	public String getValidateDeviceStateName() {
		return validateDeviceStateName;
	}

	public void setValidateDeviceStateDesc(String validateDeviceStateDesc) {
		this.validateDeviceStateDesc = validateDeviceStateDesc;
	}

	public String getValidateDeviceStateDesc() {
		return validateDeviceStateDesc;
	}
	
}
