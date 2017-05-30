package ejb;



import java.util.List;

import javax.ejb.Remote;

import jpa.TbTypeConfAutoRechar;

/**Autor
 * JGomez**/

@Remote
public interface IConfAutomaticProgramming {

	public String[] getTextHTML(Long typeConfAutoRecharId);
	
	public String setCreateTXT(Long userId, String textHtml,Long typeConfAutoRecharId);
		
	public List<TbTypeConfAutoRechar> getListTypeAutoRecharge();
	
	public long getLengthValidate(Long typeConfAutoRecharId);
	
	public String setUpdateTypePerson(Long typeConfAutoRecharId, Long action, Long userId);
	
	public String getMessageTooltip(Long confiAutomaticRechargeId);
	
	public boolean getTypePerson(Long confiAutomaticRechargeId);
}
