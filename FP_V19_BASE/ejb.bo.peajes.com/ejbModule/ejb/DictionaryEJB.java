package ejb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import crud.ObjectEM;

import util.Encryptor;

import jpa.TbPasswordDictionary;
import jpa.TbSystemUser;

@Stateless(mappedName="ejb/Dictionary")
public class DictionaryEJB implements Dictionary{
	
	@PersistenceContext(unitName = "bo") 
	EntityManager em;
	
	private ObjectEM objectEM;
	
	public DictionaryEJB(){
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TbPasswordDictionary> getPasswords() {
		List<TbPasswordDictionary> lista= new ArrayList<TbPasswordDictionary>();
		Query q=em.createQuery("select p from TbPasswordDictionary p order by p.passwordNumber");
		List<Object> lis=q.getResultList();
		
		for(Object o: lis){
			TbPasswordDictionary t= (TbPasswordDictionary) o;
			lista.add(t);
		}
		return lista;
	}


	@Override
	public boolean savePassword(String newPassword, TbSystemUser user){
		boolean res=false;
		try{
				TbPasswordDictionary tpd= new TbPasswordDictionary();
				tpd.setDateCreation(new Date());
				tpd.setPasswordNumber(newPassword);
				tpd.setUserId(user);
				objectEM= new ObjectEM(em);
				res=objectEM.create(tpd);
		
			
		}catch(Exception ex){
			res=false;
			ex.printStackTrace();
		}
		return res;
	}


	@Override
	public boolean deletePassword(Long passwordId) {
		System.out.println("passwordId" + passwordId);
		boolean res=false;
		objectEM= new ObjectEM(em);
		try{
		    TbPasswordDictionary tb= em.find(TbPasswordDictionary.class, passwordId);
		    if(tb!=null){
		    	res=objectEM.delete(tb);
		    }
		    else{
		    	res=false;
		    }
		}catch(Exception ex){
			res=false;
			ex.printStackTrace();
		}
	
		return res;
	}


	@Override
	public boolean updatePassword(Long passwordId, String newPassword2) {
		boolean res=false;
		objectEM= new ObjectEM(em);
		try{
			
				TbPasswordDictionary tb= em.find(TbPasswordDictionary.class, passwordId);
				if(tb!=null){
				   tb.setPasswordNumber(newPassword2);
				   res=objectEM.update(tb);
				}
				else{
					res=false;
				}	

		}catch(Exception ex){
			res=false;
			ex.printStackTrace();
		}
		return res;
	}
	
	@SuppressWarnings("unchecked")
	public String validateSize(String newPassword){
		boolean res=false;
		String message="OK";
		try{
			//Query q=em.createNativeQuery("select system_parameter_value from tb_system_parameter where system_parameter_id in (13,14)");
			Query q =em.createQuery("select p.systemParameterValue from TbSystemParameter p where p.systemParameterId in (13L,14L)");

			List<Object> list= (List<Object>) q.getResultList();
			
			Long min= list.get(0)!=null?Long.parseLong(list.get(0).toString()):3L;
			Long max= list.get(1)!=null?Long.parseLong(list.get(1).toString()):12L;
			
			System.out.println("min: " +min);
		    System.out.println("max: "+max);
			if(newPassword.length()<min || newPassword.length()>max){
				res=true;
				message="La longitud de la contraseña debe ser mínimo de " + min +" caracteres y máximo de " +max;
			}
			else{
				res=false;
				message="OK";
			}
			
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return message;

	}

}
