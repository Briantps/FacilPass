package ejb;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jpa.TbSystemParameter;

@Stateless(mappedName="ejb/SystemParameters")
public class SystemParametersEJB implements SystemParameters {
	
	@PersistenceContext(unitName = "bo")
	private EntityManager em;
	
	@Override
	public String getParameterValueById(Long idParameter) {
		try{
			TbSystemParameter tsp = em.find(TbSystemParameter.class,idParameter);
			return tsp.getSystemParameterValue();
		}catch(NoResultException n){
			System.out.println("Error en SystemParametersEJB.getParameterValueById");
			n.printStackTrace();
			return null;
		} 
	}
	
	
	@Override
	public String validateVigParameter(String valueParameter){
		String msj ="";
		TbSystemParameter tsp1 = em.find(TbSystemParameter.class, 21L);
		String valueP1 = tsp1.getSystemParameterValue();

		if (Long.parseLong(valueP1)<=Long.parseLong(valueParameter)){
			msj = "El valor de éste parámetro debe ser inferior al parámetro 21 el cual es de " + valueP1 + " días";
		}
	return msj;
	}
	
	@Override
	public String minimumResponses(int valueParameter){
		String msj ="";
		if (valueParameter<0L || valueParameter>4L){
			msj = "El valor de éste parámetro debe ser mínimo 0 y máximo 4 preguntas válidas de seguridad.";
		}
	return msj;
	}
	
	@Override
	public List<TbSystemParameter> getListPath() {
		List<TbSystemParameter> list = new ArrayList<TbSystemParameter>();
		try {
			Query q = 
				em.createQuery("SELECT sp FROM TbSystemParameter sp " +
					           "WHERE systemParameterId IN (28,29) " +
					           "ORDER BY systemParameterName ASC ");
			for (Object obj : q.getResultList()) {
				list.add((TbSystemParameter) obj);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en SystemParametersEJB.getListPath. ] ");
			e.printStackTrace();
		}
		return list;
	}
	
	@Override
	public String getParameterName(String typePath) {
		try{
			Query q= em.createQuery("SELECT p FROM TbSystemParameter p WHERE p.systemParameterName =?1 ");
					                q.setParameter(1, typePath).getSingleResult();                
			TbSystemParameter tsp = (TbSystemParameter) q.getSingleResult();
			return tsp.getSystemParameterValue();
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println(" [  Error en SystemParametersEJB.getParameterName. ] ");
			return null;
		}
	}
		
}
