package ejb;

import java.sql.Timestamp;
import java.util.List;

import javax.ejb.Remote;

import jpa.TbSystemUser;

import util.AllInfoAccount;
import util.LoginUser;

/**
 * @author Cesar Hoyos
 */
@Remote
public interface Login {

	/**
	 * @param mail
	 * @param password
	 * @param ip
	 * @return A LoginUser object indicating the user if exists and a message indicating if the user/password/user state are valid.
	 */
	public LoginUser  validateUser(String mail, String password, String ip);

	/**
	 * 
	 * @param systemUserId
	 * @return Long with the id of the role.
	 */
	public long getRoleByUser(long systemUserId);

	/**
	 * 
	 * @param userId
	 * @return
	 */
	public boolean updateLastLogin(long userId);
	
	public boolean inactivateUser(long userId, String ip);
	
	public boolean activateUser(long userId);
	
	public boolean preenrollUser(Long userId);

	public boolean updateAccessLogin(long userId);
	
	public boolean msgChangeReminder(Long userId);
	
	public String minimumPasswordAge(long userId);
	
	public boolean roleAdmin(long userId);
	
	public String passwordChangeReminder(Long userId);
	
	public String getLastEntry(Timestamp dateLastEntry);
	
	public TbSystemUser getUserMasterById(Long userId);

	public String questionResponseReminder(Long userId);
		
	public boolean resetCount(Long userId);
	
	public boolean lockAccount(Long userId);
	
	public boolean userOtpModal(Long userId, int count, String ip);
	
	public String validateOtpModal(String password, Long userId, String ip);
	
	public boolean userOtpLockedModal(Long userId, int count, String ip);
	
	public void inactiveOtp(String otp, Long userId);
	
	public String userOtpLogin(Long userId);
	
	public List<AllInfoAccount> getAccountLoginList(Long userId);

}
