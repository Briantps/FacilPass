/**
 * 
 */
package process.purchasing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import sessionVar.SessionUtil;

import ejb.Purchase;

import jpa.TbConsignment;

/**
 * Bean To manage the consult of consignment. 
 * @author tmolina
 *
 */
public class ConsultClientConsignment implements Serializable {
	private static final long serialVersionUID = -2955241684411433726L;

	@EJB(mappedName="ejb/Purchase")
	private Purchase purchase;
	
	// Attributes ------------- //
	
	private List<TbConsignment> consignmentList;
	
	private boolean showConsignments;
	
	/**
	 * Constructor 
	 */
	public ConsultClientConsignment() {
	}
	
	@PostConstruct
	public void initClass(){
		if (consignmentList == null) {
			consignmentList = new ArrayList<TbConsignment>();
		} else {
			consignmentList.clear();
		}
		consignmentList = purchase.getConsignmentByClient(SessionUtil
				.sessionUser().getUserId());

		if (this.getConsignmentList().size() > 0) {
			showConsignments = true;
		} else {
			showConsignments = false;
		}
	}
	
	
	// Getters and Setters -------- //

	/**
	 * @param consignmentList the consignmentList to set
	 */
	public void setConsignmentList(List<TbConsignment> consignmentList) {
		this.consignmentList = consignmentList;
	}

	/**
	 * @return the consignmentList
	 */
	public List<TbConsignment> getConsignmentList() {
		return consignmentList;
	}

	/**
	 * @param showConsignments the showConsignments to set
	 */
	public void setShowConsignments(boolean showConsignments) {
		this.showConsignments = showConsignments;
	}

	/**
	 * @return the showConsignments
	 */
	public boolean isShowConsignments() {
		return showConsignments;
	}
}