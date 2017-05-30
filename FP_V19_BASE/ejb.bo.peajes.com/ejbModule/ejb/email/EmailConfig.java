package ejb.email;

import java.util.List;

import javax.ejb.Remote;

import jpa.ReEmailParametersProcess;
import jpa.TbEmailProcess;
import jpa.TbEmailType;
import jpa.TbOptActRefefenceType;

@Remote
public interface EmailConfig {
	
	public List<TbOptActRefefenceType> getListReferences(Long idProcess);
	
	public List<TbEmailProcess> getListProcess();
	
	public List<TbEmailProcess> getListProcessNoMessage();
	
	public List<ReEmailParametersProcess> getParameters(Long processId);
	
	public boolean setMessageEmail(String idType, Long idProcess, String subject, String emailAddressList, String message, String ip, Long user);
	
	public String getAbbreviationByParameter(Long idParameter);
	
	public String getReplaceByAbbreviation(String abbreviation);
	
	public String replaceParameterPrev(String template);
	
	public List<TbEmailType> getListEmailTypeByProcess(Long processId);
	
	public TbEmailType getEmailTypebyId(Long emailId);
	
	public boolean editMessageEmail(Long emailId, String idType, Long idProcess, String subject,String emailAddressList, String message, String ip, Long user);
	
	public boolean deleteMessageEmail(Long emailId, String ip, Long user);
	
	public boolean activeMessageEmail(Long emailId, String ip, Long user);
	
	public boolean validateProcessTypeMsg(Long processId, String typeMsgId);
}
