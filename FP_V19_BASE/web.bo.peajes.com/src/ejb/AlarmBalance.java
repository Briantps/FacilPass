package ejb;

import javax.ejb.Remote;

import constant.EAlarmType;
import jpa.TbAlarmBalance;



/***
 * @author ablasquez
 */
@Remote
public interface AlarmBalance {
  
	public TbAlarmBalance getInfoByType(long type);

	/**
	 * Retorna el TbAlarmBalance asociado a la cuenta y tipo de alarma indicada
	 * @param accountId identificador de la cuenta facilpass
	 * @param type tipo de alarma
	 * @return TbAlarmBalance con la cuenta y tipo de alarma indicada
	 * @author TPS r.bautista
	 */
	public TbAlarmBalance getInfoByType(Long accountId, EAlarmType type);

}
