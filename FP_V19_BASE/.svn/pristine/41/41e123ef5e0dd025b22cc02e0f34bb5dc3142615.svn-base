package objection;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import jpa.TbAccount;
import jpa.TbCharges;
import jpa.TbCompany;
import jpa.TbLane;
import jpa.TbObjection;
import jpa.TbStationBO;
import jpa.TbSystemUser;

import ejb.Charges;
import ejb.Objection;
import ejb.User;
import ejb.crud.Company;
import ejb.crud.Station;

import security.UserLogged;
import sessionVar.SessionUtil;
import util.Objections;

public class CreateReclamationBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@EJB(mappedName="ejb/Objection")
	private Objection ObjectionEJB;
	
	@EJB(mappedName="ejb/Company")
	private Company company;
	
	@EJB(mappedName="ejb/Station")
	private Station station;
	
	@EJB(mappedName="ejb/Charges")
	private Charges chargesEjb;
	
	@EJB(mappedName="ejb/User")
	private User user;
	
	private boolean showCreate;
	
	private boolean showUpdate;
	
	private boolean showDelete;
	
	private boolean showModal;
	
	private boolean render1;
	
	private boolean showConfirmation;
	
	private String corfirmMessage;
	
	private List<Objections> reclamationsList;
	
	private Long accountId=-1L;
	
	private Date dateTransaction;
	
	private Date date;
	
	private boolean showMessage;
	
	private String message;
	
    private boolean showMessage2;
	
	private String message2;
	
    private Long laneId=0L;
	
	private Long stationId=0L;
	
	private Long companyId=0L;
	
	private Long chargeId=0L;
	
	private String objection="";
	
	private TbSystemUser tt;
	
	private Long objectionId;
	
	private List<SelectItem> lanes;
	
	private List<SelectItem> stations2;
	
	private List<SelectItem> companys;
	
	private List<SelectItem> charges;
	
	private List<SelectItem> accounts;
	
	private List<SelectItem> lanes3;
	
	private List<SelectItem> stations3;
	
	private Date newDate;
	
	private Long newAccountId=0L;
	
	private Long newChargeId=0L;
	
	private Long newCompanyId=0L;
	
	private Long newStationId=0L;
	
	private Long newLaneId=0L;
	
	private String newObjection="";
	
	private boolean showInfo;
	
	private TbObjection ob1;
	
	private String conc;
	
	private String esta;
	
	private String carr;
	
	private UserLogged usrs;
	
	private boolean showModalInsertError;
	
	private boolean showModalUpdateError;
	
	
	public CreateReclamationBean(){
		usrs = (UserLogged) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
		tt= SessionUtil.sessionUser();
	}
	
	@PostConstruct
	public void init(){
		this.getReclamationsList();
	}
	
	public void create(){
		this.setShowCreate(true);
	}
	
	public void showInf(){
		ob1=ObjectionEJB.getObjectionById(objectionId);
		if(ob1!=null){
			if(ob1.getCompanyId()!=null && ob1.getCompanyId()>0){
				conc = ObjectionEJB.getConcesionById(ob1.getCompanyId()).getCompanyName();
			}else{
				conc = "-";
			}
			if(ob1.getStationId()!=null && ob1.getStationId()>0){
				esta = ObjectionEJB.getStationById(ob1.getStationId()).getStationBOName();
			}else{
				esta = "-";
			}
			if(ob1.getLaneId()!=null && ob1.getLaneId()>0){
				carr = ObjectionEJB.getCarrilById(ob1.getLaneId()).getLaneName();
			}else{
				carr = "-";
			}
			this.setShowInfo(true);
		}
		
	}

    public void update(){
		TbObjection ob=ObjectionEJB.getObjectionById(objectionId);
		
		if(ob!=null){
			newAccountId=ob.getAccountId().getAccountId();
			newDate= ob.getDateTransaction();
			newChargeId= ob.getCharge()!=null?ob.getCharge().getChargeId():0;
			newCompanyId= ob.getCompanyId();
			newStationId=ob.getStationId();
			newLaneId= ob.getLaneId();
			newObjection= ob.getObjection();
			
			if(newCompanyId==null){
				newCompanyId=0L;
			}
			if(newStationId==null){
				newStationId=0L;
			}
			if(newLaneId==null){
				newLaneId=0L;
			}
			
			if(ob.getState()!=null){
				if(ob.getState()==0){
					if(ob.getCharge()!=null){
						if(ob.getCharge().getChargeId()==3){
							this.setRender1(false);
						}
						else{
							this.setRender1(true);
						}
						this.setShowUpdate(true);
						this.setShowMessage(false);
					}
					this.setShowUpdate(true);
					this.setShowMessage(false);
				}
				else if(ob.getState()==1){
					this.setShowUpdate(false);
					this.setMessage("La reclamación no puede ser modificada, porque ya fue aplicada por el administrador.");
					this.setShowMessage(true);
				}
				else if(ob.getState()==2){
					this.setShowUpdate(false);
					this.setMessage("La reclamación no puede ser modificada, porque fue rechazada por el administrador.");
					this.setShowMessage(true);
				}
			}
			else{
				if(ob.getCharge()!=null){
					if(ob.getCharge().getChargeId()==3){
						this.setRender1(false);
					}
					else{
						this.setRender1(true);
					}
				}
				this.setShowUpdate(true);
				this.setShowMessage(false);
			}
			
		}
		
	}

    public void updateObjection(){
    	this.setShowCreate(false);
    	this.setShowModalInsertError(false);
    	this.setShowUpdate(false);
    	this.setShowModalUpdateError(false);
    	this.setShowMessage(false);
    	Long userId=user.getSystemMasterById(usrs.getUserId());
		try{
			if(newAccountId>0L){
				if(newDate!=null){
					if(newDate.getTime()<= (new Date()).getTime()){
						if(newChargeId!=0L){
							if(!newObjection.equals("") && newObjection!=null){
								String varO= newObjection;
								if(!varO.trim().isEmpty()){
									
									if(ObjectionEJB.validateDateReclamation(newAccountId, newDate)){
										if(ObjectionEJB.updateObjection(objectionId, newDate, newObjection, tt,
												 newAccountId,SessionUtil.ip(), newChargeId, newCompanyId, newStationId, newLaneId, true)){
											this.setShowMessage(true);
											this.setMessage("Reclamación modificada correctamente asociada a la cuenta FacilPass: " + newAccountId);
											reclamationsList= ObjectionEJB.getObjectionByClient(userId);
										}
										else{
											this.setShowMessage(true);
											this.setMessage("Error al modificar reclamación");
										}
									}else{
										this.setMessage("Error: La fecha y hora de la transacción que esta ingresando es menor a la fecha y hora de creación de la cuenta.");
										this.setShowModalUpdateError(true);
									}
								
								}
								
								else{
									this.setMessage("Error. Debe escribir el motivo de la reclamación.");
									this.setShowModalUpdateError(true);
								}
							
							}
							else{
								this.setMessage("Error: Debe escribir el motivo de la reclamación.");
								this.setShowModalUpdateError(true);
							}
						
						}
						else{
							this.setMessage("Error: Debe seleccionar un tipo de cargo.");
							this.setShowModalUpdateError(true);
						}
					}
					else{
						this.setMessage("Error: La fecha y hora de la transacción no debe ser mayor a la fecha y hora actual.");
						this.setShowModalUpdateError(true);
					}
				}
				else{
					this.setMessage("Error: La fecha de transacción no debe estar vacía.");	
					this.setShowModalUpdateError(true);
				}
			
			}
			else{
				this.setMessage("Error: Debe seleccionar un número de cuenta.");
				this.setShowModalUpdateError(true);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	
    }
    
    public void delete(){
    	TbObjection ob=ObjectionEJB.getObjectionById(objectionId);
		
		if(ob!=null){
			if(ob.getState()!=null){
				if(ob.getState()==0){
					this.setCorfirmMessage("¿Está seguro que desea eliminar la reclamación?");
					this.setShowConfirmation(true);
					this.setShowMessage(false);
				}
				else if(ob.getState()==1){
					this.setShowConfirmation(false);
					this.setMessage("La reclamación no puede ser eliminada, porque ya fue aplicada por el administrador.");
					this.setShowMessage(true);
				}
				else if(ob.getState()==2){
					this.setShowConfirmation(false);
					this.setMessage("La reclamación no puede ser eliminada, porque fue rechazada por el administrador.");
					this.setShowMessage(true);
				}
			}
			else{
				this.setCorfirmMessage("¿Está seguro que desea eliminar la reclamación?");
				this.setShowConfirmation(true);
				this.setShowMessage(false);
			}
		
		}
		

    }

    public void deleteObjection(){
    	Long userId=user.getSystemMasterById(usrs.getUserId());
    	if(ObjectionEJB.deleteObjection(objectionId)){
    		this.setShowConfirmation(false);
    		this.setMessage("La reclamación fue eliminada.");
			this.setShowMessage(true);
			reclamationsList= ObjectionEJB.getObjectionByClient(userId);
    	}
    	else{
    		this.setShowConfirmation(false);
    		this.setMessage("Error en transacción");
			this.setShowMessage(true);
    	}
    	
    }
    
	/**
	 * @param reclamationsList the reclamationsList to set
	 */
	public void setReclamationsList(List<Objections> reclamationsList) {
		this.reclamationsList = reclamationsList;
	}

	/**
	 * @return the reclamationsList
	 */
	public List<Objections> getReclamationsList() {
    	Long userId=user.getSystemMasterById(usrs.getUserId());
		reclamationsList= new ArrayList<Objections>();
		reclamationsList= ObjectionEJB.getObjectionByClient(userId);
		return reclamationsList;
	}

	/**
	 * @param showCreate the showCreate to set
	 */
	public void setShowCreate(boolean showCreate) {
		this.showCreate = showCreate;
	}

	/**
	 * @return the showCreate
	 */
	public boolean isShowCreate() {
		return showCreate;
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
	 * @param accountId the accountId to set
	 */
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	/**
	 * @return the accountId
	 */
	public Long getAccountId() {
		return accountId;
	}

	/**
	 * @param dateTransaction the dateTransaction to set
	 */
	public void setDateTransaction(Date dateTransaction) {
		this.dateTransaction = dateTransaction;
	}

	/**
	 * @return the dateTransaction
	 */
	public Date getDateTransaction() {
		return dateTransaction;
	}



	/**
	 * @param laneId the laneId to set
	 */
	public void setLaneId(Long laneId) {
		this.laneId = laneId;
	}

	/**
	 * @return the laneId
	 */
	public Long getLaneId() {
		return laneId;
	}

	/**
	 * @param stationId the stationId to set
	 */
	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	/**
	 * @return the stationId
	 */
	public Long getStationId() {
		return stationId;
	}

	/**
	 * @param chargeId the chargeId to set
	 */
	public void setChargeId(Long chargeId) {
		this.chargeId = chargeId;
	}

	/**
	 * @return the chargeId
	 */
	public Long getChargeId() {
		return chargeId;
	}

	/**
	 * @param objection the objection to set
	 */
	public void setObjection(String objection) {
		this.objection = objection;
	}

	/**
	 * @return the objection
	 */
	public String getObjection() {
		return objection;
	}


	/**
	 * @param companys the companys to set
	 */
	public void setCompanys(List<SelectItem> companys) {
		this.companys = companys;
	}

	/**
	 * @return the companys
	 */
	public List<SelectItem> getCompanys() {
		companys= new ArrayList<SelectItem>();
        companys.add(new SelectItem(0L, "--Seleccione La Concesión--"));
		for(TbCompany c : company.getConcession()) {
			companys.add(new SelectItem(c.getCompanyId(), c.getCompanyName()));
		}
		return companys;
	}

	/**
	 * @param stations the stations to set
	 */
	public void setStations2(List<SelectItem> stations) {
		this.stations2 = stations;
	}

	/**
	 * @return the stations
	 */
	public List<SelectItem> getStations2() {
		stations2= new ArrayList<SelectItem>();
		if(companyId!=0L){
			List<TbStationBO> lis=station.getStationListByCompany(companyId);
			if(lis!=null){
				if(lis.size()==0){
					stations2.add(new SelectItem(-2, "No Existe información"));
				}
				else{
					stations2.add(new SelectItem(0L, "Seleccione"));
					for(TbStationBO t: lis){
						stations2.add(new SelectItem(t.getStationBOId(), t.getStationBOName()));
					}
				}
			}
			
		}
		else{
			stations2.add(new SelectItem(-1, "--Seleccione Estación--"));
		}
		return stations2;
	}
	
	/**
	 * @param lanes the lanes to set
	 */
	public void setLanes(List<SelectItem> lanes) {
		this.lanes = lanes;
	}

	/**
	 * @return the lanes
	 */
	public List<SelectItem> getLanes() {
		lanes= new ArrayList<SelectItem>();
		if(stationId!=0L && companyId!=0){
			List<TbLane> lis=station.getLaneListByStation(stationId);
			
			if(lis.size()==0 && !stationId.equals(0L)){
				lanes.add(new SelectItem(-2, "No Existe información"));
			}

			else{
				for(TbLane t: lis){
					lanes.add(new SelectItem(t.getLaneId(),t.getLaneCode()));
				}
			}
			
		}
		else{
			lanes.add(new SelectItem(-1, "--Seleccione Carril--"));
		}
		return lanes;
	}
	
	/**
	 * @param charges the charges to set
	 */
	public void setCharges(List<SelectItem> charges) {
		this.charges = charges;
	}

	/**
	 * @return the charges
	 */
	public List<SelectItem> getCharges() {
		charges= new ArrayList<SelectItem>();
		charges.add(new SelectItem(0L, "--Seleccione Cargo--"));
		for(TbCharges t: chargesEjb.getAllCharges()){
			charges.add(new SelectItem(t.getChargeId(), t.getChargeDescription()));
		}
		return charges;
	}

	/**
	 * @param accounts the accounts to set
	 */
	public void setAccounts(List<SelectItem> accounts) {
		this.accounts = accounts;
	}

	/**
	 * @return the accounts
	 */
	public List<SelectItem> getAccounts() {
    	Long userId=user.getSystemMasterById(usrs.getUserId());
		List<TbAccount> list= new ArrayList<TbAccount>();
		list = user.getClientAccount(userId);
		accounts= new ArrayList<SelectItem>();
		accounts.add(new SelectItem(-1L, "--Seleccione Cuenta--"));
		for(TbAccount t: list){
			String account="Nro Cuenta  " +  t.getAccountId().toString();
			accounts.add(new SelectItem(t.getAccountId(), account));
		}
		return accounts;
	}
	
	/**
	 * @param render1 the render1 to set
	 */
	public void setRender1(boolean render1) {
		this.render1 = render1;
	}

	/**
	 * @return the render1
	 */
	public boolean isRender1() {
		return render1;
	}

	public void validate(){
		if(chargeId!=0L){
			if(chargeId==3L){
				this.setRender1(false);
			}
			else{
				this.setCompanyId(0L);
				this.setStationId(0L);
				this.setLaneId(0L);
				this.setRender1(true);
			}
		}
	}
	
	public void validate2(){
		if(newChargeId!=0L){
			if(newChargeId==3L){
				this.setRender1(false);
			}
			else{
				this.setNewCompanyId(0L);
				this.setNewStationId(0L);
				this.setNewLaneId(0L);
				this.setRender1(true);
			}
		}
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param showMessage2 the showMessage2 to set
	 */
	public void setShowMessage2(boolean showMessage2) {
		this.showMessage2 = showMessage2;
	}

	/**
	 * @return the showMessage2
	 */
	public boolean isShowMessage2() {
		return showMessage2;
	}

	/**
	 * @param message2 the message2 to set
	 */
	public void setMessage2(String message2) {
		this.message2 = message2;
	}

	/**
	 * @return the message2
	 */
	public String getMessage2() {
		return message2;
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
	 * @param tt the tt to set
	 */
	public void setTt(TbSystemUser tt) {
		this.tt = tt;
	}

	/**
	 * @return the tt
	 */
	public TbSystemUser getTt() {
		return tt;
	}

	/**
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	/**
	 * @return the companyId
	 */
	public Long getCompanyId() {
		return companyId;
	}
	
	public String saveObjection(){
		this.setShowCreate(false);
		this.setShowModalInsertError(false);
		this.setShowMessage(false);
    	Long userId=user.getSystemMasterById(usrs.getUserId());
		date= new Timestamp(System.currentTimeMillis());
		try{
			if(accountId>0L){
				if(dateTransaction!=null){
					if(dateTransaction.getTime()<=(new Date()).getTime() ){
						if(chargeId!=0L){
							if(!objection.equals("") && objection!=null){
								String varO= objection;
								if(!varO.trim().isEmpty()){
									
									if(ObjectionEJB.validateDateReclamation(accountId, dateTransaction)){
										if(ObjectionEJB.createObjection(date, dateTransaction, objection, tt,
												 accountId,SessionUtil.ip(), chargeId, companyId, stationId, laneId, true)){
											this.setShowMessage(true);
											this.setMessage("Reclamación creada correctamente a la cuenta " + accountId);
											reclamationsList= ObjectionEJB.getObjectionByClient(userId);
										}
										else{
											this.setShowMessage(true);
											this.setMessage("Error al crear reclamación.");
										}
									}
									else{
										this.setMessage("Error: La fecha y hora de la transacción que esta ingresando es menor a la fecha y hora de creación de la cuenta.");
										this.setShowModalInsertError(true);
									}
	
								}
								else{
									this.setMessage("Error: Debe escribir el motivo de la reclamación.");
									this.setShowModalInsertError(true);
								}
							
							}
							else{
								this.setMessage("Error: Debe escribir el motivo de la reclamación.");
								this.setShowModalInsertError(true);
							}
						
						}
						else{
							this.setMessage("Error: Debe seleccionar un tipo de cargo.");
							this.setShowModalInsertError(true);
						}
					}
					else{
						this.setMessage("Error: La fecha y hora de la transacción no debe ser mayor a la fecha y hora actual.");
						this.setShowModalInsertError(true);
					}
					
				}
				else{
					this.setMessage("Error: La fecha de la transacción no puede estar vacía");
					this.setShowModalInsertError(true);
				}
			}
			else{
				this.setMessage("Error: Debe seleccionar un número de cuenta.");
				this.setShowModalInsertError(true);
			}
			
		}catch(Exception ex){
		ex.printStackTrace();	
		}
	
		return null;
	}
	

	public String hideModal(){
		this.setShowCreate(false);
		this.setShowUpdate(false);
		this.setShowDelete(false);
		this.setShowConfirmation(false);
		this.setMessage("");
		this.setShowMessage(false);
		this.setObjection("");
		this.setAccountId(-1L);
		this.setRender1(false);
		this.setLaneId(0L);
		this.setStationId(0L);
		this.setCompanyId(0L);
		this.setChargeId(0L);
		this.setShowMessage2(false);
		this.setMessage2("");
		this.setShowInfo(false);
		this.setDateTransaction(null);
		this.setShowModalInsertError(false);
		this.setShowModalUpdateError(false);
		
		return null;
	}
	
	public String hideModalInsert(){
		this.setShowModalInsertError(false);
		this.setShowMessage(false);
		this.setShowModalUpdateError(false);
		this.setShowUpdate(false);
		this.setShowCreate(true);
		this.setMessage("");
		return null;
	}
	
	public String hideModalUpdate(){
		this.setShowCreate(false);
		this.setShowModalInsertError(false);
		this.setShowModalUpdateError(false);
		this.setShowMessage(false);
		this.setShowUpdate(true);
		this.setMessage("");
		
		return null;
	}

	/**
	 * @param objectionId the objectionId to set
	 */
	public void setObjectionId(Long objectionId) {
		this.objectionId = objectionId;
	}

	/**
	 * @return the objectionId
	 */
	public Long getObjectionId() {
		return objectionId;
	}

	/**
	 * @param showUpdate the showUpdate to set
	 */
	public void setShowUpdate(boolean showUpdate) {
		this.showUpdate = showUpdate;
	}

	/**
	 * @return the showUpdate
	 */
	public boolean isShowUpdate() {
		return showUpdate;
	}

	/**
	 * @param showDelete the showDelete to set
	 */
	public void setShowDelete(boolean showDelete) {
		this.showDelete = showDelete;
	}

	/**
	 * @return the showDelete
	 */
	public boolean isShowDelete() {
		return showDelete;
	}

	/**
	 * @param newDate the newDate to set
	 */
	public void setNewDate(Date newDate) {
		this.newDate = newDate;
	}

	/**
	 * @return the newDate
	 */
	public Date getNewDate() {
		return newDate;
	}

	/**
	 * @param newAccountId the newAccountId to set
	 */
	public void setNewAccountId(Long newAccountId) {
		this.newAccountId = newAccountId;
	}

	/**
	 * @return the newAccountId
	 */
	public Long getNewAccountId() {
		return newAccountId;
	}

	/**
	 * @param newChargeId the newChargeId to set
	 */
	public void setNewChargeId(Long newChargeId) {
		this.newChargeId = newChargeId;
	}

	/**
	 * @return the newChargeId
	 */
	public Long getNewChargeId() {
		return newChargeId;
	}

	/**
	 * @param newCompanyId the newCompanyId to set
	 */
	public void setNewCompanyId(Long newCompanyId) {
		this.newCompanyId = newCompanyId;
	}

	/**
	 * @return the newCompanyId
	 */
	public Long getNewCompanyId() {
		return newCompanyId;
	}

	/**
	 * @param newStationId the newStationId to set
	 */
	public void setNewStationId(Long newStationId) {
		this.newStationId = newStationId;
	}

	/**
	 * @return the newStationId
	 */
	public Long getNewStationId() {
		return newStationId;
	}

	/**
	 * @param newLaneId the newLaneId to set
	 */
	public void setNewLaneId(Long newLaneId) {
		this.newLaneId = newLaneId;
	}

	/**
	 * @return the newLaneId
	 */
	public Long getNewLaneId() {
		return newLaneId;
	}

	/**
	 * @param newObjection the newObjection to set
	 */
	public void setNewObjection(String newObjection) {
		this.newObjection = newObjection;
	}

	/**
	 * @return the newObjection
	 */
	public String getNewObjection() {
		return newObjection;
	}

	/**
	 * @param lanes3 the lanes3 to set
	 */
	public void setLanes3(List<SelectItem> lanes3) {
		this.lanes3 = lanes3;
	}

	/**
	 * @return the lanes3
	 */
	public List<SelectItem> getLanes3() {
		lanes3= new ArrayList<SelectItem>();
		if(newStationId!=0L && newCompanyId!=0){
			List<TbLane> lis=station.getLaneListByStation(newStationId);
			
			if(lis.size()==0 && !newStationId.equals(0L)){
				lanes3.add(new SelectItem(-2, "No Existe información"));
			}

			else{
				for(TbLane t: lis){
					lanes3.add(new SelectItem(t.getLaneId(),t.getLaneCode()));
				}
			}
			
		}
		else{
			lanes3.add(new SelectItem(-1, "--Seleccione Carril--"));
		}
		return lanes3;
	}

	/**
	 * @param stations3 the stations3 to set
	 */
	public void setStations3(List<SelectItem> stations3) {
		this.stations3 = stations3;
	}

	/**
	 * @return the stations3
	 */
	public List<SelectItem> getStations3() {
		stations3= new ArrayList<SelectItem>();
		if(newCompanyId!=0L){
			List<TbStationBO> lis=station.getStationListByCompany(newCompanyId);
			if(lis!=null){
				if(lis.size()==0){
					stations3.add(new SelectItem(-2, "No Existe información"));
				}
				else{
					stations3.add(new SelectItem(0L, "Seleccione"));
					for(TbStationBO t: lis){
						stations3.add(new SelectItem(t.getStationBOId(), t.getStationBOName()));
					}
				}
			}
			
		}
		else{
			stations3.add(new SelectItem(-1, "--Seleccione Estación--"));
		}
		return stations3;
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
	 * @param showInfo the showInfo to set
	 */
	public void setShowInfo(boolean showInfo) {
		this.showInfo = showInfo;
	}

	/**
	 * @return the showInfo
	 */
	public boolean isShowInfo() {
		return showInfo;
	}

	/**
	 * @param ob1 the ob1 to set
	 */
	public void setOb1(TbObjection ob1) {
		this.ob1 = ob1;
	}

	/**
	 * @return the ob1
	 */
	public TbObjection getOb1() {
		return ob1;
	}

	/**
	 * @param conc the conc to set
	 */
	public void setConc(String conc) {
		this.conc = conc;
	}

	/**
	 * @return the conc
	 */
	public String getConc() {
		return conc;
	}

	/**
	 * @param esta the esta to set
	 */
	public void setEsta(String esta) {
		this.esta = esta;
	}

	/**
	 * @return the esta
	 */
	public String getEsta() {
		return esta;
	}

	/**
	 * @param carr the carr to set
	 */
	public void setCarr(String carr) {
		this.carr = carr;
	}

	/**
	 * @return the carr
	 */
	public String getCarr() {
		return carr;
	}

	/**
	 * @param showModalInsertError the showModalInsertError to set
	 */
	public void setShowModalInsertError(boolean showModalInsertError) {
		this.showModalInsertError = showModalInsertError;
	}

	/**
	 * @return the showModalInsertError
	 */
	public boolean isShowModalInsertError() {
		return showModalInsertError;
	}

	/**
	 * @param showModalUpdateError the showModalUpdateError to set
	 */
	public void setShowModalUpdateError(boolean showModalUpdateError) {
		this.showModalUpdateError = showModalUpdateError;
	}

	/**
	 * @return the showModalUpdateError
	 */
	public boolean isShowModalUpdateError() {
		return showModalUpdateError;
	}

}
