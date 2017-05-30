package ejb;
import java.util.List;

import javax.ejb.Remote;

import jpa.TbPathFile;

@Remote
public interface PathFile {
	
	public List<TbPathFile> getFilePaths();
	
	/**
	 * Gets the path for a particular option given by the param
	 * @param param
	 * @return
	 */
	public String getPath(String param);

	public List<TbPathFile> getFilePathsClient();
}
