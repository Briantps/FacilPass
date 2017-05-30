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
 * The persistent class for the TB_PASS_STATE database table.
 * 
 */
@Entity
@Table(name="TB_PASS_STATE")

public class TbPassState implements Serializable {	
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="TB_PASS_STATE_GENERATOR", sequenceName="TB_PASS_STATE_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TB_PASS_STATE_GENERATOR")
	@Column(name="PASS_STATE_ID", unique=true, nullable=false, precision=19)
	private Long passStateId;
	
	@Column(name="PASS_STATE_DESCRIPTION")
	private String passStateDescription;
	
	@Column(name="PASS_STATE_AVAILABLE")
	private String passStateAvailable;
	
	public TbPassState(){
		super();
	}

	public void setPassStateId(Long passStateId) {
		this.passStateId = passStateId;
	}

	public Long getPassStateId() {
		return passStateId;
	}

	public void setPassStateDescription(String passStateDescription) {
		this.passStateDescription = passStateDescription;
	}

	public String getPassStateDescription() {
		return passStateDescription;
	}

	public void setPassStateAvailable(String passStateAvailable) {
		this.passStateAvailable = passStateAvailable;
	}

	public String getPassStateAvailable() {
		return passStateAvailable;
	}

}
