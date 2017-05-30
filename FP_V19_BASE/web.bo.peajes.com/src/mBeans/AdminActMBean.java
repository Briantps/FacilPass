package mBeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import ejb.Action;


/**
 * @author cavellaneda
 * 
 */
public class AdminActMBean implements Serializable{
private static final long serialVersionUID = -5192374787432066873L;

   // Attributes ------------------------------------------------------------------------------------------------------

	@EJB(mappedName="ejb/Action")
	private Action actEjb;

	private String act;
	
	private String actMod;

	private String actId;

	private String msg;

	private List<jpa.TbAction> listAct;

	private boolean create;

	private boolean error;

	private boolean modificate;	
	
	private boolean update;

	// Constructor ----------------------------------------------------------------------------------------------------
	
	public AdminActMBean() {
		setListAct(new ArrayList<jpa.TbAction>());
	}

	// Actions --------------------------------------------------------------------------------------------------------
	
	/**
	 * Saves an Action.
	 */
	public String saveAct() {
		if(act==null || act.trim().equals("")){
			msg="Falta nombre de la Acción";
			error = true;
		}else {
			if( !act.matches("([a-z]|[A-Z]|[áéíóú]|[ÁÉÍÓÚ]|[ñÑ]|\\s)+")){
				setMsg("El nombre Acción tiene caracteres inválidos. Verifique.");
				setError(true);
			}else
				if(act.toString().length()>50){
					setMsg("El nombre Acción no puede tener más de 50 caracteres. Verifique.");
					setError(true);
				}else
			if (actEjb.createAction(act)) {
				setMsg("La Acción: " + act + ", ha sido creada con éxito.");
				clearVar();
				setCreate(true);
			} else {
				setMsg("Existe una Acción con el mismo nombre. Verifique.");
				setError(true);
			}
		}
		return null;
	}

	/**
	 * Prepares to change an action.
	 */
	public String changeAct() {
		setModificate(false);
		for (jpa.TbAction actObj : listAct) {
			if (actObj.getActionId() == Long.parseLong(actId)) {
				actMod = actObj.getActionName();
				this.update = true;			
			}
		}
		return null;
	}

	/**
	 * Updates an action.
	 */
	public String updateAct() {
		this.update = false;
		if(actMod == null || actMod.trim().equals("")){
			msg="Falta nombre de la Acción";
			error = true;
		}else {
			if( !actMod.matches("([a-z]|[A-Z]|[áéíóú]|[ÁÉÍÓÚ]|[ñÑ]|\\s)+")){
				setMsg("El nombre Acción tiene caracteres inválidos. Verifique.");
				setError(true);
			}else{
				if(actMod.toString().length()>50){
					setMsg("El nombre Acción no puede tener más de 50 caracteres. Verifique.");
					setError(true);
				}else{
			if (actEjb.updateAction(Long.parseLong(actId), actMod)) {
				setMsg("La Acción: " + actMod + ", ha sido Modificada con éxito.");
				setModificate(true);
			} else {
				setMsg("Existe una Acción con el mismo nombre. Verifique.");
				setError(true);
			}
			}
			}
		}
		return null;
	}

	/**
	 * Clear Variables.
	 */
	private void clearVar() {
		actId = "";
		act = "";
	}

	/**
	 * Control modal panel.
	 */
	public String hideModal() {
		setCreate(false);
		setError(false);
		setModificate(false);
		setUpdate(false);
		return "succInsertAct";
	}
	
	// Getters and Setters ---------------------------------------------------------------------------------------------

	/**
	 * @param listAct the listAct to set
	 */
	public void setListAct(List<jpa.TbAction> listAct) {
		this.listAct = listAct;
	}

	/**
	 * @return the listAct
	 */
	public List<jpa.TbAction> getListAct() {
		listAct = (List<jpa.TbAction>) actEjb.getAllActions();
		return listAct;
	}

	/**
	 * @param msg  the msg to set
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
	 * @param actId  the actId to set
	 */
	public void setActId(String actId) {
		this.actId = actId;
	}

	/**
	 * @return the actId
	 */
	public String getActId() {
		return actId;
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
	 * @return the act
	 */
	public String getAct() {
		return act;
	}

	/**
	 * @param act the act to set
	 */
	public void setAct(String act) {
		this.act = act;
	}

	/**
	 * @param actMod the actMod to set
	 */
	public void setActMod(String actMod) {
		this.actMod = actMod;
	}

	/**
	 * @return the actMod
	 */
	public String getActMod() {
		return actMod;
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
}