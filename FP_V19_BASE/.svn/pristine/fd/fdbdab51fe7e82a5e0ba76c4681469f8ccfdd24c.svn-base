package mBeans;

import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;

import org.jsoup.Jsoup;

import security.UserLogged;
import sessionVar.SessionUtil;
import validator.Validation;
import ejb.InformationBalanceI;
import ejb.TextEditorManager;

public class InformationBalanceBean implements Serializable {

	/**
	 * Arivera Clase para Configurar editor de Texto para Infromación de Saldos
	 */
	private static final long serialVersionUID = -5655661360656527253L;

	private UserLogged usrs;
	private String themeOne = "";
	private String themeTwo = "";
	private String themeTree = "";
	private String themeFour = "";
	private String themeFont;
	private String ip;
	private int count = 1;
	public String nameEditor = "informationBalance";
	private String textHtml;
	private boolean modal;
	private boolean modalOption;
	private String msnModalOK;
	private boolean modalCancel;
	private int line;
	private boolean panelEdit;
	@EJB(mappedName = "ejb/TextEditorManager")
	private TextEditorManager textEditorManagerEJB;

	@EJB(mappedName = "ejb/InfromationBalanceI")
	private InformationBalanceI informationBalghanceEJB;

	@PostConstruct
	public void init() {
		themeFont = "Andale Mono=andale mono,times;"
				+ "Arial=arial,helvetica,sans-serif;"
				+ "Arial Black=arial black,avant garde;"
				+ "Book Antiqua=book antiqua,palatino;"
				+ "Comic Sans MS=comic sans ms,sans-serif;"
				+ "Courier New=courier new,courier;"
				+ "Georgia=georgia,palatino;" + "Helvetica=helvetica;"
				+ "Impact=impact,chicago;"
				+ "Tahoma=tahoma,arial,helvetica,sans-serif;"
				+ "Terminal=terminal,monaco;"
				+ "Times New Roman=times new roman,times;"
				+ "Trebuchet MS=trebuchet ms,geneva;"
				+ "Verdana=verdana,geneva";	       
				this.panelEditModal();
	}

	public InformationBalanceBean() {
		usrs = (UserLogged) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("user");
		ip = SessionUtil.ip();

	}

	public void createInformactionBalance() {
		hideModal();

		setMsnModalOK(informationBalghanceEJB.setCreateTXT(usrs.getUserId(),
				textHtml, ip));
		panelEditModal();
		setModal(true);

	}

	public void panelEditModal() {

		setTextHtml(informationBalghanceEJB.getTextHTML());
		setPanelEdit(true);

	}

	
	public void confirmation() {
		String beforeText=textHtml.replaceAll("&nbsp;", "");
		String text = html2text(beforeText);
		
		text.replaceAll("^\\s", "").replaceAll("  ", "");
		  text.replaceAll("&nbsp;","");
		System.out.print("mensaje es ::::"+text+"tamaño del trexto "+text.length());
		
		if(text.toString().trim().length()==0){
			setMsnModalOK("El mensaje no puede estar vacío");
			setModal(true);		
		}else{
		if (preValidate(textHtml)) {
			System.out.println("Entre a Confirmar los cambios para guardar");
			hideModal();
			setMsnModalOK("¿Está seguro de guardar los cambios?");
			setModalOption(true);

		} else {
			setModal(true);
		}
		}
	}

	public boolean preValidate(String html) {
		String Text;
		System.out.print("Este es codigo HTML :   "+html);
		boolean rest=false;
		try {
			Text = html2text(html);
			System.out.println("El texto es ----------> " + Text);
			if (Text.replaceAll(" ", "").length() > 4000) {
				setMsnModalOK("El mensaje no debe contener más de 4000 caracteres");
				rest= false;
			} else if (!Validation.isObservationEditorText(Text)) {
				
				setMsnModalOK("El mensaje contiene caracteres Inválidos");
				rest= false;
			} else if (Text.equals(" ") || Text.equals(null)) {
				setMsnModalOK("El mensaje no puede estar vacío.");
				rest= false;
			}else{
				rest= true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error en preValidate");
			setMsnModalOK("Se ha presentado un Error, porfavor intente más tarde.");
			return rest=false;
		}
		return rest;
	}
	public void restarChanges(){
		setMsnModalOK("¿Está seguro de Cancelar?");
		setModalCancel(true);
		
	}

	public  String html2text(String html) {
		return Jsoup.parse(html).text();
	}

	public void contCatac() {
		getLine();
		System.out.println("Caracteres: " + getLine());
		if(line>=4000){
			if(!isModal()){
				setMsnModalOK("Ha excedido el límite de caracteres.");
				setModal(true);
			}
		}
	}

	public int getLine() {
		setLine(html2text(textHtml).replaceAll(" ", "").length());
		return line;
	}

	public void hideModal() {
		setModalCancel(false);
		setModal(false);
		setModalOption(false);
		setMsnModalOK("");
	
	}

	public UserLogged getUsrs() {
		return usrs;
	}

	public void setUsrs(UserLogged usrs) {
		this.usrs = usrs;
	}

	public String getThemeOne() {
		System.out.println("getThemeOne:");
		ArrayList<String> list=new ArrayList<String>();
		while (themeOne.equals("") && count <= 3) {
			list = (textEditorManagerEJB.getOptionToolBarrEditorText(count,
					nameEditor));
			if(list.size()>1){				
				themeOne=list.get(0);	
				themeFour=list.get(1);
			}else{
				themeOne=list.get(0);
			}
			System.out.println("Contador themeone: " + count);
			count++;
		}
		return themeOne;
	}

	public void setThemeOne(String themeOne) {
		this.themeOne = themeOne;
	}

	public String getThemeTwo() {
		System.out.println("getThemeTwo:");
		ArrayList<String> list=new ArrayList<String>();
		while (themeTwo.equals("") && count <= 3) {
			list=  (textEditorManagerEJB.getOptionToolBarrEditorText(count,
					nameEditor));
		
			for(String a:list){
				themeTwo=a;
			}
			System.out.println("Contador themeTwo: " + count);
			count++;
		}
		return themeTwo;
	}

	public void setThemeTwo(String themeTwo) {
		this.themeTwo = themeTwo;
	}

	public String getThemeTree() {
		System.out.println("getThemeTree:");
		ArrayList<String> list=new ArrayList<String>();
		while (themeTree.equals("") && count <= 3) {
			list = (textEditorManagerEJB.getOptionToolBarrEditorText(
					count, nameEditor));
			System.out.println("Contador themeTree: " + count);
			themeTree=list.get(0);
			System.out.println("datos traido de la barra 3"+themeTree);
			
			count++;
		}
		return themeTree;
	}
	
	public void ocult (){
		setPanelEdit(false);
		hideModal();
		setModal(false);
		setModalCancel(false);
		panelEditModal();
		
	}

	public void setThemeTree(String themeTree) {
		this.themeTree = themeTree;
	}

	public String getThemeFour() {	
		
		System.out.println("getThemeFour:");
		ArrayList<String> list=new ArrayList<String>();
		if (themeFour.equals("") && count <=3) {
			list = (textEditorManagerEJB.getOptionToolBarrEditorText(
					count, nameEditor));
			System.out.println("Contador themeFour: " + count);
			themeFour=list.get(0);
			count++;				
		}
		return themeFour;
	}

	public void setThemeFour(String themeFour) {
		this.themeFour = themeFour;
	}

	public String getThemeFont() {
		return themeFont;
	}

	public void setThemeFont(String themeFont) {
		this.themeFont = themeFont;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getTextHtml() {
		return textHtml;
	}

	public void setTextHtml(String textHtml) {
		this.textHtml = textHtml;
	}

	public boolean isModal() {
		return modal;
	}

	public void setModal(boolean modal) {
		this.modal = modal;
	}

	public boolean isModalOption() {
		return modalOption;
	}

	public void setModalOption(boolean modalOption) {
		this.modalOption = modalOption;
	}

	public String getMsnModalOK() {
		return msnModalOK;
	}

	public void setMsnModalOK(String msnModalOK) {
		this.msnModalOK = msnModalOK;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public boolean isPanelEdit() {
		return panelEdit;
	}

	public void setPanelEdit(boolean panelEdit) {
		this.panelEdit = panelEdit;
	}

	public boolean isModalCancel() {
		return modalCancel;
	}

	public void setModalCancel(boolean modalCancel) {
		this.modalCancel = modalCancel;
	}

}
