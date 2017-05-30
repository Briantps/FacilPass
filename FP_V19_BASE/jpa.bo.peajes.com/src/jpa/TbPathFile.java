/**
 * 
 */
package jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author tmolina
 *
 */
@Entity
@Table(name="TB_PATH_FILE")
public class TbPathFile implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="PATH_FILE_ID")
	private Long pathFileId;
	
	@Column(name="PATH_FILE_NAME")
	private String pathFileName;
	
	@Column(name="PATH_FILE")
	private String pathFile;
	
	/**
	 * Constructor 
	 */
	public TbPathFile(){
		super();
	}

	/**
	 * @param pathFileId the pathFileId to set
	 */
	public void setPathFileId(Long pathFileId) {
		this.pathFileId = pathFileId;
	}

	/**
	 * @return the pathFileId
	 */
	public Long getPathFileId() {
		return pathFileId;
	}

	/**
	 * @param pathFileName the pathFileName to set
	 */
	public void setPathFileName(String pathFileName) {
		this.pathFileName = pathFileName;
	}

	/**
	 * @return the pathFileName
	 */
	public String getPathFileName() {
		return pathFileName;
	}

	/**
	 * @param pathFile the pathFile to set
	 */
	public void setPathFile(String pathFile) {
		this.pathFile = pathFile;
	}

	/**
	 * @return the pathFile
	 */
	public String getPathFile() {
		return pathFile;
	}
}