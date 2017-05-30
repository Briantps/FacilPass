package jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="TB_USER_STATE_TYPE")
public class TbUserStateType implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="USER_STATE_ID")
	private Integer userStateId;
	
	@Column(name="USER_STATE_DESCRIPTION")
	private String userStateDescription;

	

	public void setUserStateDescription(String userStateDescription) {
		this.userStateDescription = userStateDescription;
	}

	public String getUserStateDescription() {
		return userStateDescription;
	}

	public void setUserStateId(Integer userStateId) {
		this.userStateId = userStateId;
	}

	public Integer getUserStateId() {
		return userStateId;
	}

}
