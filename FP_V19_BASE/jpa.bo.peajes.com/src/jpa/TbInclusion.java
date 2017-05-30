/**
 * 
 */
package jpa;

import java.io.Serializable;
import java.sql.Timestamp;

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
 * The persistent class for the TB_INCLUSION database table.
 * @author tmolina
 */
 @Entity
 @Table(name="TB_INCLUSION")
public class TbInclusion implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_INCLUSION_INCLUSIONID_GENERATOR")
	@SequenceGenerator(name="TB_INCLUSION_INCLUSIONID_GENERATOR",  sequenceName = "TB_INCLUSION_SEQ",allocationSize=1)
	@Column(name="INCLUSION_ID")
	private Long inclusionId;
	
	@Column(name="INCLUSION_DATE")
	private Timestamp inclusionDate;
	
	@ManyToOne
	@JoinColumn(name="INCLUSION_STATE_ID")
	private TbInclusionState tbInclusionState;
	
	@Column(name="INCLUSION_NUMBER")
	private Long inclusionNumber;
	
	 @ManyToOne
	 @JoinColumn(name="INCLUSION_CREATION_USER")
	 private TbSystemUser creationUser;
	 
	 @Column(name="INCLUSION_UPDATE_DATE")
	 private Timestamp inclusionUpdateDate;
	 
	 @ManyToOne
	 @JoinColumn(name="INCLUSION_UPDATE_USER")
	 private TbSystemUser updateUser;
	
	/**
	 * Constructor.
	 */
	public TbInclusion(){
		super();
	}

	/**
	 * @param inclusionId the inclusionId to set
	 */
	public void setInclusionId(Long inclusionId) {
		this.inclusionId = inclusionId;
	}

	/**
	 * @return the inclusionId
	 */
	public Long getInclusionId() {
		return inclusionId;
	}

	/**
	 * @param inclusionDate the inclusionDate to set
	 */
	public void setInclusionDate(Timestamp inclusionDate) {
		this.inclusionDate = inclusionDate;
	}

	/**
	 * @return the inclusionDate
	 */
	public Timestamp getInclusionDate() {
		return inclusionDate;
	}

	/**
	 * @param tbInclusionState the tbInclusionState to set
	 */
	public void setTbInclusionState(TbInclusionState tbInclusionState) {
		this.tbInclusionState = tbInclusionState;
	}

	/**
	 * @return the tbInclusionState
	 */
	public TbInclusionState getTbInclusionState() {
		return tbInclusionState;
	}

	/**
	 * @param inclusionNumber the inclusionNumber to set
	 */
	public void setInclusionNumber(Long inclusionNumber) {
		this.inclusionNumber = inclusionNumber;
	}

	/**
	 * @return the inclusionNumber
	 */
	public Long getInclusionNumber() {
		return inclusionNumber;
	}

	/**
	 * @param creationUser the creationUser to set
	 */
	public void setCreationUser(TbSystemUser creationUser) {
		this.creationUser = creationUser;
	}

	/**
	 * @return the creationUser
	 */
	public TbSystemUser getCreationUser() {
		return creationUser;
	}

	/**
	 * @param inclusionUpdateDate the inclusionUpdateDate to set
	 */
	public void setInclusionUpdateDate(Timestamp inclusionUpdateDate) {
		this.inclusionUpdateDate = inclusionUpdateDate;
	}

	/**
	 * @return the inclusionUpdateDate
	 */
	public Timestamp getInclusionUpdateDate() {
		return inclusionUpdateDate;
	}

	/**
	 * @param updateUser the updateUser to set
	 */
	public void setUpdateUser(TbSystemUser updateUser) {
		this.updateUser = updateUser;
	}

	/**
	 * @return the updateUser
	 */
	public TbSystemUser getUpdateUser() {
		return updateUser;
	}
}