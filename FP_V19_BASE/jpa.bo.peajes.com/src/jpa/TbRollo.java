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
@Table(name="TB_ROLL")
public class TbRollo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "TB_ROLL_SEQ", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "TB_ROLL_SEQ", sequenceName = "TB_ROLL_SEQ",allocationSize=1)
	@Column(name="ROLL_ID")
	private Long rollId;
	
	@Column(name="ROLL_NAME")
	private String rollName;
	
	@ManyToOne
	@JoinColumn(name="COURIER_ID")
	private TbCourier courierId;
	
	@Column(name="ADITIONAL1")
	private String aditional1;
	
	@Column(name="ADITIONAL2")
	private String aditioanl2;
	
	@Column(name="ROLL_STATE")
	private Integer rollState;
	
	public TbRollo(){
		
	}

	/**
	 * @param rollId the rollId to set
	 */
	public void setRollId(Long rollId) {
		this.rollId = rollId;
	}

	/**
	 * @return the rollId
	 */
	public Long getRollId() {
		return rollId;
	}

	/**
	 * @param rollName the rollName to set
	 */
	public void setRollName(String rollName) {
		this.rollName = rollName;
	}

	/**
	 * @return the rollName
	 */
	public String getRollName() {
		return rollName;
	}

	/**
	 * @param courierId the courierId to set
	 */
	public void setCourierId(TbCourier courierId) {
		this.courierId = courierId;
	}

	/**
	 * @return the courierId
	 */
	public TbCourier getCourierId() {
		return courierId;
	}

	/**
	 * @param aditional1 the aditional1 to set
	 */
	public void setAditional1(String aditional1) {
		this.aditional1 = aditional1;
	}

	/**
	 * @return the aditional1
	 */
	public String getAditional1() {
		return aditional1;
	}

	/**
	 * @param aditioanl2 the aditioanl2 to set
	 */
	public void setAditioanl2(String aditioanl2) {
		this.aditioanl2 = aditioanl2;
	}

	/**
	 * @return the aditioanl2
	 */
	public String getAditioanl2() {
		return aditioanl2;
	}

	/**
	 * @param rollState the rollState to set
	 */
	public void setRollState(Integer rollState) {
		this.rollState = rollState;
	}

	/**
	 * @return the rollState
	 */
	public Integer getRollState() {
		return rollState;
	}

}
