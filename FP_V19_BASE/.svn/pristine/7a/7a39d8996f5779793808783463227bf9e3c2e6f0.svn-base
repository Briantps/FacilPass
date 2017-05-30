package ejb;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.swing.table.TableModel;

import util.ParserReadFiles;

import constant.DeviceType;
import constant.EmailProcess;
import constant.LogAction;
import constant.LogReference;
import constant.ProcessTrackDetailType;
import constant.ProcessTrackType;
import constant.RelationDeviceAccountState;
import constant.TypeTask;
import constant.VialTypeTag;

import crud.ObjectEM;
import ejb.email.Outbox;
import ejb.taskeng.TransitTask;

import jpa.ReAccountDevice;
import jpa.ReDeviceTagType;
import jpa.TbAccount;
import jpa.TbAccountType;
import jpa.TbTemp;
import jpa.TbDevice;
import jpa.TbDeviceFacial;
import jpa.TbDeviceHistory;
import jpa.TbDeviceState;
import jpa.TbDeviceType;
import jpa.TbLogAdminDevice;
import jpa.TbProcessTrack;
import jpa.TbTag;
import jpa.TbTagType;
import jpa.TbVehicle;

/**
 * Session Bean implementation class DeviceEJB
 */
@Stateless(mappedName = "ejb/Device")
public class DeviceEJB implements Device {
	
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext(unitName = "bo") 
	EntityManager em;
	
	private ObjectEM objectEM;
	
	@EJB(mappedName="ejb/Process")
	private Process process;
	
	@EJB(mappedName="ejb/Log")
	private Log log;
	
	@EJB(mappedName="ejb/TransitTask")
	private TransitTask transitTask;
	
	@EJB(mappedName ="ejb/email/Outbox")
	private Outbox outbox;
	
	private String courier;

    /**
     * Default constructor. 
     */
    public DeviceEJB() {
    }

    /*
     * (non-Javadoc)
     * @see ejb.Device#getAllDevices()
     */
	@Override
	public List<String> getAllDevices() {
		List<String> list = new ArrayList<String>();
 	    try {
			for(Object obj : em.createQuery("SELECT de FROM TbDevice de").getResultList()){
				list.add(((TbDevice)obj).getDeviceCode()+"-"+((TbDevice)obj).getTbDeviceType().getDeviceTypeName());
			}
		} catch (Exception e) {
			System.out.println(" [ Error en DeviceEJB.getAllDevices ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Device#getDevices()
	 */
	@Override
	public List<TbDevice> getDevices() {
		List<TbDevice> list = new ArrayList<TbDevice>();
		try {
			for (Object obj : em.createQuery("SELECT de FROM TbDevice de")
					.getResultList()) {
				list.add((TbDevice) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en DeviceEJB.getDevices ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Device#getDevicesWithNoAccount()
	 */
	@Override
	public List<TbDevice> getDevicesWithNoAccount() {
		List<TbDevice> list = new ArrayList<TbDevice>();
		try {
			Query q = em
					.createQuery("SELECT de FROM TbDevice de WHERE de.deviceId NOT IN (SELECT rad.tbDevice.deviceId FROM ReAccountDevice rad )");
			for (Object obj :q.getResultList()) {
				list.add((TbDevice) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en DeviceEJB.getDevicesWithNoAccount ] ");
			e.printStackTrace();
		}
		return list;
	}

	public boolean maxDevice(long idAccount){
		boolean res = false;
		try{
			TbAccount ta = em.find(TbAccount.class, idAccount);
			long md;
			
			if(ta!=null){
				TbAccountType tat=ta.getTbAccountType();
				if(tat!=null){
					List<?> list;
					md=tat.getMaxDevice();
					Query q = em.createQuery("SELECT DISTINCT r.tbDevice FROM ReAccountDevice r " +
					"WHERE r.tbAccount.accountId = ?1 and r.relationState=0");
	                q.setParameter(1, idAccount);
	               
	                list= q.getResultList();
	                if(list.size()>= md){
	                	res=false;
	                }
	                else{
	                	res=true;
	                }
				}
			}
		}catch(NoResultException ex){
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return res;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Device#saveAssociation(java.lang.Long, java.lang.Long,
	 * java.lang.String, java.lang.Long)
	 */
	@Override
	public boolean saveAssociation(Long idClientAccount, Long idDevice, Long idVehicle,String ip,
			Long creationUser) {
		int flag=0;
		Long contractualAuth=null;
		String valor=null;
		try {
			System.out.println("idDevice: "+ idDevice);
			// Searching the client account.
			TbAccount ta = em.find(TbAccount.class, idClientAccount);
			
			//Query q=em.createNativeQuery("select system_parameter_value from tb_system_parameter where system_parameter_id=34");
			Query q =em.createQuery("select systemParameterValue from TbSystemParameter where systemParameterId in (34L)");
			valor= (String) q.getSingleResult();
			if(valor!=null){
				contractualAuth= new Long(valor);
			}
			System.out.println("contractualAuth en saveAssociation" + contractualAuth);
			
			if (ta != null) {
			    Long state=ta.getAccountState();
				if(state==0L || state==7L){
					ReAccountDevice rad = new ReAccountDevice();
					rad.setTbAccount(ta);
					TbVehicle tv = null;
					TbDevice dev = em.find(TbDevice.class, idDevice);
					
					/*
					 * se comentarea cambio de estado en el dispositivo para dejarlo tal como esta en esta do preactivo
					 */
					
//					Long stateDisp=getInitDeviceState(idClientAccount,idDevice);
//					Long stateDisp2=1L;
//					if(stateDisp==1){
//						stateDisp2=6L;
//					}
//					else if(stateDisp==3){
//						stateDisp2=3L;
//					}
//					else if(stateDisp==4){
//						stateDisp2=4L;
//					}
//					System.out.println("stateDisp: " + stateDisp);
//					System.out.println("stateDisp2: " + stateDisp2);
//					
					TbDeviceState tde= em.find(TbDeviceState.class,15L);
				    dev.setTbDeviceState(tde);
					
					if(dev.getTbDeviceType().getDeviceTypeId() == 0L){
						tv = em.find(TbVehicle.class, idVehicle);
						rad.setTbVehicle(tv);
					} else{
						rad.setTbVehicle(null);
					}
					rad.setTbDevice(dev);
					
					rad.setRelationState(RelationDeviceAccountState.ACTIVE.getId());
					rad.setDateTransaction(new Timestamp(System.currentTimeMillis()));								
					
					objectEM = new ObjectEM(em);
					if(objectEM.update(dev)){
						if (objectEM.create(rad)) {
							log.insertLog(
									"Enrolar dispositivo a Cuenta | Se ha asociado la cuenta ID: "
									+ ta.getAccountId() + ", con el dispositivo ID: " + idDevice +".",
									LogReference.ACCOUNT, LogAction.CREATE, ip,
									creationUser);
							
							// Saving in the device process.
							TbProcessTrack pt = process.searchProcess(ProcessTrackType.DEVICE, idDevice);
							
							// searching client process
							TbProcessTrack pc= process.searchProcess(ProcessTrackType.CLIENT, ta.getTbSystemUser().getUserId());
							TbProcessTrack pClient= process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, ta.getTbSystemUser().getUserId());
							
							if (pt != null && pc!=null && pClient!=null) {
								process.createProcessDetail(pt.getProcessTrackId(),ProcessTrackDetailType.DEVICE_ACCOUNT_ASSOCIATION,
										"Se ha vinculado el Dispositivo Electrónico TAG con ID Facial: " + (dev!=null?dev.getDeviceFacialId():"")
										+" a la Cuenta FacilPass No. "+ ta.getAccountId() + ".", creationUser, ip, 1, 
										"No se pudo registar en el proceso la asociación del dispositivo ID: "
										+ idDevice + ", con la cuenta del cliente ID Account: " + idClientAccount + ". ID Proceso :"
										+ pt.getProcessTrackId()+".",null,null,null,null);
								process.createProcessDetail(pc.getProcessTrackId(),ProcessTrackDetailType.DEVICE_ACCOUNT_ASSOCIATION,
										"Se ha vinculado el Dispositivo Electrónico TAG con ID Facial: " + (dev!=null?dev.getDeviceFacialId():"")
										+" a la Cuenta FacilPass No. "+ ta.getAccountId() + ".", creationUser, ip, 1, "No se pudo registar en el proceso la asociación del dispositivo ID: "
										+ idDevice + ", con la cuenta del cliente ID Account: " + idClientAccount + ". ID Proceso :"
										+ pc.getProcessTrackId()+".",null,null,null,null);
								process.createProcessDetail(pClient.getProcessTrackId(),ProcessTrackDetailType.DEVICE_ACCOUNT_ASSOCIATION,
										"Se ha vinculado el Dispositivo Electrónico TAG con ID Facial: " + (dev!=null?dev.getDeviceFacialId():"")
										+" a la Cuenta FacilPass No. "+ ta.getAccountId() + ".", creationUser, ip, 1, "No se pudo registar en el proceso la asociación del dispositivo ID: "
										+ idDevice + ", con la cuenta del cliente ID Account: " + idClientAccount + ". ID Proceso :"
										+ pClient.getProcessTrackId()+".",null,null,null,null);
							} else {
								// Log
								log.insertLog("No se encontró un proceso para el Dispositivo ID: "
										+ idDevice, LogReference.PROCESS,
										LogAction.NOTFOUND, ip, creationUser);
							}
							if(dev.getTbDeviceType().getDeviceTypeId() == 0L){
							transitTask.createTask(TypeTask.ACCOUNT, ta.getAccountId().toString());
							
							/*
							 * Se cambia estado al crear registrro en tag de activo a preactivo para que asi baje a los carriles hasta
							 * que el usuario active el tag	
							 */

							BigDecimal manufaturerId=null;
							try{
								Query qr = em.createNativeQuery("select to_number(tt.tag_type_code) from re_device_tag_type rdt "+
										"inner join tb_device d on rdt.device_id=d.device_id "+
										"inner join tb_tag_type tt on rdt.tag_type_id=tt.tag_type_id "+
										"where d.device_id=?1 and tt.tag_state=1");
								qr.setParameter(1, dev.getDeviceId().longValue());	
								manufaturerId = (BigDecimal) qr.getSingleResult();
							}catch(Exception ex){
								ex.printStackTrace();
								flag=0;
							}

							
							
							TbTag tag=null;
							try{
								String t=dev.getDeviceCode();
								System.out.println("tag-S " + dev.getDeviceCode());
								System.out.println("tag-L " + t);
								tag= em.find(TbTag.class,t);
								System.out.println("tag" + tag);
								if(tag!=null){
									tag.setDeviceCode(dev.getDeviceFacialId());
									tag.setDeviceCurrentBalance(dev.getDeviceCurrentBalance());
									tag.setCategoryId(tv.getTbCategory().getCategoryId());
									tag.setDeviceStateId(15L);
									tag.setDeviceTypeId(VialTypeTag.PREPAGO.getTipo());
									tag.setTagTypeCode(manufaturerId!=null?manufaturerId.longValue():null);
									tag.setVehiclePlate(tv.getVehiclePlate());
									tag.setContractualAuth(contractualAuth);
									tag.setAccountId(ta.getAccountId());
									
									objectEM = new ObjectEM(em);
									if(objectEM.update(tag)){
										System.out.println(tag.toString());
										transitTask.createTask(TypeTask.DEVICE, tag.getDeviceId());
									}
									System.out.println("Se modificó tag");
								}
								else{
									System.out.println("tag igual a null");
									tag = new TbTag();
									
									tag.setDeviceId(dev.getDeviceCode());
									tag.setDeviceCode(dev.getDeviceFacialId());
									tag.setDeviceCurrentBalance(dev.getDeviceCurrentBalance());
									tag.setCategoryId(tv.getTbCategory().getCategoryId());
									tag.setDeviceStateId(15L);
									tag.setDeviceTypeId(VialTypeTag.PREPAGO.getTipo());
									tag.setTagTypeCode(manufaturerId!=null?manufaturerId.longValue():null);
									tag.setVehiclePlate(tv.getVehiclePlate());	
									tag.setContractualAuth(contractualAuth);
									tag.setAccountId(ta.getAccountId());
									
									objectEM = new ObjectEM(em);
									if(objectEM.create(tag)){
										System.out.println(tag.toString());
										transitTask.createTask(TypeTask.DEVICE, tag.getDeviceId());
									}
									System.out.println("Se creó tag");
								}						
							
							}catch(Exception ex){
								ex.printStackTrace();
								return false;
							}
							}
							
							//se crea registro en la tabla re_user_vehicle
							try{
								Query qq= em.createNativeQuery("select count(*) from re_user_vehicle where vehicle_id=?1 and state_relation=0");
								qq.setParameter(1, idVehicle);
								BigDecimal cont= (BigDecimal) qq.getSingleResult();
								System.out.println("cont: " +cont);
								    
								if(cont!=null){
								    if(cont.longValue()==0){
								    	Query qq1= em.createNativeQuery("insert into RE_USER_VEHICLE (USER_VEHICLE_ID,VEHICLE_ID,USER_ID,DATE_ASSOCIATION,STATE_RELATION) " +
								    			" values(RE_USER_VEHICLE_SEQ.NEXTVAL,?1,?2,systimestamp,0)");
										qq1.setParameter(1, idVehicle);
										qq1.setParameter(2, ta.getTbSystemUser().getUserId());
										qq1.executeUpdate();
										em.flush();
								    }
								}
	
							}catch(Exception ex){
								ex.printStackTrace();
							}
							
							TbLogAdminDevice tlad= new TbLogAdminDevice();
							tlad.setUserId(ta.getTbSystemUser().getUserId());
							tlad.setDeviceFacialIdOld("");
							tlad.setDeviceFacialIdNew(dev.getDeviceFacialId());
							tlad.setPlate(tv!=null?tv.getVehiclePlate():"");
							tlad.setCourierId(dev.getCourierId());
							tlad.setRollId(dev.getRollId());
							tlad.setCobro(null);
							tlad.setObservation("");
							tlad.setDateTransaction(new Timestamp(System.currentTimeMillis()));
							tlad.setActionName("Enrolar");
							tlad.setAccountId(ta.getAccountId());
							em.persist(tlad);
							
							flag=1;
							
						} else {
							flag=0;
							log.insertLog(
									"Asociar dispositivo a Cuenta | No se pudo asociar la cuenta ID: "
									+ ta.getAccountId() + ", con el dispositivo ID: " + idDevice +".",
									LogReference.ACCOUNT, LogAction.CREATEFAIL, ip,
									creationUser);
						}
					}
					 else {
						 flag=0;
						 log.insertLog(
									"No se pudo activar el dispositivo  con ID: " + idDevice +".",
									LogReference.ACCOUNT, LogAction.CREATEFAIL, ip,
									creationUser);
						}
				}
				else{
					flag=0;
					log.insertLog(
							"Asociar dispositivo a Cuenta | No se pudo asociar la cuenta ID: "
							+ ta.getAccountId() + ", con el dispositivo ID: " + idDevice + "Debido a que la cuenta facilpass no se encuentra asociada a un producto bancario.",
							LogReference.ACCOUNT, LogAction.CREATEFAIL, ip,
							creationUser);
					return false;
				}
				
				if(flag==0){
					return false;
				}
				if(flag==1){
					try{
						outbox.insertMessageOutbox(ta.getTbSystemUser().getUserId(), 
				                   EmailProcess.ASSOCIATION_DEVICE,
				                   (idClientAccount!=null?idClientAccount:0L),
				                   null,
				                   null, 
				                   (idDevice!=null?idDevice:0L),
				                   idVehicle,
				                   null,
				                   null,
				                   null,
				                   creationUser,
				                   null,
				                   null,
				                   null,
				                   true,
				                   null);
					}catch(Exception ex){
						ex.printStackTrace();
					}
					return true;
				}
			}
		} catch (NoResultException e) {
			e.printStackTrace();
			System.out.println("DeviceEJB.saveAssociation : No se encontró cuenta al cliente ID cuenta: "
							+ idClientAccount);
		} catch (Exception e) {
			System.out.println(" [ Error en DeviceEJB.saveAssociation ] ");
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Long getInitDeviceState(Long accountId, Long deviceId){
		Long state=1L;
		Long type;
		BigDecimal balance,balanceDisp, lowBalanceAccount;
		TbAccount ta= em.find(TbAccount.class, accountId);
		if(ta!=null){
			type=ta.getDistributionTypeId().getDistributionTypeId();
			balance=ta.getAccountBalance();
			try{
				//consulta valor de saldo bajo para la cuenta
				Query q= em.createNativeQuery("select val_min_alarm from tb_alarm_balance where account_id=?1 and id_tip_alarm=4");
				q.setParameter(1, accountId);
				lowBalanceAccount=(BigDecimal) q.getSingleResult();	
			}
			//consulta el valor parametrizado para valor de saldo bajo en caso que no encuetre nada en la tabla tb_alarm_balance
			catch(NoResultException ex){
				//Query q= em.createNativeQuery("select system_parameter_value from tb_system_parameter where system_parameter_id=4");
				Query q =em.createQuery("select systemParameterValue from TbSystemParameter where systemParameterId in (4L)");
				String var=(String) q.getSingleResult();
				lowBalanceAccount= new BigDecimal(var);
			}
			//bolsa
			if(type==1){
				System.out.println("Distribucion de bolsa de dinero");
				if(balance!=null){
					if(lowBalanceAccount!=null){
						if(balance.longValue()==0){
							state=3L;
						}
						else if ((balance.longValue()>0) && (balance.longValue()<=lowBalanceAccount.longValue())){
							state=4L;
						}
						else if(balance.longValue()>lowBalanceAccount.longValue()){
							state=1L;
						}
					}
				}
				else{
					state=3L;
				}
				System.out.println("Estado para los tags de la cuenta: "+ state);
				return state;
			}
			//automatica y manual
			else{
				state=3L;
//				BigDecimal cont;
//				BigDecimal value= new BigDecimal(0);
//				System.out.println("Distribucion diferente a bolsa de dinero");
//				TbDevice td= em.find(TbDevice.class, deviceId);
//				balanceDisp=td.getDeviceCurrentBalance();
//				
//				Query q1= em.createNativeQuery("select cast(count(*) as number) from re_account_device where account_id=?1 and relation_state=0");
//				q1.setParameter(1, accountId);
//				cont= (BigDecimal) q1.getSingleResult();
//				long balanceDisp2 = 0;
//				System.out.println("cont:" + cont);
//				if(lowBalanceAccount!=null){
//					if(cont!=null){
//						if(cont.longValue()>value.longValue()){
//							balanceDisp2=lowBalanceAccount.longValue()/cont.longValue();
//						}
//						else{
//							balanceDisp2=lowBalanceAccount.longValue();
//						}
//					}
//					else{
//						balanceDisp2=lowBalanceAccount.longValue();
//					}
//				}
//				
//				System.out.println("valor para cada tag: " + balanceDisp2);
//				
//				if (balanceDisp!=null){
//						if(balanceDisp.longValue()==0){
//							state=3L;
//						}
//						else if ((balanceDisp.longValue()>0) && (balanceDisp.longValue()<=balanceDisp2)){
//							state=4L;
//						}
//						else if(balanceDisp.longValue()>balanceDisp2){
//							state=1L;
//						}
//				}
//				else{
//					state=3L;
//				}
				System.out.println("Estado inicial para el tag cuando se crea debido a que se crea en 0 pesos"+ state);
				return state;
			}
		}
		return  state;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Device#getDevicesByClient(java.lang.Long)
	 */
	@Override
	public List<TbDevice> getDevicesByClient(Long idClient) {
		List<TbDevice> list = new ArrayList<TbDevice>();
		try {
			Query q = em.createQuery("SELECT DISTINCT rad.tbDevice FROM ReAccountDevice rad " +
							"WHERE rad.tbAccount.tbSystemUser.userId = ?1 and rad.relationState=0");
			q.setParameter(1, idClient);
			for (Object obj : q.getResultList()) {
				list.add((TbDevice) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en DeviceEJB.getDevicesByClient ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see ejb.Device#getDevicesTypes()
	 */
	@Override
	public List<TbDeviceType> getDevicesTypes() {
		List<TbDeviceType> list = new ArrayList<TbDeviceType>();
		try {
			Query q = em.createQuery("SELECT tdt FROM TbDeviceType tdt order by tdt.deviceTypeId");
			for (Object obj : q.getResultList()) {
				list.add((TbDeviceType) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en DeviceEJB.getDevicesTypes ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Device#getDeviceStates()
	 */
	@Override
	public List<TbDeviceState> getDeviceStates() {
		List<TbDeviceState> list = new ArrayList<TbDeviceState>();
		try {
			Query q = em.createQuery("SELECT tds FROM TbDeviceState tds");
			for (Object obj : q.getResultList()) {
				list.add((TbDeviceState) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en DeviceEJB.getDeviceStates ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see ejb.Device#getDeviceByCode(java.lang.String)
	 */
	@Override
	public TbDevice getDeviceByCode(String code) {
		try {
			System.out.println("code: "+code);
			Query q = em.createQuery("SELECT td FROM TbDevice td WHERE td.deviceCode = ?1");
			q.setParameter(1, code.toUpperCase());
			return (TbDevice) q.getSingleResult();
		} catch (NoResultException e){	
			System.out.println("[ No se encontró un dispositivo con código: "  + code +  ". ] ");
			return null;
		} catch (NonUniqueResultException e){
			System.out.println("[ Error en DeviceEJB: Hay más de un resultado para el código de dispositivo ingresado "
							+ code + " ]");
			return null;
		} catch (Exception e) {
			System.out.println(" [ Error en DeviceEJB.getDeviceByCode ] ");
			e.printStackTrace();
			return null;
		}		
	}
	
	/**
	 * Método creado para obtener dispositivo por facial.
	 * @author psanchez
	 */
	public String getDeviceByFacial(String facial) {
		try{
			 String result = null;
			 Query q = em.createQuery("SELECT td FROM TbDevice td WHERE td.deviceFacialId = ?1 ");
			 q.setParameter(1, facial.toUpperCase());
						
			 TbDevice d = (TbDevice)q.getSingleResult();
			 result = "Error: Existe un Dispositivo con el mismo facial: "+d.getDeviceFacialId()+ ", Verifique!!!" ;
			 System.out.println(" [ Error en DeviceEJB.getDeviceByFacial ] "+result+d.getDeviceFacialId());
			 return result;
			} catch (NoResultException e){	
				System.out.println("[ No se encontró un dispositivo con facial: "  + facial +  ". ] ");
			} catch (NonUniqueResultException e){
				System.out.println("[ Error en DeviceEJB: Hay más de un resultado para el facial de dispositivo ingresado "
								+ facial + " ]");
			} catch(Exception ex){
				System.out.println(" [ Error en DeviceEJB.getDeviceByFacial ] ");
				ex.printStackTrace();
			}
		return null;
	}
	
	
	/**
	 * Método creado para obtener dispositivo por facial.
	 * @author psanchez
	 */
	public TbDevice getDeviceByFacia(String facial) {
		try{
			 String result = null;
			 Query q = em.createQuery("SELECT td FROM TbDevice td WHERE td.deviceFacialId = ?1 ");
			 q.setParameter(1, facial.toUpperCase());
						
			 TbDevice d = (TbDevice)q.getSingleResult();
			 result = "Error: Existe un Dispositivo con el mismo facial: "+d.getDeviceFacialId()+ ", Verifique!!!" ;
			 System.out.println(" [ Error en DeviceEJB.getDeviceByFacial ] "+result+d.getDeviceFacialId());
			 return (TbDevice) q.getSingleResult();
			} catch (NoResultException e){	
				System.out.println("[ No se encontró un dispositivo con facial: "  + facial +  ". ] ");
				return null;
			} catch (NonUniqueResultException e){
				System.out.println("[ Error en DeviceEJB: Hay más de un resultado para el facial de dispositivo ingresado "
								+ facial + " ]");
				return null;
			} catch(Exception ex){
				System.out.println(" [ Error en DeviceEJB.getDeviceByFacial ] ");
				ex.printStackTrace();
				return null;
			}
	}
	
	@Override
	public boolean saveDeviceCompanyTag(String code, String facial, Long balance,
			Long deviceType, Long deviceState, Long companyTagId, Long courierId, Long rollId, String ip, Long creationUser) {
		try {
			String value2=null;
			value2=code.replace(".0", "");
			TbDevice device = new TbDevice();
			device.setDeviceCode(value2);
			if(balance != null) {
				device.setDeviceCurrentBalance(new BigDecimal(balance));
			} else {
				device.setDeviceCurrentBalance(new BigDecimal(0));
			}
			if(facial != null) {
				String valuel=null;
				valuel=facial.replace(".0", "");
				device.setDeviceFacialId(valuel.toUpperCase());
			}
			if(deviceState != null) {
				device.setTbDeviceState(em.find(TbDeviceState.class, deviceState));
			}
			if(deviceType != null) {
				device.setTbDeviceType(em.find(TbDeviceType.class, deviceType));
			}
			device.setDeviceEntryDate(new Timestamp(System.currentTimeMillis()));
			
			device.setCourierId(courierId);
			device.setRollId(rollId);
			device.setValidateDeviceStateId(null);
			device.setDevicePaid(false);
			
			objectEM = new ObjectEM(em);	
			if(objectEM.create(device)){
				log.insertLog("Guardar Dispositivo | Se Ha guardado el dispositivo ID: "
						+ device.getDeviceId() + ".", LogReference.DEVICE, LogAction.CREATE, ip,
						creationUser);
				
				// Creating device process
				Long idProcess = process.createProcessTrack(ProcessTrackType.DEVICE, device.getDeviceId(), ip,
						creationUser);
				
				if (idProcess != null) {
					// Creating process detail 
					process.createProcessDetail(idProcess, ProcessTrackDetailType.DEVICE_CREATION,
							"Se ha Registrado el dispositivo en el Sistema.",
							creationUser, ip, 1,"No se ha podido Indicar en el proceso que el dispositivo código: "
									+ value2 + " ha sido registrado al sistema. ID Proceso: " + idProcess + ".",null,null,null,null);
				}
				return true;
			} else {
				log.insertLog("Guardar Dispositivo | No se pudo guardar el dispositivo Código: "
						+ value2 +".", LogReference.DEVICE, LogAction.CREATEFAIL, ip,
						creationUser);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en DeviceEJB.saveDevice ] ");
			e.printStackTrace();
		}
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Device#saveCard(java.lang.String, java.lang.String,
	 * java.lang.Long, java.lang.Long, java.lang.Long, java.lang.String,
	 * java.lang.Long)
	 */
	@Override
	public boolean saveDevice(String code, String facial, Long balance,
			Long deviceType, Long deviceState,Long courierId, Long rollId, String ip, Long creationUser) {
		System.out.println(" [ code] "+code);
		System.out.println(" [ facial] "+facial);
		System.out.println(" [ balance] "+balance);
		System.out.println(" [ deviceType] "+deviceType);
		System.out.println(" [ deviceState] "+deviceState);
		System.out.println(" [ courierId] "+courierId);
		System.out.println(" [ rollId] "+rollId);

		try {
			String value2=null;
			value2=code.replace(".0", "");
			TbDevice device = new TbDevice();
			device.setDeviceCode(value2);
			if(balance != null) {
				device.setDeviceCurrentBalance(new BigDecimal(balance));
			} else {
				device.setDeviceCurrentBalance(new BigDecimal(0));
			}
			if(facial != null) {
				String valuel=null;
				valuel=facial.replace(".0", "");
				device.setDeviceFacialId(valuel.toUpperCase());
			}
			if(deviceState != null) {
				device.setTbDeviceState(em.find(TbDeviceState.class, deviceState));
			}
			if(deviceType != null) {
				device.setTbDeviceType(em.find(TbDeviceType.class, deviceType));
			}
			device.setDeviceEntryDate(new Timestamp(System.currentTimeMillis()));
			
			device.setCourierId(courierId);
			device.setRollId(rollId);
			
			device.setDevicePaid(false);
			
			objectEM = new ObjectEM(em);	
			if(objectEM.create(device)){
				log.insertLog("Guardar Dispositivo | Se Ha guardado el dispositivo ID: "
						+ device.getDeviceId() + ".", LogReference.DEVICE, LogAction.CREATE, ip,
						creationUser);
				
				// Creating device process
				Long idProcess = process.createProcessTrack(ProcessTrackType.DEVICE, device.getDeviceId(), ip,
						creationUser);
				
				if (idProcess != null) {
					// Creating process detail 
					process.createProcessDetail(idProcess, ProcessTrackDetailType.DEVICE_CREATION,
							"Se ha Registrado el dispositivo en el Sistema.",
							creationUser, ip, 1,"No se ha podido Indicar en el proceso que el dispositivo código: "
									+ value2 + " ha sido registrado al sistema. ID Proceso: " + idProcess + ".",null,null,null,null);
				}
				return true;
			} else {
				log.insertLog("Guardar Dispositivo | No se pudo guardar el dispositivo Código: "
						+ value2 +".", LogReference.DEVICE, LogAction.CREATEFAIL, ip,
						creationUser);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en DeviceEJB.saveDevice ] ");
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Device#createRealationtagType(java.lang.Long, java.lang.Long,
	 * java.lang.String, java.lang.Long)
	 */
	@Override
	public boolean createRealationtagType(Long deviceId, Long tagTypeId,
			String ip, Long creationUser) {
		try {
			ReDeviceTagType dt = new ReDeviceTagType();
			dt.setTbDevice(em.find(TbDevice.class, deviceId));
			dt.setTbTagType(em.find(TbTagType.class, tagTypeId));
			
			objectEM = new ObjectEM(em);	
			if(objectEM.create(dt)) {
				log.insertLog("Relación Dispositivo - Tipo Tag | Se ha guardado ID: "  + dt.getDeviceTagTypeId() + ".",
						LogReference.DEVICE, LogAction.CREATE, ip, creationUser);
				return true;
			} else {
				log.insertLog("Relación Dispositivo - Tipo Tag | No se ha podido guardar la relación ID " +
						"dispositivo, ID tipo tag: " + deviceId + ", " + tagTypeId  + ".",
						LogReference.DEVICE, LogAction.CREATEFAIL, ip, creationUser);
				return false;
			}
		} catch(NullPointerException e){
			System.out.println(" [ Error en DeviceEJB.createRealationtagType ] ");
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			System.out.println(" [ Error en DeviceEJB.createRealationtagType ] ");
			e.printStackTrace();
			return false;
		} 
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Device#getDeviceFacial(constant.DeviceType, java.lang.Long)
	 */
	@Override
	public TbDeviceFacial getDeviceFacial(DeviceType deviceTypeId,
			Long companyId, String ip, Long creationUser) {
		try {
			Query q = em.createQuery("SELECT df FROM TbDeviceFacial df WHERE df.tbDeviceType.deviceTypeId = ?1 " +
					"AND df.tbCompany.companyId = ?2 ");
			q.setParameter(1, deviceTypeId.getId());
			q.setParameter(2, companyId);
			
			return (TbDeviceFacial) q.getSingleResult();
		} catch (NoResultException ne) {
			log.insertLog("Buscar Facial Dispositivo | No se encontró registro para el Tipo de Dispositivo ID:" + deviceTypeId +
					" e ID Compañía: " + companyId + ".", LogReference.DEVICEFACIAL, LogAction.NOTFOUND, ip, creationUser);
		} catch (Exception e) {
			System.out.println(" [ Error en DeviceEJB.getDeviceFacial ] ");
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Device#getAvailableDeviceByType(java.lang.Long)
	 */
	@Override
	public List<TbDevice> getAvailableDeviceByType(Long deviceTypeId) {
		List<TbDevice> list = new ArrayList<TbDevice>();
		try {
			String query = "SELECT de FROM TbDevice de ";
			if(deviceTypeId.longValue() != -1L) {
				query += " WHERE de.tbDeviceType.deviceTypeId = ?1";
			} else {
				query += " WHERE de.tbDeviceType IS NULL ";
			}
			Query q = em.createQuery(query);
			
			if(deviceTypeId.longValue() != -1L) {
				q.setParameter(1, deviceTypeId);
			}
			
			for (Object obj :q.getResultList()) {
				list.add((TbDevice) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en DeviceEJB.getAvailableDeviceByType ] ");
			e.printStackTrace();
		}
		return list;
	}
	
	
	public List<TbDevice> getAvailableDeviceByTypeNoAssig(Long deviceTypeId) {
		List<TbDevice> list = new ArrayList<TbDevice>();
		try {
			String query = "SELECT * FROM Tb_Device ";
			if(deviceTypeId.longValue() != -1L) {
				query += " WHERE device_Type_Id = ?1";
			} else {
				query += " WHERE device_Type_Id IS NULL ";
			}
			query += " and (device_state_id not in (9)) AND device_Id  not in (select device_id from re_account_device where device_id is not null and relation_state=0)";
			System.out.println(query);
			Query q = em.createNativeQuery(query,TbDevice.class);
			
			if(deviceTypeId.longValue() != -1L) {
				q.setParameter(1, deviceTypeId);
			}
			
			for (Object obj :q.getResultList()) {
				list.add((TbDevice) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en DeviceEJB.getAvailableDeviceByTypeNoAssig ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Device#getAccountDevice(java.lang.Long)
	 */
	@Override
	public List<ReAccountDevice> getAccountDevice(Long deviceId) {
		List<ReAccountDevice> list = new ArrayList<ReAccountDevice>();
		try {
			Query q = em.createQuery("SELECT de FROM ReAccountDevice de WHERE de.tbDevice.deviceId = ?1");
			q.setParameter(1, deviceId);
			for (Object obj :q.getResultList()) {
				list.add((ReAccountDevice) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en DeviceEJB.getAccountDevice ] ");
			e.printStackTrace();
		}
		return list;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Device#getAccountDevice(java.lang.Long)
	 */
	@Override
	public List<ReAccountDevice> getAccountDeviceByAccount(Long accountId) {
		List<ReAccountDevice> list = new ArrayList<ReAccountDevice>();
		try {
			Query q = em.createQuery("SELECT de FROM ReAccountDevice de WHERE de.tbAccount.accountId = ?1 and de.relationState=0");
			q.setParameter(1, accountId);
			for (Object obj :q.getResultList()) {
				list.add((ReAccountDevice) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en DeviceEJB.getAccountDevice ] ");
			e.printStackTrace();
		}
		return list;
	}
	

	/*
	 * (non-Javadoc)
	 * @see ejb.Device#getDevicesByAccount(java.lang.Long)
	 */
	@Override
	public List<TbDevice> getDevicesByAccount(Long idAccount) {
		List<TbDevice> list = new ArrayList<TbDevice>();
		try {
			Query q = em.createQuery("SELECT DISTINCT rad.tbDevice FROM ReAccountDevice rad " +
							"WHERE rad.tbAccount.accountId = ?1 and rad.relationState=0");
			q.setParameter(1, idAccount);
			for (Object obj : q.getResultList()) {
				list.add((TbDevice) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en DeviceEJB.getDevicesByAccount ] ");
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Device#getDevicesByAccountAndType(java.lang.Long,
	 * constant.DeviceType)
	 */
	@Override
	public List<TbDevice> getDevicesByAccountAndType(Long idAccount,
			DeviceType deviceType) {
		List<TbDevice> list = new ArrayList<TbDevice>();
		try {
			Query q = em.createQuery("SELECT DISTINCT rad.tbDevice FROM ReAccountDevice rad " +
					"WHERE rad.tbAccount.accountId = ?1 AND rad.tbDevice.tbDeviceType.deviceTypeId = ?2 and rad.relationState=0");
			q.setParameter(1, idAccount);
			q.setParameter(2, deviceType.getId());
			
			for (Object obj : q.getResultList()) {
				list.add((TbDevice) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en DeviceEJB.getDevicesByAccountAndType ] ");
			e.printStackTrace();
		}
		return list;
	}
	
	
	/**
	 * Método creado para obtener dispositivo por facial.
	 * @author psanchez
	 */
	public Long getDevicesByAccountClient(Long accountBankId) {
		try{
			 Query q = em.createNativeQuery("SELECT red.account_id FROM re_account_device red " +
			 		                        "INNER JOIN re_account_bank rab on red.account_id = rab.account_id " +
			 		                        "WHERE rab.account_bank_id = ?1 and rad.relation_state=0");
			 q.setParameter(1, accountBankId);
			 String result = (String) q.getSingleResult();	
			 System.out.println("result"+result);
			} catch (NoResultException e){	
				System.out.println("[ No se encontró un dispositivo con facial: "  + accountBankId +  ". ] ");
			} catch (NonUniqueResultException e){
				System.out.println("[ Error en DeviceEJB: Hay más de un resultado para el facial de dispositivo ingresado "
								+ accountBankId + " ]");
			} catch(Exception ex){
				System.out.println(" [ Error en DeviceEJB.getDeviceByFacial ] ");
				ex.printStackTrace();
			}
		return null;
	}

	public void setTransitTask(TransitTask transitTask) {
		this.transitTask = transitTask;
	}

	public TransitTask getTransitTask() {
		return transitTask;
	}
	
	
	@Override
	public TbAccount getAccountByDeviceCode(String deviceCode){
		Long account = null;
		TbAccount account2 = null;
		try{
			Query q= em.createNativeQuery("select rad.account_id from re_account_device rad "
                    + " inner join tb_device td on rad.device_id=td.device_id " 
                    + " inner join tb_account ta on rad.account_id=ta.account_id "
                    + " where td.device_code=?1 and rad.relation_state=0");
					q.setParameter(1,deviceCode);

					BigDecimal cuenta=(BigDecimal) q.getSingleResult();

					if(cuenta!=null){
						account=cuenta.longValue();
						account2= em.find(TbAccount.class, account);
					}
		}catch(NoResultException ex){
			System.out.println("No se encontro cuenta con el dispositivo " + deviceCode);
		}
	
		return account2;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ReAccountDevice> searchReAccountDevice(Long type, String value) {
		List<ReAccountDevice> list= new ArrayList<ReAccountDevice>();
		
		System.out.println("type: " + type);
		System.out.println("value: " + value);
		
		String consulta="select rad from ReAccountDevice rad, TbDevice td, TbVehicle tv "+
                        "where  rad.tbDevice.deviceId = td.deviceId "+
                        "and rad.tbVehicle.vehicleId= tv.vehicleId "+
                        "and rad.relationState =0 ";
		
		if(type==1L){
			consulta=consulta+" and tv.vehiclePlate=?1 ";
		}
		else if(type==2L){
			consulta=consulta+" and td.deviceCode=?1 ";
		}
		else{
			consulta=consulta+" and td.deviceFacialId=?1 ";
		}
		System.out.println("consulta: " + consulta);
		Query q= em.createQuery(consulta);
		q.setParameter(1, value);
		
		list=q.getResultList();
		
		return list;
	}

	@Override
	public boolean disableReAccountDevice(Long accountId, String deviceCode, Long relationId,String vehiclePlate, String ip, Long user){
		boolean res=false;
		try{
			System.out.println("DeviceEjb.disableReAccountDevice");
			System.out.println("Entre al metodo en el ejb user: " + user);
			Query q= em.createNativeQuery("update tb_device set device_state_id=7 where device_code=?1");
			q.setParameter(1, deviceCode);
			int i=q.executeUpdate();
			System.out.println("i: "+ i);
			if(i>0){
				q=em.createNativeQuery("update re_account_device " +
						"set relation_state=?1,date_transaction=systimestamp " +
						"where account_device_id=?2");
				
				q.setParameter(2, relationId);
				q.setParameter(1, RelationDeviceAccountState.INACTIVE.getId());
				int i2= q.executeUpdate();
				System.out.println("i2: "+ i2);
				if(i2>0){
					q= em.createNativeQuery("update tag set equipmentstatus=2 where equipmentobuid=?3");
					q.setParameter(3, deviceCode);
					int i3=q.executeUpdate();
					System.out.println("i3: "+ i3);
					
						Query q2= em.createNativeQuery("select device_id from tb_device where device_code=?1");
						q2.setParameter(1, deviceCode);
						BigDecimal deviceId= (BigDecimal) q2.getSingleResult();
						Long idClient = null;
						TbAccount ta=em.find(TbAccount.class, accountId);
						if(ta!=null && deviceId!=null){
							TbDevice td = em.find(TbDevice.class, deviceId.longValue());
							BigDecimal valor=td.getDeviceCurrentBalance();
							BigDecimal valor2;
							System.out.println("saldo cuenta: "+ ta.getAccountBalance());
							System.out.println("valor: "+ valor);
							valor2=ta.getAccountBalance().add(valor);
							idClient=ta.getTbSystemUser().getUserId();
							System.out.println("valor2: "+ valor2);
							Query q3= em.createNativeQuery("update tb_account set account_balance=?1 where account_id=?2");
							q3.setParameter(1, valor2).setParameter(2, ta.getAccountId());
							int i4=q3.executeUpdate();
							if(i4>0){
								q3= em.createNativeQuery("update tb_device set device_current_balance=0 where device_id=?1");
								q3.setParameter(1, deviceId.longValue());
								q3.executeUpdate();
							}

							TbProcessTrack pt = process.searchProcess(ProcessTrackType.DEVICE, deviceId.longValue());
							TbProcessTrack ptClient = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, idClient);
							TbProcessTrack pclient2 = process.searchProcess(ProcessTrackType.CLIENT, idClient);
							Long device, cclient,pc;
							if (pt == null) {
								device=process.createProcessTrack(ProcessTrackType.DEVICE, deviceId.longValue(), ip, user);
								cclient=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, idClient, ip, user);
								pc=process.createProcessTrack(ProcessTrackType.CLIENT, idClient, ip, user);
							}
							else{
								device=pt.getProcessTrackId();
								cclient=ptClient.getProcessTrackId();
								pc=pclient2.getProcessTrackId();
							}
							process.createProcessDetail(device, ProcessTrackDetailType.DEVICE_ACCOUNT_DISABLE,
									"Se realiza la desactivación del Dispositivo Electrónico TAG ID Facial "+td.getDeviceFacialId()+ " vinculado a la placa " + vehiclePlate +" de la Cuenta FacilPass No " + accountId,
									user, ip, 1, "No se pudo desactivar la relación del tag: "
									+ deviceCode + " con vehículo " + vehiclePlate + ", asociada al cliente ID: " + idClient + ". ID Proceso :"
									+ device+".",null,null,null,null);
							
							process.createProcessDetail(cclient, ProcessTrackDetailType.DEVICE_ACCOUNT_DISABLE,
									"Se realiza la desactivación del Dispositivo Electrónico TAG ID Facial "+td.getDeviceFacialId()+ " vinculado a la placa " + vehiclePlate +" de la Cuenta FacilPass No " + accountId,
									user, ip, 1, "No se pudo desactivar la relación del tag: "
									+ deviceCode + " con vehículo " + vehiclePlate + ", asociada al cliente ID: " + idClient + ". ID Proceso :"
									+ ptClient.getProcessTrackId()+".",null,null,null,null);
							
							process.createProcessDetail(pc, ProcessTrackDetailType.DEVICE_ACCOUNT_DISABLE,
									"Se realiza la desactivación del Dispositivo Electrónico TAG ID Facial "+td.getDeviceFacialId()+ " vinculado a la placa " + vehiclePlate +" de la Cuenta FacilPass No " + accountId,
									user, ip, 1, "No se pudo desactivar la relación del tag: "
									+ deviceCode + " con vehículo " + vehiclePlate + ", asociada al cliente ID: " + idClient + ". ID Proceso :"
									+ ptClient.getProcessTrackId()+".",null,null,null,null);
							
							//se consulta la vehicleId a partir de la placa
							Query v = em.createNativeQuery("Select VEHICLE_ID from TB_VEHICLE where VEHICLE_PLATE=?1");
							v.setParameter(1, vehiclePlate.toUpperCase());
							Long vehicleId = ((BigDecimal) v.getSingleResult()).longValue();
							outbox.insertMessageOutbox(idClient, 
					                   EmailProcess.DISABLE_ACCOUNT_DEVICE,
					                   accountId,
					                   null, 
					                   null, 
					                   deviceId.longValue(),
					                   vehicleId,
					                   null,
					                   null,
					                   null,
					                   user,
					                   null,
					                   null,
					                   null,
					                   true,
					                   null);
							
							res=true;
						}
				}
			}
			
			System.out.println("res: " +res);
		}catch(Exception ex){
			ex.printStackTrace();
			res=false;
		}
		
        
		return res;
	}

	@Override
	public List<TbDevice> getDevicesByState(Long stateId, String device) {
		List<TbDevice> lis= new ArrayList<TbDevice>();
		Query q;
		if(stateId!=-1L){
			 if(device.equals("") || device==null){
			     q= em.createQuery("select td from TbDevice td where td.tbDeviceState.deviceStateId=?1 order by td.deviceEntryDate");
				 q.setParameter(1, stateId);
			    }
			 else{
				 device=device.toUpperCase();
			     q= em.createQuery("select td from TbDevice td where td.tbDeviceState.deviceStateId=?1 and td.deviceCode=?2 order by td.deviceEntryDate");
				 q.setParameter(1, stateId);
				 q.setParameter(2, device);
			 }
		}
		else{
			 if(device.equals("") || device==null){
			   	q= em.createQuery("select td from TbDevice td  order by td.deviceEntryDate");
			 }
			 else{
				device=device.toUpperCase();
			   	q= em.createQuery("select td from TbDevice td where  td.deviceCode=?1 order by td.deviceEntryDate");
				q.setParameter(1, device);
			 }
		}
	
		for(Object o: q.getResultList()){
			TbDevice t= (TbDevice) o;
			
			lis.add(t);
		}
		
		return lis;
	}

	
	@Override
	public List<TbDevice> getInfoDevicesByFilters(Long codeType,Long accountId, String codeClient) {
		List<TbDevice> devices = new ArrayList<TbDevice>();
		try {
				String qry = "";
				qry = "SELECT DISTINCT rad.tbDevice FROM ReAccountDevice rad " +
					  "WHERE rad.relationState=0 " ;
				
				if(!codeClient.equals("")){
					qry = qry+"AND rad.tbAccount.tbSystemUser.userCode ='"+codeClient.trim()+"' ";
					qry = qry+"AND rad.tbAccount.tbSystemUser.tbCodeType ="+codeType+" ";
				}
				if((accountId!=null) && (accountId!=-1L)){
					qry = qry+"AND rad.tbAccount.accountId ="+accountId+" ";
				}
				Query q = em.createQuery(qry);
				for (Object obj : q.getResultList()) {
					devices.add((TbDevice) obj);
				}
			} catch (Exception e) {
				System.out.println(" [ Error en DeviceEJB.getInfoDevicesByFilters ] ");
				e.printStackTrace();
			}
		return devices;
	}
	@Override
	public List<TbDeviceHistory> getDevicesHistoryByAccount(Long idAccount) {
		List<TbDeviceHistory> list = new ArrayList<TbDeviceHistory>();
		try {
			List<TbDevice> devices=getDevicesByAccount(idAccount);
			for(int i=0;i<devices.size();i++){
				Query q=em.createQuery("SELECT devh FROM TbDeviceHistory devh " +
				"WHERE devh.deviceId=?1");
				q.setParameter(1, devices.get(i));
				list.add((TbDeviceHistory) q.getSingleResult());
			}
		} catch (Exception e) {
			System.out.println(" [ Error en DeviceEJB.getDevicesHistoryByAccount ] ");
			e.printStackTrace();
		}
		return list;
	}
	

	@Override
	@SuppressWarnings("unchecked")
	public Long stateManufacterByDevice(Long deviceId){
		long res=1;
		
		try{
			Query q= em.createNativeQuery("select tt.tag_state from tb_device td "+
					"inner join re_device_tag_type rdtt on td.device_id= rdtt.device_id "+
					"inner join tb_tag_type tt on rdtt.tag_type_id= tt.tag_type_id "+
					"where td.device_id=?1");
		
		    q.setParameter(1, deviceId);
		    List<BigDecimal> lis= q.getResultList();
		    if(lis!=null){
			    if(lis.size()>0){
			    	int con=0;
			    	for(BigDecimal f : lis){
			    		if(f!=null){	
			    			if(f.longValue()!=2){
			    				//activo
			    				res=0L;
			    				con=con+1;
			    			}
			    			else{		    				
			    				//inactivo
			    				res=1L;
			    			}
			    		}				
			    	}
			    	if(con>0){
			    		//activo
			    		res=0L;
			    		System.out.println("estado fabr diferente de 2 El tag posee fabricante activo res: " + res);
			    	}
			    	else{
			    		//inactivo
			    		res=1L;
			    		System.out.println("estado fabr igual de 2 El tag  posee fabricante inactivo res: " + res);
			    	}
			    	System.out.println("con: " + con);
			    }
			    else{
			    	//sin fabricante
			    	res=-1L;
			    	System.out.println("size 0 El tag no posee fabricante res: " + res);
			    }	
		    }
		    else{
		    	//sin fabricante
		    	res=-1L;
		    	System.out.println("lista null El tag no posee fabricante res: " + res);
		    }	
	
		}catch(NoResultException nre){
			//sin fabricante
			nre.printStackTrace();
		    res=-1L;	   
		    System.out.println("NoResultException El tag no posee fabricante res: " + res);
		    return res;
		}
	    catch(Exception e){
		    //sin fabricante
		    e.printStackTrace();
	        res=-2;	   
	        System.out.println("Error");
	        return res;
	   }
	    
		return res;
	}
	

	@Override
	public boolean haveActiveRelation(Long deviceId){
		boolean res=false;
		
		try{
			Query q= em.createNativeQuery("select td.device_id from tb_device td  " + 
                    "inner join re_account_device rad on td.device_id= rad.device_id " +
                    "where rad.device_id=?1 and rad.relation_state=0" );
			q.setParameter(1,deviceId);
			
			BigDecimal device= (BigDecimal) q.getSingleResult();
			if(device!=null){
				res=true;
			}
			else{
				res=false;
			}
			
		}catch(NoResultException ex){
			
			res=false;
		}
		
        
		return res;
	}

	//------------------------------------------------/
	@Override
	public TableModel loadFileTags(File file) {
		String name="";
		String ext="";
		try{
			TableModel result=null;
			name=file.getName();
			ext=name.substring(name.length()-3);
			System.out.println("Name:"+name);
			if(ext.toUpperCase().equals("LSX")){
				//EXCEL 2007 EN ADELANTE
				result=ParserReadFiles.readXLSX(file);
			}else if(ext.toUpperCase().equals("XLS")){
				//EXCEL ANTES DEL 2007
				result=ParserReadFiles.readXLS(file);
			}else{
				result=null;
			}
			return result;
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("[ Error DeviceEJB.loadFileTags ]");
			return null;
		}
	}
	
	/**
	 * Método creado para crear registros en la tabla tb_device.
	 * @author psanchez
	 */
	@Override
	public Long createMassiveTags(Long creationUser, String ip, TableModel t,
			Long tagTypeId, Long deviceLength, String fileName, Long companyTagId, Long userId) {
	
		String idInterno="";
		String idFacial="";
		Long courierId=0L;
		Long rollId=0L;
		Long result=-1L;
		boolean saveReg=false;
		Integer savedReg=0;
		
		for (int x = 0; x < t.getRowCount(); x++) {
			for (int y = 0; y < t.getColumnCount(); y++) {				
				if(y==0){
					saveReg=true;
					idInterno=(String)t.getValueAt(x, y);
				}
				if(y==1){
					idFacial=(String)t.getValueAt(x, y);
				}
				if(y==2){
					try{
						courierId=Long.parseLong((String)t.getValueAt(x, y));
					}catch (Exception e) {
						courierId=0L;
					}
				}
				if(y==3){
					try{
						rollId=Long.parseLong((String)t.getValueAt(x, y));
					}catch (Exception e) {
						rollId=0L;
					}
				}
				if(validateMassiveLoad(x,y,(String)t.getValueAt(x, y), 
						deviceLength, fileName, companyTagId, userId)){
					if(saveReg){
						saveReg=true;
					}else{
						saveReg=false;
					}
				}else{
					saveReg=false;
				}
			}
			if(saveReg){
				try{
					Long typeDevId = 0L;
					Long deviceState = 15L;
					long courierIdL = this.getCourierId(String.valueOf(courierId));
					long rolloIdL = this.getRollId(String.valueOf(rollId));
					
					if(this.saveDeviceCompanyTag(idInterno, idFacial, 0L, typeDevId, deviceState, companyTagId, courierIdL, rolloIdL, ip, creationUser)){
						TbDevice td =  this.getDeviceByCode(idInterno);
						this.createRealationtagType(td.getDeviceId(), tagTypeId, ip, creationUser);
						savedReg=savedReg+1;
					}
				}catch(Exception e){
					e.printStackTrace();
					System.out.println("[ Error DeviceEJB.createMassiveTags ]");
				}
			}
		}
		if(savedReg==0){
			System.out.println("[ No tiene encabezado el archivo excel. ]");
			result=0L;
		}else{
			result=(long)savedReg;	
		}
		return result;
	}
	
	/**
	 * Método creado para crear registros en la tabla tb_temp.
	 * @author psanchez
	 */
	@Override
	public Long validateMassiveTags(Long creationUser, String ip, TableModel t,
			Long tagTypeId, Long deviceLength, String fileName, Long companyTagId, Long userId) {

		String idInterno="";
		String idFacial="";
		Long courierId=0L;
		Long rollId=0L;
		Long result=0L;
		
		for (int x = 0; x < t.getRowCount(); x++) {
			for (int y = 0; y < t.getColumnCount(); y++) {				
				if(y==0){
					idInterno=(String)t.getValueAt(x, y);
				}
				if(y==1){
					idFacial=(String)t.getValueAt(x, y);
				}
				if(y==2){
					try{
						courierId=Long.parseLong((String)t.getValueAt(x, y));
					}catch (Exception e) {
						courierId=0L;
					}
				}
				if(y==3){
					try{
						rollId=Long.parseLong((String)t.getValueAt(x, y));
					}catch (Exception e) {
						rollId=0L;
					}
				}
			}
			if(this.saveTemp(idInterno, idFacial, courierId, rollId)){
				result=-1L;
			}
		}
		result = this.validateMassiveTemp(creationUser, ip, t, tagTypeId, deviceLength, fileName, companyTagId, userId);
		this.deleteTempDevice();
	return result;
	}
	
	/**
	 * Método creado para validar datos de la tabla tb_temp.
	 * @author psanchez
	 */
	@SuppressWarnings("unused")
	public Long validateMassiveTemp(Long creationUser, String ip, TableModel t,
			Long tagTypeId, Long deviceLength, String fileName, Long companyTagId, Long userId) {
		
		String idInterno="";
		String idFacial="";
		
		Long courierId=0L;
		Long rollId=0L;
		Long result=-1L;
		boolean saveReg=false;
		Integer savedReg=0;
		
		for (int x = 0; x < t.getRowCount(); x++) {
			for (int y = 0; y < t.getColumnCount(); y++) {				
				if(y==0){
					saveReg=true;
					idInterno=(String)t.getValueAt(x, y);
				}
				if(y==1){
					idFacial=(String)t.getValueAt(x, y);
				}
				if(y==2){
					try{
						courierId=Long.parseLong((String)t.getValueAt(x, y));
					}catch (Exception e) {
						courierId=0L;
					}
				}
				if(y==3){
					try{
						rollId=Long.parseLong((String)t.getValueAt(x, y));
					}catch (Exception e) {
						rollId=0L;
					}
				}
				if(validateMassiveLoad(x,y,(String)t.getValueAt(x, y),
						deviceLength, fileName, companyTagId, userId)){
					if(saveReg){
						saveReg=true;
						savedReg=savedReg+1;
					}else{
						saveReg=false;
					}
				}else{
					saveReg=false;
				}
			}
		}
		if(saveReg==true){
			System.out.println("[ validateMassiveTemp-----saveReg----> ]"+saveReg);
			//savedReg=savedReg+1;
			if(savedReg==0){
				System.out.println("[ No tiene encabezado el archivo excel. ]");
				result=0L;
			}else{
				result=(long)savedReg;	
				if(result>1){
				}
				else{
				}
			}
		}
	return result;
	}

	/**
	 * Método creado para validar datos del archivo carga masiva tags.
	 * @author psanchez
	 */
	@SuppressWarnings("unused")
	public boolean validateMassiveLoad(int row,int column, String value,
			Long deviceLength, String fileName, Long companyTagId, Long userId) {
		boolean respu=false;
		try{
			if(column==0){//ID INTERNO
				//String valuel=null;
				//valuel=value.replace(".0", "");
				if(value==null||value.equals("")){
					log.insertLogTag(fileName, 2L, String.valueOf(row+1),
							"ID INTERNO", value, "El Id Interno es un campo requerido en el archivo.",
							"Ingrese un ID Interno válido.", userId);
					respu=false;
				}else{
					System.out.println("---------------ID INTERNO  value: "+value);
					System.out.println("---------------deviceLength: "+deviceLength);
					if(deviceLength%2!=0){
						log.insertLogTag(fileName, 2L, String.valueOf(row+1),
								"ID INTERNO", value, "El ID Interno solo acepta tamaño par de 6 a 50 caracteres. " +
								"Corrija el tamaño ID Tag del fabricante seleccionado en el módulo Fabricantes.",
								"Corrija el tamaño del ID Tag en el módulo Fabricantes.", userId);
						respu=false;
					}else{
						if(value.length() > deviceLength.intValue() || value.length() < 7){
							System.out.println("id interno longitud: "+value.length());
							log.insertLogTag(fileName, 2L, String.valueOf(row+1),
								"ID INTERNO", value, "El ID Interno solo acepta el tamaño definido por el fabricante. " +
								"Mínimo 6 y máximo " +deviceLength.intValue()+ " caracteres.",
								"Corrija el tamaño del ID Interno.", userId);
							respu=false;
						}else{
							if(validateHexa(value)==false){
								System.out.println("id interno-->this.validateHexa(value): "+this.validateHexa(value));
								log.insertLogTag(fileName, 2L, String.valueOf(row+1),
									"ID INTERNO", value, "El ID Interno solo acepta valores hexadecimales en mayúscula. ",
									"Corrija el valor del ID Interno.", userId);
								respu=false;
							}else{
								if(value.length()%2!=0){
									System.out.println("id interno par: "+value.length());
									log.insertLogTag(fileName, 2L, String.valueOf(row+1),
											"ID INTERNO", value, "El tamaño del ID Interno debe ser par. ",
											"Corrija el tamaño del ID Interno.", userId);
									respu=false;
								}else{
									TbDevice td = this.getDeviceByCode(value);
									if(td != null){
										System.out.println("id interno-->this.getDeviceByCode(valuel): "+td.getDeviceCode());
										log.insertLogTag(fileName, 2L, String.valueOf(row+1),
												"ID INTERNO", value, "Existe un dispositivo con el mismo Id Interno. ",
												"Corrija el valor del ID Interno.", userId);
										respu=false;
									}else {
										boolean DeviceCodeTemp = this.getDeviceByCodeTemp(value);
										if(DeviceCodeTemp==true){
											System.out.println("id interno-->this.getDeviceByCodeTemp(valuel): "+ this.getDeviceByCodeTemp(value));
											log.insertLogTag(fileName, 2L, String.valueOf(row+1),
													"ID INTERNO", value, "El Id Interno que desea cargar se encuentra repetido en el archivo. ",
													"Corrija el valor del ID Interno.", userId);
											respu=false;
										}else{
											respu=true;
										}
									}
								}
							}
						}
					}
				}
			}else if(column==1){//ID FACIAL
				//String valuel=null;
				//valuel=value.replace(".0", "");
				if(value==null||value.equals("")){
					log.insertLogTag(fileName, 2L, String.valueOf(row+1),
							"ID FACIAL", value, "El Id Facial es un campo requerido en el archivo.",
							"Ingrese un ID Facial válido.", userId);
					respu=false;
				}else{
					System.out.println("---------------ID FACIAL  value: "+value);
					boolean vali=true;
					if(value.length()>20){
						System.out.println("id facial longitud: "+value.length());
						log.insertLogTag(fileName, 2L, String.valueOf(row+1),
								"ID FACIAL", value, "El campo Id Facial " +
								"no puede tener más de 20 caracteres.",
								"Corrija el tamaño del Id Facial. ", userId);
						vali=false;
					}
						else{
							if(!value.matches("[A-Z0-9 ]*")){
								System.out.println("id interno alfanumerico: "+value);
								log.insertLogTag(fileName, 2L, String.valueOf(row+1),
									"ID FACIAL", value, "El ID Facial solo acepta valores alfanuméricos en mayúscula. ",
									"Corrija el valor del ID Facial.", userId);
								vali=false;
						}
						else {
							TbDevice td = this.getDeviceByFacia(value);
							if(td != null){
								System.out.println("id interno-->this.getDeviceByCode(valuel): "+td.getDeviceCode());
								log.insertLogTag(fileName, 2L, String.valueOf(row+1),
										"ID FACIAL", value, "Existe un dispositivo con el mismo Id Facial. ",
										"Corrija el valor del ID Interno.", userId);
								respu=false;
							}
						else {
							boolean DeviceFacialTemp = this.getDeviceByFacialTemp(value);
							if(DeviceFacialTemp==true){
								System.out.println("id facial-->this.getDeviceByFacialTemp(valuel): "+ this.getDeviceByFacialTemp(value));
								log.insertLogTag(fileName, 2L, String.valueOf(row+1),
										"ID FACIAL", value, "El Id Facial que desea cargar se encuentra repetido en el archivo. ",
										"Corrija el valor del ID Facial.", userId);
								respu=false;
							}else{
								respu=true;
							}
						}
					}
				 }
			  }
			}else if(column==2){//COURIER
				courier=value;
				if(value==null||value.equals("")){
					log.insertLogTag(fileName, 2L, String.valueOf(row+1),
							"COURIER", value, "El campo Courier es requerido en el archivo.",
							"Ingrese un Courier válido.", userId);
					respu=false;
				}else{
					System.out.println("---------------COURIER  value: "+value +" ;longitud: "+value.length());
					boolean vali=true;
					if(value.length()>15){
						log.insertLogTag(fileName, 2L, String.valueOf(row+1),
								"COURIER", value, "El campo Courier debe ser menor de 16 caracteres.",
								"Corrija el tamaño del Courier.", userId);
						vali=false;
					}
					else {
						long result=this.getCourierId(String.valueOf(value));
						System.out.println("courier-->this.getCourierId(value): "+ result);
						if(result==-1L){
							System.out.println("courier-->El Courier no existe: "+ result);
							log.insertLogTag(fileName, 2L, String.valueOf(row+1),
									"COURIER", value, "El Courier no existe.",
									"Cree el Courier que desea subir en el archivo.", userId);
							vali=false;
						}else if(result==0L){
							System.out.println("courier-->El campo Courier debe ser numérico: "+ result);
							log.insertLogTag(fileName, 2L, String.valueOf(row+1),
									"COURIER", value, "El campo Courier debe ser numérico.",
									"Corrija el valor del Courier.", userId);
							vali=false;
						}else {
							  try{
									Query q=em.createNativeQuery("select count(*) " +
											"from TB_COURIER where courier_id=?1 " +
											"and company_tag_id=?2 ");
									q.setParameter(1, value);
									q.setParameter(2, companyTagId);
									Long relCompanyCourier = ((BigDecimal)q.getSingleResult()).longValue();
									System.out.println("Empresa-->dependencia Empresa Courier: "+relCompanyCourier);
									if(relCompanyCourier==0L){
										log.insertLogTag(fileName, 2L, String.valueOf(row+1),
												"COURIER", value, "El campo Courier no pertenece a la Empresa.",
												"El campo Courier no pertenece a la Empresa.", userId);
										vali=false;
									}
						    	}catch(NumberFormatException ex){
						        	System.out.println("El courier no es un númerico");
						    	}
						}
					}
					if(vali){
						respu=true;
					}else{
						respu=false;
					}
				}
			}else if(column==3){//ROLLO
				if(value==null||value.equals("")){
					log.insertLogTag(fileName, 2L, String.valueOf(row+1),
							"ROLLO", value, "El campo Rollo es requerido en el archivo.",
							"Ingrese un Rollo válido.", userId);
					respu=false;
				}else{
					System.out.println("---------------ROLLO  value: "+value+" ;longitud: "+value.length());
					boolean vali=true;
					if(value.length()>15){
						log.insertLogTag(fileName, 2L, String.valueOf(row+1),
								"ROLLO", value, "El campo Rollo debe ser menor de 16 caracteres.",
								"Corrija el tamaño del Rollo.", userId);
						vali=false;
					}else {
						long result=this.getRollId(String.valueOf(value));
						System.out.println("rollo-->this.getRollId(value): "+ result);
						if(result==-1L){
							System.out.println("rollo-->El rollo no existe: "+ result);
							log.insertLogTag(fileName, 2L, String.valueOf(row+1),
									"ROLLO", value, "El Rollo no existe.",
									"Cree el rollo que desea subir en el archivo.", userId);
							vali=false;
						}else if(result==0L){
							System.out.println("rollo-->El campo rollo debe ser numérico: "+ result);
							log.insertLogTag(fileName, 2L, String.valueOf(row+1),
									"ROLLO", value, "El campo Rollo debe ser numérico.",
									"Corrija el valor del Rollo.", userId);
							vali=false;
						}else {
							  try{
								Query q=em.createNativeQuery("select count(*) " +
										"from TB_ROLL where roll_id=?1 " +
										"and courier_id=?2 ");
								q.setParameter(1, value);
								q.setParameter(2, Long.parseLong(this.getCourier()));
								Long relCourierRollo = ((BigDecimal)q.getSingleResult()).longValue();
								System.out.println("rollo-->dependencia Courier Rollo: "+relCourierRollo);
								if(relCourierRollo==0L){
									log.insertLogTag(fileName, 2L, String.valueOf(row+1),
											"ROLLO", value, "El campo Rollo no pertenece al Courier.",
											"El campo Rollo no pertenece al Courier.", userId);
									vali=false;
								}
					    	}catch(NumberFormatException ex){
					        	System.out.println("El campo Rollo no es un númerico");
					    	}
						}
					}
					if(vali){
						respu=true;
					}else{
						respu=false;
					}
				}
			}else{
				respu=false;
			}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("[ Error DeviceEJB.validateMassiveLoad ]");
			respu=false;
		}			
		return respu;
	}
	
	/**
	 * Método creado para ingresar datos en la tabla tb_temp.
	 * @author psanchez
	 */
	private boolean saveTemp(String idInterno, String idFacial, Long courierId, Long rollId) {
		try {
			String value2=null;
			value2=idInterno.replace(".0", "");
			TbTemp temp = new TbTemp();
			temp.setDeviceCode(value2);
			if(idFacial != null) {
				String valuel=null;
				valuel=idFacial.replace(".0", "");
				temp.setDeviceFacialId(valuel.toUpperCase());
			}
			temp.setCourierId(courierId);
			temp.setRollId(rollId);
			
			objectEM = new ObjectEM(em);	
			if(objectEM.create(temp)){
				return true;
			} 
		} catch (Exception e) {
			System.out.println(" [ Error en DeviceEJB.saveTemp ] ");
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	
	public boolean validateHexa(String device){
		boolean result = false;
		int tamanio = device.length();
		char dato;
		for (int i=0; i<tamanio; i++){
			dato = device.charAt(i);
			if ((((int)dato) >=48) && (((int)dato) <=57)){
				result =  true;
			} else if ((((int)dato) >=65) && (((int)dato) <=70)){
				result =  true;
			} else {
				result =  false;
				break;
			}
		}
			
		return result;
	}
	
	/**
	 * Método creado para obtener el courier_id de la tabla tb_courier.
	 * @author psanchez
	 */
	private long getCourierId(String courierId) {
		try{
			 long courierIdL = Long.parseLong(courierId); 
			 Query query = em.createNativeQuery("select courier_id from tb_courier where courier_id= ?1 ");
			 query.setParameter(1, courierIdL);
			 query.getSingleResult();		
			 BigDecimal courierIdB = (BigDecimal) query.getSingleResult();
	         Long courierIdLong = courierIdB.longValue();  
				 if(courierIdLong>0L){
					 return courierIdLong;
				 } else {
					 return -1L;
				 }
			} catch(NumberFormatException nfe){
				System.out.println(nfe.getMessage());
				 return 0L;
			} catch (NoResultException e){	
				System.out.println("[ No se encontró courier Id con código: "  + courierId +  ". ] ");
			} catch(Exception ex){
				System.out.println(" [ Error en DeviceEJB.getCourierId] ");
				ex.printStackTrace();
			}
		return -1L;
	}
	
	/**
	 * Método creado para obtener el rollo_id de la tabla tb_roll.
	 * @author psanchez
	 */
	private long getRollId(String rolloId) {
		try{
			long rollIdLong = Long.parseLong(rolloId); 
			 Query query = em.createNativeQuery("select roll_id from tb_roll where roll_id= ?1 ");
			 query.setParameter(1, rollIdLong);
			 BigDecimal rolloIdB = (BigDecimal) query.getSingleResult();
	         Long rolloIdL = rolloIdB.longValue();  
				 if(rolloIdL>0L){
					 return rolloIdL;
				 } else {
					 return -1L;
				 }
			} catch(NumberFormatException nfe){
				System.out.println(nfe.getMessage());
				 return 0L;
			} catch (NoResultException e){	
				System.out.println("[ No se encontró rollo Id con código: "  + rolloId +  ". ] ");
			} catch(Exception ex){
				System.out.println(" [ Error en DeviceEJB.getRollId ] ");
				ex.printStackTrace();
			}
		return -1L;
	}
	

	/**
	 * Método creado para obtener el device_code de la tabla tb-temp.
	 * @author psanchez
	 */
	public boolean getDeviceByCodeTemp(String code) {
		try {
			   Query q = em.createNativeQuery("select count(*) from tb_temp where device_code=?1 ");
			   q.setParameter(1, code);
			   int counter = ((BigDecimal)q.getSingleResult()).intValue();
			   if(counter > 1){
				   return true;
				}
			}catch(NoResultException n){
				System.out.println("Error en DeviceEJB.getDeviceByCodeTemp");
				return false;
			}
		return false;
	}
	
	/**
	 * Método creado para obtener el device_facial_id de la tabla tb_temp.
	 * @author psanchez
	 */
	public boolean getDeviceByFacialTemp(String facial) {
		try {
			   Query q = em.createNativeQuery("select count(*) from tb_temp where device_facial_id=?1 ");
			   q.setParameter(1, facial);
			   int counter = ((BigDecimal)q.getSingleResult()).intValue();
			   if(counter > 1){
				   return true;
				}
			}catch(NoResultException n){
				System.out.println("Error en DeviceEJB.getDeviceByFacialTemp");
				return false;
			}
		return false;
	}
	
	/**
	 * Método creado para eliminar los datos de la tabla tb_temp.
	 * @author psanchez
	 */
	@Override
	public boolean deleteTempDevice() {
		Query q= em.createQuery("delete from TbTemp ");
		int i=q.executeUpdate();
		if(i>0){
			return true;
		}
		else{
			return false;
		}
	}

	public void setCourier(String courier) {
		this.courier = courier;
	}

	public String getCourier() {
		return courier;
	}
	
	@Override
	public boolean plateIsEnrollOtherClient(Long vehicleId, Long accountId){
		boolean res=false;
		Long client=null;
		TbAccount account= em.find(TbAccount.class, accountId);
		if(account!=null){
			client= account.getTbSystemUser().getUserId();
		}
		Query q= em.createNativeQuery("select count(*) from re_user_vehicle where user_id not in (?1) and vehicle_id=?2  and state_relation=0 ");
		q.setParameter(1, client);
		q.setParameter(2, vehicleId);
		BigDecimal cont= (BigDecimal) q.getSingleResult();
		System.out.println("cont: " +cont);
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
		System.out.println("cont en plateIsEnrollOtherClient: " + cont);
		System.out.println("res en plateIsEnrollOtherClient: " + res);
		
		return res;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Device#saveAssociation(java.lang.Long, java.lang.Long,
	 * java.lang.String, java.lang.Long)
	 */
	@Override
	public boolean saveAssociationCarril(Long idClientAccount, Long idDevice, Long idVehicle,String ip,
			Long creationUser) {
		int flag=0;
		Long contractualAuth=null;
		String valor=null;
		try {
			System.out.println("idDevice: "+ idDevice);
			// Searching the client account.
			TbAccount ta = em.find(TbAccount.class, idClientAccount);
			
			//Query q=em.createNativeQuery("select system_parameter_value from tb_system_parameter where system_parameter_id=34");
			Query q =em.createQuery("select systemParameterValue from TbSystemParameter where systemParameterId in (34L)");
			valor= (String) q.getSingleResult();
			if(valor!=null){
				contractualAuth= new Long(valor);
			}
			System.out.println("contractualAuth en saveAssociation" + contractualAuth);
			
			if (ta != null) {
			    Long state=ta.getAccountState();
				if(state==0L || state==7L){
					ReAccountDevice rad = new ReAccountDevice();
					rad.setTbAccount(ta);
					TbVehicle tv = null;
					TbDevice dev = em.find(TbDevice.class, idDevice);
					
					TbDeviceState tde= em.find(TbDeviceState.class,6L);
				    dev.setTbDeviceState(tde);
					
					if(dev.getTbDeviceType().getDeviceTypeId() == 0L){
						tv = em.find(TbVehicle.class, idVehicle);
						rad.setTbVehicle(tv);
					} else{
						rad.setTbVehicle(null);
					}
					rad.setTbDevice(dev);
					
					rad.setRelationState(RelationDeviceAccountState.ACTIVE.getId());
					rad.setDateTransaction(new Timestamp(System.currentTimeMillis()));								
					
					objectEM = new ObjectEM(em);
					if(objectEM.update(dev)){
						if (objectEM.create(rad)) {
							log.insertLog(
									"Enrolar dispositivo a Cuenta | Se ha asociado la cuenta ID: "
									+ ta.getAccountId() + ", con el dispositivo ID: " + idDevice +".",
									LogReference.ACCOUNT, LogAction.CREATE, ip,
									creationUser);
							
							// Saving in the device process.
							TbProcessTrack pt = process.searchProcess(ProcessTrackType.DEVICE, idDevice);
							
							// searching client process
							TbProcessTrack pc= process.searchProcess(ProcessTrackType.CLIENT, ta.getTbSystemUser().getUserId());
							TbProcessTrack pClient= process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, ta.getTbSystemUser().getUserId());
							
							if (pt != null && pc!=null && pClient!=null) {
								process.createProcessDetail(pt.getProcessTrackId(),ProcessTrackDetailType.DEVICE_ACCOUNT_ASSOCIATION,
										"Se ha vinculado el Dispositivo Electrónico TAG con ID Facial: " + (dev!=null?dev.getDeviceFacialId():"")
										+" a la Cuenta FacilPass No. "+ ta.getAccountId() + ".", creationUser, ip, 1, 
										"No se pudo registar en el proceso la asociación del dispositivo ID: "
										+ idDevice + ", con la cuenta del cliente ID Account: " + idClientAccount + ". ID Proceso :"
										+ pt.getProcessTrackId()+".",null,null,null,null);
								process.createProcessDetail(pc.getProcessTrackId(),ProcessTrackDetailType.DEVICE_ACCOUNT_ASSOCIATION,
										"Se ha vinculado el Dispositivo Electrónico TAG con ID Facial: " + (dev!=null?dev.getDeviceFacialId():"")
										+" a la Cuenta FacilPass No. "+ ta.getAccountId() + ".", creationUser, ip, 1, "No se pudo registar en el proceso la asociación del dispositivo ID: "
										+ idDevice + ", con la cuenta del cliente ID Account: " + idClientAccount + ". ID Proceso :"
										+ pc.getProcessTrackId()+".",null,null,null,null);
								process.createProcessDetail(pClient.getProcessTrackId(),ProcessTrackDetailType.DEVICE_ACCOUNT_ASSOCIATION,
										"Se ha vinculado el Dispositivo Electrónico TAG con ID Facial: " + (dev!=null?dev.getDeviceFacialId():"")
										+" a la Cuenta FacilPass No. "+ ta.getAccountId() + ".", creationUser, ip, 1, "No se pudo registar en el proceso la asociación del dispositivo ID: "
										+ idDevice + ", con la cuenta del cliente ID Account: " + idClientAccount + ". ID Proceso :"
										+ pClient.getProcessTrackId()+".",null,null,null,null);
							} else {
								// Log
								log.insertLog("No se encontró un proceso para el Dispositivo ID: "
										+ idDevice, LogReference.PROCESS,
										LogAction.NOTFOUND, ip, creationUser);
							}
							if(dev.getTbDeviceType().getDeviceTypeId() == 0L){
							transitTask.createTask(TypeTask.ACCOUNT, ta.getAccountId().toString());
							
							/*
							 * Se cambia estado al crear registrro en tag de activo a preactivo para que asi baje a los carriles hasta
							 * que el usuario active el tag	
							 */

							BigDecimal manufaturerId=null;
							try{
								Query qr = em.createNativeQuery("select to_number(tt.tag_type_code) from re_device_tag_type rdt "+
										"inner join tb_device d on rdt.device_id=d.device_id "+
										"inner join tb_tag_type tt on rdt.tag_type_id=tt.tag_type_id "+
										"where d.device_id=?1 and tt.tag_state=1");
								qr.setParameter(1, dev.getDeviceId().longValue());	
								manufaturerId = (BigDecimal) qr.getSingleResult();
							}catch(Exception ex){
								ex.printStackTrace();
								flag=0;
							}

							
							
							TbTag tag=null;
							try{
								String t=dev.getDeviceCode();
								System.out.println("tag-S " + dev.getDeviceCode());
								System.out.println("tag-L " + t);
								tag= em.find(TbTag.class,t);
								System.out.println("tag" + tag);
								if(tag!=null){
									tag.setDeviceCode(dev.getDeviceFacialId());
									tag.setDeviceCurrentBalance(dev.getDeviceCurrentBalance());
									tag.setCategoryId(tv.getTbCategory().getCategoryId());
									tag.setDeviceStateId(1L);
									tag.setDeviceTypeId(VialTypeTag.PREPAGO.getTipo());
									tag.setTagTypeCode(manufaturerId!=null?manufaturerId.longValue():null);
									tag.setVehiclePlate(tv.getVehiclePlate());
									tag.setContractualAuth(contractualAuth);
									tag.setAccountId(ta.getAccountId());
									
									objectEM = new ObjectEM(em);
									if(objectEM.update(tag)){
										System.out.println(tag.toString());
										transitTask.createTask(TypeTask.DEVICE, tag.getDeviceId());
									}
									System.out.println("Se modificó tag");
								}
								else{
									System.out.println("tag igual a null");
									tag = new TbTag();
									
									tag.setDeviceId(dev.getDeviceCode());
									tag.setDeviceCode(dev.getDeviceFacialId());
									tag.setDeviceCurrentBalance(dev.getDeviceCurrentBalance());
									tag.setCategoryId(tv.getTbCategory().getCategoryId());
									tag.setDeviceStateId(1L);
									tag.setDeviceTypeId(VialTypeTag.PREPAGO.getTipo());
									tag.setTagTypeCode(manufaturerId!=null?manufaturerId.longValue():null);
									tag.setVehiclePlate(tv.getVehiclePlate());	
									tag.setContractualAuth(contractualAuth);
									tag.setAccountId(ta.getAccountId());
									
									objectEM = new ObjectEM(em);
									if(objectEM.create(tag)){
										System.out.println(tag.toString());
										transitTask.createTask(TypeTask.DEVICE, tag.getDeviceId());
									}
									System.out.println("Se creó tag");
								}						
							
							}catch(Exception ex){
								ex.printStackTrace();
								return false;
							}
							}
							
							//se crea registro en la tabla re_user_vehicle
							try{
								Query qq= em.createNativeQuery("select count(*) from re_user_vehicle where vehicle_id=?1 and state_relation=0");
								qq.setParameter(1, idVehicle);
								BigDecimal cont= (BigDecimal) qq.getSingleResult();
								System.out.println("cont: " +cont);
								    
								if(cont!=null){
								    if(cont.longValue()==0){
								    	Query qq1= em.createNativeQuery("insert into RE_USER_VEHICLE (USER_VEHICLE_ID,VEHICLE_ID,USER_ID,DATE_ASSOCIATION,STATE_RELATION) " +
								    			" values(RE_USER_VEHICLE_SEQ.NEXTVAL,?1,?2,systimestamp,0)");
										qq1.setParameter(1, idVehicle);
										qq1.setParameter(2, ta.getTbSystemUser().getUserId());
										qq1.executeUpdate();
										em.flush();
								    }
								}
	
							}catch(Exception ex){
								ex.printStackTrace();
							}
							
							TbLogAdminDevice tlad= new TbLogAdminDevice();
							tlad.setUserId(ta.getTbSystemUser().getUserId());
							tlad.setDeviceFacialIdOld("");
							tlad.setDeviceFacialIdNew(dev.getDeviceFacialId());
							tlad.setPlate(tv!=null?tv.getVehiclePlate():"");
							tlad.setCourierId(dev.getCourierId());
							tlad.setRollId(dev.getRollId());
							tlad.setCobro(null);
							tlad.setObservation("");
							tlad.setDateTransaction(new Timestamp(System.currentTimeMillis()));
							tlad.setActionName("Enrolar");
							tlad.setAccountId(ta.getAccountId());
							em.persist(tlad);
							
							flag=1;
							
						} else {
							flag=0;
							log.insertLog(
									"Asociar dispositivo a Cuenta | No se pudo asociar la cuenta ID: "
									+ ta.getAccountId() + ", con el dispositivo ID: " + idDevice +".",
									LogReference.ACCOUNT, LogAction.CREATEFAIL, ip,
									creationUser);
						}
					}
					 else {
						 flag=0;
						 log.insertLog(
									"No se pudo activar el dispositivo  con ID: " + idDevice +".",
									LogReference.ACCOUNT, LogAction.CREATEFAIL, ip,
									creationUser);
						}
				}
				else{
					flag=0;
					log.insertLog(
							"Asociar dispositivo a Cuenta | No se pudo asociar la cuenta ID: "
							+ ta.getAccountId() + ", con el dispositivo ID: " + idDevice + "Debido a que la cuenta facilpass no se encuentra asociada a un producto bancario.",
							LogReference.ACCOUNT, LogAction.CREATEFAIL, ip,
							creationUser);
					return false;
				}
				
				if(flag==0){
					return false;
				}
				if(flag==1){
					return true;
				}
			}
		} catch (NoResultException e) {
			e.printStackTrace();
			System.out.println("DeviceEJB.saveAssociation : No se encontró cuenta al cliente ID cuenta: "
							+ idClientAccount);
		} catch (Exception e) {
			System.out.println(" [ Error en DeviceEJB.saveAssociationCarril ] ");
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * @author jromero
	 */
	@Override
	public ReAccountDevice getReAccountDeviceById(Long relationId) {
		try{
			ReAccountDevice rad=em.find(ReAccountDevice.class, relationId);
			return rad;
		}catch (Exception e) {
			System.out.println(" [ Error en DeviceEJB.getReAccountDeviceById ] ");
			e.printStackTrace();
			return null;
		}
	}
	 public Long getListReplicationAvalState (Long account){
		 System.out.println("Entre a DeviceEJB.getListReplicationAvalState cuenta " + account);
		 Long respu = 6L;
		 Long valueAcount;
		 
		 TbAccount ta = em.find(TbAccount.class, account.longValue());
		 
		 try {
			 
			 Query q = em.createNativeQuery("select nvl(max(val_min_alarm),0) from tb_alarm_balance where account_id = ?1 and id_tip_alarm = 4");
			 q.setParameter(1, account);
		     Long SaldBajo = ((BigDecimal) q.getSingleResult()).longValue();
		     
		     Query q1 = em.createNativeQuery("select nvl(max(val_minimo),0) from tb_alarm_balance where account_id = ?1 and id_tip_alarm = 4");
			 q1.setParameter(1, account);
		     Long SaldMinimo = ((BigDecimal) q1.getSingleResult()).longValue();
		     
		     
		     valueAcount = ta.getAccountBalance().longValue();
		     
		     System.out.println("Saldo Bajo TbAlarmBalance: " + SaldBajo);
		     System.out.println("Valor Min TbAlarmBalance: " + SaldMinimo);
		     System.out.println("Saldo de la Cuenta: " + valueAcount);
		     
		     if (SaldBajo != null){
		    	 
		    	 if (valueAcount == null){
		    		 valueAcount = 0L;
		    	 }
		    	 
		    	 if (valueAcount > SaldBajo.longValue()){
		    		 //Dispositivo Activo
		    		 respu = 6L;
		    	 }else if (valueAcount <= SaldBajo.longValue() && valueAcount > SaldMinimo.longValue()){
		    		 //Dispositivo Saldo Bajo
		    		 respu = 4L;
		    	 }else if (valueAcount <= SaldMinimo.longValue()){
		    		 //Dispositivo Inactivo
		    		 respu = 3L;
		    	 }
		    	 
		     }else{
		    	 respu = 6L;
		     }
		     
		} catch (Exception e) {
			System.out.println("Error en DeviceEJB.getListReplicationAvalState");
			respu = 6L;
			e.printStackTrace();
		}
		System.out.println("State Disp: " + respu);
		return respu;
		 
	 }
	@Override
	public String setStateChange(Long deviceid, Long state) {
		try {
			
			TbDevice td = em.find(TbDevice.class, deviceid);
			
			td.setTbDeviceState(em.find(TbDeviceState.class,state));
			em.merge(td);
			em.flush();
			
			TbTag tag = em.find(TbTag.class, td.getDeviceCode());
			if (state == 6L){
				state = 1L;
			}
			tag.setDeviceStateId(state);
			em.merge(td);
			em.flush();
			
		} catch (Exception e) {
			System.out.println(" [ Error en DeviceEJB.setStateChange ] ");
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public TbDevice getDeviceByCode(Long codeType, String code) {
		try {
			Query q = em.createQuery("SELECT td FROM TbDevice td  " +
					                 "WHERE td.tbDeviceType.deviceTypeId=?1 AND td.deviceCode=?2 ");
			q.setParameter(1, codeType);
			q.setParameter(2, code.toUpperCase());
			return (TbDevice) q.getSingleResult();
		} catch (NoResultException e){	
			System.out.println("[ No se encontró un dispositivo con código: "+ code +". ] ");
		} catch (NonUniqueResultException e){
			System.out.println("[ Hay más de un resultado para el código de dispositivo ingresado" + code + " ]");
		} catch (Exception e) {
			System.out.println(" [ Error en DeviceEJB.getDeviceByCode ] ");
			e.printStackTrace();
		}
		return null;
	}

	
}