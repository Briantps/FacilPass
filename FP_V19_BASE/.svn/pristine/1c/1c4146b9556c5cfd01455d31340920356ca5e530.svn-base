package ejb;



import javax.ejb.Remote;

/**Autor
 * Arivera**/

@Remote
public interface InformationBalanceI {

	public String getTextHTML();
	
	public String setCreateTXT(Long userId, String textHtml, String ip);
	
	public boolean getNotExistsPermission(Long userId);
	
	public void setCreatesPermission(Long creationUser, Long idPolitica, Long state, String ip,boolean inside,Long userId);
	
	public Long getIdHTML(long roleId);

}
