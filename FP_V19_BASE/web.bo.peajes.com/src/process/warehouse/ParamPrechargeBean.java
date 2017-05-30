/**
 * 
 */
package process.warehouse;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import sessionVar.SessionUtil;

import ejb.Warehouse;

/**
 * @author tmolina
 *
 */
public class ParamPrechargeBean implements Serializable {
	private static final long serialVersionUID = 6595972798032823195L;
	
	@EJB(mappedName="ejb/Warehouse")
	private Warehouse warehouse;
	
	private boolean compare;
	
	private String value;
	
	/**
	 * Constructor.
	 */
	public ParamPrechargeBean() {
	}
	
	/**
	 * 
	 */
	@PostConstruct
	public void post() {
		init();
	}
	
	/**
	 * 
	 * @return
	 */
	public String init(){
		compare = warehouse.compareToPrechargeFile();
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public String save () {
		warehouse.changeParamCompareToPrechargeFile(value, SessionUtil.ip(),
				SessionUtil.sessionUser().getUserId());
		init();
		return null;
	}
	
	// Getters and setters ----------- //

	/**
	 * @param compare the compare to set
	 */
	public void setCompare(boolean compare) {
		this.compare = compare;
	}

	/**
	 * @return the compare
	 */
	public boolean isCompare() {
		return compare;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
}