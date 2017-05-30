package ejb.taskeng.email.impl;

import ejb.taskeng.email.FromField;

public class FromFieldBase implements FromField {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String address;
	private String name;
	
	public FromFieldBase(){
	}
	
	/* (non-Javadoc)
	 * @see email.FromField#getAddress()
	 */
	public String getAddress() {
		return address;
	}
	/* (non-Javadoc)
	 * @see email.FromField#setAddress(java.lang.String)
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/* (non-Javadoc)
	 * @see email.FromField#getName()
	 */
	public String getName() {
		return name;
	}
	/* (non-Javadoc)
	 * @see email.FromField#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
	}	
}
