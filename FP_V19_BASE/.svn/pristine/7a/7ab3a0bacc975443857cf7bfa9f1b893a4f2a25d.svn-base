package mBeans;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import ejb.Action;
import ejb.Option;


/**
 * @author cavellaneda
 */
public class AdminOptMBean implements Serializable{
	private static final long serialVersionUID = 199399153082288754L;

	// Attributes -----------------------------------------------------------------------

	@EJB(mappedName="ejb/Option")
	private Option optEjb;
	
	@EJB(mappedName="ejb/Action")
	private Action actEjb;

	private String opt;
	
	private String optMod;

	private String optId;

	private String msg;

	private List<jpa.TbOption> listOpt;
	
	private List<util.Permission> listOptAct;

	private boolean create;

	private boolean error;

	private boolean modificate;
	
	private boolean update;
	
	private boolean afterUpdate;
	private boolean validate;
	private boolean editPermission;
	
	// Constructor ----------------------------------------------------------------------

	public AdminOptMBean() {
			setListOpt(new ArrayList<jpa.TbOption>());
			setListOptAct(new ArrayList<util.Permission>());
	}

	// Actions --------------------------------------------------------------------------
	
	/**
	 * Saves an Option.
	 */
	public String saveOpt() {
		if(opt == "" || opt.trim().equals("")){
			msg = "Falta el nombre de la Opción";
			error = true;
		}else {
			if( !opt.matches("([a-z]|[A-Z]|[áéíóú]|[ÁÉÍÓÚ]|[ñÑ]|\\s)+")){
				setMsg("El nombre Opción tiene caracteres inválidos. Verifique.");
				setError(true);
			}else{
				if(opt.toString().length()>50){
					setMsg("El nombre Opción no puede tener más de 50 caracteres. Verifique.");
					setError(true);
				}else{
			if (optEjb.createOption(opt)) {
				setMsg("La Opción: "+ opt +", ha sido creada con éxito.");
				clearVar();
				setCreate(true);
			} else {
				setMsg("Existe una Opción con el mismo nombre. Verifique.");
				setError(true);
			}
				}
			}
		}
		return null;
	}

	/**
	 * Prepares to change an Option.
	 */
	public String changeOpt() {
		setModificate(false);
		for (jpa.TbOption optObj : listOpt) {
			if (optObj.getOptionId() == Long.parseLong(optId)) {
				optMod = optObj.getOptionName();
				this.update = true;
			}
		}
		return null;
	}

	/**
	 * Updates an Option.
	 */
	public String updateOpt() {
		this.update =  false;
		if(optMod == null || optMod.trim().equals("")){
			msg = "Falta el nombre de la Opción";
			afterUpdate = true;
		}else {
			if( !optMod.matches("([a-z]|[A-Z]|[áéíóú]|[ÁÉÍÓÚ]|[ñÑ]|\\s)+")){
				setMsg("El nombre Opción tiene caracteres inválidos. Verifique.");
				setError(true);
				System.out.print("adminOpt1::   "+optMod+"  Size:  "+optMod.toString().length());
			}else{
				if(optMod.toString().length() > 50){
					System.out.print("adminOpt2::   "+optMod+"  Size:  "+optMod.toString().length());
					setMsg("El nombre Opción no puede tener más de 50 caracteres. Verifique.");
					setError(true);
				}else{
			if (optEjb.updateOption(Long.parseLong(optId), optMod)) {
				System.out.print("adminOpt3::   "+opt+"  Size:  "+optMod.toString().length());
				setMsg("La Opción: " + optMod + ", ha sido Modificada con éxito.");
			
			} else {
				System.out.print("adminOpt4::   "+optMod+"  Size:  "+optMod.toString().length());
				setMsg("Existe una Opción con el mismo nombre. Verifique.");			
			}
			setAfterUpdate(true);
		}
			}
		}
		return null;
	}

	/**
	 * Clear variables.
	 */
	private void clearVar() {
		optId = "";
		opt = "";
	}

	/**
	 * Control Modal Panel.
	 */
	public String hideModal() {
		setCreate(false);
		setError(false);
		setModificate(false);
		setUpdate(false);
		setEditPermission(false);
		setAfterUpdate(false);
		return null;
	}
	
	/**
	 * Gets a list of actions by an option.
	 */
	public String assigAct(){
		setModificate(false);
		listOptAct = (List<util.Permission>) actEjb.getActionsByOption(Long.parseLong(optId));
		this.editPermission = true;
		return null;
	}
	
	/**
	 * Creates an opt-act
	 */
	public String assigOptAct(){
		this.editPermission = false;
		for(util.Permission p: listOptAct){
			if(p.isActive() && p.getOptAct().getBehavior().trim().length() < 1){
				
				p.getOptAct().setBehavior("");
				setMsg("Escriba la regla de navegación para la opción-acción: "
						+ p.getOptAct().getOptionId().getOptionName() + "-"
						+ p.getOptAct().getActionId().getActionName() + ".");
				setError(true);
				return null;
				
			} else if (!p.isActive()){
				String result = actEjb.valRoleOptionAction(p.getOptAct()
						.getOptionId().getOptionId(), p.getOptAct()
						.getActionId().getActionId());
				if (result.length() > 0) {
						p.setActive(true);
					setMsg("Existen los siguientes roles: " + result + " asociados con la opción: "
							+ p.getOptAct().getOptionId().getOptionName() + "-"
							+ p.getOptAct().getActionId().getActionName() + ". " +
									"Debe eliminar la asociación para poder eliminar la relación.");
					setError(true);
					return null;				
				}
				boolean exist = actEjb.existRelation(p.getOptAct()
						.getOptionId().getOptionId(), p.getOptAct()
						.getActionId().getActionId());
				if((exist==false) && (p.getOptAct().getBehavior().trim().length() > 0)){
					setMsg("Debe seleccionar Asignar para la opción-acción: "
							+ p.getOptAct().getOptionId().getOptionName() + "-"
							+ p.getOptAct().getActionId().getActionName() + ".");
					setError(true);
					return null;
				}
				
			}else if(p.isActive() && p.getOptAct().getBehavior().trim().length() > 0){
				String result = actEjb.valBehavior(p.getOptAct()
						.getOptionId().getOptionId(), p.getOptAct()
						.getActionId().getActionId(),p.getOptAct().getBehavior());
				if(result.length() > 0){
					p.getOptAct().setBehavior("");
					setMsg("La regla de navegación que esta ingresando ya la esta usando otra opción. Verifique.");
					setError(true);
					return null;	
				}
			}			
		}
		
		if(actEjb.checkIfAnyOptAct(listOptAct, Long.parseLong(optId))){
			for(util.Permission p:listOptAct){//
				System.out.println("getOptAct"+p.getOptAct().getBehavior());
			
				if(p.getOptAct().getBehavior().length()>100){	
					setValidate(true);
					setMsg("Error. La regla de Navegación no puede ser mayor a 100" +
							" caracteres Por favor Verifique la Acción :" +
							" "+p.getOptAct().getActionId().getActionName());
					setModificate(true);
					break;
				}else{
					setValidate(false);
				}
			}
			if(!isValidate()==true) 
			if (actEjb.updateOptAct(listOptAct)) {
				setMsg("Las asignaciones han sido Modificadas con éxito.");
				setModificate(true);
			} else {			
				setMsg("Se ha producido un error durante la Transacción. " +
						  "Recuerde que la regla de Navegación debe ser única.");
				setError(true);
			}
			listOptAct = (List<util.Permission>) actEjb.getActionsByOption(Long.parseLong(optId));
		}else{
			setMsg("No se ha realizado ninguna Transacción. No ha cambiado ");
			setError(true);
		}
		return null;
	}

	// Getters and Setters ----------------------------------------------------------------
	
	/**
	 * @param opt the opt to set
	 */
	public void setOpt(String opt) {
		this.opt = opt;
	}

	/**
	 * @return the opt
	 */
	public String getOpt() {
		return opt;
	}

	/**
	 * @param optId the optId to set
	 */
	public void setOptId(String optId) {
		this.optId = optId;
	}

	/**
	 * @return the optId
	 */
	public String getOptId() {
		return optId;
	}

	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param listOpt the listOpt to set
	 */
	public void setListOpt(List<jpa.TbOption> listOpt) {
		this.listOpt = listOpt;
	}

	/**
	 * @return the listOpt
	 */
	public List<jpa.TbOption> getListOpt() {
		listOpt = (List<jpa.TbOption>) optEjb.getAllOptions();
		return listOpt;
	}

	/**
	 * @param create the create to set
	 */
	public void setCreate(boolean create) {
		this.create = create;
	}

	/**
	 * @return the create
	 */
	public boolean isCreate() {
		return create;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(boolean error) {
		this.error = error;
	}

	/**
	 * @return the error
	 */
	public boolean isError() {
		return error;
	}

	/**
	 * @param modificate the modificate to set
	 */
	public void setModificate(boolean modificate) {
		this.modificate = modificate;
	}

	/**
	 * @return the modificate
	 */
	public boolean isModificate() {
		return modificate;
	}

	/**
	 * @param listOptAct the listOptAct to set
	 */
	public void setListOptAct(List<util.Permission> listOptAct) {
		this.listOptAct = listOptAct;
	}

	/**
	 * @return the listOptAct
	 */
	public List<util.Permission> getListOptAct() {
		return listOptAct;
	}

	/**
	 * @param optMod the optMod to set
	 */
	public void setOptMod(String optMod) {
		this.optMod = optMod;
	}

	/**
	 * @return the optMod
	 */
	public String getOptMod() {
		return optMod;
	}

	/**
	 * @param update the update to set
	 */
	public void setUpdate(boolean update) {
		this.update = update;
	}

	/**
	 * @return the update
	 */
	public boolean isUpdate() {
		return update;
	}

	/**
	 * @param editPermission the editPermission to set
	 */
	public void setEditPermission(boolean editPermission) {
		this.editPermission = editPermission;
	}

	/**
	 * @return the editPermission
	 */
	public boolean isEditPermission() {
		return editPermission;
	}

	/**
	 * @param afterUpdate the afterUpdate to set
	 */
	public void setAfterUpdate(boolean afterUpdate) {
		this.afterUpdate = afterUpdate;
	}

	/**
	 * @return the afterUpdate
	 */
	public boolean isAfterUpdate() {
		return afterUpdate;
	}

	public boolean isValidate() {
		return validate;
	}

	public void setValidate(boolean validate) {
		this.validate = validate;
	}
	
}