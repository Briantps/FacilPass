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
 * The persistent class for the TB_QUESTION_TYPE database table.
 * 
 */

@Entity
@Table(name="TB_QUESTION_TYPE")
public class TbQuestionType implements Serializable {


	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="TB_QUESTION_TYPE_GENERATOR", sequenceName="TB_QUESTION_TYPE_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TB_QUESTION_TYPE_GENERATOR")
	@Column(name="QUESTION_TYPE_ID", unique=true, nullable=false, precision=19)
	private Long questionTypeId;
	
	@Column(name="QUESTION_TYPE_DESCRIPTION")
	private String questionTypeDescription;
	
	public TbQuestionType(){
		super();
	}

	public void setQuestionTypeId(Long questionTypeId) {
		this.questionTypeId = questionTypeId;
	}

	public Long getQuestionTypeId() {
		return questionTypeId;
	}

	public void setQuestionTypeDescription(String questionTypeDescription) {
		this.questionTypeDescription = questionTypeDescription;
	}

	public String getQuestionTypeDescription() {
		return questionTypeDescription;
	}

}
