package jpa;

import java.io.Serializable;
import java.util.Date;

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
@Table(name = "TB_STAMP")
public class TbStamp implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "TB_STAMP_SEQ", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "TB_STAMP_SEQ", sequenceName = "TB_STAMP_SEQ",allocationSize=1)
	
	@Column(name="STAMP_ID")
	private Long stampId;
	
	@ManyToOne
	@JoinColumn(name="USER_ID")
	private TbSystemUser userId;
	
	@Column(name="STAMP_DATE")
	private Date stampDate;
	
	
	
	/**
	 * Constructor
	 */
    public TbStamp() {
    	super();
    }



	public Long getStampId() {
		return stampId;
	}



	public void setStampId(Long stampId) {
		this.stampId = stampId;
	}



	public TbSystemUser getUserId() {
		return userId;
	}



	public void setUserId(TbSystemUser userId) {
		this.userId = userId;
	}



	public Date getStampDate() {
		return stampDate;
	}



	public void setStampDate(Date stampDate) {
		this.stampDate = stampDate;
	}

}