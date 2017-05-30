/**
 * 
 */
package constant;

/**
 * Enum for Email Type. 
 * 
 * @author tmolina
 * @since 18-02-2011
 */
public enum EmailType {
	
	USER("user_mail"),
	CLIENT("client_mail"),
	ALERT_DEVICE("alert_device_mail"),
	ADMIN("admin_mail");
	
	
	private String logAction;

	private EmailType(String str) {
		this.logAction = str;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return this.logAction;
	}
}