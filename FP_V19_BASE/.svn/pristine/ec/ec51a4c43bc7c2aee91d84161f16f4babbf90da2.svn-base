/**
 * 
 */
package mBeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import sessionVar.SessionUtil;

import jpa.TbSystemUser;

/**
 * @author tmolina
 *
 */
public class ReceptionBean implements Serializable{
	private static final long serialVersionUID = -4387785919942966126L;
	
	@EJB(mappedName="ejb/Process")
	private ejb.Process process;
	
	private Long id;
	
	private boolean showModal;
	
	private String messageModal;
	
	// Attributes 
	private List<TbSystemUser> clients;
	
	private boolean showTable;
	
	/**
	 * Constructor.
	 */
	public ReceptionBean(){		
	}
	
	// Actions
	
	/**
	 * Indicates that the clients documents have been received.
	 */
	public String receive(){
		if (process.affiliationDocsCheck(id, SessionUtil.sessionUser()
				.getUserId(), SessionUtil.ip())) {
			setMessageModal("Transacción Exitosa.");
		}else{
			setMessageModal("Error en Transacción, comuníquese con el Administrador.");
		}
		setShowModal(true);
		return null;
	}
	
	/**
	 * Clear modal panel data.
	 */
	public String hideModal(){
		setMessageModal("");
		setShowModal(false);
		return null;
	}
	
	// Getters and Setters

	/**
	 * @param clients the clients to set
	 */
	public void setClients(List<TbSystemUser> clients) {
		this.clients = clients;
	}

	/**
	 * @return the clients
	 */
	public List<TbSystemUser> getClients() {
			clients = new ArrayList<TbSystemUser>();
			clients = process.getPotentialClients();
		return clients;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

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
	 * @param messageModal the messageModal to set
	 */
	public void setMessageModal(String messageModal) {
		this.messageModal = messageModal;
	}

	/**
	 * @return the messageModal
	 */
	public String getMessageModal() {
		return messageModal;
	}

	/**
	 * @param showTable the showTable to set
	 */
	public void setShowTable(boolean showTable) {
		this.showTable = showTable;
	}

	/**
	 * @return the showTable
	 */
	public boolean isShowTable() {
		if (this.getClients().size() > 0) {
			showTable = true;
		} else {
			showTable = false;
		}
		return showTable;
	}
}