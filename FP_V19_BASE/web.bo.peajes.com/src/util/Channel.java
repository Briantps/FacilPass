package util;

import java.io.Serializable;

public class Channel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5653268673393196807L;
	private String idChannel;
	private String codeChannel;
	private String nameChannel;
	private String description;
	private String stateChanel;
	private String findRealtionUmbral;
	private String rechargeValue;
	private Long activeRecharge;
	
	public Channel(){}
	public String getCodeChannel() {
		return codeChannel;
	}
	public void setCodeChannel(String codeChannel) {
		this.codeChannel = codeChannel;
	}
	public String getNameChannel() {
		return nameChannel;
	}
	public void setNameChannel(String nameChannel) {
		this.nameChannel = nameChannel;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStateChanel() {
		return stateChanel;
	}
	public void setStateChanel(String stateChanel) {
		this.stateChanel = stateChanel;
	}
	public String getIdChannel() {
		return idChannel;
	}
	public void setIdChannel(String idChannel) {
		this.idChannel = idChannel;
	}

	public String getFindRealtionUmbral() {
		return findRealtionUmbral;
	}
	public void setFindRealtionUmbral(String findRealtionUmbral) {
		this.findRealtionUmbral = findRealtionUmbral;
	}
	public void setRechargeValue(String rechargeValue) {
		this.rechargeValue = rechargeValue;
	}
	public String getRechargeValue() {
		return rechargeValue;
	}
	public void setActiveRecharge(Long activeRecharge) {
		this.activeRecharge = activeRecharge;
	}
	public Long getActiveRecharge() {
		return activeRecharge;
	}
}
