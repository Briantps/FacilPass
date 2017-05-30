package jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="TB_CHANEL")
public class TbChanel implements Serializable{


	private static final long serialVersionUID = 1L;
	@Id
	@Column (name="CHANEL_ID", unique=true, nullable=false)
	private Long chanelId;
	
	@Column(name = "CHANEL_TYPE")
	private String chanelType;
	
	@Column(name = "CHANEL_STATE")
	private Long chanelState;
	
	@Column(name = "CHANEL_DESCRIPTION")
	private String chanelDescription;
	
	public TbChanel(){
		super();
	}

	public void setChanelId(Long chanelId) {
		this.chanelId = chanelId;
	}

	public Long getChanelId() {
		return chanelId;
	}

	public void setChanelType(String chanelType) {
		this.chanelType = chanelType;
	}

	public String getChanelType() {
		return chanelType;
	}

	public void setChanelState(Long chanelState) {
		this.chanelState = chanelState;
	}

	public Long getChanelState() {
		return chanelState;
	}

	public void setChanelDescription(String chanelDescription) {
		this.chanelDescription = chanelDescription;
	}

	public String getChanelDescription() {
		return chanelDescription;
	}
	
	


}
