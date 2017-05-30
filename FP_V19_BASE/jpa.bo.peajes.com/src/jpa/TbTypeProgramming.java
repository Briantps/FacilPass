/**
 * 
 */
package jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * The persistent class for the TB_BANK database table.
 * @author tmolina
 *
 */
@Entity
@Table(name="TB_TYPE_PROGRAMMING")
public class TbTypeProgramming implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="TYPE_PROGRAMMING_ID")
	private Long typeProgramminId;
	
	@Column(name="TYPE_PROGRAMMING_DESC")
	private String typeProgrammingDesc;
	
	@ManyToOne
	@JoinColumn(name="STATE_ID")
	private TbState tbState;
		
	/**
	 * Constructor.
	 */
	public TbTypeProgramming() {
		super();
	}

	public Long getTypeProgramminId() {
		return typeProgramminId;
	}

	public void setTypeProgramminId(Long typeProgramminId) {
		this.typeProgramminId = typeProgramminId;
	}

	public String getTypeProgrammingDesc() {
		return typeProgrammingDesc;
	}

	public void setTypeProgrammingDesc(String typeProgrammingDesc) {
		this.typeProgrammingDesc = typeProgrammingDesc;
	}

	public TbState getTbState() {
		return tbState;
	}

	public void setTbState(TbState tbState) {
		this.tbState = tbState;
	}		
}