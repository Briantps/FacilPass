package ejb;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import crud.ObjectEM;

import jpa.TbColor;

/**
 * Bean implementation of Color
 * @author Mauricio Gil
 */
@Stateless(mappedName = "ejb/Color")
public class ColorEJB implements Color {
	
	@PersistenceContext(unitName = "bo") EntityManager em;
	private Query allColors;
	
	public ColorEJB(){
	}
	
	/**
	 * Performs initialization to internal queries
	 */
	@PostConstruct
	public void init(){
		allColors = em.createQuery("FROM TbColor x");
	}
	
	/**
	 * Gets a color object from database
	 * @param id Color identifier
	 * @return Color object
	 */
	public TbColor getColor(long id){
		TbColor color = em.find(TbColor.class, id);
		return color;
	}
	
	/**
	 * Removes a color from database
	 * @param color Color object
	 */
	public void removeColor(TbColor color){
		new ObjectEM(em).delete(color);
	}
	
	/**
	 * Inserts a new color object in database
	 * @param color Color object
	 */
	public void addColor(TbColor color){
		new ObjectEM(em).create(color);
	}
	
	/**
	 * Changes a color in database
	 * @param color Color object
	 */
	public void updateColor(TbColor color){
		new ObjectEM(em).update(color);
	}
	
	/**
	 * Retrieves all available color objects
	 * @return List of colors
	 */
	@SuppressWarnings("unchecked")
	public List<TbColor> getColors(){
		return allColors.getResultList();
	}
}
