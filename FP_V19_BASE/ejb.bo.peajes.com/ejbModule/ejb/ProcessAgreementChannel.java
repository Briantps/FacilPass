package ejb;

import javax.ejb.Remote;

@Remote
public interface ProcessAgreementChannel {
	
	public String getParameterValueById(Long idProcessac);

}