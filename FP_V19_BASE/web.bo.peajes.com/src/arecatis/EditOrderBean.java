/**
 * 
 */
package arecatis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import constant.PurchaseOrderState;

import jpa.TbOperationType;
import jpa.TbUserData;

import ejb.Purchase;
import ejb.User;

import sessionVar.SessionUtil;
import util.PurchaseOrder;

/**
 * @author tmolina
 *
 */
public class EditOrderBean implements Serializable {
	private static final long serialVersionUID = -8976976449818756736L;
	
	@EJB(mappedName="ejb/Purchase")
	private Purchase purchase;
	
	@EJB(mappedName="ejb/User")
	private User user;
	
	// Attributes -------------- // 
	
	private Long orderNumber;
	
	private PurchaseOrder order;
	
	private Long idDetail;
	
	private Integer numOper;
	
	private Long type;
	
	private List<SelectItem> listTypes;
	
	private List<TbUserData> clientData;
	
	private List<SelectItem> clientDataList;
	
	private Long clientDataId;
	
	// Visibility --------------- //
	
	private boolean showData;
	
	private boolean showMessage;
	
	private boolean showNewDetail;
	
	private boolean showChangeOffice;
	
	// Control Modal ------------ //
	
	private boolean showModal;
	
	private String modalMessage;

	/**
	 * Constructor.
	 */
	public EditOrderBean() {
		clientDataList = new ArrayList<SelectItem>();
	}
	
	// Actions ---------------- //
	
	/*
	 * Searches Purchase Order Data  by  Order Number.
	 */
	public String search(){
		order = purchase.getPurchaseOrderByState(orderNumber, PurchaseOrderState.NEW, true);
		
		if (order != null) {
			
			numOper = order.getDetails().size();
			
			setShowData(true);
			setShowMessage(false);
		} else {
			setShowData(false);
			setShowMessage(true);
		}
		return null;
	}
	
	/*
	 * Control Modal.
	 */
	public String hideModal(){
		setModalMessage("");
		setShowModal(false);
		setShowNewDetail(false);
		setShowChangeOffice(false);
		return null;
	}
	
	/*
	 * Deletes a detail of the purchase order if this order has more than one item.
	 */
	public String deleteDetail(){
		if (numOper > 1) {
			if (purchase.deleteDetail(idDetail, SessionUtil.ip(), SessionUtil
					.sessionUser().getUserId())) {
				order = purchase.getPurchaseOrderByState(order.getOrder()
						.getPuchaseOrderNumber(), PurchaseOrderState.NEW, true);
				numOper = order.getDetails().size();
			}
		} else {
			setModalMessage("No puede eliminar todos los detalles de la Orden de Pedido.");
			setShowModal(true);
		}
		return null;
	}
	
	/*
	 * Init modal to add new Order detail.
	 */
	public String initNew(){
		setShowNewDetail(true);
		return null;
	}
	
	/*
	 * Saves an order detail.
	 */
	public String saveDetail(){
		setShowNewDetail(false);
		if(purchase.addOrderDetail(type, order.getOrder().getPurchaseOrderId(), SessionUtil.ip(),
				SessionUtil.sessionUser().getUserId())){
			order = purchase.getPurchaseOrderByState(order.getOrder()
					.getPuchaseOrderNumber(), PurchaseOrderState.NEW, true);
			numOper = order.getDetails().size();
		} else {
			setModalMessage("No se ha podido agregar el detalles a la Orden de Pedido.");
			setShowModal(true);
		}
		return null;
	}
	
	/*
	 * Changes the office.
	 */
	public String initChangeOffice(){
	 	clientData = new ArrayList<TbUserData>();
		clientData = user.getClientData(order.getOrder().getTbUserData().getTbSystemUser().getUserId());
	 	if(clientData.size() > 0){
	 		
	 		clientDataList.clear();
			for (TbUserData ud : clientData) {
				clientDataList.add(new SelectItem(ud.getUserDataId(), ud
						.getUserDataOfficeName()));
			}
	 	}
		setShowChangeOffice(true);
		return null;
	}
	
    /*
     * Changes purchase order office.
     */
	public String saveChangeOffice(){
		setShowChangeOffice(false);
		if (purchase.changeOrderClientOffice(clientDataId, order.getOrder()
				.getPurchaseOrderId(), SessionUtil.ip(), SessionUtil
				.sessionUser().getUserId())) {
			order = purchase.getPurchaseOrderByState(order.getOrder()
					.getPuchaseOrderNumber(), PurchaseOrderState.NEW, true);
			numOper = order.getDetails().size();
		} else {
			setModalMessage("No se ha podido cambiar la dependencia de la Orden de Pedido.");
			setShowModal(true);
		}
		return null;
	}
	
	// Getters and Setters -------- //

	/**
	 * @param orderNumber the orderNumber to set
	 */
	public void setOrderNumber(Long orderNumber) {
		this.orderNumber = orderNumber;
	}

	/**
	 * @return the orderNumber
	 */
	public Long getOrderNumber() {
		return orderNumber;
	}

	/**
	 * @param showData the showData to set
	 */
	public void setShowData(boolean showData) {
		this.showData = showData;
	}

	/**
	 * @return the showData
	 */
	public boolean isShowData() {
		return showData;
	}

	/**
	 * @param showMessage the showMessage to set
	 */
	public void setShowMessage(boolean showMessage) {
		this.showMessage = showMessage;
	}

	/**
	 * @return the showMessage
	 */
	public boolean isShowMessage() {
		return showMessage;
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
	 * @param order the order to set
	 */
	public void setOrder(PurchaseOrder order) {
		this.order = order;
	}

	/**
	 * @return the order
	 */
	public PurchaseOrder getOrder() {
		return order;
	}

	/**
	 * @param idDetail the idDetail to set
	 */
	public void setIdDetail(Long idDetail) {
		this.idDetail = idDetail;
	}

	/**
	 * @return the idDetail
	 */
	public Long getIdDetail() {
		return idDetail;
	}

	/**
	 * @param numOper the numOper to set
	 */
	public void setNumOper(Integer numOper) {
		this.numOper = numOper;
	}

	/**
	 * @return the numOper
	 */
	public Integer getNumOper() {
		return numOper;
	}

	/**
	 * @param showNewDetail the showNewDetail to set
	 */
	public void setShowNewDetail(boolean showNewDetail) {
		this.showNewDetail = showNewDetail;
	}

	/**
	 * @return the showNewDetail
	 */
	public boolean isShowNewDetail() {
		return showNewDetail;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(Long type) {
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public Long getType() {
		return type;
	}

	/**
	 * @param listTypes the listTypes to set
	 */
	public void setListTypes(List<SelectItem> listTypes) {
		this.listTypes = listTypes;
	}

	/**
	 * @return the listTypes
	 */
	public List<SelectItem> getListTypes() {
		if (listTypes == null) {
			listTypes = new ArrayList<SelectItem>();
			for (TbOperationType o : purchase.getListOfOperationTypes()) {
				listTypes.add(new SelectItem(o.getOperationTypeId(), o
						.getOperationTypeName()));
			}
		}
		return listTypes;
	}

	/**
	 * @param showChangeOffice the showChangeOffice to set
	 */
	public void setShowChangeOffice(boolean showChangeOffice) {
		this.showChangeOffice = showChangeOffice;
	}

	/**
	 * @return the showChangeOffice
	 */
	public boolean isShowChangeOffice() {
		return showChangeOffice;
	}

	/**
	 * @param clientData the clientData to set
	 */
	public void setClientData(List<TbUserData> clientData) {
		this.clientData = clientData;
	}

	/**
	 * @return the clientData
	 */
	public List<TbUserData> getClientData() {
		return clientData;
	}

	/**
	 * @param clientDataList the clientDataList to set
	 */
	public void setClientDataList(List<SelectItem> clientDataList) {
		this.clientDataList = clientDataList;
	}

	/**
	 * @return the clientDataList
	 */
	public List<SelectItem> getClientDataList() {
		return clientDataList;
	}

	/**
	 * @param clientDataId the clientDataId to set
	 */
	public void setClientDataId(Long clientDataId) {
		this.clientDataId = clientDataId;
	}

	/**
	 * @return the clientDataId
	 */
	public Long getClientDataId() {
		return clientDataId;
	}
}