/**
 * 
 */
package menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.el.MethodExpression;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;

import jpa.TbSystemUser;

import mBeans.LoginMBean;

import org.richfaces.component.html.HtmlPanelMenu;
import org.richfaces.component.html.HtmlPanelMenuGroup;
import org.richfaces.component.html.HtmlPanelMenuItem;

import util.OptionActionH;
import util.OptionActions;

import ejb.Permission;


/**
 * @author tmolina
 *
 */
public class SideMenuBean implements Serializable{
	private static final long serialVersionUID = 1933800214856275567L;
	HashMap<String, Boolean> menuState=new HashMap<String, Boolean>(); 

	  
	// Attributes ----------------------------------------------------------------------------------------------------------------
	
	private TbSystemUser userLogged;
	
	private HtmlPanelMenu panelMenu;
	
	private HtmlPanelMenuGroup menuGroup;
	
	private HtmlPanelMenuGroup menuGroup2;
	
	private HtmlPanelMenuItem menuItem;
	
	private HtmlPanelMenuItem menuItem2;
	
	private List<OptionActions> optionactions;
	
	private Permission permission;
	
	private String itemSelected;
	
	private boolean showmodal;
	
	private String msgmodal;
	
	private boolean podCreate;
	
	// Constructor --------------------------------------------------------------------------------------------------------------
	
	public SideMenuBean(){	
		System.out.print("   SideMenuBean     creando Side Menu");
	 try{	
		 showmodal=false;
		LoginMBean login = (LoginMBean) FacesContext.getCurrentInstance()
		.getExternalContext().getSessionMap().get("loginMBean");
		
		if(login != null){
		
			panelMenu =  new HtmlPanelMenu();
	
			setUserLogged(login.getSystemUser());		
			try {
				Context contex = new InitialContext();
				permission = (Permission) contex.lookup("ejb/Permission");
				optionactions = new ArrayList<OptionActions>();
				optionactions = permission.getOpctionAction(userLogged.getUserId());
			} catch (NamingException e) {
				e.printStackTrace();
			}
		  } else{
			  msgmodal="Ha ocurrido un error en el sistema y se cerrará la sesión";
			  showmodal = true;
		  }
	 	} catch(NullPointerException n){
	 		 System.out.println("[ Error en SideMenuBean.SideMenuBean ]");
			 n.printStackTrace();
			 msgmodal="Ha ocurrido un error en el sistema y se cerrará la sesión";
			  showmodal = true;
		}	
	}
	
	
	private void init(){
		System.out.println("Ingreso a Init");
		try{	
			LoginMBean login = (LoginMBean) FacesContext.getCurrentInstance()
			.getExternalContext().getSessionMap().get("loginMBean");
			System.out.println("login: "+login);
			if(login != null){
			
				panelMenu =  new HtmlPanelMenu();
		
				setUserLogged(login.getSystemUser());		
				try {
					Context contex = new InitialContext();
					permission = (Permission) contex.lookup("ejb/Permission");
					optionactions = new ArrayList<OptionActions>();
					optionactions = permission.getOpctionAction(userLogged.getUserId());
				} catch (NamingException e) {
					e.printStackTrace();
				}
			  } else{
				  msgmodal="Ha ocurrido un error en el sistema y se cerrará la sesión";
				  showmodal = true;
			  }
		 	} catch(NullPointerException n){
		 		 System.out.println("[ Error en SideMenuBean.init ]");
		 		 msgmodal="Ha ocurrido un error en el sistema y se cerrará la sesión";
				  showmodal = true;
			}
	}
	
	
	// Actions -------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Creates the PanelMenu
	 */
	public String createMenu(){
		System.out.print("   createMenu     creando Side Menu");
		//System.out.println("itemSelected: "+itemSelected);
		// Creating <rich:panelMenu>
			panelMenu =  new HtmlPanelMenu();		
			panelMenu.setMode("ajax");
			panelMenu.setIconExpandedGroup("grid");
			panelMenu.setIconCollapsedGroup("grid");
			panelMenu.setIconExpandedTopGroup("chevronUp");
			panelMenu.setIconCollapsedTopGroup("chevronDown");
			panelMenu.setIconGroupTopPosition("right");	
			panelMenu.setItemStyle("font-family: 'Arial'; 	font-size: 13px;");
			panelMenu.setExpandSingle(true);
			panelMenu.setSelectedChild(itemSelected);
			System.out.println("itemSelected: "+itemSelected);
			panelMenu.setHoveredItemStyle("background-color: #7171C6; color: #ffffff;");
			
			int grupos = 0;
			//System.out.println("optionactions: "+optionactions);
			if(optionactions != null){
				System.out.println("Creo un producto bancario c: "+podCreate);
				if(podCreate == true){
					optionactions.clear();
					optionactions = permission.getOpctionAction(userLogged.getUserId());
					System.out.println("volvi a crear menu");
				}
			    for (OptionActions o : optionactions){
			    	System.out.println("lista de opciones hijas: " + o.getOption());
					// Creating <rich:panelMenuGroup>
			    		menuGroup = new HtmlPanelMenuGroup();
						menuGroup.setLabel(o.getOption());
						menuGroup.setName(o.getOption());	
						menuGroup.setId("grupo_"+o.getOption().replaceAll(" ","").replaceAll("ñ","n")
								.replaceAll("á","a")
								.replaceAll("é","e")
								.replaceAll("í","i")
								.replaceAll("ó","o")
								.replaceAll("ú","u")
								.replaceAll("Á","A")
								.replaceAll("É","E")
								.replaceAll("Í","I")
								.replaceAll("Ó","O")
								.replaceAll("Ú","U")+"_"+grupos);
						//menuGroup.setStyle("font-weight:normal;");
						menuGroup.setStyle("font-family: 'Arial'; 	font-size: 13px;");
						grupos = grupos+1;
						
						panelMenu.getChildren().add(menuGroup);
						
						int grupos2=0;
						for(OptionActionH ob: o.getOptionH()){
							System.out.println("Entre al for de los opcis hijos");
							menuGroup2 = new HtmlPanelMenuGroup();
						    menuGroup2.setLabel(ob.getOption());
						    menuGroup2.setName(ob.getOption());
						    menuGroup2.setId("grupo2_"+ob.getOption().replaceAll(" ","").replaceAll("ñ","n")
						    		.replaceAll("á","a")
									.replaceAll("é","e")
									.replaceAll("í","i")
									.replaceAll("ó","o")
									.replaceAll("ú","u")
									.replaceAll("Á","A")
									.replaceAll("É","E")
									.replaceAll("Í","I")
									.replaceAll("Ó","O")
									.replaceAll("Ú","U")+"_"+grupos2);
						    //System.out.println("gh"+grupos2+ob);
						    menuGroup2.setStyle("font-family: 'Arial'; 	font-size: 13px;");
						    System.out.println("ob.getActions().size(): "+ob.getActions().size());
						    if(ob.getActions().size()<=0){
						    	menuGroup2.setStyle("font-family: 'Arial'; 	font-size: 13px; color:#D8D8D8; ");
						       	menuGroup2.setDisabled(true);
						    }else{
						    	menuGroup2.setStyle("font-family: 'Arial'; 	font-size: 13px;");
						    }
						    menuGroup.getChildren().add(menuGroup2);
							grupos2 = grupos2+1;
							
						    int items1 =0;
							for (String oj2: ob.getActions()){
								System.out.println("Entre al for de los accis hijos");
								String[] arrays = oj2.split(",");
								
								menuItem2 = new HtmlPanelMenuItem();
								menuItem2.setLabel(arrays[0]);
								menuItem2.setName(arrays[0] + arrays[1]);
								menuItem2.setId("g"+grupos2+"_item_"+arrays[0].replaceAll(" ","").replaceAll("ñ","n")
										.replaceAll("á","a")
										.replaceAll("é","e")
										.replaceAll("í","i")
										.replaceAll("ó","o")
										.replaceAll("ú","u")
										.replaceAll("Á","A")
										.replaceAll("É","E")
										.replaceAll("Í","I")
										.replaceAll("Ó","O")
										.replaceAll("Ú","U")+"_"+items1);
//								System.out.println("g"+grupos2+"_item_"+arrays[0].replaceAll(" ","").replaceAll("ñ","n")
//										.replaceAll("á","a")
//										.replaceAll("é","e")
//										.replaceAll("í","i")
//										.replaceAll("ó","o")
//										.replaceAll("ú","u")
//										.replaceAll("Á","A")
//										.replaceAll("É","E")
//										.replaceAll("Í","I")
//										.replaceAll("Ó","O")
//										.replaceAll("Ú","U")+"_"+items1);
								items1 = items1+1;
								menuItem2.setActionExpression(createActionExpression(arrays[2], String.class));		
								//menuItem2.setStyle("text-decoration: underline;");
								menuGroup.setStyle("font-family: 'Arial'; 	font-size: 13px;");
								menuGroup2.getChildren().add(menuItem2);
							}	
						}
						
						
						int items =0;
						for (String oj: o.getActions()){
							String[] array = oj.split(",");
							
							// Creating <rich:panelMenuItem>
							if(array[0]!=null){
								if(!array[0].equals("index")){
									menuItem = new HtmlPanelMenuItem();
									menuItem.setLabel(array[0]);
									menuItem.setName(array[0] + array[1]);
									menuItem.setId("g"+grupos+"_item_"+array[0].replaceAll(" ","").replaceAll("ñ","n")
											.replaceAll("á","a")
											.replaceAll("é","e")
											.replaceAll("í","i")
											.replaceAll("ó","o")
											.replaceAll("ú","u")
											.replaceAll("Á","A")
											.replaceAll("É","E")
											.replaceAll("Í","I")
											.replaceAll("Ó","O")
											.replaceAll("Ú","U")+"_"+items);
//									System.out.println("g"+grupos+"_item_"+array[0].replaceAll(" ","").replaceAll("ñ","n")
//											.replaceAll("á","a")
//											.replaceAll("é","e")
//											.replaceAll("í","i")
//											.replaceAll("ó","o")
//											.replaceAll("ú","u")
//											.replaceAll("Á","A")
//											.replaceAll("É","E")
//											.replaceAll("Í","I")
//											.replaceAll("Ó","O")
//											.replaceAll("Ú","U")+"_"+items);
									items = items+1;
									menuItem.setActionExpression(createActionExpression(array[2], String.class));							
									menuGroup.getChildren().add(menuItem);
								}
							}

						}		    	
			    }
			    return null;
			}else{
				return "successOut";
			}
			
	}
	
	/**
	 * Creates a method expression.
	 * @param actionExpression
	 * @param returnType
	 * @return
	 */
	private MethodExpression createActionExpression(String actionExpression, Class<?> returnType) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getApplication().getExpressionFactory().createMethodExpression(
            facesContext.getELContext(), actionExpression, returnType, new Class[0]);
    }
	
	// Setters and Getters -------------------------------------------------------------------------------------------------------
	
	/**
	 * @param panelMenu the panelMenu to set
	 */
	public void setPanelMenu(HtmlPanelMenu panelMenu) {
		this.panelMenu = panelMenu;
	}

	/**
	 * @return the panelMenu
	 */
	public HtmlPanelMenu getPanelMenu() {
	 try{
		 removeBeanFromSession();
		 System.out.println("panelMenu: "+panelMenu);
		 if(panelMenu == null){
			 createMenu();
		 } else if (panelMenu.getChildren().size() > 0){
			 System.out.println("menu construido");			 
		 } else {
			 createMenu();
		 }
		 
		return panelMenu;
	 } catch(NullPointerException n){
		 System.out.println("[ Error en SideMenuBean.getPanelMenu ]");
		 n.printStackTrace();
		 msgmodal="Ha ocurrido un error en el sistema y se cerrará la sesión";
		  showmodal = true;
		 return null;
	 }
	}
	
	public void removeBeanFromSession(){
		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().remove("formInsBean");
		context.getExternalContext().getSessionMap().remove("dailyConciliationBean");
		context.getExternalContext().getSessionMap().remove("managementNotificationsAdmin");
		context.getExternalContext().getSessionMap().remove("managementNotificationsClient");
		context.getExternalContext().getSessionMap().remove("emailAdmin");
		context.getExternalContext().getSessionMap().remove("emailSettings");
		context.getExternalContext().getSessionMap().remove("associationPaymentMethodBean");
		context.getExternalContext().getSessionMap().remove("accountClient");
		context.getExternalContext().getSessionMap().remove("createBankAccountAdmin");
		context.getExternalContext().getSessionMap().remove("adminTagsBean");
		context.getExternalContext().getSessionMap().remove("validateDocumentBean");
		context.getExternalContext().getSessionMap().remove("reconciliationReportBean");
		context.getExternalContext().getSessionMap().remove("reporAdminDeviceBean");
		context.getExternalContext().getSessionMap().remove("activeDeviceBean");
		context.getExternalContext().getSessionMap().remove("activeDeviceAdminBean");
		context.getExternalContext().getSessionMap().remove("disableReAccountDevice");
		context.getExternalContext().getSessionMap().remove("showDevices");
		context.getExternalContext().getSessionMap().remove("disableReAccountDevice");
		context.getExternalContext().getSessionMap().remove("accountDeviceBean");
		context.getExternalContext().getSessionMap().remove("lowBalanceBean");
		context.getExternalContext().getSessionMap().remove("lowBalanceAdminBean");
		context.getExternalContext().getSessionMap().remove("rechargeClient");
		context.getExternalContext().getSessionMap().remove("minimumBalanceBean");
		context.getExternalContext().getSessionMap().remove("homeBean");
		context.getExternalContext().getSessionMap().remove("chartBean");
		context.getExternalContext().getSessionMap().remove("adminVehicle");
		context.getExternalContext().getSessionMap().remove("reportVehicleBeanAdmin");
		context.getExternalContext().getSessionMap().remove("adminVehicleAdmin");
		context.getExternalContext().getSessionMap().remove("textEditorManager");
		context.getExternalContext().getSessionMap().remove("dataPoliciesBean");
		context.getExternalContext().getSessionMap().remove("contractClients");
		context.getExternalContext().getSessionMap().remove("electronicRechargeBean");
		context.getExternalContext().getSessionMap().remove("createObjection");
		context.getExternalContext().getSessionMap().remove("passwordsManagementBean");
		context.getExternalContext().getSessionMap().remove("chargeMassiveTagsBean");
		context.getExternalContext().getSessionMap().remove("invoiceReportByPlateBeanAdmin");
		context.getExternalContext().getSessionMap().remove("formInsUserClientBean");
		context.getExternalContext().getSessionMap().remove("clientProcessBean");
		context.getExternalContext().getSessionMap().remove("myProcessBean");
		context.getExternalContext().getSessionMap().remove("informationBalanceBean");
		context.getExternalContext().getSessionMap().remove("systemParameters");
		context.getExternalContext().getSessionMap().remove("informationBalanceAccount");
		context.getExternalContext().getSessionMap().remove("adminChannel");
		context.getExternalContext().getSessionMap().remove("agreementChanel");
		context.getExternalContext().getSessionMap().remove("agreementsManagementBean");
		context.getExternalContext().getSessionMap().remove("accountNote");
		context.getExternalContext().getSessionMap().remove("fileUploadAdminBean");
		context.getExternalContext().getSessionMap().remove("accountProcessBean");	
		context.getExternalContext().getSessionMap().remove("fileDownloadBean");
		context.getExternalContext().getSessionMap().remove("automaticProgrammingEntitiesBean");
		context.getExternalContext().getSessionMap().remove("confAutomaticProgramming");
		
	}
	
	/**
	 * @param userLogged the userLogged to set
	 */
	public void setUserLogged(TbSystemUser userLogged) {
		this.userLogged = userLogged;
	}

	/**
	 * @return the userLogged
	 */
	public TbSystemUser getUserLogged() {
		return userLogged;
	}

	/**
	 * @param optionactions the optionactions to set
	 */
	public void setOptionactions(List<OptionActions> optionactions) {
		this.optionactions = optionactions;
	}

	/**
	 * @return the optionactions
	 */
	public List<OptionActions> getOptionactions() {
		return optionactions;
	}
	
	public Map getState() {
        return menuState;
    }

	public void setItemSelected(String itemSelected) {
		this.itemSelected = itemSelected;
	}

	public String getItemSelected() {
		return itemSelected;
	} 
	
	public String logout() {
		showmodal=false;
		System.out.println("salida slidemenu");
		FacesContext context = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) context.getExternalContext()
				.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		return "successOut";
	}


	public void setShowmodal(boolean showmodal) {
		this.showmodal = showmodal;
	}


	public boolean isShowmodal() {
		return showmodal;
	}


	public void setMsgmodal(String msgmodal) {
		this.msgmodal = msgmodal;
	}


	public String getMsgmodal() {
		return msgmodal;
	}


	public void setPodCreate(boolean podCreate) {
		this.podCreate = podCreate;
	}


	public boolean isPodCreate() {
		return podCreate;
	}
}