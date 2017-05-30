package ejb.taskeng.email;

import java.io.File;
import java.io.Serializable;

/**
 * Representa un archivo adjunto en un correo
 * @author Mauricio Gil
 */
public interface Attachment extends Serializable {

	/**
	 * Establece la ruta del archivo
	 * @param filePath
	 */
	public void setFilePath(String filePath);

	/**
	 * Obtiene la ruta del archivo
	 * @return
	 */
	public String getFilePath();

	/**
	 * Obtiene la ruta del archivo como un objeto File
	 * @return
	 */
	public File getFile();

}