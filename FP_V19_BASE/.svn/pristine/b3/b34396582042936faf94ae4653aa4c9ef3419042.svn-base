package ejb;

import java.util.List;

import javax.ejb.Remote;

import jpa.TbPasswordDictionary;
import jpa.TbSystemUser;

@Remote
public interface Dictionary {

	public List<TbPasswordDictionary> getPasswords();
	
	public boolean savePassword(String newPassword, TbSystemUser user);
	
	public boolean deletePassword(Long passwordId);
	
	public boolean updatePassword(Long passwordId, String newPassword2);
	
	public String validateSize(String newPassword);
}
