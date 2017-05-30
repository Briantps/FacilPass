/**
 * 
 */
package util;

import java.io.Serializable;
import java.util.List;

/**
 * @author tmolina
 *
 */
public class OptionActionH implements Serializable{
	private static final long serialVersionUID = -8394058584955856126L;
	
	private String option;
	
	private List<String> actions;

	
	/**
	 * @param option the option to set
	 */
	public void setOption(String option) {		
		this.option = option;
	}
	/**
	 * @return the option
	 */
	public String getOption() {
		return option;
	}
	/**
	 * @param actions the actions to set
	 */
	public void setActions(List<String> actions) {
		this.actions = actions;
	}
	/**
	 * @return the actions
	 */
	public List<String> getActions() {
		return actions;
	}

}
