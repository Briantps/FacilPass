package mBeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import sessionVar.SessionUtil;
import jpa.TbBrand;
import ejb.BrandManager;

/**
 * Allows the administration of vehicle brands in BackOffice
 * 
 * 
 * @author Mauricio Gil
 *
 */
public class AdminBrand implements Serializable{

	private static final long serialVersionUID = 132513249667519425L;

	private Context context;
	
	private List<jpa.TbBrand> listBrand;
	
	private Long brandId;
	
	private String brandName;
		
	private BrandManager bm;

	private int currentRow;

	private TbBrand currentItem;
	
	private TbBrand newItem;
	
	private Set<Integer> keys = new HashSet<Integer>();
	
	private boolean showModal;
	
	private String msg;	

	/**
	 * Constructor. Initializes the initial context and the EJB BrandManager.
	 */
	public AdminBrand(){
		try {
			context = new InitialContext();
			bm = (BrandManager)context.lookup("ejb/BrandManager");
			newItem = new TbBrand();
			setListBrand(new ArrayList<TbBrand>());
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	public String hideModal() {
		
		this.setMsg("");
		this.setShowModal(false);
		return "success";
	}	
	
	/**
	 * Retrieves the row and brand from the brand table where has ocurred a
	 * contextual menu event.
	 *  
	 * @param event Event object
	 */
	public void fetchCurrentRow(ActionEvent event){
		String brId=(FacesContext.getCurrentInstance().
				getExternalContext().getRequestParameterMap().get("brandId"));
		currentRow = Integer.parseInt(FacesContext.getCurrentInstance().
				getExternalContext().getRequestParameterMap().get("row"));
		for (TbBrand brand : listBrand) {
			if (brand.getBrandId().toString().equals(brId)){
				currentItem=brand;
				break;
			}
		}
	}
	
	public List<TbBrand> autocomplete(Object suggest){
		String pref = (String) suggest;
		ArrayList<TbBrand> result = new ArrayList<TbBrand>();	
	
		Iterator<TbBrand> iterator =  bm.getBrandsByName((String)suggest).iterator();
		while (iterator.hasNext()) {
			TbBrand elem = ((TbBrand) iterator.next());
			if ((elem != null && elem.getBrandName().toLowerCase().indexOf(pref.toLowerCase()) == 0)
					|| "".equals(pref)) {
				result.add(elem);
			}
		}
		return result;
	}
	
	public List<String> autocomplete2(Object suggest){
		String pref = (String) suggest;
		ArrayList<String> result = new ArrayList<String>();	
	
		Iterator<String> iterator =  bm.getBrandsByName2((String)suggest).iterator();
		while (iterator.hasNext()) {
			String elem = ((String) iterator.next());
			if ((elem != null && elem.toLowerCase().indexOf(pref.toLowerCase()) == 0)
					|| "".equals(pref)) {
				result.add(elem);
			}
		}
		return result;
	}
	
	public List<SelectItem> getSelectListBrand(){
		List<SelectItem> list = new ArrayList<SelectItem>();
		
		for (jpa.TbBrand brand : bm.getBrands()) {
			list.add(new SelectItem(brand.getBrandId(), brand.getBrandName()));
		}
		
		return list;
	}
	
	public String storeNew(){
		if(newItem.getBrandName().equals(null) || newItem.getBrandName().equals("") || !newItem.getBrandName().matches(".*\\w.*"))
		{
			this.setMsg("La marca es requerida.");
			setShowModal(true);
		}
		else if(newItem.getBrandName().length() < 2)
		{	
			setMsg("La longitud de la marca no es válida.");
			setShowModal(true);
		}
		else
		{
			Long result = bm.insertBrand(newItem, SessionUtil.ip(), SessionUtil.sessionUser().getUserId());
			if (result != null) {
				if(result != -1L){
					setMsg("La marca ha sido creada con éxito.");
					setListBrand(null);					
				} else {
					setMsg("Ya hay una marca con ese nombre. Verifique.");
				}
			} else {
				setMsg("Error en Transacción.");
			}
			setShowModal(true);
			//setBrand(newItem);
		}
		newItem = new TbBrand();
		return null;
	}
	
	/**
	 * Stores changes done to the selected brand
	 */
	public String store() {
		if(currentItem.getBrandName().equals(null) || currentItem.getBrandName().equals("") || !currentItem.getBrandName().matches(".*\\w.*"))
		{
			this.setMsg("La marca es requerida.");
			setShowModal(true);
		}
		else if(currentItem.getBrandName().length() < 2)
		{	
			setMsg("La longitud de la marca no es válida.");
			setShowModal(true);
		}
		else if(currentItem.getBrandName()!=null && currentItem.getBrandId()!=null){
			for(TbBrand b : listBrand){
				if(b!=null){
					if(b.getBrandName()!=null && b.getBrandName().equals(currentItem.getBrandName().toUpperCase()) && b.getBrandId().longValue()!=currentItem.getBrandId().longValue()){
						setMsg("Ya existe una marca con ese nombre, Verifique.");
						setShowModal(true);
						return null;
					}
				}
			}
			
			if (bm.editBrand(currentItem, SessionUtil.ip(),	SessionUtil.sessionUser().getUserId())) {
				setMsg("La marca ha sido modificada con éxito.");
				setListBrand(null);
			} else {
				setMsg("Error en Transacción.");
			}
		}
		setShowModal(true);
		return "";
		
		/*else
		{
			setBrand(currentItem);
			listBrand.set(currentRow, currentItem);
			keys.clear();
			keys.add(currentRow);
			setMsg("La marca ha sido actualizada.");
			setShowModal(true);
		}
		return "";*/
	}
	
	/**
	 * Sets the list of brands of this managed bean.
	 * @param listBrand List of objects TbBrand
	 */
	public void setListBrand(List<jpa.TbBrand> listBrand) {
		this.listBrand = listBrand;
	}

	/**
	 * Gets the list of brands of this managed bean.
	 * @return List of objects TbBrand
	 */
	public List<jpa.TbBrand> getListBrand() {
		listBrand = bm.getBrands();
		return listBrand;
	}
	
	/**
	 * Gets the list of brands of this managed bean by range
	 * @param first Row number where the range starts
	 * @param count Number of rows to get
	 * @return List of objects TbBrand
	 */
	public List<jpa.TbBrand> getListBrandByRange(Long first, Long count){
		listBrand = bm.getBrands(first, count);
		return listBrand;
	}
	
	/**
	 * Gets the brand of a vehicle.
	 * @param id Identification
	 * @return Brand of the specified id
	 */
	public jpa.TbBrand getBrand(Long id){
		return bm.getBrand(id);
	}
	
	/**
	 * Sets the brand of a vehicle.
	 * @param brand Vehicle's brand
	 */
	public void setBrand(TbBrand brand){
		//bm.setBrand(brand);
		bm.insertBrand(brand, SessionUtil.ip(), SessionUtil.sessionUser().getUserId());
	}
	
	/**
	 * Determines if there is a particular brand in the database
	 * @param id Identification
	 * @return True if the specified brand id exists, false otherwise.
	 */
	public boolean hasBrand(Long id){
		return bm.hasBrandById(id);
	}
	
	/**
	 * Gets the count of all brands available in the database
	 * @return Brand count
	 */
	public int getAllBrandsCount(){
		return bm.getAllBrandsCount();
	}
	
	/**
	 * Sets the selected row of the user
	 * @param currentRow Selected row. Zero based.
	 */
	public void setCurrentRow(int currentRow) {
		this.currentRow = currentRow;
	}

	/**
	 * Gets the selected row of the user
	 * @return Selected row. Zero based.
	 */
	public int getCurrentRow() {
		return currentRow;
	}

	/**
	 * Sets the user-selected brand
	 * @param currentItem brand
	 */
	public void setCurrentItem(TbBrand currentItem) {
		this.currentItem = currentItem;
	}

	/**
	 * Gets the user-selected brand
	 * @return brand
	 */
	public TbBrand getCurrentItem() {
		return currentItem;
	}

	/**
	 * Sets the rows to be updated after an AJAX request
	 * @param keys Primary keys of rows to update
	 */
	public void setKeys(Set<Integer> keys) {
		this.keys = keys;
	}

	/**
	 * Gets the primary keys to be updated after an AJAX request
	 * @return Primary keys of rows to update
	 */
	public Set<Integer> getKeys() {
		return keys;
	}

	public void setNewItem(TbBrand newItem) {
		this.newItem = newItem;
	}

	public TbBrand getNewItem() {
		return newItem;
	}
	
	public void setShowModal(boolean showModal) {
		this.showModal = showModal;
	}
	
	public boolean isShowModal() {
		return showModal;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public String getMsg() {
		return msg;
	}	
}