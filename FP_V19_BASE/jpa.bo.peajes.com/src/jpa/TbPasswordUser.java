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

@Entity
@Table (name="TB_PASSWORD_USER")
public class TbPasswordUser implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_PASSWORD_USER_SEQ_GENERATOR")
	@SequenceGenerator(name = "TB_PASSWORD_USER_SEQ_GENERATOR", sequenceName = "TB_PASSWORD_USER_SEQ",allocationSize=1)
	@Column(name="PASSWORD_ID")
	private Long passwordId;

	@Column(name="PASSWORD_NUMBER")
	private String passwordNumber;
	
	@Column(name="DATE_CREATION")
	private Timestamp dateCreation;
	
	@ManyToOne
	@JoinColumn(name="USER_ID")	
	private TbSystemUser user;
	
	public TbPasswordUser (){
		super();
	}

	/**
	 * @param passwordId the passwordId to set
	 */
	public void setPasswordId(Long passwordId) {
		this.passwordId = passwordId;
	}

	/**
	 * @return the passwordId
	 */
	public Long getPasswordId() {
		return passwordId;
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
	public void setDateCreation(Timestamp dateCreation) {
		this.dateCreation = dateCreation;
	}

	/**
	 * @return the dateCreation
	 */
	public Timestamp getDateCreation() {
		return dateCreation;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(TbSystemUser user) {
		this.user = user;
	}

	/**
	 * @return the user
	 */
	public TbSystemUser getUser() {
		return user;
	}

}
