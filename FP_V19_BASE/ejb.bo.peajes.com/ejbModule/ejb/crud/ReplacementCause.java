package ejb.crud;
import java.util.List;

import javax.ejb.Remote;

import jpa.TbReplacementCause;

@Remote
public interface ReplacementCause {
	
	/**
	 * 
	 * @return {@link TbReplacementCause} {@link List}
	 */
	public List<TbReplacementCause> getReplacementCauses();
	
	/**
	 * 
	 * @param cause
	 * @param ip
	 * @param creationUser
	 * @return {@link Long} -1 if the cause exits, null if error, 0 if successful.
	 */
	public Long insertCause(String cause, String ip, Long creationUser);
	
	/**
	 * @param causeId
	 * @param cause
	 * @param ip
	 * @param creationUser
	 * @return <code>true</code> if successful, <code>false</code> otherwise.
	 */
	public boolean editCause(Long causeId, String cause, String ip, Long creationUser);
}
