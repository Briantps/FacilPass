package ejb;

import java.util.List;

import javax.ejb.Remote;

import jpa.TbCategory;

/**
 * Defines a set of methods to perform queries to TB_CATEGORY
 * @author Mauricio Gil
 */
@Remote
public interface Category {

	/**
	 * Gets a category from database
	 * @param id Category identifier
	 * @return Category object
	 */
	public TbCategory getCategory(long id);

	/**
	 * Removes from database a category
	 * @param cat Category object
	 */
	public void removeCategory(TbCategory cat);

	/**
	 * Persists an object in database
	 * @param cat Category object
	 */
	public void addCategory(TbCategory cat);

	/**
	 * Retrieves all available categories
	 * @return Category list
	 */
	public List<TbCategory> getCategories();

	/**
	 * Changes a category in database
	 * @param cat Category object
	 */
	public void updateCategory(TbCategory cat);

}