package ejb.taskeng.workflow.impl;

import java.io.Serializable;

import ejb.taskeng.workflow.ActionParam;

/**
 * Implementacion de ActionParam. El nombre y valor de un parametro
 * utilizan variables String.
 * 
 * @author Mauricio Gil
 */
public class ActionParamBase implements ActionParam, Serializable {
	
	private static final long serialVersionUID = -7320664064931032367L;
	private String name;
	private String value;
	
	public Object clone(){
		ActionParamBase obj = new ActionParamBase();
		obj.setName(new String(this.name));
		obj.setValue(new String(this.value));
		return obj;
	}
	
	public boolean equals(Object obj){
		if(obj instanceof ActionParamBase){
			ActionParamBase apb = (ActionParamBase) obj;
			return apb.getName().equals(name)
				&& apb.getValue().equals(value);
		}
		return false;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	} 
}
