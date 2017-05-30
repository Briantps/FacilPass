package ejb;
import java.util.List;

import javax.ejb.Remote;

import jpa.TbBrand;

/**
 * Controls all related queries to brand table in database
 * @author Mauricio Gil
 */
@Remote
public interface BrandManager {

	/**
	 * Retrieves the list of brands available in the database
	 * 
	 * @return Brand list
	 */
	public List<TbBrand> getBrands();

	/**
	 * Retrieves the list of brands available in the database specifying a row
	 * range
	 * 
	 * @param first
	 *            Starting row of the range
	 * @param count
	 *            Number of rows in the range
	 * @return Brand list
	 */
	public List<TbBrand> getBrands(Long first, Long count);

	/**
	 * Gets the brand of a vehicle.
	 * 
	 * @param id
	 *            Identification
	 * @return Brand of the specified id
	 */
	public TbBrand getBrand(Long id);

	/**
	 * Determines if there is a particular brand in the database
	 * 
	 * @param id
	 *            Identification
	 * @return True if the specified brand id exists, false otherwise
	 */
	public boolean hasBrandById(Long id);

	/**
	 * Gets the count of all brands available in the database
	 * 
	 * @return Brand count
	 */
	public int getAllBrandsCount();
	
	/**
	 * Sets the brand of a vehicle
	 * 
	 * @param brand
	 *            Vehicle's brand
	 */
	public void setBrand(TbBrand brand);

	/**
	 * Gets a set of brands
	 * 
	 * @param name Part of the desired brand name
	 * @return List of brands
	 */
	public List<TbBrand> getBrandsByName(String name);
	public List<String> getBrandsByName2(String name);
	
	public Long insertBrand(TbBrand brand, String ip, Long creationUser);
	
	public boolean editBrand(TbBrand brand, String ip, Long creationUser);

	public TbBrand getBrandByName(String newBrand1);
	
	public Long getBrandIdByName(String newBrand1);

}
