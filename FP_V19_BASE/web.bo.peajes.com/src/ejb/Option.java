package ejb;

import java.util.List;

import javax.ejb.Remote;

import jpa.ReOptionAction;
import jpa.TbOptActRefefenceType;
import jpa.TbOption;

@Remote
public interface Option {

	/**
	 * 
	 * @param role Role
	 * @return List of options by Role.
	 */
	public List<TbOption> getOptionsByRole(long role);
	
	/**
	 * 
	 * @return List of all options.
	 */
	public List<TbOption> getAllOptions();
	
	/**
	 * 
	 * @param optionName Option Name
	 * @return
	 */
	public boolean createOption(String optionName);
	
	/**
	 * 
	 * @param optId Option Id
	 * @param optionName Option Name
	 * @return True if an Option is properly updated, otherwise false.
	 */
	public boolean updateOption(long optId, String optionName);
	
	public List<TbOption> getListMenu();
	
	public boolean upOrderOptionAction(Long idOptionAction,Long optionId);
	
	public boolean downOrderOptionAction(Long idOptionAction,Long optionId);
	
	public Long getMinValueOrder();
	
	public Long getMaxValueOrder();
	
	public boolean upOrderOption(Long idOption);
	
	public boolean downOrderOption(Long idOption);
	
	public List<ReOptionAction> getListActionsByOption(Long optionId);
	
	public String getNameOptionByOption(Long optionId);
	
	public Long getMinValueOrderAction(Long optionId);
	
	public Long getMaxValueOrderAction(Long optionId);
	
	public List<TbOptActRefefenceType> getListReferences();
	
	public String getNameOptionActionByOptionAction(Long optionActionId);
	
	public boolean setReferenceIdByOpcAct(Long optionActionId, String referenceId);
}
