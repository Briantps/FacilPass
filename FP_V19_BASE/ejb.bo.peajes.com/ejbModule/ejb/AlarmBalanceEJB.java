package ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import constant.EAlarmType;
import jpa.TbAlarmBalance;

@Stateless(mappedName = "ejb/AlarmBalance")
public class AlarmBalanceEJB implements AlarmBalance {
	
	@PersistenceContext(unitName = "bo") 
	EntityManager em;
	
	public TbAlarmBalance getInfoByType (long type){
		TbAlarmBalance tab=null;
		
		Query qry = em.createQuery("Select tba from TbAlarmBalance tba Where tba.idTipAlarm=?1");
		qry.setParameter(1, type);
		tab=(TbAlarmBalance) qry.getSingleResult();
		return tab;
	}

	/**
	 * @see ejb.AlarmBalance#getInfoByType(Long, EAlarmType)
	 */
	@Override
	public TbAlarmBalance getInfoByType(Long accountId, EAlarmType type) {
		if(accountId == null){
			throw new IllegalArgumentException("El valor de la cuenta no puede ser vacío");
		}
		
		if (type == null){
			throw new IllegalArgumentException("El valor del tipo de alarma no puede ser vacío");
		}
		
		Query qry = em.createQuery(" SELECT tba from TbAlarmBalance tba WHERE tba.accountId=?1 AND tba.idTipAlarm=?2");
		qry.setParameter(1, accountId);
		qry.setParameter(2, type.getId());
		return (TbAlarmBalance) qry.getSingleResult();
	}

}
