package util.ws;

import java.util.Collection;
import java.util.Date;

import javax.ejb.Remote;

import com.corficolombiana.facilpass.ws.connector.gpmntcommerce.dto.AddTransactionResponseDTO;
import com.corficolombiana.facilpass.ws.connector.gpmntcommerce.dto.FeeDTO;
import com.corficolombiana.facilpass.ws.connector.gpmntcommerce.dto.GetTransactionResponseDTO;
import com.corficolombiana.facilpass.ws.connector.gpmntcommerce.dto.PersonalDataDTO;
import com.corficolombiana.facilpass.ws.connector.gpmntcommerce.dto.ReferenceDTO;
import com.corficolombiana.facilpass.ws.connector.gpmntcommerce.dto.SecretListDTO;

@Remote
public interface WSservices {
	
	
	public AddTransactionResponseDTO addTransactionRequest(Long rqUID, String channel, Date clientDt, String ipAddr, String idType, String idNum, String agreementId, String portalURL, Collection<SecretListDTO> secretList, PersonalDataDTO personalData, FeeDTO fee, Collection<ReferenceDTO> reference, String trnType,Long userId,Long accountId);
	
	public GetTransactionResponseDTO  getTransaction(Long rqUID, String channel, Date clientDt, String ipAddr, String idType, String idNum, String agreementId, String pmtId,Long userId,Long accountId,String bankName,String valuerecharge,String radId );	
}
