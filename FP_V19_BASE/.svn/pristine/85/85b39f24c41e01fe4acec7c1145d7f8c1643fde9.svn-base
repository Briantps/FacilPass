package objection;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import jpa.TbObjection;
import sessionVar.SessionUtil;
import validator.Validation;
import constant.AjustmentStateType;
import constant.ObjectionStateType;
import ejb.Objection;

public class ApproveObjectionBean implements  Serializable {

	private static final long serialVersionUID = 1L;

	@EJB(mappedName="ejb/Objection")
	private Objection objection;


	private List<TbObjection> listObjection;

	private Long objectionId;

	private String objectionComments;

	private Long accountId;

	private String charge;

	private Timestamp dateTransaction;

	private Long valueAjust;

	private boolean modalDetailObjection;

	private boolean detailTransaction;

	private String conc;

	private String esta;

	private String carr;

	private String observ;

	private boolean showError;

	private String msgError;

	private String modalMessage;

	private boolean showModal;

	private String modalMessageRej;

	private boolean showModalRej;

	private String valueAjustText;

	private int typeOp = 1;

	public void setListObjection(List<TbObjection> listObjection) {
		this.listObjection = listObjection;
	}

	public List<TbObjection> getListObjection() {
		if(listObjection != null){
			listObjection.clear();
		} else {
			listObjection = new ArrayList<TbObjection>();
		}
		listObjection = objection.getObjectionByState(ObjectionStateType.PENDING_AUTHORIZATION.getId());
		return listObjection;
	}

	public void setObjectionComments(String objectionComments) {
		this.objectionComments = objectionComments;
	}

	public String getObjectionComments() {
		return objectionComments;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setCharge(String charge) {
		this.charge = charge;
	}

	public String getCharge() {
		return charge;
	}

	public void setDateTransaction(Timestamp dateTransaction) {
		this.dateTransaction = dateTransaction;
	}

	public Timestamp getDateTransaction() {
		return dateTransaction;
	}

	public void setValueAjust(Long valueAjust) {
		this.valueAjust = valueAjust;
	}

	public Long getValueAjust() {
		return valueAjust;
	}

	public void setModalDetailObjection(boolean modalDetailObjection) {
		this.modalDetailObjection = modalDetailObjection;
	}

	public boolean isModalDetailObjection() {
		return modalDetailObjection;
	}

	public void setDetailTransaction(boolean detailTransaction) {
		this.detailTransaction = detailTransaction;
	}

	public boolean isDetailTransaction() {
		return detailTransaction;
	}

	public void setConc(String conc) {
		this.conc = conc;
	}

	public String getConc() {
		return conc;
	}

	public void setEsta(String esta) {
		this.esta = esta;
	}

	public String getEsta() {
		return esta;
	}

	public void setCarr(String carr) {
		this.carr = carr;
	}

	public String getCarr() {
		return carr;
	}

	public void setObserv(String observ) {
		this.observ = observ;
	}

	public String getObserv() {
		return observ;
	}

	public void setShowError(boolean showError) {
		this.showError = showError;
	}

	public boolean isShowError() {
		return showError;
	}

	public void setMsgError(String msgError) {
		this.msgError = msgError;
	}

	public String getMsgError() {
		return msgError;
	}

	public void setModalMessage(String modalMessage) {
		this.modalMessage = modalMessage;
	}

	public String getModalMessage() {
		return modalMessage;
	}

	public void setShowModal(boolean showModal) {
		this.showModal = showModal;
	}

	public boolean isShowModal() {
		return showModal;
	}

	public void detailOpen(){
		try{	
			modalDetailObjection = false;
			TbObjection obj = objection.getObjectionById(objectionId);

			accountId = obj.getAccountId().getAccountId();
			charge = obj.getCharge().getChargeDescription();
			dateTransaction = obj.getDateTransaction();
			detailTransaction = false;
			observ = obj.getObjection();
			if(obj.getCharge().getChargeTypeValue() == 0 ){
				if(obj.getCompanyId()!=null && obj.getCompanyId()>0){
					conc = objection.getConcesionById(obj.getCompanyId()).getCompanyName();
				}else{
					conc = "-";
				}
				if(obj.getStationId()!=null && obj.getStationId()>0){
					esta = objection.getStationById(obj.getStationId()).getStationBOName();
				}else{
					esta = "-";
				}
				if(obj.getLaneId()!=null && obj.getLaneId()>0){
					carr = objection.getCarrilById(obj.getLaneId()).getLaneName();
				}else{
					carr = "-";
				}			
				valueAjust = null;
				detailTransaction = true;
			}else {
				detailTransaction = false;
				valueAjust = obj.getCharge().getChargeValue();
				DecimalFormat nf = new DecimalFormat("#,###,###,###");
				valueAjustText = nf.format(valueAjust);
			}

			modalDetailObjection = true;
		} catch(NullPointerException n){
			n.printStackTrace();
			modalDetailObjection = false;
			msgError = "Error al Consultar el Detalle de la Objeción "+objectionId;
			showError = true;
		} catch(Exception e){
			e.printStackTrace();
			modalDetailObjection = false;
			msgError = "Error al Consultar el Detalle de la Objeción "+objectionId;
			showError = true;
		}
	}

	public void msgSave(){
		showError = false;
		showModal = false;
		String descripType ="";
		if((valueAjustText.equalsIgnoreCase(null)) || (valueAjustText.equals("")) || (valueAjustText.equals("0"))){
			msgError = "Falta Digitar el Valor del Ajuste";
			showError = true;
		} else if(!Validation.isNumericPuntoYComaNoConsecutive(valueAjustText)){
			System.out.println("Caracteres invalidos.");
			msgError = "El Valor del Ajuste contiene caracteres inválidos, por favor verifique";
			showError = true;
		}else {
			valueAjust = Long.parseLong(valueAjustText.replace(".", "").replace(",", ""));
			if(typeOp == 1){
				descripType = "Reintegro";			  
			}else if (typeOp == 2){
				descripType = "Descuento";
				valueAjust = (valueAjust*-1);
				//valueAjustText = "-"+valueAjustText;
			}
			modalMessage = "¿Está Seguro de Realizar "+descripType+" por valor de "+valueAjustText+" a la Cuenta "+accountId+"?";
			showModal = true;
		}
	}

	public void hideModal(){
		showModal = false;
		this.setValueAjust(0L);
		this.setValueAjustText("");
	}

	public void hideError(){
		showError = false;
		this.setValueAjust(0L);
		this.setValueAjustText("");
	}

	public void cancelClose(){
		modalDetailObjection = false;
		this.setValueAjustText("0");
	}

	public void apply(){		
		showModal = false;
		showModalRej = false;
		if (objection.setAjustmentObjection(valueAjust, objectionId, AjustmentStateType.PENDING_FOR_APPLY, SessionUtil.ip(), SessionUtil
				.sessionUser().getUserId())){
			System.out.println("Transaccion Exitosa");
			msgError = "Transacción Exitosa";
			showError = true;
			this.getListObjection();
			modalDetailObjection = false;			
		} else {
			msgError = "Error en la Transacción";
			showError = true;
		}
	}

	public void rejected(){
		showModalRej = true;
		modalMessageRej = "¿Está seguro de Rechazar la Objeción para la Cuenta "+accountId+"?";
	}

	public void setModalMessageRej(String modalMessageRej) {
		this.modalMessageRej = modalMessageRej;
	}

	public String getModalMessageRej() {
		return modalMessageRej;
	}

	public void setShowModalRej(boolean showModalRej) {
		this.showModalRej = showModalRej;
	}

	public boolean isShowModalRej() {
		return showModalRej;
	}

	public void hideModalRej(){
		showModalRej = false;
		this.setValueAjust(0L);
		this.setValueAjustText("");
	}

	public void rechazar(){
		showError = false;
		showModal = false;
		showModalRej = false;
		if (objection.setAjustmentObjection(valueAjust, objectionId, AjustmentStateType.REJECTED, SessionUtil.ip(), SessionUtil
				.sessionUser().getUserId())){
			msgError = "Transacción Exitosa";
			showError = true;
			this.getListObjection();
			modalDetailObjection = false;			
		} else {
			msgError = "Error en la Transacción";
			showError = true;
		}
	}

	public void setObjectionId(Long objectionId) {
		this.objectionId = objectionId;
	}

	public Long getObjectionId() {
		return objectionId;
	}

	public void setValueAjustText(String valueAjustText) {
		this.valueAjustText = valueAjustText;
	}

	public String getValueAjustText() {
		return valueAjustText;
	}

	public void setTypeOp(int typeOp) {
		this.typeOp = typeOp;
	}

	public int getTypeOp() {
		typeOp = 1;
		return typeOp;
	}
}
