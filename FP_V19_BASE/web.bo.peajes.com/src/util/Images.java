package util;

import java.io.File;
import java.io.Serializable;

public class Images implements Serializable{

	private static final long serialVersionUID = 1L;
		
	private String name;
	
	private String path;
	
	private File file;
	
	private String bufString;
	
	private byte[] buf;
	
	private int id;
	
	
	public Images(){
		super();
	}


	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}


	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param file the file to set
	 */
	public void setFile(File file) {
		this.file = file;
	}


	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}


	/**
	 * @param buf the buf to set
	 */
	public void setBuf(byte[] buf) {
		this.buf = buf;
	}


	/**
	 * @return the buf
	 */
	public byte[] getBuf() {
		return buf;
	}


	/**
	 * @param bufString the bufString to set
	 */
	public void setBufString(String bufString) {
		this.bufString = bufString;
	}


	/**
	 * @return the bufString
	 */
	public String getBufString() {
		return bufString;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}


	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

}
