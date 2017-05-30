package ejb.taskeng.email.impl;

import ejb.taskeng.email.Body;
import ejb.taskeng.email.EmailDef;
import ejb.taskeng.email.Header;

public class EmailDefBase implements EmailDef {
	
	private static final long serialVersionUID = 4201815758968240918L;
	
	private String type;
	private Header header;
	private Body body;
	
	public EmailDefBase(){
	}
	
	/* (non-Javadoc)
	 * @see email.Email#setHeaders(java.util.List)
	 */
	public void setHeader(Header header) {
		this.header = header;
	}
	
	/* (non-Javadoc)
	 * @see email.Email#getHeaders()
	 */
	public Header getHeader() {
		return header;
	}
	
	/* (non-Javadoc)
	 * @see email.Email#setType(java.lang.String)
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/* (non-Javadoc)
	 * @see email.Email#getType()
	 */
	public String getType() {
		return type;
	}
	
	public void setBody(Body body) {
		this.body = body;
	}

	public Body getBody() {
		return body;
	}
}
