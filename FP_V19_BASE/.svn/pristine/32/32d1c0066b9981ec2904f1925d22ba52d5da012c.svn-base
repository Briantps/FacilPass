/**
 * 
 */
package process.recharge;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import sessionVar.SessionUtil;

import jpa.TbPoint;
import jpa.TbSystemUser;

import ejb.User;
import ejb.crud.Point;

/**
 * @author tmolina
 *
 */
public class AsignPointBean implements Serializable {
	private static final long serialVersionUID = 6500805642305307288L;
	
	@EJB(mappedName="ejb/User")
	private User user;
	
	@EJB(mappedName="ejb/Point")
	private Point point;
	
	// Attributes ------ //
	
	private Long userCodeTypeId;
	
	private String userCode;
	
	private boolean showData;
	 
	private boolean showMessage;
	
	private boolean showModal;
	
	private String modalMessage;
	
	private TbSystemUser userObject;
	
	private List<TbPoint> userPoints;
	
	private boolean showPointList;
	
	private boolean showAddPanel;
	
	private boolean showAvailablePointList;
	
	private List<TbPoint> pointList;
	
	private Long pointId;
	
	/**
	 * Constructor.
	 */
	public AsignPointBean () {
	}
	
	// Actions -------------- //
	
	/**
	 * Hides Modal Panel.
	 */
	public String hideModal() {
		setShowModal(false);
		setModalMessage(null);
		setShowAddPanel(false);
		return null;
	}
	
	/**
	 * 
	 */
	public  String search() {
		if(userCodeTypeId != null) {
			userObject = user.getUserByCode(userCode, userCodeTypeId);
			if (userObject != null) {
				setShowMessage(false);
				setShowData(true);
				
				if (userPoints == null) {
					userPoints = new ArrayList<TbPoint>();
				} else {
					userPoints.clear();
				}
				
				userPoints = point.getUserPoints(userObject.getUserId());
				if(userPoints.size() > 0) {
					setShowPointList(true);
				} else {
					setShowPointList(false);
				}
				
			} else {
				setShowMessage(true);
				setShowData(false);
			}
		} else {
			setShowModal(true);
			setModalMessage("Debe Seleccionar un Tipo de Documento.");
		}
		return null;
	}

	/**
	 * 
	 */
	public  String initAsign() {
		setShowAddPanel(true);
		if(pointList == null) {
			pointList = new ArrayList<TbPoint>();
		} else {
			pointList.clear();
		}
		pointList = point.getAvailablePoints(userObject.getUserId());
		if (pointList.size() > 0) {
			setShowAvailablePointList(true);
		} else {
			setShowAvailablePointList(false);
		}
		return null;
	}
	
	/**
	 * 
	 */
	public String asign() {
		setShowAddPanel(false);
		if (point.asignPointToClient(userObject.getUserId(), pointId,
				SessionUtil.sessionUser().getUserId(), SessionUtil.ip())) {
			setModalMessage("Transacción Exitosa");
			this.search();
		} else {
			setModalMessage("Error en Transacción. ");
		}
		setShowModal(true);
		return null;
	}
	
	/**
	 * 
	 */
	public String removeAsign() {
		if (point.removeClientPoint(userObject.getUserId(), pointId,
				SessionUtil.sessionUser().getUserId(), SessionUtil.ip())) {
			setModalMessage("Transacción Exitosa");
			this.search();
		} else {
			setModalMessage("Error en Transacción. ");
		}
		setShowModal(true);
		return null;
	}
	
	// Getters and Setters --------- //
	
	/**
	 * @param userCodeTypeId the userCodeTypeId to set
	 */
	public void setUserCodeTypeId(Long userCodeTypeId) {
		this.userCodeTypeId = userCodeTypeId;
	}

	/**
	 * @return the userCodeTypeId
	 */
	public Long getUserCodeTypeId() {
		return userCodeTypeId;
	}

	/**
	 * @param userCode the userCode to set
	 */
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	/**
	 * @return the userCode
	 */
	public String getUserCode() {
		return userCode;
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
	 * @param userObject the userObject to set
	 */
	public void setUserObject(TbSystemUser userObject) {
		this.userObject = userObject;
	}

	/**
	 * @return the userObject
	 */
	public TbSystemUser getUserObject() {
		return userObject;
	}

	/**
	 * @param userPoints the userPoints to set
	 */
	public void setUserPoints(List<TbPoint> userPoints) {
		this.userPoints = userPoints;
	}

	/**
	 * @return the userPoints
	 */
	public List<TbPoint> getUserPoints() {
		return userPoints;
	}

	/**
	 * @param showPointList the showPointList to set
	 */
	public void setShowPointList(boolean showPointList) {
		this.showPointList = showPointList;
	}

	/**
	 * @return the showPointList
	 */
	public boolean isShowPointList() {
		return showPointList;
	}

	/**
	 * @param showAddPanel the showAddPanel to set
	 */
	public void setShowAddPanel(boolean showAddPanel) {
		this.showAddPanel = showAddPanel;
	}

	/**
	 * @return the showAddPanel
	 */
	public boolean isShowAddPanel() {
		return showAddPanel;
	}

	/**
	 * @param showAvailablePointList the showAvailablePointList to set
	 */
	public void setShowAvailablePointList(boolean showAvailablePointList) {
		this.showAvailablePointList = showAvailablePointList;
	}

	/**
	 * @return the showAvailablePointList
	 */
	public boolean isShowAvailablePointList() {
		return showAvailablePointList;
	}

	/**
	 * @param pointList the pointList to set
	 */
	public void setPointList(List<TbPoint> pointList) {
		this.pointList = pointList;
	}

	/**
	 * @return the pointList
	 */
	public List<TbPoint> getPointList() {
		return pointList;
	}

	/**
	 * @param pointId the pointId to set
	 */
	public void setPointId(Long pointId) {
		this.pointId = pointId;
	}

	/**
	 * @return the pointId
	 */
	public Long getPointId() {
		return pointId;
	}
}