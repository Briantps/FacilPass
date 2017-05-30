package util;

import java.io.Serializable;

public class NotificationsList implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String notificationDate;
	
	private String notification;
	
	private String descriptionType;
	
	private String imageUrl;
	
	private String usrs;
	
	public NotificationsList(){
	    super();
	}

	/**
	 * @param notificationDate the notificationDate to set
	 */
	public void setNotificationDate(String notificationDate) {
		this.notificationDate = notificationDate;
	}

	/**
	 * @return the notificationDate
	 */
	public String getNotificationDate() {
		return notificationDate;
	}

	/**
	 * @param notification the notification to set
	 */
	public void setNotification(String notification) {
		this.notification = notification;
	}

	/**
	 * @return the notification
	 */
	public String getNotification() {
		return notification;
	}

	/**
	 * @param descriptionType the descriptionType to set
	 */
	public void setDescriptionType(String descriptionType) {
		this.descriptionType = descriptionType;
	}

	/**
	 * @return the descriptionType
	 */
	public String getDescriptionType() {
		return descriptionType;
	}

	/**
	 * @param imageUrl the imageUrl to set
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	/**
	 * @return the imageUrl
	 */
	public String getImageUrl() {
		return imageUrl;
	}

	/**
	 * @param usrs the usrs to set
	 */
	public void setUsrs(String usrs) {
		this.usrs = usrs;
	}

	/**
	 * @return the usrs
	 */
	public String getUsrs() {
		return usrs;
	}


	

}
