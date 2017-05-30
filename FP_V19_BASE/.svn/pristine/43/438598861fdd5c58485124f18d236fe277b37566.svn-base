package ejb.crud;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import util.AllInfoHoliday;

import constant.LogAction;
import constant.LogReference;
import constant.ProcessTrackDetailType;
import constant.ProcessTrackType;

import jpa.TbHolidays;
import jpa.TbProcessTrack;

import crud.ObjectEM;

import ejb.Log;
import ejb.Process;

/**
 * Session Bean implementation class HolidayEJB
 */
@Stateless(mappedName = "ejb/Holiday")
public class HolidayEJB implements Holiday {
	
	@PersistenceContext(unitName = "bo")
	private EntityManager em;
	
	@EJB(mappedName="ejb/Process")
	private Process process;
	
	@EJB(mappedName = "ejb/Log")
	private Log log;
	
	private ObjectEM objectEM;

    /**
     * Default constructor. 
     */
    public HolidayEJB() {
    }

    /**
	 * Método encargado de listar los dias festivos del año actual.
	 * @return the list
	 * @author psanchez
	 */
   @SuppressWarnings("unchecked")
	@Override
	public List<TbHolidays> getHolidays() {
		List<TbHolidays> holidays = new ArrayList<TbHolidays>();
	  try {
			Query q =
				em.createNativeQuery("SELECT distinct to_char(id_holiday), holiday_date, holiday_name, to_char(sysdate,'YYYY') FROM tb_holidays " +
									 "WHERE to_CHAR(holiday_date, 'YY')= TO_CHAR(SYSDATE,'YY') AND holiday_state=1 " +
									 "ORDER BY holiday_date ASC ");
			
			List<Object> lis= (List<Object>)q.getResultList();
			for(int i=0;i<lis.size();i++){
				Object[] o=(Object[]) lis.get(i);                                   					
				TbHolidays holiday = new TbHolidays();
				if(holiday!=null){	
					holiday.setIdHoliday(Long.parseLong(o[0].toString()));
					holiday.setHolidayDate((Date) o[1]);	
					holiday.setHolidayName(o[2].toString());
					holidays.add(holiday);
				}
			 }		
		  } catch (Exception e) {
			System.out.println(" [ Error en HolidayEJB.getHolidays. ] ");
			e.printStackTrace();
		}
	  return holidays;
	}
	
	@SuppressWarnings("unchecked")
	public List<AllInfoHoliday> getInfoHolidasByFilters(String name, String strDate) {
	  List<AllInfoHoliday> list = new ArrayList<AllInfoHoliday>();
	  try {
		    String qry = "";
			String endqry = " ORDER BY holiday_date ASC ";
			qry = "SELECT distinct to_char(id_holiday), holiday_date, holiday_name " +
				  "FROM tb_holidays " +
				  "WHERE holiday_state=1 " ;
						
			 if(!name.equals("")){
				System.out.println(" [ Ingrese a getInfoHolidasByFilters.name] "+name);
				qry = qry+"AND Upper(holiday_name) LIKE '%"+name.toUpperCase()+"%' ";
			 }if(!strDate.equals("")){
				 qry = qry+"AND to_char(holiday_date, 'yyyy')= ?1 ";	
			 }
			  Query q = em.createNativeQuery(qry+endqry);
			  q.setParameter(1, strDate);
			  System.out.println("qry real: "+qry);
			  
			  List<Object> lis= (List<Object>)q.getResultList();
				for(int i=0;i<lis.size();i++){
					Object[] o=(Object[]) lis.get(i);
		                                       					
					AllInfoHoliday th = new AllInfoHoliday();
					if(th!=null){	
						th.setIdHoliday(Long.parseLong(o[0].toString()));
						th.setHolidayDate((Date) o[1]);	
						th.setDescription(o[2].toString());
	    			  list.add(th);
					}
				}
		} catch (Exception e) {
			System.out.println(" [ Error en HolidayEJB.getInfoHolidasByFilters. ] ");
			e.printStackTrace();
		}
	  return list;
	}

	/**
	 * Método creado para guardar dias festivos y la descripción
	 * @author psanchez
	 */
	@SuppressWarnings("unused")
	public String insertHoliday(Date holidayDate, String holidayName, String ip, Long creationUser) {
	   try {
			// creating the holiday.
			TbHolidays th = new TbHolidays();
			th.setHolidayDate(holidayDate);
			th.setHolidayName(holidayName.toUpperCase().trim());
			th.setHolidayState(1);
			
			objectEM = new ObjectEM(em);
			if(objectEM.create(th)){
				log.insertLog("Creación de Día Festivo | Se ha creado Día Festivo: " + th.getIdHoliday()+ 
				              " fecha " + th.getHolidayDate()+ " descripción" + th.getHolidayName(),LogReference.HOLIDAY, LogAction.DELETE, ip, creationUser);
				//proceso administrador
				TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, creationUser);
				Long idPTA;
				if(pt==null){
					idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  creationUser, ip, creationUser);
				}else{
					idPTA=pt.getProcessTrackId();
				}
				Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.HOLIDAY, 
						    "Se ha creado el dia festivo."+th.getHolidayDate()+ ".", creationUser, ip, 1, "", null,null,null,null);
				
				
				//proceso cliente
				TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,creationUser);
				Long idPTC;
				if(ptc==null){
					idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, creationUser, ip, creationUser);
				}else{
					idPTC=ptc.getProcessTrackId();
				}
				Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.HOLIDAY, 
							"Se ha creado el dia festivo."+th.getHolidayDate()+".", creationUser, ip, 1, "", null,null,null,null);
				return "El día festivo ha sido creado con éxito.";	
			}
		} catch (Exception e) {
			System.out.println(" [ Error en HolidayEJB.insertHoliday. ] ");
			e.printStackTrace();
			return "El día festivo no ha sido creado.";

		}
		return null;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public boolean existHoliday(Date holidayDate) {
	    boolean result=false;
		try{
			Query query= 
				em.createNativeQuery("SELECT id_holiday " +
				                     "FROM tb_holidays " +
									 "WHERE holiday_date = to_date(to_char(?2,'mm/dd/yy'),'mm/dd/yy') " +
									 "AND holiday_state=1 ");
				query.setParameter(2, holidayDate);

				List<Object> list= new ArrayList<Object>();
				list=query.getResultList();
				if(list.size()>0){
					  result=false;
				  }else{
					  result=true; 
				  }
		}catch(NoResultException ex){
			System.out.println(" [ Error en HolidayEJB.existHoliday. ] ");
			result=false;
		}
		return result;
	}
	
	
	@SuppressWarnings("unchecked")
	public boolean existHolidayU(Long idHoliday, Date holidayDate) {
		try{
			Query query= 
				em.createNativeQuery("SELECT holiday_date FROM tb_holidays " +
						             "WHERE id_holiday=?1 " +
						             "AND holiday_state=1 ");
				query.setParameter(1, idHoliday);
				Date HolidayDate=(Date) query.getSingleResult();
				if(holidayDate.equals(HolidayDate)){
					return true;
				}else{
				Query query1= 
					em.createNativeQuery("SELECT id_holiday " +
					                     "FROM tb_holidays " +
										 "WHERE id_holiday <> ?1 " +
										 "AND holiday_date = to_date(to_char(?2,'mm/dd/yy'),'mm/dd/yy') " +
										 "AND holiday_state=1 ");
					query1.setParameter(1, idHoliday);
					query1.setParameter(2, holidayDate);
					List<Object> list= new ArrayList<Object>();
					list=query1.getResultList();
					  if(list != null && list.size()>0){
						  return false;
					  }else{
						  return true;
					  }
				}
		}catch(NoResultException ex){
			System.out.println(" [ Error en HolidayEJB.existHolidayU. ] ");
			 return false;
		}
	}
	
	/**
	 * Método creado para desactivar días festivos
	 * @return return true si el día festivo fué desactivado (2) o false en otro caso.
	 * @author psanchez
	 */
	@SuppressWarnings("unused")
	@Override
	public boolean removeHoliday(Long idHoliday, String ip, Long creationUser) {
	  try {	   
			TbHolidays th= em.find(TbHolidays.class, idHoliday);
            th.setHolidayState(2);
            objectEM = new ObjectEM(em);	
			if(objectEM.create(th)){
				log.insertLog("Desactivar Día Festivo | No se ha desactivado Día Festivo: " + th.getIdHoliday()+ 
				              " fecha " + th.getHolidayDate()+ " descripción" + th.getHolidayName(),LogReference.HOLIDAY, LogAction.DELETE, ip, creationUser);
				  
					//proceso administrador
		        TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, creationUser);
				Long idPTA;
					if(pt==null){
						idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  creationUser, ip, creationUser);
					}else{
						idPTA=pt.getProcessTrackId();
					}
				Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.HOLIDAY,  
						     "Desactivación Día Festivo "+th.getHolidayDate()+".", creationUser, ip, 1, "", null,null,null,null);					
				return true;
			} else {
				log.insertLog("Desactivación Día Festivo | No se ha desactivado Día Festivo: " + th.getIdHoliday()+ 
						      " fecha " + th.getHolidayDate()+ " descripción" + th.getHolidayName(),LogReference.HOLIDAY, LogAction.DELETEFAIL, ip, creationUser);
				return false;
			}
	     } catch (Exception e) {
			System.out.println(" [ Error en HolidayEJB.removeHoliday ] ");
			e.printStackTrace();
			return false;
		}	
	}

	/**
	 * Método creado para editar días festivos y/o descripción
	 * @return return true si los datos fuerón editados o false en otro caso.
	 * @author psanchez
	 */
	@Override
	public String editHoliday(Long idHoliday, Date holidayDate, String holidayName, String ip, Long creationUser) {
	 try {	
			TbHolidays th = em.find(TbHolidays.class, idHoliday);
			Date oldHolidayDate = th.getHolidayDate();
			th.setHolidayDate(holidayDate);
			String oldHolidayName = th.getHolidayName();
			th.setHolidayName(holidayName.toUpperCase().trim());
			//update
			objectEM = new ObjectEM(em);
			if(objectEM.update(th)){
				log.insertLog("Editar Día Festivo | Se ha actualizado el Día Festivo ID: " + th.getIdHoliday()+ 
							". Antes fecha - Descripción: " + oldHolidayDate +  oldHolidayName,
							LogReference.HOLIDAY, LogAction.UPDATE, ip, creationUser);
				return "Los datos del día festivo han sido modificados con éxito.";
			} else {
				log.insertLog("Editar Día Festivo | No Se ha actualizado el Día Festivo ID: " + th.getIdHoliday()+ 
								". Antes fecha - Descripción: " + oldHolidayDate +  oldHolidayName,
								LogReference.HOLIDAY, LogAction.UPDATE, ip, creationUser);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en HolidayEJB.editHoliday. ] ");
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AllInfoHoliday> getAllInfoHolidays() {
	  List<AllInfoHoliday> holidays = new ArrayList<AllInfoHoliday>();
	  try {
			Query q = em.createNativeQuery("SELECT distinct to_char(id_holiday), holiday_date, holiday_name, to_char(sysdate,'YYYY') " +
					                       "FROM tb_holidays WHERE to_CHAR(holiday_date, 'YY')= TO_CHAR(SYSDATE,'YY') " +
					                       "AND holiday_state=1 ORDER BY holiday_date ASC ");

				List<Object> lis= (List<Object>)q.getResultList();
				for(int i=0;i<lis.size();i++){
					Object[] o=(Object[]) lis.get(i);                                   					
					AllInfoHoliday holiday = new AllInfoHoliday();
					if(holiday!=null){	
						holiday.setIdHoliday(Long.parseLong(o[0].toString()));
						holiday.setHolidayDate((Date) o[1]);	
						holiday.setDescription(o[2].toString());
						holidays.add(holiday);
					}
				}		
		  } catch (Exception e) {
			System.out.println(" [ Error en HolidayEJB.getAllInfoHolidays. ] ");
			e.printStackTrace();
		}	
	  return holidays;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AllInfoHoliday> getInfoHolidasByFiltersHolidayName(String holidayName) {
		List<AllInfoHoliday> list = new ArrayList<AllInfoHoliday>();
		try {
				String qry = "";
				String endqry = " ORDER BY holiday_date ASC";
				qry = "SELECT distinct to_char(id_holiday), holiday_date, holiday_name " +
					  "FROM tb_holidays " +
					  "WHERE holiday_state=1 " +
					  "AND to_char(holiday_date, 'yyyy') BETWEEN 2013 AND 9999 ";
							
				if(!holidayName.equals("")){
					qry = qry+"AND Upper(holiday_name) LIKE '%"+holidayName.toUpperCase()+"%' ";
				}
				Query q = em.createNativeQuery(qry+endqry);
				System.out.println("qry real: "+qry);
				List<Object> lis= (List<Object>)q.getResultList();
				for(int i=0;i<lis.size();i++){
					Object[] o=(Object[]) lis.get(i);	
					AllInfoHoliday th = new AllInfoHoliday();
					if(th!=null){	
						th.setIdHoliday(Long.parseLong(o[0].toString()));
						th.setHolidayDate((Date) o[1]);	
						th.setDescription(o[2].toString());
	    			  list.add(th);
					}
				}
			} catch (Exception e) {
				System.out.println(" [ Error en HolidayEJB.getInfoHolidasByFiltersHolidayName. ] ");
				e.printStackTrace();
			}
		return list;
	}

	@Override
	public boolean getValidateYears(String date) {
	  BigDecimal id=null;
	  boolean res=false;
	  try{	
			Query q= em.createNativeQuery("SELECT max(id_holiday) FROM tb_holidays " +
					                      "WHERE holiday_state=1 " +
					                      "AND to_char(holiday_date, 'yyyy')=?1 " +
					                      "AND to_char(holiday_date, 'yyyy') BETWEEN 2013 AND 9999 ");
			q.setParameter(1, date);
			id = (BigDecimal) q.getSingleResult();
			if(id!=null){
				res=true;
			}else{
				res=false;
			}
		  }catch(NoResultException ex){
			System.out.println(" [ Error en HolidayEJB.getValidateHolidays. ] ");
			res=false;
		  }
	return res;	
	}

	
}
