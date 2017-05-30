package ejb;
import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import constant.ConsignmentState;
import constant.PurchaseOrderState;

import util.Order;
import util.PurchaseOrder;
import util.device.ToRecharge;

import jpa.TbAccount;
import jpa.TbBank;
import jpa.TbBankAccount;
import jpa.TbConsignment;
import jpa.TbDevice;
import jpa.TbPayType;
import jpa.TbOperationType;
import jpa.TbPurchaseOrderDetail;
import jpa.TbStationBO;
import jpa.TbSystemUser;

@Remote
public interface Purchase {
	
	/**
	 * 
	 * @param accountClientId The client account of the consignment.
	 * @param value Consignment Value
	 * @param number Consignment Number
	 * @param date Consignment Date
	 * @param ip User IP Address.
	 * @param consigType Consignment Pay Type.
	 * @param numCheck
	 * @param idBank
	 * @param configOffice
	 * @param accountId
	 * @param originAccount
	 * @param idCreationUser
	 * @param consignmentConcept
	 * @param idUser Client associated 
	 * @return Id if the consignment if created, null otherwise.
	 */
	public Long saveConsignment(Long accountClientId, Long value,
			String number, Date date, String ip, Long consigType,
			String numCheck, Long idBank, String configOffice,
			Long accountId, String originAccount, Long idCreationUser,
			String consignmentConcept);
	
	/**
	 * 
	 * @param idData Id  TbUserData
	 * @param transNumber Number of transactions to do.
	 * @param ip Creation User IP
	 * @param creationUser Creation User Id
	 * @param idStation
	 * @param detailList
	 * @return true is successful, false otherwise.
	 */
	public PurchaseOrder savePuchaseOrder(Long idData, Long transNumber, String ip,
			Long creationUser, Long idStation, List<Order> detailList);
	
	/**
	 * 
	 * @return List of Stations Type = 2
	 */
	public List<TbStationBO> getRechargeStations();
	
	/**
	 * 
	 * @return List of Operation types.
	 */
	public List<Order> getOperationTypes();
	
	/**
	 * Gets the value the client has to pay when he buys a new card.
	 * @return New Card Value.
	 */
	public Long getNewCardValue();
	
	/**
	 * 
	 * @return List of Consignment Pay types.
	 */
	public List<TbPayType> getPayTypes();
	
	/**
	 * 
	 * @return List Of Banks.
	 */
	public List<TbBank> getBanks();
	
	/**
	 * 
	 * @return List of Bank Accounts.
	 */
	public List<TbBankAccount> getAccounts();
	
	/**
	 * 
	 * @param orderNumber
	 * @param state State of the Order.
	 * @param searchBystate indicates if has to search by state.
	 * @return A Purchase Order With the associated details.
	 */
	public PurchaseOrder getPurchaseOrderByState(Long orderNumber,
			PurchaseOrderState state, boolean searchBystate);
	
	/**
	 * Deletes a purchase order detail.
	 * @param idDetail
	 * @param ip
	 * @param creationUser
	 * @return true if successful, false otherwise.
	 */
	public boolean deleteDetail(Long idDetail, String ip, Long creationUser);
	
	/**
	 * 
	 * @return List of Operation Types.
	 */
	public List<TbOperationType> getListOfOperationTypes();
	
	/**
	 * Adds a new detail to the purchase order.
	 * @param idType Purchase Order Detail Type.
	 * @param idOrder Purchase Order Id.
	 * @param ip IP transaction User.
	 * @param creationUser Creation User Id.
	 * @return true is successful, false otherwise. 
	 */
	public boolean addOrderDetail(Long idType, Long idOrder, String ip,
			Long creationUser);
	
	/**
	 * Changes dependency of the client on the purchase order.
	 * @param idData Id new Data (Office)
	 * @param idOrder Purchase Order Id.
	 * @param ip IP transaction User.
	 * @param creationUser Creation User Id.
	 * @return true is successful, false otherwise. 
	 */
	public boolean changeOrderClientOffice(Long idData, Long idOrder, String ip,
			Long creationUser);
	
	/**
	 * 
	 * @param idOrder
	 * @param idcreationUser
	 * @param accountId
	 * @param ip
	 * @return
	 */
	public boolean saveOrderAccount(Long idOrder, Long idcreationUser,
			Long accountId, String ip);
	
	/**
	 * Updates a detail order.
	 * @param idDetail
	 * @param idDevice
	 * @param idStation
	 * @param value
	 * @param ip
	 * @param creationUser
	 * @return true if successful, false otherwise.
	 */
	public boolean updateDetail(Long idDetail, Long idDevice, Long idStation,
			Long value, String ip, Long creationUser);
	
	/**
	 * 
	 * @param idOrder
	 * @param ip
	 * @param creationUser
	 * @param invoiceNumber
	 * @param orderValue
	 * @param accountId
	 * @return <code>true</code> if successful, <code>false</code> otherwise.
	 */
	public boolean orderSendTreasury(Long idOrder, String ip,
			Long creationUser, String invoiceNumber, Long orderValue, Long accountId);
	
	/**
	 * 
	 * @param state State of consignment.
	 * @return {@link List} Of {@link TbConsignment}
	 */
	public List<TbConsignment> getConsignmentByState(ConsignmentState state);
	
	/**
	 * Approbation of a consignment.
	 * @param idConsig
	 * @param ip
	 * @param creationUser
	 * @return <code>true</code> if successful, <code>false</code> otherwise.
	 */
	public boolean approve (Long idConsig, String ip, Long creationUser);
	
	/**
	 * 
	 * @param state State of the {@link PurchaseOrder}
	 * @return {@link List} of {@link PurchaseOrder}
	 */
	public List<PurchaseOrder> getPurchaseOrdersByState(PurchaseOrderState state);
	
	/**
	 * Confirmation by warehouse of purchase order.
	 * @param idOrder
	 * @param creationUser
	 * @param ip
	 * @return <code>true</code> if successful, <code>false</code> otherwise.
	 */
	public boolean confirmOrder(Long idOrder, Long creationUser, String ip);
	
	/**
	 * 
	 * @return {@link ToRecharge} {@link List} 
	 */
	public List<ToRecharge> getRechageList();
	
	/**
	 * 
	 * @param toRechargeObject
	 * @param newBalance
	 * @param ip
	 * @param creationUser
	 * @return
	 */
	public boolean recharge(ToRecharge toRechargeObject, Long oldBalance, Long newBalance,
			String ip, Long creationUser);
	
	/**
	 * 
	 * @return {@link TbSystemUser} {@link List}
	 */
	public List<TbSystemUser> getOrderClients();
	
	/**
	 * 
	 * @return {@link TbDevice} {@link List}
	 */
	public List<TbDevice> getOrderDevices();
	
	/**
	 * 
	 * @param idOperationType
	 * @param checkDate
	 * @param begDate
	 * @param endDate
	 * @param checkClient
	 * @param idClient
	 * @param checkTisc
	 * @param idDevice
	 * @param checkValue
	 * @param value
	 * @return {@link TbPurchaseOrderDetail} {@link List}
	 */
	public List<TbPurchaseOrderDetail> histoConsult(Long idOperationType, boolean checkDate, Date begDate, Date endDate,
			boolean checkClient, Long idClient, boolean checkTisc, Long idDevice, boolean checkValue, Long value);
	
	/**
	 * Rejection of a consignment.
	 * @param idConsig
	 * @param ip
	 * @param creationUser
	 * @return <code>true</code> if successful, <code>false</code> otherwise.
	 */
	public boolean reject (Long idConsig, String ip, Long creationUser);
	
	/**
	 * 
	 * @param state State of consignment.
	 * @return {@link List} Of {@link TbConsignment}
	 */
	public List<TbConsignment> getConsignmentByStateAndClient(
			ConsignmentState state, Long clientId);
	
	/**
	 * 
	 * @param clientId Id Client.
	 * @return {@link List} Of {@link TbConsignment}
	 */
	public List<TbConsignment> getConsignmentByClient(Long clientId);
	
	/**
	 * 
	 * @param accoundId
	 * @param deviceId
	 * @param value
	 * @param ip
	 * @param creationUser
	 * @return
	 */
	public boolean savePayment(Long accoundId, Long deviceId, Long value,
			String ip, Long creationUser);
	
	/**
	 * 
	 * @param purchaseDetailId
	 * @param account
	 * @param value
	 * @param ip
	 * @param creationUser
	 * @param stationId
	 * @return
	 */
	public boolean saveRecharge(Long purchaseDetailId, Long account,
			Long value, String ip, Long creationUser, Long stationId);
	
	/**
	 * 
	 * @param deviceId
	 * @param value
	 * @param accountId
	 * @param ip
	 * @param creationUser
	 * @param stationId
	 * @param email
	 * @return
	 */
	public boolean recharge(Long deviceId, Long value, Long accountId,
			String ip, Long creationUser, Long stationId, String email);
	
	
	public String distributionBalance(TbAccount account,Long value,String ip, Long creationUser);
	
	boolean recharge1(Long deviceId, Long value, Long accountId, String ip,
			Long creationUser, Long stationId, String email);
	
	public String selectReAccountDevice(Long deviceId);
	
	public String getTbVehiclePlate(String idVehicle);
}