package ejb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jpa.TbBrand;
import constant.LogAction;
import constant.LogReference;
import crud.ObjectEM;

/**
 * Session Bean implementation of BrandManager
 */
@Stateless(mappedName = "ejb/BrandManager")
public class BrandManagerEJB implements BrandManager {

	@PersistenceContext(unitName = "bo")
	EntityManager em;
	private ObjectEM emObj;
	private Query allBrands;
	private Query brandsByRange;
	private Query brandsByName;
	private Query brandsByName2;
	
	
	@EJB(mappedName = "ejb/Log")
	private Log log;

	/**
	 * Default constructor.
	 */
	public BrandManagerEJB() {
	}

	/**
	 * Performs initialization to internal queries
	 */
	@PostConstruct
	public void init() {
		allBrands = em.createQuery("FROM TbBrand b");
		brandsByRange = em
				.createQuery("SELECT b from TbBrand b where rownum >= :first and rownum <= :last");
		brandsByName = em.createNativeQuery(
				"select * from tb_brand where brand_name like :name or lower(brand_name) like :name",
				TbBrand.class);
		brandsByName2 = em.createNativeQuery(
				"select brand_name from tb_brand where brand_name like :name or lower(brand_name) like :name");
	
	}

	/**
	 * Retrieves the list of brands available in the database
	 * 
	 * @return Brand list
	 */
	@Override
	public List<TbBrand> getBrands() {
		List<TbBrand> list = new ArrayList<TbBrand>();
		for(Object obj : allBrands.getResultList()){
			TbBrand brand = (TbBrand)obj;
			list.add(brand);
		}
		return list;
	}

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
	@Override
	public List<TbBrand> getBrands(Long first, Long count) {
		brandsByRange.setParameter("first", first);
		brandsByRange.setParameter("last", first + count);
		
		List<TbBrand> list = new ArrayList<TbBrand>();
		for(Object obj : brandsByRange.getResultList()){
			TbBrand brand = (TbBrand)obj;
			list.add(brand);
		}
		return list;
	}

	/**
	 * Gets the brand of a vehicle.
	 * 
	 * @param id
	 *            Identification
	 * @return Brand of the specified id
	 */
	@Override
	public TbBrand getBrand(Long id) {
		TbBrand brand = em.find(TbBrand.class, id);
		return brand;
	}

	/**
	 * Gets a set of brands
	 * 
	 * @param name Part of the desired brand name
	 * @return List of brands
	 */
	@SuppressWarnings("unchecked")
	public List<TbBrand> getBrandsByName(String name) {
		brandsByName.setParameter("name", name + "%");
		return (List<TbBrand>)brandsByName.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getBrandsByName2(String name) {
		brandsByName2.setParameter("name", name + "%");
		return (List<String>)brandsByName2.getResultList();
	}

	/**
	 * Determines if there is a particular brand in the database
	 * 
	 * @param id
	 *            Identification
	 * @return True if the specified brand id exists, false otherwise
	 */
	@Override
	public boolean hasBrandById(Long id) {
		return em.find(TbBrand.class, id) != null;
	}

	/**
	 * Gets the count of all brands available in the database
	 * 
	 * @return Brand count
	 */
	@Override
	public int getAllBrandsCount() {
		List<TbBrand> brands = getBrands();
		return brands.size();
	}

	/**
	 * Sets the brand of a vehicle
	 * 
	 * @param brand
	 *            Vehicle's brand
	 */
	@Override
	public void setBrand(TbBrand brand) {
		new ObjectEM(em).update(brand);
	}
	
	@Override
	public Long insertBrand(TbBrand brand, String ip, Long creationUser) {
		try {
			Query q = em
					.createQuery("SELECT brand FROM TbBrand brand WHERE brand.brandName = ?1");
			q.setParameter(1, brand.getBrandName().toUpperCase());
			q.getSingleResult();
			
			return -1L;
		} catch (NoResultException e) {
			try{
			// creating the brand.
			TbBrand newBrand = new TbBrand();
			newBrand.setBrandId(brand.getBrandId());
			newBrand.setBrandName(brand.getBrandName().toUpperCase());
			
			emObj = new ObjectEM(em);
			
			if(emObj.create(newBrand)){
				log.insertLog("Creación de Marca | Se ha creado la marca ID: " + newBrand.getBrandId() + ".",
						LogReference.BRAND, LogAction.CREATE, ip, creationUser);
				return 0L;
			} else {
				log.insertLog("Creación de Marca | No se pudo crear la marca: " + brand + ".",
						LogReference.BRAND, LogAction.CREATEFAIL, ip, creationUser);
			}
			}catch(Exception ex) {
				System.out.println(" [ Error en BrandEJB.insertBrand. ] ");
				ex.printStackTrace();			
			}
		} catch (Exception e) {
			System.out.println(" [ Error en BrandEJB.insertBrand. ] ");
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public boolean editBrand(TbBrand brand, String ip, Long creationUser) {
		try {	
			TbBrand edBrand = em.find(TbBrand.class, brand.getBrandId());
			String old = edBrand.getBrandName();
			edBrand.setBrandName(brand.getBrandName());
			
			//update
			emObj = new ObjectEM(em);
			
			if(emObj.update(edBrand)){
				log.insertLog("Editar Marca | Se ha actualizado la marca ID: " + edBrand.getBrandId()+ ". Antes: " + old,
							LogReference.BRAND, LogAction.UPDATE, ip, creationUser);
				return true;
			} else {
				log.insertLog("Editar Marca | No Se ha podido actualizar ID: " + edBrand.getBrandId() + " a : "+ brand.getBrandName() + ".",
						LogReference.BRAND, LogAction.UPDATEFAIL, ip, creationUser);
			}
		} catch (Exception e) {
			System.out.println(" [ Error en BrandEJB.editBrand. ] ");
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public TbBrand getBrandByName(String newBrand1) {
		try{
			Query q= em.createQuery("select t from TbBrand t where t.brandName=?1");
			q.setParameter(1, newBrand1);
			
			TbBrand tb=(TbBrand) q.getSingleResult();
			return tb;
		}catch(NoResultException ex){
			return null;
		}
		
	}

	@Override
	public Long getBrandIdByName(String newBrand1) {
		try{
			Query q= em.createNativeQuery("select BRAND_ID from TB_BRAND where BRAND_NAME=?1");
			q.setParameter(1, newBrand1);
			BigDecimal resul=(BigDecimal) q.getSingleResult();
			return resul.longValue();
		}catch(NoResultException ex){
			return 0L;
		}
	}	
	
}
