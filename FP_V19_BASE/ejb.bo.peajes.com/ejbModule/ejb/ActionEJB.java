package ejb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jpa.TbAction;
import jpa.TbOptActRefefenceType;
import jpa.TbOption;
import jpa.ReOptionAction;
import jpa.TbRole;

import util.Permission;

import crud.ObjectEM;

/**
 * Session Bean implementation class ActionEJB
 */
@Stateless(mappedName = "ejb/Action")
public class ActionEJB implements Action {

	@PersistenceContext(unitName = "bo")
	private EntityManager em;

	private Query queryOAct;
	private Query actionQuery;
	private Query queryUpdateOptAct;
	private Query queryOptAct;
	private Query behavior;

	private ObjectEM objectEm;

	/**
	 * Default constructor.
	 */
	public ActionEJB() {

	}

	@PostConstruct
	public void init() {
		queryOAct = em
				.createNativeQuery("SELECT action_id FROM re_option_action WHERE option_id = ?1");

		queryUpdateOptAct = em
				.createNativeQuery("SELECT option_action_id FROM re_option_action WHERE option_id = ?1 AND action_id  = ?2");

		actionQuery = em.createQuery("FROM TbAction action ORDER BY action.actionId, action.actionName");
		
		queryOptAct = em.createNativeQuery("SELECT option_action_id FROM re_option_action WHERE option_id = ?1 ");
		
		behavior = em.createQuery("SELECT oa FROM ReOptionAction oa  WHERE oa.optionId.optionId = ?1 AND oa.actionId.actionId = ?2");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Action#getAllActions()
	 */
	@Override
	public List<TbAction> getAllActions() {
		List<TbAction> actionList = new ArrayList<TbAction>();
		try {
			for (Object obj : actionQuery.getResultList()) {
				TbAction o = (TbAction) obj;
				if(o.getActionName().trim().length()>0){
				 actionList.add(o);
				}
			}
		} catch (Exception e) {
			System.out.println("[ Ocurrió un error al obtener las acciones. ]");
			e.printStackTrace();
		}
		return actionList;
	}

	/**
	 * 
	 * @param optionId
	 * @param actionId
	 * @return
	 */
	private boolean validateOptAct(long optionId, long actionId) {
		boolean result = false;
		for (Object object : queryOAct.setParameter(1, optionId)
				.getResultList()) {
			try {
				if (actionId == ((BigDecimal) object).longValue()) {
					result = true;
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 
	 * @param optionId
	 * @param actionId
	 * @return
	 */
	private String findBehavior(long optionId, long actionId) {
		String result = "";
		try {
			Query q = behavior.setParameter(1, optionId).setParameter(2, actionId);
			result = ((ReOptionAction)q.getSingleResult()).getBehavior();
		} catch (NoResultException e) {
		} catch(Exception ex){
			System.out.println(" [ Error en ActionEJB.findBehavior ] ");
			ex.printStackTrace();
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Action#getActionsByOption(long)
	 */
	@Override
	public List<util.Permission> getActionsByOption(long optionId) {
		List<util.Permission> permissions = new ArrayList<util.Permission>();
		boolean active = false;
		try {
			for (Object object : getAllActions()) {
				TbAction action = (TbAction) object;
				if (validateOptAct(optionId, action.getActionId())) {
					active = true;
				} else {
					active = false;
				}
				ReOptionAction optAct = new ReOptionAction();
				optAct.setOptionId(em.find(TbOption.class, optionId));
				optAct.setActionId(action);
				optAct.setBehavior(findBehavior(optionId, action.getActionId()));
				optAct.setOptionActionReference(em.find(TbOptActRefefenceType.class, "A"));
				Query q = em.createNativeQuery("select nvl(max(roa.option_action_order),0) from re_option_action roa where roa.option_id= ?1");
				q.setParameter(1, optionId);
				int result = ((BigDecimal) q.getSingleResult()).intValue();				
				optAct.setOptionActionOrder(result+1);
				Permission permission = new Permission(optAct, active);
				permissions.add(permission);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return permissions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Action#createAction(java.lang.String)
	 */
	@Override
	public boolean createAction(String actionName) {
		/* se busca si existe algun action con el mismo nombre */
		Query q = em.createNativeQuery("select count(*) as exit from tb_action where action_name = ?1");
		q.setParameter(1, actionName.toUpperCase());
		int result = ((BigDecimal)q.getSingleResult()).intValue();
		if (result == 0){
			TbAction action = new TbAction();
			action.setActionName(actionName);
			return new ObjectEM(em).create(action);
		} else {
			return false;			
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Action#updateAction(long, java.lang.String)
	 */
	@Override
	public boolean updateAction(long actionId, String actionName) {
		/* se busca si existe algun action con el mismo nombre */
		Query q = em.createNativeQuery("select count(*) as exit from tb_action where action_name = ?1");
		q.setParameter(1, actionName.toUpperCase());
		int result = ((BigDecimal)q.getSingleResult()).intValue();
		if (result == 0){	
			TbAction action = em.find(TbAction.class, actionId);
			action.setActionName(actionName);
			return new ObjectEM(em).update(action);
		} else {
			return false;			
		}	
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Action#updateOptAct(java.util.List)
	 */
	@Override
	public boolean updateOptAct(List<Permission> permissions) {
		boolean result = false;
		objectEm = new ObjectEM(em);
		Object object;
		for (util.Permission permission : permissions) {
			try {
				object = (queryUpdateOptAct.setParameter(1,
						permission.getOptAct().getOptionId().getOptionId())
						.setParameter(
								2,
								permission.getOptAct().getActionId()
										.getActionId()).getSingleResult());
				if (!permission.isActive()) {
					ReOptionAction optAct = em.find(ReOptionAction.class, ((BigDecimal) object)
							.longValue());
					result = objectEm.delete(optAct);
					if (!result) break;
				}
			} catch (NoResultException e) {
				if (permission.isActive()) {
					ReOptionAction optAct = new ReOptionAction();
					optAct.setOptionId(permission.getOptAct().getOptionId());
					optAct.setActionId(permission.getOptAct().getActionId());
					optAct.setBehavior(permission.getOptAct().getBehavior());
					optAct.setOptionActionState(0);
					optAct.setOptionActionReference(em.find(TbOptActRefefenceType.class, "A"));
					Query q = em.createNativeQuery("select nvl(max(roa.option_action_order),0) from re_option_action roa where roa.option_id= ?1");
					q.setParameter(1, permission.getOptAct().getOptionId());
					int order = ((BigDecimal) q.getSingleResult()).intValue();				
					optAct.setOptionActionOrder(order+1);
					result = objectEm.create(optAct);
					if (!result) break;
				}else{
					result = true;
				}
			} catch (Exception e) {
				result = false;
				e.printStackTrace();
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Action#checkIfAnyOptAct(java.util.List)
	 */
	@Override
	public boolean checkIfAnyOptAct(List<Permission> permissions, long idOpt) {
		try{
			@SuppressWarnings("unused")
			Object object;
			try{
				object = (queryOptAct.setMaxResults(1).setParameter(1, idOpt).getSingleResult());
				return true;
			}catch (NoResultException  e) {
				for (util.Permission permission : permissions) {
					if (permission.isActive()) {
						return true;
					}
				}
			}
		}catch (Exception e) {
			System.out.println(" [ Error en ActionEJB.checkIfAnyOptAct ] ");
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Action#valRoleOptionAction(long, long)
	 */
	@Override
	public String valRoleOptionAction(long idOption, long idAction) {
		String result = "";
		try {
			
			Query q = em.createQuery("  SELECT tr " +
												"   FROM "+
												"          ReOptionAction toa, " +
												"          ReRoleOptionAction troa, " +
												"          TbRole tr " +
												"   WHERE  " +
												"          toa.optionActiontId = troa.reOptionAction.optionActiontId " +
												"          AND toa.actionId.actionId = ?1 AND toa.optionId.optionId = ?2 " +
												"          and troa.tbRole.roleId = tr.roleId");
			q.setParameter(1, idAction);
			q.setParameter(2, idOption);
			
			for (Object o : q.getResultList()){
				result = ((TbRole) o).getRoleName() + ", ";
			}
			
		}catch (NoResultException nr){
			
		}catch (Exception e) {
			System.out.println(" [ Error en ActionEJB.valRoleOptionAction ] ");
			e.printStackTrace();
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Action#valBehavior(long, long)
	 */
	@Override
	public String valBehavior(long idOption, long idAction, String behavior) {
		String result = "";
		try {
			Query q = em.createQuery("  SELECT toa " + 
												 "  FROM " +
												 "        ReOptionAction toa " +
												 "  WHERE " + 
												 "        toa.behavior = ?1 ");
			q.setParameter(1, behavior);		
			
			for (Object o : q.getResultList()){
				ReOptionAction oa = (ReOptionAction) o;
				if ((oa.getActionId().getActionId() != idAction
						|| oa.getOptionId().getOptionId() != idOption)
						&& oa.getBehavior().equals(behavior)) {
						return oa.getOptionId().getOptionName()+"-"+oa.getActionId().getActionName();
				}
			}
			
		} catch (NoResultException nr) {
		} catch (Exception e) {
			System.out.println(" [ Error en ActionEJB.valBehavior ] ");
		}
		return result;
	}

	@Override
	public boolean existRelation(long idOption, long idAction) {
		boolean result = false;
		try{
			Query q = em.createQuery("  SELECT toa " +
					"   FROM "+
					"          ReOptionAction toa " +					
					"   WHERE  " +
					"          toa.actionId.actionId = ?1 AND toa.optionId.optionId = ?2 ");
				q.setParameter(1, idAction);
				q.setParameter(2, idOption);
		 if(q.getResultList().size()>0){
			 result=true;
		 }
		}catch(NoResultException e){
			result=false;
		}
		return result;
	}
}