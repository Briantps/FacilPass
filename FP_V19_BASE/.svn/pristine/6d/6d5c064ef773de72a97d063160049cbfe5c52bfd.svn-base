package ejb;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import constant.DeviceType;
import constant.DistributionType;
import constant.LogAction;
import constant.LogReference;
import constant.ProcessTrackDetailType;
import constant.ProcessTrackType;
import constant.TransactionType;
import constant.TypeTask;
import constant.VialTypeTag;

import crud.ObjectEM;
import ejb.taskeng.TransitTask;

import jpa.ReAccountDevice;
import jpa.ReDeviceTagType;
import jpa.TbAccount;
import jpa.TbDevice;
import jpa.TbDeviceState;
import jpa.TbDistributionType;
import jpa.TbProcessTrack;
import jpa.TbTag;

@Stateless(mappedName = "ejb/TypeDistribution")
public class TypeDistributionEJB implements TypeDistribution {

	@PersistenceContext(unitName = "bo")
	private EntityManager em;
	
	@EJB(mappedName = "ejb/User")
	private User user;
	
	@EJB(mappedName="ejb/Log")
	private Log log;	
	
	@EJB(mappedName="ejb/TransitTask")
	private TransitTask transitTask;
	
	@EJB(mappedName ="ejb/Transaction")
	private Transaction transaction;
	
	@EJB(mappedName="ejb/Process")
	private Process process;
	
	private ObjectEM emObj;
	
	public List<TbDistributionType> getDistributionType(){
		List<TbDistributionType> tipos = new ArrayList<TbDistributionType>();
		try {
		  Query q = em.createQuery("Select dt from TbDistributionType dt Order By dt.distributionTypeName");
		  for (Object object : q.getResultList()) {
				tipos.add((TbDistributionType) object);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tipos;
	}
	
	
	public boolean saveTypeDistribution(Long accountId, Long typeId, String ip, Long creationUser){
		boolean result = false;
		try {
			TbAccount cta = em.find(TbAccount.class, accountId);
			TbDistributionType t = em.find(TbDistributionType.class, typeId);
			cta.setDistributionTypeId(t);
			emObj = new ObjectEM(em);
			emObj.update(cta);
			
			if(typeId.longValue()==DistributionType.BAGMONEY.getDistributionID().longValue()){
				transitTask.createTask(TypeTask.ACCOUNT, cta.getAccountId().toString());
			}
				
			
			log.insertLog("Configuracion Dispositivo | Se ha cambiado el tipo de distribucion de la cuenta :  " + accountId +" a tipo "+t.getDistributionTypeName(),LogReference.ACCOUNT, LogAction.UPDATE,ip, creationUser);
			/*
			//creacion de proceso de cliente en Mi priceso
			TbProcessTrack idProc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, user.getSystemUser(creationUser).getUserId());
			Long newProc = null;
			if(idProc == null){
				newProc = process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, user.getSystemUser(creationUser).getUserId(), ip, creationUser);
			}else{
				newProc = idProc.getProcessTrackId();
			}
			process.createProcessDetail(newProc, ProcessTrackDetailType.MANUAL_RECHARGE,
					msgProcess, user.getSystemUser(creationUser).getUserId(), ip, 1, "Error al Crear Proceso para Recarga Manual de la cuenta "+cta.getAccountId());
			
			//creacion de proceso de cliente
			TbProcessTrack idProcClient = process.searchProcess(ProcessTrackType.CLIENT, user.getSystemUser(creationUser).getUserId());
			Long newProcClient = null;
			if(idProcClient == null){
				newProcClient = process.createProcessTrack(ProcessTrackType.CLIENT, user.getSystemUser(creationUser).getUserId(), ip, creationUser);
			}else{
				newProcClient = idProcClient.getProcessTrackId();
			}
			process.createProcessDetail(newProcClient, ProcessTrackDetailType.MANUAL_RECHARGE,
					msgProcess, user.getSystemUser(creationUser).getUserId(), ip, 1, "Error al Crear Proceso para Recarga Manual de la cuenta "+cta.getAccountId());
			
			*/
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
			return  result;
	}

	public void setLog(Log log) {
		this.log = log;
	}

	public Log getLog() {
		return log;
	}

	public void setEmObj(ObjectEM emObj) {
		this.emObj = emObj;
	}

	public ObjectEM getEmObj() {
		return emObj;
	}

	@Override
	public TbAccount getAccount(Long idAccount) {
		TbAccount cta = em.find(TbAccount.class, idAccount);		
		return cta;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public boolean applyChageInDevice(Long accountId, Long typeId, String ip, Long creationUser){
	 try{	
		TbAccount cta = em.find(TbAccount.class, accountId);	
		
		if((typeId.longValue()==DistributionType.BAGMONEY.getDistributionID().longValue()) || (typeId.longValue()==DistributionType.MANUAL.getDistributionID().longValue()))	{				
			System.out.println("typeId: "+typeId);
			System.out.println("accountId: "+accountId);
			Query q = em.createQuery("Select rad from ReAccountDevice rad where rad.tbAccount.accountId = ?1 and rad.tbDevice.tbDeviceType.deviceTypeId = ?2 and rad.tbDevice.tbDeviceState.deviceStateId not in (7,9) and rad.relationState=0");
			q.setParameter(1, accountId);			
			q.setParameter(2, DeviceType.TAG.getId());	
			
			Query q1=em.createNativeQuery("select min(val_min_alarm) from tb_alarm_balance where account_id=?1 and id_tip_alarm = 4");
			q1.setParameter(1, cta.getAccountId());
			BigDecimal value1=(BigDecimal) q1.getSingleResult();
			Long estado = 1L;
			Long estado2 = 6L;
			emObj = new ObjectEM(em);
			List items = q.getResultList();
			Iterator l = items.iterator();
			
			while (l.hasNext()) {		 
			  	ReAccountDevice d = (ReAccountDevice)l.next();				
				System.out.println("Dispositivo: "+d.getTbDevice().getDeviceCode());
		
				BigDecimal previous = cta.getAccountBalance();
				if(d.getTbDevice().getDeviceCurrentBalance().longValue() != 0L){
				
				 cta.setAccountBalance(cta.getAccountBalance().add(d.getTbDevice().getDeviceCurrentBalance()));
				
				 log.insertLog("Cambio configuracion de Cuenta | Cambio saldo cuenta: Anterior : " + previous + ", Actual: " + cta.getAccountBalance() + ".", 
						LogReference.ACCOUNT, LogAction.UPDATE, ip, creationUser);
			
				 emObj.update(cta);
				 previous = d.getTbDevice().getDeviceCurrentBalance();
				 d.getTbDevice().setDeviceCurrentBalance(new BigDecimal(0));
				
				Query qr = em.createQuery("select rdt from ReDeviceTagType rdt where tbDevice = ?1");
				
				qr.setParameter(1, d.getTbDevice());					
				
				ReDeviceTagType manufaturerId = (ReDeviceTagType) qr.getSingleResult();
				
				TbTag tag = em.find(TbTag.class, d.getTbDevice().getDeviceCode());
				if(tag == null){
					TbTag tagNew = new TbTag();						
					tagNew.setDeviceId(d.getTbDevice().getDeviceCode());
					tagNew.setDeviceCode(d.getTbDevice().getDeviceFacialId());
					tagNew.setDeviceCurrentBalance(d.getTbDevice().getDeviceCurrentBalance());
					tagNew.setTagTypeCode(new Long(manufaturerId.getTbTagType().getTagTypeCode()));
					tagNew.setCategoryId(d.getTbVehicle().getTbCategory().getCategoryId());
					tagNew.setDeviceStateId(1L);
					tagNew.setDeviceTypeId(VialTypeTag.PREPAGO.getTipo());
					tagNew.setVehiclePlate(d.getTbVehicle().getVehiclePlate());
					tagNew.setContractualAuth(5L);
					if((typeId.longValue()==DistributionType.BAGMONEY.getDistributionID().longValue())){
						tagNew.setAccountId(d.getTbAccount().getAccountId().longValue());
					} else{
						tagNew.setAccountId(null);
					}
					emObj = new ObjectEM(em);
					emObj.create(tagNew);
					transitTask.createTask(TypeTask.DEVICE, tagNew.getDeviceId());
				} else {
				 	tag.setDeviceCode(d.getTbDevice().getDeviceFacialId());
					tag.setDeviceCurrentBalance(d.getTbDevice().getDeviceCurrentBalance());
					if(d.getTbVehicle()!=null){
						if(d.getTbVehicle().getTbCategory()!=null){
							tag.setCategoryId(d.getTbVehicle().getTbCategory().getCategoryId());
						}	
					}
					tag.setDeviceTypeId(VialTypeTag.PREPAGO.getTipo());
					tag.setVehiclePlate(d.getTbVehicle()!=null?d.getTbVehicle().getVehiclePlate():"");
					tag.setTagTypeCode(new Long(manufaturerId.getTbTagType().getTagTypeCode()));
					if((typeId.longValue()==DistributionType.BAGMONEY.getDistributionID().longValue())){
						tag.setAccountId(d.getTbAccount().getAccountId().longValue());
					} else{
						tag.setAccountId(null);
					}
					emObj.update(tag);
					transitTask.createTask(TypeTask.DEVICE, tag.getDeviceId());
				}
			  }	
			}	
			//Despues de recargada la bolsa se calcula el estado para los tags dependiendo el saldo total de la bolsa
			if(value1!=null){
				System.out.print("value1: "+value1);
				if(cta.getAccountBalance().longValue()>value1.longValue()){
					System.out.println("con saldo");
					estado=1L;
					estado2=6L;
				}
				else if(cta.getAccountBalance().longValue()<=0){
					System.out.println("sin saldo");
					estado=3L;
					estado2=3L;
				}
				else if((cta.getAccountBalance().longValue()>0L) && (cta.getAccountBalance().longValue()<=value1.longValue())){
					System.out.println("con saldo bajo");
					estado=4L;
					estado2=4L;
				}	
			}
			if(estado!=null){
				q = em.createQuery("Select rad from ReAccountDevice rad where rad.tbAccount.accountId = ?1 and rad.tbDevice.tbDeviceType.deviceTypeId = ?2 and rad.tbDevice.tbDeviceState.deviceStateId not in (7,9) and rad.relationState=0");
				q.setParameter(1, accountId);			
				q.setParameter(2, DeviceType.TAG.getId());
				
				List items2 = q.getResultList();
				Iterator l2 = items2.iterator();
				TbDeviceState tde= em.find(TbDeviceState.class, estado2);
				while (l2.hasNext()) {		 
				  	ReAccountDevice d = (ReAccountDevice)l2.next();	
				  	TbTag tag = em.find(TbTag.class, d.getTbDevice().getDeviceCode());
				  	if(tag!=null){
				  		if(cta.getDistributionTypeId().getDistributionTypeId()==1L){
						    if(tag.getDeviceStateId()==1 || tag.getDeviceStateId()==3 ||tag.getDeviceStateId()==4){
							    System.out.println("tag activo");
							    System.out.println("estado :" + estado);
							    tag.setDeviceStateId(estado);	
							    emObj.update(tag);
							    
							    d.getTbDevice().setTbDeviceState(tde);
							    emObj.update(d.getTbDevice());
						    }
					     }
				  	}
				    
				}
			}
			
			
			
		 } else if (typeId.longValue()==DistributionType.AUTOMATIC.getDistributionID().longValue())	{
			 int numDevice = 0;
			 Query q = em.createQuery("Select rad from ReAccountDevice rad where rad.tbAccount = ?1 and rad.tbDevice.tbDeviceType.deviceTypeId = ?2 and rad.tbDevice.tbDeviceState.deviceStateId not in (7,9) and rad.relationState=0");
				q.setParameter(1, cta);			
				q.setParameter(2, DeviceType.TAG.getId());
				
				List items = q.getResultList();
				Iterator l = items.iterator();
				while (l.hasNext()) {	
					numDevice = numDevice+1;
				  	ReAccountDevice d = (ReAccountDevice)l.next();				
					System.out.println("Dispositivo: "+d.getTbDevice().getDeviceCode());
			
					BigDecimal previous = cta.getAccountBalance();
				  if(d.getTbDevice().getDeviceCurrentBalance().longValue() != 0L){
					cta.setAccountBalance(cta.getAccountBalance().add(d.getTbDevice().getDeviceCurrentBalance()));
					emObj = new ObjectEM(em);
					emObj.update(cta);
					
					
					log.insertLog("Cambio configuracion de Cuenta | Cambio saldo cuenta: Anterior : " + previous + ", Actual: " + cta.getAccountBalance() + ".", 
							LogReference.ACCOUNT, LogAction.UPDATE, ip, creationUser);
					
					
					previous = d.getTbDevice().getDeviceCurrentBalance();
					d.getTbDevice().setDeviceCurrentBalance(new BigDecimal(0));
					
					
					Query qr = em.createQuery("select rdt from ReDeviceTagType rdt where tbDevice = ?1");
					
					qr.setParameter(1, d.getTbDevice());					
					
					ReDeviceTagType manufaturerId = (ReDeviceTagType) qr.getSingleResult();
					
					TbTag tag = em.find(TbTag.class, d.getTbDevice().getDeviceCode());
					if(tag == null){
						TbTag tagNew = new TbTag();						
						tagNew.setDeviceId(d.getTbDevice().getDeviceCode());
						tagNew.setDeviceCode(d.getTbDevice().getDeviceFacialId());
						tagNew.setDeviceCurrentBalance(d.getTbDevice().getDeviceCurrentBalance());
						tagNew.setTagTypeCode(new Long(manufaturerId.getTbTagType().getTagTypeCode()));
						if(d.getTbVehicle()!=null){
							if(d.getTbVehicle().getTbCategory()!=null){
								tagNew.setCategoryId(d.getTbVehicle().getTbCategory().getCategoryId());	
							}
						}
						
						tagNew.setDeviceStateId(1L);
						tagNew.setDeviceTypeId(VialTypeTag.PREPAGO.getTipo());
						tagNew.setVehiclePlate(d.getTbVehicle().getVehiclePlate());
						tagNew.setContractualAuth(5L);
						if(d.getTbAccount().getDistributionTypeId().getDistributionTypeId()==1L){
							tagNew.setAccountId(d.getTbAccount().getAccountId().longValue());
						} else{
							tagNew.setAccountId(null);
						}
						emObj = new ObjectEM(em);
						emObj.create(tagNew);
						transitTask.createTask(TypeTask.DEVICE, tagNew.getDeviceId());
					} else {
						
					 	tag.setDeviceCode(d.getTbDevice().getDeviceFacialId());
						tag.setDeviceCurrentBalance(d.getTbDevice().getDeviceCurrentBalance());
						if(d.getTbVehicle()!=null){
							if(d.getTbVehicle().getTbCategory()!=null){
								tag.setCategoryId(d.getTbVehicle().getTbCategory().getCategoryId());	
							}
						}
						//tag.setDeviceStateId(1L);
						tag.setDeviceTypeId(VialTypeTag.PREPAGO.getTipo());
						tag.setVehiclePlate(d.getTbVehicle()!=null?d.getTbVehicle().getVehiclePlate():"");
						if(d.getTbAccount().getDistributionTypeId().getDistributionTypeId()==1L){
							tag.setAccountId(d.getTbAccount().getAccountId().longValue());
						} else{
							tag.setAccountId(null);
						}
						tag.setTagTypeCode(new Long(manufaturerId.getTbTagType().getTagTypeCode()));
						emObj = new ObjectEM(em);
						emObj.update(tag);
						transitTask.createTask(TypeTask.DEVICE, tag.getDeviceId());
					}	
				  }
				}
				
			//Recalcular saldo y redistribuirlos
			if(numDevice > 0){	
				BigDecimal newBalance = new BigDecimal((cta.getAccountBalance().longValue()/numDevice));
				
				for (int i=0; i < items.size();i++) {
					ReAccountDevice d = (ReAccountDevice)items.get(i);
					
					if(recharge1(d.getTbDevice().getDeviceId(), newBalance.longValue(), cta.getAccountId(), ip, creationUser, null, cta.getTbSystemUser().getUserEmail(),numDevice)){
						System.out.println("Recarga OK Dispositivo: "+d.getTbDevice().getDeviceCode());
					}
				}	
			}	
		 }		
		return true;
	 } catch(Exception e){
		 e.printStackTrace();
		 return false;
	 }
	}
	
	public boolean recharge(Long deviceId, Long value, Long accountId,
			String ip, Long creationUser, Long stationId, String email, int numDevice) {
		try {
			// searching the account and doing the validation
			TbAccount ta = em.find(TbAccount.class, accountId);
			
			// Then, updating the balance in the device Object.
			TbDevice device = em.find(TbDevice.class, deviceId);
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
					System.out.println("Tipo Transaccion: "+TransactionType.DEBIT);
					System.out.println("Dispositivo: "+device.getDeviceCode());
					System.out.println("Valor: "+value);
					System.out.println("IP: "+ip);
					System.out.println("Usuario: "+creationUser);
					System.out.println("Saldo Anterior: "+accountPreviousBalance);
					System.out.println("Nuevo Saldo: "+ta.getAccountBalance().longValue());
					
					System.out.print("numDevice en TypeDistributionEJB" + numDevice);
					TbTag tag = em.find(TbTag.class, device.getDeviceCode());
					if(tag.getDeviceStateId()==1 || tag.getDeviceStateId()==3 ||tag.getDeviceStateId()==4){
						System.out.println("tag activo");
						q=em.createNativeQuery("select min(val_min_alarm) from tb_alarm_balance where account_id=?1 and id_tip_alarm = 4");
						q.setParameter(1, ta.getAccountId());
						BigDecimal value1=(BigDecimal) q.getSingleResult();
						TbDeviceState tde;
						if(value1!=null){
							int valDef=value1.intValue()/numDevice;
							System.out.print("valDef: "+valDef);
							if(device.getDeviceCurrentBalance().longValue()>valDef){
								System.out.println("con saldo");
								tag.setDeviceStateId(1L);
								tde=em.find(TbDeviceState.class, 6L);
								device.setTbDeviceState(tde);
							}
							else if(device.getDeviceCurrentBalance().longValue()<=0){
								System.out.println("sin saldo");
								tag.setDeviceStateId(3L);
								tde=em.find(TbDeviceState.class, 3L);
								device.setTbDeviceState(tde);
							}
							else if((device.getDeviceCurrentBalance().longValue()>0L) && (device.getDeviceCurrentBalance().longValue()<=valDef)){
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
					// Creating the transaction
					
					// Indication in the device process track.
					TbProcessTrack  p = process.searchProcess(ProcessTrackType.DEVICE, device.getDeviceId());
					
					if (p != null) {
						// save the detail 
						process.createProcessDetail(p.getProcessTrackId(), ProcessTrackDetailType.DEVICE_RECHARGE,  "Se ha hecho una recarga al dispositivo por valor: $" +new DecimalFormat("#,###,###,###").format(value), creationUser, ip, 1, "No se ha podido registar en el proceso que Se ha hecho una recarga al dispositivo por valor: $" + new DecimalFormat("#,###,###,###").format(value)+ ". Proceso ID: " + p.getProcessTrackId(),null,null,null,null);
					} else {
						log.insertLog("Recarga | El Dispositivo ID: " + device.getDeviceId() + " no tiene proceso asociado.", LogReference.PROCESS, LogAction.NOTFOUND, ip, creationUser);
					}
						
				
					return true;
					
				} catch (NoResultException e) {
					// save log failure. 
					log.insertLog("Recarga | No se pudo guardar la transacción asociada a la estación ingresada.  valores: "+ "Fecha: " + new Timestamp(System.currentTimeMillis())+ ", Nuevo saldo: " + device.getDeviceCurrentBalance() 	+ ", Saldo anterior: " + oldBalanceDev+  ". Estacion ID: " +  stationId + ". Tipo : Crédito, Valor: " + value + "." ,   LogReference.TRANSACTION, LogAction.CREATEFAIL, ip, creationUser);
				}
				
			} else{
				//save log failure.
				log.insertLog("Recargar | No se pudo actualizar el valor del nuevo saldo en el dispositivo ID: " + device.getDeviceId() + ". Saldo anterior: " + oldBalanceDev + ", nuevo saldo: " + device.getDeviceCurrentBalance() + ".", LogReference.DEVICE, LogAction.UPDATEFAIL, ip, creationUser);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en TypeDistributionEJB.recharge ] ");
			e.printStackTrace();
		}
		return false;
	}

	public boolean recharge1(Long deviceId, Long value, Long accountId,
			String ip, Long creationUser, Long stationId, String email, int numDevice) {
		try {
			// searching the account and doing the validation
			TbAccount ta = em.find(TbAccount.class, accountId);
			
			// Then, updating the balance in the device Object.
			TbDevice device = em.find(TbDevice.class, deviceId);
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
					System.out.println("Tipo Transaccion: "+TransactionType.DEBIT);
					System.out.println("Dispositivo: "+device.getDeviceCode());
					System.out.println("Valor: "+value);
					System.out.println("IP: "+ip);
					System.out.println("Usuario: "+creationUser);
					System.out.println("Saldo Anterior: "+accountPreviousBalance);
					System.out.println("Nuevo Saldo: "+ta.getAccountBalance().longValue());
					
					try{
						Query q1 = em.createNativeQuery("Insert Into Tb_Transaction (TRANSACTION_ID,ACCOUNT_ID,TRANSACTION_TYPE_ID,TRANSACTION_TIME,PREVIOUS_BALANCE,NEW_BALANCE,TRANSACTION_VALUE,TRANSACTION_DESCRIPTION,CONSIGNMENT_ID,USER_ID,TRANSACTION_PROCESS_TIME) "
										+ " values(OFFICE.TB_TRANSACTION_SEQ.nextval,?1,4,?2,?3,?4,?5,?6 ,null,?7,?8)");
						
						q1.setParameter(1, accountId);
						q1.setParameter(2, new Timestamp(System.currentTimeMillis()));
						q1.setParameter(3, accountPreviousBalance);
						q1.setParameter(4,  ta.getAccountBalance().longValue());
						q1.setParameter(5,  value);
						q1.setParameter(6,  "Dispositivo No: " +device.getDeviceCode());
						q1.setParameter(7,  ta.getTbSystemUser().getUserId());
						q1.setParameter(8,  new Timestamp(System.currentTimeMillis()));
						
						q1.executeUpdate();
						
						} catch (NoSuchMethodError e){
							return false;					
						}
					
					
					System.out.print("numDevice en TypeDistributionEJB" + numDevice);
					TbTag tag = em.find(TbTag.class, device.getDeviceCode());
					if(tag.getDeviceStateId()==1 || tag.getDeviceStateId()==3 ||tag.getDeviceStateId()==4){
						System.out.println("tag activo");
						q=em.createNativeQuery("select min(val_min_alarm) from tb_alarm_balance where account_id=?1 and id_tip_alarm = 4");
						q.setParameter(1, ta.getAccountId());
						BigDecimal value1=(BigDecimal) q.getSingleResult();
						TbDeviceState tde;
						if(value1!=null){
							int valDef=value1.intValue()/numDevice;
							System.out.print("valDef: "+valDef);
							if(device.getDeviceCurrentBalance().longValue()>valDef){
								System.out.println("con saldo");
								tag.setDeviceStateId(1L);
								tde=em.find(TbDeviceState.class, 6L);
								device.setTbDeviceState(tde);
							}
							else if(device.getDeviceCurrentBalance().longValue()<=0){
								System.out.println("sin saldo");
								tag.setDeviceStateId(3L);
								tde=em.find(TbDeviceState.class, 3L);
								device.setTbDeviceState(tde);
							}
							else if((device.getDeviceCurrentBalance().longValue()>0L) && (device.getDeviceCurrentBalance().longValue()<=valDef)){
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
					
				   		Query q1 = em.createNativeQuery("Insert Into Tb_Transaction (TRANSACTION_ID,ACCOUNT_ID,TRANSACTION_TYPE_ID,TRANSACTION_TIME,PREVIOUS_BALANCE,NEW_BALANCE,TRANSACTION_VALUE,TRANSACTION_DESCRIPTION,DEVICE_ID,CONSIGNMENT_ID,USER_ID,TRANSACTION_PROCESS_TIME) "
								+ " values(OFFICE.TB_TRANSACTION_SEQ.nextval,?1,3,?2,?3,?4,?5,?6,?7 ,null,?8,?9)");
				
				        q1.setParameter(1, accountId);
				        q1.setParameter(2, new Timestamp(System.currentTimeMillis()));
				        q1.setParameter(3, oldBalanceDev);
				        q1.setParameter(4,   device.getDeviceCurrentBalance().longValue());
				        q1.setParameter(5,  value);
				        q1.setParameter(6,  "Transferencia del Monto al dispositivo" );
				        q1.setParameter(7,  device.getDeviceId());
				        q1.setParameter(8,  ta.getTbSystemUser().getUserId());
				        q1.setParameter(9,  new Timestamp(System.currentTimeMillis()));
				        
				        q1.executeUpdate();
				        
					} catch (NoSuchMethodError e){
						return false;					
					}	
					// Indication in the device process track.
					TbProcessTrack  p = process.searchProcess(ProcessTrackType.DEVICE, device.getDeviceId());
					
					if (p != null) {
						// save the detail 
						process.createProcessDetail(p.getProcessTrackId(), ProcessTrackDetailType.DEVICE_RECHARGE,  "Se ha hecho una recarga al dispositivo por valor: $" +new DecimalFormat("#,###,###,###").format(value), creationUser, ip, 1, "No se ha podido registar en el proceso que Se ha hecho una recarga al dispositivo por valor: $" + new DecimalFormat("#,###,###,###").format(value)+ ". Proceso ID: " + p.getProcessTrackId(),null,null,null,null);
					} else {
						log.insertLog("Recarga | El Dispositivo ID: " + device.getDeviceId() + " no tiene proceso asociado.", LogReference.PROCESS, LogAction.NOTFOUND, ip, creationUser);
					}
						
				
					return true;
					
				} catch (NoResultException e) {
					// save log failure. 
					log.insertLog("Recarga | No se pudo guardar la transacción asociada a la estación ingresada.  valores: "+ "Fecha: " + new Timestamp(System.currentTimeMillis())+ ", Nuevo saldo: " + device.getDeviceCurrentBalance() 	+ ", Saldo anterior: " + oldBalanceDev+  ". Estacion ID: " +  stationId + ". Tipo : Crédito, Valor: " + value + "." ,   LogReference.TRANSACTION, LogAction.CREATEFAIL, ip, creationUser);
				}
				
			} else{
				//save log failure.
				log.insertLog("Recargar | No se pudo actualizar el valor del nuevo saldo en el dispositivo ID: " + device.getDeviceId() + ". Saldo anterior: " + oldBalanceDev + ", nuevo saldo: " + device.getDeviceCurrentBalance() + ".", LogReference.DEVICE, LogAction.UPDATEFAIL, ip, creationUser);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en TypeDistributionEJB.recharge ] ");
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public String getDescriptionType(Long type) {
		String info="";
		try{
		 TbDistributionType td = em.find(TbDistributionType.class, type);
		 if(td!=null){
			 info=td.getDistributionTypeDescription();
		 }else{
			 info="";
		 }
		}catch(NoResultException e){
			System.out.println(" [ Error en TypeDistributionEJB.getDescriptionType ] ");
			e.printStackTrace();
		}
		return info;
	}
}

