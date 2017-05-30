package ejb.crud;
import java.util.List;

import javax.ejb.Remote;

import jpa.TbWarehouseDependency;

@Remote
public interface WarehouseDependency {
	
	/**
	 * 
	 * @return {@link TbWarehouseDependency} {@link List}
	 */
	public List<TbWarehouseDependency> getWarehouseDependecy();
	
	/**
	 * 
	 * @param dependency
	 * @param ip
	 * @param creationUser
	 * @return {@link Long} -1 if the dependency exits, null if error, 0 if successful.
	 */
	public Long insertDependency(String dependency, String ip, Long creationUser);
	
	/**
	 * @param dependencyId
	 * @param dependency
	 * @param ip
	 * @param creationUser
	 * @return <code>true</code> if successful, <code>false</code> otherwise.
	 */
	public boolean editDependency(Long dependencyId, String dependency,
			String ip, Long creationUser);
}
