package ejb;

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

import constant.EmailProcess;
import constant.LogAction;
import constant.LogReference;
import constant.ProcessTrackDetailType;
import constant.ProcessTrackType;

import jpa.ReUserRole;
import jpa.TbProcessTrackDetailType;
import jpa.TbRole;
import jpa.TbSystemUser;
import jpa.TbTypeRole;
import util.TimeUtil;
import util.UsrRole;
import crud.ObjectEM;
import ejb.email.Outbox;

/**
 * Session Bean implementation class RoleEJB
 */
@Stateless(mappedName = "ejb/Role")
public class RoleEJB implements Role {
	
	@PersistenceContext(unitName = "bo")
	private EntityManager em;
	
	@EJB(mappedName="ejb/Process")
	private Process process;
	
	@EJB(mappedName = "ejb/Log")
	private Log log;
	
	@EJB(mappedName ="ejb/email/Outbox")
	private Outbox outbox;
	
	@EJB(mappedName="ejb/Task")
	private Task task;
	
	private ObjectEM objectEm;

	private List<util.UsrRole> userRoles;

	/**
	 * Default constructor.
	 */
	public RoleEJB() {

	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Role#createRole(java.lang.String)
	 */
	@Override
	public boolean createRole(String roleName, Long typeRoleId){
	long counter_role = -1L;
	long maximum_role = -1L;
	boolean response=false;
	try{
		//cantidad de roles
		Query q1= em.createNativeQuery("SELECT COUNT (role_id) FROM tb_role ");		
		counter_role = Long.parseLong(q1.getSingleResult().toString());		
		
		//roles maximos parametrizados
		//Query q2= em.createNativeQuery("SELECT system_parameter_value FROM tb_system_parameter WHERE system_parameter_id in (36)");
		Query q2 =em.createQuery("select systemParameterValue from TbSystemParameter where systemParameterId in (36L)");
		maximum_role = Long.parseLong(q2.getSingleResult().toString());	
		
			if (counter_role<maximum_role){			
				TbRole role = new jpa.TbRole();
				TbTypeRole typeRole= em.find(TbTypeRole.class, typeRoleId);
				role.setRoleName(roleName.toUpperCase());
				role.setTbTypeRole(typeRole);
				new ObjectEM(em).create(role);
				response=true;
		     }else{
		    	response=false;
			 }
		}catch(NoResultException ex){
			System.out.println(" [ Error en RoleEJB.createRole ] "+response);
			response=false;
		}
		return response;
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public boolean existRoleI(String nameRole){
		boolean res=false;
		try{
			System.out.print("nombre del cargo: " + nameRole);
			Query q= em.createNativeQuery("select role_name from tb_role where upper(role_name)=upper(?1)");
			q.setParameter(1, nameRole);
			List<String> list= new ArrayList<String>();
			list=q.getResultList();
			if(list!=null){
				if(list.size()>0){
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
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean existRoleU(Long roleId, String nameRole){
		boolean res=false;
		try{
			System.out.print("nombre del cargo: " + roleId);
			System.out.print("id del cargo: " + nameRole);
			Query q= em.createNativeQuery("select role_id from tb_role where role_id <>?2 and (upper(role_name)=upper(?1))");
			q.setParameter(1, nameRole);
			q.setParameter(2, roleId);
			List<Object> list= new ArrayList<Object>();
			list=q.getResultList();
			System.out.println("lista" + list);
			if(list!=null){
				if(list.size()>0){
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
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Role#updateRole(long)
	 */
	@Override
	public boolean updateRole(Long roleId, String roleName, Long typeRoleId) {
		TbRole role = em.find(jpa.TbRole.class, roleId);
		TbTypeRole typeRole=em.find(TbTypeRole.class, typeRoleId);	
		role.setRoleName(roleName.toUpperCase());
		role.setTbTypeRole(typeRole);
		return new ObjectEM(em).update(role);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Role#getAllRoles()
	 */
	@Override
	public List<jpa.TbRole> getAllRoles() {
		List<jpa.TbRole> roles = new ArrayList<jpa.TbRole>();
		try {
			Query validateQuery = em.createQuery("SELECT role FROM TbRole role ORDER BY role.roleId, role.roleName");
			for (Object object : validateQuery.getResultList()) {
				roles.add((jpa.TbRole) object);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return roles;
	}
	
	@Override
	public List<jpa.TbRole> getAllRoles2() {
		List<jpa.TbRole> roles = new ArrayList<jpa.TbRole>();
		try {
			Query validateQuery = em.createQuery("SELECT role FROM TbRole role where role.roleId not in (2,3) ORDER BY role.roleId, role.roleName ");
			for (Object object : validateQuery.getResultList()) {
				roles.add((jpa.TbRole) object);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return roles;
	}
	
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Role#validateUserRole(long, long)
	 */
	@Override
	public boolean validateUserRole(long userId, long role) {
		boolean result = false;
		Query queryUsrRole = em.createNativeQuery("SELECT role_id FROM re_user_role " +
				                                   "where USER_ID=?1" +
				                                   " and role_id is not null");
		for (Object object : queryUsrRole.setParameter(1, userId)
				.getResultList()) {
			try {
				if (role == ((BigDecimal) object).longValue()) {
					result = true;
					break;
				}
			} catch (NullPointerException e) {
				System.out.println("validateUserRole--->usuario:="+userId+" role:="+role);
				e.printStackTrace();
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Permission#getUsrRole(long)
	 */
	@Override
	public List<UsrRole> getUsrRole(long userId) {
		userRoles = new ArrayList<util.UsrRole>();
		boolean active = false;
		UsrRole usrRole = null;
		for (Object object : getAllRoles2()) {
			jpa.TbRole role = (jpa.TbRole) object;
			try {
				if (validateUserRole(userId, role.getRoleId())) {
					active = true;
				} else {
					active = false;
				}
			} catch(NullPointerException e){
				System.out.println("usuario:="+userId+" no tiene rol asignado.");
			} 	
			usrRole = new UsrRole(role, active);
			userRoles.add(usrRole);
		}
		return userRoles;
	}
	
	
	@Override
	public List<UsrRole> getCreateUsrRole(long userId) {
		userRoles = new ArrayList<util.UsrRole>();
		boolean active = false;
		UsrRole usrRole = null;
		for (Object object : getAllRoles2()) {
			jpa.TbRole role = (jpa.TbRole) object;
			try {
				active = false;
				/*if (validateUserRole(userId, role.getRoleId())) {
					active = true;
				} else {
					active = false;
				}*/
			} catch(NullPointerException e){
				System.out.println("usuario:="+userId+" no tiene rol asignado.");
			} 	
			usrRole = new UsrRole(role, active);
			userRoles.add(usrRole);
		}
		return userRoles;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Permission#updateUserRole(java.util.List, long)
	 */
	@Override
	public boolean updateUserRole(List<UsrRole> usrRole, long userId) {
		boolean result = false;
		objectEm = new ObjectEM(em);
		Object object = null;
		for (UsrRole userRole : usrRole) {
			try {
				Query queryUsrRoleSingle = em
					.createQuery("SELECT ur FROM ReUserRole ur WHERE ur.tbSystemUser.userId = ?1 AND ur.tbRole.roleId = ?2");
				object = (Object) queryUsrRoleSingle.setParameter(1, userId)
						.setParameter(2, userRole.getRole().getRoleId())
						.getSingleResult();
				result=true;
				if (!userRole.isActive()) {
					Long rolInactive = null;
					ReUserRole usRole = em.find(ReUserRole.class, ((ReUserRole)object).getUserRoleId());
					
					Query queryUsrRoleInactiveSingle = em
					.createQuery("Update ReUserRole ur SET ur.tbRole.roleId = ?1 WHERE ur.tbSystemUser.userId = ?2 " +
							"AND ur.tbRole.roleId = ?3");
					queryUsrRoleInactiveSingle.setParameter(1, rolInactive);
					queryUsrRoleInactiveSingle.setParameter(2, userId);
					queryUsrRoleInactiveSingle.setParameter(3, userRole.getRole().getRoleId());
					queryUsrRoleInactiveSingle.executeUpdate();					
					//result = objectEm.delete(usRole);
					result = objectEm.update(usRole);
					if (!result) break;
				}
			} catch (NoResultException e) {
				if (userRole.isActive()) {
					ReUserRole ur = new ReUserRole();
					ur.setTbRole(userRole.getRole());
					ur.setTbSystemUser(em.find(TbSystemUser.class,userId));	
					result = objectEm.create(ur);
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
	 * @see ejb.Role#checkIfAnyUserRole(java.util.List, long)
	 */
	@Override
	public boolean checkIfAnyUserRole(List<UsrRole> usrRole, long userId) {
		try{
			@SuppressWarnings("unused")
			Object object;
			try{
				Query queryUsrRole = em
					.createNativeQuery("SELECT role_id FROM re_user_role WHERE user_id = ?1");
				object = (queryUsrRole.setMaxResults(1).setParameter(1, userId).getSingleResult());
				return true;
			}catch (NoResultException  e) {
				for (UsrRole userRole : usrRole) {
					if (userRole.isActive()) {
						return true;
					}
				}
			}
		}catch (Exception e) {
			System.out.println(" [ Error en RoleEJB.checkIfAnyOptAct ] ");
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ejb.Role#createUserRole(java.lang.Long, java.lang.String)
	 */
	@Override
	public boolean createUserRole(Long userId, String roleName) {
		try {
			Query q = em
					.createQuery("SELECT ro FROM TbRole ro WHERE ro.roleName = ?1");
			q.setParameter(1, roleName);
			TbRole role = (TbRole) q.getSingleResult();
			
			if (role != null) {
				ReUserRole reUserRole = new ReUserRole();
				reUserRole.setTbRole(role);
				reUserRole.setTbSystemUser(em.find(TbSystemUser.class, userId));
				em.persist(reUserRole);
				em.flush();
				return true;
			}
			
		} catch (NoResultException e) {
			System.out
					.println(" [ Error en RoleEJB.createUserRole: No se encontró el Rol con el nombre:"
							+ roleName + "  ] ");
		} catch (NonUniqueResultException nre) {
			System.out
					.println(" [ Error en RoleEJB.createUserRole: más de un resultado para el rol: "
							+ roleName + " ]");
		} catch (Exception e) {
			System.out.println(" [ Error en RoleEJB.createUserRole ] ");
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public boolean createUserRole(Long userId, long roleId) {
		try {
			Query q = em
					.createQuery("SELECT ro FROM TbRole ro WHERE ro.roleId = ?1");
			q.setParameter(1, roleId);
			TbRole role = (TbRole) q.getSingleResult();
			
			if (role != null) {
				ReUserRole reUserRole = new ReUserRole();
				reUserRole.setTbRole(role);
				reUserRole.setTbSystemUser(em.find(TbSystemUser.class, userId));
				em.persist(reUserRole);
				em.flush();
				return true;
			}
			
		} catch (NoResultException e) {
			System.out
					.println(" [ Error en RoleEJB.createUserRole: No se encontró el Rol con el Id:"
							+ roleId + "  ] ");
		} catch (NonUniqueResultException nre) {
			System.out
					.println(" [ Error en RoleEJB.createUserRole: más de un resultado para el rol: "
							+ roleId + " ]");
		} catch (Exception e) {
			System.out.println(" [ Error en RoleEJB.createUserRole ] ");
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean isClient(Long userId){
		boolean res=false;
		BigDecimal cont=null;
		try{
			Query q= em.createNativeQuery("select count(*) from re_user_role where user_id=?1 and role_id in (2,3)");
			q.setParameter(1, userId);
			long i=-1;
			cont=(BigDecimal) q.getSingleResult();
			System.out.println("cont en isClient" + cont);
			
			if(cont!=null){
				i=cont.longValue();
				if(i>0){
					res=true;
					System.out.println("Si es cliente");
				}
				else{
					res=false;
					System.out.println("No es cliente");
				}
			}
			else{
				res=false;
			}
		}catch(NoResultException ex){
			res=false;
			System.out.println("No es cliente");
		}
		System.out.println("res " +res);
		return res;
	}


	@Override
	public List<TbTypeRole> getTypeRoleList() {
		List<TbTypeRole> list = new ArrayList<TbTypeRole>();
		try{
		  Query q = em.createQuery("SELECT tr FROM TbTypeRole tr WHERE typeRoleState=1 ORDER BY typeRoleId ");
		  for (Object obj : q.getResultList()) {
  			  list.add((TbTypeRole) obj);
		  }
		} catch (Exception e) {			
			e.printStackTrace();
		}
		return list;
	}


	/**
	 * Método creado para listar los tipos de rol.
	 * @return return lista tipo de roles.
	 * @author psanchez
	 */
	@Override
	public List<TbRole> getTypeRoleClientList() {
		List<TbRole> list = new ArrayList<TbRole>();
		try{
		  Query q = em.createQuery("Select tr From TbRole tr where tbTypeRole=3 ");
		  for (Object obj : q.getResultList()) {
  			  list.add((TbRole) obj);
		  }
		} catch (Exception e) {			
			e.printStackTrace();
		}
		return list;
	}


	@Override
	public boolean createUserRoleMaster(long userId, long roleId, String ip, long creationUser) {
		try {
			Query query = em.createQuery("SELECT ro FROM TbRole ro WHERE ro.roleId = ?1");
			    query.setParameter(1, roleId);
			    TbRole role = (TbRole) query.getSingleResult();
			    
			if (role != null) {
					ReUserRole reUserRole = new ReUserRole();
					reUserRole.setTbRole(role);
					reUserRole.setTbSystemUser(em.find(TbSystemUser.class, userId));
				
				objectEm = new ObjectEM(em);	
				if(objectEm.create(reUserRole)){
					// Creating a message to track process of the client.
					String message = "Se ha Creado el Usuario en el Sistema.";

					// Creating the process track, relation with the client
					Long proId = process.createProcessTrack(ProcessTrackType.CLIENT, userId, ip, creationUser);
					Long proCliId = process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, userId, ip, creationUser);
					
					// If the process track was created.
					if (proId != null) {// Creating the detail of the process.	
						// Indicating that the potential client has been created. Process detail type = 1
						Long detailId = process.createProcessDetail(proId, ProcessTrackDetailType.CLIENT_CREATION, message, creationUser, ip, 0, 
								" No Se ha podido crear el detalle del proceso ID: " + proId + ", Tipo detalle: 100.",null,null,null,null);
						TbProcessTrackDetailType dt = em.find(TbProcessTrackDetailType.class, ProcessTrackDetailType.CLIENT_CREATION.getId());
						
						// Task Creation.
						task.createTask(detailId, 0, new Timestamp(System.currentTimeMillis()), 
								TimeUtil.calculateAds(dt.getAdsTime()), dt.getDetailTypePriority(), 
								message, 
								dt.getTbTaskType().getTaskTypeId(), creationUser, ip, null);

						
						
						if (proCliId != null) {
							process.createProcessDetail(proCliId,
									ProcessTrackDetailType.MY_CLIENT_CREATION, "Ha sido Creado en el sistema FacilPass.", creationUser, ip, 1,
									" No Se ha podido crear el detalle del proceso ID: "+ proId + ", Tipo detalle: 600.",null,null,null,null);
						}				
					}
				}else {
					log.insertLog("Error al crear el usuario Código: " + userId + "- "
					+ userId, LogReference.CLIENT, LogAction.CREATEFAIL, ip, creationUser);
				}
			}	
		} catch (NoResultException e) {
			System.out
					.println(" [ Error en RoleEJB.createUserRoleMaster: No se encontró el Rol con el Id:"
							+ roleId + "  ] ");
		} catch (NonUniqueResultException nre) {
			System.out
					.println(" [ Error en RoleEJB.createUserRoleMaster: más de un resultado para el rol: "
							+ roleId + " ]");
		} catch (Exception e) {
			System.out.println(" [ Error en RoleEJB.createUserRoleMaster ] ");
			e.printStackTrace();
		}
		return false;
	}
	
    @Override
	public BigDecimal getCountRoles() {
    	BigDecimal con=null ;
	
		try {
			Query q = em.createNativeQuery("select count(*) from tb_role ");
            con= (BigDecimal) q.getSingleResult();
            System.out.println("cantidad de roles creados: "  + con);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}
}