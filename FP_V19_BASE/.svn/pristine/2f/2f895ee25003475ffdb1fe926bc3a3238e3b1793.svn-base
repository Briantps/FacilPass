package menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import sessionVar.SessionUtil;
import constant.AccountStateType;

import jpa.ReOptionAction;
import jpa.TbAccount;
import jpa.TbOptActRefefenceType;
import jpa.TbOption;
import ejb.Option;


public class AdminMenuBean implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@EJB(mappedName = "ejb/Option")
	private Option options;
	
	private List<TbOption> listMenu;
	private List<ReOptionAction> listActions;
	private List<TbOptActRefefenceType> listReferences;
	private List<SelectItem> selListRef;
	
	private Long idOpt;
	private String nameOpt;
	
	private Long idOptAct;
	private String nameOptAct;
		
	private Long minValueOrder;
	private Long maxValueOrder;
	
	private Long minValueOrderAction;
	private Long maxValueOrderAction;
	
	private boolean showActions;
	private boolean showMsg;
	private String msg;
	
	private boolean showReferences;
	private String referenceId;
	
	private boolean showPrevMenu;
	private String referenceIdPrev;
	
	public void setOptions(Option options) {
		this.options = options;
	}

	public Option getOptions() {
		return options;
	}

	public void setListMenu(List<TbOption> listMenu) {
		this.listMenu = listMenu;
	}

	public List<TbOption> getListMenu() {
		if(listMenu == null){
			listMenu = new ArrayList<TbOption>();
		}else{
			listMenu.clear();
		}
		listMenu = options.getListMenu();
		return listMenu;
	}

	public void setIdOpt(Long idOpt) {
		this.idOpt = idOpt;
	}

	public Long getIdOpt() {
		return idOpt;
	}
	
	public void setShowMsg(boolean showMsg) {
		this.showMsg = showMsg;
	}

	public boolean isShowMsg() {
		return showMsg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void upOrder(){
		showMsg = false;
		msg = "";	
	  if (idOpt != null){
		 if(options.upOrderOption(idOpt)) {
			 msg = "Transacción Exitosa";	
			 showMsg = true;
			 this.listMenu.clear();
			 this.getListMenu();
		 } else{
			 msg = "Error al Procesar la Solicitud";	
			 showMsg = true;
		 }
	  }
	}
	
	public void downOrder(){
		showMsg = false;
		msg = "";	
	  if (idOpt != null){
		 if(options.downOrderOption(idOpt)) {
			 msg = "Transacción Exitosa";	
			 showMsg = true;
			 this.listMenu.clear();
			 this.getListMenu();
		 } else{
			 msg = "Error al Procesar la Solicitud";	
			 showMsg = true;
		 }
	  }
	}
	
	public void hideModal(){
		showMsg = false;
		this.setShowActions(false);
		msg = "";
	}

	public void setMinValueOrder(Long minValueOrder) {
		this.minValueOrder = minValueOrder;
	}

	public Long getMinValueOrder() {
		minValueOrder = options.getMinValueOrder();
		return minValueOrder;
	}

	public void setMaxValueOrder(Long maxValueOrder) {
		this.maxValueOrder = maxValueOrder;
	}

	public Long getMaxValueOrder() {
		maxValueOrder = options.getMaxValueOrder();
		return maxValueOrder;
	}

	public void setShowActions(boolean showActions) {
		this.showActions = showActions;
	}

	public boolean isShowActions() {
		return showActions;
	}
	
	public void viewActions(){
		if(idOpt != null){
			if(listActions == null){
				listActions = new ArrayList<ReOptionAction>();
			}else{
				listActions.clear();
			}
			nameOpt = options.getNameOptionByOption(idOpt);
			listActions = options.getListActionsByOption(idOpt);
			minValueOrderAction = options.getMinValueOrderAction(idOpt);
			maxValueOrderAction = options.getMaxValueOrderAction(idOpt);
			showActions = true;
		}
	}

	public void setListActions(List<ReOptionAction> listActions) {
		this.listActions = listActions;
	}

	public List<ReOptionAction> getListActions() {
		return listActions;
	}

	public void setNameOpt(String nameOpt) {
		this.nameOpt = nameOpt;
	}

	public String getNameOpt() {
		return nameOpt;
	}

	public void setIdOptAct(Long idOptAct) {
		this.idOptAct = idOptAct;
	}

	public Long getIdOptAct() {
		return idOptAct;
	}

	public void setMinValueOrderAction(Long minValueOrderAction) {
		this.minValueOrderAction = minValueOrderAction;
	}

	public Long getMinValueOrderAction() {
		return minValueOrderAction;
	}

	public void setMaxValueOrderAction(Long maxValueOrderAction) {
		this.maxValueOrderAction = maxValueOrderAction;
	}

	public Long getMaxValueOrderAction() {		
		return maxValueOrderAction;
	}
	
	public void upOrderAction(){
		showMsg = false;
		msg = "";	
	  if (idOptAct != null){
		 if(options.upOrderOptionAction(idOptAct,idOpt)) {
			 msg = "Transacción Exitosa";	
			 showMsg = true;
			 this.listMenu.clear();
			 this.getListMenu();
		 } else{
			 msg = "Error al Procesar la Solicitud";	
			 showMsg = true;
		 }
	  }
	}
	
	public void downOrderAction(){
		showMsg = false;
		msg = "";	
	  if (idOptAct != null){
		 if(options.downOrderOptionAction(idOptAct,idOpt)) {
			 msg = "Transacción Exitosa";	
			 showMsg = true;
			 this.listMenu.clear();
			 this.getListMenu();
		 } else{
			 msg = "Error al Procesar la Solicitud";	
			 showMsg = true;
		 }
	  }
	}
	
	public void hideModalOptions(){
		showActions = false;
		showMsg = false;
	}
	
	public void changeReference(){
		if(selListRef == null){			
			selListRef = new ArrayList<SelectItem>();
			for(TbOptActRefefenceType ref : options.getListReferences()){
		    	selListRef.add(new SelectItem(ref.getOptActReferenceId(),ref.getOptActDescription()));		  
		    }
		}	
		nameOpt = options.getNameOptionByOption(idOpt);
		nameOptAct = options.getNameOptionActionByOptionAction(idOptAct);
		showReferences = true;		
	}

	public void setShowReferences(boolean showReferences) {
		this.showReferences = showReferences;
	}

	public boolean isShowReferences() {
		return showReferences;
	}

	public void setListReferences(List<TbOptActRefefenceType> listReferences) {
		this.listReferences = listReferences;
	}

	public List<TbOptActRefefenceType> getListReferences() {
		return listReferences;
	}
	
	public void hideModalReferences(){
		showReferences = false;	
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setSelListRef(List<SelectItem> selListRef) {
		this.selListRef = selListRef;
	}

	public List<SelectItem> getSelListRef() {
		referenceId = null;
		if(selListRef == null){			
			selListRef = new ArrayList<SelectItem>();
			for(TbOptActRefefenceType ref : options.getListReferences()){
		    	selListRef.add(new SelectItem(ref.getOptActReferenceId(),ref.getOptActDescription()));		  
		    }
		}
		return selListRef;
	}

	public void setNameOptAct(String nameOptAct) {
		this.nameOptAct = nameOptAct;
	}

	public String getNameOptAct() {
		return nameOptAct;
	}
	
	public void changingReference(){
		if (referenceId != null){
			if(options.setReferenceIdByOpcAct(idOptAct,referenceId)){
				showReferences = false; 
				msg = "Transacción Exitosa";	
				showMsg = true;				 
				this.viewActions();
			} else{
			    msg = "Error al Procesar la Solicitud";	
				showMsg = true;
			}
		}
	}

	public void setShowPrevMenu(boolean showPrevMenu) {
		this.showPrevMenu = showPrevMenu;
	}

	public boolean isShowPrevMenu() {
		return showPrevMenu;
	}

	public void setReferenceIdPrev(String referenceIdPrev) {
		this.referenceIdPrev = referenceIdPrev;
	}

	public String getReferenceIdPrev() {
		return referenceIdPrev;
	}
	
	
		
}
