package ejb;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import util.Order;
import util.OrderDetail;
import util.PurchaseOrder;
import util.TimeUtil;
import util.device.ToRecharge;

import constant.ConsignmentState;
import constant.DeviceType;
import constant.EmailProcess;
import constant.EmailSubject;
import constant.EmailType;
import constant.LogAction;
import constant.LogReference;
import constant.OperationType;
import constant.ProcessTrackDetailType;
import constant.ProcessTrackType;
import constant.PurchaseOrderDetailState;
import constant.PurchaseOrderState;
import constant.StationType;
import constant.TransactionType;
import constant.TypeTask;
import crud.ObjectEM;
import ejb.email.Outbox;
import ejb.taskeng.TransitTask;

import jpa.ReAccountDevice;
import jpa.ReAccountPurchaseOrder;
import jpa.TbAccount;
import jpa.TbBank;
import jpa.TbBankAccount;
import jpa.TbConsignment;
import jpa.TbConsignmentState;
import jpa.TbDevice;
import jpa.TbDeviceState;
import jpa.TbPayType;
import jpa.TbOperationType;
import jpa.TbProcessTrack;
import jpa.TbProcessTrackDetailType;
import jpa.TbPurchaseOrder;
import jpa.TbPurchaseOrderDetail;
import jpa.TbStationBO;
import jpa.TbSystemParameter;
import jpa.TbSystemUser;
import jpa.TbTag;
import jpa.TbTask;
import jpa.TbTransaction;
import jpa.TbTransactionType;
import jpa.TbUserData;

/**
 * Session Bean implementation class PurchaseEJB
 */
@Stateless(mappedName = "ejb/Purchase")
public class PurchaseEJB implements Purchase {

	@PersistenceContext(unitName = "bo")
	private EntityManager em;
	
	@EJB(mappedName="ejb/Log")
	private Log log;
	
	@EJB(mappedName="ejb/Process")
	private Process process;
	
	@EJB(mappedName="ejb/Task")
	private Task task;
	
	@EJB(mappedName ="ejb/sendMail")
	private SendMail sendMail;
	
	@EJB(mappedName ="ejb/email/Outbox")
	private Outbox outbox;
	
	@EJB(mappedName ="ejb/Transaction")
	private Transaction transaction;
	
	@EJB(mappedName="ejb/TransitTask")
	private TransitTask transitTask;
	
	@EJB(mappedName="ejb/EnrollDevice")
	private EnrollDevice enrollDevice;
	
	private ObjectEM emObj;
	
    /**
     * Default constructor. 
     */
    public PurchaseEJB() {
    }

   /*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Purchase#saveConsignment(java.lang.Long, java.lang.Long,
	 * java.lang.String, java.util.Date, java.lang.String, java.lang.Long,
	 * java.lang.String, java.lang.Long, java.lang.String, java.lang.Long,
	 * java.lang.String, java.lang.Long, java.lang.Long)
	 */
    @Override
	public Long saveConsignment(Long accountClientId, Long value,
			String number, Date date, String ip, Long consigType,
			String numCheck, Long idBank, String configOffice,
			Long accountId, String originAccount, Long idCreationUser,
			String consignmentConcept) {
		try {
			TbConsignment consignment = new TbConsignment();
			
			if (date != null) {
				consignment.setConsignmentDate(new Timestamp(date.getTime()));
			}
			
			consignment.setConsignmentNumber(number);
			consignment.setTbPayType(em.find(TbPayType.class, consigType.longValue()));
			consignment.setConsignmentRegisterDate(new Timestamp(System.currentTimeMillis()));
			consignment.setTbConsignmentState(em.find(TbConsignmentState.class,
					ConsignmentState.NEW.getId()));
			consignment.setConsignmentValue(value);
			consignment.setConsignmentConcept(consignmentConcept);
			
			TbAccount a = em.find(TbAccount.class, accountClientId);
			
			consignment.setTbAccount(a);
			consignment.setConsignmentCheckNumber(numCheck);
			
			if (idBank != null) {
				consignment.setTbBank(em.find(TbBank.class, idBank));
			}
			
			consignment.setConsignmentOffice(configOffice);
			if (accountId != null) {
				consignment.setTbBankAccount(em.find(TbBankAccount.class,
						accountId));
			}			
			consignment.setOriginAccountNumber(originAccount);
						
			emObj = new ObjectEM(em);
		
			if (emObj.create(consignment)) {		
				// Saving this transaction into the client process track;
				// First, looking for the client process.
				TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, a.getTbSystemUser().getUserId());
				TbProcessTrack ptClient = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, a.getTbSystemUser().getUserId());
				
				if (pt != null) {// If found any (should find one), then create the new detail.
					
					String msg = "Se ha creado la Consignación por valor:  " + "$" + consignment.getConsignmentValue()
							+ ", por concepto de: " + consignmentConcept + ".";

					Long processDetailId = process.createProcessDetail(pt.getProcessTrackId(), ProcessTrackDetailType.CLIENT_CONSIGMENT_REGISTER,
							msg, idCreationUser, ip, 1, "No se pudo registar en el proceso la consignación ID: "+ consignment.getConsignmentId() +". ID. Proceso :"
									+ pt.getProcessTrackId(),null,null,null,null);
					
					process.createProcessDetail(ptClient.getProcessTrackId(), ProcessTrackDetailType.CLIENT_CONSIGMENT_REGISTER,
							msg, idCreationUser, ip, 1, "No se pudo registar en el proceso la consignación ID: "+ consignment.getConsignmentId() +". ID. Proceso :"
									+ ptClient.getProcessTrackId(),null,null,null,null);
					
					// Searching for the client to get the name
					TbSystemUser su = em.find(TbSystemUser.class, pt.getProcessTrackReferencedId());
					
					String name = ". Cliente: " + su.getUserNames();
					
					if(su.getTbCodeType().getCodeTypeId().longValue() != 3L) {
						name += " " + su.getUserSecondNames();
					}
					
					TbProcessTrackDetailType dt = em.find(TbProcessTrackDetailType.class,
							ProcessTrackDetailType.CLIENT_CONSIGMENT_REGISTER.getId());
					
					// Creating the task.
					task.createTask(processDetailId, 0, new Timestamp(System.currentTimeMillis()), TimeUtil.calculateAds(dt.getAdsTime()) ,
							dt.getDetailTypePriority(), msg + name + ".", dt.getTbTaskType().getTaskTypeId(), idCreationUser, ip, 
							consignment.getConsignmentId());
					
				} else {
					// Log
					log.insertLog("No se encontró un proceso para el Cliente ID: "
							+ a.getTbSystemUser().getUserId(), LogReference.PROCESS,
							LogAction.NOTFOUND, ip, idCreationUser);
				}
				
				log.insertLog("Se Ha Creado la consignación ID: "
						+ consignment.getConsignmentId(), LogReference.CONSIGNMENT,
						LogAction.CREATE, ip, idCreationUser);
				
				return consignment.getConsignmentId();
			} else {
				log.insertLog("No se ha podido crear la concignación al cliente ID: "
						+ a.getTbSystemUser().getUserId() , LogReference.CONSIGNMENT,
						LogAction.CREATEFAIL, ip, idCreationUser);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en PurchaseEJB.saveConsignment ] ");
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Purchase#savePuchaseOrder(java.lang.Long, java.lang.Long,
	 * java.lang.String, java.lang.Long, java.lang.Long, java.util.List)
	 */
	@Override
	public PurchaseOrder savePuchaseOrder(Long idData, Long transNumber, String ip,
			Long creationUser, Long idStation, List<Order> detailList) {
		try {
			TbUserData data = em.find(TbUserData.class, idData);
			if (data != null) {
				
				emObj = new ObjectEM(em);
								
				// Creating the purchase Order.
				TbPurchaseOrder order = new TbPurchaseOrder();
				order.setPurchaseOrderDate(new Timestamp(System.currentTimeMillis()));
				order.setPurchaseOrderState(PurchaseOrderState.NEW.getId());
				order.setTbUserData(data);
				order.setTransactionsToDo(transNumber);
				order.setTbStationBO(em.find(TbStationBO.class, idStation));
				order.setPurchaseOrderValue(new BigDecimal(0));
				
				if (emObj.create(order)) { // If the Order is created
					// saving the log
					log.insertLog("Se ha creado la orden de pedido ID: " + order.getPurchaseOrderId(),
							LogReference.PURCHASEORDER, LogAction.CREATE, ip, creationUser);
					
					Query q = em
					.createNativeQuery("SELECT po.PURCHASE_ORDER_NUMBER FROM Tb_Purchase_Order po WHERE po.purchase_order_id = ?1");
					q.setParameter(1, order.getPurchaseOrderId());

					order.setPuchaseOrderNumber(Long.parseLong(q.getSingleResult().toString()));
					
					// Saving the detail,
					List<TbPurchaseOrderDetail> details = new ArrayList<TbPurchaseOrderDetail>();
					for (Order or : detailList) {
						if (or.getNumberOfOperations().longValue() > 0) {
							
							BigDecimal value = null;
							
				
							// Saving the details.
							for (int i = 0; i < or.getNumberOfOperations().longValue(); i++) {
								TbPurchaseOrderDetail detail = new TbPurchaseOrderDetail();
								detail.setDetailState(PurchaseOrderDetailState.NEW.getId()); 
								detail.setTbOperationType(em.find(TbOperationType.class, or.getOperType().getOperationTypeId()));
								detail.setTbPurchaseOrder(order);
								detail.setDetailOperationValue(value);
								if (!emObj.create(detail)) {
									log.insertLog("No Se ha podido crear el detalle de la orden de Pedido ID: "+ order
																	.getPurchaseOrderId() + ", Tipo Operación: " + or.getOperType()
																	.getOperationTypeName(), LogReference.PURCHASEORDERDETAIL,
																	LogAction.CREATEFAIL, ip, creationUser);
								} else {
									details.add(detail);
								}
							}
						}
					}				
					
					// creation of purchase order process 
					Long oderProcessId = process.createProcessTrack(ProcessTrackType.PURCHASE_ORDER, order
									.getPurchaseOrderId(), ip, creationUser);
				
					if (oderProcessId != null) {
						// Indicating that a purchase order has been created. Process detail type = 6
						process.createProcessDetail(oderProcessId, ProcessTrackDetailType.PURCHASE_ORDER_CREATION,
											"Se ha realizado una Orden de Pedido Número: "
										+ order.getPuchaseOrderNumber() + ".", creationUser, ip, 1, 
										" No Se ha podido crear el detalle del proceso ID: " + 
										oderProcessId + ", Tipo detalle: PURCHASE_ORDER_CREATION.",null,null,null,null);
					}	
					
					PurchaseOrder po = new PurchaseOrder();
					po.setOrder(order);
					po.setDetails(details);
					
					return po;
				} else {
					log.insertLog( "No se pudo crear la Orden de Pedido (No. Trans: "
									+ transNumber + ") del cliente ID Data: " + data.getUserDataId(),
							LogReference.PURCHASEORDER, LogAction.CREATEFAIL,
							ip, creationUser);
				}
			}
		} catch (Exception e) {
			System.out.println(" [ Error en PurchaseEJB.savePuchaseOrder ] ");
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Purchase#getRechargeStations()
	 */
	@Override
	public List<TbStationBO> getRechargeStations() {
		List<TbStationBO> list = new ArrayList<TbStationBO>();
		try {
			Query q = em
					.createQuery("SELECT ts FROM TbStationBO ts WHERE ts.tbStationType.stationTypeId = ?1");
			q.setParameter(1, StationType.RECHARGE_STATION.getId());
			for (Object obj : q.getResultList()) {
				list.add((TbStationBO) obj);
			}
		} catch (Exception e) {
			System.out
					.println(" [ Error en PurchaseEJB.getRechargeStations ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see ejb.Purchase#getOperationTypes()
	 */
	@Override
	public List<Order> getOperationTypes() {
		List<Order> list = new ArrayList<Order>();
		try {
			Query q = em.createQuery("SELECT ot FROM TbOperationType ot WHERE ot.operationTypeId = ?1 " +
					"ORDER BY ot.operationTypeId ");
			q.setParameter(1, OperationType.RECHARGE.getId());
			for (Object object : q.getResultList()) {
				Order order = new Order();
				order.setOperType((TbOperationType) object);
				order.setNumberOfOperations(0L);			
				list.add(order);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en PurchaseEJB.getOperationTypes ] ");
			e.printStackTrace();
		}
		return list;
	}

    /*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Purchase#getNewCardValue()
	 */
	@Override
	public Long getNewCardValue() {
		Long value = null;
		try {
			Query q = em
					.createQuery("SELECT sp FROM TbSystemParameter sp WHERE sp.systemParameterName = 'Valor Tarjeta Nueva'");
		TbSystemParameter sp = (TbSystemParameter) q.getSingleResult();
		return Long.parseLong(sp.getSystemParameterValue());
		} catch (Exception e) {
			System.out.println(" [ Error en PurchaseEJB.getNewCardValue ] ");
			e.printStackTrace();
		}
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Purchase#getConsigTypes()
	 */
	@Override
	public List<TbPayType> getPayTypes() {
		List<TbPayType> list = new ArrayList<TbPayType>();
		try {
			Query q = em.createQuery("SELECT ct FROM TbPayType ct ORDER BY ct.payTypeId");
			for (Object obj : q.getResultList()) {
				list.add((TbPayType) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en PurchaseEJB.getConsigTypes ] ");
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Purchase#getBanks()
	 */
	@Override
	public List<TbBank> getBanks() {
		List<TbBank> list = new ArrayList<TbBank>();
		try {
			for (Object obj : em.createQuery("SELECT tb FROM TbBank tb")
					.getResultList()) {
				list.add((TbBank) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en PurchaseEJB.getBanks ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Purchase#getAccounts()
	 */
	@Override
	public List<TbBankAccount> getAccounts() {
		List<TbBankAccount> list = new ArrayList<TbBankAccount>();
		try {
			for (Object obj : em.createQuery("SELECT ba FROM TbBankAccount ba")
					.getResultList()) {
				list.add((TbBankAccount) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en PurchaseEJB.getAccounts ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Purchase#getPurchaseOrderByState(java.lang.Long,
	 * java.lang.Integer, boolean)
	 */
	@Override
	public PurchaseOrder getPurchaseOrderByState(Long orderNumber,
			PurchaseOrderState state, boolean searchBystate) {
		try {
			PurchaseOrder order = null;
			
			String query = "SELECT po FROM TbPurchaseOrder po WHERE  po.puchaseOrderNumber = " + orderNumber;
			
			if(searchBystate){
				query += " AND po.purchaseOrderState = " + state.getId();
			}
			
			Query q = em.createQuery(query);
			TbPurchaseOrder po = (TbPurchaseOrder) q.getSingleResult();
			
			q = em
					.createQuery("SELECT pod FROM TbPurchaseOrderDetail pod WHERE pod.tbPurchaseOrder. purchaseOrderId = ?1");
			q.setParameter(1, po.getPurchaseOrderId());
			
			List<TbPurchaseOrderDetail> details = new ArrayList<TbPurchaseOrderDetail>();
			List<OrderDetail> toDetail = new ArrayList<OrderDetail>();
			
			boolean account = false;
			boolean transfer = false;
			
			BigDecimal valueTo = new BigDecimal(0);
			
			for (Object obj : q.getResultList()) {
				TbPurchaseOrderDetail pod = (TbPurchaseOrderDetail) obj;
									
				if (pod.getDetailOperationValue() != null) {
					valueTo = valueTo.add(pod.getDetailOperationValue());
				}
				OrderDetail od = new OrderDetail();
				od.setDetail(pod);
				if (pod.getTbDevice() != null) {
					od.setIdDevice(pod.getTbDevice().getDeviceId());
				}
				if (pod.getTbStationBO() != null) {
					od.setIdStation(pod.getTbStationBO().getStationBOId());
				}
				if (pod.getDetailOperationValue() != null) {
					od.setValueTo(pod.getDetailOperationValue().longValue());
				}
				toDetail.add(od);
				
				details.add(pod);
				
				if(pod.getTbOperationType().getOperationTypeId().longValue() == 4L){
					transfer = true; 
				} else {
					account = true;
				}
			}
			
			Query qa = em.createQuery("SELECT ra.tbAccount FROM ReAccountPurchaseOrder ra " +
					"WHERE ra.tbPurchaseOrder.purchaseOrderId = ?1");
			qa.setParameter(1, po.getPurchaseOrderId());
			order = new PurchaseOrder();
			boolean showAccountDetails;
			try{
				order.setTbAccount((TbAccount) qa.getSingleResult());
				account = false;
				showAccountDetails = true;
			} catch (Exception e) {
				showAccountDetails = false;
			}
			order.setAccount(account);
			order.setTransfer(transfer);
			order.setOrder(po);
			order.setDetails(details);
			order.setToDetailList(toDetail);
			if(showAccountDetails){
				order.setValueToAsign(transaction.getAvailableBalanceAccount(order.getTbAccount().getAccountId()) - valueTo.longValue());
			} else {
				order.setValueToAsign(0L);
			}
			order.setShowAccountDetail(showAccountDetails);
			order.setValueTo(valueTo.longValue());
			
			// if transfer or consignment are false then allow operations value detail.
			if(!transfer && !account){
				order.setAllowDetail(true);
			}
			return order;
		} catch (NoResultException nr) {
			System.out.println(" [ No se encontró la Orden de Pedido No." + orderNumber +", con estado: "+ state.getId() +" ] ");
			return null;
		} catch (Exception e) {
			System.out.println(" [ Error en PurchaseEJB.getPurchaseOrderByState ] ");
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Purchase#deleteDetail(java.lang.Long, java.lang.String,
	 * java.lang.Long)
	 */
	@Override
	public boolean deleteDetail(Long idDetail, String ip, Long creationUser) {
		try {
			TbPurchaseOrderDetail d = em.find(TbPurchaseOrderDetail.class, idDetail);
			
			if (d != null) {
				
				emObj = new ObjectEM(em);
				Long numOrder = d.getTbPurchaseOrder().getPuchaseOrderNumber();
				String type = d.getTbOperationType().getOperationTypeName();
				
				if (emObj.delete(d)) {
					log.insertLog("Se ha Eliminado un detalle de la Orden de Pedido No. "
							+ numOrder + ", Tipo: " + type, LogReference.PURCHASEORDERDETAIL, LogAction.DELETE,
							ip, creationUser);
					return true;
				} else {
					log.insertLog("No se ha podido eliminar el detalle de la Orden de Pedido No. "
									+ numOrder + ", Tipo: " + type, LogReference.PURCHASEORDERDETAIL, LogAction.DELETEFAIL,
									ip, creationUser);
				}
			}
		} catch (Exception e) {
			System.out.println(" [ Error en PurchaseEJB.deleteDetail ] ");
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Purchase#getListOfOperationTypes()
	 */
	@Override
	public List<TbOperationType> getListOfOperationTypes() {
		List<TbOperationType> list = new ArrayList<TbOperationType>();
		try {
			Query q = em.createQuery("SELECT ot FROM TbOperationType ot WHERE ot.operationTypeId = ?1 " +
				"ORDER BY ot.operationTypeId ");
			q.setParameter(1, OperationType.RECHARGE.getId());
			for (Object obj : q.getResultList()) {
				list.add((TbOperationType) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en PurchaseEJB.getListOfOperationTypes ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Purchase#addOrderDetail(java.lang.Long, java.lang.Long,
	 * java.lang.String, java.lang.Long)
	 */
	@Override
	public boolean addOrderDetail(Long idType, Long idOrder, String ip,
			Long creationUser) {
		try {
			TbPurchaseOrderDetail detail = new TbPurchaseOrderDetail();
			detail.setDetailState(PurchaseOrderDetailState.NEW.getId());
			detail.setTbPurchaseOrder(em.find(TbPurchaseOrder.class, idOrder));
			detail.setTbOperationType(em.find(TbOperationType.class, idType));
			
//			if(idType.longValue() == 2 ||
//					idType.longValue() == 3){
//				Long value = null;
//				value = this.getNewCardValue();
//				detail.setDetailOperationValue(new BigDecimal(value));
//			}
			
			emObj = new ObjectEM(em);
			
			if (emObj.create(detail)) {
				log.insertLog(
						"Se ha creado un detalle de la Orden de Pedido No. "
								+ em.find(TbPurchaseOrder.class, idOrder)
										.getPuchaseOrderNumber()
								+ ", Tipo: " + em.find(TbOperationType.class, idType)
										.getOperationTypeName(),
						LogReference.PURCHASEORDERDETAIL, LogAction.CREATE, ip,
						creationUser);
				return true;
			} else {
				log.insertLog(
						"No se ha podido crear un detalle de la Orden de Pedido No. "
								+ em.find(TbPurchaseOrder.class, idOrder)
										.getPuchaseOrderNumber()
								+ ", Tipo: " + em.find(TbOperationType.class, idType)
										.getOperationTypeName(),
						LogReference.PURCHASEORDERDETAIL, LogAction.CREATEFAIL, ip,
						creationUser);
			}
		} catch (Exception e) {
			System.out.println(" Error en PurchaseEJB.addOrderDetail ");
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Purchase#changeOrderClientOffice(java.lang.Long, java.lang.Long,
	 * java.lang.String, java.lang.Long)
	 */
	@Override
	public boolean changeOrderClientOffice(Long idData, Long idOrder,
			String ip, Long creationUser) {
		try {
			TbPurchaseOrder order = em.find(TbPurchaseOrder.class, idOrder);
			if( order != null){
				TbUserData u = em.find(TbUserData.class, idData);
				if (u != null) {
					emObj = new ObjectEM(em);
					String oldOfficeName = order.getTbUserData()
							.getUserDataOfficeName();
					order.setTbUserData(u);
					
					if(emObj.update(order)){
						log.insertLog("Se ha modificado la dependencia de la Orden de Pedido No. "
										+ order.getPuchaseOrderNumber()
										+ ", Nombre Dependencia Anterior: " + oldOfficeName,
								LogReference.PURCHASEORDER, LogAction.UPDATE, ip,
								creationUser);
						return true;
					}else{
						log.insertLog("Se ha podido modificar la dependencia de la Orden de Pedido No. "
								+ order.getPuchaseOrderNumber()
								+ ", Nombre Dependencia Nueva: " + u.getUserDataOfficeName(),
						LogReference.PURCHASEORDER, LogAction.UPDATEFAIL, ip,
						creationUser);
					}
				}
			}
		} catch (Exception e) {
			System.out.println(" [ Error en PurchaseEJB.changeOrderClientOffice ] ");
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Purchase#saveOrderConsignment(java.lang.Long, java.lang.Long,
	 * java.lang.Long, java.lang.String)
	 */
	@Override
	public boolean saveOrderAccount(Long idOrder, Long idcreationUser,
			Long accountId, String ip) {
		try {
			
			// Searching for the purchase order.
			TbPurchaseOrder order = em.find(TbPurchaseOrder.class, idOrder);
			emObj = new ObjectEM(em);
			
			if (order != null) { // If found
				
					// Then, doing the assignment of that consignment to the order.
					// Searching for the consignment
					TbAccount a = em.find(TbAccount.class, accountId);

					ReAccountPurchaseOrder roc = new ReAccountPurchaseOrder();
					roc.setTbAccount(a);
					roc.setTbPurchaseOrder(order);
					
					if (emObj.create(roc)) { // Creating the relation between the account  and the order.
						log.insertLog("Se ha creado la realción orden de pedido ID: "
								+ idOrder + " con la cuenta ID: "+ accountId, LogReference.PURCHASEORDER,
								LogAction.UPDATE, ip, idcreationUser);
						return true;	
					} else {
						log.insertLog("No se pudo crear la realción orden de pedido ID: "
								+ idOrder + " con la cuenta ID: "+ accountId, LogReference.PURCHASEORDER,
								LogAction.UPDATEFAIL, ip, idcreationUser);
					}			
			} else {
				log.insertLog("Guardar Cuenta Orden | No se encontró la orden ID.  " + idOrder,
						LogReference.PURCHASEORDER, LogAction.NOTFOUND, ip,
						idcreationUser);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en PurchaseEJB.saveOrderAccount ] ");
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Purchase#updateDetail(java.lang.Long, java.lang.Long,
	 * java.lang.Long, java.lang.Long, java.lang.String, java.lang.Long)
	 */
	@Override
	public boolean updateDetail(Long idDetail, Long idDevice, Long idStation, Long value,
			String ip, Long creationUser) {
		try {
			TbPurchaseOrderDetail d = em.find(TbPurchaseOrderDetail.class,
					idDetail);
			if (d != null) {
				emObj = new ObjectEM(em);
				d.setDetailOperationValue(new BigDecimal(value));
				d.setTbDevice(em.find(TbDevice.class, idDevice));
				d.setTbStationBO(em.find(TbStationBO.class, idStation));
				
				if (emObj.update(d)) {
					log.insertLog(
							"Modificar Detalle Orden | Se ha modificador el detalle ID: " + idDetail,
							LogReference.PURCHASEORDERDETAIL, LogAction.UPDATE, ip, creationUser);
					return true;
				} else {
					log.insertLog(
							"Modificar Detalle Orden | No se pudo modificar el detalle ID: " + idDetail,
							LogReference.PURCHASEORDERDETAIL, LogAction.UPDATEFAIL, ip, creationUser);
				}
			}
		} catch (Exception e) {
			System.out.println(" [ Error en PurchaseEJB.updateDetail ] ");
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Purchase#orderSendTreasury(java.lang.Long, java.lang.String,
	 * java.lang.Long, java.lang.String, java.lang.Long, java.lang.Long)
	 */
	@Override
	public boolean orderSendTreasury(Long idOrder, String ip,
			Long creationUser, String invoiceNumber, Long orderValue, Long accountId) {//TODO
		try {
			TbPurchaseOrder o = em.find(TbPurchaseOrder.class, idOrder);
			if (o != null) {
				o.setPurchaseOrderState(PurchaseOrderState.DETAILED.getId());
				o.setInvoiceNumber(invoiceNumber);
				o.setPurchaseOrderValue(new BigDecimal(orderValue));
				
				emObj = new ObjectEM(em);
				if (emObj.update(o)) {
					log.insertLog("Guardar Detallado Orden de Pedido | " +
							"Se ha cambiado a 'Detallada' el estado de la orden ID: " + idOrder,
							LogReference.PURCHASEORDER, LogAction.UPDATE, ip, creationUser);
					
					// Searching the process to indicate 
					TbProcessTrack pt = process.searchProcess(ProcessTrackType.PURCHASE_ORDER, o
									.getPurchaseOrderId());
					
					if (pt != null) {// If found any (should find one), then create the new detail.
						process.createProcessDetail(pt.getProcessTrackId(), ProcessTrackDetailType.PURCHASE_ORDER_DETAIL_DONE,
								"Se ha detallado la orden de pedido No. " + o.getPuchaseOrderNumber() +
								", se puede proceder a realizar las recargas correspondientes.",
								creationUser, ip, 1, "No se ha indicado en proceso que se ha detallado la orden No. "+ o.getPuchaseOrderNumber()
								+ ". ID. Proceso :" + pt.getProcessTrackId()+".",null,null,null,null);
					}
					return true;
				}else{
					log.insertLog("Guardar Detallado Orden de Pedido | No se pudo cambiar el estado de la orden ID: " + idOrder,
							LogReference.PURCHASEORDER, LogAction.UPDATEFAIL, ip,
							creationUser);
				}
			}
		} catch (Exception e) {
			System.out.println(" [ Error en PurchaseEJB.orderSendTreasury ] ");
			e.printStackTrace();
		}
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Purchase#getConsignmentByState(constant.ConsignmentState)
	 */
	@Override
	public List<TbConsignment> getConsignmentByState(ConsignmentState state) {
		List<TbConsignment> list = new ArrayList<TbConsignment>();
		try {
			Query q = em
					.createQuery("SELECT tc FROM TbConsignment tc WHERE tc.tbConsignmentState.consignmentStatetId = ?1");
			q.setParameter(1, state.getId());
			for (Object obj : q.getResultList()) {
				list.add((TbConsignment) obj);
			}
		} catch (Exception e) {
			System.out
					.println(" [ Error en PurchaseEJB.getConsignmentByState ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Purchase#approve(java.lang.Long, java.lang.String,
	 * java.lang.Long)
	 */
	@Override
	public boolean approve(Long idConsig, String ip, Long creationUser) {
		try {
			TbConsignment c = em.find(TbConsignment.class, idConsig);
			if (c != null) {
				
				c.setTbConsignmentState(em.find(TbConsignmentState.class,
						ConsignmentState.APPROVED.getId())); // Approved by  treasury.
				emObj = new ObjectEM(em);
				
				if (emObj.update(c)) {
					log.insertLog("Aprobar Consignación | Se ha cambiado a 1 el estado de la consignación  ID:  " + idConsig + ".",
							LogReference.CONSIGNMENT, LogAction.UPDATE, ip, creationUser);
					
					// Updating balance of the account
					TbAccount a = em.find(TbAccount.class, c.getTbAccount()
							.getAccountId());
					Long previous = a.getAccountBalance().longValue();
					a.setAccountBalance(a.getAccountBalance().add(new BigDecimal(c.getConsignmentValue())));
					em.merge(a);
					em.flush();
					
					log.insertLog("Aprobar Consignación | Cambio saldo cuenta: Anterior : " + previous + ", Actual: " + a.getAccountBalance() + ".", 
							LogReference.ACCOUNT, LogAction.UPDATE, ip, creationUser);
					
					transaction.saveAccountTransaction(a.getAccountId(), c.getConsignmentId(), null, TransactionType.CREDIT,
							 "Consignación Aprobada" +  " - Tipo de pago: "+ c.getTbPayType().getPayTypeName() + ".",
							 c.getConsignmentValue(), ip, creationUser, previous, a.getAccountBalance().longValue(), null,
							 null, null, null);
					
					/* Se realiza recarga de dispositivos de acuerdo al tipo de distribucion de la cuenta */
					/* ablasquez 21/08/12
					 * 
					 */
					if(c.getConsignmentConcept().contains("RECARGA DE DISPOSITIVO")){
						distributionBalance(a,c.getConsignmentValue(),ip, creationUser);	
					}					
					
					
					/*------------------------------------------------------------------------------------*/
					
					
					// Searching the process of client
					TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, c.getTbAccount().getTbSystemUser().getUserId());
					TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, c.getTbAccount().getTbSystemUser().getUserId());
					
					if (pt != null) {// If found any (should find one), then create the new detail.
						
						process.createProcessDetail(pt.getProcessTrackId(), ProcessTrackDetailType.CLIENT_CONSIGMENT_APPROVED,
								"Se ha Aprobado la consignación Por valor de: $"	+ c.getConsignmentValue() + ".",
								creationUser, ip, 1, "No se ha indicado en el proceso que se ha aprobado la consignación ID: "+ idConsig
								+ ". ID. Proceso :" + pt.getProcessTrackId()+".",null,null,null,null);
						
						process.createProcessDetail(ptc.getProcessTrackId(), ProcessTrackDetailType.CLIENT_CONSIGMENT_APPROVED,
								"Se ha Aprobado la consignación Por valor de: $"	+ c.getConsignmentValue() + ".",
								creationUser, ip, 1, "No se ha indicado en el proceso que se ha aprobado la consignación ID: "+ idConsig
								+ ". ID. Proceso :" + ptc.getProcessTrackId()+".",null,null,null,null);
					}
					
					// Searching for the task to Update 
					Query q = em.createQuery("SELECT ta FROM TbTask ta WHERE ta.referencedId = ?1");
					q.setParameter(1, c.getConsignmentId());
					
					TbTask ta = (TbTask) q.getSingleResult();
					ta.setTaskState(1);
					ta.setTaskActive(false);
					ta.setTaskUpdateDate(new Timestamp(System.currentTimeMillis()));
					em.merge(ta);
					em.flush();
					
					// Send Notification to Client.
					/*sendMail.sendMail(EmailType.CLIENT, c.getTbAccount().getTbSystemUser().getUserEmail(), 
							EmailSubject.CLIENT_APPROVE_CONSIGNMENT_NOTIFY,  "\\$" + c.getConsignmentValue());*/
					
					return true;
				} else {
					log.insertLog("Aprobar Consignación | No se pudo cambiar el estado de la consignación ID: " + idConsig + ".",
							LogReference.CONSIGNMENT, LogAction.UPDATEFAIL, ip, creationUser);
				}
			}
		} catch (Exception e) {
			System.out.println(" [ Error en PurchaseEJB.approve ] ");
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Purchase#getPurchaseOrdersByState(java.lang.Long)
	 */
	@Override
	public List<PurchaseOrder> getPurchaseOrdersByState(PurchaseOrderState state) {
		List<PurchaseOrder> list = new ArrayList<PurchaseOrder>();
		try {
			Query q = em.createQuery("SELECT po FROM TbPurchaseOrder po WHERE po.purchaseOrderState = ?1");
			q.setParameter(1, state.getId());
			for (Object obj : q.getResultList()) {
				list.add(this.getPurchaseOrderByState(((TbPurchaseOrder) obj)
						.getPuchaseOrderNumber(), state, true));
			}
		} catch (Exception e) {
			System.out.println(" [ Error en PurchaseEJB.getPurchaseOrdersByState ] ");
			e.printStackTrace();
		}
		return list;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Purchase#confirmOrder(java.lang.Long, java.lang.Long,
	 * java.lang.String)
	 */
	@Override
	public boolean confirmOrder(Long idOrder, Long creationUser, String ip) {
		try {
			TbPurchaseOrder po = em.find(TbPurchaseOrder.class, idOrder);
			
			if (po != null) {
				po.setPurchaseOrderState(3); // confirm by warehouse
				
				emObj = new ObjectEM(em);
				
				if(emObj.update(po)){
					log.insertLog("Confirmar Orden de Pedido | Se ha cambiado a 3(Confirmación del Almacén) el estado de la Oden   ID:  " + idOrder + ".",
							LogReference.PURCHASEORDER, LogAction.UPDATE, ip, creationUser);
					return true;
				} else{
					log.insertLog("Confirmar Orden de Pedido | No se pudo cambiar el estado a 3(Confirmación del Almacén) de la Oden ID: " + idOrder + ".",
							LogReference.PURCHASEORDER, LogAction.UPDATEFAIL, ip, creationUser);
				}
			}
		} catch (Exception e) {
			System.out.println(" [ Error en PurchaseEJB.confirmOrder ] ");
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Purchase#getRechageList()
	 */
	@Override
	public List<ToRecharge> getRechageList() {
		List<ToRecharge> list = new ArrayList<ToRecharge>();
		try {
			Query q = em.createQuery("SELECT pd FROM TbPurchaseOrderDetail pd WHERE pd.detailState = ?1");
			q.setParameter(1, PurchaseOrderDetailState.READY_TO_RECHARGE.getId());
			for (Object obj : q.getResultList()) {
				ToRecharge recharge = new ToRecharge();
				recharge.setDetail((TbPurchaseOrderDetail) obj);
				list.add(recharge);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en PurchaseEJB.getRechageList ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Purchase#recharge(util.device.ToRecharge, java.lang.Long,
	 * java.lang.Long, java.lang.String, java.lang.Long)
	 */
	@Override
	public boolean recharge(ToRecharge toRechargeObject, Long oldBalance,  Long newBalance,
			String ip, Long creationUser) {
		try {
			
			TbPurchaseOrderDetail detail = em.find(TbPurchaseOrderDetail.class,
					toRechargeObject.getDetail().getPurchaseOrderDetailId());
			emObj = new ObjectEM(em);
			
			// Updating the state of the purchase order detail.
			detail.setDetailState(PurchaseOrderDetailState.PROCESS_FINISHED.getId()); // Final state of detail in Process.
			detail.setRechargeDate(new Timestamp(System.currentTimeMillis()));
			
			if(emObj.update(detail)){
				
				log.insertLog("Recargar  | Se Ha actualizado el estado a 'finalizado Proceso' del detalle ID: " + detail.getPurchaseOrderDetailId() ,
						LogReference.PURCHASEORDERDETAIL, LogAction.UPDATE, ip, creationUser);
				
				// Then, updating the balance in the device Object.
				TbDevice device = em.find(TbDevice.class, detail.getTbDevice().getDeviceId());
				Long oldBalanceDev = device.getDeviceCurrentBalance().longValue();
				device.setDeviceCurrentBalance(new BigDecimal(newBalance));
				
				if(emObj.update(device)){
					
					//save log.
					log.insertLog("Recargar | Se ha actualizado el valor del nuevo saldo en el dispositivo ID: " + device.getDeviceId() + 
							"Saldo anterior BaseDatos dispositivo: "+oldBalanceDev +". Saldo anterior tarjeta: " + oldBalance + ", nuevo saldo: " + newBalance + ".",
							LogReference.DEVICE, LogAction.UPDATE, ip, creationUser);
					
					// Creating the transaction
										
					try{
						TbTransaction transaction = new TbTransaction();
						transaction.setBackOfficeTime(new Timestamp(System.currentTimeMillis()));
						transaction.setNewBalance(newBalance);
						transaction.setPreviousBalance(oldBalance);
						transaction.setTbDevice(device);
						transaction.setTransactionTime(new Timestamp(System.currentTimeMillis()));
						transaction.setTbTransactionType(em.find(TbTransactionType.class, TransactionType.CREDIT.getId()));
						transaction.setTransactionValue(detail.getDetailOperationValue().longValue());
						transaction.setTbStationBO(em.find(TbStationBO.class, detail.getTbStationBO().getStationBOId()));
						
						if(emObj.create(transaction)){
							
							// save log.
							log.insertLog("Recarga | Se ha creado la transacción ID: "+ transaction.getTransactionId() +".",
									LogReference.TRANSACTION, LogAction.CREATE, ip, creationUser);
							
							// Indication in the device process track.
							TbProcessTrack  p = process.searchProcess(ProcessTrackType.DEVICE, device.getDeviceId());
							
							if (p != null) {
								// save the detail 
								process.createProcessDetail(p.getProcessTrackId(), ProcessTrackDetailType.DEVICE_RECHARGE, 
										"Se ha hecho una recarga al dispositivo por valor: $" +new DecimalFormat("#,###,###,###").format(detail.getDetailOperationValue()) ,
										creationUser, ip, 1, "No se ha podido registar en el proceso que Se ha hecho una recarga al dispositivo por valor: $" + 
										new DecimalFormat("#,###,###,###").format(detail.getDetailOperationValue()) + ". Proceso ID: " + p.getProcessTrackId(),null,null,null,null);
							} else {
								log.insertLog("Recarga | El Dispositivo ID: " + device.getDeviceId() + " no tiene proceso asociado.",
										LogReference.PROCESS, LogAction.NOTFOUND, ip, creationUser);
							}
							
							// Sending email to Client . 	
							/*sendMail.sendMail(EmailType.CLIENT, detail.getTbPurchaseOrder().getTbUserData().getTbSystemUser().getUserEmail(),
									EmailSubject.CLIENT_CARD_RECHARGE_NOTIFY, "\\$" + detail.getDetailOperationValue());*/
							
							return true;
						} else {
							
							//save log failure.
							log.insertLog("Recarga | No se pudo guardar la transacción, valores: "+
									"Fecha: " + transaction.getTransactionTime() +
									", Nuevo saldo: " + newBalance + ", Saldo anterior: " + oldBalance + ". Estación BO ID: " + detail.getTbStationBO().getStationBOId() + 
									". Tipo : Crédito, Valor: " + transaction.getTransactionValue() + "." ,
									LogReference.TRANSACTION, LogAction.CREATEFAIL, ip, creationUser);
						}
						
					} catch (NoResultException e) {
						// save log failure. 
						log.insertLog("Recarga | No se pudo guardar la transacción asociada a la estación ingresada.  valores: "+
									"Fecha: " + new Timestamp(System.currentTimeMillis())+ ", Nuevo saldo: " + newBalance + ", Saldo anterior: " + oldBalance + ". Estacion ID: " + 
									detail.getTbStationBO().getStationBOId() + ". Tipo : Crédito, Valor: " + detail.getDetailOperationValue()+ "." ,
								    LogReference.TRANSACTION, LogAction.CREATEFAIL, ip, creationUser);
					}
					
				} else{
					//save log failure.
					log.insertLog("Recargar | No se pudo actualizar el valor del nuevo saldo en el dispositivo ID: " + device.getDeviceId() + 
							". Saldo anterior Tarjeta: " + oldBalance + ", nuevo saldo: " + newBalance + ".",
							LogReference.DEVICE, LogAction.UPDATEFAIL, ip, creationUser);
				}
				
			} else {
				log.insertLog("Recargar  | No se pudo actualizar el estado a 3 del detalle ID: " + detail.getPurchaseOrderDetailId() ,
						LogReference.PURCHASEORDERDETAIL, LogAction.UPDATEFAIL, ip, creationUser);
			}
			
		} catch (Exception e) {
			System.out.println(" [ Error en PurchaseEJB.recharge ] ");
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see ejb.Purchase#getOrderClients()
	 */
	@Override
	public List<TbSystemUser> getOrderClients() {
		List<TbSystemUser> list = new ArrayList<TbSystemUser>();
		try {
			Query q = em.createQuery("SELECT DISTINCT po.tbUserData.tbSystemUser  FROM TbPurchaseOrder po ");
			for(Object obj: q.getResultList()){
				list.add((TbSystemUser) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en PurchaseEJB.getOrderClients ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see ejb.Purchase#getOrderDevices()
	 */
	@Override
	public List<TbDevice> getOrderDevices() {
		List<TbDevice> list = new ArrayList<TbDevice>();
		try {
			Query q = em.createQuery("SELECT DISTINCT pod.tbDevice  FROM TbPurchaseOrderDetail pod ");
			for (Object obj : q.getResultList()) {
				list.add((TbDevice) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en PurchaseEJB.getOrderDevices ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Purchase#histoConsult(java.lang.Long, boolean, java.util.Date,
	 * java.util.Date, boolean, java.lang.Long, boolean, java.lang.Long,
	 * boolean, java.lang.Long)
	 */
	@Override
	public List<TbPurchaseOrderDetail> histoConsult(Long idOperationType,
			boolean checkDate, Date begDate, Date endDate, boolean checkClient,
			Long idClient, boolean checkTisc, Long idDevice,
			boolean checkValue, Long value) {
		List<TbPurchaseOrderDetail> list = new ArrayList<TbPurchaseOrderDetail>();
		try {
			String query = "SELECT od FROM TbPurchaseOrderDetail od WHERE  " +
					"  od.detailState = 3 AND od.tbOperationType.operationTypeId =  ?1 " ;
				
			if(idOperationType.longValue() == 1L){
				if(checkDate){
					query+= " AND od.rechargeDate BETWEEN  ?2 AND ?3 ";
				}
			}
			
			if(checkClient) {
				query += " AND od.tbPurchaseOrder.tbUserData.tbSystemUser.userId = ?4 ";
			}
			
			if(checkTisc){
				query += " AND od.tbDevice.deviceId = ?5 ";
			}
			
			if(checkValue){
				query+= " AND od.detailOperationValue = ?6 ";
			}
			
			query += " ORDER BY od.tbPurchaseOrder.puchaseOrderNumber ";
			
			Query q = em.createQuery(query);
			q.setParameter(1, idOperationType);
			if(idOperationType.longValue() == 1L){
				if(checkDate){
					System.out.println("enters");
					q.setParameter(2, new Timestamp(begDate.getTime()));
					q.setParameter(3, new Timestamp(endDate.getTime()));
				}
			}
			if(checkClient) {
				q.setParameter(4, idClient);
			}
			
			if(checkTisc){
				q.setParameter(5, idDevice);
			}
			
			if(checkValue){
				q.setParameter(6, new BigDecimal(value));
			}
						
			for (Object obj : q.getResultList()) {
				list.add((TbPurchaseOrderDetail) obj);
			}
			
		} catch (Exception e) {
			System.out.println(" [ Error en PurchaseEJB.histoConsult ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see ejb.Purchase#reject(java.lang.Long, java.lang.String, java.lang.Long)
	 */
	@Override
	public boolean reject(Long idConsig, String ip, Long creationUser) {  
		try {
			TbConsignment c = em.find(TbConsignment.class, idConsig);
			if (c != null) {
				
				c.setTbConsignmentState(em.find(TbConsignmentState.class,
						ConsignmentState.REJECTED.getId()));
				emObj = new ObjectEM(em);
				
				if (emObj.update(c)) {
					log.insertLog("Rechazar Consignación | Se ha cambiado a 2 el estado de la consignación  ID:  " + idConsig + ".",
							LogReference.CONSIGNMENT, LogAction.UPDATE, ip, creationUser);
					
					// Searching the process of client
					TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, c.getTbAccount().getTbSystemUser().getUserId());
					TbProcessTrack ptClient = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, c.getTbAccount().getTbSystemUser().getUserId());
					
					if (pt != null) {// If found any (should find one), then create the new detail.
						
						process.createProcessDetail(pt.getProcessTrackId(), ProcessTrackDetailType.CLIENT_CONSIGMENT_REJECTION,
								"Se ha Rechazado la consignación Por valor de: $"	+ c.getConsignmentValue() + ". Por concepto de: " + c.getConsignmentConcept() + "." ,
								creationUser, ip, 1, "No se ha indicado en el proceso que se ha rechazado la consignación ID: "+ idConsig
								+ ". ID. Proceso :" + pt.getProcessTrackId()+".",null,null,null,null);
						
						process.createProcessDetail(ptClient.getProcessTrackId(), ProcessTrackDetailType.CLIENT_CONSIGMENT_REJECTION,
								"Se ha Rechazado la consignación Por valor de: $"	+ c.getConsignmentValue() + ". Por concepto de: " + c.getConsignmentConcept() + ".",
								creationUser, ip, 1, "No se ha indicado en el proceso que se ha rechazado la consignación ID: "+ idConsig
								+ ". ID. Proceso :" + ptClient.getProcessTrackId()+".",null,null,null,null);
					}
					
					// Searching for the task to Update 
					Query q = em.createQuery("SELECT ta FROM TbTask ta WHERE ta.referencedId = ?1");
					q.setParameter(1, c.getConsignmentId());
					
					TbTask ta = (TbTask) q.getSingleResult();
					ta.setTaskState(1);
					ta.setTaskActive(false);
					ta.setTaskUpdateDate(new Timestamp(System.currentTimeMillis()));
					em.merge(ta);
					em.flush();
					
					// Send Notification to Client.
					/*sendMail.sendMail(EmailType.CLIENT, c.getTbAccount().getTbSystemUser().getUserEmail(), 
							EmailSubject.CLIENT_REJECT_CONSIGNMENT_NOTIFY,  "\\$" + c.getConsignmentValue() + ". Por concepto de: " + c.getConsignmentConcept() + ".");
					*/
					return true;
				} else {
					log.insertLog("Rechazar Consignación | No se pudo cambiar el estado de la consignación ID: " + idConsig + ".",
							LogReference.CONSIGNMENT, LogAction.UPDATEFAIL, ip, creationUser);
				}
			}
		} catch (Exception e) {
			System.out.println(" [ Error en PurchaseEJB.reject ] ");
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ejb.Purchase#getConsignmentByStateAndClient(constant.ConsignmentState,
	 * java.lang.Long)
	 */
	@Override
	public List<TbConsignment> getConsignmentByStateAndClient(
			ConsignmentState state, Long clientId) {
		List<TbConsignment> list = new ArrayList<TbConsignment>();
		try {
			Query q = em
					.createQuery("SELECT tc FROM TbConsignment tc WHERE  tc.tbConsignmentState.consignmentStatetId = ?1" +
							" AND tc.tbAccount.tbSystemUser.userId = ?2 ");
			q.setParameter(1, state.getId());
			q.setParameter(2, clientId);
			for (Object obj : q.getResultList()) {
				list.add((TbConsignment) obj);
			}
		} catch (Exception e) {
			System.out
					.println(" [ Error en PurchaseEJB.getConsignmentByStateAndClient ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Purchase#getConsignmentByClient(constant.ConsignmentState,
	 * java.lang.Long)
	 */
	@Override
	public List<TbConsignment> getConsignmentByClient(Long clientId) {
		List<TbConsignment> list = new ArrayList<TbConsignment>();
		try {
			Query q = em
					.createQuery("SELECT tc FROM TbConsignment tc WHERE " +
							"  tc.tbAccount.tbSystemUser.userId = ?1 ORDER BY tc.consignmentRegisterDate");
			q.setParameter(1, clientId);
			for (Object obj : q.getResultList()) {
				list.add((TbConsignment) obj);
			}
		} catch (Exception e) {
			System.out
					.println(" [ Error en PurchaseEJB.getConsignmentByClient ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Purchase#savePayment(java.lang.Long, java.lang.Long,
	 * java.lang.Long, java.lang.String, java.lang.Long)
	 */
	@Override
	public boolean savePayment(Long accoundId, Long deviceId, Long value,
			String ip, Long creationUser) {
		try {
			//start
			// search for the account
			TbAccount ac = em.find(TbAccount.class, accoundId);
			// Discount the value of device from account balance
			Long previous = ac.getAccountBalance().longValue();
			ac.setAccountBalance(ac.getAccountBalance().subtract(
					new BigDecimal(value)));
			
			//updating the account
			emObj = new ObjectEM(em);
			if (emObj.update(ac)) {
				
				// save log
				log.insertLog("Guardar Pago de Dispositivo | Actualización de saldo en cuenta. Nuevo valor: \\$"
						+ ac.getAccountBalance() + ", ID Account: " + accoundId + " valor compra del tag :" + value,
						LogReference.ACCOUNT, LogAction.UPDATEFAIL, ip, creationUser);
				
				// searching the device
				TbDevice device = em.find(TbDevice.class, deviceId);
				
				// Register the account transaction
				transaction.saveAccountTransaction(accoundId, null, deviceId, TransactionType.DEBIT,
						"Pago reposición de dispositivo.  " + device.getDeviceFacialId() + ".", 
						value, ip, creationUser, previous, ac.getAccountBalance().longValue(), null, null, null, null);
				
				// updating device
				device.setDevicePaid(true);
				em.merge(device);
				em.flush();
				
				transitTask.createTask(TypeTask.ACCOUNT, accoundId.toString());
				
				// Save into client process and device process.
				// Searching the process of client and device process
				TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, ac.getTbSystemUser().getUserId());
				TbProcessTrack ptClient = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, ac.getTbSystemUser().getUserId());
				TbProcessTrack ptDevice = process.searchProcess(ProcessTrackType.DEVICE, deviceId);
				
				String plate= enrollDevice.getPlateByDevice(accoundId,deviceId);
				
				String message = "Se realiza el pago por reposición al Dispositivo Electrónico TAG ID Facial "+device.getDeviceFacialId() +
				                      " vinculado a la placa "+plate + " de la Cuenta FacilPass No "+accoundId;
				
				if (pt != null) {// If found any (should find one), then create the new detail.
					
					process.createProcessDetail(pt.getProcessTrackId(), ProcessTrackDetailType.CLIENT_DEVICE_PAY,
							message, 	creationUser, ip, 1, "No se ha indicado en el proceso : " + message + ". ID. Proceso :"
							+ pt.getProcessTrackId()+".",null,null,null,null);
					
					process.createProcessDetail(ptClient.getProcessTrackId(), ProcessTrackDetailType.CLIENT_DEVICE_PAY,
							message, 	creationUser, ip, 1, "No se ha indicado en el proceso : " + message + ". ID. Proceso :"
							+ ptClient.getProcessTrackId()+".",null,null,null,null);
					
					if(ptDevice!= null){
						process.createProcessDetail(ptDevice.getProcessTrackId(),ProcessTrackDetailType.CLIENT_DEVICE_PAY,
								message, creationUser, ip, 1,"No se ha indicado en el proceso : " + message
										+ ". ID. Proceso :"	+ ptDevice.getProcessTrackId() + ".",null,null,null,null);
					}
				}
				
				// Send Notification to Client. 
				outbox.insertMessageOutbox(ac.getTbSystemUser().getUserId(), 
		                   EmailProcess.PAYMENT_DEVICE,
		                   ac.getAccountId(),
		                   null,
		                   null, 
		                   device.getDeviceId(),
		                   null,
		                   null,
		                   null,
		                   null,
		                   creationUser,
		                   null,
		                   null,
		                   null,
		                   true,
					       null);
				
				final String email =  ac.getTbSystemUser().getUserEmail();
				final String msg = message;
				new Thread () {
					@Override
					public void run() {
						/*sendMail.sendMail(EmailType.CLIENT, email, 
						EmailSubject.CLIENT_REGISTER_PAY, "Se ha registrado en el sistema: " + msg+  ".");*/
					}
				}.start();
				// End sent notification.
				
			} else {
				// save failure log
				log.insertLog("Guardar Pago de Dispositivo | Saldo en cuenta sin actualizar. Valor a sumar: $" + value +
						", ID account: " + accoundId + ".", LogReference.ACCOUNT, LogAction.UPDATEFAIL, 
						ip, creationUser);
			}
			return true;
		} catch (Exception e) {
			System.out.println(" [ Error en PurchaseEJB.savePayment ] ");
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Purchase#saveRecharge(java.lang.Long, java.lang.Long,
	 * java.lang.Long, java.lang.String, java.lang.Long)
	 */
	@Override
	public boolean saveRecharge(Long purchaseDetailId, Long account, Long value, String ip, Long creationUser, Long stationId) {
		try {
			// searching the account and doing the validation
			TbAccount ta = em.find(TbAccount.class, account);
			if (ta.getAccountBalance().longValue() < value.longValue()) {
				return false;
			}
			
			TbPurchaseOrderDetail detail = em.find(TbPurchaseOrderDetail.class,purchaseDetailId);
			emObj = new ObjectEM(em);
			
			// Updating the state of the purchase order detail.
			detail.setDetailState(PurchaseOrderDetailState.PROCESS_FINISHED.getId()); // Final state of detail in Process.
			detail.setRechargeDate(new Timestamp(System.currentTimeMillis()));			
			if(emObj.update(detail)){
				
				// Save log
				log.insertLog("Recargar | Se Ha actualizado el estado a 'finalizado Proceso' del detalle ID: " + detail.getPurchaseOrderDetailId(), LogReference.PURCHASEORDERDETAIL, LogAction.UPDATE, ip, creationUser);
				
				return this.recharge(detail.getTbDevice().getDeviceId(), value, account, ip, creationUser, stationId, detail.getTbPurchaseOrder().getTbUserData().getTbSystemUser().getUserEmail());
				
			} else {
				log.insertLog("Recargar  | No se pudo actualizar el estado del detalle ID: " + detail.getPurchaseOrderDetailId() , LogReference.PURCHASEORDERDETAIL, LogAction.UPDATEFAIL, ip, creationUser);
			}		
		} catch (Exception e) {
			System.out.println(" [ Error en PurchaseEJB.saveRecharge ] ");
			e.printStackTrace();
		}
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Purchase#recharge(java.lang.Long, java.lang.Long,
	 * java.lang.Long, java.lang.String, java.lang.Long, java.lang.Long,
	 * java.lang.String)
	 */
	@Override
	public boolean recharge(Long deviceId, Long value, Long accountId,
			String ip, Long creationUser, Long stationId, String email) {
		try {
			// searching the account and doing the validation
			TbAccount ta = em.find(TbAccount.class, accountId);
			Long userId=null;
			if(ta!=null){
				userId= ta.getTbSystemUser().getUserId();
			}
			int numDevice = 0;
			// Then, updating the balance in the device Object.
			TbDevice device = em.find(TbDevice.class, deviceId);
			
			 Query qq = em.createQuery("Select rad from ReAccountDevice rad where rad.tbAccount.accountId = ?1 and rad.tbDevice.tbDeviceType.deviceTypeId = ?2 and rad.tbDevice.tbDeviceState.deviceStateId not in (7,9) and rad.relationState=0");
			 qq.setParameter(1, accountId);			
			 qq.setParameter(2, DeviceType.TAG.getId());
			 List<?> lis= qq.getResultList();	
			 if(lis!=null){
				 numDevice=lis.size();
			 }
			 System.out.print("numDevice en PurchaseEJB: " + numDevice);
			if(device.getTbDeviceState().getDeviceStateId()!=7 && device.getTbDeviceState().getDeviceStateId()!=9){
				Long oldBalanceDev = device.getDeviceCurrentBalance().longValue();
				device.setDeviceCurrentBalance(new BigDecimal(value.longValue() + oldBalanceDev.longValue()));
				Query q;
				emObj = new ObjectEM(em);
				if(emObj.update(device)){
					
					System.out.println(LogReference.DEVICE);
					//save log.
					log.insertLog("Recargar | Se ha actualizado el valor del nuevo saldo en el dispositivo ID: " + device.getDeviceId() +  ". Saldo anterior dispositivo: "+oldBalanceDev +", nuevo saldo: " + device.getDeviceCurrentBalance() + ".",
							LogReference.DEVICE, 
							LogAction.UPDATE, ip, creationUser);
					
					//Updating the account balance
					Long accountPreviousBalance  = ta.getAccountBalance().longValue();
					ta.setAccountBalance(new BigDecimal(accountPreviousBalance - value.longValue()));
					
					if (emObj.update(ta)) {
						log.insertLog("Recargar | Se ha actualizado el valor del  saldo en la cuenta ID: " + ta.getAccountId() +  
								". Saldo anterior Cuenta: "+accountPreviousBalance +", nuevo saldo: " + ta.getAccountBalance() + ".", 
								LogReference.ACCOUNT, LogAction.UPDATE, ip, creationUser);
						
						// save the account transaction
						System.out.println("Cuenta: "+accountId);
						System.out.println("Tipo Transaccion: "+TransactionType.INTERNAL_TRANSFER);
						System.out.println("Dispositivo: "+device.getDeviceCode());
						System.out.println("Valor: "+value);
						System.out.println("IP: "+ip);
						System.out.println("Usuario: "+creationUser);
						System.out.println("Saldo Anterior: "+accountPreviousBalance);
						System.out.println("Nuevo Saldo: "+ta.getAccountBalance().longValue());
						
						try{
//						transaction.saveAccountTransaction(accountId, null, deviceId, TransactionType.INTERNAL_TRANSFER,
//								"Dispositivo No: " + device.getDeviceCode() + "."  , 
//								value, ip, creationUser, accountPreviousBalance, ta.getAccountBalance().longValue(), null, null, null, null);
//						
						
						} catch (NoSuchMethodError e){
							return false;					
						}
						
						TbTag tag = em.find(TbTag.class, device.getDeviceCode());
						TbDeviceState tde;
						if(tag.getDeviceStateId()==1 || tag.getDeviceStateId()==3 || tag.getDeviceStateId()==4){
							System.out.println("tag activo");
							q=em.createNativeQuery("select min(val_min_alarm) from tb_alarm_balance where account_id=?1 and id_tip_alarm = 4");
							q.setParameter(1, ta.getAccountId());
							BigDecimal value1=(BigDecimal) q.getSingleResult();
							int valDef=value1.intValue()/numDevice;
							System.out.print("valDef: " + valDef);
							if(value!=null){
								if(device.getDeviceCurrentBalance().longValue()>valDef){
									System.out.println("con saldo");
									tag.setDeviceStateId(1L);
									tde=em.find(TbDeviceState.class, 6L);
									device.setTbDeviceState(tde);
								}
								else if(device.getDeviceCurrentBalance().longValue()<=0L){
									System.out.println("sin saldo");
									tag.setDeviceStateId(3L);
									tde=em.find(TbDeviceState.class, 3L);
									device.setTbDeviceState(tde);
								}
								else if(device.getDeviceCurrentBalance().longValue()>0 && device.getDeviceCurrentBalance().longValue()<valDef){
									System.out.println("con saldo bajo");
									tag.setDeviceStateId(4L);
									tde=em.find(TbDeviceState.class, 4L);
									device.setTbDeviceState(tde);
								}	
								emObj.update(tag);
								emObj.update(device);
							}
						}
						else{
							System.out.println("tag inactivo y no se le cambia el estado");
						}
						
					} else {
						log.insertLog("Recargar | No se pudo actualizar el saldo en la cuenta: " + ta.getAccountId() +  
								". Nuevo saldo: " + accountPreviousBalance + value.longValue() + ".", 
								LogReference.ACCOUNT, LogAction.UPDATEFAIL, ip, creationUser);
					}
					
					try {
						//Creating the transaction
						try{
					        
						} catch (NoSuchMethodError e){
							return false;					
						}	
						// Indication in the device process track.
						TbProcessTrack  p = process.searchProcess(ProcessTrackType.DEVICE, device.getDeviceId());
						
						if (p != null) {
							// save the detail 
							process.createProcessDetail(p.getProcessTrackId(), ProcessTrackDetailType.DEVICE_RECHARGE,  "Se ha hecho una recarga al dispositivo por valor: $" +new DecimalFormat("#,###,###,###").format(value), creationUser, ip, 1, "No se ha podido registar en el proceso que Se ha hecho una recarga al dispositivio por valor: $"  +new DecimalFormat("#,###,###,###").format(value)+ ". Proceso ID: " + p.getProcessTrackId(),null,null,null,null);
						} else {
							log.insertLog("Recarga | El Dispositivo ID: " + device.getDeviceId() + " no tiene proceso asociado.", LogReference.PROCESS, LogAction.NOTFOUND, ip, creationUser);
						}
							
						// Sending email to Client . 
						final String ema = email;
						final String valueEma = value + "";
						new Thread () {
							@Override
							public void run() {
								//sendMail.sendMail(EmailType.CLIENT, ema, EmailSubject.CLIENT_CARD_RECHARGE_NOTIFY, "\\$" + valueEma);
							}
						}.start();
						// End sent notification.			
						
						
						return true;
						
					} catch (NoResultException e) {
						// save log failure. 
						log.insertLog("Recarga | No se pudo guardar la transacción asociada a la estación ingresada.  valores: "+ "Fecha: " + new Timestamp(System.currentTimeMillis())+ ", Nuevo saldo: " + device.getDeviceCurrentBalance() 	+ ", Saldo anterior: " + oldBalanceDev+  ". Estacion ID: " +  stationId + ". Tipo : Crédito, Valor: " + value + "." ,   LogReference.TRANSACTION, LogAction.CREATEFAIL, ip, creationUser);
					}
					
				} else{
					//save log failure.
					log.insertLog("Recargar | No se pudo actualizar el valor del nuevo saldo en el dispositivo ID: " + device.getDeviceId() + ". Saldo anterior: " + oldBalanceDev + ", nuevo saldo: " + device.getDeviceCurrentBalance() + ".", LogReference.DEVICE, LogAction.UPDATEFAIL, ip, creationUser);
				}
			}
			else{
				System.out.println("el tag se encuentra desactivado o en lista negra no se le puede asignar dinero");
				return false;
			}
			
		} catch (Exception e) {
			System.out.println(" [ Error en PurchaseEJB.recharge ] ");
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public boolean recharge1(Long deviceId, Long value, Long accountId,
			String ip, Long creationUser, Long stationId, String email) {
		try {
			// searching the account and doing the validation
			TbAccount ta = em.find(TbAccount.class, accountId);
			Long userId=null;
			if(ta!=null){
				userId= ta.getTbSystemUser().getUserId();
			}
			int numDevice = 0;
			// Then, updating the balance in the device Object.
			TbDevice device = em.find(TbDevice.class, deviceId);
			
			 Query qq = em.createQuery("Select rad from ReAccountDevice rad where rad.tbAccount.accountId = ?1 and rad.tbDevice.tbDeviceType.deviceTypeId = ?2 and rad.tbDevice.tbDeviceState.deviceStateId not in (7,9) and rad.relationState=0");
			 qq.setParameter(1, accountId);			
			 qq.setParameter(2, DeviceType.TAG.getId());
			 List<?> lis= qq.getResultList();	
			 if(lis!=null){
				 numDevice=lis.size();
			 }
			 System.out.print("numDevice en PurchaseEJB: " + numDevice);
			if(device.getTbDeviceState().getDeviceStateId()!=7 && device.getTbDeviceState().getDeviceStateId()!=9){
				Long oldBalanceDev = device.getDeviceCurrentBalance().longValue();
				device.setDeviceCurrentBalance(new BigDecimal(value.longValue() + oldBalanceDev.longValue()));
				Query q;
				emObj = new ObjectEM(em);
				if(emObj.update(device)){
					
					System.out.println(LogReference.DEVICE);
					//save log.
					log.insertLog("Recargar | Se ha actualizado el valor del nuevo saldo en el dispositivo ID: " + device.getDeviceId() +  ". Saldo anterior dispositivo: "+oldBalanceDev +", nuevo saldo: " + device.getDeviceCurrentBalance() + ".",
							LogReference.DEVICE, 
							LogAction.UPDATE, ip, creationUser);
					
					//Updating the account balance
					Long accountPreviousBalance  = ta.getAccountBalance().longValue();
					ta.setAccountBalance(new BigDecimal(accountPreviousBalance - value.longValue()));
					
					if (emObj.update(ta)) {
						log.insertLog("Recargar | Se ha actualizado el valor del  saldo en la cuenta ID: " + ta.getAccountId() +  
								". Saldo anterior Cuenta: "+accountPreviousBalance +", nuevo saldo: " + ta.getAccountBalance() + ".", 
								LogReference.ACCOUNT, LogAction.UPDATE, ip, creationUser);
						
						// save the account transaction
						System.out.println("Cuenta: "+accountId);
						System.out.println("Tipo Transaccion: "+TransactionType.INTERNAL_TRANSFER);
						System.out.println("Dispositivo: "+device.getDeviceCode());
						System.out.println("Valor: "+value);
						System.out.println("IP: "+ip);
						System.out.println("Usuario: "+creationUser);
						System.out.println("Saldo Anterior: "+accountPreviousBalance);
						System.out.println("Nuevo Saldo: "+ta.getAccountBalance().longValue());
						
						try{
//							transaction.saveAccountTransaction(accountId, null, deviceId, TransactionType.INTERNAL_TRANSFER,
//									"Dispositivo No: " + device.getDeviceCode() + "."  , 
//									value, ip, creationUser, accountPreviousBalance, ta.getAccountBalance().longValue(), null, null, null, null);
//							
							Query q1 = em.createNativeQuery("Insert Into Tb_Transaction (TRANSACTION_ID,ACCOUNT_ID,TRANSACTION_TYPE_ID,TRANSACTION_TIME,PREVIOUS_BALANCE,NEW_BALANCE,TRANSACTION_VALUE,TRANSACTION_DESCRIPTION,CONSIGNMENT_ID,USER_ID,TRANSACTION_PROCESS_TIME) "
											+ " values(OFFICE.TB_TRANSACTION_SEQ.nextval,?1,4,?2,?3,?4,?5,?6 ,null,?7,?8)");
							
							q1.setParameter(1, accountId);
							q1.setParameter(2, new Timestamp(System.currentTimeMillis()));
							q1.setParameter(3, accountPreviousBalance);
							q1.setParameter(4,  ta.getAccountBalance().longValue());
							q1.setParameter(5,  value);
							q1.setParameter(6,  "Dispositivo No: " +device.getDeviceCode());
							q1.setParameter(7,  userId);
							q1.setParameter(8,  new Timestamp(System.currentTimeMillis()));
							
							q1.executeUpdate();
							
							} catch (NoSuchMethodError e){
								return false;					
							}
						
						TbTag tag = em.find(TbTag.class, device.getDeviceCode());
						TbDeviceState tde;
						if(tag.getDeviceStateId()==1 || tag.getDeviceStateId()==3 || tag.getDeviceStateId()==4){
							System.out.println("tag activo");
							q=em.createNativeQuery("select min(val_min_alarm) from tb_alarm_balance where account_id=?1 and id_tip_alarm = 4");
							q.setParameter(1, ta.getAccountId());
							BigDecimal value1=(BigDecimal) q.getSingleResult();
							int valDef=value1.intValue()/numDevice;
							System.out.print("valDef: " + valDef);
							if(value!=null){
								if(device.getDeviceCurrentBalance().longValue()>valDef){
									System.out.println("con saldo");
									tag.setDeviceStateId(1L);
									tde=em.find(TbDeviceState.class, 6L);
									device.setTbDeviceState(tde);
								}
								else if(device.getDeviceCurrentBalance().longValue()<=0L){
									System.out.println("sin saldo");
									tag.setDeviceStateId(3L);
									tde=em.find(TbDeviceState.class, 3L);
									device.setTbDeviceState(tde);
								}
								else if(device.getDeviceCurrentBalance().longValue()>0 && device.getDeviceCurrentBalance().longValue()<=valDef){
									System.out.println("con saldo bajo");
									tag.setDeviceStateId(4L);
									tde=em.find(TbDeviceState.class, 4L);
									device.setTbDeviceState(tde);
								}	
								emObj.update(tag);
								emObj.update(device);
							}
						}
						else{
							System.out.println("tag inactivo y no se le cambia el estado");
						}
						
					} else {
						log.insertLog("Recargar | No se pudo actualizar el saldo en la cuenta: " + ta.getAccountId() +  
								". Nuevo saldo: " + accountPreviousBalance + value.longValue() + ".", 
								LogReference.ACCOUNT, LogAction.UPDATEFAIL, ip, creationUser);
					}
					
					try {
						//Creating the transaction
						try{
						
//							transaction.saveAccountTransaction(accountId, null, device.getDeviceId(), TransactionType.ASSIGNMENT,
//									"Transferencia del Monto al Dispositivo. ", 
//									value, ip, creationUser, oldBalanceDev, device.getDeviceCurrentBalance().longValue(), null, null,
//								stationId, null);
						
					   		Query q1 = em.createNativeQuery("Insert Into Tb_Transaction (TRANSACTION_ID,ACCOUNT_ID,TRANSACTION_TYPE_ID,TRANSACTION_TIME,PREVIOUS_BALANCE,NEW_BALANCE,TRANSACTION_VALUE,TRANSACTION_DESCRIPTION,DEVICE_ID,CONSIGNMENT_ID,USER_ID,TRANSACTION_PROCESS_TIME) "
									+ " values(OFFICE.TB_TRANSACTION_SEQ.nextval,?1,3,?2,?3,?4,?5,?6,?7 ,null,?8,?9)");
					
					        q1.setParameter(1, accountId);
					        q1.setParameter(2, new Timestamp(System.currentTimeMillis()));
					        q1.setParameter(3, oldBalanceDev);
					        q1.setParameter(4,   device.getDeviceCurrentBalance().longValue());
					        q1.setParameter(5,  value);
					        q1.setParameter(6,  "Transferencia del Monto al dispositivo" );
					        q1.setParameter(7,  device.getDeviceId());
					        q1.setParameter(8,  userId);
					        q1.setParameter(9,  new Timestamp(System.currentTimeMillis()));
					        
					        q1.executeUpdate();
					        
						} catch (NoSuchMethodError e){
							return false;					
						}	
						// Indication in the device process track.
						TbProcessTrack  p = process.searchProcess(ProcessTrackType.DEVICE, device.getDeviceId());
						
						if (p != null) {
							// save the detail 
							process.createProcessDetail(p.getProcessTrackId(), ProcessTrackDetailType.DEVICE_RECHARGE,  "Se ha hecho una recarga al dispositivo por valor: $" +new DecimalFormat("#,###,###,###").format(value), creationUser, ip, 1, "No se ha podido registar en el proceso que Se ha hecho una recarga al dispositivio por valor: $"  +new DecimalFormat("#,###,###,###").format(value)+ ". Proceso ID: " + p.getProcessTrackId(),null,null,null,null);
						} else {
							log.insertLog("Recarga | El Dispositivo ID: " + device.getDeviceId() + " no tiene proceso asociado.", LogReference.PROCESS, LogAction.NOTFOUND, ip, creationUser);
						}
							
						// Sending email to Client . 
						/*final String ema = email;
						final String valueEma = value + "";
						new Thread () {
							@Override
							public void run() {
								sendMail.sendMail(EmailType.CLIENT, ema, EmailSubject.CLIENT_CARD_RECHARGE_NOTIFY, "\\$" + valueEma);
							}
						}.start();*/
						// End sent notification.			
						
						
						return true;
						
					} catch (NoResultException e) {
						// save log failure. 
						log.insertLog("Recarga | No se pudo guardar la transacción asociada a la estación ingresada.  valores: "+ "Fecha: " + new Timestamp(System.currentTimeMillis())+ ", Nuevo saldo: " + device.getDeviceCurrentBalance() 	+ ", Saldo anterior: " + oldBalanceDev+  ". Estacion ID: " +  stationId + ". Tipo : Crédito, Valor: " + value + "." ,   LogReference.TRANSACTION, LogAction.CREATEFAIL, ip, creationUser);
					}
					
				} else{
					//save log failure.
					log.insertLog("Recargar | No se pudo actualizar el valor del nuevo saldo en el dispositivo ID: " + device.getDeviceId() + ". Saldo anterior: " + oldBalanceDev + ", nuevo saldo: " + device.getDeviceCurrentBalance() + ".", LogReference.DEVICE, LogAction.UPDATEFAIL, ip, creationUser);
				}
			}
			else{
				System.out.println("el tag se encuentra desactivado o en lista negra no se le puede asignar dinero");
				return false;
			}
			
		} catch (Exception e) {
			System.out.println(" [ Error en PurchaseEJB.recharge ] ");
			e.printStackTrace();
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public String distributionBalance(TbAccount account,Long value,String ip, Long creationUser){
		if (account.getDistributionTypeId().getDistributionTypeId()==2L){
			/*
			 * Distribucion de saldo Automaticamente:
			 * se crea una recarga para los dispositivos tag prepago de la cuenta
			 * con el valor de la consignacion devidida en el numero de dispositivos
			 */
			Query q = em.createQuery("Select rad from ReAccountDevice rad where rad.tbAccount = ?1 and rad.tbDevice.tbDeviceType.deviceTypeId = ?2 and rad.relationstate=0");
			q.setParameter(1, account);			
			q.setParameter(2, DeviceType.TAG.getId());	
			
			int countDevice = q.getResultList().size();
			Long valueRecharge = (value/countDevice);

			List items = q.getResultList();
			Iterator l = items.iterator();
			
			while (l.hasNext()) {
				ReAccountDevice d = (ReAccountDevice)l.next();
				
				System.out.println("Dispositivo: "+d.getTbDevice().getDeviceCode());				
				
				if(recharge(d.getTbDevice().getDeviceId(), valueRecharge, account.getAccountId(), ip, creationUser, null, account.getTbSystemUser().getUserEmail())){
					System.out.println("Recarga OK Dispositivo: "+d.getTbDevice().getDeviceCode());
				}
			}			
		}
		
		if(account.getDistributionTypeId().getDistributionTypeId()==1L){ 
			/*
			 * Distribucion de saldo por bolsa de dinero:			
			 */
			
			transitTask.createTask(TypeTask.ACCOUNT, account.getAccountId().toString());
		}
		return null;
	}
	
	public String selectReAccountDevice(Long deviceId){
		String idVehicle=null;
		try {
			Object ob = em.createNativeQuery("select vehicle_id from re_account_device where DEVICE_ID=?1").setParameter(1,deviceId).getSingleResult();
			idVehicle = ob.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return idVehicle;
	}

	@Override
	public String getTbVehiclePlate(String idVehicle) {
		String plate=null;
		try {
			Object ob = em.createNativeQuery("select vehicle_plate from TB_VEHICLE where VEHICLE_ID=?1").setParameter(1,idVehicle).getSingleResult();
			plate = ob.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return plate;
	}
}