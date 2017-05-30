
package ejb;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import util.TransactionStatus;

import jpa.TbAccount;
import jpa.TbTransaction;
import jpa.TbTransactionDac;

import constant.TransactionType;

@Remote
public interface Transaction {

	/**
	 * 
	 * @param idAccount
	 * @param initDate
	 * @param endDate
	 * @return
	 */
	public List<TbTransaction> getTransactionByAccountAndDate(
			Long idAccount, Date initDate, Date endDate);
	
	
	/**
	 * 
	 * @param accoundId
	 * @param consignmentId
	 * @param deviceId
	 * @param transactionType
	 * @param description
	 * @param value
	 * @param ip
	 * @param creationUser
	 * @param previousBalance
	 * @param newBalance
	 * @param backOfficeTime
	 * @param laneId
	 * @param stationBoId
	 * @return
	 */
	public boolean saveAccountTransaction(Long accoundId, Long consignmentId,
			Long deviceId, TransactionType transactionType, String description, Long value,
			String ip, Long creationUser,Long previousBalance, Long newBalance,
			Timestamp backOfficeTime, Long laneId, Long stationBoId, Long vehicleId);
	
	 /**
	  * 
	  * @param clientId
	  * @return
	  */
	public List<TbAccount> getClientAccounts(Long clientId);
	
	/**
	 * 
	 * @param accountId
	 * @return
	 */
	public Long getAvailableBalanceAccount(Long accountId);
	
	/**
	 * 
	 * @param transaction string with required transaction values
	 * @return true is successful, false otherwise.
	 */
	public boolean upTranTelepeaje(String[] transaction);
	
	public List<TransactionStatus> getTransaction(Date initDate, Date endDate,
			Long companyId, Long stationId, Long laneId, Long clientId,
			Long accountId, Long deviceId);


	public TbTransactionDac getDetection(Long transactionId);
	
	public Long getLaneByTransaction(Long idTransaction);
	
	public List<TbAccount> getAvailableBalanceUserAccount(Long codeType,Long accountId, String codeClient);
	
	public List<String> getTransactionDesc(Long accountId, Long vehicleId,
			Long userId, Timestamp dateIni, Timestamp dateEnd);


	public List<String> getTransactionRec(Long accountId, Long vehicleId,
			Long userId,Timestamp dateIni, Timestamp dateEnd);
	
	public List<String> getTransactionDescRec(Long accountId, Long vehicleId,
			Long userId,Timestamp dateIni, Timestamp dateEnd);
	
	public String getFileTransactionPath();
	
	public Object[][] getSumaryByAccount(Long userId, Long accountId, Timestamp dateIni,
			Timestamp dateEnd);
	
	public BigDecimal getPreviousBalance(Long accountId, Timestamp dateIni,Timestamp dateEnd);
	
	public BigDecimal getNewBalance(Long accountId, Timestamp dateIni,Timestamp dateEnd);
	
	public Object[] getDeviceByTransaction(Long idTransaction);
	
	public boolean changeInfoTag(String tag, String placa, Long cate, Long user, String ip,Long res);
	
	public Long isEnrollVehicleOtherDevice(String tag, String placa);
	
	public boolean existVehicle(String placa);
	
	public boolean changeInfoTagReplace(String tag, String placa, Long cate, Long user, String ip);
}
