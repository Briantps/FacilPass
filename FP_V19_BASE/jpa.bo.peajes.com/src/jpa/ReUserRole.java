package jpa;

import java.io.Serializable;

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
@Table(name = "RE_USER_ROLE")
public class ReUserRole implements Serializable{
	private static final long serialVersionUID = 1L;
	
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="RE_USER_ROLE_USERROLEID_GENERATOR")
    @SequenceGenerator(name="RE_USER_ROLE_USERROLEID_GENERATOR", sequenceName="RE_USER_ROLE_SEQ",allocationSize=1)
    @Column(name="USER_ROLE_ID")
    private Long userRoleId;
    
    @ManyToOne
    @JoinColumn(name="USER_ID")
    private TbSystemUser tbSystemUser;
    
    @ManyToOne
    @JoinColumn(name="ROLE_ID")
    private TbRole tbRole;
    
    @Column(name = "ROLE_ID_OLD")
	private Long roleIdOld;
	
	/**
	 * Constructor
	 */
	public ReUserRole(){
		super();
	}

	/**
	 * @param userRoleId the userRoleId to set
	 */
	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}

	/**
	 * @return the userRoleId
	 */
	public Long getUserRoleId() {
		return userRoleId;
	}

	/**
	 * @param tbSystemUser the tbSystemUser to set
	 */
	public void setTbSystemUser(TbSystemUser tbSystemUser) {
		this.tbSystemUser = tbSystemUser;
	}

	/**
	 * @return the tbSystemUser
	 */
	public TbSystemUser getTbSystemUser() {
		return tbSystemUser;
	}

	/**
	 * @param tbRole the tbRole to set
	 */
	public void setTbRole(TbRole tbRole) {
		this.tbRole = tbRole;
	}

	/**
	 * @return the tbRole
	 */
	public TbRole getTbRole() {
		return tbRole;
	}

	public void setRoleIdOld(Long roleIdOld) {
		this.roleIdOld = roleIdOld;
	}

	public Long getRoleIdOld() {
		return roleIdOld;
	}
}