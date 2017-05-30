package security;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

/**
 * HttpSessionBindingListener causes an object to be notified when it is bound
 * to or unbound from a session.
 */
public class UserLogged implements HttpSessionBindingListener, Serializable{
	private static final long serialVersionUID = 1L;

	// Properties -----------------------------------------------------------------------------------------------------------------
	
    private static Map<UserLogged, HttpSession> logins = new HashMap<UserLogged, HttpSession>();
 
    private Long userId;
    
    private String userName;
    
    /*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		return (object instanceof UserLogged) && (userId != null) ? userId
				.equals(((UserLogged) object).userId) : (object == this);
	}
    
    /*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (userId != null) ? (this.getClass().hashCode() + userId
				.hashCode()) : super.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpSessionBindingListener#valueBound(javax.servlet.http.HttpSessionBindingEvent)
	 */
	@Override
	public void valueBound(HttpSessionBindingEvent event) {
		HttpSession session = logins.remove(this);
		if (session != null) {
			session.invalidate();
		}
		logins.put(this, event.getSession());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpSessionBindingListener#valueUnbound(javax.servlet.http.HttpSessionBindingEvent)
	 */
	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
		logins.remove(this);
	}

	/**
	 * @param id the id to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * @return the id
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * @param username the username to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return userName;
	}
}