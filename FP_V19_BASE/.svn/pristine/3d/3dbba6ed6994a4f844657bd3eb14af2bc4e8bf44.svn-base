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
 * The persistent class for the TB_PASS_GROUP database table.
 * 
 */
@Entity
@Table(name="TB_PASS_GROUP")

public class TbPassGroup implements Serializable {	
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="TB_PASS_GROUP_GENERATOR", sequenceName="TB_PASS_GROUP_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TB_PASS_GROUP_GENERATOR")
	@Column(name="PASS_GROUP_ID", unique=true, nullable=false, precision=19)
	private Long passGroupId;
	
	@Column(name="PASS_GROUP_NAME")
	private String passGroupName;
	
	@Column(name="PASS_GROUP_DESCRIPTION")
	private String passGroupDescription;
	
	@Column(name="PASS_GROUP_STATE")
	private String passGroupState;
	
	
	public TbPassGroup(){
		super();
	}

	public void setPassGroupId(Long passGroupId) {
		this.passGroupId = passGroupId;
	}

	public Long getPassGroupId() {
		return passGroupId;
	}

	public void setPassGroupName(String passGroupName) {
		this.passGroupName = passGroupName;
	}

	public String getPassGroupName() {
		return passGroupName;
	}

	public void setPassGroupDescription(String passGroupDescription) {
		this.passGroupDescription = passGroupDescription;
	}

	public String getPassGroupDescription() {
		return passGroupDescription;
	}

	public void setPassGroupState(String passGroupState) {
		this.passGroupState = passGroupState;
	}

	public String getPassGroupState() {
		return passGroupState;
	}

}
