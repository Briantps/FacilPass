/**
 * 
 */
package ejb.taskeng.workflow.impl;

import java.io.Serializable;
import java.util.ArrayList;

import ejb.taskeng.workflow.Action;
import ejb.taskeng.workflow.Step;



/**
 * Implementacion de Step. El identificador esta definido como int,
 * el nombre con un objeto String, la lista de acciones es de la clase
 * ArrayList<Action> y el estado inicial se define tambien como int.
 * 
 * @author Mauricio Gil
 */
public class StepBase implements Step, Serializable {
	private static final long serialVersionUID = -639422274746391891L;
	private int id;
	private String name = "";
	private ArrayList<Action> actions = new ArrayList<Action>();
	private int stateInit = 0;
	
	public Object clone(){
		Step obj = new StepBase();
		obj.setId(this.id);
		obj.setName(new String(this.name));
		ArrayList<Action> list = obj.getActions();
		for (Action a : actions) {
			list.add((Action) a.clone());
		}
		
		obj.setStateInit(this.stateInit);
		return obj;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("ID=" + this.id);
		sb.append(";name=" + this.name);
		sb.append(";actions=" + this.actions);
		sb.append(";stateInit=" + this.stateInit);
		sb.append("}");
		
		return sb.toString();
	}
	
	/**
	 * Obtiene la primer accion que realiza este paso.
	 * @return Si la lista de acciones no contiene elementos, este metodo retorna null.
	 * En caso contrario, retorna el primer elemento de dicha lista.
	 */
	public Action getFirst(){
		if (actions.size() > 0) {
			return actions.iterator().next();
		}
		return null;
	}
	
	public boolean equals(Object obj){
		if(obj instanceof StepBase){
			StepBase other = (StepBase)obj;
			return this.name.equalsIgnoreCase(other.name);
		}
		return false;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setActions(ArrayList<Action> actions) {
		this.actions = actions;
	}

	public ArrayList<Action> getActions() {
		System.out.println("StepBase.getActions. Size: " + actions.size());
		return actions;
	}

	public int getActionCount(){
		return actions.size();
	}
	
	/**
	 * Agrega una accion a este paso. Si la accion ya existe,
	 * este metodo no tiene efecto.
	 * @param action
	 */
	public void addAction(Action action){
		if(!actions.contains(action)){
			actions.add(action);
		}
	}

	public int getStateInit() {
		return stateInit;
	}

	public void setStateInit(int stateInit) {
		this.stateInit = stateInit;
	}
}