/**
 * 
 */
package process;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import constant.ProcessTrackType;

import ejb.Master;

import jpa.TbInclusion;
import jpa.TbProcessTrackDetail;

/**
 * Bean to manage Device Process Track.
 * @author tmolina
 *
 */
public class InclusionProcessBean implements Serializable {
	private static final long serialVersionUID = 1758966582439674378L;

	@EJB(mappedName="ejb/Process")
	private ejb.Process process;
	
	@EJB(mappedName = "ejb/Master")
	private Master master;
	
	// Attributes ------------- //
	
	private List<TbProcessTrackDetail> details;
	
	private Long inclusionNumber;
	
	private TbInclusion inclusion;
	
	// Control Visibility --------------- //

	private String message;
	
	private boolean showMessage;
	
	private boolean showData;

	/**
	 * Constructor.
	 */
	public InclusionProcessBean() {
	}
	
	// Actions --------------- //
	
	/**
	 * Searches process track.
	 */
	public String search() {
			if (inclusionNumber != null) {
				inclusion = master.getInclusionByNumber(inclusionNumber);
				
				if (inclusion != null) {
					if(details == null){
						details = new ArrayList<TbProcessTrackDetail>();
					}else{
						details.clear();
					}
					details = process.getProcessDetails(inclusion.getInclusionId(), ProcessTrackType.INCLUSION);
					
					if (details.size() <= 0) {
						setShowData(false);
						setShowMessage(true);
						setMessage("No se Encontró un proceso para la inclusión No. " + inclusionNumber + ".");
					} else {
							setShowData(true);
							setShowMessage(false);
					}
				}else{
					setShowMessage(true);
					setMessage("No se encontró la inclusión No. " + inclusionNumber + ".");
				}
			} else{
				setShowData(false);
				setShowMessage(true);
				setMessage("Digite el número de Inclusión Por Favor.");
			}
		return null;
	}
	
	// Getters and Setters ------ //

	/**
	 * @param details the details to set
	 */
	public void setDetails(List<TbProcessTrackDetail> details) {
		this.details = details;
	}

	/**
	 * @return the details
	 */
	public List<TbProcessTrackDetail> getDetails() {
		return details;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param showMessage the showMessage to set
	 */
	public void setShowMessage(boolean showMessage) {
		this.showMessage = showMessage;
	}

	/**
	 * @return the showMessage
	 */
	public boolean isShowMessage() {
		return showMessage;
	}

	/**
	 * @param showData the showData to set
	 */
	public void setShowData(boolean showData) {
		this.showData = showData;
	}

	/**
	 * @return the showData
	 */
	public boolean isShowData() {
		return showData;
	}

	/**
	 * @param inclusionNumber the inclusionNumber to set
	 */
	public void setInclusionNumber(Long inclusionNumber) {
		this.inclusionNumber = inclusionNumber;
	}

	/**
	 * @return the inclusionNumber
	 */
	public Long getInclusionNumber() {
		return inclusionNumber;
	}

	/**
	 * @param inclusion the inclusion to set
	 */
	public void setInclusion(TbInclusion inclusion) {
		this.inclusion = inclusion;
	}

	/**
	 * @return the inclusion
	 */
	public TbInclusion getInclusion() {
		return inclusion;
	}
}