package process.conciliation;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import jpa.TbCodeType;
import jpa.TbDailyConcDetail;
import jpa.TbDailyConciliation;
import sessionVar.SessionUtil;
import validator.Validation;
import ejb.Conciliation;
import ejb.User;

public class DailyConciliationBean implements Serializable{

	private static final long serialVersionUID = 1L;

	@EJB(mappedName="ejb/User")
	private User userEjb;
	
	@EJB(mappedName="ejb/Conciliation")
	private Conciliation conciliationEjb;

	private Long typeId=-1L;

	private String codClient="";

	private String nomClient="";

	private String apeClient="";

	private String user="";

	private List<SelectItem> typeList;

	private String message;

	private String corfirmMessage;

	private int scrollerPage=1;

	private boolean showModalError;

	private boolean showConfirmation;

	private boolean showConfirmation2;

	private boolean showConfirmation3;

	private boolean showModal;

	private boolean showModalError2;

	private boolean showSummary=false;

	private boolean showClients=false;

	private Date fechaCierre;

	private String nameUser="";
	
	private String systemDate="";
	
	private String sFechaCierre="";
	
	private Long valueConc=0L;
	
	private String valueConcTxt="";

	private Long valueCash=0L;
	
	private String valueCashTxt="";
	
	private Long valueDiff=0L;
	
	private String valueDiffTxt="";
	
	private List<TbDailyConcDetail> dailyConcDetails;
	
	private Long dailyConcId=0L;
	
	private Integer numberClients=0;
	
	private String observation="";
	
	private boolean showAlterConc=false;
	
	private boolean permOpenConc=false;
	
	private boolean permManageConc=false;
	
	private boolean permUpdateConc=false;
	
	private String textDiffTxt="";
	
	public DailyConciliationBean(){
		setShowModalError(false);
		setSystemDate(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
	}

	@SuppressWarnings("unused")
	@PostConstruct
	private void init() {
		//TODO post constructor
		setFechaCierre(new Date());
		setSystemDate(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
		permOpenConc=userEjb.getPermission(SessionUtil.sessionUser().getUserId(), "dailyConcOpen");
		permManageConc=userEjb.getPermission(SessionUtil.sessionUser().getUserId(), "dailyConcManage");
		permUpdateConc=userEjb.getPermission(SessionUtil.sessionUser().getUserId(), "dailyConcUpdate");
	}

	public void ocult(){
		this.setShowModalError(false);
		this.setShowModalError2(false);
	}

	public void ocult1(){
		try{
			this.setShowSummary(false);
			this.setShowModalError(false);
			this.setShowModalError2(false);
		}catch(java.util.ConcurrentModificationException ex){
			System.out.println("Entre al error ConcurrentModificationException");
			ex.printStackTrace();
		}

	}

	public String search(){
		this.setDailyConcId(0L);
		this.setNameUser("");
		this.setValueConcTxt("");
		this.setValueConc(0L);
		this.setValueCashTxt("");
		this.setValueCash(0L);
		this.setValueDiff(0L);
		this.setValueDiffTxt("");
		this.setNumberClients(0);
		this.setShowClients(false);
		this.setShowSummary(false);
		this.setShowModalError(false);
		this.setShowConfirmation(false);
		this.setShowConfirmation2(false);
		this.setShowConfirmation3(false);
		this.setCorfirmMessage("");
		this.setObservation("");
		this.setShowAlterConc(false);

		System.out.println("typeId: " + typeId);
		System.out.println("codClient: " + codClient);
		System.out.println("nomClient: " + nomClient);
		System.out.println("apeClient: " + apeClient);
		System.out.println("user: " + user);
		System.out.println("fechaCierre: " + fechaCierre);

		if(!codClient.equals("") && !Validation.isNumeric(codClient)){
			this.setMessage("El campo No. Identificación solo debe contener números.");
			this.setShowModalError(true);
		}
		else if (!nomClient.equals("") && !Validation.isAlphaNumCompany2(nomClient)){
			this.setMessage("El campo Nombre tiene caracteres inválidos.");
			this.setShowModalError(true);
		}
		else if(!apeClient.equals("") && !Validation.apellidoCliente(apeClient)){
			this.setMessage("El campo Apellidos tiene caracteres inválidos.");
			this.setShowModalError(true);
		}
		else if(user!="" && !Validation.isEmail2(user)){
			this.setMessage("El campo Usuario tiene caracteres inválidos.");
			this.setShowModalError(true);
		}
		else if(fechaCierre==null||fechaCierre.equals("")){
			this.setMessage("El campo Fecha de Cierre es obligatorio.");
			this.setShowModalError(true);
		}
		else{
			if(permManageConc==false){
				this.setDailyConcId(conciliationEjb.getDailyConcByDate(SessionUtil.sessionUser().getUserId(), fechaCierre));
			}else{
				this.setDailyConcId(conciliationEjb.getDailyConcByFilters(typeId, codClient, nomClient, apeClient, user, fechaCierre));
			}
			System.out.println("search.dailyConcId: "+dailyConcId);
			if(dailyConcId==0L){
				this.setMessage("No se encontraron resultados para la búsqueda.");
				this.setShowModalError(true);
			}else if(dailyConcId==-1L){
				this.setMessage("Ocurrió un error al momento de realizar la búsqueda.");
				this.setShowModalError(true);
			}else if(dailyConcId==-2L){
				this.setMessage("Hay más de un resultado en la consulta, por favor agregue otro campo en el filtro.");
				this.setShowModalError(true);
			}else{
				TbDailyConciliation c=conciliationEjb.getDailyConc(dailyConcId);
				if(c!=null){
					this.setShowModalError(false);
					this.setShowClients(true);
					this.setShowSummary(true);
					setNameUser(c.getUserId().getUserNames()+" "+c.getUserId().getUserSecondNames());
					setsFechaCierre(new SimpleDateFormat("dd/MM/yyyy").format(fechaCierre));
					setValueConcTxt("$"+new DecimalFormat("#,###,###,###").format(c.getDailyConcBalance()));
					setValueConc(c.getDailyConcBalance());
					setValueCashTxt(new DecimalFormat("#,###,###,###").format(c.getDailyConcEffective()));
					setValueCash(c.getDailyConcEffective());
					setValueDiff(conciliationEjb.calculateDiffDailyConc(valueConc, valueCash));
					setValueDiffTxt("$"+new DecimalFormat("#,###,###,###").format(valueDiff));
					if(valueDiff!=0L){
						this.setTextDiffTxt("SI");
					}else{
						this.setTextDiffTxt("NO");
					}
					this.getDailyConcDetails();
					setNumberClients(dailyConcDetails.size());
				}else{
					System.out.println("Error null");
					this.setMessage("Ocurrió un error al momento de realizar la búsqueda.");
					this.setShowModalError(true);
				}
			}
		}
		return "";
	}
	
	public String preCloseDaily(){
		if(conciliationEjb.getDailyConcStateById(dailyConcId)!=1L){
			this.setMessage("La conciliación ya se encuentra en estado cerrada, por favor verifique.");
			this.setShowModal(true);
		}else{
			if((valueCashTxt.equals("")) || (valueCashTxt.equals(null))){
				valueCash = 0L;
			} else{
				valueCash = Long.parseLong(valueCashTxt.replace(".", "").replace(",", ""));
			}
			System.out.println("preCloseDaily.valueCash: "+valueCash);
			setValueDiff(conciliationEjb.calculateDiffDailyConc(valueConc, valueCash));
			setValueDiffTxt("$"+new DecimalFormat("#,###,###,###").format(valueDiff));
			if(valueDiff!=0L){
				this.setTextDiffTxt("SI");
			}else{
				this.setTextDiffTxt("NO");
			}
			conciliationEjb.createLogDailyConc(dailyConcId, 1L, valueCash, "", 
					SessionUtil.sessionUser().getUserId(), SessionUtil.ip());
		}
		return null;
	}
	
	public String closeDaily(){
		if(conciliationEjb.getDailyConcStateById(dailyConcId)==3L||conciliationEjb.getDailyConcStateById(dailyConcId)==2L){
			//TODO validar mensaje
			this.setMessage("La conciliación ya se encuentra en estado cerrada, por favor verifique.");
			this.setShowModal(true);
		}else{
			if((valueCashTxt.equals("")) || (valueCashTxt.equals(null))){
				valueCash = 0L;
			} else{
				valueCash = Long.parseLong(valueCashTxt.replace(".", "").replace(",", ""));
			}
			System.out.println("closeDaily.valueCash: "+valueCash);
			setValueDiff(conciliationEjb.calculateDiffDailyConc(valueConc, valueCash));
			setValueDiffTxt("$"+new DecimalFormat("#,###,###,###").format(valueDiff));
			if(valueDiff==0L){
				setCorfirmMessage("Conciliación exitosa, se realizará el cierre.");
				this.setTextDiffTxt("NO");
			}else{
				this.setTextDiffTxt("SI");
				setCorfirmMessage("Tiene una diferencia, ¿está seguro de realizar el cierre?.");
			}
			this.setShowConfirmation(true);
		}
		return null;
	}
	
	public String confirmationCloseDaily(){
		String mensaje="";
		hideModal();
		if(conciliationEjb.updateStateDailyConc(dailyConcId, 2L, valueCash, 
				SessionUtil.ip(), SessionUtil.sessionUser().getUserId())){
			mensaje="La conciliación fue registrada en el sistema y se realizó el cierre.";
		}else{
			mensaje="Se presentó un error y no fue posible realizar el cierre, intente nuevamente " +
					"y si persiste el error comuníquese con el administrador.";
		}
		conciliationEjb.createLogDailyConc(dailyConcId, 2L, valueCash, "", 
				SessionUtil.sessionUser().getUserId(), SessionUtil.ip());
		this.reset();
		this.setMessage(mensaje);
		this.setShowModal(true);
		return null;
	}
	
	public String saveChange(){
		if(conciliationEjb.getDailyConcStateById(dailyConcId)==1L){
			//TODO validar mensaje
			this.setMessage("La conciliación se encuentra en estado sin cerrar, por favor verifique.");
			this.setShowModal(true);
		}else if(conciliationEjb.getDailyConcStateById(dailyConcId)==3L){
			//TODO validar mensaje
			this.setMessage("La conciliación ya se encuentra en estado modificada, por favor verifique.");
			this.setShowModal(true);
		}else{
			if((valueCashTxt.equals("")) || (valueCashTxt.equals(null))){
				valueCash = 0L;
			} else{
				valueCash = Long.parseLong(valueCashTxt.replace(".", "").replace(",", ""));
			}
			System.out.println("saveChange.valueCash: "+valueCash);
			setValueDiff(conciliationEjb.calculateDiffDailyConc(valueConc, valueCash));
			setValueDiffTxt("$"+new DecimalFormat("#,###,###,###").format(valueDiff));
			if(valueDiff>0L){
				this.setCorfirmMessage("Tiene una diferencia de "+valueDiffTxt+" por debajo del valor recaudado, " +
						"¿está seguro de realizar el cierre?.");
			}else if(valueDiff<0L){
				String diffAux="$"+new DecimalFormat("#,###,###,###").format(valueDiff*(-1L));
				this.setCorfirmMessage("Tiene una diferencia de "+diffAux+" por encima del valor recaudado," +
						" ¿está seguro de realizar el cierre?.");
			}else{
				this.setCorfirmMessage("Conciliación exitosa, se realizará el cierre.");
			}
			this.setShowConfirmation2(true);
		}
		return null;
	}
	
	public String showSaveChange(){
		this.setShowModalError(false);
		this.setShowModal(false);
		this.setShowModalError2(false);
		this.setShowConfirmation(false);
		this.setShowConfirmation2(false);
		this.setShowConfirmation3(false);
		this.setMessage("");
		this.setCorfirmMessage("");
		this.setShowAlterConc(true);
		return null;
	}
	
	public String alterDailyConc(){
		System.out.println("alterDailyConc.valueCash: "+valueCash);
		if(observation==null||observation.trim().equals("")){
			this.setMessage("El campo observación es" +
					" requerido," +
					" por favor verifique.");
			this.setShowModal(true);
		}else{
			if(observation.length()>200){
				this.setMessage("El campo observación " +
						"debe tener menos de 200 caracteres.");
				this.setShowModal(true);
			}else{
				String mensaje="";
				hideModal();
				if(conciliationEjb.updateStateDailyConc(dailyConcId, 3L, valueCash, 
						SessionUtil.ip(), SessionUtil.sessionUser().getUserId())){
					//TODO validar mensaje
					mensaje="La conciliación fue registrada en el sistema y se realizó la modificación.";
				}else{
					//TODO validar mensaje
					mensaje="Se presentó un error y no fue posible realizar la modificación, intente nuevamente " +
							"y si persiste el error comuníquese con el administrador.";
				}
				conciliationEjb.createLogDailyConc(dailyConcId, 3L, valueCash, observation, 
						SessionUtil.sessionUser().getUserId(), SessionUtil.ip());
				this.reset();
				this.setMessage(mensaje);
				this.setShowModal(true);
			}
		}
		return null;
	}
	
	public String openDailyConc(){
		try{
			Date systemD=new SimpleDateFormat("dd/MM/yyyy").parse(systemDate);
			Date closeD=new SimpleDateFormat("dd/MM/yyyy").parse(sFechaCierre);
			System.out.println("systemD: "+systemD+" -- closeD: "+closeD);
			if(systemD.equals(closeD)){
				if(conciliationEjb.getDailyConcStateById(dailyConcId)==1L){
					this.setMessage("Solo se puede abrir una conciliación que se encuentre cerrada.");
					this.setShowModal(true);
				}else{
					this.setCorfirmMessage("¿Está seguro de realizar la apertura de la conciliación?.");
					this.setShowConfirmation3(true);
				}
			}else{
				this.setMessage("La fecha de conciliación que se quiere abrir " +
						"tiene que ser igual que la fecha del sistema.");
				this.setShowModal(true);
			}
		}catch (Exception e) {
			System.out.println(" [ Error en DailyConciliationBean.openDailyConc ] ");
			e.printStackTrace();
			this.setMessage("Se presentó un error y no fue posible realizar la apertura, intente nuevamente " +
					"y si persiste el error comuníquese con el administrador.");
			this.setShowModal(true);
		}
		return null;
	}
	
	public String confirmationOpenDailyConc(){
		String mensaje="";
		hideModal();
		if(conciliationEjb.updateStateDailyConc(dailyConcId, 4L, valueCash, 
				SessionUtil.ip(), SessionUtil.sessionUser().getUserId())){
			//TODO validar mensaje
			mensaje="La conciliación ya se encuentra abierta, por favor verifique.";
		}else{
			//TODO validar mensaje
			mensaje="Se presentó un error y no fue posible realizar la apertura, intente nuevamente " +
					"y si persiste el error comuníquese con el administrador.";
		}
		conciliationEjb.createLogDailyConc(dailyConcId, 4L, valueCash, observation, 
				SessionUtil.sessionUser().getUserId(), SessionUtil.ip());
		this.reset();
		this.setMessage(mensaje);
		this.setShowModal(true);
		return null;
	}

	public String reset(){
		this.setShowClients(false);
		this.setTypeId(-1L);
		this.setCodClient("");
		this.setNomClient("");
		this.setApeClient("");
		this.setUser("");
		this.setDailyConcId(0L);
		this.setShowSummary(false);
		this.setShowModalError(false);
		this.setShowModalError2(false);
		this.setFechaCierre(new Date());
		this.setSystemDate(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
		this.setObservation("");
		this.setShowAlterConc(false);
		return "";	
	}

	public void hideModal(){
		this.setShowModalError(false);
		this.setShowModal(false);
		this.setShowModalError2(false);
		this.setShowConfirmation(false);
		this.setShowConfirmation2(false);
		this.setShowConfirmation3(false);
		this.setMessage("");
		this.setCorfirmMessage("");
		this.setShowAlterConc(false);
	}

	public void hideModal1(){
		this.setShowModal(false);
		this.setShowModalError(false);
		this.setShowModalError2(false);
		this.setShowConfirmation(false);
		this.setShowConfirmation2(false);
		this.setShowConfirmation3(false);
		this.setMessage("");
		this.setCorfirmMessage("");
		this.setScrollerPage(1);
	}

	public void hideModal2(){
		this.setShowModalError2(false);
		this.setShowModal(false);
		this.setShowModalError(false);
		this.setMessage("");
	}

	public void cancelar(){
		this.setCorfirmMessage("");
		this.setShowConfirmation(false);
		this.setShowConfirmation2(false);
		this.setShowConfirmation3(false);
		this.setShowAlterConc(false);
		this.setMessage("La acción ha sido cancelada.");
		this.setShowModalError(true);
	}

	public void setShowConfirmation3(boolean showConfirmation3) {
		this.showConfirmation3 = showConfirmation3;
	}

	public boolean isShowConfirmation3() {
		return showConfirmation3;
	}

	public void setShowSummary(boolean showSummary) {
		this.showSummary = showSummary;
	}

	public boolean isShowSummary() {
		return showSummary;
	}

	public boolean isShowClients() {
		return showClients;
	}

	public void setShowClients(boolean showClients) {
		this.showClients = showClients;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param showModalError the showModalError to set
	 */
	public void setShowModalError(boolean showModalError) {
		this.showModalError = showModalError;
	}

	/**
	 * @return the showModalError
	 */
	public boolean isShowModalError() {
		return showModalError;
	}

	/**
	 * @param corfirmMessage the corfirmMessage to set
	 */
	public void setCorfirmMessage(String corfirmMessage) {
		this.corfirmMessage = corfirmMessage;
	}

	/**
	 * @return the corfirmMessage
	 */
	public String getCorfirmMessage() {
		return corfirmMessage;
	}

	/**
	 * @param showConfirmation the showConfirmation to set
	 */
	public void setShowConfirmation(boolean showConfirmation) {
		this.showConfirmation = showConfirmation;
	}

	/**
	 * @return the showConfirmation
	 */
	public boolean isShowConfirmation() {
		return showConfirmation;
	}

	/**
	 * @param showConfirmation2 the showConfirmation2 to set
	 */
	public void setShowConfirmation2(boolean showConfirmation2) {
		this.showConfirmation2 = showConfirmation2;
	}

	/**
	 * @return the showConfirmation2
	 */
	public boolean isShowConfirmation2() {
		return showConfirmation2;
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
	 * @param scrollerPage the scrollerPage to set
	 */
	public void setScrollerPage(int scrollerPage) {
		this.scrollerPage = scrollerPage;
	}

	/**
	 * @return the scrollerPage
	 */
	public int getScrollerPage() {
		return scrollerPage;
	}

	/**
	 * @param showModalError2 the showModalError2 to set
	 */
	public void setShowModalError2(boolean showModalError2) {
		this.showModalError2 = showModalError2;
	}

	/**
	 * @return the showModalError2
	 */
	public boolean isShowModalError2() {
		return showModalError2;
	}

	/**
	 * @param typeId the typeId to set
	 */
	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	/**
	 * @return the typeId
	 */
	public Long getTypeId() {
		return typeId;
	}

	/**
	 * @param codClient the codClient to set
	 */
	public void setCodClient(String codClient) {
		this.codClient = codClient;
	}

	/**
	 * @return the codClient
	 */
	public String getCodClient() {
		return codClient;
	}

	/**
	 * @param nomClient the nomClient to set
	 */
	public void setNomClient(String nomClient) {
		this.nomClient = nomClient;
	}

	/**
	 * @return the nomClient
	 */
	public String getNomClient() {
		return nomClient;
	}

	/**
	 * @param apeClient the apeClient to set
	 */
	public void setApeClient(String apeClient) {
		this.apeClient = apeClient;
	}

	/**
	 * @return the apeClient
	 */
	public String getApeClient() {
		return apeClient;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param typeList the typeList to set
	 */
	public void setTypeList(List<SelectItem> typeList) {
		this.typeList = typeList;
	}

	/**
	 * @return the typeList
	 */
	public List<SelectItem> getTypeList() {
		if(typeList==null){
			typeList = new ArrayList<SelectItem>();
			typeList.add(new SelectItem(-1L,"--Seleccione Uno--"));
			for (TbCodeType type : userEjb.getCodeTypes()) {
				typeList.add(new SelectItem(type.getCodeTypeId(), type.getCodeTypeAbbreviation()));
			}
		}
		return typeList;
	}

	public Date getFechaCierre() {
		return fechaCierre;
	}

	public void setFechaCierre(Date fechaCierre) {
		this.fechaCierre = fechaCierre;
	}

	public String getNameUser() {
		return nameUser;
	}

	public void setNameUser(String nameUser) {
		this.nameUser = nameUser;
	}

	public Long getDailyConcId() {
		return dailyConcId;
	}

	public void setDailyConcId(Long dailyConcId) {
		this.dailyConcId = dailyConcId;
	}

	public List<TbDailyConcDetail> getDailyConcDetails() {
		dailyConcDetails=conciliationEjb.getDailyConcDetail(dailyConcId);
		return dailyConcDetails;
	}

	public void setDailyConcDetails(List<TbDailyConcDetail> dailyConcDetails) {
		this.dailyConcDetails = dailyConcDetails;
	}

	public String getSystemDate() {
		return systemDate;
	}

	public void setSystemDate(String systemDate) {
		this.systemDate = systemDate;
	}

	public String getsFechaCierre() {
		return sFechaCierre;
	}

	public void setsFechaCierre(String sFechaCierre) {
		this.sFechaCierre = sFechaCierre;
	}

	public Long getValueCash() {
		return valueCash;
	}

	public void setValueCash(Long valueCash) {
		this.valueCash = valueCash;
	}

	public String getValueCashTxt() {
		return valueCashTxt;
	}

	public void setValueCashTxt(String valueCashTxt) {
		this.valueCashTxt = valueCashTxt;
	}

	public Long getValueDiff() {
		return valueDiff;
	}

	public void setValueDiff(Long valueDiff) {
		this.valueDiff = valueDiff;
	}

	public String getValueDiffTxt() {
		return valueDiffTxt;
	}

	public void setValueDiffTxt(String valueDiffTxt) {
		this.valueDiffTxt = valueDiffTxt;
	}

	public Long getValueConc() {
		return valueConc;
	}

	public void setValueConc(Long valueConc) {
		this.valueConc = valueConc;
	}

	public String getValueConcTxt() {
		return valueConcTxt;
	}

	public void setValueConcTxt(String valueConcTxt) {
		this.valueConcTxt = valueConcTxt;
	}

	public Integer getNumberClients() {
		return numberClients;
	}

	public void setNumberClients(Integer numberClients) {
		this.numberClients = numberClients;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public boolean isShowAlterConc() {
		return showAlterConc;
	}

	public void setShowAlterConc(boolean showAlterConc) {
		this.showAlterConc = showAlterConc;
	}

	public boolean isPermOpenConc() {
		return permOpenConc;
	}

	public void setPermOpenConc(boolean permOpenConc) {
		this.permOpenConc = permOpenConc;
	}

	public boolean isPermManageConc() {
		return permManageConc;
	}

	public void setPermManageConc(boolean permManageConc) {
		this.permManageConc = permManageConc;
	}

	public boolean isPermUpdateConc() {
		return permUpdateConc;
	}

	public void setPermUpdateConc(boolean permUpdateConc) {
		this.permUpdateConc = permUpdateConc;
	}

	public String getTextDiffTxt() {
		return textDiffTxt;
	}

	public void setTextDiffTxt(String textDiffTxt) {
		this.textDiffTxt = textDiffTxt;
	}

}