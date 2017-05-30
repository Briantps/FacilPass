package util;

import java.io.Serializable;

public class ReGroupPassQ implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String nameGroupQuestionU;
	private String questionU;
	private String creationDateQuestioneU;
	private String stateQuestionU;
	private String userEmailU;
	private Long groupQuestionIdU;
	private Long stateQuestionIdU;
	private Long questionIdU;
	private Long reQuestionResponseIdU;
	private Long modifiedQuestionIdU;
	
	
	public ReGroupPassQ(){
		super();
	}


	public void setNameGroupQuestionU(String nameGroupQuestionU) {
		this.nameGroupQuestionU = nameGroupQuestionU;
	}


	public String getNameGroupQuestionU() {
		return nameGroupQuestionU;
	}

	public void setCreationDateQuestioneU(String creationDateQuestioneU) {
		this.creationDateQuestioneU = creationDateQuestioneU;
	}

	public String getCreationDateQuestioneU() {
		return creationDateQuestioneU;
	}

	public void setUserEmailU(String userEmailU) {
		this.userEmailU = userEmailU;
	}

	public String getUserEmailU() {
		return userEmailU;
	}

	public void setModifiedQuestionIdU(Long modifiedQuestionIdU) {
		this.modifiedQuestionIdU = modifiedQuestionIdU;
	}


	public Long getModifiedQuestionIdU() {
		return modifiedQuestionIdU;
	}

	public void setStateQuestionIdU(Long stateQuestionIdU) {
		this.stateQuestionIdU = stateQuestionIdU;
	}


	public Long getStateQuestionIdU() {
		return stateQuestionIdU;
	}


	public void setGroupQuestionIdU(Long groupQuestionIdU) {
		this.groupQuestionIdU = groupQuestionIdU;
	}


	public Long getGroupQuestionIdU() {
		return groupQuestionIdU;
	}

	public void setStateQuestionU(String stateQuestionU) {
		this.stateQuestionU = stateQuestionU;
	}


	public String getStateQuestionU() {
		return stateQuestionU;
	}


	public void setReQuestionResponseIdU(Long reQuestionResponseIdU) {
		this.reQuestionResponseIdU = reQuestionResponseIdU;
	}

	public Long getReQuestionResponseIdU() {
		return reQuestionResponseIdU;
	}


	public void setQuestionIdU(Long questionIdU) {
		this.questionIdU = questionIdU;
	}


	public Long getQuestionIdU() {
		return questionIdU;
	}


	public void setQuestionU(String questionU) {
		this.questionU = questionU;
	}


	public String getQuestionU() {
		return questionU;
	}

}
