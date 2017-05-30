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
@Table(name="TB_NOTIFICATION")
public class TbNotificationTransaction implements Serializable{

	private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name="NOTIFICATION_SEQ",sequenceName="TB_NOTIFICATION_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="NOTIFICATION_SEQ")
    @Column(name="NOTIFICATION_ID")
	private Long notificationId;
	
    @Column(name="NOTIFICATION_DATE")
	private Date notificationDate;
	
    @Column(name="DESCRIPTION")
	private String description;
	
    @ManyToOne
    @JoinColumn(name="CODE_IMAGE_REJECTED_ID")
	private TbCodeImagesRejected codeImageId;
	
    @ManyToOne
    @JoinColumn(name="IMAGE_ID")
	private TbImage imageId;
	
    @Column(name="USRS")
	private String user;
	
    @ManyToOne
    @JoinColumn(name="TRANSACTION_ID")
	private TbTransaction transactionId;
	
    
	public TbNotificationTransaction(){
		super();
	}

	/**
	 * @param notificationId the notificationId to set
	 */
	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}

	/**
	 * @return the notificationId
	 */
	public Long getNotificationId() {
		return notificationId;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param codeImageId the codeImageId to set
	 */
	public void setCodeImageId(TbCodeImagesRejected codeImageId) {
		this.codeImageId = codeImageId;
	}

	/**
	 * @return the codeImageId
	 */
	public TbCodeImagesRejected getCodeImageId() {
		return codeImageId;
	}

	/**
	 * @param imageId the imageId to set
	 */
	public void setImageId(TbImage imageId) {
		this.imageId = imageId;
	}

	/**
	 * @return the imageId
	 */
	public TbImage getImageId() {
		return imageId;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param transactionId the transactionId to set
	 */
	public void setTransactionId(TbTransaction transactionId) {
		this.transactionId = transactionId;
	}

	/**
	 * @return the transactionId
	 */
	public TbTransaction getTransactionId() {
		return transactionId;
	}

	/**
	 * @param notificationDate the notificationDate to set
	 */
	public void setNotificationDate(Date notificationDate) {
		this.notificationDate = notificationDate;
	}

	/**
	 * @return the notificationDate
	 */
	public Date getNotificationDate() {
		return notificationDate;
	}
}
