/**
 * 
 */
package crud;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import sessionVar.SessionUtil;

import ejb.crud.WarehouseDependency;

import jpa.TbWarehouseDependency;

/**
 * Bean to Manage the administration of warehouse Dependency.
 * @author tmolina
 *
 */
public class WarehouseDependencyBean implements Serializable {
	private static final long serialVersionUID = 3627127370805971958L;

	@EJB(mappedName="ejb/WarehouseDependency")
	private WarehouseDependency ware;
	
	// Attributes.
	
	private boolean showModal;
	
	private String modalMessage;
	
	private List<TbWarehouseDependency> dependencyList;
	
	private Long dependencyId;
	
	private boolean showInsert;
	
	private String dependency;
	
	private boolean showEdit;

	/**
	 * Constructor.
	 */
	public WarehouseDependencyBean() {
	}
	
	// Actions ------------ //
	
	/**
	 * Init Add warehouse dependency.
	 */
	public String initAddDependency(){
		setShowInsert(true);
		setShowEdit(false);
		setDependency(null);
		return null;
	}
	
	/**
	 * Inserts a new warehouse dependency.
	 */
	public String addDependency(){
		this.hideModal();
		if (dependency.equals(null)|| dependency.equals("") || !dependency.matches(".*\\w.*"))
		{
			setModalMessage("El Nombre de la Dependencia es necesario.");
		}
		else if (dependency.length() > 50)
		{
			setModalMessage("La longitud del campo Dependencia no es correcta. Recuerde que debe ser máximo 50 caracteres.");			
		}
		else
		{
			Long result = ware.insertDependency(dependency, SessionUtil.ip(),
					SessionUtil.sessionUser().getUserId());		
			if (result != null) {
				if(result != -1L){
					setModalMessage("Se ha creado la dependencia exitosamente.");
					setDependencyList(null);
				} else {
					setModalMessage("Ya hay una dependencia con ese nombre. Verifique.");
				}
			} else {
				setModalMessage("Error en Transacción.");
			}		
		}
		setShowModal(true);
		return null;
	}
	
	/**
	 * Init Dependency edition.
	 */
	public String initEditDependency(){
		setShowInsert(false);
		setShowEdit(true);
		for(TbWarehouseDependency w : dependencyList){
			if(dependencyId.longValue() == w.getWarehouseDependencyId().longValue()){
				dependency = w.getWarehouseDependencyName();
				break;
			}
		}
		return null;
	}
	
	/**
	 * Edits a warehouse dependency.
	 */
	public String editDependency(){
		setShowEdit(false);
		setShowInsert(false);
		if (dependency.equals(null)|| dependency.equals("") || !dependency.matches(".*\\w.*"))
		{
			setModalMessage("El Nombre de la Dependencia es necesario.");
		}
		else if (dependency.length() > 50)
		{
			setModalMessage("La longitud del campo Dependencia no es correcta. Recuerde que debe ser máximo 50 caracteres.");			
		}
		else
		{
			if (dependency != null) {
				for(TbWarehouseDependency w : dependencyList){
					if(dependencyId.longValue() != w.getWarehouseDependencyId().longValue()
							&& w.getWarehouseDependencyName().equals(dependency.toUpperCase())){
						setModalMessage("Ya hay una dependencia con ese nombre, Verifique.");
						setShowModal(true);
						return null;
					}
				}
				
				if (ware.editDependency(dependencyId, dependency, SessionUtil.ip(),
						SessionUtil.sessionUser().getUserId())) {
					setModalMessage("Se ha editado la dependencia exitosamente.");
					setDependencyList(null);
				} else {
					setModalMessage("Error en Transacción.");
				}
			}
		}
		setShowModal(true);
		return null;
	}
	
	/**
	 * Hides modal windows.
	 */
	public String hideModal(){
		setShowInsert(false);
		setShowEdit(false);
		setShowModal(false);
		setModalMessage(null);
		return null;
	}
	
	// Getters and Setters ------------ //

	/**
	 * @param showModal the showModal to set
	 */
	public void setShowModal(boolean showModal) {
		this.showModal = showModal;
	}

	/**
	 * @return the showModal
	 */
	public boolean isShowModal() {
		return showModal;
	}

	/**
	 * @param modalMessage the modalMessage to set
	 */
	public void setModalMessage(String modalMessage) {
		this.modalMessage = modalMessage;
	}

	/**
	 * @return the modalMessage
	 */
	public String getModalMessage() {
		return modalMessage;
	}

	/**
	 * @param showInsert the showInsert to set
	 */
	public void setShowInsert(boolean showInsert) {
		this.showInsert = showInsert;
	}

	/**
	 * @return the showInsert
	 */
	public boolean isShowInsert() {
		return showInsert;
	}

	/**
	 * @param showEdit the showEdit to set
	 */
	public void setShowEdit(boolean showEdit) {
		this.showEdit = showEdit;
	}

	/**
	 * @return the showEdit
	 */
	public boolean isShowEdit() {
		return showEdit;
	}

	/**
	 * @param dependencyId the dependencyId to set
	 */
	public void setDependencyId(Long dependencyId) {
		this.dependencyId = dependencyId;
	}

	/**
	 * @return the dependencyId
	 */
	public Long getDependencyId() {
		return dependencyId;
	}

	/**
	 * @param dependency the dependency to set
	 */
	public void setDependency(String dependency) {
		this.dependency = dependency;
	}

	/**
	 * @return the dependency
	 */
	public String getDependency() {
		return dependency;
	}

	/**
	 * @param dependencyList the dependencyList to set
	 */
	public void setDependencyList(List<TbWarehouseDependency> dependencyList) {
		this.dependencyList = dependencyList;
	}

	/**
	 * @return the dependencyList
	 */
	public List<TbWarehouseDependency> getDependencyList() {
		if(dependencyList == null){
			dependencyList = new ArrayList<TbWarehouseDependency>();
			dependencyList = ware.getWarehouseDependecy();
		}
		return dependencyList;
	}
}