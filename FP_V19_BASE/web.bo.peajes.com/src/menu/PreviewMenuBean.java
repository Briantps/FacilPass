package menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.el.MethodExpression;
import javax.faces.context.FacesContext;

import org.richfaces.component.html.HtmlPanelMenu;
import org.richfaces.component.html.HtmlPanelMenuGroup;
import org.richfaces.component.html.HtmlPanelMenuItem;

import util.OptionActions;
import ejb.Permission;


public class PreviewMenuBean implements Serializable{
	private static final long serialVersionUID = 1933800214856275568L;
	HashMap<String, Boolean> menuState=new HashMap<String, Boolean>(); 

	  
	// Attributes ----------------------------------------------------------------------------------------------------------------
	
	@EJB(mappedName = "ejb/Permission")
	private Permission permisos;
	
	private HtmlPanelMenu panelMenuPrev;
	private HtmlPanelMenuGroup menuGroupPrev;
	private HtmlPanelMenuItem menuItemPrev;
	private List<OptionActions> optionactionsPrev;
	private String itemSelectedPrev;
	private String referenceId = "A";
	
	private String descript;
	
	private boolean showPrevMenu;
	
	public void setPermisos(Permission permisos) {
		this.permisos = permisos;
	}


	public Permission getPermisos() {
		return permisos;
	}


	public void setPanelMenuPrev(HtmlPanelMenu panelMenuPrev) {
		this.panelMenuPrev = panelMenuPrev;
	}


	public HtmlPanelMenu getPanelMenuPrev() {
		if(referenceId.equals(null) || referenceId.equals("")){
			referenceId = "A";
		}
		
		if(panelMenuPrev == null){
			panelMenuPrev =  new HtmlPanelMenu();
		} else {
			//System.out.println("Clear panelmenu");
			panelMenuPrev.getChildren().clear();
		}
		System.out.println("referenceId "+referenceId);
		this.createMenuPrev();		  
		return panelMenuPrev;
	}


	public void setMenuGroupPrev(HtmlPanelMenuGroup menuGroupPrev) {
		this.menuGroupPrev = menuGroupPrev;
	}


	public HtmlPanelMenuGroup getMenuGroupPrev() {
		return menuGroupPrev;
	}


	public void setMenuItemPrev(HtmlPanelMenuItem menuItemPrev) {
		this.menuItemPrev = menuItemPrev;
	}


	public HtmlPanelMenuItem getMenuItemPrev() {
		return menuItemPrev;
	}


	public void setOptionactionsPrev(List<OptionActions> optionactionsPrev) {
		this.optionactionsPrev = optionactionsPrev;
	}


	public List<OptionActions> getOptionactionsPrev() {
		return optionactionsPrev;
	}


	public void setItemSelectedPrev(String itemSelectedPrev) {
		this.itemSelectedPrev = itemSelectedPrev;
	}


	public String getItemSelectedPrev() {
		return itemSelectedPrev;
	}


	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}


	public String getReferenceId() {
		return referenceId;
	}	
	
	public void createMenuPrev(){		
		if(optionactionsPrev == null){
			//System.out.println("new");
			optionactionsPrev = new ArrayList<OptionActions>();
		} else{
			//System.out.println("clear");
			optionactionsPrev.clear();
		}	
		optionactionsPrev = permisos.getOpctionActionPrev(referenceId);
		
		
		
		
		panelMenuPrev.setMode("ajax");
		panelMenuPrev.setIconExpandedGroup("disc");
		panelMenuPrev.setIconCollapsedGroup("disc");
		panelMenuPrev.setIconExpandedTopGroup("chevronUp");
		panelMenuPrev.setIconCollapsedTopGroup("chevronDown");
		panelMenuPrev.setIconGroupTopPosition("right");	
		panelMenuPrev.setItemStyle("font-family: 'Arial'; 	font-size: 11px;");
		panelMenuPrev.setExpandSingle(true);
		
		int grupos = 0;
		
		
		
	    for (OptionActions o : optionactionsPrev){
			// Creating <rich:panelMenuGroup>
	    	menuGroupPrev = new HtmlPanelMenuGroup();
	    	menuGroupPrev.setLabel( o.getOption());
	    	menuGroupPrev.setName(o.getOption());
	    	//System.out.println("grupo_"+o.getOption().replaceAll(" ","").replaceAll("ñ","n")+"_"+grupos);
	    	//menuGroupPrev.setId("grupo_"+o.getOption().replaceAll(" ","").replaceAll("ñ","n")+"_"+grupos);
				
				grupos = grupos+1;
				panelMenuPrev.getChildren().add(menuGroupPrev);
				int items =0;
					for (String oj: o.getActions()){
						String[] array = oj.split(",");
						
						// Creating <rich:panelMenuItem>
						menuItemPrev = new HtmlPanelMenuItem();
						menuItemPrev.setLabel(array[0]);
						menuItemPrev.setName(array[0] + array[1]);
						//System.out.println("g"+grupos+"_item_"+array[0].replaceAll(" ","").replaceAll("ñ","n")+"_"+items);
						//menuItemPrev.setId("g"+grupos+"_item_"+array[0].replaceAll(" ","").replaceAll("ñ","n")+"_"+items);
						
						items = items+1;
						//menuItemPrev.setActionExpression(createActionExpression(array[2], String.class));							
						menuGroupPrev.getChildren().add(menuItemPrev);
					}		    	
	    }	
	}
	
	public void client(){
		referenceId = "C";	
		descript = "Vista Previa Menú Cliente";
		this.getPanelMenuPrev();
		setShowPrevMenu(true);
	}
	
	public void admin(){
		referenceId = "U";	
		descript = "Vista Previa Menú Usuario - Administrador";
		this.getPanelMenuPrev();
		setShowPrevMenu(true);
	}


	public void setShowPrevMenu(boolean showPrevMenu) {
		this.showPrevMenu = showPrevMenu;
	}


	public boolean isShowPrevMenu() {
		return showPrevMenu;
	}
	
	public void hideModalPrev(){
		showPrevMenu = false;
	}
	
	@SuppressWarnings("unused")
	private MethodExpression createActionExpression(String actionExpression, Class<?> returnType) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getApplication().getExpressionFactory().createMethodExpression(
            facesContext.getELContext(), actionExpression, returnType, new Class[0]);
    }


	public void setDescript(String descript) {
		this.descript = descript;
	}


	public String getDescript() {
		return descript;
	}
}