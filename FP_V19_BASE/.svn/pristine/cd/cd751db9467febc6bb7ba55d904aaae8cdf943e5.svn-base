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
 * The persistent class for the RE_QUESTION_RESPONSE database table.
 * 
 */

@Entity
@Table(name="RE_QUESTION_RESPONSE")
public class ReQuestionResponse  implements Serializable{


	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="RE_QUESTION_RESPONSE_GENERATOR", sequenceName="RE_QUESTION_RESPONSE_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="RE_QUESTION_RESPONSE_GENERATOR")
	@Column(name="QUESTION_RESPONSE_ID", unique=true, nullable=false, precision=19)
	private Long questionResponseId;
	
	@ManyToOne
	@JoinColumn(name="PASS_QUESTION_ID")
	private TbPassQuestion passQuestionId;
	
	@ManyToOne
	@JoinColumn(name="RESPONSE_ID")
	private TbResponse responseId;
	
	@ManyToOne
	@JoinColumn(name="USER_ID")
	private TbSystemUser userId;
	
	@Column(name="DATE_RELATION")
	private Timestamp dateRelation;
	
	@Column(name="STATE_RELATION")
	private Long stateRelation;
	
	public ReQuestionResponse(){
		super();
	}

	public void setQuestionResponseId(Long questionResponseId) {
		this.questionResponseId = questionResponseId;
	}

	public Long getQuestionResponseId() {
		return questionResponseId;
	}

	public void setDateRelation(Timestamp dateRelation) {
		this.dateRelation = dateRelation;
	}

	public Timestamp getDateRelation() {
		return dateRelation;
	}

	public void setStateRelation(Long stateRelation) {
		this.stateRelation = stateRelation;
	}

	public Long getStateRelation() {
		return stateRelation;
	}

	public void setPassQuestionId(TbPassQuestion passQuestionId) {
		this.passQuestionId = passQuestionId;
	}

	public TbPassQuestion getPassQuestionId() {
		return passQuestionId;
	}

	public void setResponseId(TbResponse responseId) {
		this.responseId = responseId;
	}

	public TbResponse getResponseId() {
		return responseId;
	}

	public void setUserId(TbSystemUser userId) {
		this.userId = userId;
	}

	public TbSystemUser getUserId() {
		return userId;
	}
	
}
