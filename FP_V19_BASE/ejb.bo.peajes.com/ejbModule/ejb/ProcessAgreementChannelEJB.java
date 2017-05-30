package ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import jpa.TbAgreement;
import jpa.TbChanel;

/*
 * Clase que permite obtener el valor Minimo a recargar teniendo en cuenta si esta activado
 *  el Canal (Channel_state- Channel_minimun_active- Channel_minimun_Allocation) * En caso de no cumplir las condiciones valida el 
 *  Convenio (State_id- Agreement_minimun_active- Agreement_minimun_allocation)  * En caso de no cumplir las condiciones valida con los Parámetros del sistema
 */


@Stateless(mappedName = "ejb/ProcessAgreementChannel")
public class ProcessAgreementChannelEJB implements ProcessAgreementChannel {

	@PersistenceContext(unitName = "bo")
	private EntityManager em;

	@SuppressWarnings("unchecked")
	@Override
	public String getParameterValueById(Long processrechargeavalid) {
		TbChanel chanel = new TbChanel();
		TbAgreement agreement = new TbAgreement();
		long chanelactivo = 0, agreementactivo = 0;
		long chanelstate = 0, agreementstate = 0;
		long chanelvalue, agreementvalue = 0;
		long valmin = 0;

		try {
			
			Query que=em.createNativeQuery(" select CHANEL_ID, AGREEMENT_ID from tb_process_agreement_channel where process_recharge_aval_id=?1 ").setParameter(1, processrechargeavalid);
						 			
			List<Object> list = (List<Object>) que.getResultList();
			
			if (list.size() > 0) {								
				
				for (int i = 0; i < list.size(); i++) {
					
					Object[] o = (Object[]) list.get(i);
					
					chanel = em.find(TbChanel.class, Long.parseLong(o[0].toString()));
					agreement = em.find(TbAgreement.class, Long.parseLong(o[1].toString()));
					
					
					// Estado del canal
					chanelstate = chanel.getChanelState();	
					chanelactivo=(chanel.getMinimumActive() == null ? 0 : chanel.getMinimumActive());
					chanelvalue = (chanel.getMinimumValue() == null ? 0 : chanel.getMinimumValue());
					
					// Convenio
					agreementstate=agreement.getTbState().getStateId();			
					agreementactivo = (agreement.getMinimumActive() == null ? 0 : agreement.getMinimumActive());
					agreementvalue = (agreement.getMinimumValue() == null ? 0 : agreement.getMinimumValue());
					
					if (chanelstate == 1 & chanelactivo == 1) {
						valmin = chanelvalue;
						System.out.println("Se retorna el valor del canal -- El Canal es" + chanel.getChanelId());
						return Long.toString(valmin);
					}

					else if (agreementstate == 1 & agreementactivo == 1) {
						valmin = agreementvalue;
						System.out.println("Se retorna el valor del convenio -- El Convenio es" + agreement.getAgreementId());
						return Long.toString(valmin);
					}
					
				}
											
			} else  {				
				return "0";				
			}			 			 	

		} catch (NullPointerException e) {
			e.printStackTrace();
			return null;
		} catch (NoResultException e) {
			e.printStackTrace();
			return null;
		}

		return "0";
	}

}

