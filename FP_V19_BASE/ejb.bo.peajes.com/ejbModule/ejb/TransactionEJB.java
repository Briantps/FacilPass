package ejb;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.exception.ConstraintViolationException;

import util.TransactionStatus;

import constant.LogAction;
import constant.LogReference;
import constant.RelationDeviceAccountState;
import constant.TransactionType;
import constant.TypeTask;
import constant.VialTypeOpertionTag;
import constant.VialTypeTag;
import crud.ObjectEM;
import ejb.taskeng.TransitTask;

import jpa.ReAccountDevice;
import jpa.TbAccount;
import jpa.TbConsignment;
import jpa.TbConsignmentState;
import jpa.TbDevice;
import jpa.TbLane;
import jpa.TbPayType;
import jpa.TbStationBO;
import jpa.TbTag;
import jpa.TbTranTag;
import jpa.TbTransaction;
import jpa.TbTransactionDac;
import jpa.TbTransactionType;
import jpa.TbVehicle;

/**
 * Session Bean implementation class TransactionEJB
 * @author tmolina
 */
@Stateless(mappedName = "ejb/Transaction")
public class TransactionEJB implements Transaction {
	
	@PersistenceContext(unitName = "bo") 
	EntityManager em;

	private ObjectEM emObj;
	
	@EJB(mappedName="ejb/Log")
	private Log log;
	
	@EJB(mappedName="ejb/Vehicle")
	private Vehicle vehicle;
	
	@EJB(mappedName="ejb/TransitTask")
	private TransitTask transitTask;
	
	@EJB(mappedName="ejb/User")
	private User userEjb;
	
	@EJB(mappedName="ejb/Vehicle")
	private Vehicle vehicleEjb;
	
    /**
     * Default constructor. 
     */
    public TransactionEJB() {
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Transaction#getTransactionByAccountAndDate(java.lang.Long,
	 * java.util.Date, java.util.Date)
	 */
	@Override
	public List<TbTransaction> getTransactionByAccountAndDate(
			Long idAccount, Date initDate, Date endDate) {
		List<TbTransaction> list = new ArrayList<TbTransaction>();
		try {
			Query q = em.createQuery("SELECT at FROM TbTransaction at WHERE " +
					"at.tbAccount.accountId = ?1 and at.tbTransactionType.transactionTypeId in (1,2,5,6) AND at.transactionTime BETWEEN ?2 AND ?3 " +
					"ORDER BY at.transactionTime");
			q.setParameter(1, idAccount);
			q.setParameter(2, new Timestamp(initDate.getTime()));
			q.setParameter(3, new Timestamp(endDate.getTime() + 86399000));
			for(Object obj: q.getResultList()) {
				list.add((TbTransaction) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en TransactionEJB.getTransactionByAccountAndDate ] ");
			e.printStackTrace();
		}
		return list;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Transaction#saveAccountTransaction(java.lang.Long,
	 * java.lang.Long, java.lang.Long, constant.TransactionType,
	 * java.lang.String, java.lang.Long, java.lang.String, java.lang.Long,
	 * java.lang.Long, java.lang.Long, java.sql.Timestamp, java.lang.Long,
	 * java.lang.Long, java.lang.Long)
	 */
	
	
	public boolean saveAccountTransaction(Long accoundId, Long consignmentId,
			Long deviceId, TransactionType transactionType, String description, Long value,
			String ip, Long creationUser,Long previousBalance, Long newBalance,
			Timestamp backOfficeTime, Long laneId, Long stationBoId, Long vehicleId) {
		try {
			TbTransaction ac = new TbTransaction();
			
			accoundId = accoundId != null ? accoundId: -1L;
			TbAccount ta=em.find(TbAccount.class, accoundId);
			ac.setTbAccount(ta);
			
			ac.setTbTransactionType(em.find(TbTransactionType.class,
					transactionType.getId()));
			
			ac.setTransactionTime(new Timestamp(System.currentTimeMillis()));
			
			ac.setPreviousBalance(previousBalance);
			ac.setNewBalance(newBalance);
			ac.setTransactionValue(value);
			
			ac.setTransactionDescription(description);
			
			ac.setBackOfficeTime(backOfficeTime);
			
			deviceId = deviceId != null ? deviceId: -1L;
			ac.setTbDevice(em.find(TbDevice.class, deviceId));
			
			laneId = laneId != null ? laneId: -1L;
			ac.setTbLane(em.find(TbLane.class, laneId));
			
			vehicleId = vehicleId != null ? vehicleId: -1L;
			ac.setTbVehicle(em.find(TbVehicle.class, vehicleId));

			consignmentId = consignmentId != null ? consignmentId: -1L;
			ac.setTbConsignment(em.find(TbConsignment.class, consignmentId));
			
			stationBoId = stationBoId != null ? stationBoId: -1L;
			ac.setTbStationBO(em.find(TbStationBO.class, stationBoId));
			
			ac.setTransactionProcess(new Timestamp(System.currentTimeMillis()));
			if(ta!=null){
				ac.setUserId(ta.getTbSystemUser().getUserId());
			}
			
			if(ta!=null){
				ac.setAccountBankId(userEjb.getProductByAccount(ta.getAccountId()));
			}
			System.out.println("Dispositivo id: "+deviceId);
			
			emObj = new ObjectEM(em);
			if (emObj.create(ac)) {				
				
				if((accoundId==-1L) && (transactionType==TransactionType.CREDIT)){
					
					// Recarga de Dispositivos		
				
					
					System.out.println("Dispositivo id 2: "+deviceId);
					
					Query q = em.createQuery("Select rad from ReAccountDevice rad where rad.tbDevice.deviceId = ?1 and rad.relationState=0");
					q.setParameter(1, deviceId);
										
					ReAccountDevice rad = (ReAccountDevice) q.getSingleResult();
					
					Query qr = em.createNativeQuery("select to_number(tt.tag_type_code) from re_device_tag_type rdt "+
							"inner join tb_device d on rdt.device_id=d.device_id "+
							"inner join tb_tag_type tt on rdt.tag_type_id=tt.tag_type_id "+
							"where d.device_id=?1");
					qr.setParameter(1, deviceId);					

					BigDecimal manufaturerId = (BigDecimal) qr.getSingleResult();
					
					
						TbTag tag = em.find(TbTag.class, rad.getTbDevice().getDeviceCode());
						if(tag == null){
							TbTag tagNew = new TbTag();						
							tagNew.setDeviceId(rad.getTbDevice().getDeviceCode());
							tagNew.setDeviceCode(rad.getTbDevice().getDeviceFacialId());
							tagNew.setDeviceCurrentBalance(rad.getTbDevice().getDeviceCurrentBalance());
							tagNew.setCategoryId(rad.getTbVehicle().getTbCategory().getCategoryId());
							tagNew.setDeviceStateId(1L);
							tagNew.setDeviceTypeId(VialTypeTag.PREPAGO.getTipo());
							tagNew.setTagTypeCode(manufaturerId.longValue());
							tagNew.setVehiclePlate(rad.getTbVehicle().getVehiclePlate());
							tagNew.setContractualAuth(5L);
							if(rad.getTbAccount().getDistributionTypeId().getDistributionTypeId()==1L){
								tagNew.setAccountId(rad.getTbAccount().getAccountId().longValue());
							} 
							emObj = new ObjectEM(em);
							emObj.create(tagNew);
							transitTask.createTask(TypeTask.DEVICE, tagNew.getDeviceId());
						} else {
						 	tag.setDeviceCode(rad.getTbDevice().getDeviceFacialId());
							tag.setDeviceCurrentBalance(rad.getTbDevice().getDeviceCurrentBalance());
							tag.setCategoryId(rad.getTbVehicle().getTbCategory().getCategoryId());
							tag.setDeviceStateId(1L);
							tag.setDeviceTypeId(VialTypeTag.PREPAGO.getTipo());
							tag.setVehiclePlate(rad.getTbVehicle().getVehiclePlate());
							tag.setTagTypeCode(manufaturerId.longValue());
							if(rad.getTbAccount().getDistributionTypeId().getDistributionTypeId()==1L){
								tag.setAccountId(rad.getTbAccount().getAccountId().longValue());
							} 
							emObj = new ObjectEM(em);
							emObj.update(tag);
							transitTask.createTask(TypeTask.DEVICE, tag.getDeviceId());
						}				
					
					
					TbTranTag tt = new TbTranTag();
					tt.setTransactionId(ac.getTransactionId());
					tt.setDeviceId(ac.getTbDevice().getDeviceCode());
					tt.setTransactionTypeId(VialTypeOpertionTag.RECARGA.getTipo());
					tt.setPreviousBalance(ac.getPreviousBalance());
					tt.setNewBalance(ac.getNewBalance());
					tt.setTransactionValue(ac.getTransactionValue());
					tt.setTransactionTime(ac.getTransactionTime());
					tt.setOrigin("VR-"+ip);
					tt.setObserv(ac.getTransactionDescription());
					
					emObj = new ObjectEM(em);
					if(emObj.create(tt)){
						transitTask.createTask(TypeTask.RECHARGEDEVICE, tt.getTransactionId().toString());
					}					
					
				}
				
				// save log
				log.insertLog("Guardar Transacción | ID: " + ac.getTransactionId(), 
						LogReference.ACCOUNT_TRANSACTION, LogAction.CREATE, ip, creationUser);
			} else {
				// save fail into log
				log.insertLog("Guardar Transacción | Error al guardar, datos: Cuenta: " +accoundId + ", Transacción: " +transactionType
						+ ", descripción: " + description + ", valor: " + value + ", device Id: " + deviceId + 
						", consignment Id: " + consignmentId + ", vehicle Id: " + vehicleId + ".",
						LogReference.ACCOUNT_TRANSACTION, 
						LogAction.CREATEFAIL, ip, creationUser);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en TransactionEJB.saveAccountTransaction ] ");
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see ejb.Transaction#getClientAccounts(java.lang.Long)
	 */
	@Override
	public List<TbAccount> getClientAccounts(Long clientId) {
		List<TbAccount> list = new ArrayList<TbAccount>();
		try {
			Query q = em.createQuery("SELECT ta FROM TbAccount ta WHERE ta.tbSystemUser.userId = ?1");
			q.setParameter(1, clientId);
			for (Object obj : q.getResultList()) {
				list.add((TbAccount) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en TransactionEJB.getClientAccounts ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see ejb.Transaction#getAvailableBalanceAccount(java.lang.Long)
	 */
	@Override
	public Long getAvailableBalanceAccount(Long accountId) {
		Long result = 0L;
		try {
					
			TbAccount a =em.find(TbAccount.class, accountId);
			result= a.getAccountBalance().longValue();
			
		} catch (Exception e) {
			System.out.println(" [ Error en TransactionEJB.getAvailableBalanceAccount ] ");
			e.printStackTrace();
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TransactionStatus>  getTransaction(Date initDate, Date endDate, Long companyId, Long stationId, 
			Long laneId, Long clientId, Long accountId, Long deviceId){
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
		//SimpleDateFormat sdf2= new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String fec1, fec2;
		fec1= sdf.format(initDate);
		fec2= sdf.format(endDate);
		System.out.println("fecha 1: " + fec1);
		System.out.println("fecha 2: " + fec2);
		fec1=fec1+ " " + "00:00:00";
		fec2=fec2+ " " + "23:59:59";
		
		System.out.println("fec1: " + fec1);
		System.out.println("fec2: " + fec2);
		Timestamp f1, f2;
		f1=Timestamp.valueOf(fec1);
		f2=Timestamp.valueOf(fec2);
		System.out.println("f1: " + f1);
		System.out.println("f2: " + f2);
		List<TransactionStatus> list= new ArrayList<TransactionStatus>();
		String where="" ;
		
		String consulta="select tt.transaction_id, to_char(tt.transaction_time,'dd/MM/yyyy hh24:mi:ss'), tc.company_name, ts.station_bo_name, tl.lane_code, tt.category_id,td.device_code, tv.vehicle_plate, tts.name_state,tt.transaction_value  "
                              + " from tb_transaction tt "
                              + " inner join tb_lane tl on tt.lane_id = tl.lane_id " 
                              + " inner join tb_station_bo ts on tl.station_id= ts.station_bo_id "
                              + " inner join tb_company tc on ts.company_id= tc.company_id "
                              + " inner join tb_device td on tt.device_id= td.device_id "
                              + " inner join tb_account ta on ta.account_id= tt.account_id "
                              + " left join tb_vehicle tv on tt.vehicle_id= tv.vehicle_id "
                              + " left join tb_transaction_validate_state tv on tt.validate_type_id= tv.validate_id "
                              + " left join tb_transaction_state tts on tv.tb_transaction_state_id = tts.state_id "
                              + " where tc.company_id=?1 and ta.user_id=?2 and (tt.account_id is not null) and tt.transaction_type_id=2 and  tc.company_type_id=1 and " 
                              + " (tt.transaction_time BETWEEN ?3 and ?4) ";
		int flag1=0, flag2=0, flag3=0, flag4=0;
		if(stationId>0L){
		    where=" and ts.station_bo_id=?5 ";	
		    flag1=1;
		}
		if(laneId>0L){
			where= where+ " and tl.lane_id=?6 ";
			flag2=1;
		}
		if(accountId>0L){
			where= where+ " and ta.account_id=?7 ";
			flag3=1;
		}
		if(deviceId>0L){
			where= where+ " and td.device_id=?8 ";
			flag4=1;
		}
		
		consulta= consulta + where + "order by tt.transaction_time ";
		System.out.println("Consulta " + consulta);
		Query q = em.createNativeQuery(consulta).setParameter(1, companyId).setParameter(2, clientId).setParameter(3, f1)
		.setParameter(4, f2);
		if(flag1==1){
			q.setParameter(5, stationId);
		}
		if(flag2==1){
			q.setParameter(6, laneId);
		}
		if(flag3==1){
			q.setParameter(7, accountId);
		}
		if(flag4==1){
			q.setParameter(8, deviceId);
		}
		
		List<Object> lis= (List<Object>)q.getResultList();
		
		for(int i=0;i<=lis.size()-1;i++){
			Object o[]= (Object[]) lis.get(i);
			TransactionStatus tran= new TransactionStatus();
			tran.setTransactionId(Long.parseLong(o[0].toString()));
			//String fecha= sdf2.format(o[1]);
			
			tran.setDateTransaction(o[1].toString());
			tran.setCompany(o[2].toString());
			tran.setStation(o[3].toString());
			tran.setLane(Long.parseLong(o[4].toString()));
			tran.setCate(o[5]!=null?Long.parseLong(o[5].toString()):null);
			tran.setDevice(o[6]!=null?o[6].toString():"-");
			tran.setPlate(o[7]!=null?o[7].toString():"-");
			tran.setValidate(o[8]!=null?o[8].toString():"Sin Validar");
			tran.setDisabled(o[8]!=null?true:false);
			tran.setValue(o[9]!=null?"$"+new DecimalFormat("#,###,###,###").format(Long.parseLong(o[9].toString())):null);
			list.add(tran);
		}
		return list;
		
	}
	
	@Override
	public TbTransactionDac getDetection(Long transactionId) {
		TbTransactionDac t = null;
		try{
			Query q= em.createQuery("select td from TbTransactionDac td where td.transactionId.transactionId=?1");
			t=(TbTransactionDac) q.setParameter(1, transactionId).getSingleResult();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return t;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Transaction#upTranTelepeaje(java.lang.String)
	 */
	@Override
	public boolean upTranTelepeaje(String[] transaction) {
		try {
			/*
				 Description of transaction array
				 transaction position 0 : transaction tag id, it becomes the transaction id,
				  						  if transaction operation type is 2.
				 transaction position 1 : transaction operation type, 1 is credit, 2 is debit.
				 transaction position 2 : transaction date.
				 transaction position 3 : previous balance.
				 transaction position 4 : new balance.
				 transaction position 5 : operation/transaction value.
				 transaction position 6 : vehicle plate number.
				 transaction position 7 : Lane ID.
			 */
			
			DateFormat formatter = new SimpleDateFormat("dd/MM/yy HH:mm:ss.SSS");
			Date date;
			Long prev = Long.valueOf(transaction[3]);
			Long curr =  Long.valueOf(transaction[4]);
			Long value = Long.valueOf(transaction[5]);
		
			// searching for vehicle				
			TbVehicle v = vehicle.getVehicleByPlate(transaction[6]);				
			if (v != null) { // if vehicle is found in system
							
				//searching for device
				Query q = em.createQuery("SELECT rad FROM ReAccountDevice rad " +
						"WHERE rad.tbVehicle.vehicleId = ?1 AND rad.relationState = 0 ").setMaxResults(1);
				q.setParameter(1, v.getVehicleId());
				
				try {
					ReAccountDevice rad = (ReAccountDevice) q.getSingleResult();
					
					//Date
					date = (Date)formatter.parse(transaction[2]);
					System.out.println(date);
					
					TbAccount account = em.find(TbAccount.class, rad.getTbAccount().getAccountId());
					TbDevice device = em.find(TbDevice.class, rad.getTbDevice().getDeviceId());
					TbVehicle vehicle = em.find(TbVehicle.class, rad.getTbVehicle().getVehicleId());
					
					// Asking what kind of transaction is
					if (transaction[1].equals("2")) { // if debit,					
						// Creating transaction 
						this.saveTransaction(null, 2L, date, prev, curr, value, "Descuento por Paso Peaje",
								device, em.find(TbLane.class, Long.parseLong(transaction[7].trim())), 
								Long.valueOf(transaction[0]), vehicle.getVehicleId());
						
						//Update device value
						device.setDeviceCurrentBalance(new BigDecimal(device.getDeviceCurrentBalance().longValue() - value));
						em.merge(device);
						//em.flush();
					} else { // if credit					
						
						// validate if consignment has not been entered yet
						Query qc = em.createQuery("SELECT tc FROM TbConsignment tc WHERE" +
								" tc.tbAccount.accountId = ?1 AND tc.consignmentValue = ?2 " +
								" AND tc.consignmentDate = ?3");
						qc.setParameter(1, account.getAccountId());
						qc.setParameter(2, value);
						qc.setParameter(3, new Timestamp(date.getTime()));
						
						try {
							qc.getSingleResult();
						} catch (NoResultException e) {
							// First creating the consign 
							TbConsignment c = new TbConsignment();
							c.setConsignmentValue(value);
							c.setConsignmentDate(new Timestamp(date.getTime()));
							c.setConsignmentRegisterDate(new Timestamp(System.currentTimeMillis()));
							c.setTbPayType(em.find(TbPayType.class, 0L));
							c.setTbAccount(account);
							c.setTbConsignmentState(em.find(TbConsignmentState.class, 1L));
							c.setConsignmentConcept("RECARGA DE DISPOSITIVO");
							em.persist(c);
							em.flush();
							
							// Creating the transaction for the account
							this.saveTransaction(account, 1L, date, account.getAccountBalance().longValue(),
									account.getAccountBalance().longValue() + value, 
									 value, "Consignación Aprobada", null, null, null, null);	
							
							//Update Account value
							account.setAccountBalance(new BigDecimal(account.getAccountBalance().longValue() + value));
							em.merge(account);
							em.flush();
							
							//Creating the transaction of the account discount 
							this.saveTransaction(account, 2L, date, account.getAccountBalance().longValue(),
									account.getAccountBalance().longValue() - value,
									value, "Descuento por recarga de Dispositivo ID Interno Dispositivo :" 
									+ device.getDeviceCode(), device, null, null, null);	
							
							//Update Account value
							account.setAccountBalance(new BigDecimal(account.getAccountBalance().longValue() - value));
							em.merge(account);
							em.flush();
							
							//Creating the transaction of the device recharge
							this.saveTransaction(null, 1L, date, device.getDeviceCurrentBalance().longValue(),
									device.getDeviceCurrentBalance().longValue() + value,
									value, "Se ha recargado el Dispositivo por medio de la cuenta No. :" 
									+ account.getAccountId(), device, null, null, null);	
							
							//Update device value
							device.setDeviceCurrentBalance(new BigDecimal(device.getDeviceCurrentBalance().longValue() + value));
							em.merge(device);
							em.flush();
						}
					}	
				} catch (NoResultException e) {
					System.out.println("No se encontró la relación entre la placa: " + transaction[6] + ", y una cuenta de Cliente.");
				}
			}
			else {
				//searching for device
				Query q = em.createQuery("SELECT rad FROM ReAccountDevice rad " +
						"WHERE rad.tbDevice.deviceCode = ?1 AND rad.relationState = 0 ").setMaxResults(1);
				q.setParameter(1,transaction[6]);
				
				try {
					ReAccountDevice rad = (ReAccountDevice) q.getSingleResult();
					
					//Date
					date = (Date)formatter.parse(transaction[2]);
					System.out.println(date);
					
					TbAccount account = em.find(TbAccount.class, rad.getTbAccount().getAccountId());
					TbDevice device = em.find(TbDevice.class, rad.getTbDevice().getDeviceId());
					//TbVehicle vehicle = em.find(TbVehicle.class, rad.getTbVehicle().getVehicleId());
					
					// Asking what kind of transaction is
					if (transaction[1].equals("2")) { // if debit,					
						// Creating transaction 
						this.saveTransaction(null, 2L, date, prev, curr, value, "Descuento por Paso Peaje",
								device, em.find(TbLane.class, Long.parseLong(transaction[7].trim())), 
								Long.valueOf(transaction[0]), null);
						
						//Update device value
						device.setDeviceCurrentBalance(new BigDecimal(device.getDeviceCurrentBalance().longValue() - value));
						em.merge(device);
						//em.flush();
					} else { // if credit					
						
						// validate if consignment has not been entered yet
						Query qc = em.createQuery("SELECT tc FROM TbConsignment tc WHERE" +
								" tc.tbAccount.accountId = ?1 AND tc.consignmentValue = ?2 " +
								" AND tc.consignmentDate = ?3");
						qc.setParameter(1, account.getAccountId());
						qc.setParameter(2, value);
						qc.setParameter(3, new Timestamp(date.getTime()));
						
						try {
							qc.getSingleResult();
						} catch (NoResultException e) {
							// First creating the consign 
							TbConsignment c = new TbConsignment();
							c.setConsignmentValue(value);
							c.setConsignmentDate(new Timestamp(date.getTime()));
							c.setConsignmentRegisterDate(new Timestamp(System.currentTimeMillis()));
							c.setTbPayType(em.find(TbPayType.class, 0L));
							c.setTbAccount(account);
							c.setTbConsignmentState(em.find(TbConsignmentState.class, 1L));
							c.setConsignmentConcept("RECARGA DE DISPOSITIVO");
							em.persist(c);
							em.flush();
							
							// Creating the transaction for the account
							this.saveTransaction(account, 1L, date, account.getAccountBalance().longValue(),
									account.getAccountBalance().longValue() + value, 
									 value, "Consignación Aprobada", null, null, null, null);	
							
							//Update Account value
							account.setAccountBalance(new BigDecimal(account.getAccountBalance().longValue() + value));
							em.merge(account);
							em.flush();
							
							//Creating the transaction of the account discount 
							this.saveTransaction(account, 2L, date, account.getAccountBalance().longValue(),
									account.getAccountBalance().longValue() - value,
									value, "Descuento por recarga de Dispositivo ID Interno Dispositivo :" 
									+ device.getDeviceCode(), device, null, null, null);	
							
							//Update Account value
							account.setAccountBalance(new BigDecimal(account.getAccountBalance().longValue() - value));
							em.merge(account);
							em.flush();
							
							//Creating the transaction of the device recharge
							this.saveTransaction(null, 1L, date, device.getDeviceCurrentBalance().longValue(),
									device.getDeviceCurrentBalance().longValue() + value,
									value, "Se ha recargado el Dispositivo por medio de la cuenta No. :" 
									+ account.getAccountId(), device, em.find(TbLane.class, Long.parseLong(transaction[7].trim())), 
									Long.valueOf(transaction[0]), null);	
							
							//Update device value
							device.setDeviceCurrentBalance(new BigDecimal(device.getDeviceCurrentBalance().longValue() + value));
							em.merge(device);
							em.flush();
						}
					}	
				} catch (NoResultException e) {
					System.out.println("No se encontró la relación entre la placa: " + transaction[6] + ", y una cuenta de Cliente.");
				}
			}
		} catch (Exception e) {
			System.out.println(" [ Error en TransactionEJB.upTranTelepeaje ] ");
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Saves a transaction.
	 * @param tbAccount
	 * @param tranType
	 * @param date
	 * @param previousBalance
	 * @param currentBalance
	 * @param value
	 * @param message
	 * @param device
	 */
	private void saveTransaction(TbAccount tbAccount, Long tranType, Date date,
			Long previousBalance, Long currentBalance, Long value, String message,
			TbDevice device, TbLane lane, Long transactionID, Long vehicleId) {
		try {
			if (transactionID != null) {
				String query = "INSERT INTO tb_transaction " +
				"(transaction_id, " +
				"transaction_type_id, " +
				"transaction_time, " +
				"previous_balance, " +
				"new_balance, " +
				"transaction_value," +
				"transaction_description, " +
				"device_id, " +
				"lane_id, " +
				"vehicle_id) VALUES ( "
				+ transactionID + ", " +
				+ tranType + ", " +
				"?3, " +
				previousBalance      + ", " +
				currentBalance       + ", " +
				value                + ",'" +
				message              + "'," +
				device.getDeviceId() + ", " +
				lane.getLaneId()     + ", " +
				vehicleId + ") ";
				System.out.println(query);
				
				Query q = em.createNativeQuery(query);
				q.setParameter(3, new Timestamp(date.getTime()));
				q.executeUpdate();
			} else {
				TbTransaction t = new TbTransaction();	
				t.setTbAccount(tbAccount);
				t.setTbTransactionType(em.find(TbTransactionType.class, tranType));
				t.setTransactionTime(new Timestamp(date.getTime()));
				t.setPreviousBalance(previousBalance);
				t.setNewBalance(currentBalance);
				t.setTransactionValue(value);
				t.setTransactionDescription(message);
				t.setTbDevice(device);
				t.setTbLane(lane);
				em.persist(t);
				em.flush();
			}	
			
		} catch (ConstraintViolationException ce) {
			System.out.println("Intento de ingresar una transacción ya ingresada.");
		} catch (Exception e) {
			System.out.println("Error en TransactionEJB.saveTransaction ");
			e.printStackTrace();
		}
	}

	
	@Override
	public Long getLaneByTransaction(Long idTransaction){
		Long c;
		Query q= em.createQuery("select t.tbLane.laneId from TbTransaction t where t.transactionId=?1")
		  .setParameter(1, idTransaction);
		
		c=(Long) q.getSingleResult();
		return c;
		
	}
	
	@Override
	public Object[] getDeviceByTransaction(Long idTransaction){
		Object[] c;
		Query q= em.createNativeQuery("select t.equipmentobuid , t.vehiclelicenceplatenumber, t.vehicleclass from tb_transaction tt "
                                + " inner join tb_device td on tt.device_id=td.device_id "
								+ " inner join tag t on td.device_code=t.equipmentobuid "
								+ " where tt.transaction_id=?1 ") 
				.setParameter(1, idTransaction);
		
		c=(Object[]) q.getSingleResult();
		return c;
		
	}
	
	@Override
	public boolean changeInfoTag(String tag, String placa, Long cate, Long user, String ip, Long resp){
		boolean res=false;
		int flag=0;
		int var=0;
		try{
			placa= placa.toUpperCase();
			//placa pertenece al mismo tag, solo se va a cambiar la categoria
			if(resp.equals(1L)){
				//se actualiza el tag
				Query q= em.createNativeQuery("update tag set vehiclelicenceplatenumber=?1,  vehicleclass=?2 where equipmentobuid=?3 ");
				q.setParameter(1, placa);
				q.setParameter(2, cate);
				q.setParameter(3, tag);
				q.executeUpdate();
				
				//se actualiza la placa
				q= em.createNativeQuery("update tb_vehicle set category_id=?1 where vehicle_plate=?2 ");
				q.setParameter(1, cate);
				q.setParameter(2, placa);
				q.executeUpdate();
				
				em.flush();
				res=true;
			}
			
			//la placa no pertenece a ningun tag
			else{
				//se busca la cuenta y el id del dispositivo que se recibe por parametro para crear el re_account_device
				Query q1= em.createNativeQuery("select ta.account_id, td.device_id, ta.user_id from re_Account_device rad "
	                                    + " inner join tb_device td on rad.device_id= td.device_id "
	                                    + " inner join tb_account ta on rad.account_id= ta.account_id "
	                                    + " where td.device_code=?1 and relation_state=0 ");
				
				q1.setParameter(1, tag);
				Object[] ob= (Object[])  q1.getSingleResult();
				Long account=ob[0]!=null?Long.parseLong(ob[0].toString()):0L;
				Long device=ob[1]!=null?Long.parseLong(ob[1].toString()):0L;
				Long userId=ob[2]!=null?Long.parseLong(ob[2].toString()):0L;
				
				if(!this.existVehicle(placa)){
					Long result=vehicleEjb.createVehicle(user,ip,placa, "", "",	0L, null,	cate,"", "", "", "",
							new Timestamp(System.currentTimeMillis()),"", userId);
					if(result!=0){
						flag=1;
					}
				}
				else{
					var=1;
					flag=1;
				}
				
				if(flag==1){
					//se actualiza el tag
					Query q2= em.createNativeQuery("update tag set vehiclelicenceplatenumber=?1, vehicleclass=?2 where equipmentobuid=?3 ");
					q2.setParameter(1, placa);
					q2.setParameter(2, cate);
					q2.setParameter(3, tag);
					q2.executeUpdate();
					
					//se busca el id del nuevo vehiculo
					q2= em.createNativeQuery(" select tv.vehicle_id from tb_vehicle tv  where tv.vehicle_plate=?1 ");
					q2.setParameter(1, placa);
					BigDecimal vehicle= (BigDecimal) q2.getSingleResult();
					System.out.println("vehicleId : " +  vehicle);
					
					if(var==1){
						//se actualiza la placa
						q2= em.createNativeQuery("update tb_vehicle set category_id=?1 where vehicle_plate=?2 ");
						q2.setParameter(1, cate);
						q2.setParameter(2, placa);
						q2.executeUpdate();
						
						q2= em.createNativeQuery("update re_user_vehicle set user_id=?1 where vehicle_id=?2");
						q2.setParameter(1, userId);
						q2.setParameter(2, vehicle);
						q2.executeUpdate();
						
					}
					
					//se inactiva la relacion antigua
					q2= em.createNativeQuery("update re_account_device set relation_state=1 where (vehicle_id=?1 or device_id=?2) and relation_state=0 ");
					q2.setParameter(1, vehicle);
					q2.setParameter(2, device);
					q2.executeUpdate();
					
					//se crea la nueva relacion
					q2= em.createNativeQuery("insert into re_account_device (account_device_id, account_id, device_id, vehicle_id, relation_state, " +
							                   "date_transaction) values (re_account_device_seq.nextval,?1,?2,?3,0,sysdate) ");
					q2.setParameter(1, account);
					q2.setParameter(2, device);
					q2.setParameter(3, vehicle);
					q2.executeUpdate();
					em.flush();
					
					res=true;
				}
				else{
					res=false;
					System.out.println("Error al crear o modificar placa");
				}
			}
			
			log.insertLog("Se realizó cambio en el tag : " + tag + " - Nueva placa:  " + placa + ", Nueva categoria: " 
					+ cate, LogReference.DEVICE, LogAction.UPDATE,ip, user);
			
		}
		catch(NoResultException ex){
			res=false;
			ex.printStackTrace();
			log.insertLog("Error al modificar placa y categoría del tag: " + tag, LogReference.DEVICE, LogAction.UPDATEFAIL,ip, user);
		}
		catch(Exception ex){
			res=false;
			ex.printStackTrace();
			log.insertLog("Error al modificar placa y categoría del tag: " + tag, LogReference.DEVICE, LogAction.UPDATEFAIL,ip, user);
		}
		return res;
	}
	
	@Override
	public Long isEnrollVehicleOtherDevice(String tag, String placa){
		Long res=-1L;
		
		try{
			Query q= em.createNativeQuery("select td.device_code from re_Account_device rad "
	                + " inner join tb_device td on rad.device_id= td.device_id "
					+ " inner join tb_vehicle tv on rad.vehicle_id= tv.vehicle_id "
                    + " where  rad.relation_state=0 and tv.vehicle_plate=?1 ");
            q.setParameter(1, placa);
            String t= (String) q.getSingleResult();
            
            if(t!=null){
            	if(!t.equals(tag)){
            		res=0L;
            	}
            	else{
            	    res=1L;	
            	}
            		
            }
            else{
            	res=-1L;
            }
            
		}catch(NoResultException ex){
			res=-1L;
		}
		catch(NonUniqueResultException ex){
			res=-2L;
		}
		
		return res;
	}
	
	@Override
	public boolean existVehicle(String placa){
		boolean res=false;
		try{
			Query q= em.createNativeQuery("select count(*) from tb_vehicle where vehicle_plate=?1");
			q.setParameter(1, placa);
			BigDecimal cont= (BigDecimal) q.getSingleResult();
			
			if(cont!=null){
				if(cont.longValue()>0){
					res=true;
				}
				else{
					res=false;
				}
				
			}
			else{
				res=false;
			}
		}catch(NoResultException ex){
			res=false;
		}
		return res;
	}
	
	@Override
	public boolean changeInfoTagReplace(String tag, String placa, Long cate, Long user, String ip){
		boolean res=false;
		int flag=0;
		int var=0;
		try{
			placa= placa.toUpperCase();
			//se busca la cuenta y el id del dispositivo que se recibe por parametro para crear el re_account_device
			Query q1= em.createNativeQuery("select ta.account_id, td.device_id, ta.user_id from re_Account_device rad "
                                    + " inner join tb_device td on rad.device_id= td.device_id "
                                    + " inner join tb_account ta on rad.account_id= ta.account_id "
                                    + " where td.device_code=?1 and relation_state=0 ");
			
			q1.setParameter(1, tag);
			Object[] ob= (Object[])  q1.getSingleResult();
			Long account=ob[0]!=null?Long.parseLong(ob[0].toString()):0L;
			Long device=ob[1]!=null?Long.parseLong(ob[1].toString()):0L;
			Long userId=ob[2]!=null?Long.parseLong(ob[2].toString()):0L;
			
			if(!this.existVehicle(placa)){
				Long result=vehicleEjb.createVehicle(user,ip,placa, "", "",	0L, null,	cate,"", "", "", "",
						new Timestamp(System.currentTimeMillis()),"", userId);
				if(result!=0){
					flag=1;
				}
			}
			else{
				var=1;
				flag=1;
			}
			
			if(flag==1){
				//inactivar el tag anterior
				Query q= em.createNativeQuery("update tag set equipmentstatus=2 where vehiclelicenceplatenumber=?1 ");
				q.setParameter(1, placa);
				q.executeUpdate();
				
				if(var==1){
					//se busca el id del vehiculo y del tag que tiene actualmente
					q= em.createNativeQuery(" select rad.device_id from re_Account_device rad "
		                                    + " inner join tb_vehicle tv on rad.vehicle_id= tv.vehicle_id "
		                                    + " where  rad.relation_state=0 and tv.vehicle_plate=?1 ");
					q.setParameter(1, placa);
					BigDecimal deviceOld= (BigDecimal) q.getSingleResult();

					
					q= em.createNativeQuery("update tb_device set device_state_id=7 where device_id=?1 ");
					q.setParameter(1, deviceOld);
					q.executeUpdate();
				}
				
				//se busca el id del nuevo vehiculo
				q= em.createNativeQuery(" select tv.vehicle_id from tb_vehicle tv  where tv.vehicle_plate=?1 ");
				q.setParameter(1, placa);
				BigDecimal vehicleId= (BigDecimal) q.getSingleResult();
				System.out.println("vehicleId : " +  vehicle);
				
				
				//se actualiza el nuevo tag
				q= em.createNativeQuery("update tag set vehiclelicenceplatenumber=?1, vehicleclass=?2 where equipmentobuid=?3 ");
				q.setParameter(1, placa);
				q.setParameter(2, cate);
				q.setParameter(3, tag);
				q.executeUpdate();

				
				if(var==1){
					//se actualiza la placa
					q= em.createNativeQuery("update tb_vehicle set category_id=?1 where vehicle_plate=?2 ");
					q.setParameter(1, cate);
					q.setParameter(2, placa);
					q.executeUpdate();
					
					q= em.createNativeQuery("update re_user_vehicle set user_id=?1 where vehicle_id=?2");
					q.setParameter(1, userId);
					q.setParameter(2, vehicleId);
					q.executeUpdate();
					
				}

				//se inactiva la relacion antigua
				q= em.createNativeQuery("update re_account_device set relation_state=1 where (vehicle_id=?1 or device_id=?2) and relation_state=0 ");
				q.setParameter(1, vehicleId);
				q.setParameter(2, device);
				q.executeUpdate();
				
				//se crea la nueva relacion
				q= em.createNativeQuery("insert into re_account_device(account_device_id, account_id, device_id, vehicle_id, relation_state, " +
						                   "date_transaction) values (re_account_device_seq.nextval,?1,?2,?3,0,sysdate) ");
				q.setParameter(1, account);
				q.setParameter(2, device);
				q.setParameter(3, vehicleId);
				q.executeUpdate();
				em.flush();
				
				res=true;

				log.insertLog("Se realizó cambio en el tag : " + tag + " - Nueva placa:  " + placa + ", Nueva categoría: " 
						+ cate, LogReference.DEVICE, LogAction.UPDATE,ip, user);
			}
			else{
				res=false;
				System.out.println("Error al crear o modificar placa");
			}
			
			
		}
		catch(NoResultException ex){
			res=false;
			ex.printStackTrace();
			log.insertLog("Error al modificar placa y categoría del tag: " + tag, LogReference.DEVICE, LogAction.UPDATEFAIL,ip, user);
		}
		catch(Exception ex){
			res=false;
			ex.printStackTrace();
			log.insertLog("Error al modificar placa y categoría del tag: " + tag, LogReference.DEVICE, LogAction.UPDATEFAIL,ip, user);
		}
		return res;
	}

	@Override
	public List<String> getTransactionDesc(Long accountId, Long vehicleId, Long userId,Timestamp dateIni, Timestamp dateEnd){
		
		List<String> lis= new ArrayList<String>();
		/*Query q= em.createNativeQuery("select tt.transaction_time||'|'||tt.account_id||'|'||tv.vehicle_plate||'|'||tt.category_id||'|'||td.device_facial_id||'|'||tt.original_id||'|'|| "
				 +" tc.company_name||'|'||tsb.station_bo_name||'|'||tl.lane_code||'|'||tt.sent||'|'||tt.transaction_value||'|'||decode(tt.transaction_type_id,2,tt.transaction_value,6,tt.transaction_value,0)||'|'|| "
				+" decode(tt.transaction_type_id,1,tt.transaction_value,5,tt.transaction_value,0)||'|'|| "
				+" decode(tt.transaction_type_id,2,'Descuento por paso de Peaje',1,'Asignacion de Recursos Aprobados por Banco',5,'Ajustes a la Transacción del dia '||tt.transaction_time,6,'Ajustes a la Transacción del dia '||tt.transaction_time)||'|'||tt.original_id||'|'||tc.fideicomiso||'|'||tc.nit_fideicomiso||'|'||tc.nro_contrato "
				+" from tb_transaction tt "
				+" left join tb_device td on tt.device_id= td.device_id "
				+" left join re_account_device rad on td.device_id= rad.device_id "
				+" inner join tb_account ta on tt.account_id= ta.account_id "
				+" left join tb_vehicle tv on rad.vehicle_id=tv.vehicle_id "
				+" left join tb_system_user tsu on tt.user_id=tsu.user_id "
				+" left join tb_lane tl on tt.lane_id= tl.lane_id "
				+" left join tb_station_bo tsb on tl.station_id= tsb.station_bo_id "
				+" left join tb_company tc on tsb.company_id= tc.company_id "
				+" where "
				+" tt.account_id=?1 "
				+" and tt.vehicle_id= ?2 "
				+" and (tt.user_id=?3 and tt.transaction_type_id in  (2,6)) "
				+" and tt.transaction_time between ?4 and ?5 "
				+" and (rad.relation_state=0  and tt.transaction_type_id in  (2,6)) "
				+" UNION "
				+" select tt.transaction_time||'|'||tt.account_id||'|'||tv.vehicle_plate||'|'||tt.category_id||'|'||td.device_facial_id||'|'||tt.original_id||'|'|| "
				+" tc.company_name||'|'||tsb.station_bo_name||'|'||tl.lane_code||'|'||tt.sent||'|'||tt.transaction_value||'|'||decode(tt.transaction_type_id,2,tt.transaction_value,6,tt.transaction_value,0)||'|'|| "
				+" decode(tt.transaction_type_id,1,tt.transaction_value,5,tt.transaction_value,0)||'|'|| "
				+" decode(tt.transaction_type_id,2,'Descuento por paso de Peaje',1,'Asignacion de Recursos Aprobados por Banco',5,'Ajustes a la Transacción del dia '||tt.transaction_time,6,'Ajustes a la Transacción del dia '||tt.transaction_time)||'|'||tt.original_id||'|'||tc.fideicomiso||'|'||tc.nit_fideicomiso||'|'||tc.nro_contrato "
				+" from tb_transaction tt "
				+" left join tb_device td on tt.device_id= td.device_id "
				+" left join re_account_device rad on td.device_id= rad.device_id "
				+" inner join tb_account ta on tt.account_id= ta.account_id "
				+" left join tb_vehicle tv on rad.vehicle_id=tv.vehicle_id "
				+" left join tb_system_user tsu on tt.user_id=tsu.user_id "
				+" left join tb_lane tl on tt.lane_id= tl.lane_id "
				+" left join tb_station_bo tsb on tl.station_id= tsb.station_bo_id "
				+" left join tb_company tc on tsb.company_id= tc.company_id "
				+" where "
				+" 	tt.account_id=?1 "
				+" and -1= ?2 "
				+" and (tt.user_id=?3 and tt.transaction_type_id in  (2,6)) "
				+" and tt.transaction_time between ?4 and ?5 "
				+" and (rad.relation_state=0  and tt.transaction_type_id in  (2,6)) "
				+" UNION "
				+" select tt.transaction_time||'|'||tt.account_id||'|'||tv.vehicle_plate||'|'||tt.category_id||'|'||td.device_facial_id||'|'||tt.original_id||'|'|| "
				+" tc.company_name||'|'||tsb.station_bo_name||'|'||tl.lane_code||'|'||tt.sent||'|'||tt.transaction_value||'|'||decode(tt.transaction_type_id,2,tt.transaction_value,6,tt.transaction_value,0)||'|'|| "
				+" decode(tt.transaction_type_id,1,tt.transaction_value,5,tt.transaction_value,0)||'|'|| "
				+" decode(tt.transaction_type_id,2,'Descuento por paso de Peaje',1,'Asignacion de Recursos Aprobados por Banco',5,'Ajustes a la Transacción del dia '||tt.transaction_time,6,'Ajustes a la Transacción del dia '||tt.transaction_time)||'|'||tt.original_id||'|'||tc.fideicomiso||'|'||tc.nit_fideicomiso||'|'||tc.nro_contrato "
				+" from tb_transaction tt "
				+" left join tb_device td on tt.device_id= td.device_id "
				+" left join re_account_device rad on td.device_id= rad.device_id "
				+" inner join tb_account ta on tt.account_id= ta.account_id "
				+" left join tb_vehicle tv on rad.vehicle_id=tv.vehicle_id "
				+" left join tb_system_user tsu on tt.user_id=tsu.user_id "
				+" left join tb_lane tl on tt.lane_id= tl.lane_id "
				+" left join tb_station_bo tsb on tl.station_id= tsb.station_bo_id "
				+" left join tb_company tc on tsb.company_id= tc.company_id "
				+" where "
				+" 	-1=?1 "
				+" and -1= ?2 "
				+" and (tt.user_id=?3 and tt.transaction_type_id in  (2,6)) "
				+" and tt.transaction_time between ?4 and ?5 "
				+" and (rad.relation_state=0  and tt.transaction_type_id in  (2,6)) "
		
				+" order by 1");
		
		q.setParameter(1, accountId);
		q.setParameter(2, vehicleId);
		q.setParameter(3, userId);
		q.setParameter(4, dateIni);
		q.setParameter(5, dateEnd);
		
		List<Object> list = (List<Object>)q.getResultList();
		
		for(Object o: list){
			lis.add(o.toString());
			System.out.println(o.toString());
		}
		*/
		return lis;
	}

	@Override
	public List<String> getTransactionRec(Long accountId, Long vehicleId,Long userId,Timestamp dateIni, Timestamp dateEnd){
		
		List<String> lis= new ArrayList<String>();
		/*Query q= em.createNativeQuery("select tt.transaction_time||'|'||tt.account_id||'|'||tv.vehicle_plate||'|'||tt.category_id||'|'||td.device_facial_id||'|'||tt.original_id||'|'|| "
					+" tc.company_name||'|'||tsb.station_bo_name||'|'||tl.lane_code||'|'||tt.sent||'|'||tt.transaction_value||'|'||decode(tt.transaction_type_id,2,tt.transaction_value,6,tt.transaction_value,0)||'|'|| "
					+" decode(tt.transaction_type_id,1,tt.transaction_value,5,tt.transaction_value,0)||'|'|| "
					+" decode(tt.transaction_type_id,2,'Descuento por paso de Peaje',1,'Asignacion de Recursos Aprobados por Banco',5,'Ajustes a la Transacción del dia '||tt.transaction_time,6,'Ajustes a la Transacción del dia '||tt.transaction_time)||'|'||tt.original_id||'|'||tc.fideicomiso||'|'||tc.nit_fideicomiso||'|'||tc.nro_contrato "
					+" from tb_transaction tt "
					+" left join tb_device td on tt.device_id= td.device_id "
					+" left join re_account_device rad on td.device_id= rad.device_id "
					+" inner join tb_account ta on tt.account_id= ta.account_id "
					+" left join tb_vehicle tv on rad.vehicle_id=tv.vehicle_id "
					+" left join tb_system_user tsu on tt.user_id=tsu.user_id "
					+" left join tb_lane tl on tt.lane_id= tl.lane_id "
					+" left join tb_station_bo tsb on tl.station_id= tsb.station_bo_id "
					+" left join tb_company tc on tsb.company_id= tc.company_id "
					+" where "
					+" tt.account_id=?1 "
					+" and tt.vehicle_id=?2 "
					+" and ((tt.user_id=?3 or tt.user_id is null) and tt.transaction_type_id in (1,5)) "
					+" and tt.transaction_time between ?4 and ?5 "
					+" and (tt.transaction_type_id in (1,5)) "
					+" UNION "
					+" select tt.transaction_time||'|'||tt.account_id||'|'||tv.vehicle_plate||'|'||tt.category_id||'|'||td.device_facial_id||'|'||tt.original_id||'|'|| "
					+" tc.company_name||'|'||tsb.station_bo_name||'|'||tl.lane_code||'|'||tt.sent||'|'||tt.transaction_value||'|'||decode(tt.transaction_type_id,2,tt.transaction_value,6,tt.transaction_value,0)||'|'|| "
					+" decode(tt.transaction_type_id,1,tt.transaction_value,5,tt.transaction_value,0)||'|'|| "
					+" decode(tt.transaction_type_id,2,'Descuento por paso de Peaje',1,'Asignacion de Recursos Aprobados por Banco',5,'Ajustes a la Transacción del dia '||tt.transaction_time,6,'Ajustes a la Transacción del dia '||tt.transaction_time)||'|'||tt.original_id||'|'||tc.fideicomiso||'|'||tc.nit_fideicomiso||'|'||tc.nro_contrato "
					+" from tb_transaction tt "
					+" left join tb_device td on tt.device_id= td.device_id "
					+" left join re_account_device rad on td.device_id= rad.device_id "
					+" inner join tb_account ta on tt.account_id= ta.account_id "
					+" left join tb_vehicle tv on rad.vehicle_id=tv.vehicle_id "
					+" left join tb_system_user tsu on tt.user_id=tsu.user_id "
					+" left join tb_lane tl on tt.lane_id= tl.lane_id "
					+" left join tb_station_bo tsb on tl.station_id= tsb.station_bo_id "
					+" left join tb_company tc on tsb.company_id= tc.company_id "
					+" where "
					+" tt.account_id=?1 "
					+" and -1=?2 "
					+" and ((tt.user_id=?3 or tt.user_id is null) and tt.transaction_type_id in (1,5)) "
					+" and tt.transaction_time between ?4 and ?5 "
					+" and (tt.transaction_type_id in (1,5)) "
					+" UNION "
					+" select tt.transaction_time||'|'||tt.account_id||'|'||tv.vehicle_plate||'|'||tt.category_id||'|'||td.device_facial_id||'|'||tt.original_id||'|'|| "
					+" tc.company_name||'|'||tsb.station_bo_name||'|'||tl.lane_code||'|'||tt.sent||'|'||tt.transaction_value||'|'||decode(tt.transaction_type_id,2,tt.transaction_value,6,tt.transaction_value,0)||'|'|| "
					+" decode(tt.transaction_type_id,1,tt.transaction_value,5,tt.transaction_value,0)||'|'|| "
					+" decode(tt.transaction_type_id,2,'Descuento por paso de Peaje',1,'Asignacion de Recursos Aprobados por Banco',5,'Ajustes a la Transacción del dia '||tt.transaction_time,6,'Ajustes a la Transacción del dia '||tt.transaction_time)||'|'||tt.original_id||'|'||tc.fideicomiso||'|'||tc.nit_fideicomiso||'|'||tc.nro_contrato "
					+" from tb_transaction tt "
					+" left join tb_device td on tt.device_id= td.device_id "
					+" left join re_account_device rad on td.device_id= rad.device_id "
					+" inner join tb_account ta on tt.account_id= ta.account_id "
					+" left join tb_vehicle tv on rad.vehicle_id=tv.vehicle_id "
					+" left join tb_system_user tsu on tt.user_id=tsu.user_id "
					+" left join tb_lane tl on tt.lane_id= tl.lane_id "
					+" left join tb_station_bo tsb on tl.station_id= tsb.station_bo_id "
					+" left join tb_company tc on tsb.company_id= tc.company_id "
					+" where "
					+" -1=?1 "
					+" and -1=?2 "
					+" and tt.account_id in (select account_id from tb_account where user_id=?3)"
					+" and ((tt.user_id=?3 or tt.user_id is null) and tt.transaction_type_id in (1,5)) "
					+" and tt.transaction_time between ?4 and ?5 "
					+" and (tt.transaction_type_id in (1,5)) "
					+" order by 1"); 
		
		q.setParameter(1, accountId);
		q.setParameter(2, vehicleId);
		q.setParameter(3, userId);
		q.setParameter(4, dateIni);
		q.setParameter(5, dateEnd);
		
		List<Object> list = (List<Object>)q.getResultList();
		
		for(Object o: list){
			lis.add(o.toString());
			System.out.println(o.toString());
		}
		*/
		return lis;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getTransactionDescRec(Long accountId, Long vehicleId,Long userId,Timestamp dateIni, Timestamp dateEnd){
		
		System.out.println("1.accountId:"+accountId);
		System.out.println("2.vehicleId:"+vehicleId);
		System.out.println("3.userId:"+userId);
		System.out.println("4.dateIni:"+dateIni);
		System.out.println("5.dateEnd:"+dateEnd);
		
		List<String> lis= new ArrayList<String>();
		Query q= em.createNativeQuery("SELECT * FROM TABLE(FUNC_REPORT_GENERAL_TXT(?1,?2,?3,?4,?5))"); 
		
		q.setParameter(1, accountId);
		q.setParameter(2, vehicleId);
		q.setParameter(3, userId);
		q.setParameter(4, dateIni);
		q.setParameter(5, dateEnd);
		
		List<Object> list = (List<Object>)q.getResultList();
		
		for(Object o: list){
			lis.add(o.toString());
			System.out.println(o.toString());
		}
		
		return lis;
	}
	
	@Override
	public String getFileTransactionPath(){
		try{
			//Query q= em.createNativeQuery("select system_parameter_value from tb_system_parameter where system_parameter_id=35");
			Query q =em.createQuery("select systemParameterValue from TbSystemParameter where systemParameterId in (35L)");
			String r=(String) q.getSingleResult();
			return r;
		}catch(Exception ex){
			return null;
		}
	}
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public Object[][] getSumaryByAccount(Long userId , Long accountId, Timestamp dateIni, Timestamp dateEnd){
	    BigDecimal previousBalance = null;
		Object[][] object = {{null,null,null,null,null,null,null,null,null}};
		
		if(accountId!=-1){
			try{
				Query q1= em.createNativeQuery("select tt.previous_balance "
						+ " from tb_transaction tt "
						+ " where tt.account_id=?1  and (tt.account_id is not null) and tt.transaction_time  between  ?2 and ?3 "
						+ " and transaction_time = (select min(tt1.transaction_time) "
						+ " from tb_transaction tt1 "
						+ " where tt1.account_id=?1 and (tt1.account_id is not null) and tt1.transaction_time  between ?2 and ?3) "); 
				
				q1.setParameter(1,accountId);
				q1.setParameter(2,dateIni);
				q1.setParameter(3,dateEnd);
		        q1.setMaxResults(1);
		        previousBalance=(BigDecimal) q1.getSingleResult();
				
			}catch(NoResultException ex){
				Query q1= em.createNativeQuery("select account_balance from tb_account where account_id=?1");
				q1.setParameter(1, accountId);
				previousBalance=(BigDecimal) q1.getSingleResult();
			}
			
			System.out.println("previousbalance" + previousBalance);
			Query q= em.createNativeQuery("select to_date(to_char(tt.transaction_time,'dd/mm/yyyy'),'dd/mm/yyyy') as fecha,  tt.transaction_type_id, "
					+ " 0 as previousbalance, "
					+ " decode(tt.transaction_type_id,2,sum(tt.transaction_value),6,sum(tt.transaction_value),4,sum(tt.transaction_value),0) as debitos, "
					+ " decode(tt.transaction_type_id,1,sum(tt.transaction_value),5,sum(tt.transaction_value),0) as creditos, "
					+ " 0 as newbalance, "
					+ " decode(tt.transaction_type_id,1,'Asignación de Recursos',2,'Paso Por Peaje',5,'Ajustes',6,'Ajustes',4, 'Crédito a Dsipositivos') as Transacciones,count(*) as cont, "
					+ " decode(tt.transaction_type_id,1,'Asignación de Recursos Aprobados',2,'Debitos Por Paso de Peaje',5,'Ajustes a la Cuenta FacilPass',6,'Ajustes a la Cuenta FacilPass',4,'Crédito a Dispositivos') as detalle "
					+ " from tb_transaction tt "
					+ " inner join tb_account ta on tt.account_id= ta.account_id " 
					+ " inner join tb_system_user tsu on ta.user_id= tsu.user_id "
					+ " where tt.account_id=?1 "
					+ " and tt.account_id is not null "
					+ " and ta.user_id= tsu.user_id "
					+ " and tsu.user_id=?2 "
					+ " and tt.transaction_type_id in (1,2,4,5,6) "
					+ " and tt.transaction_time between ?3 and ?4 "
					+ " group by to_date(to_char(tt.transaction_time,'dd/mm/yyyy'),'dd/mm/yyyy'), tt.transaction_type_id"
					
					+ " UNION  "
					+ " select to_date(to_char(tt.transaction_time,'dd/mm/yyyy'),'dd/mm/yyyy')   as fecha,tt.transaction_type_id, "
					+ " 0 as previousbalance, "
					+ " decode(tt.transaction_type_id,2,sum(tt.transaction_value),6,sum(tt.transaction_value),4,sum(tt.transaction_value),0) as debitos, "
					+ " decode(tt.transaction_type_id,1,sum(tt.transaction_value),5,sum(tt.transaction_value),0) as creditos, "
					+ " 0 as newbalance, "
					+ " decode(tt.transaction_type_id,1,'Asignación de Recursos',2,'Paso Por Peaje',5,'Ajustes',6,'Ajustes',4,'Crédito a Dispositivos') as Transacciones,count(*) as cont, "
					+ " decode(tt.transaction_type_id,1,'Asignación de Recursos Aprobados',2,'Debitos Por Paso de Peaje',5,'Ajustes a la Cuenta FacilPass',6,'Ajustes a la Cuenta FacilPass',4,'Crédito a Dispositivoss') as detalle "
					+ " from tb_transaction tt "
					+ " inner join tb_account ta on tt.account_id= ta.account_id "
					+ " inner join tb_system_user tsu on ta.user_id= tsu.user_id "
					+ " where -1=?1 "
					+ " and tt.account_id is not null "
					+ " and ta.user_id= tsu.user_id "
					+ " and tsu.user_id=?2 "
					+ " and tt.transaction_type_id in (1,2,4,5,6) "
					+ " and tt.transaction_time between ?3 and ?4 "
					+ " group by to_date(to_char(tt.transaction_time,'dd/mm/yyyy'),'dd/mm/yyyy'), tt.transaction_type_id"
					+ " order by 1");
			
			q.setParameter(1,accountId);
			q.setParameter(2,userId);
			q.setParameter(3,dateIni);
			q.setParameter(4,dateEnd);
			
			List<?> list = q.getResultList();	
			if(list.size()>0){
				object = new  Object[list.size()][9];
				BigDecimal newBalance = new BigDecimal(0) ;
				for(int i = 0; i <list.size(); i++){
					Object[] obj = (Object[]) list.get(i);
					
					if(i==0){
						object[i][1] = (BigDecimal)previousBalance;//saldoanterior
						newBalance=previousBalance.add(((BigDecimal)obj[4])).subtract((BigDecimal)obj[3]);//nuevosaldo
						object[i][4] = (BigDecimal) newBalance;
					}
					else{
						BigDecimal newBalance2 = (BigDecimal) object[i-1][4];
						object[i][1] = (BigDecimal)object[i-1][4];//saldoanterior
						object[i][4] = newBalance2.add(((BigDecimal)obj[4])).subtract((BigDecimal)obj[3]);//nuevosaldo
					}
					
					SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
					String fec= null;
					if(obj[0]!=null){
						fec=sdf.format(obj[0]);	
					}
					object[i][0] = fec ;//fecha
					//object[i][1] = obj[2] != null ? ((BigDecimal)obj[2]).longValue() : null;//saldoanterior
					object[i][2] = obj[3] != null ? ((BigDecimal)obj[3]).longValue() : null;//debitos
					object[i][3] = obj[4] != null ? ((BigDecimal)obj[4]).longValue() : null;//creditos
					//object[i][4] = obj[5] != null ? ((BigDecimal)obj[5]).longValue() : null;//nuevosaldo
					object[i][5] = obj[6] != null ? obj[6].toString(): null;//transacciones
					object[i][6] = obj[7] != null ? ((BigDecimal)obj[7]).longValue() : null;//cantidad
					object[i][7] = obj[8] != null ? obj[8].toString(): null;//detalle
					
					System.out.println("cont" + obj[7] != null ? ((BigDecimal)obj[7]).longValue() : null);
					
					System.out.println("newBalance" + newBalance);
				}

			}
		}
		else{
			Query q= em.createNativeQuery("select ta.account_id from tb_account ta where user_id=?1");
			List<Object> list=q.getResultList();
			
			for(Object o: list){
				BigDecimal account= (BigDecimal) o;
			}
			
		}
		
		return object;	
	}
	
	@Override
	public BigDecimal getPreviousBalance(Long accountId, Timestamp dateIni, Timestamp dateEnd){
		BigDecimal previousBalance;
		System.out.println("dateIni en getPreviousBalance" + dateIni);
		System.out.println("dateEnd en getPreviousBalance" + dateEnd);
		try{
			Query q1= em.createNativeQuery("select tt.previous_balance "
					+ " from tb_transaction tt "
					+ " where tt.account_id=?1 and tt.transaction_type_id in (1,2,4,5,6)  and (tt.account_id is not null) and tt.transaction_process_time  between  ?2 and ?3 "
					+ " and transaction_process_time = (select min(tt1.transaction_process_time) "
					+ " from tb_transaction tt1 "
					+ " where tt1.account_id=?1 and tt1.transaction_type_id in (1,2,4,5,6) and (tt1.account_id is not null) and tt1.transaction_process_time  between ?2 and ?3) "); 
			
			q1.setParameter(1,accountId);
			q1.setParameter(2,dateIni);
			q1.setParameter(3,dateEnd);
	        q1.setMaxResults(1);
	        previousBalance=(BigDecimal) q1.getSingleResult();
			
		}catch(NoResultException ex){
//			Query q1= em.createNativeQuery("select account_balance from tb_account where account_id=?1");
//			q1.setParameter(1, accountId);
			previousBalance=new BigDecimal(0);
		}
		System.out.println("previousbalance" + previousBalance);
		
		return previousBalance;
	}
	
	@Override
	public BigDecimal getNewBalance(Long accountId, Timestamp dateIni, Timestamp dateEnd){
		System.out.println("dateIni en getNewBalance" + dateIni);
		System.out.println("dateEnd en getNewBalance" + dateEnd);
		BigDecimal newBalance;
		try{
			Query q1= em.createNativeQuery("select tt.new_balance "
					+ " from tb_transaction tt "
					+ " where tt.account_id=?1  and tt.transaction_type_id in (1,2,4,5,6) and (tt.account_id is not null) and tt.transaction_process_time  between  ?2 and ?3 "
					+ " and transaction_process_time = (select max(tt1.transaction_process_time) "
					+ " from tb_transaction tt1 "
					+ " where tt1.account_id=?1 and  tt1.transaction_type_id in (1,2,4,5,6) and (tt1.account_id is not null) and tt1.transaction_process_time  between ?2 and ?3) "); 
			
			q1.setParameter(1,accountId);
			q1.setParameter(2,dateIni);
			q1.setParameter(3,dateEnd);
	        q1.setMaxResults(1);
	        newBalance=(BigDecimal) q1.getSingleResult();
			
		}catch(NoResultException ex){
//			Query q1= em.createNativeQuery("select account_balance from tb_account where account_id=?1");
//			q1.setParameter(1, accountId);
			newBalance=new BigDecimal(0);
		}
		System.out.println("newbalance" + newBalance);
		
		return newBalance;
	}
		
	
	@Override
	public List<TbAccount> getAvailableBalanceUserAccount(Long codeType,Long accountId, String codeClient) {
	  List<TbAccount> list = new ArrayList<TbAccount>();
	  try {	
			String qry = "";
			qry = "SELECT DISTINCT ta FROM TbAccount ta " ;

			if((accountId!=null) && (accountId!=-1L) && (!codeClient.equals(""))){
				Query userId1 = em.createQuery("SELECT su.userId FROM TbSystemUser su WHERE su.userCode = ?1 " +
						"AND su.tbCodeType.codeTypeId = ?2");
				userId1.setParameter(1,  codeClient.trim());
				userId1.setParameter(2, codeType);
				Long userId = (Long) userId1.getSingleResult();
				qry = qry+"WHERE ta.accountId ="+accountId+" ";
				qry = qry+"AND ta.tbSystemUser.userId ="+userId+" ";
			}else if(!codeClient.equals("")){
				Query userId1 = em.createQuery("SELECT su.userId FROM TbSystemUser su WHERE su.userCode = ?1 " +
						"AND su.tbCodeType.codeTypeId = ?2");
				userId1.setParameter(1,  codeClient.trim());
				userId1.setParameter(2, codeType);
				Long userId = (Long) userId1.getSingleResult();
				qry = qry+"WHERE ta.tbSystemUser.userId ="+userId+" ";
			}else if((accountId!=null) && (accountId!=-1L)){
				qry = qry+"WHERE ta.accountId ="+accountId+" ";
			}
				Query q = em.createQuery(qry);
				for (Object obj : q.getResultList()) {
					list.add((TbAccount) obj);
				}
		} catch (Exception e) {
				System.out.println(" [ Error en TransactionEJB.getAvailableBalanceUserAccount ] ");
				e.printStackTrace();
		}
		return list;
	}

}