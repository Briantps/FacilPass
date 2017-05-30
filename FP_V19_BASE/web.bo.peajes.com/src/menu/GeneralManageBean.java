/**
 * 
 */
package menu;

import java.io.Serializable;

import javax.faces.context.FacesContext;

import crud.BankAccountBean;
import crud.BankBean;
import crud.CompanyBean;
import crud.ExemptOfficeBean;
import crud.InstallationLocationBean;
import crud.PointBean;
import crud.StationBean;
import crud.ReplacementCausesBean;
import crud.SpecialExemptTypeBean;
import crud.WarehouseCardTypeBean;
import crud.WarehouseDependencyBean;

import arecatis.ConsultOrder;
import arecatis.EditOrderBean;
import arecatis.OrderBean;
import arecatis.PurchaseBean;
import arecatis.RechargeBean;

import process.ClientProcessBean;
import process.CustomizationProcessBean;
import process.DeviceProcessBean;
import process.InclusionProcessBean;
import process.PurchaseOrderProcessBean;
import process.account.AccountDeviceBean;
import process.account.DistributionTagBean;
import process.affiliation.AccountClient;
import process.affiliation.DeviceAccount;
import process.device.AddCard;
import process.device.ConsultCard;
import process.device.EnterDevice;
import process.device.SavePayment;
import process.device.perti.AuthorizeVipsa;
import process.device.perti.ModInclusionBean;
import process.device.perti.FixMaster;
import process.device.perti.InclusionBean;
import process.device.perti.InstallationBean;
import process.device.perti.MasterBean;
import process.device.perti.PersonalizationBean;
import process.purchasing.ApproveConsignment;
import process.purchasing.ConsigBean;
import process.purchasing.ConsultConsignment;
import process.recharge.AsignPointBean;
import process.verification.VerificationBean;
import process.warehouse.EntryOrderBean;
import process.warehouse.EntryOrderConsultBean;
import process.warehouse.ParamPrechargeBean;
import process.warehouse.PrechargeBean;
import process.warehouse.UpdateEntryOrderBean;
import process.warehouse.initialization.InitializationBean;
import reportBean.TraceabilityPlateReportBean;
import telepeaje.UpTranTelepeajeBean;
import transaction.AccountTransactionBean;

import mBeans.ClientBean;
import mBeans.ListClient;
import mBeans.PermissionMBean;

/**
 * @author tmolina
 *
 */
public class GeneralManageBean implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public GeneralManageBean(){
	}
	
	/**
	 * Init -> Roles -> Asignar Permisos.
	 */
	public String initAsigPerm() {
		PermissionMBean permission = new PermissionMBean();
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.put("permission", permission);
		return "asigPermission";
	}
	
	/**
	 * Init -> Seguimiento de Proceso -> Ver Procesos Cliente.
	 */
	public String initClientProcess() {
		try {
			ClientProcessBean clientProcessBean = (ClientProcessBean) FacesContext
			.getCurrentInstance().getExternalContext().getSessionMap().get(
					"clientProcessBean");
			if(clientProcessBean != null){ 
				clientProcessBean.setShowMessage(false);
				clientProcessBean.setShowData(false);
				clientProcessBean.setCodeClient("");
				clientProcessBean.setListClients(null);
			}else{
			}
		} catch (Exception e) {
			System.out.println( " [ Error en GeneralManageBean.initClientProcess ] ");
		}
		return "viewClientProcess";
	}
	
	/**
	 * Init -> Proceso de Verificación -> Verificación de Documentos.
	 */
	public String initVerificationDocs(){
		try {
			VerificationBean verificationBean = (VerificationBean) FacesContext
			.getCurrentInstance().getExternalContext().getSessionMap().get(
					"verificationBean");
			if(verificationBean != null){ 
				verificationBean.setShowMessage(false);
				verificationBean.setShowData(false);
				verificationBean.setCodeClient("");
			}else{
			}
		} catch (Exception e) {
			System.out.println( " [ Error en GeneralManageBean.initVerificationDocs ] ");
		}
		return "docsVerification";
	}
	
	/**
	 * Init -> Clientes -> Crear Dependencia Cliente.
	 */
	public String initCreateOffice(){
		ClientBean clientBean = (ClientBean) FacesContext
		.getCurrentInstance().getExternalContext().getSessionMap().get(
				"clientBean");
		if(clientBean != null){ 
			clientBean.setShowMessage(false);
			clientBean.setShowData(false);
			clientBean.setCodeClient("");
			clientBean.hideModal();
		}
		return "createOffice";
	}
	
	/**
	 * Init -> TISC -> Orden de Pedido.
	 */
	public String initOrder(){
		OrderBean orderBean = (OrderBean) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("orderBean");
		if (orderBean != null) {
			orderBean.hideModal();
			
			// Clearing List to reload clients and data.
			orderBean.setClientNames(null);
			orderBean.setClients(null);
			orderBean.setCodeTypesList(null); 
			orderBean.setShowData(false);
			orderBean.setShowMessage(false);
			orderBean.setCodeClient(null);
			orderBean.setIdClient(-1L);
		}
		return "createOrder";
	}
	
	/**
	 * Init -> TISC -> Editar Orden de Pedido.
	 */
	public String initEditOrder(){
		EditOrderBean orderBean = (EditOrderBean) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("editOrderBean");
		if (orderBean != null) {
			orderBean.hideModal();
			
			// Clearing List to reload clients and data.
			orderBean.setOrderNumber(null);
			orderBean.setShowData(false);
			orderBean.setShowMessage(false);
		}
		return "editOrder";
	}
	
	/**
	 * Init -> TISC -> Editar Orden de Pedido.
	 */
	public String initPurchase(){
		PurchaseBean purchaseBean = (PurchaseBean) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("purchaseBean");
		if (purchaseBean != null) {
			purchaseBean.hideModal();
			
			// Clearing List to reload clients and data.
			purchaseBean.setOrderNumber(null);
			purchaseBean.setShowData(false);
			purchaseBean.setShowMessage(false);
			purchaseBean.setDevices(null);
			purchaseBean.clearLogA();
			purchaseBean.clearLogC();
		}
		return "purchase";
	}
	
	/**
	 * Init -> Compras -> Registar consignación.
	 */
	public String initConsig(){
		ConsigBean consigBean = (ConsigBean) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("consigBean");
		if (consigBean != null) {
			consigBean.hideModal();
			consigBean.clear();
			consigBean.changeForm();
		}
		return "userShipment";
	}
	
	/**
	 * Init->Clientes-> Crear Cuenta Cliente.
	 */
	public String initAccountCreate(){
		AccountClient accountClient = (AccountClient) FacesContext
		.getCurrentInstance().getExternalContext().getSessionMap().get(
				"accountClient");
		if (accountClient != null) {
			accountClient.init();
		}
		return "accountCreate";
	} 
	
	/**
	 * Init -> Dispositivos -> Asociar con cuenta.  
	 */
	public String initDeviceAccount(){
		DeviceAccount deviceAccount = (DeviceAccount) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap().get(
						"deviceAccount");
		if (deviceAccount != null) {
			deviceAccount.init();
		}
		return "deviceAccount";
	}
	
	/**
	 * Init->Tesorería->Aprobar Consignaciones
	 */
	public String initApprove(){
		ApproveConsignment approveConsignment = (ApproveConsignment) FacesContext
		.getCurrentInstance().getExternalContext().getSessionMap().get(
				"approveConsignment"); 
		if (approveConsignment != null) {
			approveConsignment.hideModal();
			approveConsignment.setConList(null);
		}
		return "approveCosig";
	}
	
	/**
	 * Init->TISC-> Consultar Order de Pedido.
	 */
	public String initConsultOrder() {
		ConsultOrder consultOrder = (ConsultOrder) FacesContext
		.getCurrentInstance().getExternalContext().getSessionMap().get(
				"consultOrder"); 
		if(consultOrder != null){
			// Clearing List to reload data.
			consultOrder.setOrderNumber(null);
			consultOrder.setShowData(false);
			consultOrder.setShowMessage(false);
		}
		return "consultOrder";
	}
	
	/**
	 * Init-> Almacén-> Confirmar Orden de Pedido.
	 */
	public String initConfirmOrder(){
		return "confirmOrder";
	}
	
	/**
	 * Init->Tisc->Recarga 
	 */
	public String initRecharge(){
		RechargeBean rechargeBean = (RechargeBean) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap().get(
						"rechargeBean"); 
		if(rechargeBean!=null){
			rechargeBean.getLoga().clear();
			rechargeBean.getLogc().clear();
		}
		return "recharge";
	}
	
	/**
	 * Init->Tisc->Consulta Saldo de Tarjetas. 
	 */
	public String initConsultCard() {
		try {
			ConsultCard consultCard = (ConsultCard) FacesContext
			.getCurrentInstance().getExternalContext().getSessionMap().get(
					"consultCard"); 
			if(consultCard!=null){
				consultCard.getLoga().clear();
				consultCard.getLogc().clear();
			}
		} catch (Exception e) {
			System.out.println( " [ Error en GeneralManageBean.initConsultCard ] ");
		}
		return "consultCard";
	}
	
	/**
	 * Init-> Proceso -> Consultar Proceso Dispositivo.
	 */
	public String initDeviceProcess(){
		DeviceProcessBean deviceProcessBean = (DeviceProcessBean) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap().get(
						"deviceProcessBean"); 
		if(deviceProcessBean != null){
			deviceProcessBean.setShowData(false);
			deviceProcessBean.setShowMessage(false);
			deviceProcessBean.setDevicesTypes(null);
			deviceProcessBean.setTypeDevId(-1L);
			deviceProcessBean.setDeviceId(-1L);;
			deviceProcessBean.setFacial(null);
		}
		return "viewDeviceProcess";
	}
	
	/**
	 * Init->Tisc->Añadir de Tarjetas. 
	 */
	public String initAddCard() {
		AddCard addCard = (AddCard) FacesContext
		.getCurrentInstance().getExternalContext().getSessionMap().get(
				"addCard"); 
		if (addCard != null) {
			addCard.init();
		}
		return "addCard";
	}
	
	public String consultHisRecharge(){
		return "consultHisRecharge";
	}
	
	public String registerMaster(){
		MasterBean masterBean = (MasterBean) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("masterBean"); 
		if (masterBean != null) {
			masterBean.clearData();
		}
		return "newMaster";
	}
	
	public String authorizeVipsa() {
		AuthorizeVipsa authorizeVipsa = (AuthorizeVipsa) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap().get("authorizeVipsa");
		if (authorizeVipsa != null) {
			authorizeVipsa.init();
		}
		return "authorizeVipsa";
	}
	
	/**
	 * Init -> Seguimiento de Proceso -> Ver Proceso Personalizacion.
	 */
	public String initCustomizationProcess() {
		try {
			CustomizationProcessBean customizationProcessBean = (CustomizationProcessBean) FacesContext
			.getCurrentInstance().getExternalContext().getSessionMap().get(
					"customizationProcessBean");
			if(customizationProcessBean != null){ 
				customizationProcessBean.setShowMessage(false);
				customizationProcessBean.setShowData(false);
				customizationProcessBean.setPlate(null);
			}
		} catch (Exception e) {
			System.out.println( " [ Error en GeneralManageBean.initCustomizationProcess ] ");
		}
		return "viewCustomizationProcess";
	}
	
	/**
	 * Init -> Inclusiones -> Crear.
	 */
	public String createInclusion() {
		try {
			InclusionBean inclusionBean = (InclusionBean) FacesContext
			.getCurrentInstance().getExternalContext().getSessionMap().get(
					"inclusionBean");
			if(inclusionBean != null){ 
				inclusionBean.init();
			}
		} catch (Exception e) {
			System.out.println( " [ Error en GeneralManageBean.createInclusion ] ");
		}
		return "createInclusion";
	}
	
	/**
	 * Init -> Seguimiento de Proceso -> Ver Proceso Inclusion.
	 */
	public String initInclusionProcess() {
		try {
			InclusionProcessBean inclusionProcessBean = (InclusionProcessBean) FacesContext
			.getCurrentInstance().getExternalContext().getSessionMap().get(
					"inclusionProcessBean");
			if(inclusionProcessBean != null){ 
				inclusionProcessBean.setShowMessage(false);
				inclusionProcessBean.setShowData(false);
				inclusionProcessBean.setInclusionNumber(null);
			}
		} catch (Exception e) {
			System.out.println( " [ Error en GeneralManageBean.initInclusionProcess ] ");
		}
		return "viewInclusionProcess";
	}
	
	/**
	 * Init -> Inclusiones -> Modificar.
	 */
	public String modInclusion() {
		try {
			ModInclusionBean modInclusionBean = (ModInclusionBean) FacesContext
			.getCurrentInstance().getExternalContext().getSessionMap().get(
					"modInclusionBean");
			if(modInclusionBean != null){ 
				modInclusionBean.init();
			}
		} catch (Exception e) {
			System.out.println( " [ Error en GeneralManageBean.modInclusion ] ");
		}
		return "modInclusion";
	}
	
	/**
	 * Init->Ubicaciones de Instalación -> Administrar
	 */
	public String initInstallLocation(){
		try {
			InstallationLocationBean installationLocationBean = (InstallationLocationBean) FacesContext
			.getCurrentInstance().getExternalContext().getSessionMap().get(
					"installationLocationBean");
			if(installationLocationBean != null){ 
				installationLocationBean.setLocationList(null);
			}
		} catch (Exception e) {
			System.out.println( " [ Error en GeneralManageBean.initInstallLocation ] ");
		}
		return "installLocation";
	}
	
	/**
	 * Init->Causas de Reposición -> Administrar
	 */
	public String initReplacementCauses(){
		try {
			ReplacementCausesBean replacementCausesBean = (ReplacementCausesBean) FacesContext
			.getCurrentInstance().getExternalContext().getSessionMap().get(
					"replacementCausesBean");
			if(replacementCausesBean != null){ 
				replacementCausesBean.setCausesList(null);
			}
		} catch (Exception e) {
			System.out.println( " [ Error en GeneralManageBean.initReplacementCauses ] ");
		}
		return "replacementCauses";
	}
	
	/**
	 * Init -> Exentos y Especiales-> Instalación.
	 */
	public String initInstall(){
		try {
			InstallationBean installationBean = (InstallationBean) FacesContext
			.getCurrentInstance().getExternalContext().getSessionMap().get(
					"installationBean");
			if(installationBean != null){ 
				installationBean.setSearchByPlate(true);
				installationBean.setShowData(false);
				installationBean.clear();
			}
		} catch (Exception e) {
			System.out.println( " [ Error en GeneralManageBean.initInstall ] ");
		}
		return "initInstall";
	}
	
	/**
	 * Init->Tipos de Especial Exento -> Administrar
	 */
	public String initSpecialExemptType(){
		try {
			SpecialExemptTypeBean specialExemptTypeBean = (SpecialExemptTypeBean) FacesContext
			.getCurrentInstance().getExternalContext().getSessionMap().get(
					"specialExemptTypeBean");
			if(specialExemptTypeBean != null){ 
				specialExemptTypeBean.setTypeList(null);
			}
		} catch (Exception e) {
			System.out.println( " [ Error en GeneralManageBean.initSpecialExemptType ] ");
		}
		return "initSpecialExemptType";
	}
	
	/**
	 * Init->Tipos de Especial Exento -> Crear dependencia exento.
	 */
	public String initExemptOffice() {
		try {
			ExemptOfficeBean exemptOfficeBean = (ExemptOfficeBean) FacesContext
			.getCurrentInstance().getExternalContext().getSessionMap().get(
					"exemptOfficeBean");
			if(exemptOfficeBean != null){ 
				exemptOfficeBean.setOfficeList(null);
			}
		} catch (Exception e) {
			System.out.println( " [ Error en GeneralManageBean.initExemptOffice ] ");
		}
		return "initExemptOffice";
	}
	
	/**
	 * Init -> Tesorería -> Registar consignación.
	 */
	public String initAddConsig(){
		ConsigBean consigBean = (ConsigBean) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("consigBean");
		if (consigBean != null) {
			consigBean.hideModal();
			consigBean.clear();
			consigBean.changeForm();
			consigBean.setClients(null);
		}
		return "initAddConsig";
	}
	
	/**
	 * Init -> Seguimiento de Proceso -> Ver Proceso Orden de Pedido.
	 */
	public String initOrderProcess() {
		try {
			PurchaseOrderProcessBean purchaseOrderProcessBean = (PurchaseOrderProcessBean) FacesContext
			.getCurrentInstance().getExternalContext().getSessionMap().get(
					"purchaseOrderProcessBean");
			if(purchaseOrderProcessBean != null){ 
				purchaseOrderProcessBean.setShowMessage(false);
				purchaseOrderProcessBean.setShowData(false);
				purchaseOrderProcessBean.setOrderNumber(null);
			}
		} catch (Exception e) {
			System.out.println( " [ Error en GeneralManageBean.initOrderProcess ] ");
		}
		return "initOrderProcess";
	}
	
	/**
	 * Init -> Tesorería -> Consultar Consignaciones 
	 */
	public String initConsultConsig() {
		try {
			ConsultConsignment consultConsignment = (ConsultConsignment) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap().get(
					"consultConsignment");
			if(consultConsignment != null){ 
				consultConsignment.setClients(null);
				consultConsignment.setNoData(false);
				consultConsignment.setShowConsignments(false);
			}
		} catch (Exception e) {
			System.out.println( " [ Error en GeneralManageBean.initConsultConsig ] ");
		}
		return "initConsultConsig";
	}
	
	/**
	 * Init->Bancos -> Administrar
	 */
	public String adminBank() {
		try {
			BankBean bankBean = (BankBean) FacesContext
			.getCurrentInstance().getExternalContext().getSessionMap().get(
					"bankBean");
			if(bankBean != null){ 
				bankBean.setBankList(null);
			}
		} catch (Exception e) {
			System.out.println( " [ Error en GeneralManageBean.adminBank ] ");
		}
		return "adminBank";
	}
	
	/**
	 * Init->Cuenta de Bancos -> Administrar
	 */
	public String adminBankAccount() {
		try {
			BankAccountBean bankAccountBean = (BankAccountBean) FacesContext
			.getCurrentInstance().getExternalContext().getSessionMap().get(
					"bankAccountBean");
			if(bankAccountBean != null){ 
				bankAccountBean.setBankAccountList(null);
			}
		} catch (Exception e) {
			System.out.println( " [ Error en GeneralManageBean.adminBankAccount ] ");
		}
		return "adminBankAccount";
	}
	
	/**
	 * Init->Dependencia de Almacén -> Administrar
	 */
	public String adminWarehouseDependency() {
		try {
			WarehouseDependencyBean warehouseDependencyBean = (WarehouseDependencyBean) FacesContext
			.getCurrentInstance().getExternalContext().getSessionMap().get(
					"warehouseDependencyBean");
			if(warehouseDependencyBean != null){ 
				warehouseDependencyBean.setDependencyList(null);
			}
		} catch (Exception e) {
			System.out.println( " [ Error en GeneralManageBean.adminWarehouseDependency ] ");
		}
		return "adminWarehouseDependency";
	}
	
	/**
	 * Init->Administrar -> Tipos de Tarjeta.
	 */
	public String adminWarehouseCardType() {
		try {
			WarehouseCardTypeBean warehouseCardTypeBean = (WarehouseCardTypeBean) FacesContext
			.getCurrentInstance().getExternalContext().getSessionMap().get(
					"warehouseCardTypeBean");
			if(warehouseCardTypeBean != null){ 
				warehouseCardTypeBean.setCardTypeList(null);
			}
		} catch (Exception e) {
			System.out.println( " [ Error en GeneralManageBean.adminWarehouseCardType ] ");
		}
		return "adminWarehouseCardType";
	}
	
	/**
	 * Init->Clientes -> Administrar 
	 */
	public String listClient() {
		try {
			ListClient listClient = (ListClient) FacesContext
			.getCurrentInstance().getExternalContext().getSessionMap().get(
					"listClient");
			if(listClient != null){ 
				listClient.setListClient(null);
			}
		} catch (Exception e) {
			System.out.println( " [ Error en GeneralManageBean.listClient ] ");
		}
		return "listClient";
	}
	
	/**
	 * Init->Empresas -> Administrar 
	 */
	public String initCompany() {
		try {
			CompanyBean companyBean = (CompanyBean) FacesContext
			.getCurrentInstance().getExternalContext().getSessionMap().get(
					"companyBean");
			if(companyBean != null){ 
				companyBean.setCompanyList(null);
			}
		} catch (Exception e) {
			System.out.println( " [ Error en GeneralManageBean.initCompany ] ");
		}
		return "initCompany";
	}
	
	/**
	 * Init->Estaciones  -> Crear 
	 */
	public String createStation() {
		try {
			StationBean stationBean = (StationBean) FacesContext
			.getCurrentInstance().getExternalContext().getSessionMap().get(
					"stationBean");
			if(stationBean != null){ 
				stationBean.init();
			}
		} catch (Exception e) {
			System.out.println( " [ Error en GeneralManageBean.createStation ] ");
		}
		return "createStation";
	}
	
	/**
	 * Init->Estaciones -> Consultar 
	 */
	public String listStation() {
		try {
			StationBean stationBean = (StationBean) FacesContext
			.getCurrentInstance().getExternalContext().getSessionMap().get(
					"stationBean");
			if(stationBean != null){ 
				stationBean.setStations(null);
			}
		} catch (Exception e) {
			System.out.println( " [ Error en GeneralManageBean.listStation ] ");
		}
		return "listStation";
	}
	
	/**
	 * Init-> Especiales Y Exentos -> Correción de malas digitaciones.
	 */
	public String initFixTyped() {
		try {
			FixMaster fixMaster = (FixMaster) FacesContext
					.getCurrentInstance().getExternalContext().getSessionMap().get("fixMaster");
			if (fixMaster != null) {
				fixMaster.setCusList(null);
				fixMaster.setShowData(false);
				fixMaster.setShowMessage(false);
				fixMaster.setShowEditArea(false);
				fixMaster.hideModal();
			} 
		} catch (Exception e) {
			System.out.println( " [ Error en GeneralManageBean.initFixTyped ] ");
			e.printStackTrace();
		}
		return "initFixTyped";
	}
	
	/**
	 * Init->Administración de Parámetros -> Sistema de Recarga.
	 */
	public String adminPoint(){
		try {
			PointBean pointBean = (PointBean) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap().get("pointBean");
			if(pointBean != null){ 
				pointBean.setPointList(null);
				pointBean.hideModal();
			}
		} catch (Exception e) {
			System.out.println( " [ Error en GeneralManageBean.adminPoint ] ");
		}
		return "adminPoint";
	}
	
	/**
	 * Init->Asignar Punto de Recarga Usuario.
	 */
	public String asignPoint(){
		try {
			AsignPointBean asignPointBean = (AsignPointBean) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap().get("asignPointBean");
			if(asignPointBean != null){
				asignPointBean.setShowData(false);
				asignPointBean.setShowMessage(false);
				asignPointBean.setUserCode(null);
				asignPointBean.setUserCodeTypeId(1L);
				asignPointBean.hideModal();
			}
		} catch (Exception e) {
			System.out.println( " [ Error en GeneralManageBean.asignPoint ] ");
		}
		return "asignPoint";
	}
	
	/**
	 *  
	 */
	public String createEntryOrder() {
		try {
			EntryOrderBean entryOrderBean = (EntryOrderBean) FacesContext
					.getCurrentInstance().getExternalContext().getSessionMap().get(
					"entryOrderBean"); 
			if (entryOrderBean != null) {
				entryOrderBean.init();
			}
		} catch (Exception e) {
			System.out.println( " [ Error en GeneralManageBean.createEntryOrder ] ");
		}
		return "createEntryOrder";
	}
	
	/**
	 *  
	 */
	public String updateEntryOrder() {
		try {
			UpdateEntryOrderBean updateEntryOrderBean = (UpdateEntryOrderBean) FacesContext
					.getCurrentInstance().getExternalContext().getSessionMap().get(
					"updateEntryOrderBean"); 
			if (updateEntryOrderBean != null) {
				updateEntryOrderBean.init();
				updateEntryOrderBean.clear();
			}
		} catch (Exception e) {
			System.out.println( " [ Error en GeneralManageBean.updateEntryOrder ] ");
		}
		return "updateEntryOrder";
	}
	
	/**
	 *  
	 */
	public String initPrechargeTag() {
		try {
			PrechargeBean prechargeBean = (PrechargeBean) FacesContext
					.getCurrentInstance().getExternalContext().getSessionMap().get(
					"prechargeBean"); 
			if (prechargeBean != null) {
				prechargeBean.init();
			}
		} catch (Exception e) {
			System.out.println( " [ Error en GeneralManageBean.initPrechargeTag ] ");
		}
		return "initPrechargeTag";
	}
	
	/**
	 *  
	 */
	public String initConsultOrderDate() {
		try {
			EntryOrderConsultBean entryOrderConsultBean = (EntryOrderConsultBean) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap().get(
				"entryOrderConsultBean"); 
			if (entryOrderConsultBean != null) {
				entryOrderConsultBean.init();
			}
		} catch (Exception e) {
			System.out.println( " [ Error en GeneralManageBean.initConsultOrderDate ] ");
		}
		return "initConsultOrderDate";
	}
	
	/**
	 *  
	 */
	public String initConsultOrderNumber() {
		try {
			EntryOrderConsultBean entryOrderConsultBean = (EntryOrderConsultBean) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap().get(
				"entryOrderConsultBean"); 
			if (entryOrderConsultBean != null) {
				entryOrderConsultBean.init();
			}
		} catch (Exception e) {
			System.out.println( " [ Error en GeneralManageBean.initConsultOrderNumber ] ");
		}
		return "initConsultOrderNumber";
	}
	
	/**
	 *  
	 */
	public String initParamPrecharge() {
		try {
			ParamPrechargeBean paramPrechargeBean = (ParamPrechargeBean) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap().get(
				"paramPrechargeBean"); 
			if (paramPrechargeBean != null) {
				paramPrechargeBean.init();
			}
		} catch (Exception e) {
			System.out.println( " [ Error en GeneralManageBean.initParamPrecharge ] ");
		}
		return "initParamPrecharge";
	}
	
	/**
	 *  
	 */
	public String initIniSpecialCard() {
		try {
			InitializationBean initializationBean = (InitializationBean) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap().get("initializationBean"); 
			if (initializationBean != null) {
				initializationBean.init();
			}
		} catch (Exception e) {
			System.out.println( " [ Error en GeneralManageBean.initIniSpecialCard ] ");
		}
		return "initIniSpecialCard";
	}
	
	/**
	 *  
	 */
	public String initPerSpecialCard() {
		try {
			PersonalizationBean personalizationBean = (PersonalizationBean) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap().get("personalizationBean"); 
			if (personalizationBean != null) {
				personalizationBean.init();
			}
		} catch (Exception e) {
			System.out.println( " [ Error en GeneralManageBean.initPerSpecialCard ] ");
		}
		return "initPerSpecialCard";
	}
	
	/**
	 *  
	 */
	public String initSavePay() {
		try {
			SavePayment  savePayment = (SavePayment) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap().get("savePayment"); 
			if (savePayment != null) {
				savePayment.init();
			}
		} catch (Exception e) {
			System.out.println( " [ Error en GeneralManageBean.initSavePay ] ");
		}
		return "initSavePay";
	}
	
	/**
	 *  
	 */
	public String initAccountMove() {
		try {
			AccountTransactionBean  accountTransactionBean = (AccountTransactionBean) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap().get("accountTransactionBean"); 
			if (accountTransactionBean != null) {
				accountTransactionBean.init();
			}
		} catch (Exception e) {
			System.out.println( " [ Error en GeneralManageBean.initAccountMove ] ");
		}
		return "initAccountMove";
	}
	
	/**
	 *  
	 */
	public String iniAccountDevices() {
		try {
			AccountDeviceBean  accountDeviceBean = (AccountDeviceBean) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap().get("accountDeviceBean"); 
			if (accountDeviceBean != null) {
				accountDeviceBean.init();
			}
		} catch (Exception e) {
			System.out.println( " [ Error en GeneralManageBean.iniAccountDevices ] ");
		}
		return "iniAccountDevices";
	}
	
	/**
	 *  
	 */
	public String iniDistTag() {
		try {
			DistributionTagBean  distributionTagBean = (DistributionTagBean) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap().get("distributionTagBean"); 
			if (distributionTagBean != null) {
				distributionTagBean.init();
			}
		} catch (Exception e) {
			System.out.println( " [ Error en GeneralManageBean.iniDistTag ] ");
		}
		return "iniDistTag";
	}
	
	/**
	 *  
	 */
	public String initMyAccountDevices() {
		try {
			AccountDeviceBean  accountDeviceBean = (AccountDeviceBean) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap().get("accountDeviceBean"); 
			if (accountDeviceBean != null) {
				accountDeviceBean.init();
			}		
		} catch (Exception e) {
			System.out.println( " [ Error en GeneralManageBean.initMyAccountDevices ] ");
		}
		return "initMyAccountDevices";
	}
	
	/**
	 *  
	 */
	public String initMyAccountMove() {
		try {
			AccountTransactionBean  accountTransactionBean = (AccountTransactionBean) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap().get("accountTransactionBean"); 
			if (accountTransactionBean != null) {
				accountTransactionBean.init();
			}
		} catch (Exception e) {
			System.out.println( " [ Error en GeneralManageBean.initMyAccountMove ] ");
		}
		return "initMyAccountMove";
	}
	
	/**
	 *  
	 */
	public String initTraceabilityByPlate() {
		try {
			TraceabilityPlateReportBean  traceabilityPlateReportBean = (TraceabilityPlateReportBean) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap().get("traceabilityPlateReportBean"); 
			if (traceabilityPlateReportBean != null) {
				//traceabilityPlateReportBean.reset();
			}
		} catch (Exception e) {
			System.out.println( " [ Error en GeneralManageBean.initTraceabilityByPlate ] ");
		}
		return "initTraceabilityByPlate";
	}
	
	/**
	 *  
	 */
	public String initUpTranTelepeaje() {
		try {
			UpTranTelepeajeBean  upTranTelepeajeBean = (UpTranTelepeajeBean) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap().get("upTranTelepeajeBean"); 
			if (upTranTelepeajeBean != null) {
				upTranTelepeajeBean.init();
			}
		} catch (Exception e) {
			System.out.println( " [ Error en GeneralManageBean.initUpTranTelepeaje ] ");
		}
		return "initUpTranTelepeaje";
	}
	
	/**
	 *  
	 */
	public String initEnterDevice() {
		try {
			EnterDevice  enterDevice = (EnterDevice) FacesContext
				.getCurrentInstance().getExternalContext().getSessionMap().get("enterDevice"); 
			if (enterDevice != null) {
				enterDevice.init();
			}
		} catch (Exception e) {
			System.out.println( " [ Error en GeneralManageBean.initEnterDevice ] ");
		}
		return "initEnterDevice";
	}
}