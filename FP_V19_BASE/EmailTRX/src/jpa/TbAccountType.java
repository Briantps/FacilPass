package jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="TB_ACCOUNT_TYPE")
public class TbAccountType implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="ACCOUNT_TYPE_ID")
	private Long accountTypeId;
	
	@Column(name="NAME_TYPE")
	private String nameType;
	
	@Column(name="MAX_DEVICE")
	private Long maxDevice ;
	
	public TbAccountType(){
		super();
	}
	
	public void setAccountTypeId(Long accountTypeId) {
		this.accountTypeId = accountTypeId;
	}
	public Long getAccountTypeId() {
		return accountTypeId;
	}
	public void setNameType(String nameType) {
		this.nameType = nameType;
	}
	public String getNameType() {
		return nameType;
	}
	public void setMaxDevice(Long maxDevice) {
		this.maxDevice = maxDevice;
	}
	public Long getMaxDevice() {
		return maxDevice;
	}

}
