package ejb;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jpa.TbAccount;
import jpa.TbFrequency;

@Stateless(mappedName = "ejb/AutomaticRecharge")
public class AutomaticRechargeEJB implements AutomaticRecharge {
   
	@PersistenceContext(unitName = "bo") 
	EntityManager em;

	public TbAccount getAccountbyCode(Long accountId) {
	  try {
		TbAccount cta = em.find(TbAccount.class, accountId);		
		return cta;
	  } catch (NullPointerException e){
		  e.printStackTrace();
		  return null;
	  } catch (NoResultException e){
		  e.printStackTrace();
		  return null;
	  }
	}

	public List<TbFrequency> getListFrequency() {
		List<TbFrequency> list = new ArrayList<TbFrequency>();
		try{
		  Query q = em.createQuery("Select fq From TbFrequency");
		  for (Object obj : q.getResultList()) {
  			  list.add((TbFrequency) obj);
		  }
		} catch (Exception e) {			
			e.printStackTrace();
		}
		return list;
	}

}
