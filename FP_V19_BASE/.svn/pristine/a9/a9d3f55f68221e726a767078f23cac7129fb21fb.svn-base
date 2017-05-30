package jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="CODES_IMAGES_REJECTED")
public class TbCodeImagesRejected implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="NOTIFICATION_TYPE_ID")
	private Long notificationErrorId;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@Column(name="TYPE")
	private Long type;
	
	public TbCodeImagesRejected(){
		super();
	}

	/**
	 * @param notificationErrorId the notificationErrorId to set
	 */
	public void setNotificationErrorId(Long notificationErrorId) {
		this.notificationErrorId = notificationErrorId;
	}

	/**
	 * @return the notificationErrorId
	 */
	public Long getNotificationErrorId() {
		return notificationErrorId;
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
	 * @param type the type to set
	 */
	public void setType(Long type) {
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public Long getType() {
		return type;
	}
	
	
}
