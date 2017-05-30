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
import jpa.TbCodeType;
import jpa.TbCompany;
import jpa.TbLane;
import jpa.TbStationBO;
import jpa.TbSystemUser;

import security.UserLogged;
import sessionVar.SessionUtil;

import ejb.Charges;
import ejb.Objection;
import ejb.Role;
import ejb.User;
import ejb.crud.Company;
import ejb.crud.Station;

public class CreateObjectionBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@EJB(mappedName="ejb/Objection")
	private Objection objectionEJB;

	@EJB(mappedName="ejb/User")
	private User user;
	
	@EJB(mappedName="ejb/Company")
	private Company company;
	
	@EJB(mappedName="ejb/Station")
	private Station station;
	
	@EJB(mappedName="ejb/Charges")
	private Charges chargesEjb;
	
	@EJB(mappedName="ejb/Role")
	private Role role;
	
	private Date date;
	
	private Date dateTransaction;
	
	private String objection="";
	
	private boolean showMessage;
	
	private String message;
	
	private String userCode="";
	
	private String userName="";
	
	private UserLogged usrs;
	
	private Long accountId=-1L;
	
	private List<SelectItem> accounts;
	
	private List<TbAccount> list;
	
	private Long laneId=0L;
	
	private Long stationId=0L;
	
	private Long companyId=0L;
	
	private Long chargeId=0L;
	
	private List<SelectItem> lanes;
	
	private List<SelectItem> stations;
	
	private List<SelectItem> companys;
	
	private List<SelectItem> charges;
	
	private boolean render1;
	
	private TbSystemUser tt;
	
	private boolean disable;
	
	private boolean res1;
	
	private boolean showBotton;
	
	private boolean showMessage2;
	
	private String message2;
	
	private Long codeType;
	
	private List<SelectItem> codeTypesList;
	
	private List<TbCodeType> codeTypes;
	
	public CreateObjectionBean(){
		super();
		dateTransaction= new Timestamp(System.currentTimeMillis());
		date= new Timestamp(System.currentTimeMillis());
		usrs = (UserLogged) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
		accounts= new ArrayList<SelectItem>();
		accounts.add(new SelectItem(-1L, "--Seleccione Cuenta--"));
	}
	
	@PostConstruct
	public void init(){
		tt= SessionUtil.sessionUser();
		res1=role.isClient(tt.getUserId());
	       
		if(res1==true){
			userCode= tt.getUserCode();
			userName= tt.getUserNames() + " " + tt.getUserSecondNames();
			getInf();
			disable=true;
			showBotton=false;
		}
		else{
//			userCode=null;
//			userName=null;
		    disable=false;
		    showBotton=true;
		}
		
	}
	
	public String getInf(){
		list= new ArrayList<TbAccount>();
		list = user.getClientAccount(usrs.getUserId());
		if(accounts!=null){
			accounts.clear();
			accounts.add(new SelectItem(-1L, "--Seleccione Cuenta--"));
		}
		for(TbAccount t: list){
			String account="Nro Cuenta  " +  t.getAccountId().toString();
			accounts.add(new SelectItem(t.getAccountId(), account));
		}
		return null;
	}
	
	public String getInf2(){
		accounts.clear();
		
	  if(postValidationNew()){	
		if(userCode!=null){
			if(!userCode.equals(null) && !userCode.equals("")){
				TbSystemUser tsu=user.getUserByCode(userCode, codeType);
				if(tsu!=null){
					userName=tsu.getUserNames() + " " + tsu.getUserSecondNames();
					
					System.out.println("userCode en getInf2"+userCode);
					System.out.println("codeType en getInf2"+codeType);
					list= new ArrayList<TbAccount>();
					list = user.getClientAccount(tsu.getUserId());
					if(accounts!=null){
						accounts.clear();
						accounts.add(new SelectItem(-1L, "--Seleccione Cuenta--"));
					}
					for(TbAccount t: list){
						String account="Nro Cuenta  " +  t.getAccountId().toString();
						accounts.add(new SelectItem(t.getAccountId(), account));
					}
					System.out.println("userCode en getInf2 - 2 "+userCode);
					
				}
				else{
					userName="Sin Información";
					if(accounts!=null){
						accounts.clear();
						accounts.add(new SelectItem(-1L, "--Seleccione Cuenta--"));
					}
					this.setMessage2("No se encontró información para el  cliente digitado.");
					this.setShowMessage2(true);
				}
			}
			else{
				if(accounts!=null){
					accounts.clear();
					accounts.add(new SelectItem(-1L, "--Seleccione Cuenta--"));
				}
				this.setMessage2("El campo número de identificación no debe estar vacío.");
				this.setShowMessage2(true);
			}
		}
	  }
		else{
			if(accounts!=null){
				accounts.clear();
				accounts.add(new SelectItem(-1L, "--Seleccione Cuenta--"));
			}
			this.setMessage2("El campo número de identificación no debe estar vacío.");
			this.setShowMessage2(true);
		}
		
		
		
		
		return null;
	}
	
	
	private boolean postValidationNew(){
		if(userCode!="" && (!userCode.matches("([0-9])+"))){
			this.setMessage2("El campo No. de Identificación tiene caracteres inválidos. Verifique.");
			this.setShowMessage2(true);
			return false;
		}
		return true;		
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
	
	public String saveObjection(){
		
		UserLogged usrs = (UserLogged) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
		System.out.println("usrs" + usrs.getUsername() + usrs.getUserId() + usrs.hashCode());
		System.out.println("cod user" + userCode);
		date= new Timestamp(System.currentTimeMillis());
		try{
			if(accountId>0L){
				if(chargeId!=0L){
					if(!objection.equals("")){
						if(dateTransaction!=null){
							if(dateTransaction.getTime()<= (new Date()).getTime()){
								if(objectionEJB.validateDateReclamation(accountId, dateTransaction)){
									if(objectionEJB.createObjection(date, dateTransaction, objection, tt,
											 accountId,SessionUtil.ip(), chargeId, companyId, stationId, laneId, res1)){
										this.setShowMessage(true);
										this.setMessage("Objeción creada correctamente a la cuenta " + accountId);
									}
									else{
										this.setShowMessage2(true);
										this.setMessage2("Error al crear objeción");
									}
								}
								else{
									this.setShowMessage(true);
									this.setMessage("Error: La fecha y hora de la transacción que esta ingresando es menor a la fecha y hora de creación de la cuenta.");
								}
							
							}
							else{
								this.setShowMessage2(true);
								this.setMessage2("Error: La fecha y hora de la transacción no debe ser mayor a la fecha y hora actual.");
							}
						
						}
						else{
							this.setShowMessage2(true);
							this.setMessage2("Error: La fecha de la transacción no debe estar vacía.");
						}
					
					}
					else{
						this.setShowMessage2(true);
						this.setMessage2("Error: Debe escribir el motivo de la objeción");
					}
				
				}
				else{
					this.setShowMessage2(true);
					this.setMessage2("Error: Debe seleccionar un tipo de cargo.");
				}
			}
			else{
				this.setShowMessage2(true);
				this.setMessage2("Error: Debe seleccionar un número de cuenta.");
			}
			
		}catch(Exception ex){
		ex.printStackTrace();	
		}
	
		return null;
	}
	
	public String hideModal(){
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
		this.setDateTransaction(null);
		userName="";
		userCode="";
		if(accounts!=null){
			accounts.clear();
			accounts.add(new SelectItem(-1L, "--Seleccione Cuenta--"));
		}
		return null;
	}
	
	public void hideModal2(){
		this.setShowMessage2(false);
		this.setMessage2("");
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
		String userCode = "";
		if(res1){
			userCode=tt.getUserCode();
		}
		else{
			userCode=this.userCode;
		}
		
		return userCode;
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
	 * @param accounts the accounts to set
	 */
	public void setAccounts(List<SelectItem> accounts) {
		this.accounts = accounts;
	}

	/**
	 * @return the accounts
	 */
	public List<SelectItem> getAccounts() {
		return accounts;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<TbAccount> list) {
		this.list = list;
	}

	/**
	 * @return the list
	 */
	public List<TbAccount> getList() {
		return list;
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
	 * @param stations the stations to set
	 */
	public void setStations(List<SelectItem> stations) {
		this.stations = stations;
	}

	/**
	 * @return the stations
	 */
	public List<SelectItem> getStations() {
		stations= new ArrayList<SelectItem>();
		if(companyId!=0L){
			List<TbStationBO> lis=station.getStationListByCompany(companyId);
			if(lis.size()==0){
				stations.add(new SelectItem(-2, "No Existe información"));
			}
			else{
				stations.add(new SelectItem(0L, "Seleccione"));
				for(TbStationBO t: lis){
					stations.add(new SelectItem(t.getStationBOId(), t.getStationBOName()));
				}
			}
		}
		else{
			stations.add(new SelectItem(-1, "--Seleccione Estación--"));
		}
		return stations;
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

	public void validate(){
		if(chargeId!=0L){
			if(chargeId==3L){
				this.setRender1(false);
			}
			else{
				this.setRender1(true);
			}
		}
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
	 * @param disable the disable to set
	 */
	public void setDisable(boolean disable) {
		this.disable = disable;
	}

	/**
	 * @return the disable
	 */
	public boolean isDisable() {
		return disable;
	}

	/**
	 * @param res1 the res1 to set
	 */
	public void setRes1(boolean res1) {
		this.res1 = res1;
	}

	/**
	 * @return the res1
	 */
	public boolean isRes1() {
		return res1;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		String userName = "";
		if(res1){
			userName=tt.getUserNames() + " " + tt.getUserSecondNames();
		}
		else{
			userName=this.userName;
		}
		return userName;
	}

	/**
	 * @param showBotton the showBotton to set
	 */
	public void setShowBotton(boolean showBotton) {
		this.showBotton = showBotton;
	}

	/**
	 * @return the showBotton
	 */
	public boolean isShowBotton() {
		return showBotton;
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
	 * @param codeType the codeType to set
	 */
	public void setCodeType(Long codeType) {
		this.codeType = codeType;
	}

	/**
	 * @return the codeType
	 */
	public Long getCodeType() {
		return codeType;
	}
	

	

	/**
	 * @param codeTypesList the codeTypesList to set
	 */
	public void setCodeTypesList(List<SelectItem> codeTypesList) {
		this.codeTypesList = codeTypesList;
	}

	/**
	 * @return the codeTypesList
	 */
	public List<SelectItem> getCodeTypesList() {
		if(codeTypesList==null){
			codeTypesList = new ArrayList<SelectItem>();
			for (TbCodeType type : getCodeTypes()) {
				codeTypesList.add(new SelectItem(type.getCodeTypeId(), type
						.getCodeTypeAbbreviation()));
			}
		}
		return codeTypesList;
	}

	/**
	 * @param codeTypes the codeTypes to set
	 */
	public void setCodeTypes(List<TbCodeType> codeTypes) {
		this.codeTypes = codeTypes;
	}

	/**
	 * @return the codeTypes
	 */
	public List<TbCodeType> getCodeTypes() {
		if(codeTypes == null){
		    codeTypes = new ArrayList<TbCodeType>();
	    }
	    codeTypes = user.getCodeTypes();
	    return codeTypes;
	}
	public String changeTypeDoc() {
		setShowMessage(false);
		showMessage2 = false;
		this.setUserCode("");
		this.setUserName("");
		accountId=-1L;
		accounts.clear();
		accounts.add(new SelectItem(-1L, "--Seleccione Cuenta--"));
	
		return null;
	}

}
