/**
 * 
 */
package process.purchasing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import com.sun.xml.rpc.processor.modeler.j2ee.xml.exceptionMappingType;

import ejb.Purchase;
import ejb.User;

import jpa.TbConsignment;
import jpa.TbSystemUser;

/**
 * Bean To manage the consult of consignment. 
 * @author tmolina
 *
 */
public class ConsultConsignment implements Serializable {
	private static final long serialVersionUID = -8164832276033427511L;

	@EJB(mappedName="ejb/Purchase")
	private Purchase purchase;
	
	@EJB(mappedName="ejb/User")
	private User user;
	
	// Attributes ------------- //
	
	private List<TbConsignment> consignmentList;
	
	private List<SelectItem> clients;
	
	private Long idClient;
	
	private boolean showConsignments;
	
	private boolean noData;
	
	private String dataMessage;
	
	// Modal Control -------- //
	
	private boolean showModal;
	
	private String modalMessage;
	
	/**
	 * Constructor 
	 */
	public ConsultConsignment() {
	}
	
	// Actions ---------------- //
	
	/**
	 * Hides Modal.
	 */
	public String hideModal(){
		setShowModal(false);
		setNoData(false);
		setModalMessage("");
		setDataMessage("");
		return null;
	}
	
	/**
	 * 
	 */
	public String search() {
		System.out.println(idClient);
		setDataMessage(null);
		if(idClient.longValue() != -1L) {
			if(consignmentList == null) {
				consignmentList = new ArrayList<TbConsignment>();
			} else {
				consignmentList.clear();
			}
			consignmentList = purchase.getConsignmentByClient(idClient);
			if(consignmentList.size() > 0) {
				showConsignments = true;
				noData = false;
			} else {
				setDataMessage("El Cliente no tiene consignaciones Asociadas.");
				showConsignments = false;
				noData = true;
			}
		} else {
			showConsignments = false;
			noData = true;
			setDataMessage("Debe Seleccionar un Cliente.");
		}
		return null;
	}
	
	// Getters and Setters -------- //

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
	 * @param clients the clients to set
	 */
	public void setClients(List<SelectItem> clients) {
		this.clients = clients;
	}

	/**
	 * @return the clients
	 */
	public List<SelectItem> getClients() {
	 try{	
		if(clients == null){
			clients = new ArrayList<SelectItem>();
			clients.add(new SelectItem(-1L, "--Seleccione Uno--"));
			for(TbSystemUser su : user.getAllActiveClient()){
				String name = su.getUserNames();
				if(su.getTbCodeType().getCodeTypeId().longValue() != 3L){
					name += " " + su.getUserSecondNames();
				}
				clients.add(new SelectItem(su.getUserId(), name));
			}
		}
		return clients;
	 }	
	  catch(Exception e){
		  e.printStackTrace();
		  return null;
	  }
	}

	/**
	 * @param idClient the idClient to set
	 */
	public void setIdClient(Long idClient) {
		this.idClient = idClient;
	}

	/**
	 * @return the idClient
	 */
	public Long getIdClient() {
		return idClient;
	}

	/**
	 * @param consignmentList the consignmentList to set
	 */
	public void setConsignmentList(List<TbConsignment> consignmentList) {
		this.consignmentList = consignmentList;
	}

	/**
	 * @return the consignmentList
	 */
	public List<TbConsignment> getConsignmentList() {
		return consignmentList;
	}

	/**
	 * @param showConsignments the showConsignments to set
	 */
	public void setShowConsignments(boolean showConsignments) {
		this.showConsignments = showConsignments;
	}

	/**
	 * @return the showConsignments
	 */
	public boolean isShowConsignments() {
		return showConsignments;
	}

	/**
	 * @param noData the noData to set
	 */
	public void setNoData(boolean noData) {
		this.noData = noData;
	}

	/**
	 * @return the noData
	 */
	public boolean isNoData() {
		return noData;
	}

	/**
	 * @param dataMessage the dataMessage to set
	 */
	public void setDataMessage(String dataMessage) {
		this.dataMessage = dataMessage;
	}

	/**
	 * @return the dataMessage
	 */
	public String getDataMessage() {
		return dataMessage;
	}
}