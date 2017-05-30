/**
 * 
 */
package crud;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import sessionVar.SessionUtil;

import ejb.crud.Point;

import jpa.TbPoint;

/**
 * Bean to Manage the administration of Point.
 * @author tmolina
 *
 */
public class PointBean implements Serializable {
	private static final long serialVersionUID = 925480315162572942L;

	@EJB(mappedName="ejb/Point")
	private Point point;
	
	// Attributes --------- //
	
	private boolean showModal;
	
	private String modalMessage;
	
	private List<TbPoint> pointList;
	
	private Long pointId;

	private Integer pointPort;
	
	private String pointName;
	
	private String pointIp;
	
	private String ip1;
	
	private String ip2;
	
	private String ip3;
	
	private String ip4;
	
	private boolean showInsert;
	
	private boolean showEdit;

	/**
	 * Constructor.
	 */
	public PointBean() {
	}
	
	// Actions ------------ //
	
	/**
	 * Init Add Point.
	 */
	public String initAddPoint(){
		setShowInsert(true);
		setShowEdit(false);
		setIp1(null);
		setIp2(null);
		setIp3(null);
		setIp4(null);
		setPointPort(null);
		return null;
	}
	
	/**
	 * Inserts a new Point.
	 */
	public String addPoint(){
		setShowInsert(false);
		pointIp = ip1.trim() + "." + ip2.trim() + "." + ip3.trim() + "." + ip4.trim();
		Long result = point.insertPoint(pointIp, pointPort, pointName, SessionUtil.ip(),
				SessionUtil.sessionUser().getUserId());
		if (result != null) {
			if(result != -1L){
				setModalMessage("Transacción Exitosa.");
				setPointList(null);
			} else {
				setModalMessage("Ya hay una Punto de recarga con esa IP/Puerto. Verifique.");
			}
		} else {
			setModalMessage("Error en Transacción.");
		}
		setShowModal(true);
		return null;
	}
	
	/**
	 * Init Edition of Point.
	 */
	public String initEditPoint(){
		setShowInsert(false);
		setShowEdit(true);
		for(TbPoint po : pointList){
			if(pointId.longValue() == po.getPointId().longValue()){
				pointPort = po.getPointPort();
				pointIp = po.getPointIp();
				pointName = po.getPointName();
				String[] a = pointIp.split("\\.");
				ip1 = a[0]; ip2 = a[1]; ip3 = a[2]; ip4 = a[3];
				break;
			}
		}
		return null;
	}
	
	/**
	 * Edits a Point.
	 */
	public String editPoint(){
		setShowEdit(false);
		setShowInsert(false);
		pointIp = ip1.trim() + "." + ip2.trim() + "." + ip3.trim() + "." + ip4.trim();
		if(pointIp!=null && pointPort!=null){
			for(TbPoint po : pointList){	
				if(pointId.longValue() != po.getPointId().longValue()){
					if( po.getPointIp().equals(pointIp) && po.getPointPort().intValue() == pointPort.intValue()){
					  if(po.getPointName().toUpperCase().equals(pointName.toUpperCase())){	
						setModalMessage("Ya hay una Punto de recarga con esa IP/Puerto/Nombre, Verifique por Favor.");
						setShowModal(true);
						return null;
					  }
					}
				}
			}
			if (point.editPoint(pointId, pointIp, pointPort,pointName, SessionUtil.ip(),
					SessionUtil.sessionUser().getUserId())) {
				setModalMessage("Transacción Exitosa.");
				setPointList(null);
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
	 * @param pointList the pointList to set
	 */
	public void setPointList(List<TbPoint> pointList) {
		this.pointList = pointList;
	}

	/**
	 * @return the pointList
	 */
	public List<TbPoint> getPointList() {
		if(pointList == null) {
			pointList = new ArrayList<TbPoint>();
			pointList = point.getPoints();
		}
		return pointList;
	}

	/**
	 * @param pointPort the pointPort to set
	 */
	public void setPointPort(Integer pointPort) {
		this.pointPort = pointPort;
	}

	/**
	 * @return the pointPort
	 */
	public Integer getPointPort() {
		return pointPort;
	}

	/**
	 * @param pointIp the pointIp to set
	 */
	public void setPointIp(String pointIp) {
		this.pointIp = pointIp;
	}

	/**
	 * @return the pointIp
	 */
	public String getPointIp() {
		return pointIp;
	}

	/**
	 * @param ip1 the ip1 to set
	 */
	public void setIp1(String ip1) {
		this.ip1 = ip1;
	}

	/**
	 * @return the ip1
	 */
	public String getIp1() {
		return ip1;
	}

	/**
	 * @param ip2 the ip2 to set
	 */
	public void setIp2(String ip2) {
		this.ip2 = ip2;
	}

	/**
	 * @return the ip2
	 */
	public String getIp2() {
		return ip2;
	}

	/**
	 * @param ip3 the ip3 to set
	 */
	public void setIp3(String ip3) {
		this.ip3 = ip3;
	}

	/**
	 * @return the ip3
	 */
	public String getIp3() {
		return ip3;
	}

	/**
	 * @param ip4 the ip4 to set
	 */
	public void setIp4(String ip4) {
		this.ip4 = ip4;
	}

	/**
	 * @return the ip4
	 */
	public String getIp4() {
		return ip4;
	}
	
	/**
	 * @return the pointId
	 */
	public Long getPointId() {
		return pointId;
	}

	/**
	 * @param pointId the pointId to set
	 */
	public void setPointId(Long pointId) {
		this.pointId = pointId;
	}

	public void setPointName(String pointName) {
		this.pointName = pointName;
	}

	public String getPointName() {
		return pointName;
	}
}