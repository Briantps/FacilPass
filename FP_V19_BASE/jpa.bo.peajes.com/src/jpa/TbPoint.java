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
 * @author tmolina
 *
 */
@Entity
@Table(name="TB_POINT")
public class TbPoint implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_POINT_POINTID_GENERATOR")
	@SequenceGenerator(name="TB_POINT_POINTID_GENERATOR",  sequenceName = "TB_POINT_SEQ",allocationSize=1)
	@Column(name="POINT_ID")
	private Long pointId;
	
	@Column(name="POINT_IP")
	private String pointIp;
	
	@Column(name="POINT_PORT")
	private Integer pointPort;
	
	@Column(name="POINT_NAME")
	private String pointName;

	/**
	 * Constructor.
	 */
	public TbPoint() {
		super();
	}

	/**
	 * @param pointId the pointId to set
	 */
	public void setPointId(Long pointId) {
		this.pointId = pointId;
	}

	/**
	 * @return the pointId
	 */
	public Long getPointId() {
		return pointId;
	}

	/**
	 * @param pointIp the pointIp to set
	 */
	public void setPointIp(String pointIp) {
		this.pointIp = pointIp;
	}

	/**
	 * @return the pointIp
	 */
	public String getPointIp() {
		return pointIp;
	}

	/**
	 * @param pointPort the pointPort to set
	 */
	public void setPointPort(Integer pointPort) {
		this.pointPort = pointPort;
	}

	/**
	 * @return the pointPort
	 */
	public Integer getPointPort() {
		return pointPort;
	}

	public void setPointName(String pointName) {
		this.pointName = pointName;
	}

	public String getPointName() {
		return pointName;
	}
}