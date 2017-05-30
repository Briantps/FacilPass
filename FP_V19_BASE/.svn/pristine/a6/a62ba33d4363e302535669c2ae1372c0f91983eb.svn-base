package ejb;

import java.math.BigDecimal;
import java.sql.Timestamp;
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

import jpa.TbAccount;
import jpa.TbDailyConcDetail;
import jpa.TbDailyConciliation;
import jpa.TbDailyConciliationState;
import jpa.TbLogDailyConc;
import jpa.TbSystemUser;
import jpa.TbVehicle;
import constant.LogAction;
import constant.LogReference;

/**
 * Bean implementation of Conciliation
 * @author Julián Romero
 */
@Stateless(mappedName = "ejb/Conciliation")
public class ConciliationEJB implements Conciliation {
	
	@PersistenceContext(unitName = "bo")
	EntityManager em;
	
	@EJB(mappedName = "ejb/Log")
	private Log log;
	
	public ConciliationEJB() {
	}

	@Override
	public List<TbDailyConcDetail> getDailyConcDetail(Long dailyConcId) {
		List<TbDailyConcDetail> list = new ArrayList<TbDailyConcDetail>();
		try {
			Query q = em.createNativeQuery("SELECT * FROM tb_daily_conc_detail " +
					"WHERE daily_conc_id=?1", TbDailyConcDetail.class); 
			q.setParameter(1, dailyConcId);
			for(Object ob :q.getResultList()){
				list.add((TbDailyConcDetail) ob);
			}
			System.out.println("getDailyConcDetail.list "+list.size());
		} catch (Exception e) {
			System.out.println(" [ Error en ConciliationEJB.getDailyConcDetail ] ");
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public boolean saveClientConc(Long userId, Long vehicleId, Long accountId,
			Long valueRecharge, Long dailyConcId, String ip) {
		try{
			TbDailyConcDetail dct=new TbDailyConcDetail();
			dct.setAccountId(em.find(TbAccount.class, accountId));
			dct.setDailyConcDetaDate(new Timestamp(System.currentTimeMillis()));
			dct.setDailyConcId(em.find(TbDailyConciliation.class, dailyConcId));
			dct.setRechargeValue(valueRecharge);
			dct.setVehicleId(em.find(TbVehicle.class, vehicleId));
			dct.setUserId(em.find(TbSystemUser.class, userId));
			
			em.persist(dct);
			
			em.flush();
			
			TbDailyConciliation dc=em.find(TbDailyConciliation.class, dailyConcId);
			System.out.println("dailyConcId: "+dailyConcId);
			Long balance=dc.getDailyConcBalance();
			System.out.println("balance: "+balance);
			dc.setDailyConcBalance(balance+valueRecharge);
			
			em.merge(dc);
			
			em.flush();
			
			log.insertLog(" Creación Detalle Conciliación Diaria | Se ha Creado el detalle para la Conciliación Diaria ID: " + 
					dct.getDailyConcDetaId() + ".",LogReference.CONCILIATION, LogAction.CREATE, ip, em.find(TbDailyConciliation.class,
							dailyConcId).getUserId().getUserId());
			return true;
		}catch (Exception e) {
			System.out.println(" [ Error en ConciliationEJB.saveClientConc ] ");
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Long createDailyConc(Long userId, Date dateClosing,String ip) {
		try {	
			Query q = em.createQuery("SELECT dc FROM TbDailyConciliation dc WHERE dc.userId.userId = ?1 " +
					"AND dc.dailyConcDate = ?2");
			q.setParameter(1, userId);
			q.setParameter(2, dateClosing);
			
			q.getSingleResult();
			return 0L;
		
		}catch (NoResultException e) {
			TbDailyConciliation dc=new TbDailyConciliation();
			dc.setDailyConcBalance(0L);
			dc.setDailyConcEffective(0L);
			dc.setDailyConcDiff(0L);
			dc.setDailyConcDate(dateClosing);
			dc.setDailyConcStateId(em.find(TbDailyConciliationState.class, 1L));
			dc.setDateTransaction(new Timestamp(System.currentTimeMillis()));
			dc.setUserId(em.find(TbSystemUser.class, userId));
			
			em.persist(dc);
			
			em.flush();
			
			log.insertLog(" Creación De Conciliación Diaria | Se ha Creado una Conciliación Diaria ID: " + dc.getDailyConcId() + ".",
					LogReference.CONCILIATION, LogAction.CREATE, ip, userId);
			return  dc.getDailyConcId();
		} catch (Exception e) {
			System.out.println(" [ Error en ConciliationEJB.createDailyConc ] ");
			e.printStackTrace();
			return -1L;
		}
	}

	@Override
	public TbDailyConciliation getDailyConc(Long dailyConcId) {
		try{
			return em.find(TbDailyConciliation.class, dailyConcId);
		}catch (Exception e) {
			System.out.println(" [ Error en ConciliationEJB.getDailyConc ] ");
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean createLogDailyConc(Long dailyConcId, Long dailyConcStateId,
			Long valEffe,String observation, Long creatorUser,String ip) {
		try{
			TbDailyConciliation dc=em.find(TbDailyConciliation.class, dailyConcId);
			TbLogDailyConc l=new TbLogDailyConc();
			l.setDailyConcId(dc);
			l.setDailyConcStateId(em.find(TbDailyConciliationState.class, dailyConcStateId));
			l.setLogDailyConcDate(new Timestamp(System.currentTimeMillis()));
			l.setLogDailyConcBala(dc.getDailyConcBalance());
			l.setLogDailyConcDiff(this.calculateDiffDailyConc(dc.getDailyConcBalance(), valEffe));
			l.setLogDailyConcEffe(valEffe);
			l.setLogDailyConcObse(observation);
			l.setUserId(em.find(TbSystemUser.class, creatorUser));
			
			em.persist(l);
			
			em.flush();

			log.insertLog(" Creación Log Conciliación Diaria | Se ha Creado Log para Conciliación Diaria ID: " + l.getLogDailyConcId() + ".",
					LogReference.CONCILIATION, LogAction.CREATE, ip, creatorUser);
			
			return true;
		}catch (Exception e) {
			System.out.println(" [ Error en ConciliationEJB.createLogDailyConc ] ");
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Long calculateDiffDailyConc(Long valBala, Long valueEffec) {
		Long value=valBala-valueEffec;
		System.out.println("value: "+value);
		return value;
	}

	@Override
	public boolean updateStateDailyConc(Long dailyConcId,
			Long dailyConcStateId, Long valEffe,String ip,Long creatorUser) {
		try{
			boolean respu=false;
			TbDailyConciliation dc=em.find(TbDailyConciliation.class, dailyConcId);
			if((dc.getDailyConcStateId().getDailyConcStateId()==1L && dailyConcStateId==2L)
					||(dailyConcStateId==3L && dc.getDailyConcStateId().getDailyConcStateId()==2L)){
				dc.setDailyConcEffective(valEffe);
				dc.setDailyConcDiff(this.calculateDiffDailyConc(dc.getDailyConcBalance(), valEffe));
				dc.setDailyConcStateId(em.find(TbDailyConciliationState.class, dailyConcStateId));
				respu=true;
			}else if(dailyConcStateId==4L && (dc.getDailyConcStateId().getDailyConcStateId()==3L||
					dc.getDailyConcStateId().getDailyConcStateId()==2L)){
				dc.setDailyConcStateId(em.find(TbDailyConciliationState.class, 1L));
				respu=true;
			}
			
			if(respu){
				System.out.println("dailyConcStateId: "+dailyConcStateId);
				dc.setDateTransaction(new Timestamp(System.currentTimeMillis()));
				em.merge(dc);
				em.flush();
				log.insertLog(" Modificación Conciliación Diaria | Se ha Modificado Conciliación Diaria ID: " + dc.getDailyConcId() + ".",
						LogReference.CONCILIATION, LogAction.UPDATE, ip, creatorUser);
			}
			
			return true;
		}catch (Exception e) {
			System.out.println(" [ Error en ConciliationEJB.updateStateDailyConc ] ");
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean isCloseDayById(Long dailyConcId) {
		try{
			if(this.getDailyConc(dailyConcId).getDailyConcStateId().getDailyConcStateId()==1L){
				return false;
			}else{
				return true;
			}
		}catch (Exception e) {
			System.out.println(" [ Error en ConciliationEJB.isCloseDayById ] ");
			e.printStackTrace();
			return true;
		}
	}

	@Override
	public Long getDailyConcByFilters(Long codeType, String code,
			String firstName, String lastName, String userEmail,
			Date fechaCierre) {
		try{
			String query="SELECT DC.DAILY_CONC_ID " +
					"FROM TB_DAILY_CONCILIATION DC, TB_SYSTEM_USER US " +
					"WHERE US.USER_ID = DC.USER_ID " +
					"AND DC.DAILY_CONC_DATE = TO_DATE('"+new SimpleDateFormat("dd/MM/yyyy").format(fechaCierre)+"','DD/MM/YYYY') ";
			if(codeType!=0L&&codeType!=-1L){
				query=query+"AND US.CODE_TYPE_ID="+codeType+" ";
			}
			if(!code.equals("")){
				query=query+"AND US.USER_CODE like '%"+code+"%' ";
			}
			if(!firstName.equals("")){
				query=query+"AND UPPER(US.USER_NAMES) like '%"+firstName.toUpperCase()+"%' ";
			}
			if(!lastName.equals("")){
				query=query+"AND UPPER(US.USER_SECOND_NAMES) like '%"+lastName.toUpperCase()+"%' ";
			}
			if(!userEmail.equals("")){
				query=query+"AND UPPER(US.USER_EMAIL) like '%"+userEmail.toUpperCase()+"%' ";
			}
			System.out.println("query: "+query);
			Query q=em.createNativeQuery(query);
			Long dcId=((BigDecimal)q.getSingleResult()).longValue();
			System.out.println("dcId: "+dcId);
			return dcId;
		}catch (NonUniqueResultException ex1) {
			System.out.println(" [ Error en ConciliationEJB.getDailyConcByFilters.NonUniqueResultException ] ");
			ex1.printStackTrace();
			return -2L;
		}catch (NoResultException ex) {
			System.out.println(" [ Error en ConciliationEJB.getDailyConcByFilters.NoResultException ] ");
			ex.printStackTrace();
			return 0L;
		}catch (Exception e) {
			System.out.println(" [ Error en ConciliationEJB.getDailyConcByFilters ] ");
			e.printStackTrace();
			return -1L;
		}
	}

	@Override
	public Long getDailyConcByDate(Long userId, Date dateClosing) {
		try{
			System.out.println("getDailyConcByDate--");
			System.out.println("userId: "+userId);
			String query="SELECT daily_conc_id " +
					"FROM tb_daily_conciliation " +
					"WHERE user_id = ?1 " +
					"AND daily_conc_date = to_date('"+new SimpleDateFormat("dd/MM/yyyy").format(dateClosing)+"','dd/mm/yyyy')";
			System.out.println("query: "+query);
			Query q=em.createNativeQuery(query);
			q.setParameter(1, userId);
			Long dcId=((BigDecimal)q.getSingleResult()).longValue();
			System.out.println("dcId.getDailyConcByDate: "+dcId);
			return dcId;
		}catch (NonUniqueResultException ex1) {
			System.out.println(" [ Error en ConciliationEJB.getDailyConcByDate.NonUniqueResultException ] ");
			ex1.printStackTrace();
			return -2L;
		}catch (NoResultException ex) {
			System.out.println(" [ Error en ConciliationEJB.getDailyConcByDate.NoResultException ] ");
			ex.printStackTrace();
			return 0L;
		}catch (Exception e) {
			System.out.println(" [ Error en ConciliationEJB.getDailyConcByDate ] ");
			e.printStackTrace();
			return -1L;
		}
	}

	@Override
	public Long getDailyConcStateById(Long dailyConc) {
		try{
			return (em.find(TbDailyConciliation.class, dailyConc))
			.getDailyConcStateId().getDailyConcStateId();
		}catch (Exception e) {
			System.out.println(" [ Error en ConciliationEJB.getDailyConc ] ");
			e.printStackTrace();
			return 0L;
		}
	}
}