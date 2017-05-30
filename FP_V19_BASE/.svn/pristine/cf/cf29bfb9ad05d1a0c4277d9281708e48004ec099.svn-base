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

import com.itextpdf.text.log.SysoLogger;

import util.OptionActionH;
import util.OptionActions;

import crud.ObjectEM;

import jpa.TbAction;
import jpa.TbOption;
import jpa.ReOptionAction;
import jpa.ReRoleOptionAction;
import jpa.TbRole;

/**
 * Session Bean implementation class PermissionEJB
 */
@Stateless(mappedName = "ejb/Permission")
public class PermissionEJB implements Permission {

	@PersistenceContext(unitName = "bo")
	private EntityManager em;

	private Query queryOptAct;
	private Query queryPerm;
	private Query queryUpdatePerm;
	private Query queryOAUser;
	private ObjectEM objectEm;

	private List<util.Permission> permissions;

	/**
	 * Default constructor.
	 */
	public PermissionEJB() {

	}

	@PostConstruct
	public void init() {
		queryOptAct = em.createQuery("FROM ReOptionAction optact ORDER BY optact.optionId.optionName, optact.actionId.actionName ");

		queryPerm = em
				.createNativeQuery("SELECT option_action_id FROM re_role_option_action WHERE role_id = ?1");

		queryUpdatePerm = em
				.createNativeQuery("SELECT role_id, option_action_id FROM re_role_option_action WHERE role_id = ?1 AND option_action_id = ?2");

		queryOAUser = em
				.createNativeQuery("SELECT option_id, action_id FROM re_option_action WHERE option_action_id IN (SELECT option_action_id FROM re_role_option_action WHERE role_id in (SELECT role_id FROM re_user_role WHERE user_id = ?1))");
	}

	/**
	 * 
	 * @return List with combination of options and actions.
	 */
	private List<ReOptionAction> getAllOptActs() {
		List<ReOptionAction> optActs = new ArrayList<ReOptionAction>();
		try {
			for (Object object : queryOptAct.getResultList()) {
				ReOptionAction roa = (ReOptionAction) object;
				if(roa.getOptionActionState()!=1){
					optActs.add(roa);
				}				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return optActs;
	}
	
	private List<ReOptionAction> getOptActsByRole(long role) {
		List<ReOptionAction> optActs = new ArrayList<ReOptionAction>();		
		try {
		  TbRole r = em.find(TbRole.class, role);
		  System.out.println("role: "+role);
		  if(r!=null){
			  System.out.println("tipo rol: "+r.getTbTypeRole().getTypeRoleId());
			if(r.getTbTypeRole().getTypeRoleId().equals(2L) || r.getTbTypeRole().getTypeRoleId().equals(3L)){
				for (Object object : queryOptAct.getResultList()) {
					ReOptionAction roa = (ReOptionAction) object;
					System.out.println(roa.getOptionActionState());
					System.out.println(roa.getOptionActionReference().getOptActReferenceId());
					System.out.println(roa.getOptionId().getOptionName() +" - "+roa.getActionId().getActionName());					
					if(roa.getOptionActionState()!=1 && (roa.getOptionActionReference().getOptActReferenceId().equals("C") || roa.getOptionActionReference().getOptActReferenceId().equals("A"))){
						optActs.add(roa);						
					}				
				}
			}else{
				for (Object object : queryOptAct.getResultList()) {
					ReOptionAction roa = (ReOptionAction) object;
					if(roa.getOptionActionState()!=1 && (roa.getOptionActionReference().getOptActReferenceId().equals("U") || roa.getOptionActionReference().getOptActReferenceId().equals("A"))){
						optActs.add(roa);
					}				
				}
			}
			
		  }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return optActs;
	}

	/**
	 * 
	 * @param role
	 * @param optActId
	 * @return True if role is already assigned, otherwise false.
	 */
	private boolean validateRole(long role, long optActId) {
		boolean result = false;
		for (Object object : queryPerm.setParameter(1, role).getResultList()) {
			try {
				long oaId = ((BigDecimal) object).longValue();
				if (optActId == oaId) {
					result = true;
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Permission#getPermission(long)
	 */
	@Override
	public List<util.Permission> getPermissionByRole(long role) {
		permissions = new ArrayList<util.Permission>();
		boolean active = false;
		util.Permission permission = null;
		for (Object object : getOptActsByRole(role)) {
			ReOptionAction optAct = (ReOptionAction) object;
			if (validateRole(role, optAct.getOptionActiontId())) {
				active = true;
			} else {
				active = false;
			}
			permission = new util.Permission(optAct, active);
			permissions.add(permission);
		}
		return permissions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Permission#updatePermission(java.util.List, long)
	 */
	@Override
	public boolean updatePermission(List<util.Permission> permissions, long role) {
		boolean result = false;
		objectEm = new ObjectEM(em);
		Object[] objectList;
		for (util.Permission permission : permissions) {
			try {
				objectList = (Object[]) queryUpdatePerm.setParameter(1, role)
						.setParameter(2, permission.getOptAct().getOptionActiontId())
						.getSingleResult();
				if (!permission.isActive()) {
					
					Query q = em.createQuery("SELECT roa FROM ReRoleOptionAction roa" +
							" WHERE roa.tbRole.roleId = ?1 AND " +
							"roa.reOptionAction.optionActiontId = ?2");
					q.setParameter(1, ((BigDecimal) objectList[0]).longValue());
					q.setParameter(2, ((BigDecimal) objectList[1]).longValue());
					result = objectEm.delete( (ReRoleOptionAction)q.getSingleResult());
				}
			} catch (NoResultException e) {
				if (permission.isActive()) {
					ReRoleOptionAction roa = new ReRoleOptionAction();
					roa.setTbRole(em.find(TbRole.class, role));
					roa.setReOptionAction(permission.getOptAct());
					result = objectEm.create(roa);
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
	 * @see ejb.Permission#getPermissionByUser(long)
	 */
	@Override
	public List<util.Permission> getPermissionByUser(long userId) {
		List<util.Permission> permissions = new ArrayList<util.Permission>();
		Object[] objectList;
		try {
			for (Object object : queryOAUser.setParameter(1, userId)
					.getResultList()) {
				objectList = (Object[]) object;				
				TbOption opt = em.find(TbOption.class, ((BigDecimal) objectList[0]).longValue());
				TbAction action = em.find(TbAction.class, ((BigDecimal) objectList[1]).longValue());
				
				Query q = em
						.createQuery("SELECT roa FROM ReOptionAction roa WHERE roa.actionId = ?1 AND optionId = ?2");
				q.setParameter(1, action).setParameter(2, opt);
				
				ReOptionAction optAct = (ReOptionAction) q.getSingleResult();
				
				util.Permission permission = new util.Permission(optAct, true);
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
	 * @see ejb.Permission#getOpctionAction(long)
	 */
	@Override
	public  List<OptionActions> getOpctionAction(long userId){
		List<OptionActions> list = new ArrayList<OptionActions>();
		
		/**
		 * @author ablasquez
		 */
		 // se Verifica el tipo de usuario para cargar el menu, si es Cliente o Administrador
		String validType=getTypeUser(userId);
		boolean isClient = false;
		boolean hasBanking = true;
		if(validType != null){
			if(validType.equals("C")){
				isClient =  true;
			}
		}
		
		if(isClient==true){
			hasBanking = validateProductBankClient(userId);
		}
		 
		String query ="SELECT oa.option_action_id FROM " +
						    "re_user_role ur " + 
						    "INNER JOIN re_role_option_action roa ON ur.role_id = roa.role_id " + 
						    "INNER JOIN re_option_action oa ON roa.option_action_id = oa.option_action_id " +
						    "INNER JOIN tb_option top ON oa.option_id=top.option_id "+
						    "WHERE oa.OPTION_ACTION_STATE=0 and ur.user_id = " + userId;
		try{
			if(isClient){
				query = query+" and oa.OPTION_ACTION_REFERENCE in ('C','A') ";
			}else{
				query = query+" and oa.OPTION_ACTION_REFERENCE in ('U','A') ";
			}
			Query q = em.createNativeQuery( query + " group by oa.option_action_id,top.option_order  ORDER BY top.option_order");
			
			String temp = "";
			for (Object o : q.getResultList()){
				ReOptionAction oa = em.find(ReOptionAction.class, ((BigDecimal)o).longValue());
				String opt="";
				if(oa.getOptionId().getOptionFatherId()==null){
					opt = oa.getOptionId().getOptionName();
					System.out.println("opcion padre: " + opt);
					if(!opt.equals(temp)){
						OptionActions oas = new OptionActions();
						//se guarda el opci padre
						oas.setOption(opt);
						
						//se guardan los opcis hijos del padre y los accis de los opcis hijos
						Query qq= em.createNativeQuery("select option_id from tb_option where option_father=?1");
						qq.setParameter(1, oa.getOptionId().getOptionId());
						
						Query qu1 = null;
						
						List<OptionActionH> lista= new ArrayList<OptionActionH>();
						for(Object ob: qq.getResultList()){
							System.out.println("opcion hijo: " + ob.toString());
							Long id= Long.parseLong(ob.toString());
							TbOption to= em.find(TbOption.class, id);
							if(to!=null){
								OptionActionH oah = new OptionActionH();
								oah.setOption(to.getOptionName());
								qu1 = em.createNativeQuery(query + " AND oa.option_id = " + Long.parseLong(ob.toString()) + " group by oa.option_action_id,oa.option_action_order ORDER BY oa.option_action_order ");
								List<String> listB = new ArrayList<String>();

								Long tem1 = -1L;
								
								for (Object oj : qu1.getResultList()){
									if(tem1 != ((BigDecimal)oj).longValue()){
										ReOptionAction a = em.find(ReOptionAction.class, ((BigDecimal)oj).longValue());
										listB.add(a.getActionId().getActionName() + "," + a.getOptionId().getOptionId() + "," + a.getBehavior());
										tem1 = ((BigDecimal)oj).longValue();
									}
								}
								System.out.println("actions hijas "+ listB);
								oah.setActions(listB);
								lista.add(oah);
							}
							
						}

						
						//se guardan los accis del padre
						List<String> listA = new ArrayList<String>();
						
						Query qu = em.createNativeQuery( query + " AND oa.option_id = " + oa.getOptionId().getOptionId() + " group by oa.option_action_id,oa.option_action_order ORDER BY oa.option_action_order ");
						
						Long tem = -1L;
						
						for (Object oj : qu.getResultList()){
							if(tem != ((BigDecimal)oj).longValue()){
								ReOptionAction a = em.find(ReOptionAction.class, ((BigDecimal)oj).longValue());
								//if(hasBanking==true){
									listA.add(a.getActionId().getActionName() + "," + oa.getOptionId().getOptionId() + "," + a.getBehavior());
								/*}else{
									if(a.getActionId().getActionId()!=62L){
										listA.add(a.getActionId().getActionName() + "," + oa.getOptionId().getOptionId() + "," + a.getBehavior());
									}
								}	*/
								tem = ((BigDecimal)oj).longValue();
							}
						}
						
						oas.setActions(listA);
						oas.setOptionH(lista);
						list.add(oas);
						temp = opt;
					}	
				
				}
				
									
			}		
		}catch(Exception e){
			System.out.println(" [ Error en PermissionEJB.getOpctionAction ] ");
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<OptionActions> getOpctionActionPrev(String referenceId) {
List<OptionActions> list = new ArrayList<OptionActions>();
		
		String query ="SELECT oa.option_action_id FROM " +
						    "re_user_role ur " + 
						    "INNER JOIN re_role_option_action roa ON ur.role_id = roa.role_id " + 
						    "INNER JOIN re_option_action oa ON roa.option_action_id = oa.option_action_id " +
						    "INNER JOIN tb_option top ON oa.option_id=top.option_id "+
						    "WHERE oa.OPTION_ACTION_REFERENCE in ('A','" + referenceId+"') ";
		System.out.println(query);
		try{
			Query q = em.createNativeQuery( query + " group by oa.option_action_id,top.option_order ORDER BY top.option_order");
			
			String temp = "";
			for (Object o : q.getResultList()){
				ReOptionAction oa = em.find(ReOptionAction.class, ((BigDecimal)o).longValue());
				
				String opt = oa.getOptionId().getOptionName();
				
				if(!opt.equals(temp)){
					OptionActions oas = new OptionActions();
					oas.setOption(opt);
					
					List<String> listA = new ArrayList<String>();
					
					Query qu = em.createNativeQuery( query + " AND oa.option_id = " + oa.getOptionId().getOptionId() + " group by oa.option_action_id,oa.option_action_order ORDER BY oa.option_action_order ");
					
					Long tem = -1L;
					
					for (Object oj : qu.getResultList()){
						if(tem != ((BigDecimal)oj).longValue()){							
							ReOptionAction a = em.find(ReOptionAction.class, ((BigDecimal)oj).longValue());
							//System.out.println(a.getActionId().getActionName() + "," + oa.getOptionId().getOptionId() + "," + a.getBehavior());
							listA.add(a.getActionId().getActionName() + "," + oa.getOptionId().getOptionId() + "," + a.getBehavior());
							tem = ((BigDecimal)oj).longValue();
						}
					}
					
					oas.setActions(listA);
					list.add(oas);
					temp = opt;
				}							
			}		
		}catch(Exception e){
			System.out.println(" [ Error en PermissionEJB.getOpctionActionPrev ] ");
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * @author ablasquez
	 * metodo que permite conocer si el usuario es cliente o administrador
	 */
	
	public String getTypeUser(long userId){
		try {
			Query q = em.createNativeQuery("select count(*) "+
						"from tb_system_user tu "+
						"inner join re_user_role rer on tu.user_id=rer.user_id "+ 
						"inner join tb_role r on rer.role_id=r.role_id "+
						"where r.type_role_id=1 "+
						"and tu.user_id=?1 ");
			q.setParameter(1, userId);
			
			int result = ((BigDecimal)q.getSingleResult()).intValue();
			
			if(result > 0){
				//es Usuario - Administrador
				return "U";
			}else{
				//es Cliente
				return "C";
			}
		} catch(NoResultException n){
			System.out.println("Error en PermissionEJB.getTypeUser");
			return null;
		}
	}
	
	/**
	 * @author ablasquez
	 * metodo que permite saber si un cliente tiene producto bancario registrado o no
	 */
	
	public boolean validateProductBankClient(Long userId){
		try{
		  boolean result = false;
		   Query q = em.createNativeQuery("select count(*) from tb_bank_account where usrs=?1");
		   q.setParameter(1, userId);
		   int counter = ((BigDecimal)q.getSingleResult()).intValue();
		   if(counter > 0){
			   //si tiene productos bancarios registrados
			   result = true;
			}else{
				//no tiene productos bancarios registrados 
				result = false;
			}
		  return result;
		}catch(NoResultException n){
			System.out.println("Error en PermissionEJB.validateProductBankClient");
			return false;
		}
	}
}