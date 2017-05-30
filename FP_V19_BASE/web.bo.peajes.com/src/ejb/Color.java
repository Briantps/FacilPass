package ejb;

import java.util.List;

import javax.ejb.Remote;

import jpa.TbColor;

/**
 * Defines a set of methods to perform control on colors in table TB_COLOR
 * @author Mauricio Gil
 */
@Remote
public interface Color {

	/**
	 * Gets a color object from database
	 * @param id Color identifier
	 * @return Color object
	 */
	public TbColor getColor(long id);

	/**
	 * Removes a color from database
	 * @param color Color object
	 */
	public void removeColor(TbColor color);

	/**
	 * Inserts a new color object in database
	 * @param color Color object
	 */
	public void addColor(TbColor color);

	/**
	 * Retrieves all available color objects
	 * @return List of colors
	 */
	public List<TbColor> getColors();

	/**
	 * Changes a color in database
	 * @param color Color object
	 */
	public void updateColor(TbColor color);

}