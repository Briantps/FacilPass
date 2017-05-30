package jpa;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="TB_PASSWORD_DICTIONARY")
public class TbPasswordDictionary implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="SEQ_PASSWORD_DICTIONARY_GENERATOR")
    @SequenceGenerator(name="SEQ_PASSWORD_DICTIONARY_GENERATOR", sequenceName="seq_password_dictionary",allocationSize=1)
	@Column(name="PASSWORD_DICTIONAY_ID")
	private Long passwordDictionaryId;
	
	@Column(name="PASSWORDNUMBER")
	private String passwordNumber;
	
	@Column(name="DATE_CREATION")
	private Date dateCreation;
	
	@ManyToOne
	@JoinColumn(name="USER_ID")
	private TbSystemUser userId;
	
	public TbPasswordDictionary(){
		super();
	}

	/**
	 * @param passwordDictionaryId the passwordDictionaryId to set
	 */
	public void setPasswordDictionaryId(Long passwordDictionaryId) {
		this.passwordDictionaryId = passwordDictionaryId;
	}

	/**
	 * @return the passwordDictionaryId
	 */
	public Long getPasswordDictionaryId() {
		return passwordDictionaryId;
	}

	/**
	 * @param passwordNumber the passwordNumber to set
	 */
	public void setPasswordNumber(String passwordNumber) {
		this.passwordNumber = passwordNumber;
	}

	/**
	 * @return the passwordNumber
	 */
	public String getPasswordNumber() {
		return passwordNumber;
	}

	/**
	 * @param dateCreation the dateCreation to set
	 */
	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	/**
	 * @return the dateCreation
	 */
	public Date getDateCreation() {
		return dateCreation;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(TbSystemUser userId) {
		this.userId = userId;
	}

	/**
	 * @return the userId
	 */
	public TbSystemUser getUserId() {
		return userId;
	}
	
	

}
