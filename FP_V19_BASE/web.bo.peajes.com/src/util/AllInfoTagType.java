package util;

import java.io.Serializable;

public class AllInfoTagType implements Serializable{
	private static final long serialVersionUID = -8394058584955856126L;
	
	private Long tagTypeIdU;
	private String tagTypeCodeU;
	private String tagTypeNameU;
	private String serialLengthU;
	private Long stateU;

	 public AllInfoTagType(){
	    	super();
	    }

	/**
	 * @param tagTypeIdU the tagTypeIdU to set
	 */
	public void setTagTypeIdU(Long tagTypeIdU) {
		this.tagTypeIdU = tagTypeIdU;
	}

	/**
	 * @return the tagTypeIdU
	 */
	public Long getTagTypeIdU() {
		return tagTypeIdU;
	}

	/**
	 * @param tagTypeCodeU the tagTypeCodeU to set
	 */
	public void setTagTypeCodeU(String tagTypeCodeU) {
		this.tagTypeCodeU = tagTypeCodeU;
	}

	/**
	 * @return the tagTypeCodeU
	 */
	public String getTagTypeCodeU() {
		return tagTypeCodeU;
	}

	/**
	 * @param tagTypeNameU the tagTypeNameU to set
	 */
	public void setTagTypeNameU(String tagTypeNameU) {
		this.tagTypeNameU = tagTypeNameU;
	}

	/**
	 * @return the tagTypeNameU
	 */
	public String getTagTypeNameU() {
		return tagTypeNameU;
	}

	/**
	 * @param serialLengthU the serialLengthU to set
	 */
	public void setSerialLengthU(String serialLengthU) {
		this.serialLengthU = serialLengthU;
	}

	/**
	 * @return the serialLengthU
	 */
	public String getSerialLengthU() {
		return serialLengthU;
	}

	/**
	 * @param stateU the stateU to set
	 */
	public void setStateU(Long stateU) {
		this.stateU = stateU;
	}

	/**
	 * @return the stateU
	 */
	public Long getStateU() {
		return stateU;
	}



}
