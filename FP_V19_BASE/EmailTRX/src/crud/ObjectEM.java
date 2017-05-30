/**
 * 
 */
package crud;

import java.io.Serializable;

import javax.persistence.EntityManager;

/**
 * Persistence class that abstracts the process of creating, updating and
 * deleting objects.
 * 
 * @author Frances Zucchet
 */
public class ObjectEM {

	private EntityManager em;

	public ObjectEM(EntityManager em) {
		this.em = em;
	}

	/**
	 * 
	 * @param object
	 * @return True if the operation was successful, otherwise false.
	 */
	public boolean create(Serializable object,EntityManager em) {
		boolean result = false;
		try {
			em.getTransaction().begin();
			em.persist(object);
			em.getTransaction().commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error ObjectEMCreate:"+e.getMessage());
		} 
		return result;
	}

	/**
	 * 
	 * @param object
	 * @return True if the operation was successful, otherwise false.
	 */
	public boolean update(Serializable object) {
		boolean result = false;
		try {
			em.getTransaction().begin();
			em.merge(object);
			em.getTransaction().commit();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 
	 * @param object
	 * @return True if the operation was successful, otherwise false.
	 */
	public boolean delete(Serializable object) {
		boolean result = false;
		try {
			em.remove(object);
			em.flush();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
