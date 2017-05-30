package mBeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import jpa.TbTypeRole;

import org.jsoup.Jsoup;

import security.UserLogged;
import sessionVar.SessionUtil;
import validator.Validation;
import ejb.DataPolicies;
import ejb.Role;
import ejb.TextEditorManager;

/**
 * @author Jgomez
 */
public class DataPoliciesBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB(mappedName = "ejb/Role")
	private Role RoleEJB;

	@EJB(mappedName = "ejb/DataPolicies")
	private DataPolicies dataPoliciesEJB;

	@EJB(mappedName = "ejb/TextEditorManager")
	private TextEditorManager textEditorManagerEJB;

	private UserLogged usrs;
	private String ip;
	private List<SelectItem> listUserMessage;
	private Long roleId;
	private String textHtml;
	
	private boolean panelEdit;
	private boolean modal;
	private boolean modalOption;
	private String msnModalOK;
	private String themeOne = "";
	private String themeTwo = "";
	private String themeTree = "";
	private String themeFour = "";

	private String themeFont;
	public int count = 1;
	public boolean validateEditorOne = true;
	public String nameEditor = "dataPolicies";
	private int line;

	public DataPoliciesBean() {
		usrs = (UserLogged) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("user");
		ip = SessionUtil.ip();
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

	}

	public List<SelectItem> getListUserMessage() {
		if (listUserMessage == null) {
			listUserMessage = new ArrayList<SelectItem>();
		} else {
			listUserMessage.clear();
		}
		listUserMessage.add(new SelectItem(-1L, "--Seleccione Uno--"));
		for (TbTypeRole tt : RoleEJB.getTypeRoleList()) {

			if (tt.getTypeRoleId() != 1L) {
				listUserMessage.add(new SelectItem(tt.getTypeRoleId(), tt
						.getTypeRoleDescription()));
			}

		}
		return listUserMessage;
	}

	public void panelEditModal() {
		if (roleId > 0L) {
			setTextHtml(dataPoliciesEJB.getTextHTML(roleId));
			setPanelEdit(true);
		} else {
			ocult();
		}

	}

	public void ocult() {
		setPanelEdit(false);
		setTextHtml("");
		hideModal();
		setRoleId(-1L);
	}

	public void createPolicy() {
		hideModal();

		setMsnModalOK(dataPoliciesEJB.setCreateTXT(usrs.getUserId(), textHtml,
				ip, roleId));
		panelEditModal();
		setModal(true);

	}

	public boolean preValidate(String html) {
		String Text;
		try {
			Text = html2text(html);
			System.out.println("El texto es ----------> " + Text);
			if (Text.replaceAll(" ", "").length() > 4000) {
				setMsnModalOK("El mensaje no debe contener más de 4000 caracteres");
				return false;
			} else if (!Validation.isObservationEditorText(Text)) {
				setMsnModalOK("El mensaje contiene caracteres Inválidos");
				return false;
			} else if (Text.equals(" ") || Text.equals(null)) {
				setMsnModalOK("El mensaje no puede estar vacío");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error en DataPoliciesBean.preValidate");
			return false;
		}

		return true;

	}

	public void hideModal() {
		setModal(false);
		setModalOption(false);
		setMsnModalOK("");
	}

	public void confirmation() {
		String beforeText = textHtml.replaceAll("&nbsp;", "");
		String text = html2text(beforeText);

		text.replaceAll("^\\s", "").replaceAll("  ", "");
		text.replaceAll("&nbsp;", "");
		System.out.print("mensaje es ::::" + text + "tamaño del trexto "
				+ text.length());

		if (text.toString().trim().length() == 0) {
			setMsnModalOK("El mensaje no puede estar vacío.");
			setModal(true);
		} else {
			if (preValidate(textHtml)) {
				System.out
						.println("Entre a Confirmar los cambios para guardar");
				hideModal();
				setMsnModalOK("¿Está seguro de guardar los cambios?");
				setModalOption(true);

			} else {
				setModal(true);
			}

		}

	}

	public void cancelEdit() {
		panelEditModal();
	}

	public String getTextHtml() {
		return textHtml;
	}

	public void setTextHtml(String textHtml) {
		this.textHtml = textHtml;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public void setListUserMessage(List<SelectItem> listUserMessage) {
		this.listUserMessage = listUserMessage;
	}

	public boolean isPanelEdit() {
		return panelEdit;
	}

	public void setPanelEdit(boolean panelEdit) {
		this.panelEdit = panelEdit;
	}

	public String getMsnModalOK() {
		return msnModalOK;
	}

	public void setMsnModalOK(String msnModalOK) {
		this.msnModalOK = msnModalOK;
	}

	public String getThemeOne() {
		System.out.println("getThemeOne:");
		ArrayList<String> list = new ArrayList<String>();
		while (themeOne.equals("") && count <= 3) {
			list = (textEditorManagerEJB.getOptionToolBarrEditorText(count,
					nameEditor));
			if (list.size() > 1) {
				themeOne = list.get(0);
				themeFour = list.get(1);
			} else {
				themeOne = list.get(0);
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
		ArrayList<String> list = new ArrayList<String>();
		while (themeTwo.equals("") && count <= 3) {
			list = (textEditorManagerEJB.getOptionToolBarrEditorText(count,
					nameEditor));

			for (String a : list) {
				themeTwo = a;
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
		ArrayList<String> list = new ArrayList<String>();
		while (themeTree.equals("") && count <= 3) {
			list = (textEditorManagerEJB.getOptionToolBarrEditorText(count,
					nameEditor));
			System.out.println("Contador themeTree: " + count);
			themeTree = list.get(0);
			System.out.println("datos traido de la barra 3" + themeTree);

			count++;
		}
		return themeTree;
	}

	public void setThemeTree(String themeTree) {
		this.themeTree = themeTree;
	}

	public String getThemeFour() {

		System.out.println("getThemeFour:");
		ArrayList<String> list = new ArrayList<String>();
		if (themeFour.equals("") && count <= 3) {
			list = (textEditorManagerEJB.getOptionToolBarrEditorText(count,
					nameEditor));
			System.out.println("Contador themeFour: " + count);
			themeFour = list.get(0);
			count++;
		}
		return themeFour;
	}

	public void setThemeFour(String themeFour) {
		this.themeFour = themeFour;
	}

	public static String html2text(String html) {
		return Jsoup.parse(html).text();
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

	public int getLine() {
		setLine(html2text(textHtml).replaceAll(" ", "").length());
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public void contCatac() {	
		getLine();
		System.out.println("Caracteres: " + getLine());
		if(line>=4000){
			if(!isModal()){
				setMsnModalOK("Ha excedido el límite de caracteres.");
				setModal(true);
			}
		}else{
			
		}
	}

	public String getThemeFont() {
		return themeFont;
	}

	public void setThemeFont(String themeFont) {
		this.themeFont = themeFont;
	}
}
