package ejb;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import constant.EmailProcess;
import constant.LogAction;
import constant.LogReference;
import constant.ProcessTrackDetailType;
import constant.ProcessTrackType;
import constant.TransactionType;
import constant.TypeTask;
import constant.VialTypeTag;
import crud.ObjectEM;
import ejb.email.Outbox;
import ejb.taskeng.TransitTask;

import jpa.ReAccountDevice;
import jpa.TbAccount;
import jpa.TbCourier;
import jpa.TbDevice;
import jpa.TbDeviceState;
import jpa.TbLogAdminDevice;
import jpa.TbProcessTrack;
import jpa.TbRollo;
import jpa.TbTag;
import jpa.TbVehicle;

import util.AllActiveDispositive;
import util.DeviceState;
import util.DevicesAvailable;
import util.InfoClient;

@Stateless(mappedName="ejb/EnrollDevice")
public class EnrollDeviceEJB implements EnrollDevice{

	@PersistenceContext(unitName="bo")
	EntityManager em;
	
	@EJB(mappedName="ejb/Process")
	private Process process;
	
	@EJB(mappedName="ejb/Log")
	private Log log;
	
	@EJB(mappedName ="ejb/email/Outbox")
	private Outbox outbox;
	
	@EJB(mappedName ="ejb/Transaction")
	private Transaction transaction;
	
	@EJB(mappedName="ejb/TransitTask")
	private TransitTask transitTask;
	
	@EJB(mappedName="ejb/Device")
	private Device deviceEJB;
	
	@EJB(mappedName="ejb/MinimumBalance")
	private MinimumBalance MinimumBalanceEJB;
	
	@EJB(mappedName="ejb/Vehicle")
	private Vehicle vehicleEJB;
	
	private ObjectEM emObj;
	
	@Override
	public Long validateClient(Long typeId, String codClient,String nomClient, String apeClient, String user,
			Long accountClient, String plateClient, String aditional1,String aditional2, String aditional3) {

		Long cont=0L;
		try{
			String consulta= "select count(*) from ( "
				+ " select distinct(tsu.user_id) from tb_system_user tsu "
				+ " inner join tb_code_type tct on tsu.code_type_id= tct.code_type_id "
				+ " left join tb_account ta on tsu.user_id= ta.user_id "
				+ " left join re_user_vehicle rv on rv.user_id= tsu.user_id  "
				+ " left join tb_vehicle tv on rv.vehicle_id= tv.vehicle_id "
				+ " where 1=1 ";

			if(typeId!=null && !typeId.equals(-1L)){
				consulta= consulta+ " and tsu.code_type_id = "+typeId+" ";	
			}
			if(codClient!=null && !codClient.trim().equals("")){
			    consulta= consulta+ " and tsu.user_code= '"+codClient+"' ";	
			}
			if(nomClient!=null && !nomClient.trim().equals("")){
				consulta= consulta+ " and upper(tsu.user_names) like upper('%"+nomClient+"%') ";	
			}
			if(apeClient!=null && !apeClient.trim().equals("")){
				consulta= consulta+ " and upper(tsu.user_second_names) like upper('%"+apeClient+"%') ";	
			}
			if(user!=null && !user.trim().equals("")){
				user=user.replace("'", "");
				System.out.println("user: " + user);
			    consulta= consulta+ " and upper(tsu.user_email)= upper('"+user+"')";	
			}
			if(accountClient!=null){
					consulta= consulta+ " and ta.account_id=  "+accountClient+" ";	             
			}
					
			if(plateClient!=null && !plateClient.trim().equals("")){
				consulta= consulta+ " and upper(tv.vehicle_plate)= upper('"+plateClient+"') ";	
			}
			if(aditional1!=null && !aditional1.trim().equals("")){
				aditional1=aditional1.replace("'", "");
				System.out.println("aditional1: " + aditional1);
				consulta= consulta+ " and upper(tv.aditional1)= upper('"+aditional1+"') ";	
			}
			if(aditional2!=null && !aditional2.trim().equals("")){
				aditional2=aditional2.replace("'", "");
				System.out.println("aditional2: " + aditional2);
				consulta= consulta+ " and upper(tv.aditional2)= upper('"+aditional2+"') ";	
			}
			if(aditional3!=null && !aditional3.trim().equals("")){
				aditional3=aditional3.replace("'", "");
				System.out.println("aditional3: " + aditional3);
				consulta= consulta+ " and upper(tv.aditional3)= upper('"+aditional3+"') ";	
			}
					
			consulta= consulta + " ) ";
			
			System.out.println("consulta: "+consulta);		
					
			Query q= em.createNativeQuery(consulta);
			cont= ((BigDecimal) q.getSingleResult()).longValue();
					
			System.out.println("cont: " +cont);
			
		}catch(NoResultException ex){
			cont=0L;
		}catch(Exception e){
			cont=-1L;
			e.printStackTrace();
		}
		
		return cont;
	}
	
	@SuppressWarnings("unchecked")
	public InfoClient searchClient(Long typeId, String codClient,String nomClient, String apeClient, String user,
			Long accountClient, String plateClient, String aditional1,String aditional2, String aditional3) {

		InfoClient client= null;
		try{
			String consulta= "select tsu.user_id, tct.code_type_abbreviation, tsu.user_code, tsu.user_names, tsu.user_second_names "
				+ " from tb_system_user tsu "
				+ " inner join tb_code_type tct on tsu.code_type_id= tct.code_type_id "
				+ " left join tb_account ta on tsu.user_id= ta.user_id "
				+ " left join re_user_vehicle rv on rv.user_id= tsu.user_id  "
				+ " left join tb_vehicle tv on rv.vehicle_id= tv.vehicle_id "
				+ " where 1=1 ";

			if(typeId!=null && !typeId.equals(-1L)){
				consulta= consulta+ " and tsu.code_type_id = "+typeId+" ";	
			}
			if(codClient!=null && !codClient.trim().equals("")){
				consulta= consulta+ " and tsu.user_code=  '"+codClient+"' ";	
			}
			if(nomClient!=null && !nomClient.trim().equals("")){
				consulta= consulta+ " and upper(tsu.user_names) like upper('%"+nomClient+"%')";	
			}
			if(apeClient!=null && !apeClient.trim().equals("")){
				consulta= consulta+ " and upper(tsu.user_second_names) like upper('%"+apeClient+"%') ";	
			}
			if(user!=null && !user.trim().equals("")){
				user=user.replace("'", "");
				System.out.println("user: " + user);
				consulta= consulta+ " and upper(tsu.user_email)= upper('"+user+"') ";	
			}
			if(accountClient!=null){
				if(!accountClient.equals(0L)){
					consulta= consulta+ " and ta.account_id=  "+accountClient;	
				}
			}
			
			if(plateClient!=null && !plateClient.trim().equals("")){
				consulta= consulta+ " and upper(tv.vehicle_plate)= upper('"+plateClient+"') ";	
			}
			if(aditional1!=null && !aditional1.trim().equals("")){
				aditional1=aditional1.replace("'", "");
				System.out.println("aditional1: " + aditional1);
				consulta= consulta+ " and upper(tv.aditional1)= upper('"+aditional1+"') ";	
			}
			if(aditional2!=null && !aditional2.trim().equals("")){
				aditional2=aditional2.replace("'", "");
				System.out.println("aditional2: " + aditional2);
				consulta= consulta+ " and upper(tv.aditional2)= upper('"+aditional2+"') ";	
			}
			if(aditional3!=null && !aditional3.trim().equals("")){
				aditional3=aditional3.replace("'", "");
				System.out.println("aditional3: " + aditional3);
				consulta= consulta+ " and upper(tv.aditional3)= upper('"+aditional3+"') ";	
			}
			
			consulta= consulta + " group by tsu.user_id, tct.code_type_abbreviation, tsu.user_code, tsu.user_names, tsu.user_second_names ";
							
			
			System.out.println("consulta: "+consulta);		
			
			Query q= em.createNativeQuery(consulta);
			List<Object[]> lis= q.getResultList();
			System.out.println("lis: " +lis);
			
			if(lis!=null){
			
				Object[] ob= lis.get(0);
				client= new InfoClient();
				System.out.println("userId: "+ ob[0]!=null?ob[0].toString():0);
				System.out.println("typeId: "+ ob[1]!=null?ob[1].toString():"");
				System.out.println("codClient: "+ ob[2]!=null?ob[2].toString():"");
				System.out.println("nomClient: "+ ob[3]!=null?ob[3].toString():"");
				System.out.println("apeClient: "+ ob[4]!=null?ob[4].toString():"");
				
				client.setIdClient(ob[0]!=null?Long.parseLong(ob[0].toString()):0);
				client.setType(ob[1]!=null?ob[1].toString():"");
				client.setCod(ob[2]!=null?ob[2].toString():"");
				client.setNom(ob[3]!=null?ob[3].toString():"");
				client.setApe(ob[4]!=null?ob[4].toString():"");
			
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}

		return client;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<TbCourier> getCouries(){
		List<TbCourier> lista= new ArrayList<TbCourier>();
		
		Query q = em.createQuery("select cr from TbCourier cr order by courierName");
		lista=q.getResultList();

		return lista;
		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<TbRollo> getRollosByCourier(Long courierId){
		List<TbRollo> lista= new ArrayList<TbRollo>();
		
		Query q = em.createQuery("select rl from TbRollo rl where rl.courierId.courierId=?1  order by rl.rollName");
		q.setParameter(1, courierId);
		lista=q.getResultList();
		
		return lista;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getVehiclesByClient(Long clientId){
		
		Query q= em.createNativeQuery("select ruv.vehicle_id, tv.vehicle_plate from re_user_vehicle ruv "
									  +" inner join tb_vehicle tv on ruv.vehicle_id= tv.vehicle_id  where user_id=?1 "
									  +" and ruv.state_relation=0 and tv.vehicle_id not in (select rad.vehicle_id from re_account_device rad where rad.vehicle_id is not null "
									  +" and relation_state=0) order by tv.vehicle_plate");
		q.setParameter(1, clientId); 
		
		List<Object[]> lista= q.getResultList();
			
		return lista;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<TbVehicle> getVehiclesActiveByClient(Long clientId, Long accountId){
		List<TbVehicle> list= new ArrayList<TbVehicle>();
		Query q= em.createNativeQuery("select ruv.vehicle_id, tv.vehicle_plate from re_user_vehicle ruv "
									  +" inner join tb_vehicle tv on ruv.vehicle_id= tv.vehicle_id  where user_id=?1 "
									  +" and tv.vehicle_id  in (select rad.vehicle_id from re_account_device rad where rad.account_id=?2 and rad.relation_state=0 and rad.vehicle_id is not null) "
									  +" and state_relation=0 order by tv.vehicle_plate");
		q.setParameter(1, clientId);
		q.setParameter(2, accountId);
		
		List<Object[]> lista= q.getResultList();
		BigDecimal v1;
		String v2;
		for(Object[] ob: lista){
			v1=ob[0]!=null?BigDecimal.valueOf(Long.parseLong(ob[0].toString())):null;
			v2= ob[1]!=null?ob[1].toString():"";
			
			if(v1!=null){
				TbVehicle vc= em.find(TbVehicle.class, v1.longValue());
                list.add(vc);
			}
		}
			
		return list;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getDevicesByRollo(Long rolloId){
		
		Query q= em.createNativeQuery("select device_id, device_facial_id from tb_device where device_state_id = 15 and device_roll_id=?1 " +
				" and device_id not in (select device_id from re_account_device) order by device_entry_date");
		q.setParameter(1, rolloId);

		List<Object[]> lista= q.getResultList();

		return lista;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<DeviceState> getDevices(Long rolloId){
		
		List<DeviceState> list= new ArrayList<DeviceState>();
		Query q= em.createNativeQuery("select device_id, device_facial_id from tb_device where device_state_id = 15 and device_roll_id=?1 " +
				" and device_id not in (select device_id from re_account_device) order by device_entry_date");
		q.setParameter(1, rolloId);

		List<Object[]> lista= q.getResultList();
		String facial="-";
		for(Object[] ob: lista){
			DeviceState ds= new DeviceState();
			if(ob[1]!=null){
				if(!ob[1].equals("")){
					facial= ob[1].toString();
				}
				else{
					facial="-";
				}
			}
			else{
				facial="-";
			}
			
			System.out.println("tag disponible: " + facial);
			ds.setDeviceId(ob[0]!=null?Long.parseLong(ob[0].toString()):0L);
			ds.setDeviceFacialId(facial);
			ds.setState("0");
			
			list.add(ds);
		}

		return list;
	}
	
	@Override
	public List<DevicesAvailable> getVehiclesAvaliableByClient(Long clientId){
		System.out.println("Entre a obtener dispositivos disponibles");
		List<DevicesAvailable> lista= new ArrayList<DevicesAvailable>();
		
		List<Object[]> lisv= this.getVehiclesByClient(clientId);
		
		for(int i=0;i<lisv.size();i++){
			DevicesAvailable da= new DevicesAvailable();
			
		    Object[] obv=lisv.get(i);

			
			da.setVehicleId(obv[0]!=null?Long.parseLong(obv[0].toString()):0L);
			da.setVehiclePlate(obv[1]!=null?obv[1].toString():"-");

			da.setDeviceId(null);
			da.setDeviceFacialId("-");
		
			da.setActive(false);

			lista.add(da);
		}
		
		return lista;
		
	}
	
	@Override
	public List<DevicesAvailable> getDevicesAvaliable(Long clientId, Long rolloId){
		System.out.println("Entre a obtener dispositivos disponibles");
		List<DevicesAvailable> lista= new ArrayList<DevicesAvailable>();
		
		List<Object[]> lisv= this.getVehiclesByClient(clientId);
		List<Object[]> lisd= this.getDevicesByRollo(rolloId);
		
		for(int i=0;i<lisv.size();i++){
			DevicesAvailable da= new DevicesAvailable();
			
		    Object[] obv=lisv.get(i);
		    Object[] obd = null;
		    if(i<lisd.size()){
		    	obd= lisd.get(i);
		    }
			
			da.setVehicleId(obv[0]!=null?Long.parseLong(obv[0].toString()):0L);
			da.setVehiclePlate(obv[1]!=null?obv[1].toString():"-");
			if(obd!=null){
				da.setDeviceId(obd[0]!=null?Long.parseLong(obd[0].toString()):0L);
				da.setDeviceFacialId(obd[1]!=null?obd[1].toString():"-");
			}
			else{
				da.setDeviceId(0L);
				da.setDeviceFacialId("-");
			}
			
			da.setActive(false);

			lista.add(da);
		}
		
		return lista;
		
	}
	
	@Override
	public boolean enrolar(Long clientId, Long accountId, List<DevicesAvailable> devices, String ip, Long creationUser){
		boolean res=false;
		try{
			Query q;
			Timestamp fecha= new Timestamp(System.currentTimeMillis());
			// searching client process
			TbProcessTrack pc= process.searchProcess(ProcessTrackType.CLIENT, clientId);
			TbProcessTrack pClient= process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, clientId);
			TbDevice dev;
			TbVehicle tv;
			for(DevicesAvailable da: devices){
				dev= em.find(TbDevice.class, da.getDeviceId());
				tv= em.find(TbVehicle.class, da.getVehicleId());
				
				System.out.println("dispositivo: " +da.getDeviceFacialId());
				System.out.println("Se va a enrolar la placa: " + da.getVehiclePlate()+ " - con el tag: "+da.getDeviceFacialId() );
			    q= em.createNativeQuery("insert into re_account_device values (re_account_device_seq.nextval,?1,?2,?3,0,?4)");
			    q.setParameter(1, accountId);
			    q.setParameter(2, da.getDeviceId());
			    q.setParameter(3, da.getVehicleId());
			    q.setParameter(4, fecha);
			    
			    int e=q.executeUpdate();
			    System.out.println("e" +e);
			    if(e>0){
			        Query q1= em.createNativeQuery("select tr.roll_id, tc.courier_id from tb_device td "
 							  + " left join tb_roll tr on td.device_roll_id= tr.roll_id "
 							  + " left join tb_courier tc on tr.courier_id= tc.courier_id "
 							  + " where td.device_id=?1");
 
					q1.setParameter(1, da.getDeviceId());
					Object[] c=(Object[]) q1.getSingleResult();
					Long ri=0L, ci=0L;
					if(c!=null){
					 	ri=c[0]!=null?Long.parseLong(c[0].toString()):0L;
					 	ci=c[1]!=null?Long.parseLong(c[1].toString()):0L;
					}
						 
					TbLogAdminDevice tlad= new TbLogAdminDevice();
					tlad.setUserId(clientId);
					tlad.setDeviceFacialIdOld("");
					tlad.setDeviceFacialIdNew(da.getDeviceFacialId());
					tlad.setPlate(da.getVehiclePlate());
					tlad.setCourierId(ci);
					tlad.setRollId(ri);
					tlad.setCobro(null);
					tlad.setObservation("");
					tlad.setDateTransaction(fecha);
					tlad.setActionName("Enrolamiento de Tag");
					tlad.setAccountId(accountId);
					em.persist(tlad);	 
					
					
					/*
					 * Se crea tag con estdo preactivo y si ya existe se modifica	
					 */

					BigDecimal manufaturerId=null;
					try{
						Query qr = em.createNativeQuery("select to_number(tt.tag_type_code) from re_device_tag_type rdt "+
								"inner join tb_device d on rdt.device_id=d.device_id "+
								"inner join tb_tag_type tt on rdt.tag_type_id=tt.tag_type_id "+
								"where d.device_id=?1 and tt.tag_state=1");
						qr.setParameter(1, da.getDeviceId());	
						manufaturerId = (BigDecimal) qr.getSingleResult();
					}catch(Exception ex){
						ex.printStackTrace();
					}
					
					TbTag tag=null;
					try{
						
						Long contractualAuth=null;
						String valor=null;
						//Query qe=em.createNativeQuery("select system_parameter_value from tb_system_parameter where system_parameter_id=34");
						Query qe =em.createQuery("select systemParameterValue from TbSystemParameter where systemParameterId in (34L)");
						valor= (String) qe.getSingleResult();
						if(valor!=null){
							contractualAuth= new Long(valor);
						}
						System.out.println("contractualAuth en enrolar" + contractualAuth);
						
						String t=dev.getDeviceCode();
						tag= em.find(TbTag.class,t);
						System.out.println("tag" + tag);
						if(tag!=null){
							tag.setDeviceCode(dev.getDeviceFacialId());
							tag.setDeviceCurrentBalance(dev.getDeviceCurrentBalance());
							tag.setCategoryId(tv.getTbCategory().getCategoryId());
							tag.setDeviceStateId(15L);
							tag.setDeviceTypeId(VialTypeTag.PREPAGO.getTipo());
							tag.setTagTypeCode(manufaturerId!=null?manufaturerId.longValue():null);
							tag.setVehiclePlate(da.getVehiclePlate());
							tag.setContractualAuth(contractualAuth);
							tag.setAccountId(accountId);
							
							emObj = new ObjectEM(em);
							if(emObj.update(tag)){
								System.out.println(tag.toString());
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
							tag.setVehiclePlate(da.getVehiclePlate());	
							tag.setContractualAuth(contractualAuth);
							tag.setAccountId(accountId);
							
							emObj = new ObjectEM(em);
							if(emObj.create(tag)){
								System.out.println(tag.toString());
							}
							System.out.println("Se creó tag");
						}						
					
					}catch(Exception ex){
						ex.printStackTrace();
					}
	 
					log.insertLog(
							"Enrolar dispositivo a Cuenta | Se ha asociado la cuenta ID: "
							 + accountId + ", con el dispositivo ID: " + da.getDeviceFacialId() +" y la placa: "+ da.getVehiclePlate(),
							 LogReference.ACCOUNT, LogAction.CREATE, ip,
							 creationUser);
					

					if ( pc!=null && pClient!=null) {

						if(devices!=null && devices.size()==1){
							process.createProcessDetail(pc.getProcessTrackId(),ProcessTrackDetailType.DEVICE_ACCOUNT_ENROLL,
									"Se asoció el dispositivo con ID Facial: "+da.getDeviceFacialId()+" al vehículo con placa: " +
											da.getVehiclePlate()+".", creationUser, ip, 1, "Error en transaccion DEVICE_ACCOUNT_ENROLL",null,null,null,null);
							
							process.createProcessDetail(pClient.getProcessTrackId(),ProcessTrackDetailType.DEVICE_ACCOUNT_ENROLL,
									"Se asoció el dispositivo con ID Facial: "+da.getDeviceFacialId()+" al vehículo con placa: " +
											da.getVehiclePlate()+" y está disponible para su activación.", creationUser, ip, 1, "Error en transaccion DEVICE_ACCOUNT_ENROLL",null,null,null,null);
                        try{
                        	System.out.println("se envio un solo correo por un tag enrolado");
							outbox.insertMessageOutbox(clientId, 
							        EmailProcess.ASSOCIATION_DEVICE,
							        accountId,
							        null,
							        null, 
							        da.getDeviceId(),
							        da.getVehicleId(),
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
                        	res=true;
                        }

						}
					}
			    }

			}

			em.flush();
			res=true;
			
			int cont=0;
			if(devices!=null){
				cont= devices.size();
				if (cont>1){
					process.createProcessDetail(pc.getProcessTrackId(),ProcessTrackDetailType.DEVICE_ACCOUNT_ENROLL,
							"Se asociaron "+cont+" dispositivos.", creationUser, ip, 1, "Error en transaccion",null,null,null,null);
					
					process.createProcessDetail(pClient.getProcessTrackId(),ProcessTrackDetailType.DEVICE_ACCOUNT_ENROLL,
							"Se asociaron "+cont+" dispositivos, los cuales están disponibles para su activación.", creationUser, ip, 1, "Error en transaccion",null,null,null,null);

					try{
						System.out.println("se envio un solo correo por varios tags enrolado");
						outbox.insertMessageOutbox(clientId, 
						        EmailProcess.ENROL_DEVICES_MASSIVE,
						        accountId,
						        null,
						        null, 
						        null,
						        null,
						        Long.parseLong(String.valueOf(cont)),
						        null,
						        null, 
						        creationUser,
						        null,
						        null,
						        null,
						        true,
							       null);
					}catch(Exception ex){
						res=true;
					}
					
				}
			}
			
		}
		catch(Exception ex){
			res=false;
			ex.printStackTrace();	
		}

		return res;
	}
	

	@Override
	public String getFacialByVehicle(Long vehicleId){
		String facial="";
		
		try{
			Query q= em.createNativeQuery("select td.device_facial_id from re_account_device rad "
					+ " inner join tb_device td on rad.device_id= td.device_id "
					+ " inner join tb_vehicle tv on rad.vehicle_id= tv.vehicle_id "
					+ " where tv.vehicle_id=?1 and rad.relation_state=0" );
					q.setParameter(1, vehicleId);
					
			facial=(String) q.getSingleResult();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		
		return facial;
	}
	
	@Override
	public String getVehicleById(Long vehicleId){
		String placa="";
		
		try{
			Query q= em.createNativeQuery("select vehicle_plate from tb_vehicle where vehicle_id=?1" );
			q.setParameter(1, vehicleId);
					
			placa=(String) q.getSingleResult();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return placa;
	}
	
	@Override
	public TbDevice getDeviceAvailableByRollo(Long rolloId){
		TbDevice td=null;
		try{
			Query q= em.createNativeQuery("select device_id from ( "
					  +" select device_id from tb_device  "
					  +" where device_state_id =15 and device_roll_id=?1 "
					  +" and device_id not in (select device_id from re_account_device) "
					  +" order by device_entry_date) "
					  +" where rownum=1");
			q.setParameter(1, rolloId);
			
			BigDecimal t= (BigDecimal) q.getSingleResult();
			
			if(t!=null){
			  td= em.find(TbDevice.class, t.longValue());
			  if(td!=null){
			    System.out.println("tag: " + td.getDeviceFacialId());
			  }
			}
		}catch(NoResultException ex){
			ex.printStackTrace();
		}
	
		return td;
	}
	
	@Override
	public boolean saveChangeDevice(Long clientId, Long accountId, Long deviceId,String facialNew, String facialOld,
			                         Long vehicleId, String observation, Long creationUser, String ip){
		boolean res=false;
		System.out.println("Entre al metodo EnrollDeviceEJB.saveChangeDevice");
		try{
			Query qq= em.createNativeQuery("select device_id from re_Account_device where account_id=?1 and vehicle_id=?2 and relation_state=0");
			qq.setParameter(1, accountId);
			qq.setParameter(2, vehicleId);
			BigDecimal dev= (BigDecimal) qq.getSingleResult();
			
			Long deviceOld=0L;
			if(dev!=null){
				deviceOld= dev.longValue();
			}
			Query q= em.createNativeQuery("update re_account_device " +
					"set relation_state=1,date_transaction=systimestamp " +
					"where account_id=?1 and vehicle_id=?2 and relation_state=0 ");
			q.setParameter(1, accountId);
			q.setParameter(2, vehicleId);
			
			int e=q.executeUpdate();
			
			System.out.println("e: "+e);
			
			Query query= em.createNativeQuery("insert into re_account_device values (re_account_device_seq.nextval,?1,?2,?3,0,systimestamp)");
			query.setParameter(1, accountId);
			query.setParameter(2, deviceId);
			query.setParameter(3, vehicleId);
			
			int e1=query.executeUpdate();
			
			if(e1>0){
				
				TbDevice td= em.find(TbDevice.class, deviceOld);
				
				td.setTbDeviceState(em.find(TbDeviceState.class, 7L));
				String deviceCode=td.getDeviceCode();
				em.merge(td);
				em.flush();
				
				Query q2= em.createNativeQuery("update tag set equipmentstatus=2 where equipmentobuid=?1");
				q2.setParameter(1, deviceCode);
				q2.executeUpdate();
				
				Query q1= em.createNativeQuery("select tr.roll_id, tc.courier_id from tb_device td "
						  + " left join tb_roll tr on td.device_roll_id= tr.roll_id "
						  + " left join tb_courier tc on tr.courier_id= tc.courier_id "
						  + " where td.device_id=?1");

				q1.setParameter(1, deviceId);
				Object[] c=(Object[]) q1.getSingleResult();
				Long ri=0L, ci=0L;
				if(c!=null){
				 	ri=c[0]!=null?Long.parseLong(c[0].toString()):0L;
				 	ci=c[1]!=null?Long.parseLong(c[1].toString()):0L;
				}
				
				TbVehicle tv=null;
				String placa="";
				if(vehicleId!=null){
					tv= em.find(TbVehicle.class, vehicleId);
					placa=(tv!=null?tv.getVehiclePlate():"");
				}
				
				if(observation!=null){
					observation=observation.toUpperCase();
				}
				
				TbLogAdminDevice tlad= new TbLogAdminDevice();
				tlad.setUserId(clientId);
				tlad.setDeviceFacialIdOld(facialOld);
				tlad.setDeviceFacialIdNew(facialNew);
				tlad.setPlate(placa);
				tlad.setCourierId(ci);
				tlad.setRollId(ri);
				tlad.setCobro(null);
				tlad.setObservation(observation);
				tlad.setDateTransaction(new Timestamp(System.currentTimeMillis()));
				tlad.setActionName("Cambio de Tag");
				tlad.setAccountId(accountId);
				em.persist(tlad);
				em.flush();
					 
					 
				log.insertLog(
						"Cambio dispositivo a Vehículo | Se le ha cambiado el dispositivo ID: " + facialNew +" a la placa: "+ placa,
						 LogReference.DEVICE, LogAction.UPDATE, ip,
						 creationUser);
				
				TbProcessTrack pClient= process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, clientId);
				TbProcessTrack pc= process.searchProcess(ProcessTrackType.CLIENT, clientId);
				
				if ( pc!=null && pClient!=null) {
					
					process.createProcessDetail(pClient.getProcessTrackId(),ProcessTrackDetailType.DEVICE_ACCOUNT_CHANGE,
							"Se cambió el dispositivo con ID Facial: " +facialOld+ " por el dispositivo con ID Facial: "+
							facialNew+ " para el vehículo con placa: "+placa+" y está disponible para su activación.", creationUser, ip, 1, "Error en transaccion DEVICE_ACCOUNT_CHANGE",null,null,null,null);	
			
				    process.createProcessDetail(pc.getProcessTrackId(),ProcessTrackDetailType.DEVICE_ACCOUNT_CHANGE,
				    		"Se remplazó el dispositivo con ID Facial: " +facialOld+ " por el dispositivo con ID Facial: "+
							facialNew+ " para el vehículo con placa: "+placa+"." , creationUser, ip, 1, "Error en transaccion DEVICE_ACCOUNT_CHANGE",null,null,null,null);
						
					outbox.insertMessageOutbox(clientId, 
					                           EmailProcess.CHANGE_DEVICE,
					                           accountId,
					                           null,
					                           null, 
					                           deviceId,
					                           vehicleId,
					                           null,
					                           null,
					                           null, 
					                           creationUser,
					                           null,
					                           facialOld,
					                           "Cambio",
					                           true,
										       null);



				}
				res=true;
			}
		}catch(Exception ex){
			res=false;
			ex.printStackTrace();
		}
		return res;
	}
	
	@Override      
	public boolean saveReplaceDevice(Long clientId, Long accountId, Long deviceId,String facialNew, String facialOld,
				            Long vehicleId, String observation, Long creationUser, String ip, Long cobro){
	    boolean res=false;
	    Long contractualAuth=null;
		String valor=null;
		System.out.println("Entre al metodo EnrollDeviceEJB.saveReplaceDevice");
		try{
			Query qq= em.createNativeQuery("select device_id from re_Account_device where account_id=?1 and vehicle_id=?2 and relation_state=0");
			qq.setParameter(1, accountId);
			qq.setParameter(2, vehicleId);
			BigDecimal dev= (BigDecimal) qq.getSingleResult();
			
			Long deviceOld=0L;
			if(dev!=null){
				deviceOld= dev.longValue();
			}
			Query q= em.createNativeQuery("update re_account_device " +
					"set relation_state=1,date_transaction=systimestamp " +
					"where account_id=?1 and vehicle_id=?2 and relation_state=0 ");
			q.setParameter(1, accountId);
			q.setParameter(2, vehicleId);
			
			int e=q.executeUpdate();
			
			System.out.println("e: "+e);
			
			Query query= em.createNativeQuery("insert into re_account_device values (re_account_device_seq.nextval,?1,?2,?3,0,systimestamp)");
			query.setParameter(1, accountId);
			query.setParameter(2, deviceId);
			query.setParameter(3, vehicleId);
			
			int e1=query.executeUpdate();
			
			Query qSystemParameterValue =em.createQuery("select systemParameterValue from TbSystemParameter where systemParameterId in (34L)");
			valor= (String) qSystemParameterValue.getSingleResult();
			if(valor!=null){
				contractualAuth= new Long(valor);
			}
			
			
			if(e1>0){
				if(savePayment(accountId, deviceOld, cobro, ip, creationUser)){
				//	TbAccount ta = em.find(TbAccount.class, clientId);
					TbDevice td= em.find(TbDevice.class, deviceOld);
					
					td.setTbDeviceState(em.find(TbDeviceState.class, 7L));
					String deviceCode=td.getDeviceCode();
					em.merge(td);
					em.flush();
					
					Query q2= em.createNativeQuery("update tag set equipmentstatus=2 where equipmentobuid=?1");
					q2.setParameter(1, deviceCode);
					q2.executeUpdate();
					
					TbDevice device=em.find(TbDevice.class, deviceId);
					
					
					
					TbVehicle tvH=em.find(TbVehicle.class, vehicleId);
					
					BigDecimal manufaturerId=null;
					try{
						Query qr = em.createNativeQuery("select to_number(tt.tag_type_code) from re_device_tag_type rdt "+
								"inner join tb_device d on rdt.device_id=d.device_id "+
								"inner join tb_tag_type tt on rdt.tag_type_id=tt.tag_type_id "+
								"where d.device_id=?1 and tt.tag_state=1");
						qr.setParameter(1, device.getDeviceId().longValue());	
						manufaturerId = (BigDecimal) qr.getSingleResult();
					}catch(Exception ex){
						ex.printStackTrace();					
					}
					
					TbTag tag=new TbTag();
					tag.setDeviceId(device.getDeviceCode());
					tag.setDeviceCode(device.getDeviceFacialId());
					tag.setDeviceCurrentBalance(device.getDeviceCurrentBalance());
					tag.setCategoryId(tvH.getTbCategory().getCategoryId());
					tag.setDeviceStateId(15L);
					tag.setDeviceTypeId(VialTypeTag.PREPAGO.getTipo());
					tag.setTagTypeCode(manufaturerId!=null?manufaturerId.longValue():null);
					tag.setVehiclePlate(tvH.getVehiclePlate());	
					tag.setContractualAuth(contractualAuth);
					tag.setAccountId(accountId);
					emObj = new ObjectEM(em);
					if(emObj.create(tag)){
						System.out.println(tag.toString());
						transitTask.createTask(TypeTask.DEVICE, tag.getDeviceId());
					}
					System.out.println("Se creó tag");
										

				    Query q1= em.createNativeQuery("select tr.roll_id, tc.courier_id from tb_device td "
                              + " left join tb_roll tr on td.device_roll_id= tr.roll_id "
                              + " left join tb_courier tc on tr.courier_id= tc.courier_id "
                              + " where td.device_id=?1");

				    
				    q1.setParameter(1, deviceId);
				    Object[] c=(Object[]) q1.getSingleResult();
				    Long ri=0L, ci=0L;
				    if(c!=null){
				    	ri=c[0]!=null?Long.parseLong(c[0].toString()):0L;
				    	ci=c[1]!=null?Long.parseLong(c[1].toString()):0L;
				    }

				    TbVehicle tv=null;
				    String placa="";
				    if(vehicleId!=null){
				    	tv= em.find(TbVehicle.class, vehicleId);
				    	placa=(tv!=null?tv.getVehiclePlate():"");
				    }

				    if(observation!=null){
						observation=observation.toUpperCase();
					}
				    
				    TbLogAdminDevice tlad= new TbLogAdminDevice();
				    tlad.setUserId(clientId);
				    tlad.setDeviceFacialIdOld(facialOld);
				    tlad.setDeviceFacialIdNew(facialNew);
				    tlad.setPlate(placa);
				    tlad.setCourierId(ci);
				    tlad.setRollId(ri);
				    tlad.setCobro(new BigDecimal(cobro));
				    tlad.setObservation(observation);
				    tlad.setDateTransaction(new Timestamp(System.currentTimeMillis()));
				    tlad.setActionName("Reposición de Tag");
				    tlad.setAccountId(accountId);
				    em.persist(tlad);
				    em.flush();


					log.insertLog(
					"Reposición de dispositivo a Vehículo | Se le ha hecho reposición de dispositivo con ID: " + facialNew +" a la placa: "+ placa,
					LogReference.DEVICE, LogAction.UPDATE, ip,
					creationUser);
					
					
					TbProcessTrack pc= process.searchProcess(ProcessTrackType.CLIENT, clientId);
					TbProcessTrack pClient= process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, clientId);
					if ( pc!=null && pClient!=null) {
					
						process.createProcessDetail(pClient.getProcessTrackId(),ProcessTrackDetailType.DEVICE_ACCOUNT_REPLACE,
								"Se repuso el dispositivo con ID Facial: "+ facialOld+" por el dispositivo con ID Facial: "+
								facialNew+ " para el vehículo con placa: "+placa+" y está disponible para su activación.", 
								creationUser, ip, 1, "Error en transaccion DEVICE_ACCOUNT_REPLACE",null,null,null,null);
						
						process.createProcessDetail(pc.getProcessTrackId(),ProcessTrackDetailType.DEVICE_ACCOUNT_REPLACE,
								"Se repuso el dispositivo con ID Facial: "+ facialOld+" por el dispositivo con ID Facial: "+
								facialNew+ " para el vehículo con placa: "+placa+".", 
								creationUser, ip, 1, "Error en transaccion DEVICE_ACCOUNT_REPLACE",null,null,null,null);			
						
						
						outbox.insertMessageOutbox(clientId, 
                                EmailProcess.REPLACEMENT_DEVICE,
                                accountId,
                                null,
                                null, 
                                deviceId,
                                vehicleId,
                                null,
                                null,
                                null,
                                creationUser,
                                null,
                                facialOld,
                                "Reposición",                                          
                                true,
 						       null);

					}
					res=true;
				}
				else{
				}
			  
			}
			else{
				res=false;
			}
		}catch(Exception ex){
			res=false;
		    ex.printStackTrace();
		}
		return res;
	}
	
	
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


				
			} else {
				// save failure log
				log.insertLog("Guardar Pago de Dispositivo | Saldo en cuenta sin actualizar. Valor a sumar: $" + value +
						", ID account: " + accoundId + ".", LogReference.ACCOUNT, LogAction.UPDATEFAIL, 
						ip, creationUser);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public String getVehicleByPlateAditional(String placa, String aditional1, String aditional2, String aditional3){
		String vehicleId = null;
		String consulta="select vehicle_plate from tb_vehicle where vehicle_plate is not null ";
		int flag=0;
		if(placa!=null && !placa.equals("")){
			consulta= consulta + " and upper(vehicle_plate)=upper(?1)";
			System.out.println("Entre a placa 1");
			flag=1;
		}
		if(aditional1!=null && !aditional1.equals("")){
			System.out.println("Entre a aditional1 1");
			consulta= consulta + " and upper(aditional1)=upper(?2)";
			flag=1;
		}
		if(aditional2!=null && !aditional2.equals("")){
			System.out.println("Entre a aditional2 1");
			consulta= consulta + " and upper(aditional2)=upper(?3)";
			flag=1;
		}
		if(aditional3!=null && !aditional3.equals("")){
			System.out.println("Entre a aditional3 1");
			consulta= consulta + " and upper(aditional3)=upper(?4)";
			flag=1;
		}
		
		try{
			Query q= em.createNativeQuery(consulta);
			if(placa!=null && !placa.equals("")){
				q.setParameter(1, placa);
				System.out.println("Entre a placa 2");
			}
			if(aditional1!=null && !aditional1.equals("")){
				q.setParameter(2, aditional1);
				System.out.println("Entre a aditional1 2");
			}
			if(aditional2!=null && !aditional2.equals("")){
				System.out.println("Entre a aditional2 2");
				q.setParameter(3, aditional2);
			}
			if(aditional3!=null && !aditional3.equals("")){
				System.out.println("Entre a aditional3 2");
				q.setParameter(4, aditional3);
			}
            System.out.println("consulta: " +consulta);
			if(flag==1){
				System.out.print("Entre a flag 1");
				vehicleId=(String) q.getSingleResult();
			}
			else{
				System.out.print("Entre a flag 0");
			}
		}catch(NoResultException ex){
			ex.printStackTrace();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return vehicleId;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Long> searchListClient(Long typeId, String codClient,String nomClient, String apeClient, String user,
			Long accountClient, String plateClient, String aditional1,String aditional2, String aditional3) {

		List<Long> clientes= new ArrayList<Long>();

		try{
			String consulta= "select tsu.user_id"
				+ " from tb_system_user tsu "
				+ " inner join tb_code_type tct on tsu.code_type_id= tct.code_type_id "
				+ " left join tb_account ta on tsu.user_id= ta.user_id "
				+ " left join re_user_vehicle rv on rv.user_id= tsu.user_id  "
				+ " left join tb_vehicle tv on rv.vehicle_id= tv.vehicle_id "
				+ " where tsu.code_type_id = "+typeId;

			if(!codClient.equals("")){
				consulta= consulta+ " and tsu.user_code=  '"+codClient+"' ";	
			}
			if(!nomClient.equals("")){
				consulta= consulta+ " and upper(tsu.user_names) like upper('%"+nomClient+"%')";	
			}
			if(!apeClient.equals("")){
				consulta= consulta+ " and upper(tsu.user_second_names) like upper('%"+apeClient+"%') ";	
			}
			if(!user.equals("")){
				consulta= consulta+ " and upper(tsu.user_email)= upper('"+user+"') ";	
			}
			if(accountClient!=null){
				if(!accountClient.equals(0L)){
					consulta= consulta+ " and ta.account_id=  "+accountClient;	
				}
			}
			
			if(!plateClient.equals("")){
				consulta= consulta+ " and upper(tv.vehicle_plate)= upper('"+plateClient+"') ";	
			}
			if(!aditional1.equals("")){
				consulta= consulta+ " and upper(tv.aditional1)= upper('"+aditional1+"') ";	
			}
			if(!aditional2.equals("")){
				consulta= consulta+ " and upper(tv.aditional2)= upper('"+aditional2+"') ";	
			}
			if(!aditional3.equals("")){
				consulta= consulta+ " and upper(tv.aditional3)= upper('"+aditional3+"') ";	
			}
			
			consulta= consulta + " group by tsu.user_id, tct.code_type_abbreviation, tsu.user_code, tsu.user_names, tsu.user_second_names ";
							
			
			System.out.println("consulta: "+consulta);		
			
			Query q= em.createNativeQuery(consulta);
			List<Object> lis= q.getResultList();
			System.out.println("lis: " +lis);
			
			if(lis!=null){
			
				Object ob= lis.get(0);
				Long cliente=(Long) ob;
				System.out.println("userId: "+cliente);
				
               clientes.add(cliente);
			
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}

		return clientes;
	}
	
	@Override
	public String getPlateByDevice(Long accountId, Long deviceId){
		String plate="";
		try{
			Query q= em.createNativeQuery(" select tv.vehicle_plate from re_account_device rad "
					+ " inner join tb_vehicle tv on rad.vehicle_id = tv.vehicle_id "
					+ " where rad.account_id=?1 and rad.device_id=?2 and rad.relation_state=0 ");
		
		    q.setParameter(1, accountId);
		    q.setParameter(2, deviceId);
		
		    plate= (String) q.getSingleResult();
		}catch(Exception ex){
			plate="";
			ex.printStackTrace();
		}
		return plate;
	}


	/**
	 * Método creado para listar TAG asociados - estado Preactivo.
	 * @author psanchez
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<AllActiveDispositive> getAllDevice(String userId) {
		 List<AllActiveDispositive> list = new ArrayList<AllActiveDispositive>();
		  try {
				Query query = 
					em.createNativeQuery("SELECT DISTINCT to_char(rad.account_device_id), " +
							"tv.vehicle_plate, tv.aditional1, tv.aditional2, tv.aditional3, " +
							"tv.category_id, td.device_code, td.device_facial_id " +
							"FROM re_account_device rad " +
							"INNER JOIN tb_device td ON rad.device_id = td.device_id " +
							"INNER JOIN tb_account ta ON rad.account_id = ta.account_id " +
							"INNER JOIN tb_vehicle tv ON rad.vehicle_id = tv.vehicle_id " +			    
							"INNER JOIN tb_device_state tde ON tde.device_state_id = td.device_state_id " +
							"WHERE ta.user_id =?1 " +
							"AND td.device_state_id = 15 " +
							"AND rad.relation_state=0 " +
							"ORDER BY 2 ASC ");
				    query.setParameter(1, userId);

					List<Object> lis= (List<Object>)query.getResultList();
					for(int i=0;i<lis.size();i++){
						Object[] o=(Object[]) lis.get(i);                                   					
						AllActiveDispositive activeDispositive = new AllActiveDispositive();
						if(activeDispositive!=null){
							activeDispositive.setReAccountDeviceIdU(Long.parseLong(o[0].toString()));
							activeDispositive.setPlacU(o[1]!=null?o[1].toString():"-");
							activeDispositive.setAditional1U(o[2]!=null?o[2].toString():"-");
							activeDispositive.setAditional2U(o[3]!=null?o[3].toString():"-");
							activeDispositive.setAditional3U(o[4]!=null?o[4].toString():"-");
							activeDispositive.setCategoryU(o[5]!=null?o[5].toString():"-");
							activeDispositive.setDeviceCodeU(o[6]!=null?o[6].toString():"-");
							activeDispositive.setDeviceFacialU(o[7]!=null?o[7].toString():"-");
							list.add(activeDispositive);
						}
					}		
			  } catch (Exception e) {
				System.out.println(" [ Error en EnrollDeviceEJB.getAllDevice Client] ");
				e.printStackTrace();
			}	
		return list;
	}
	
	
	/**
	 * Método creado para listar TAG asociados - estado Preactivo.
	 * @author psanchez
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<AllActiveDispositive> getAllDevice() {
		 List<AllActiveDispositive> list = new ArrayList<AllActiveDispositive>();
		  try {
				Query query = 
					em.createNativeQuery("SELECT DISTINCT to_char(rad.account_device_id), " +
							"tv.vehicle_plate, tv.aditional1, tv.aditional2, tv.aditional3, " +
							"tv.category_id, td.device_code, td.device_facial_id " +
							"FROM tb_system_user tu " +
						    "INNER JOIN Tb_User_Data ud ON Ud.User_Id=Tu.User_Id " +
						    "LEFT JOIN tb_account ta ON ta.user_id=tu.user_id " +
						    "LEFT JOIN Re_Account_Device Rad ON Rad.Account_Id=Ta.Account_Id " +
							"INNER JOIN tb_device td ON rad.device_id = td.device_id " +
							"INNER JOIN tb_vehicle tv ON rad.vehicle_id = tv.vehicle_id " +				    
							"INNER JOIN tb_device_state tde ON tde.device_state_id = td.device_state_id " +
							"AND td.device_state_id = 15 " +
							"AND rad.relation_state=0 " +
							"ORDER BY 2 ASC ");

					List<Object> lis= (List<Object>)query.getResultList();
					for(int i=0;i<lis.size();i++){
						Object[] o=(Object[]) lis.get(i);                                   					
						AllActiveDispositive activeDispositive = new AllActiveDispositive();
						if(activeDispositive!=null){
							activeDispositive.setReAccountDeviceIdU(Long.parseLong(o[0].toString()));
							activeDispositive.setPlacU(o[1]!=null?o[1].toString():"-");
							activeDispositive.setAditional1U(o[2]!=null?o[2].toString():"-");
							activeDispositive.setAditional2U(o[3]!=null?o[3].toString():"-");
							activeDispositive.setAditional3U(o[4]!=null?o[4].toString():"-");
							activeDispositive.setCategoryU(o[5]!=null?o[5].toString():"-");
							activeDispositive.setDeviceCodeU(o[6]!=null?o[6].toString():"-");
							activeDispositive.setDeviceFacialU(o[7]!=null?o[7].toString():"-");
							list.add(activeDispositive);
						}
					}		
			  } catch (Exception e) {
				System.out.println(" [ Error en EnrollDeviceEJB.getAllDevice ] ");
				e.printStackTrace();
			}	
		return list;
	}
	/**
	 * Método creado para listar usando el filtro de búsqueda.
	 * @author psanchez
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<AllActiveDispositive> getDeviceByFiltersClient(String plate, String aditional1, 
			String aditional2, String aditional3, Long category, String account, String userId) {
		List<AllActiveDispositive> list = new ArrayList<AllActiveDispositive>();

		String qry = "";
		String endqry = " ORDER BY 2 ASC ";
		try {	
		qry = "SELECT DISTINCT to_char(rad.account_device_id), " +
				"tv.vehicle_plate, tv.aditional1, tv.aditional2, tv.aditional3, " +
				"tv.category_id, td.device_code, td.device_facial_id " +
				"FROM re_account_device rad " +
				"INNER JOIN tb_device td ON rad.device_id = td.device_id " +
				"INNER JOIN tb_account ta ON rad.account_id = ta.account_id " +
				"INNER JOIN tb_vehicle tv ON rad.vehicle_id = tv.vehicle_id " +
				"INNER JOIN tb_device_state tde ON tde.device_state_id = td.device_state_id " +
				"AND td.device_state_id = 15 " +
				"AND rad.relation_state = 0 " ;		
		

		   if(!userId.equals("")){
				qry = qry+"AND ta.user_id="+userId.trim()+" ";
			}
			if(!plate.equals("")){
				qry = qry+"AND tv.vehicle_plate like '%"+plate.trim()+"%' ";
			}
			if(!aditional1.equals("")){
				qry = qry+"AND Upper(tv.aditional1) like '%"+aditional1.toUpperCase()+"%' ";
			}
			if(!aditional2.equals("")){
				qry = qry+"AND Upper(tv.aditional2) like '%"+aditional2.toUpperCase()+"%' ";
			}
			if(!aditional3.equals("")){
				qry = qry+"AND Upper(tv.aditional3) like '%"+aditional3.toUpperCase()+"%' ";
			}
			if((category!=null) && (category!=-1L)){
				qry = qry+"AND tv.category_id like '%"+category+"%' ";
			} 
			if(!account.equals("")){
				qry = qry+"AND ta.account_id like '%"+account+"%' ";
			} 
			System.out.println("qry: "+qry+endqry);
			Query q = em.createNativeQuery(qry+endqry);
			List<Object> result= (List<Object>)q.getResultList();
			for(int i=0;i<result.size();i++){
				Object[] o=(Object[]) result.get(i);                                   					
				AllActiveDispositive activeDispositive = new AllActiveDispositive();
				if(activeDispositive!=null){
					activeDispositive.setReAccountDeviceIdU(Long.parseLong(o[0].toString()));
					activeDispositive.setPlacU(o[1]!=null?o[1].toString():"-");
					activeDispositive.setAditional1U(o[2]!=null?o[2].toString():"-");
					activeDispositive.setAditional2U(o[3]!=null?o[3].toString():"-");
					activeDispositive.setAditional3U(o[4]!=null?o[4].toString():"-");
					activeDispositive.setCategoryU(o[5]!=null?o[5].toString():"-");
					activeDispositive.setDeviceCodeU(o[6]!=null?o[6].toString():"-");
					activeDispositive.setDeviceFacialU(o[7]!=null?o[7].toString():"-");
					list.add(activeDispositive);
				}
			}
		} catch (Exception e) {
			System.out.println(" [ Error en EnrollDeviceEJB.getActiveDeviceByFilters. ] ");
			e.printStackTrace();
		}	
		return list;
	}
	
	/**
	 * Método creado para listar usando el filtro de búsqueda.
	 * @author psanchez
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<AllActiveDispositive> getDeviceByFiltersAdmin(Long codeType, String doc, String userName, 
			String secondName, String userEmail, String plate, String aditional1, 
			String aditional2, String aditional3, Long category, String account) {
		List<AllActiveDispositive> list = new ArrayList<AllActiveDispositive>();

		String qry = "";
		String endqry = " ORDER BY 2 ASC ";
		try {	
		qry = "SELECT DISTINCT to_char(rad.account_device_id), " +
				"tv.vehicle_plate, tv.aditional1, tv.aditional2, tv.aditional3, " +
				"tv.category_id, td.device_code, td.device_facial_id " +
				"FROM tb_system_user tu " +
			    "INNER JOIN Tb_User_Data ud ON Ud.User_Id=Tu.User_Id " +
				"LEFT JOIN tb_code_type ct ON tu.code_type_id=ct.code_type_id " +
			    "INNER JOIN tb_account ta ON ta.user_id=tu.user_id " +
			    "LEFT JOIN Re_Account_Device Rad ON Rad.Account_Id=Ta.Account_Id " +
			    "INNER JOIN Tb_Vehicle Tv ON Rad.Vehicle_Id=Tv.Vehicle_Id " +
			    "INNER JOIN Tb_device td ON Rad.device_id = Td.device_id " +					    
				"INNER JOIN tb_device_state tde ON tde.device_state_id = td.device_state_id " +
				"AND td.device_state_id = 15 " +
				"AND rad.relation_state=0 " ;		
		
			if((codeType!=null) && (codeType!=-1L)){
				qry = qry+"AND ct.code_type_id="+codeType+" ";
			}
			if(!doc.equals("")){
				qry = qry+"AND tu.user_code like '%"+doc.trim()+"%' ";
			}
			if(!userName.equals("")){
				qry = qry+"AND Upper(tu.user_names) like '%"+userName.toUpperCase()+"%' ";
			}
			if(!secondName.equals("")){
				qry = qry+"AND Upper(tu.user_second_names) like '%"+secondName.toUpperCase()+"%' ";
			}
			if(!userEmail.equals("") && userEmail.contains("_")){
				qry = qry+"AND Upper(tu.user_email) like '%\\_%' ESCAPE '\\' ";
			}
			if(!userEmail.equals("") && !userEmail.equals("_")){
				qry = qry+"AND lower(tu.user_email) like '%"+userEmail.toLowerCase()+"%' ";
			}
			if(!plate.equals("")){
				qry = qry+"AND tv.vehicle_plate like '%"+plate.trim()+"%' ";
			}
			if(!aditional1.equals("")){
				qry = qry+"AND Upper(tv.aditional1) like '%"+aditional1.toUpperCase()+"%' ";
			}
			if(!aditional2.equals("")){
				qry = qry+"AND Upper(tv.aditional2) like '%"+aditional2.toUpperCase()+"%' ";
			}
			if(!aditional3.equals("")){
				qry = qry+"AND Upper(tv.aditional3) like '%"+aditional3.toUpperCase()+"%' ";
			}
			if((category!=null) && (category!=-1L)){
				qry = qry+"AND tv.category_id like '%"+category+"%' ";
			} 
			if(!account.equals("")){
				qry = qry+"AND ta.account_id like '%"+account+"%' ";
			} 
			System.out.println("qry: "+qry+endqry);
			Query q = em.createNativeQuery(qry+endqry);
			List<Object> result= (List<Object>)q.getResultList();
			for(int i=0;i<result.size();i++){
				Object[] o=(Object[]) result.get(i);                                   					
				AllActiveDispositive activeDispositive = new AllActiveDispositive();
				if(activeDispositive!=null){
					activeDispositive.setReAccountDeviceIdU(Long.parseLong(o[0].toString()));
					activeDispositive.setPlacU(o[1]!=null?o[1].toString():"-");
					activeDispositive.setAditional1U(o[2]!=null?o[2].toString():"-");
					activeDispositive.setAditional2U(o[3]!=null?o[3].toString():"-");
					activeDispositive.setAditional3U(o[4]!=null?o[4].toString():"-");
					activeDispositive.setCategoryU(o[5]!=null?o[5].toString():"-");
					activeDispositive.setDeviceCodeU(o[6]!=null?o[6].toString():"-");
					activeDispositive.setDeviceFacialU(o[7]!=null?o[7].toString():"-");
					list.add(activeDispositive);
				}
			}
		} catch (Exception e) {
			System.out.println(" [ Error en EnrollDeviceEJB.getActiveDeviceByFilters. ] ");
			e.printStackTrace();
		}	
		return list;
	}


	/**
	 * Método creado para activar un dispositivo.
	 * @author psanchez
	 */
	@Override
	public boolean getActiveDispositive(List<AllActiveDispositive> listaTemp, String observation, 
			String ip, Long creationUser) {
		Long emisor=null;
		String valor=null;
		try{	
			for(AllActiveDispositive dv: listaTemp){
			     Query query = em.createNativeQuery("SELECT device_id FROM re_account_device WHERE account_device_id=?1 ");
			     query.setParameter(1, dv.getReAccountDeviceIdU());
			     BigDecimal deviceId = (BigDecimal) query.getSingleResult();
			        		     
			     Query query1 = em.createNativeQuery("SELECT vehicle_id FROM re_account_device WHERE account_device_id=?1 ");
			     query1.setParameter(1, dv.getReAccountDeviceIdU());
			     BigDecimal vehicleId = (BigDecimal) query1.getSingleResult();
		    	 
			     TbDevice td = em.find(TbDevice.class,deviceId.longValue());
			     TbVehicle tv = em.find(TbVehicle.class,vehicleId.longValue());
			     TbDeviceState tde= em.find(TbDeviceState.class,1L);

			     Query query3 = em.createNativeQuery("SELECT account_id FROM re_account_device WHERE account_device_id=?1 ");
			     query3.setParameter(1, dv.getReAccountDeviceIdU());
			     BigDecimal accountId = (BigDecimal) query3.getSingleResult();
			     
			     Query query4 = em.createNativeQuery("SELECT user_id FROM tb_account WHERE account_id=?1 ");
			     query4.setParameter(1, accountId);
			     BigDecimal clientId = (BigDecimal) query4.getSingleResult();
			     Long tipBank = -1L;
			     try {
			    	 Query query7 = em.createNativeQuery("select tb.bank_aval from tb_account ta " +
					     		"inner join re_account_bank rak on rak.account_id = ta.account_id " +
					     		"inner join tb_bank_account tba on tba.bank_account_id = rak.bank_account_id " +
					     		"inner join tb_bank tb on tb.bank_id = tba.product " +
					     		"where ta.account_id =?1 " +
					     		"and rak.state_account_bank = 1 ");
					     query7.setParameter(1, accountId);
					     tipBank = ((BigDecimal) query7.getSingleResult()).longValue();
				} catch (Exception e) {
					tipBank = -1L;
				}
			     
			     
				 TbAccount ta = em.find(TbAccount.class, accountId.longValue());
			     
				//update
				emObj = new ObjectEM(em);
				if(emObj.update(td)){
				//Query q=em.createNativeQuery("select system_parameter_value from tb_system_parameter where system_parameter_id=34");
				Query q =em.createQuery("select systemParameterValue from TbSystemParameter where systemParameterId in (34L)");
				valor= (String) q.getSingleResult();
				if(valor!=null){
					emisor= new Long(valor);
				}
				System.out.println("emisor-->" + emisor);
				if(td.getTbDeviceType().getDeviceTypeId() == 0L){
					transitTask.createTask(TypeTask.ACCOUNT, ta.getAccountId().toString());
					
					BigDecimal manufaturerId=null;
					try{
						Query qr = em.createNativeQuery("select to_number(tt.tag_type_code) from re_device_tag_type rdt "+
								"inner join tb_device d on rdt.device_id=d.device_id "+
								"inner join tb_tag_type tt on rdt.tag_type_id=tt.tag_type_id "+
								"where d.device_id=?1 and tt.tag_state=1");
						qr.setParameter(1, td.getDeviceId().longValue());	
						manufaturerId = (BigDecimal) qr.getSingleResult();
					}catch(Exception ex){
						ex.printStackTrace();
						return false;

					}

					TbTag tag=null;
					try{						
						 Long stateDevice = deviceEJB.getListReplicationAvalState(accountId.longValue());
						 	 
					     Query query2 = em.createNativeQuery("UPDATE tb_device SET device_state_id=?1, observation=?3 " +
					     		                             "WHERE device_id=?2 ");
					     query2.setParameter(1, stateDevice);
					     query2.setParameter(2, td.getDeviceId());
					     query2.setParameter(3, observation);
					     query2.executeUpdate();
					        
						String t=td.getDeviceCode();
						System.out.println("tag-S " + td.getDeviceCode());
						System.out.println("tag-L " + t);
						tag= em.find(TbTag.class,t);
						System.out.println("tag" + tag);
						
						if (stateDevice == 6){
							stateDevice = 1L;
							System.out.println("Devic State TAG: " + stateDevice);
						}						
						if (stateDevice==3L) {
							ta.setWithoutBalance(1L);
						}else{
							ta.setWithoutBalance(0L);
						}
						em.merge(ta);
						em.flush();
						if(tag!=null){
							tag.setDeviceCode(td.getDeviceFacialId());
							tag.setDeviceCurrentBalance(td.getDeviceCurrentBalance());
							tag.setCategoryId(tv.getTbCategory().getCategoryId());
							tag.setDeviceStateId(stateDevice);
							tag.setDeviceTypeId(VialTypeTag.PREPAGO.getTipo());
							tag.setTagTypeCode(manufaturerId!=null?manufaturerId.longValue():null);
							tag.setVehiclePlate(tv.getVehiclePlate());
							tag.setContractualAuth(emisor);
							tag.setAccountId(ta.getAccountId());
							
							emObj = new ObjectEM(em);
							if(emObj.update(tag)){
								System.out.println(tag.toString());
								transitTask.createTask(TypeTask.DEVICE, tag.getDeviceId());
							}
							System.out.println("Se modificó tag");
						}
						else{
							System.out.println("tag igual a null");
							tag = new TbTag();
							
							tag.setDeviceId(td.getDeviceCode());
							tag.setDeviceCode(td.getDeviceFacialId());
							tag.setDeviceCurrentBalance(td.getDeviceCurrentBalance());
							tag.setCategoryId(tv.getTbCategory().getCategoryId());
							tag.setDeviceStateId(stateDevice);
							tag.setDeviceTypeId(VialTypeTag.PREPAGO.getTipo());
							tag.setTagTypeCode(manufaturerId!=null?manufaturerId.longValue():null);
							tag.setVehiclePlate(tv.getVehiclePlate());	
							tag.setContractualAuth(emisor);
							tag.setAccountId(ta.getAccountId());
							
							emObj = new ObjectEM(em);
							if(emObj.create(tag)){
								System.out.println(tag.toString());
								transitTask.createTask(TypeTask.DEVICE, tag.getDeviceId());
							}
							System.out.println("Se creó tag");
						}						
					}catch(Exception ex){
						ex.printStackTrace();
						return false;
					}
					
					try {
						
						Long stateDevice = deviceEJB.getListReplicationAvalState(accountId.longValue());
						Long RelationID;	
						if (tipBank == 0){							
							MinimumBalanceEJB.setCalculateMinimumBalanceAccount(accountId.longValue(),creationUser,ip);
					    	 /*Long categoryId = 0L;
					    	 Long categoryClient = vehicleEJB.getMaxCategoryClient(accountId.longValue());
					    	 Long idMini = MinimumBalanceEJB.getMaxCategoryAprob(categoryClient);
					    	 
								if(idMini == 0){
									categoryId = 0L;
								}else{
									jpa.TbMinimumBalance tm = em.find(jpa.TbMinimumBalance.class, idMini);
									categoryId = tm.getCategoryid().getCategoryId() ;
									System.out.println("Id TbMinimumBalance: " + idMini);
								}
					    	 if(categoryClient.equals(categoryId)){
					    		 System.out.println("Entre a PSE con Categoria Maxima Aprobada");
					    		 MinimumBalanceEJB.setAlarmBalance(accountId.longValue(),idMini,creationUser,ta.getTbSystemUser().getUserId(),ip);
					    		 List<BigDecimal> relationDevices;							
					    		 relationDevices = MinimumBalanceEJB.getRelationDevices(accountId.longValue());	
					    		 stateDevice = deviceEJB.getListReplicationAvalState(accountId.longValue());
					    		 
					    		 if (relationDevices.size()>0){
										System.out.println("Entre. se encontraron dispositivos Asociados");
										for (int j = 0; j < relationDevices.size(); j++) {

											RelationID = relationDevices.get(j).longValue();
											System.out.println("Relacion PSE " + RelationID);
											ReAccountDevice re = em.find(ReAccountDevice.class, RelationID);
											System.out.println("Dispositivo: " + re.getTbDevice().getDeviceCode());
											deviceEJB.setStateChange(re.getTbDevice().getDeviceId(),stateDevice);
											System.out.println("Dispositivo: " + re.getTbDevice().getDeviceCode() + " Cambia al estado: " +  stateDevice);

										}
										if (stateDevice==3L) {
											ta.setWithoutBalance(1L);
										}else{
											ta.setWithoutBalance(0L);
										}

										em.merge(ta);
										em.flush();								

									}else{
										System.out.println("No se encontraron dispositivos Activos");
									}
					    		 
					    	 }else{
					    		 System.out.println("Entre a PSE con Categoria Maxima NO Aprobada");
					    		 MinimumBalanceEJB.setAlarmBalance(accountId.longValue(),0L,creationUser,ta.getTbSystemUser().getUserId(),ip);
					    		 stateDevice = deviceEJB.getListReplicationAvalState(accountId.longValue());					    		 
					    		 List<BigDecimal> relationDevices;							
					    		 relationDevices = MinimumBalanceEJB.getRelationDevices(accountId.longValue());	
					    		 
					    		 if (relationDevices.size()>0){
										System.out.println("Entre. se encontraron dispositivos Asociados");
										for (int j = 0; j < relationDevices.size(); j++) {

											RelationID = relationDevices.get(j).longValue();
											System.out.println("Relacion PSE " + RelationID);
											ReAccountDevice re = em.find(ReAccountDevice.class, RelationID);
											System.out.println("Dispositivo: " + re.getTbDevice().getDeviceCode());
											deviceEJB.setStateChange(re.getTbDevice().getDeviceId(),stateDevice);
											System.out.println("Dispositivo: " + re.getTbDevice().getDeviceCode() + " Cambia al estado: " +  stateDevice);

										}
										if (stateDevice==3L) {
											ta.setWithoutBalance(1L);
										}else{
											ta.setWithoutBalance(0L);
										}

										em.merge(ta);
										em.flush();								

									}else{
										System.out.println("No se encontraron dispositivos Activos");
									}
					    		 
					    		 if (!MinimumBalanceEJB.getExistCategoryesAprob()){
					    			 	ArrayList<String> parameters=new ArrayList<String>();
										
										parameters.add("#CATM="+categoryClient);
										parameters.add("#OBERROR="+"FPSM_001");
										System.out.println("parameters: "+parameters.toString());
										outbox.insertMessageOutbox(creationUser,
												EmailProcess.ERROR_MINIMUM_BALANCE, 
												accountId.longValue(), 
												null, 
												null, 
												null, 
												null, 
												null, 
												null, 
												null, 
												creationUser, 
												null, 
												null,
												"No existen Categorías Aprobadas", true,
												parameters);
					    		 }
					    			 
					    			 	ArrayList<String> parameterst=new ArrayList<String>();										
										parameterst.add("#CATM="+categoryClient);
										parameterst.add("#OBERROR="+"FPSM_002");
										System.out.println("parameters: "+parameterst.toString());
										outbox.insertMessageOutbox(creationUser,
												EmailProcess.ERROR_MINIMUM_BALANCE, 
												accountId.longValue(), 
												null, 
												null, 
												null, 
												null, 
												null, 
												null, 
												null, 
												creationUser, 
												null, 
												null,
												"Categoría no Aprobada", true,
												parameterst);			    			 
					    		
					    	 }*/
					     }else{
					    	 System.out.println("Cuenta Aval " + accountId);
					     }
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("Error en EnrollDeviceEJB.getActiveDispositive");
					}
					
					log.insertLog("Activación dispositivo | Se activó el dispositivo con ID Facial: "+
							td.getDeviceFacialId()+" asociado al vehículo con Placa: "+tv.getVehiclePlate(),
							LogReference.DEVICE, LogAction.UPDATE, ip, creationUser);
						
						TbProcessTrack pd = process.searchProcess(ProcessTrackType.DEVICE, td.getDeviceId());
						TbProcessTrack pc= process.searchProcess(ProcessTrackType.CLIENT, clientId.longValue());
						TbProcessTrack pClient= process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, clientId.longValue());
					
					if ( pd!=null && pc!=null && pClient!=null) {
						process.createProcessDetail(pd.getProcessTrackId(), ProcessTrackDetailType.DEVICE_STATE_CHANGE, 
								"Se activó el dispositivo con ID Facial: "+td.getDeviceFacialId()+" asociado al vehículo con Placa: "+
								tv.getVehiclePlate(), creationUser, ip, 1,
								"No se activó el dispositivo con ID Facial: "+td.getDeviceFacialId()+" asociado al vehículo con Placa: "+
								tv.getVehiclePlate()+".",null,null,null,null);
						
						process.createProcessDetail(pc.getProcessTrackId(), ProcessTrackDetailType.DEVICE_STATE_CHANGE, 
								"Se activó el dispositivo con ID Facial: "+td.getDeviceFacialId()+" asociado al vehículo con Placa: "+
								tv.getVehiclePlate(), creationUser, ip, 1,
								"No se activó el dispositivo con ID Facial: "+td.getDeviceFacialId()+" asociado al vehículo con Placa: "+
								tv.getVehiclePlate()+".",null,null,null,null);
							
						process.createProcessDetail(pClient.getProcessTrackId(), ProcessTrackDetailType.DEVICE_STATE_CHANGE, 
								"Se activó el dispositivo con ID Facial: "+td.getDeviceFacialId()+" asociado al vehículo con Placa: "+
								tv.getVehiclePlate(), creationUser, ip, 1,
								"No se activó el dispositivo con ID Facial: "+td.getDeviceFacialId()+" asociado al vehículo con Placa: "+
								tv.getVehiclePlate()+".",null,null,null,null);
					} else {
							log.insertLog("Activación dispositivo | No se activó el dispositivo con ID Facial: "+
									td.getDeviceFacialId()+" asociado al vehículo con Placa: "+tv.getVehiclePlate(),
									LogReference.DEVICE, LogAction.UPDATEFAIL, ip,creationUser);
							return false;
					}
				 return true;
				}
			}else {
				log.insertLog("Activación dispositivo | No se activó el dispositivo con ID Facial: "+
						td.getDeviceFacialId()+" asociado al vehículo con Placa: "+tv.getVehiclePlate(),
						LogReference.DEVICE, LogAction.UPDATEFAIL, ip,creationUser);
				return false;
			}
		}
	  }catch (Exception e) {
		System.out.println(" [ Error EnrollDeviceEJB.getActiveDispositiveAll ] ");
		e.printStackTrace();
		return false;
	  }
		return false;
	}
	
	/**
	 * Método creado para activar mas de un dispositivo.
	 * @author psanchez
	 */
	@Override
	public boolean getActiveDispositiveAll(List<AllActiveDispositive> listfilter, 
			String observation, String ip, Long creationUser) {
		Long emisor=null;
		String valor=null;
		int count=0;
		try{
			for(AllActiveDispositive dv: listfilter){
				System.out.println("dv.getReAccountDeviceIdU-->" + dv.getReAccountDeviceIdU());
			     Query query = em.createNativeQuery("SELECT device_id FROM re_account_device WHERE account_device_id=?1 ");
			     query.setParameter(1, dv.getReAccountDeviceIdU());
			     BigDecimal deviceId = (BigDecimal) query.getSingleResult();
		     		     
			     TbDevice td = em.find(TbDevice.class,deviceId.longValue());
			     TbDeviceState tde= em.find(TbDeviceState.class,1L);
			    			     
			     Query query3 = em.createNativeQuery("SELECT account_id FROM re_account_device WHERE account_device_id=?1 ");
			     query3.setParameter(1, dv.getReAccountDeviceIdU());
			     BigDecimal accountId = (BigDecimal) query3.getSingleResult();
			     
			     Query query4 = em.createNativeQuery("SELECT user_id FROM tb_account WHERE account_id=?1 ");
			     query4.setParameter(1, accountId);
			     BigDecimal clientId = (BigDecimal) query4.getSingleResult();
			     
			     Query query5 = em.createNativeQuery("SELECT vehicle_id FROM re_account_device WHERE account_device_id=?1 ");
			     query5.setParameter(1, dv.getReAccountDeviceIdU());
			     BigDecimal vehicleId = (BigDecimal) query5.getSingleResult();
			     
			     Long tipBank = -1L;
			     try {
			    	 Query query7 = em.createNativeQuery("select tb.bank_aval from tb_account ta " +
					     		"inner join re_account_bank rak on rak.account_id = ta.account_id " +
					     		"inner join tb_bank_account tba on tba.bank_account_id = rak.bank_account_id " +
					     		"inner join tb_bank tb on tb.bank_id = tba.product " +
					     		"where ta.account_id =?1 " +
					     		"and rak.state_account_bank = 1 ");
					     query7.setParameter(1, accountId);
					     tipBank = ((BigDecimal) query7.getSingleResult()).longValue();
				} catch (Exception e) {
					tipBank = -1L;
				};
			      
				 TbAccount ta = em.find(TbAccount.class, accountId.longValue());
				 TbVehicle tv = em.find(TbVehicle.class, vehicleId.longValue());
				 
				 //Query q=em.createNativeQuery("select system_parameter_value from tb_system_parameter where system_parameter_id=34");
					Query q =em.createQuery("select systemParameterValue from TbSystemParameter where systemParameterId in (34L)");
					valor= (String) q.getSingleResult();
					if(valor!=null){
						emisor= new Long(valor);
					}
					System.out.println("emisor-->" + emisor);
					if(td.getTbDeviceType().getDeviceTypeId() == 0L){
						transitTask.createTask(TypeTask.ACCOUNT, ta.getAccountId().toString());
						
						BigDecimal manufaturerId=null;
						try{
							Query qr = em.createNativeQuery("select to_number(tt.tag_type_code) from re_device_tag_type rdt "+
									"inner join tb_device d on rdt.device_id=d.device_id "+
									"inner join tb_tag_type tt on rdt.tag_type_id=tt.tag_type_id "+
									"where d.device_id=?1 and tt.tag_state=1");
							qr.setParameter(1, td.getDeviceId().longValue());	
							manufaturerId = (BigDecimal) qr.getSingleResult();
						}catch(Exception ex){
							ex.printStackTrace();
							return false;

						}
						emObj = new ObjectEM(em);
						if(emObj.update(td)){
							TbTag tag=null;
							try{
																
								Long stateDevice = deviceEJB.getListReplicationAvalState(accountId.longValue());
								
							     Query query2 = em.createNativeQuery("UPDATE tb_device SET device_state_id=?1, observation=?3 " +
														    		 "WHERE device_id=?2 ");
							     query2.setParameter(1, stateDevice);
							     query2.setParameter(2, td.getDeviceId());
							     query2.setParameter(3, observation);
							     query2.executeUpdate();
								
								String t=td.getDeviceCode();
								System.out.println("tag-S " + td.getDeviceCode());
								System.out.println("tag-L " + t);
								tag= em.find(TbTag.class,t);
								System.out.println("tag" + tag);
								//Si el estado es 6 Activo para el DISP, lo pasa a 1 para el TAG
								if (stateDevice == 6){
									stateDevice = 1L;
									System.out.println("Devic State TAG: " + stateDevice);
								}
								if (stateDevice==3L) {
									ta.setWithoutBalance(1L);
								}else{
									ta.setWithoutBalance(0L);
								}
								em.merge(ta);
								em.flush();
								
								if(tag!=null){
									tag.setDeviceCode(td.getDeviceFacialId());
									tag.setDeviceCurrentBalance(td.getDeviceCurrentBalance());
									tag.setCategoryId(tv.getTbCategory().getCategoryId());
									tag.setDeviceStateId(stateDevice);
									tag.setDeviceTypeId(VialTypeTag.PREPAGO.getTipo());
									tag.setTagTypeCode(manufaturerId!=null?manufaturerId.longValue():null);
									tag.setVehiclePlate(tv.getVehiclePlate());
									tag.setContractualAuth(emisor);
									tag.setAccountId(ta.getAccountId());
									
									emObj = new ObjectEM(em);
									if(emObj.update(tag)){
										System.out.println(tag.toString());
										transitTask.createTask(TypeTask.DEVICE, tag.getDeviceId());
									}
									System.out.println("Se modificó tag");
								}
								else{
									System.out.println("tag igual a null");
									tag = new TbTag();
									
									tag.setDeviceId(td.getDeviceCode());
									tag.setDeviceCode(td.getDeviceFacialId());
									tag.setDeviceCurrentBalance(td.getDeviceCurrentBalance());
									tag.setCategoryId(tv.getTbCategory().getCategoryId());
									tag.setDeviceStateId(stateDevice);
									tag.setDeviceTypeId(VialTypeTag.PREPAGO.getTipo());
									tag.setTagTypeCode(manufaturerId!=null?manufaturerId.longValue():null);
									tag.setVehiclePlate(tv.getVehiclePlate());	
									tag.setContractualAuth(emisor);
									tag.setAccountId(ta.getAccountId());
									
									emObj = new ObjectEM(em);
									if(emObj.create(tag)){
										System.out.println(tag.toString());
										transitTask.createTask(TypeTask.DEVICE, tag.getDeviceId());
									}
									System.out.println("Se creó tag");
								}						
							}catch(Exception ex){
								ex.printStackTrace();
								return false;
							}
							//Metodo que Actualiza el Saldo Minimo o Saldo Bajo. al Finalizar replica listas.
							try {
								
								Long stateDevice = deviceEJB.getListReplicationAvalState(accountId.longValue());
								Long RelationID;	
								if (tipBank == 0){							
									MinimumBalanceEJB.setCalculateMinimumBalanceAccount(accountId.longValue(),creationUser,ip);
							    	/* Long categoryId = 0L;
							    	 Long categoryClient = vehicleEJB.getMaxCategoryClient(accountId.longValue());
							    	 Long idMini = MinimumBalanceEJB.getMaxCategoryAprob(categoryClient);
							    	 
										if(idMini == 0){
											categoryId = 0L;
										}else{
											jpa.TbMinimumBalance tm = em.find(jpa.TbMinimumBalance.class, idMini);
											categoryId = tm.getCategoryid().getCategoryId() ;
											System.out.println("Id TbMinimumBalance: " + idMini);
										}
							    	 if(categoryClient.equals(categoryId)){
							    		 System.out.println("Entre a PSE con Categoria Maxima Aprobada");
							    		 MinimumBalanceEJB.setAlarmBalance(accountId.longValue(),idMini,creationUser,ta.getTbSystemUser().getUserId(),ip);
							    		 List<BigDecimal> relationDevices;							
							    		 relationDevices = MinimumBalanceEJB.getRelationDevices(accountId.longValue());	
							    		 stateDevice = deviceEJB.getListReplicationAvalState(accountId.longValue());
							    		 if (relationDevices.size()>0){
												System.out.println("Entre. se encontraron dispositivos Asociados");
												for (int j = 0; j < relationDevices.size(); j++) {

													RelationID = relationDevices.get(j).longValue();
													System.out.println("Relacion PSE " + RelationID);
													ReAccountDevice re = em.find(ReAccountDevice.class, RelationID);
													System.out.println("Dispositivo: " + re.getTbDevice().getDeviceCode());
													deviceEJB.setStateChange(re.getTbDevice().getDeviceId(),stateDevice);
													System.out.println("Dispositivo: " + re.getTbDevice().getDeviceCode() + " Cambia al estado: " +  stateDevice);

												}
												if (stateDevice==3L) {
													ta.setWithoutBalance(1L);
												}else{
													ta.setWithoutBalance(0L);
												}

												em.merge(ta);
												em.flush();								

											}else{
												System.out.println("No se encontraron dispositivos Activos");
											}
							    		 
							    	 }else{
							    		 System.out.println("Entre a PSE con Categoria Maxima NO Aprobada");
							    		 MinimumBalanceEJB.setAlarmBalance(accountId.longValue(),0L,creationUser,ta.getTbSystemUser().getUserId(),ip);
							    		 stateDevice = deviceEJB.getListReplicationAvalState(accountId.longValue());
							    		 List<BigDecimal> relationDevices;							
							    		 relationDevices = MinimumBalanceEJB.getRelationDevices(accountId.longValue());	
							    		 
							    		 if (relationDevices.size()>0){
												System.out.println("Entre. se encontraron dispositivos Asociados");
												for (int j = 0; j < relationDevices.size(); j++) {

													RelationID = relationDevices.get(j).longValue();
													System.out.println("Relacion PSE " + RelationID);
													ReAccountDevice re = em.find(ReAccountDevice.class, RelationID);
													System.out.println("Dispositivo: " + re.getTbDevice().getDeviceCode());
													deviceEJB.setStateChange(re.getTbDevice().getDeviceId(),stateDevice);
													System.out.println("Dispositivo: " + re.getTbDevice().getDeviceCode() + " Cambia al estado: " +  stateDevice);

												}
												if (stateDevice==3L) {
													ta.setWithoutBalance(1L);
												}else{
													ta.setWithoutBalance(0L);
												}

												em.merge(ta);
												em.flush();						

											}else{
												System.out.println("No se encontraron dispositivos Activos");
											}
							    		 
							    		 if (!MinimumBalanceEJB.getExistCategoryesAprob()){
							    			 	ArrayList<String> parameters=new ArrayList<String>();
												
												parameters.add("#CATM="+categoryClient);
												parameters.add("#OBERROR="+"FPSM_001");
												System.out.println("parameters: "+parameters.toString());
												outbox.insertMessageOutbox(creationUser,
														EmailProcess.ERROR_MINIMUM_BALANCE, 
														accountId.longValue(), 
														null, 
														null, 
														null, 
														null, 
														null, 
														null, 
														null, 
														creationUser, 
														null, 
														null,
														"No existen Categorías Aprobadas", true,
														parameters);
							    		 }
							    			 
							    			 	ArrayList<String> parameters=new ArrayList<String>();										
												parameters.add("#CATM="+categoryClient);
												parameters.add("#OBERROR="+"FPSM_002");
												System.out.println("parameters: "+parameters.toString());
												outbox.insertMessageOutbox(creationUser,
														EmailProcess.ERROR_MINIMUM_BALANCE, 
														accountId.longValue(), 
														null, 
														null, 
														null, 
														null, 
														null, 
														null, 
														null, 
														creationUser, 
														null, 
														null,
														"Categoría no Aprobada", true,
														parameters);    		 
							    		 
							    	 }*/
							     }else{
							    	 System.out.println("Cuenta Aval " + accountId);
							     }
							} catch (Exception e) {
								e.printStackTrace();
								System.out.println("Error en EnrollDeviceEJB.getActiveDispositiveAll");
							}
							
							if (count==0){
								log.insertLog("Activación dispositivo | Se activaron "+ listfilter.size() + " dispositivos asociados a sus vehículos.",
										LogReference.DEVICE, LogAction.UPDATE, ip, creationUser);
									
								//searching the process to indicate
								TbProcessTrack pd = process.searchProcess(ProcessTrackType.DEVICE, td.getDeviceId());
								TbProcessTrack pc= process.searchProcess(ProcessTrackType.CLIENT, clientId.longValue());
								TbProcessTrack pClient= process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS, clientId.longValue());
							
								if ( pd!=null && pc!=null && pClient!=null) {
									process.createProcessDetail(pd.getProcessTrackId(), ProcessTrackDetailType.DEVICE_STATE_CHANGE, 
											"Se activaron "+ listfilter.size() + " dispositivos asociados a sus vehículos ", creationUser, ip, 1,
											"No se activaron "+ listfilter.size() + " dispositivos asociados a sus vehículos.",null,null,null,null);
									
									process.createProcessDetail(pc.getProcessTrackId(), ProcessTrackDetailType.DEVICE_STATE_CHANGE, 
											"Se activaron "+ listfilter.size() + " dispositivos asociados a sus vehículos ", creationUser, ip, 1,
											"No se activaron "+ listfilter.size() + " dispositivos asociados a sus vehículos.",null,null,null,null);
										
									process.createProcessDetail(pClient.getProcessTrackId(), ProcessTrackDetailType.DEVICE_STATE_CHANGE, 
											"Se activaron "+ listfilter.size() + " dispositivos asociados a sus vehículos ", creationUser, ip, 1,
											"No se activaron "+ listfilter.size() + " dispositivos asociados a sus vehículos.",null,null,null,null);
									
								     
								}else {
									log.insertLog("Activación dispositivo | No se activaron "+ listfilter.size() + " dispositivos asociados a sus vehículos.",
											LogReference.DEVICE, LogAction.UPDATEFAIL, ip,creationUser);
									return false;
								}
								count++;
							}
				 }
			}
		}
	  }catch (Exception e) {
		System.out.println(" [ Error EnrollDeviceEJB.getActiveDispositiveAll ] ");
		e.printStackTrace();
		return false;
	  }
	  return true;
	}

	@Override
	public Long validateInfoClient(Long codeType, String numberDoc, String userName, String secondName, String userEmail,
			String plate, String aditional1, String aditional2, String aditional3, Long category, String account) {
		Long cont=0L;
		String consulta = "";
		try{
			consulta= "SELECT count(*) from ( " +
				"SELECT distinct(tu.user_id) " +
				"FROM tb_system_user tu " +
			    "INNER JOIN Tb_User_Data ud ON Ud.User_Id=Tu.User_Id " +
				"LEFT JOIN tb_code_type ct ON tu.code_type_id=ct.code_type_id " +
			    "INNER JOIN tb_account ta ON ta.user_id=tu.user_id " +
			    "LEFT JOIN Re_Account_Device Rad ON Rad.Account_Id=Ta.Account_Id " +
			    "INNER JOIN Tb_Vehicle Tv ON Rad.Vehicle_Id=Tv.Vehicle_Id " +
			    "INNER JOIN Tb_device td ON Rad.device_id = Td.device_id " +					    
				"INNER JOIN tb_device_state tde ON tde.device_state_id = td.device_state_id " +
				"AND td.device_state_id = 15 " +
				"AND rad.relation_state=0 ";

			if((codeType!=null) && (codeType!=-1L)){
				consulta = consulta+"AND ct.code_type_id="+codeType+" ";
			}
			if(!numberDoc.equals("")){
				consulta = consulta+"AND tu.user_code like '%"+numberDoc.trim()+"%' ";
			}
			if(!userName.equals("")){
				consulta = consulta+"AND Upper(tu.user_names) like '%"+userName.toUpperCase()+"%' ";
			}
			if(!secondName.equals("")){
				consulta = consulta+"AND Upper(tu.user_second_names) like '%"+secondName.toUpperCase()+"%' ";
			}
			if(!userEmail.equals("") && userEmail.contains("_")){
				consulta = consulta+"AND Upper(tu.user_email) like '%\\_%' ESCAPE '\\' ";
			}
			if(!userEmail.equals("") && !userEmail.equals("_")){
				consulta = consulta+"AND lower(tu.user_email) like '%"+userEmail.toLowerCase()+"%' ";
			}
			if(!plate.equals("")){
				consulta = consulta+"AND tv.vehicle_plate like '%"+plate.trim()+"%' ";
			}
			if(!aditional1.equals("")){
				consulta = consulta+"AND Upper(tv.aditional1) like '%"+aditional1.toUpperCase()+"%' ";
			}
			if(!aditional2.equals("")){
				consulta = consulta+"AND Upper(tv.aditional2) like '%"+aditional2.toUpperCase()+"%' ";
			}
			if(!aditional3.equals("")){
				consulta = consulta+"AND Upper(tv.aditional3) like '%"+aditional3.toUpperCase()+"%' ";
			}
			if((category!=null) && (category!=-1L)){
				consulta = consulta+"AND tv.category_id like '%"+category+"%' ";
			} 
			if(!account.equals("")){
				consulta = consulta+"AND ta.account_id like '%"+account+"%' ";
			} 
					
			consulta= consulta + " ) ";
			System.out.println("consulta: "+consulta);		
					
			Query q= em.createNativeQuery(consulta);
			cont= ((BigDecimal) q.getSingleResult()).longValue();
					
			System.out.println("cont: " +cont);
			
		}catch(NoResultException ex){
			cont=0L;
		}catch(Exception e){
			cont=-1L;
			e.printStackTrace();
		}
		
		return cont;
	}
	
}