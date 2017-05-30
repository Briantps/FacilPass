package ejb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import crud.ObjectEM;

import jpa.ReOptionAction;
import jpa.TbOptActRefefenceType;
import jpa.TbOption;

/**
 * Session Bean implementation class OptionEJB
 */
@Stateless(mappedName = "ejb/Option")
public class OptionEJB implements Option {

	@PersistenceContext(unitName = "bo")
	private EntityManager em;

	private Query validateQuery;
	private Query optQuery;

	/**
	 * Default constructor.
	 */
	public OptionEJB() {

	}

	@PostConstruct
	public void init() {
		validateQuery = em
				.createNativeQuery("SELECT DISTINCT o.option_id, o.option_name "
						+ " FROM re_role_option_action roa "
						+ " INNER JOIN re_option_action oa ON roa.option_action_id = oa.option_action_id "
						+ " INNER JOIN tb_option o ON oa.option_id = o.option_id"
						+ " WHERE roa.role_id = ?1");
		optQuery = em.createQuery("FROM TbOption opt ORDER BY opt.optionId, opt.optionName");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Option#getOptionsByRole(long)
	 */
	@Override
	public List<TbOption> getOptionsByRole(long role) {
		List<TbOption> options = new ArrayList<TbOption>();
		TbOption opt = null;
		Object[] objectList;
		try {
			for (Object object : validateQuery.setParameter(1, role)
					.getResultList()) {
				opt = new TbOption();
				objectList = (Object[]) object;
				opt.setOptionId(((BigDecimal) objectList[0]).longValue());
				opt.setOptionName((String) objectList[1]);
				Query q = em.createNativeQuery("select nvl(max(option_order),0) from tb_option");
				Long result = ((BigDecimal) q.getSingleResult()).longValue();
				opt.setOptionOrder((result+1L));
				options.add(opt);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return options;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Option#createOption(java.lang.String)
	 */
	@Override
	public boolean createOption(String optionName) {
		Query o = em.createNativeQuery("Select count(*) from tb_option where option_name = ?1");
		o.setParameter(1,optionName.toUpperCase());
		int ex = ((BigDecimal) o.getSingleResult()).intValue();
		if(ex == 0){
			TbOption option = new TbOption();		
			option.setOptionName(optionName);
			Query q = em.createNativeQuery("select nvl(max(option_order),0) from tb_option");
			Long result = ((BigDecimal) q.getSingleResult()).longValue();
			option.setOptionOrder((result+1L));
			return new ObjectEM(em).create(option);
		} else {
			return false;
		}
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Option#getAllOptions()
	 */
	@Override
	public List<TbOption> getAllOptions() {
		List<TbOption> optList = new ArrayList<TbOption>();
		try {
			for(Object obj: optQuery.getResultList()){
				optList.add((TbOption)obj);
			}
		} catch (Exception e) {
			System.out
			.println("[ Ocurrio un error al obtener las opciones ]");
			e.printStackTrace();
		}
		return optList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Option#updateOption(long, java.lang.String)
	 */
	@Override
	public boolean updateOption(long optId, String optionName) {
		TbOption option = em.find(TbOption.class, optId);
		option.setOptionName(optionName);
		return new ObjectEM(em).update(option);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TbOption> getListMenu() {
	  try{	
		List<TbOption> list = new ArrayList<TbOption>();
		Query q = em.createQuery("Select opt from TbOption opt order by opt.optionOrder");
		list = (List<TbOption>) q.getResultList();
		return list;
	  }catch (Exception e){
		  return null;
	  }		
	}

	@Override
	public boolean upOrderOptionAction(Long idOptionAction,Long optionId) {
		try {
			ReOptionAction result = em.find(ReOptionAction.class, idOptionAction);
			
			if(result != null){
				Query q1 = em.createNativeQuery("update RE_OPTION_ACTION set OPTION_ACTION_ORDER = (OPTION_ACTION_ORDER+1) where OPTION_ACTION_ORDER = ?1 and Option_id= ?2");
				q1.setParameter(1, (result.getOptionActionOrder()-1L));
				q1.setParameter(2, optionId);
				q1.executeUpdate();	
				
				Query q2 = em.createNativeQuery("update RE_OPTION_ACTION set OPTION_ACTION_ORDER = (OPTION_ACTION_ORDER-1) where OPTION_ACTION_ID = ?1 and Option_id= ?2");					
				q2.setParameter(1, idOptionAction);
				q2.setParameter(2, optionId);
				int aplicados = q2.executeUpdate();					
				if ( aplicados >= 1){
					return true;
				}else {
					return false;
				}
			}else{
				return false;
			}
		}catch(Exception e){
			System.out.println("Error en upOrderOptionAction con el Id "+idOptionAction);
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean downOrderOptionAction(Long idOptionAction, Long optionId) {
		try {
			ReOptionAction result = em.find(ReOptionAction.class, idOptionAction);
			
			if(result != null){
				Query q1 = em.createNativeQuery("update RE_OPTION_ACTION set OPTION_ACTION_ORDER = (OPTION_ACTION_ORDER-1) where OPTION_ACTION_ORDER = ?1 and Option_id= ?2");
				q1.setParameter(1, (result.getOptionActionOrder()+1L));
				q1.setParameter(2, optionId);
				q1.executeUpdate();	
				
				Query q2 = em.createNativeQuery("update RE_OPTION_ACTION set OPTION_ACTION_ORDER = (OPTION_ACTION_ORDER+1) where OPTION_ACTION_ID = ?1 and Option_id= ?2");					
				q2.setParameter(1, idOptionAction);
				q2.setParameter(2, optionId);
				int aplicados = q2.executeUpdate();					
				if ( aplicados >= 1){
					return true;
				}else {
					return false;
				}
			}else{
				return false;
			}
		}catch(Exception e){
			System.out.println("Error en downOrderOptionAction con el Id "+idOptionAction);
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Long getMinValueOrder() {
		Query q = em.createNativeQuery("select min(OPTION_ORDER) from TB_OPTION");
		Long result = ((BigDecimal) q.getSingleResult()).longValue();
		return result;
	}

	@Override
	public Long getMaxValueOrder() {
		Query q = em.createNativeQuery("select max(OPTION_ORDER) from TB_OPTION");
		Long result = ((BigDecimal) q.getSingleResult()).longValue();
		return result;
	}

	@Override
	public boolean downOrderOption(Long idOption) {
		try {
			TbOption result = em.find(TbOption.class, idOption);
			
			if(result != null){
				Query q1 = em.createNativeQuery("update TB_OPTION set OPTION_ORDER = (OPTION_ORDER-1) where OPTION_ORDER = ?1");
				q1.setParameter(1, (result.getOptionOrder()+1L));
				q1.executeUpdate();	
				
				Query q2 = em.createNativeQuery("update TB_OPTION set OPTION_ORDER = (OPTION_ORDER+1) where OPTION_ID = ?1");					
				q2.setParameter(1, idOption);
				int aplicados = q2.executeUpdate();					
				if ( aplicados >= 1){
					return true;
				}else {
					return false;
				}
			}else{
				return false;
			}
		}catch(Exception e){
			System.out.println("Error en downOrderOption con el Id "+idOption);
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean upOrderOption(Long idOption) {
		try {
			TbOption result = em.find(TbOption.class, idOption);
			
			if(result != null){
				Query q1 = em.createNativeQuery("update TB_OPTION set OPTION_ORDER = (OPTION_ORDER+1) where OPTION_ORDER = ?1");
				q1.setParameter(1, (result.getOptionOrder()-1L));
				q1.executeUpdate();	
				
				Query q2 = em.createNativeQuery("update TB_OPTION set OPTION_ORDER = (OPTION_ORDER-1) where OPTION_ID = ?1");					
				q2.setParameter(1, idOption);
				int aplicados = q2.executeUpdate();					
				if ( aplicados >= 1){
					return true;
				}else {
					return false;
				}
			}else{
				return false;
			}
		}catch(Exception e){
			System.out.println("Error en upOrderOption con el Id "+idOption);
			e.printStackTrace();
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public List<ReOptionAction> getListActionsByOption(Long optionId) {
		try{
			List<ReOptionAction> list = new ArrayList<ReOptionAction>();
			Query q = em.createQuery("Select re From ReOptionAction re Where re.optionId.optionId = ?1 order by optionActionOrder");
			q.setParameter(1, optionId);			
			list = (List<ReOptionAction>) q.getResultList();			
			return list;
		}catch(Exception e){
			System.out.println("Error en getListActionsByOption con el Id "+optionId);
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String getNameOptionByOption(Long optionId) {
		TbOption opt = em.find(TbOption.class, optionId);
		String name  = "";
		if(opt != null){
			name = opt.getOptionName();
		}else{
			name = "Opcion No encontrada";
		}
		return name;
	}

	@Override
	public Long getMaxValueOrderAction(Long optionId) {
		Query q = em.createNativeQuery("select max(OPTION_ACTION_ORDER) from RE_OPTION_ACTION where OPTION_ID = ?1");
		q.setParameter(1, optionId);
		Long result=null;
		if(q.getSingleResult()!=null){
			result = ((BigDecimal) q.getSingleResult()).longValue();	
		}
		return result;
	}

	@Override
	public Long getMinValueOrderAction(Long optionId) {
		Query q = em.createNativeQuery("select min(OPTION_ACTION_ORDER) from RE_OPTION_ACTION where OPTION_ID = ?1");
		q.setParameter(1, optionId);
		Long result=null;
		if(q.getSingleResult()!=null){
			result = ((BigDecimal) q.getSingleResult()).longValue();	
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TbOptActRefefenceType> getListReferences() {
		try {
			Query q = em.createQuery("select ref from TbOptActRefefenceType ref order by optActReferenceId");			
			return ((List<TbOptActRefefenceType>) q.getResultList());
		} catch(Exception e){
			System.out.println("Error en getListReferences");
			e.printStackTrace();
			return null;
		}		
	}

	@Override
	public String getNameOptionActionByOptionAction(Long optionActionId) {
		ReOptionAction opt = em.find(ReOptionAction.class, optionActionId);
		String name  = "";
		if(opt != null){
			name = opt.getActionId().getActionName();
		}else{
			name = "Accion No encontrada";
		}
		return name;
	}

	@Override
	public boolean setReferenceIdByOpcAct(Long optionActionId, String referenceId) {
		try{
			ReOptionAction opt = em.find(ReOptionAction.class, optionActionId);
			if(opt != null){
				TbOptActRefefenceType oaft = em.find(TbOptActRefefenceType.class, referenceId);
				if(oaft != null){
					opt.setOptionActionReference(oaft);
					return true;
				}else{
					System.out.println("No se encontro TbOptActRefefenceType "+referenceId); 
					return false;
				}
			}else{
				System.out.println("No se encontro ReOpcionAccionId "+optionActionId); 
				return false;
			}
		}catch(Exception e){
			System.out.println("Error en setReferenceIdByOpcAct");
			e.printStackTrace();
			return false;
		}		
	}
}