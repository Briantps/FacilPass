/**
 * 
 */
package crud;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import sessionVar.SessionUtil;

import ejb.crud.WarehouseCardType;

import jpa.TbWarehouseCardType;

/**
 * Bean to Manage the administration of card types
 * @author tmolina
 *
 */
public class WarehouseCardTypeBean implements Serializable {
	private static final long serialVersionUID = 7403948675136849675L;

	@EJB(mappedName="ejb/WarehouseCardType")
	private WarehouseCardType ware;
	
	// Attributes ----- //
	
	private boolean showModal;
	
	private String modalMessage;
	
	private List<TbWarehouseCardType> cardTypeList;
	
	private Long cardTypeId;
	
	private boolean showInsert;
	
	private String cardType;
	
	private boolean showEdit;

	/**
	 * Constructor.
	 */
	public WarehouseCardTypeBean() {
	}
	
	// Actions ------------ //
	
	/**
	 * Init Add Card Type.
	 */
	public String initAddCardType(){
		setShowInsert(true);
		setShowEdit(false);
		setCardType(null);
		return null;
	}
	
	/**
	 * Inserts a new Card Type.
	 */
	public String addCardType(){
		setShowInsert(false);
		Long result = ware.insertCardType(cardType, SessionUtil.ip(),
				SessionUtil.sessionUser().getUserId());
		if (result != null) {
			if(result != -1L){
				setModalMessage("Transacción Exitosa.");
				setCardTypeList(null);
			} else {
				setModalMessage("Ya hay un Tipo de Tarjeta con ese nombre. Verifique.");
			}
		} else {
			setModalMessage("Error en Transacción.");
		}
		setShowModal(true);
		return null;
	}
	
	/**
	 * Init Card Type edition.
	 */
	public String initEditCardType(){
		setShowInsert(false);
		setShowEdit(true);
		for(TbWarehouseCardType w : cardTypeList){
			if(cardTypeId.longValue() == w.getWarehouseCardTypeId().longValue()){
				cardType = w.getWarehouseCardTypeName();
				break;
			}
		}
		return null;
	}
	
	/**
	 * Edits a Card Type.
	 */
	public String editCardType() {
		setShowEdit(false);
		setShowInsert(false);
		if (cardType != null) {
			for(TbWarehouseCardType  w : cardTypeList){
				if(cardTypeId.longValue() != w.getWarehouseCardTypeId().longValue()
						&& w.getWarehouseCardTypeName().equals(cardType.toUpperCase())){
					setModalMessage("Ya hay un tipo de tarjeta con ese nombre, Verifique.");
					setShowModal(true);
					return null;
				}
			}
			
			if (ware.editCardType(cardTypeId, cardType, SessionUtil.ip(),
					SessionUtil.sessionUser().getUserId())) {
				setModalMessage("Transacción Exitosa.");
				setCardTypeList(null);
			} else {
				setModalMessage("Error en Transacción.");
			}
			setShowModal(true);
		}
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
	 * @param cardTypeId the cardTypeId to set
	 */
	public void setCardTypeId(Long cardTypeId) {
		this.cardTypeId = cardTypeId;
	}

	/**
	 * @return the cardTypeId
	 */
	public Long getCardTypeId() {
		return cardTypeId;
	}

	/**
	 * @param cardType the cardType to set
	 */
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	/**
	 * @return the cardType
	 */
	public String getCardType() {
		return cardType;
	}

	/**
	 * @param cardTypeList the cardTypeList to set
	 */
	public void setCardTypeList(List<TbWarehouseCardType> cardTypeList) {
		this.cardTypeList = cardTypeList;
	}

	/**
	 * @return the cardTypeList
	 */
	public List<TbWarehouseCardType> getCardTypeList() {
		if(cardTypeList == null) {
			cardTypeList = new ArrayList<TbWarehouseCardType>();
			cardTypeList = ware.getWarehouseCardType();
		}
		return cardTypeList;
	}
}