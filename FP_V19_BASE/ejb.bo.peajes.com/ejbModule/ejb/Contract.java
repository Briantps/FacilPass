package ejb;

import java.sql.Timestamp;

import javax.ejb.Remote;


/**
 * Interface of ContractEJB
 * @author jromero
 */
@Remote
public interface Contract {

	public boolean signContract(Long userID,Long documentId,String ip);
	
	public Long getTypeContract(Long userId);
	
	public String[] getContract(Long documentId);
	
	public Long saveRegDocument(Long userId,Integer state,String url,
			Timestamp uploadDate,Timestamp processDate);
	
	public void sendMail(String mess,String ip);
	
	public boolean isFirstPass();
	
	public boolean isOldPass(String pass);
	
	public boolean changePassCrl(String pass,String ip,Long userId);
	
	public String getParameterValueById(Long idParameter);
	
	public boolean saveRegacceptscontrac(long userId, String ip, boolean aceptcient);
	
	public Long getStateDocument(Long userId);
	
}
