package jpa;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the TB_ROLE database table.
 * 
 */
@Entity
@Table(name = "TB_ROLE")
public class TbRole implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_ROLE_TBROLEID_GENERATOR")
	@SequenceGenerator(name = "TB_ROLE_TBROLEID_GENERATOR", sequenceName = "TB_ROLE_SEQ",allocationSize=1)
	@Column(name="ROLE_ID")
	private Long roleId;

	@Column(name="ROLE_NAME")
	private String roleName;
	
	@ManyToOne
	@JoinColumn(name="TYPE_ROLE_ID")
	private TbTypeRole tbTypeRole;

	//bi-directional many-to-many association to TbSystemUser
	@ManyToMany
	@JoinTable(name="RE_USER_ROLE", joinColumns = @JoinColumn(name="ROLE_ID"), inverseJoinColumns = @JoinColumn(name="USER_ID"))
	private Set<TbSystemUser> tbSystemUsers;
	
	//bi-directional many-to-one association to TbRoleOptAct
	//@OneToMany(mappedBy="tbRole")
	//private Set<TbRoleOptAct> tbRoleOptActs;

	/**
	 * Constructor
	 */
    public TbRole() {
    	super();
    }

    /**
     * 
     * @return roleId
     */
	public Long getRoleId() {
		return this.roleId;
	}

	/**
	 * Setter for roleId
	 * @param roleId
	 */
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	/**
	 * 
	 * @return roleName
	 */
	public String getRoleName() {
		return this.roleName;
	}

	/**
	 * Setter for roleName
	 * @param roleName
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	/**
	 * 
	 * @return tbSystemUsers
	 */
	public Set<TbSystemUser> getTbSystemUsers() {
		return this.tbSystemUsers;
	}

	/**
	 * Setter for tbSystemUsers
	 * @param tbSystemUsers
	 */
	public void setTbSystemUsers(Set<TbSystemUser> tbSystemUsers) {
		this.tbSystemUsers = tbSystemUsers;
	}

	/**
	 * @param tbTypeRole the tbTypeRole to set
	 */
	public void setTbTypeRole(TbTypeRole tbTypeRole) {
		this.tbTypeRole = tbTypeRole;
	}

	/**
	 * @return the tbTypeRole
	 */
	public TbTypeRole getTbTypeRole() {
		return tbTypeRole;
	}


}