package util.ws;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import jpa.TbAvalTransaction;
import jpa.TbUmbral;
import util.HistoricalRecharges;


/**
 * 
 * @author jromero
 * Ejb para el servicio PSE
 */
@Remote
public interface PseWS {
	public List<com.ath.service.payments.pseservices.BankACHData> getPSEBanks(Long usrs, Long idClientAccount);
	public Long processTransactionPse(Long accountId,String pseUserType,String pseCodigoBanco,
			String nombreBanco,Long valor,String context,Long userCId,String ip);
	//process: 0 genera: 1 Sonda cliente
	public Long endPseTransaction(Long pseId,String ip,Long cUserId,Long process);
	public Long endAvalTransaction(Long avalId,String ip,Long cUserId,Long process);
	
	public String encodePSETransaction(String idPse);
	public String decodePSETransaction(String idPse);
	
	public String getUrlRedirigirById(Long pseId);
	public String getUrlRedirigirByIdAval(Long avalId);
	
	public boolean isUsedURL(Long idPse);
	public boolean isUsedURLAval(Long idAval);
	
	public boolean isOnTimeURLbyPSE(Long idPse);
	
	public boolean isOnTimeURLbyUmbral(Long idPse);
	public boolean isOnTimeURLbyUmbralAval(Long idAval);
	
	public boolean existURL(Long idPse);
	public boolean existURLAval(Long idAval);
	
	public Long getAccountByPse(Long idPse);
	public Long getAccountByAval(Long idAval);
	public TbAvalTransaction getTransactionAval(Long idAval);
	
	public Long getAprobadoById(Long idPse);
	public Long getAprobadoByIdAval(Long idaval);
	
	public Long getValorById(Long idPse);
	public Long getValorByIdAval(Long idAval);
	
	
	public String getBancoById(Long idPse);
	public String getBancoByIdAval(Long idAval);
	
	public Long getCodigoFinById(Long idPse);
	public String getBankByCode(List<com.ath.service.payments.pseservices.BankACHData> l,String code);
	public void createProcessForPse(Long creationUser, String ip,
			String messDetail, String messError, Long userId,Long processPSE,String file,Long pseId);
	public String getVoucherById(Long pseId);
	
	public Long getPseIdByUmbral(Long umbralId);
	public Long getAvalIdByUmbral(Long umbralId);
	
	public boolean applyRecharge(Long umbralId,Long state,String ip,Long cUserId);
	public boolean verifyStateUmbral(Long umbralId, Long processState);
	
	public boolean processingPse(Long pseId);
	public boolean processingAval(Long avalId);
	
	public Long getUmbralByPseId(Long pseId);
	public Long getUmbralByAvalId(Long avalId);
	
	public ArrayList<HistoricalRecharges> getPendingRecharges(Long accountId);
	public String generateXML(Object o,String code,Long type);
	public List<TbUmbral> getPendingRechargesForClient(Long accountId);
	
	public boolean isOnDayURLbyUmbral(Long idPse);
	public boolean isOnDayURLbyUmbralAval(Long idPse);
	
	public boolean validateUserTransaction(Long pseId,Long userId);
	public boolean listPendientes(Long idClientAccount);
	public Long activeRelationshipAgreement(Long userId, Long idClientAccount );
	public List<TbUmbral> getPendingRechargesForClientAval(Long accountId);
	public Long createTransactionAval(Long accountId, Long userId, String ipTransaction, Long valueRecharge,String context);
}