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
public class OptionActions implements Serializable{
	private static final long serialVersionUID = -8394058584955856126L;
	
	private String option;
	
	private List<String> actions;
	
	private List<OptionActionH> optionH;
	

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
	/**
	 * @param optionH the optionH to set
	 */
	public void setOptionH(List<OptionActionH> optionH) {
		this.optionH = optionH;
	}
	/**
	 * @return the optionH
	 */
	public List<OptionActionH> getOptionH() {
		return optionH;
	}

}
