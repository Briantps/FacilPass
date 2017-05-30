package ejb;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import crud.ObjectEM;

import jpa.TbCategory;

/**
 * Bean implementation of Category
 * @author Mauricio Gil
 */
@Stateless(mappedName = "ejb/Category")
public class CategoryEJB implements Category {

	@PersistenceContext(unitName = "bo") EntityManager em;
	private Query allCategories;
	
	public CategoryEJB(){
	}
	
	/**
	 * Performs initialization to internal queries
	 */
	@PostConstruct
	public void init(){
		allCategories = em.createQuery("FROM TbCategory x");
	}
	
	/**
	 * Gets a category from database
	 * @param id Category identifier
	 * @return Category object
	 */
	public TbCategory getCategory(long id){
		TbCategory cat = em.find(TbCategory.class, id);
		return cat;
	}
	
	/**
	 * Removes from database a category
	 * @param cat Category object
	 */
	public void removeCategory(TbCategory cat){
		new ObjectEM(em).delete(cat);
	}
	
	/**
	 * Persists an object in database
	 * @param cat Category object
	 */
	public void addCategory(TbCategory cat){
		new ObjectEM(em).create(cat);
	}
	
	/**
	 * Changes a category in database
	 * @param cat Category object
	 */
	public void updateCategory(TbCategory cat){
		new ObjectEM(em).update(cat);
	}
	
	/**
	 * Retrieves all available categories
	 * @return Category list
	 */
	@SuppressWarnings("unchecked")
	public List<TbCategory> getCategories(){
		return allCategories.getResultList();
	}
}
