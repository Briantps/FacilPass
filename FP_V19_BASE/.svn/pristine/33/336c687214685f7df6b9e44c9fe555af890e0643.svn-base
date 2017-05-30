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
@Table(name = "RE_USER_POINT")
public class ReUserPoint implements Serializable{
	private static final long serialVersionUID = 1L;
	
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="RE_USER_ROLE_USERPOINTID_GENERATOR")
    @SequenceGenerator(name="RE_USER_ROLE_USERPOINTID_GENERATOR", sequenceName="RE_USER_POINT_SEQ",allocationSize=1)
    @Column(name="USER_POINT_ID")
    private Long userPointId;
    
    @ManyToOne
    @JoinColumn(name="USER_ID")
    private TbSystemUser tbSystemUser;
    
    @ManyToOne
    @JoinColumn(name="POINT_ID")
    private TbPoint tbPoint;
	
	/**
	 * Constructor
	 */
	public ReUserPoint(){
		super();
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
	 * @param userPointId the userPointId to set
	 */
	public void setUserPointId(Long userPointId) {
		this.userPointId = userPointId;
	}

	/**
	 * @return the userPointId
	 */
	public Long getUserPointId() {
		return userPointId;
	}

	/**
	 * @param tbPoint the tbPoint to set
	 */
	public void setTbPoint(TbPoint tbPoint) {
		this.tbPoint = tbPoint;
	}

	/**
	 * @return the tbPoint
	 */
	public TbPoint getTbPoint() {
		return tbPoint;
	}
}