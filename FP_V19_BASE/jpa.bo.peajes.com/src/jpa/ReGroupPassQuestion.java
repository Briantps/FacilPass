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
 * The persistent class for the RE_GROUP_PASS_QUESTION database table.
 * 
 */
@Entity
@Table(name="RE_GROUP_PASS_QUESTION")

public class ReGroupPassQuestion implements Serializable {	
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="RE_GROUP_PASS_QUESTION_GENERATOR", sequenceName="RE_GROUP_PASS_QUESTION_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="RE_GROUP_PASS_QUESTION_GENERATOR")
	@Column(name="GROUP_PASS_QUESTION_ID", unique=true, nullable=false, precision=19)
	private Long groupPassQuestId;
	
	@ManyToOne
	@JoinColumn(name="PASS_GROUP_ID")
	private TbPassGroup passGroupId;
	
	@ManyToOne
	@JoinColumn(name="PASS_QUESTION_ID")
	private TbPassQuestion passQuestionId;
	
	@ManyToOne
	@JoinColumn(name="PASS_STATE_ID")
	private TbPassState passStateId;
	
	@ManyToOne
	@JoinColumn(name="USER_ID ")
	private TbSystemUser userId;
	
	@Column(name="RELATION_DATE")
	private Timestamp relationDate;
	
	@Column(name="MODIFICATION_DATE")
	private Timestamp modificationDate;
	
	public ReGroupPassQuestion(){
		super();
	}

	public void setGroupPassQuestId(Long groupPassQuestId) {
		this.groupPassQuestId = groupPassQuestId;
	}

	public Long getGroupPassQuestId() {
		return groupPassQuestId;
	}

	public void setRelationDate(Timestamp relationDate) {
		this.relationDate = relationDate;
	}

	public Timestamp getRelationDate() {
		return relationDate;
	}

	public void setPassGroupId(TbPassGroup passGroupId) {
		this.passGroupId = passGroupId;
	}

	public TbPassGroup getPassGroupId() {
		return passGroupId;
	}

	public void setPassQuestionId(TbPassQuestion passQuestionId) {
		this.passQuestionId = passQuestionId;
	}

	public TbPassQuestion getPassQuestionId() {
		return passQuestionId;
	}

	public void setPassStateId(TbPassState passStateId) {
		this.passStateId = passStateId;
	}

	public TbPassState getPassStateId() {
		return passStateId;
	}

	public void setUserId(TbSystemUser userId) {
		this.userId = userId;
	}

	public TbSystemUser getUserId() {
		return userId;
	}

	public Timestamp getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(Timestamp modificationDate) {
		this.modificationDate = modificationDate;
	}


}
