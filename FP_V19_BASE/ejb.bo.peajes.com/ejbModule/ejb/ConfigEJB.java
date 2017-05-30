package ejb;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import constant.LogAction;
import constant.LogReference;

import crud.ObjectEM;
import jpa.TbSystemParameter;

@Stateless(mappedName="ejb/Config")
public class ConfigEJB implements Config{
	
	@PersistenceContext(unitName="bo")
	EntityManager em;
	
	private ObjectEM objectEM;
	
	@EJB(mappedName="ejb/Log")
	private Log log;
	

	@Override
	public List<TbSystemParameter> getParameters() {
		List<TbSystemParameter> lis= new ArrayList<TbSystemParameter>();
		Query q=em.createQuery("select t from TbSystemParameter t  where t.systemParameterId not in (34) order by t.systemParameterId");
		List<?> list=q.getResultList();
		
		for(Object o: list){
			TbSystemParameter tsp=(TbSystemParameter) o;
			lis.add(tsp);
		}
		
		return lis;
	}

	@Override
	public boolean updateParameter(Long parameterId, String valueParameter, String nameParameter, Long userId, String ip) {
		boolean res=false;
		objectEM= new ObjectEM(em);
		try{
			TbSystemParameter tb= em.find(TbSystemParameter.class, parameterId);
			if(tb!=null){
			   tb.setSystemParameterValue(valueParameter);
			   res=objectEM.update(tb);
			   if(res){
				   System.out.println("valueParameter.length(): " + valueParameter.length());
				   if(valueParameter.length() < 3000){
					   log.insertLog("El usuario con id: " + userId + " cambió el valor del parámetro del sistema con ID: " + parameterId + " por el valor: " +valueParameter, LogReference.CHANGESYSTEMPARAMETER, LogAction.UPDATE,
								ip, userId);
				   }else{
					   log.insertLog("El usuario con id: " + userId + " cambió el valor del parámetro del sistema con ID: " + parameterId + " por el valor: " +valueParameter.substring(0, 3900), LogReference.CHANGESYSTEMPARAMETER, LogAction.UPDATE,
								ip, userId);
				   }
				   
			   }
			}
			else{
				res=false;
			}
		}catch(Exception ex){
			res=false;
		}
		return res;
	}
	
	@Override
	public String getParameter(Long idParameter){
		try{
			TbSystemParameter tsp = em.find(TbSystemParameter.class,idParameter);
			return tsp.getSystemParameterValue();
		}catch(NoResultException n){
			System.out.println("Error en SystemParametersEJB.getParameterValueById");
			n.printStackTrace();
			return null;
		} 
	}

}
