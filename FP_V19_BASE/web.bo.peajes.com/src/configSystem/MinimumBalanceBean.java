package configSystem;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import jpa.TbCategory;
import security.UserLogged;
import sessionVar.SessionUtil;
import util.TbMinimumBalance;
import validator.Validation;
import ejb.Category;
import ejb.MinimumBalance;


public class MinimumBalanceBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@EJB(mappedName="ejb/Category")
	private Category CategoryEJB;
	
	@EJB(mappedName="ejb/MinimumBalance")
	private MinimumBalance MinimumBalanceEJB;
	
	private Long categoryId;
	private String valor = "0";
	private String saldoMinimo = "0";
	private String observacion;
	private List<SelectItem> categoryList;
	private String namebutton;
	private UserLogged usrs;
	private String ip;
	private String idMinimum;
	private boolean permEdita;
	private boolean permAprueba;
	private boolean permCreate;
	private boolean activeButton;
	private String nameperm;
	private ArrayList<TbMinimumBalance> categoryrates;
	private boolean modal1;
	private boolean modalOption;
	private boolean modalOptionCreEdit;
	private String msnmodal;
	public String state;
	
	
	/** Constructor **/
	public MinimumBalanceBean (){
		usrs = (UserLogged) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");	
		ip=SessionUtil.ip();
		namebutton = "Crear";
	}
	
	@PostConstruct
	public void init (){
		if (usrs.getUserId() != null) {
			
			setActiveButton(true);
			
			nameperm = "editMinimumBalance";
			permEdita = MinimumBalanceEJB.getpermission(usrs.getUserId(),nameperm); 
			System.out.println("Permiso de Editar " + permEdita);
			
			nameperm = "aprobeMinimumBalance";
			permAprueba = MinimumBalanceEJB.getpermission(usrs.getUserId(),nameperm);
			System.out.println("Permiso de Aprobar " + permAprueba);
			
			nameperm = "createMinimumBalance";
			permCreate = MinimumBalanceEJB.getpermission(usrs.getUserId(),nameperm);
			System.out.println("Permiso de Crear " + permCreate);
			
			categoryrates = new ArrayList<TbMinimumBalance> ();
			categoryrates = MinimumBalanceEJB.getCategoryRates ();
			
			
		} 
		
	}
	
	
	public List<SelectItem> getcategoryList() {
		if(categoryList == null){
			categoryList = new ArrayList<SelectItem>();
		}else{
			categoryList.clear();
		}
		categoryList.add(new SelectItem(-1L,"-Seleccione una Opción-"));
		for(TbCategory catego : CategoryEJB.getCategories()){
			categoryList.add(new SelectItem(catego.getCategoryCode(), catego.getCategoryName()));
		}
		return categoryList;
	}
	
	public void createeditCat (){
		setModalOptionCreEdit(false);
		if (namebutton!=null){
			if(namebutton.equals("Crear")){
						setMsnmodal(MinimumBalanceEJB.getcreate(categoryId,valor,saldoMinimo,observacion,usrs.getUserId(),ip));
						System.out.println("Respuesta ------> "+msnmodal);
						init();
						setModal1(true);						
									
			}else if (namebutton.equals("Editar")) {
					setMsnmodal(MinimumBalanceEJB.getEdit(valor,saldoMinimo,observacion,usrs.getUserId(),idMinimum,ip));
					System.out.println("Respuesta ------> "+msnmodal);
					init();
					setModal1(true);					
			}else {
				System.out.println("Error No se Reconoce la accion");
			}
			
			
			
		}
		
		cancelCat();
		
	}
	
	public boolean validateinf () {
		
		if (categoryId == -1L || categoryId == null){
			setMsnmodal("Falta seleccionar una categoría");
			
		}
		else if (MinimumBalanceEJB.getExitsCategory(categoryId) && !namebutton.equals("Editar")){
			setMsnmodal("La categoría " + categoryId + ", ya se encuentra registrada en el sistema");
		}
		else if (valor.equals(null) || valor.equals("")){
			setMsnmodal("El campo Valor no puede estar vacío");
			
		}
		else if (!Validation.isNumeric(valor.replace(".", ""))){
			setMsnmodal("El campo Valor contiene caracteres inválidos");
			
		}
		else if (Long.parseLong(valor.replace(".", ""))<=0){
			this.valor = "0";
		    this.saldoMinimo="0";
			setMsnmodal("El campo Valor debe contener valores superiores a cero (0)");		
			
		}
		else if (valor.replace(".", "").length()> 12){
			setMsnmodal("El campo Valor debe contener valores inferiores a  999.999.999.999");			
		}
		else if (saldoMinimo.equals(null)|| saldoMinimo.equals("")){
			setMsnmodal("El campo Saldo Mínimo no puede estar vacío");			
		}
		else if (!Validation.isNumeric(saldoMinimo.replace(".", ""))){
			setMsnmodal("El campo Saldo Mínimo contiene caracteres inválidos");			
		}
		else if (Long.parseLong(saldoMinimo.replace(".", ""))<=0){
			setMsnmodal("El campo Saldo Mínimo debe contener valores superiores a cero (0)");			
		}
		else if (saldoMinimo.replace(".", "").length()> 12){
			setMsnmodal("El campo Saldo Mínimo debe contener valores inferiores a  999.999.999.999");			
		}
		else if (observacion.equals(null) || observacion.trim().length()== 0){
			setMsnmodal("El campo Observación no puede estar vacío");
		}
		else if (!Validation.isObservationPSE(observacion)) {
			setMsnmodal("El campo Observación contiene caracteres inválidos");
		}		
		else if (observacion.length()> 200){
			setMsnmodal("El campo Observación no debe contener más de 200 caracteres");
				
		}else {
			if(!saldoMinimo.equals("")){
				if(saldoMinimo!="0"){
					for(int i=0;i<=saldoMinimo.length();i++){
						saldoMinimo=saldoMinimo.replace(".", "");	
						System.out.println("SALDO MINIMO: "+saldoMinimo);
					}
					String sMin=saldoMinimo.replaceFirst("^0*", "").trim();
					if(!sMin.isEmpty()){

						DecimalFormat format=new DecimalFormat("#,###.###");
						setSaldoMinimo(format.format(Long.valueOf(sMin)));
					}
				}
			}
			if(!valor.equals("")){
				if(valor!="0"){
					for(int i=0;i<=valor.length();i++){
						valor=valor.replace(".", "");	
						System.out.println("VALOR: "+valor);
					}
					String sValue=valor.replaceFirst("^0*", "").trim();
					System.out.print("sValor:  "+ sValue);
					if(!sValue.isEmpty()){
						DecimalFormat format=new DecimalFormat("#,###.###");
						setValor(format.format(Long.valueOf(sValue)));
					}
					System.out.print("saldo minimo"+sValue);
				}
			}
			
			return true;
		}
		return false;
	}
	
	public void editCategory (){
		for (int i = 0; i < categoryrates.size(); i++){
			util.TbMinimumBalance value = categoryrates.get(i);
			value.setEditar("Editar");
			setObservacion("");
			if (value.getCategory()!= null){
				if (Long.parseLong(value.getCategory())==(categoryId)){
					namebutton = "Editar";
					value.setEditar("Editando");
					setPermCreate(true);
				}
			}
		}
	}
	
	public void cancelCat (){
		System.out.println("Entre a limpiar Variables");
		setModalOption(false);
		setModalOptionCreEdit(false);		
		setCategoryId(-1L);
		setValor("0");
		setSaldoMinimo("0");
		setObservacion("");
		setNamebutton("Crear");
		setIdMinimum("0");
		init();
				
	}
	
	public void createEditCatOption (){
	
		if (namebutton!=null){
			if(namebutton.equals("Crear")){
				if (validateinf()){
				setMsnmodal("Está seguro de Crear el saldo mínimo por valor de $" + getSaldoMinimo() + " para la categoría " + getCategoryId());
				setModalOptionCreEdit(true);
				}else{
					setModal1(true);
				}
			}else if (namebutton.equals("Editar")) {
				if (validateinf()){
				setMsnmodal("Está seguro de editar  el saldo mínimo por valor de $" + getSaldoMinimo() + " para la categoría " + getCategoryId());
				setModalOptionCreEdit(true);
				}else{
					setModal1(true);
				}
			}else {
				System.out.println("Error No se Reconoce la accion");
			}
			
		}
	}
	
	public void aprobCategoryOption (){
		setMsnmodal("Está seguro de autorizar el saldo mínimo por valor de $" + getSaldoMinimo() + " para la categoría " + getCategoryId());
		setModalOption(true);
	}
	
	public void aprobCategory (){
		setModalOption(false);
		String resp;
		if (state.equals("Aprobado")){
			setMsnmodal("EL registro ya se encuentra aprobado");
			setModal1(true);
		}else if (state.equals("Creado") || state.equals("Editado")){
			System.out.println("Entre a arpobar la transaccion");
			resp=MinimumBalanceEJB.getapprovesCategory(idMinimum,saldoMinimo,usrs.getUserId(),ip);
			init();
			
			setMsnmodal(resp);
			setModal1(true);
			
			/*
			if(resp){
				setMsnmodal("La categoría se ha aprobado con éxito");
				setModal1(true);
			}else{
				setMsnmodal("Error al aprobar la categoría");
				setModal1(true);
			}
			*/
		}else {
			setMsnmodal("Error en la transaccion no se reconoce el estado");
			setModal1(true);
		}
		cancelCat();		
	}
	
	public void ocult (){
		setMsnmodal("");
		setModal1(false);
		setModalOption(false);
		setModalOptionCreEdit(false);
	}
	
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public List<SelectItem> getCategoryList() {
		return categoryList;
	}
	public void setCategoryList(List<SelectItem> categoryList) {
		this.categoryList = categoryList;
	}
	public String getValor() {
		
		//Se comentarea ya que ocacionan un error cuando se intenta agregar caracteres invalidos
		/*if(!valor.equals("")){
		if(valor!="0"){
		for(int i=0;i<=valor.length();i++){
			valor=valor.replace(".", "");	
			System.out.println("VALOR: "+valor);
		}
		String sValue=valor.replaceFirst("^0*", "").trim();
		System.out.print("sValor:  "+ sValue);
		if(!sValue.isEmpty()){
		DecimalFormat format=new DecimalFormat("#,###.###");
		valor=format.format(Long.valueOf(sValue));
		}
	    System.out.print("saldo minimo"+sValue);
		}
		}*/
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String getSaldoMinimo() {
		//Se comentarea ya que ocacionan un error cuando se intenta agregar caracteres invalidos
		/*if(!saldoMinimo.equals("")){
		if(saldoMinimo!="0"){
		for(int i=0;i<=saldoMinimo.length();i++){
			saldoMinimo=saldoMinimo.replace(".", "");	
			System.out.println("SALDO MINIMO: "+saldoMinimo);
		}
		String sMin=saldoMinimo.replaceFirst("^0*", "").trim();
		if(!sMin.isEmpty()){
		
		DecimalFormat format=new DecimalFormat("#,###.###");
		saldoMinimo=format.format(Long.valueOf(sMin));
		}
		}
		}*/
		return saldoMinimo;
	}
	public void setSaldoMinimo(String saldoMinimo) {
		this.saldoMinimo = saldoMinimo;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public String getNamebutton() {
		return namebutton;
	}
	public void setNamebutton(String namebutton) {
		this.namebutton = namebutton;
	}
	public UserLogged getUsrs() {
		return usrs;
	}
	public void setUsrs(UserLogged usrs) {
		this.usrs = usrs;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public boolean isPermEdita() {
		return permEdita;
	}
	public void setPermEdita(boolean permEdita) {
		this.permEdita = permEdita;
	}
	public boolean isPermAprueba() {
		return permAprueba;
	}
	public void setPermAprueba(boolean permAprueba) {
		this.permAprueba = permAprueba;
	}
	public String getNameperm() {
		return nameperm;
	}
	public void setNameperm(String nameperm) {
		this.nameperm = nameperm;
	}

	public ArrayList<TbMinimumBalance> getCategoryrates() {
		return categoryrates;
	}

	public void setCategoryrates(ArrayList<TbMinimumBalance> categoryrates) {
		this.categoryrates = categoryrates;
	}

	public boolean isActiveButton() {
		return activeButton;
	}

	public void setActiveButton(boolean activeButton) {
		this.activeButton = activeButton;
	}

	public boolean isModal1() {
		return modal1;
	}

	public void setModal1(boolean modal1) {
		this.modal1 = modal1;
	}

	public String getMsnmodal() {
		return msnmodal;
	}

	public void setMsnmodal(String msnmodal) {
		this.msnmodal = msnmodal;
	}

	public boolean isPermCreate() {
		return permCreate;
	}

	public void setPermCreate(boolean permCreate) {
		this.permCreate = permCreate;
	}

	public String getIdMinimum() {
		return idMinimum;
	}

	public void setIdMinimum(String idMinimum) {
		this.idMinimum = idMinimum;
	}

	public boolean isModalOption() {
		return modalOption;
	}

	public void setModalOption(boolean modalOption) {
		this.modalOption = modalOption;
	}

	public boolean isModalOptionCreEdit() {
		return modalOptionCreEdit;
	}

	public void setModalOptionCreEdit(boolean modalOptionCreEdit) {
		this.modalOptionCreEdit = modalOptionCreEdit;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	

}
