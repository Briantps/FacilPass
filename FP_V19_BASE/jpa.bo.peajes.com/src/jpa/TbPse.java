package jpa;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the TB_PSE database table.
 * 
 */
@Entity
@Table(name="TB_PSE")
public class TbPse implements Serializable {	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TB_PSE_GENERATOR", sequenceName="TB_PSE_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TB_PSE_GENERATOR")
	@Column(name="PSE_ID", unique=true, nullable=false, precision=19)
	private Long pseId;
	
	@Column(name="USER_ID")
	private Long userId;
	
	@Column(name="USER_CREATOR")
	private Long userCreator;
	
	@Column(name="DATE_CREATOR")
	private Timestamp dateCreator;

	/**
	 * Constructor
	 */
    public TbPse() {
    }

	public void setPseId(Long pseId) {
		this.pseId = pseId;
	}

	public Long getPseId() {
		return pseId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserCreator(Long userCreator) {
		this.userCreator = userCreator;
	}

	public Long getUserCreator() {
		return userCreator;
	}

	public void setDateCreator(Timestamp dateCreator) {
		this.dateCreator = dateCreator;
	}

	public Timestamp getDateCreator() {
		return dateCreator;
	}

}