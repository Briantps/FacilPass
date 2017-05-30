package mBeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import jpa.TbAlarmBalance;
import jpa.TbDeviceType;

/***
 * 
 * @author ablasquez
 *
 */

public class ConfigBalanceBean implements Serializable{
	private static final long serialVersionUID = -5192374787432067073L;
	
	@EJB(mappedName="ejb/Device")
	private ejb.Device deviceEJB;
	
	@EJB(mappedName="ejb/AlarmBalance")
	private ejb.AlarmBalance alarmBalanceEJB;
	
	private List<SelectItem> listaDisp;
	private Long tipodisp=0L;
	private Long valmin=0L;
	private Long nummin=3600L;
	private Long typeDevId=0L;
	
	public ConfigBalanceBean(){
		clear();
	}
	
	public void init(){
		clear();
	}
	
	// Actions ----------------- //
	
	public String clear() {
		
		this.setTypeDevId(null);
		this.setValmin(0L);
		this.setNummin(3600L);
		return "success";
	}
	
	public String changeTypeDevice (){
		TbAlarmBalance tab = alarmBalanceEJB.getInfoByType(typeDevId.longValue());		
		this.setValmin(tab.getValMinAlarm());
		this.setNummin(tab.getFrequency());
		return null;
	} 

	public void setListaDisp(List<SelectItem> listaDisp) {
		this.listaDisp = listaDisp;
	}


	public List<SelectItem> getListaDisp() {
		if (listaDisp == null) {
			listaDisp = new ArrayList<SelectItem>();
			for (TbDeviceType dt : deviceEJB.getDevicesTypes()) {
				listaDisp.add(new SelectItem(dt.getDeviceTypeId(), dt
							.getDeviceTypeName()));
			}
		}
		return listaDisp;
	}	

	public void setTipodisp(long tipodisp) {
		this.tipodisp = tipodisp;
	}


	public long getTipodisp() {
		return tipodisp;
	}


	public void setValmin(long valmin) {
		this.valmin = valmin;
	}


	public long getValmin() {
		return valmin;
	}


	public void setNummin(long nummin) {
		this.nummin = nummin;
	}


	public long getNummin() {
		return nummin;
	}


	public void setTypeDevId(Long typeDevId) {
		this.typeDevId = typeDevId;
	}


	public Long getTypeDevId() {
		return typeDevId;
	}

}
