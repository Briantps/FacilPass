package util;

import java.sql.Timestamp;

/**
 * 
 * @author jromero
 *
 */
public class HistoricalRecharges {
	private Long umbralId;
	private Timestamp umbralTime;
	private String typeRecharge;
	private Long umbralValue;
	private String rechargeBank;
	private String umbralState;
	private Long umbralStateId;
	private Long umbralChannel;
	private int numColm;
	//Propiedad que determina si se puede visualizar el reporte
	private boolean verReporte; 
	
	public int getNumColm() {
		return numColm;
	}
	public void setNumColm(int numColm) {
		this.numColm = numColm;
	}
	public Long getUmbralId() {
		return umbralId;
	}
	public void setUmbralId(Long umbralId) {
		this.umbralId = umbralId;
	}
	public Timestamp getUmbralTime() {
		return umbralTime;
	}
	public void setUmbralTime(Timestamp umbralTime) {
		this.umbralTime = umbralTime;
	}
	public String getTypeRecharge() {
		return typeRecharge;
	}
	public void setTypeRecharge(String typeRecharge) {
		this.typeRecharge = typeRecharge;
	}
	public Long getUmbralValue() {
		return umbralValue;
	}
	public void setUmbralValue(Long umbralValue) {
		this.umbralValue = umbralValue;
	}
	public String getRechargeBank() {
		return rechargeBank;
	}
	public void setRechargeBank(String rechargeBank) {
		this.rechargeBank = rechargeBank;
	}
	public String getUmbralState() {
		return umbralState;
	}
	public void setUmbralState(String umbralState) {
		this.umbralState = umbralState;
	}
	public Long getUmbralChannel() {
		return umbralChannel;
	}
	public void setUmbralChannel(Long umbralChannel) {
		this.umbralChannel = umbralChannel;
	}
	public Long getUmbralStateId() {
		return umbralStateId;
	}
	public void setUmbralStateId(Long umbralStateId) {
		this.umbralStateId = umbralStateId;
	}
	
	//Se realiza la validacion para la visualizacion del comprobante  del historico de recarga
	public boolean isVerReporte() {
		if (umbralChannel!=null&&umbralChannel!=0&&umbralStateId!=0L&&umbralStateId!=4L&&umbralStateId!=5L) {
			return true;
		}else
			return false;
	}
	public void setVerReporte(boolean verReporte) {
		this.verReporte = verReporte;
	}
	
	@Override
	public String toString() {
		return "HistoricalRecharges [umbralId=" + umbralId + ", umbralTime="
				+ umbralTime + ", typeRecharge=" + typeRecharge
				+ ", umbralValue=" + umbralValue + ", rechargeBank="
				+ rechargeBank + ", umbralState=" + umbralState
				+ ", umbralStateId=" + umbralStateId + ", umbralChannel="
				+ umbralChannel + "]";
	}
}