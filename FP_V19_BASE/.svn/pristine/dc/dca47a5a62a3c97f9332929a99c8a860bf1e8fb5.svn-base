package jpa;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the RE_ROLE_OPTION_ACTION database table.
 * 
 */
@Entity
@Table(name="RE_ROLE_OPTION_ACTION")
public class ReRoleOptionAction implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="RE_ROLE_OPTION_ACTION_ROLEOPTIONACTIONID_GENERETOR")
	@SequenceGenerator(name="RE_ROLE_OPTION_ACTION_ROLEOPTIONACTIONID_GENERETOR",sequenceName="RE_ROLE_OPTION_ACTION_SEQ",allocationSize=1)
	@Column(name="ROLE_OPTION_ACTION_ID")
	private Long roleOptionActionId;
	
	@ManyToOne
	@JoinColumn(name="ROLE_ID")
	private TbRole tbRole;
	
	@ManyToOne
	@JoinColumn(name="OPTION_ACTION_ID")
	private ReOptionAction reOptionAction;
	
	/**
	 * Constructor
	 */
	public ReRoleOptionAction(){
		super();
	}

	/**
	 * @param roleOptionActionId the roleOptionActionId to set
	 */
	public void setRoleOptionActionId(Long roleOptionActionId) {
		this.roleOptionActionId = roleOptionActionId;
	}

	/**
	 * @return the roleOptionActionId
	 */
	public Long getRoleOptionActionId() {
		return roleOptionActionId;
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

	/**
	 * @param reOptionAction the reOptionAction to set
	 */
	public void setReOptionAction(ReOptionAction reOptionAction) {
		this.reOptionAction = reOptionAction;
	}

	/**
	 * @return the reOptionAction
	 */
	public ReOptionAction getReOptionAction() {
		return reOptionAction;
	}
}