package ejb;



import javax.ejb.Remote;

/**Autor
 * JGomez**/

@Remote
public interface DataPolicies {

	public String getTextHTML(Long roleId);
	
	public String setCreateTXT(Long userId, String textHtml, String ip,Long roleId);
	
	public boolean getNotExistsPermission(Long userId);
	
	public void setCreatesPermission(Long creationUser, Long idPolitica, Long state, String ip,boolean inside,Long userId);
	
	public Long getIdHTML(long roleId);

}
