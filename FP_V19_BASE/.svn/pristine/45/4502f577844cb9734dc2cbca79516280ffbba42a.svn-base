package ejb.taskeng.workflow.impl;

import java.io.Serializable;
import java.util.ArrayList;

import ejb.taskeng.workflow.Action;
import ejb.taskeng.workflow.ActionParam;

/**
 * Implementacion de Action. Maneja una lista interna de parametros
 * basada en ArrayList, y el nombre se guarda en una variable String.
 * 
 * @author Mauricio Gil
 */
public class ActionBase implements Action, Serializable {

	private static final long serialVersionUID = -1287800509715545153L;
	
	private ArrayList<ActionParam> params = new ArrayList<ActionParam>();
	private String name;
	
	public ActionBase(){
	}

	public Object clone(){
		Action obj = new ActionBase();
		obj.setName(new String(this.name));
		ArrayList<ActionParam> list = obj.getParams();
		for (ActionParam a : params) {
			list.add((ActionParam) a.clone());
		}		
		return obj;
	}
	
	/**
	 * Obtiene el primer parametro de esta accion.
	 * @returns Si la lista de parametros no contiene elementos, retorna null. 
	 * En caso contrario el primer elemento de la lista.
	 */
	public ActionParam getFirst(){
		if(params.size() > 0){
			return params.iterator().next();
		}
		return null;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("name=" + this.name);
		sb.append(";params=" + this.params);
		sb.append("}");
		return sb.toString();
	}
	
	/* (non-Javadoc)
	 * @see workflow.Action#setParams(java.util.List)
	 */
	public void setParams(ArrayList<ActionParam> params) {
		this.params = params;
	}

	public int getParamCount(){
		return params.size();
	}
	
	public ArrayList<ActionParam> getParams() {
		System.out.println("ActionBase.getParams. Size: " + params.size());
		return params;
	}
	
	/**
	 * Agrega un parametro a esta accion. Si el parametro ya existe en la lista
	 * de parametros, este metodo no tiene efecto.
	 */
	public void addParam(ActionParam param){
		if(!params.contains(param)){
			params.add(param);
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
