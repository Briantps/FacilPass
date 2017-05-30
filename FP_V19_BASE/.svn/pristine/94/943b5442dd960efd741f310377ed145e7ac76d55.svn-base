package ejb;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jpa.TbAccount;
import jpa.TbDevice;
import jpa.TbDeviceState;
import jpa.TbProcessTrack;
import jpa.TbTag;
import util.ReAlarmBalance;
import constant.EmailProcess;
import constant.LogAction;
import constant.LogReference;
import constant.ProcessTrackDetailType;
import constant.ProcessTrackType;
import ejb.email.Outbox;


@Stateless(mappedName = "ejb/LowBalanceAdmin")
public class LowBalanceAdminEJB implements LowBalanceAdmin{
	
	@PersistenceContext(unitName = "bo") 
	EntityManager em;
	
	@EJB(mappedName ="ejb/email/Outbox")
	private Outbox outbox;
	
	@EJB(mappedName="ejb/Process")
	private Process process;
	
	@EJB(mappedName="ejb/Log")
	private Log log;
	
	@EJB(mappedName="ejb/Device")
	private Device deviceEJB;
	
	private static String PUNTO=".";
	
	private List<BigDecimal> devicesList;
	
	public ArrayList<ReAlarmBalance> getAcountUser (long UserId, Long codeType, String numberDoc, String userName, String secondName, String userEmail, String accountId) {
		System.out.println("getAcountUser EJB " + UserId );
		ArrayList<ReAlarmBalance> list = new ArrayList<ReAlarmBalance> ();
		try {
			System.out.println("Se reciben los Siguientes filtos" + codeType + numberDoc + userName + secondName + userEmail + accountId );
			String qry = "";
			String endqry = " ORDER BY vw.account_id DESC ";
			qry = "select distinct vw.account_id cuenta, nvl(tab.val_minimo,0) SaldoMinimo, tab.val_min_alarm SaldoBajo,tb.bank_aval Aval,(case " + 
					  "WHEN tb.bank_aval = 1 " + 
					  "THEN (select 'El Saldo Bajo no prodrá ser inferior a $'|| dbms_lob.substr( system_parameter_value, 4000, 1 ) from tb_system_parameter where system_parameter_id = 4) "+
					  "WHEN tb.bank_aval = 0 " +
					  "THEN ('El Saldo Bajo no podrá ser inferior a $'|| trim(To_char(nvl(tab.val_minimo,0)*2,'999G999G999G999'))) "+
					  "END ) as msn,tu.user_id usrs, nvl(tab.note,' ') " +
				   	  "FROM vw_products_associated_user vw " +
					  "INNER JOIN tb_bank tb on vw.bank_id = tb.bank_id " +
					  "INNER JOIN tb_alarm_balance tab on vw.account_id=tab.account_id " +
					"INNER JOIN tb_system_user tu on tu.user_id = vw.usrs " +
					"INNER JOIN tb_code_type tc on tc.code_type_id = tu.code_type_id " +
					"WHERE tab.id_tip_alarm=4 ";
			
				if((codeType!=null) && (codeType!=-1L)){
					qry = qry+"AND tc.code_type_id="+codeType+" ";
				}
				if(!numberDoc.equals("")){
					qry = qry+"AND tu.user_code like '%"+numberDoc.trim()+"%' ";
				}
				if(!userName.equals("")){
					qry = qry+"AND Upper(tu.user_names) like '%"+userName.toUpperCase()+"%' ";
				}
				if(!secondName.equals("")){
					qry = qry+"AND Upper(tu.user_second_names) like '%"+secondName.toUpperCase()+"%' ";
				}
				if(!userEmail.equals("")){
					qry = qry+"AND lower(tu.user_email) like '%"+userEmail.toLowerCase()+"%' ";
				}
				if(!accountId.equals("")) {
					qry = qry+"AND vw.account_id like '%"+accountId+"%' ";
				}
				
				String queryString =qry+endqry;
				System.out.println("queryString: "+queryString);
					
			Query q = em.createNativeQuery(queryString);
			@SuppressWarnings("unchecked")
			List<Object> lis= (List<Object>)q.getResultList();
			System.out.println("Estoy llenando el objeto " + lis.size());
			for(int i=0;i<lis.size();i++){
				Object[] o=(Object[]) lis.get(i);
				ReAlarmBalance r= new ReAlarmBalance();
				
				
				
				r.setAccount(o[0]!=null?o[0].toString():"-");
				r.setValminimo(formateador((o[1]!=null?((BigDecimal)o[1]):new BigDecimal(0))));
				r.setValbajo(formateador((o[2]!=null?((BigDecimal)o[2]):new BigDecimal(0))));
				/*r.setValminimo(o[1]!=null?o[1].toString():"-");
				r.setValbajo(o[2]!=null?o[2].toString():"-");*/
				r.setAval(o[3]!=null?o[3].toString():"-");
				r.setMsn(o[4]!=null?o[4].toString().trim():"-");
				r.setUsercount(o[5]!=null?o[5].toString():"-");	
				r.setObser("");	
				list.add(r);
				
				
				 
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("El cliente no tiene Cuentas asociadas.");
		}
		System.out.println("list.size()----->"+list.size());
		return list;
	}
	
	public Long getconfirmAcountUser (long usrs,Long codeType,String numberDoc,String userName,String secondName,String userEmail,String accountId){
		
		Long cont = 0L;
		try {
			
			System.out.println("Entre a validar si hay mas de un resultado");
			String qry = "";
			String endqry = " ) ";
			qry = "SELECT COUNT (*) FROM (SELECT DISTINCT tu.user_id usrs " +
					"FROM vw_products_associated_user vw " +
					"INNER JOIN tb_bank tb on vw.bank_id = tb.bank_id " +
					"INNER JOIN tb_alarm_balance tab on vw.account_id=tab.account_id " +
					"INNER JOIN tb_system_user tu on tu.user_id = vw.usrs " +
					"INNER JOIN tb_code_type tc on tc.code_type_id = tu.code_type_id " +
					"WHERE tab.id_tip_alarm=4 ";
			
				if((codeType!=null) && (codeType!=-1L)){
					qry = qry+"AND tc.code_type_id="+codeType+" ";
				}
				if(!numberDoc.equals("")){
					qry = qry+"AND tu.user_code like '%"+numberDoc.trim()+"%' ";
				}
				if(!userName.equals("")){
					qry = qry+"AND Upper(tu.user_names) like '%"+userName.toUpperCase()+"%' ";
				}
				if(!secondName.equals("")){
					qry = qry+"AND Upper(tu.user_second_names) like '%"+secondName.toUpperCase()+"%' ";
				}
				if(!userEmail.equals("")){
					qry = qry+"AND lower(tu.user_email) like '%"+userEmail.toLowerCase()+"%' ";
				}
				if(!accountId.equals("")) {
					qry = qry+"AND vw.account_id like '%"+accountId+"%' ";
				}
				
				String queryString =qry+endqry;
				System.out.println("queryString: "+queryString);
					
			    Query q = em.createNativeQuery(queryString);
			    cont= ((BigDecimal) q.getSingleResult()).longValue();
			    
		}catch(NoResultException ex){
			cont=0L;
		}catch(Exception e){
			cont=-1L;
			e.printStackTrace();
		}
		
		
		return cont;
	}
	
	public Long getValueAval () {
		Long valaval = 0L;
		try {
			System.out.println("getAcountUser EJB. Entre a Validar Parametro 4 Aval");
			//Query q = em.createNativeQuery ("select system_parameter_value from tb_system_parameter where system_parameter_id = 4");
			Query q =em.createQuery("select systemParameterValue from TbSystemParameter where systemParameterId in (4L)");
			String param =(String) q.getSingleResult();
			if(param !=null){
				valaval = Long.parseLong(param) ;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		return valaval;
				
	}
	
	
	public String getUpdateDate (ArrayList<ReAlarmBalance> TemUpdatepaccountclient,String ip,long usrs){
		String msnModal = null;
		Long valueList;
		Long valueMinList;
		
		try {
			
			for (int i = 0; i < TemUpdatepaccountclient.size(); i++) {
				util.ReAlarmBalance value = TemUpdatepaccountclient.get(i);
				
				if (value.getAccount()!= null&& value.getValbajo()!=null){
					valueList = Long.parseLong(value.getValbajo().replace(".", "").replace(",", ""));
					valueMinList = Long.parseLong(value.getValminimo().replace(".", "").replace(",", ""));
					System.out.println("Entre con el Update. Valor Bajo: " + value.getValbajo() +" Cuenta: "+value.getAccount() );
					Query q = em.createNativeQuery("update tb_alarm_balance set val_min_alarm = ?2 , note = ?3 where account_id = ?1 and id_tip_alarm = 4");
					
					q.setParameter(1, value.getAccount());
					q.setParameter(2, valueList);
					q.setParameter(3, value.getObser());
					int count = q.executeUpdate();
										
					if (count > 0){
						System.out.println("Cambio Realizado con exito. : " + value.getValbajo() +" Cuenta: "+value.getAccount() );
						
						outbox.insertMessageOutbox(Long.parseLong(value.getUsercount()), 
								EmailProcess.UPDATE_LOW_BALANCE, 
								Long.parseLong(value.getAccount()), 
								null, 
								null, 
								null, 
								null, 
								null, 
								null, 
								null, 
								usrs, 
								null, 
								null,
								"Actualizacion de Saldo Bajo", true,
							       null);
						
						 
						TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, Long.parseLong(value.getUsercount()));
						Long idPTA;
						if(pt==null){
							idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  Long.parseLong(value.getUsercount()), ip, usrs);
						}
						else{
							idPTA=pt.getProcessTrackId();
						}
						Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.UPDATE_LOW_BALANCE_ADMIN, 
								"Se realizó " +
								"la asignación de saldo bajo a la cuenta FacilPass No. "+value.getAccount()+" por valor de $ "+formateador(new BigDecimal(valueList))+".",
								usrs, ip,1,"Error en la Asignacion",null,null,null,null);			
						//proceso cliente
						TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,Long.parseLong(value.getUsercount()));
						Long idPTC;
						if(ptc==null){
							idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, Long.parseLong(value.getUsercount()), ip, usrs);
						}
						else{
							idPTC=ptc.getProcessTrackId();
						}
						Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.UPDATE_LOW_BALANCE, 
								"Se realizó " +
								"la asignación de saldo bajo a la cuenta FacilPass No. "+value.getAccount()+" por valor de $ "+formateador(new BigDecimal(valueList))+".",
								usrs, ip,1,"Error en la Asignacion",null,null,null,null);
						
						/****/
						
						log.insertLog("Actualizacion de Saldo Bajo | Cuenta: " +value.getAccount() +" Valor: " + formateador(new BigDecimal(valueList)), 
								LogReference.USER,
						LogAction.UPDATE, ip, usrs);
						
						System.out.println("Valor Bajo: " + valueList + "Valor Minimo:  " + valueMinList);
						
						if (value.getAccount()!= null){
							devicesList = getDevicesByAccount(Long.parseLong(value.getAccount()));
							TbAccount ta =em.find(TbAccount.class, Long.parseLong(value.getAccount()));
							
							if (devicesList.size()>0){
								System.out.println("La Lista esta llena");
								Long stateDevice = deviceEJB.getListReplicationAvalState(Long.parseLong(value.getAccount()));
								System.out.println("Valor Bajo: " + valueList + "Valor Minimo:  " + valueMinList);
								System.out.println("Saldo Cuenta: " + ta.getAccountBalance());
								for (int j = 0; j < devicesList.size(); j++) {
									 devicesList.get(j);
									
									Long deviceid = devicesList.get(j)!=null? devicesList.get(j).longValue():null;
									System.out.println("Dispositivo Seleccionado " + deviceid);
									System.out.println("Estado del Dispositivo: " + stateDevice);
									setStateChange(deviceid,stateDevice);
																	
									
									/*
									if(Long.parseLong(value.getAval()) == 1){
										if (ta.getAccountBalance().longValue()<= valueMinList){
											
											System.out.println("Dispositivos sin saldo");
											setStateChange (deviceid,3L);
											ta.setWithoutBalance(1L);
											
										}else if (ta.getAccountBalance().longValue()> valueMinList && ta.getAccountBalance().longValue() <= valueList){
											
											System.out.println("Dispositivos con Saldo Bajo");
											setStateChange (deviceid,4L);
											ta.setWithoutBalance(0L);
											
										}else if (ta.getAccountBalance().longValue()> valueMinList && ta.getAccountBalance().longValue() > valueList){
											
											System.out.println("Dispositivos con Saldo");
											setStateChange (deviceid,6L);
											ta.setWithoutBalance(0L);
											
										}
									}else if(Long.parseLong(value.getAval()) == 0){
										if (ta.getAccountBalance().longValue()< valueMinList){
											
											System.out.println("Dispositivos sin saldo");
											setStateChange (deviceid,3L);
											ta.setWithoutBalance(1L);
											
										}else if (ta.getAccountBalance().longValue()>= valueMinList && ta.getAccountBalance().longValue() < valueList){
											
											System.out.println("Dispositivos con Saldo Bajo");
											setStateChange (deviceid,4L);
											ta.setWithoutBalance(0L);
											
										}else if (ta.getAccountBalance().longValue()> valueMinList && ta.getAccountBalance().longValue() >= valueList){
											
											System.out.println("Dispositivos con Saldo");
											setStateChange (deviceid,6L);
											ta.setWithoutBalance(0L);
											
										}else {
											System.out.println("Error en Cambio estado de listas");
										}							
									}else {
										System.out.println("Error en Tipo de cuenta LowBalanceEJB");
									}
									em.merge(ta);
									em.flush();
									*/
								}
								if (stateDevice==3L) {
									ta.setWithoutBalance(1L);
								}else{
									ta.setWithoutBalance(0L);
								}
								
								em.merge(ta);
								em.flush();
							}else{
								System.out.println("La Lista esta vacia");
							}
							
						}
					}
					
				}
				
			}
			msnModal = "Se han actualizado los valores correctamente";
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("LowBalanceEJB.getUpdateDate");
			msnModal = "No Se han actualizado los valores correctamente";
		}
		
		
		return msnModal;
				
	}
	
	private String formateador (BigDecimal number){
		 
		 String valueChargeTxt = null;
		 
		 if((number != null)){
			 DecimalFormat formateador = new DecimalFormat("###,###.##");
			 valueChargeTxt = formateador.format(number);
			 System.out.println("valueChargeTxt: "+valueChargeTxt);
			 valueChargeTxt = valueChargeTxt.replaceAll(",", ".");

		 }else {
			 valueChargeTxt = "0";
		 }
			 
		return valueChargeTxt;
		 
	 }
	
	public List<BigDecimal> getDevicesByAccount(Long idAccount) {
		List<BigDecimal> list = new ArrayList<BigDecimal>();
		try {
			Query q = em.createNativeQuery("select td.device_id from tb_device td "+
										"inner join re_account_device re on td.device_id = re.device_id "+
										"inner join tb_account ta on re.account_id = ta.account_id "+
										"where re.account_id = ?1 "+
										"and re.relation_state = 0 "+
										"and td.device_state_id in (3,4,6)");
			q.setParameter(1, idAccount);
			for (Object obj :  q.getResultList()) {
				list.add((BigDecimal) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en LowBalanceEJB.getDevicesByAccount ] ");
			e.printStackTrace();
		}
		return list;
	}
	
	public String setStateChange (long deviceid, long state){
		
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
			System.out.println(" [ Error en LowBalanceEJB.setStateChange ] ");
			e.printStackTrace();
		}
		
		return null;
	}
}
