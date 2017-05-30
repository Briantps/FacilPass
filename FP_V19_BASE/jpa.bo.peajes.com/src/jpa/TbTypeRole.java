/**
 * 
 */
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
 * @author psanchez
 *
 */
@Entity
@Table(name="TB_TYPE_ROLE")
public class TbTypeRole  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "TB_TYPE_ROL_SEQ", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "TB_TYPE_ROL_SEQ", sequenceName = "TB_TYPE_ROL_SEQ",allocationSize=1)
	
	@Column(name="TYPE_ROLE_ID")
	private Long typeRoleId;
	
	@Column(name="TYPE_ROLE_DESCRIPTION")
	private String typeRoleDescription;
	
	@Column(name="TYPE_ROLE_STATE")
	private Integer typeRoleState;
	
	public TbTypeRole(){
		super();
	}

	/**
	 * @param typeRoleId the typeRoleId to set
	 */
	public void setTypeRoleId(Long typeRoleId) {
		this.typeRoleId = typeRoleId;
	}

	/**
	 * @return the typeRoleId
	 */
	public Long getTypeRoleId() {
		return typeRoleId;
	}

	/**
	 * @param typeRoleDescription the typeRoleDescription to set
	 */
	public void setTypeRoleDescription(String typeRoleDescription) {
		this.typeRoleDescription = typeRoleDescription;
	}

	/**
	 * @return the typeRoleDescription
	 */
	public String getTypeRoleDescription() {
		return typeRoleDescription;
	}

	/**
	 * @param typeRoleState the typeRoleState to set
	 */
	public void setTypeRoleState(Integer typeRoleState) {
		this.typeRoleState = typeRoleState;
	}

	/**
	 * @return the typeRoleState
	 */
	public Integer getTypeRoleState() {
		return typeRoleState;
	}

	
	
}