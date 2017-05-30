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
 * The persistent class for the TB_PASS_QUESTION database table.
 * 
 */
@Entity
@Table(name="TB_PASS_QUESTION")

public class TbPassQuestion implements Serializable {	
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="TB_PASS_QUESTION_GENERATOR", sequenceName="TB_PASS_QUESTION_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TB_PASS_QUESTION_GENERATOR")
	@Column(name="PASS_QUESTION_ID", unique=true, nullable=false, precision=19)
	private Long passQuestionId;
	
	@Column(name="PASS_QUESTION_DESCRIPTION")
	private String passQuestDesc;
	
	@Column(name="PASS_QUESTION_DATE")
	private Timestamp passQuestDate;
	
	@ManyToOne
	@JoinColumn(name="QUESTION_TYPE_ID")
	private TbQuestionType questionTypeId;
	
	public TbPassQuestion(){
		super();
	}

	public void setPassQuestDesc(String passQuestDesc) {
		this.passQuestDesc = passQuestDesc;
	}

	public String getPassQuestDesc() {
		return passQuestDesc;
	}

	public void setPassQuestDate(Timestamp passQuestDate) {
		this.passQuestDate = passQuestDate;
	}

	public Timestamp getPassQuestDate() {
		return passQuestDate;
	}

	public void setPassQuestionId(Long passQuestionId) {
		this.passQuestionId = passQuestionId;
	}

	public Long getPassQuestionId() {
		return passQuestionId;
	}

	public void setQuestionTypeId(TbQuestionType questionTypeId) {
		this.questionTypeId = questionTypeId;
	}

	public TbQuestionType getQuestionTypeId() {
		return questionTypeId;
	}
}
