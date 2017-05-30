package ejb;
import java.sql.Clob;
import java.sql.Timestamp;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import jpa.TbBalancesInformation;
import jpa.TbSystemUser;
import constant.LogAction;
import constant.LogReference;
import crud.ObjectEM;
import ejb.email.Outbox;

@Stateless(mappedName = "ejb/InfromationBalanceI")
public class InformationBalanceEJB implements InformationBalanceI {

	@EJB(mappedName = "ejb/Log")
	private Log log;

	@PersistenceContext(unitName = "bo")
	EntityManager em;

	@EJB(mappedName = "ejb/Process")
	private Process process;

	@EJB(mappedName = "ejb/email/Outbox")
	private Outbox outbox;

	private ObjectEM objectEM;

	@Override
	public String getTextHTML() {
		String textHTML = "";

		try {
			System.out.println("Entre a DataPoliciesEJB.getTextHTML");

			Query q = em
					.createNativeQuery("select BALANCES_INFORMATION_TXT from TB_BALANCES_INFORMATION "
							+ "  where  BALANCES_INFORMATION_STATE_ID = 1");
			Clob clob = (Clob) q.getSingleResult();
			textHTML = clob.getSubString(1, (int) clob.length());

		} catch (NoResultException ex) {
			textHTML = " ";
			System.out
					.println("Entre a NoResultexeption en DataPoliciesEJB.getTextHTML");
		} catch (Exception e) {
			e.printStackTrace();
			textHTML = " ";
			System.out.println("Error en DataPoliciesEJB.getTextHTML");
		}

		return textHTML;

	}

	@Override
	public String setCreateTXT(Long userId, String textHtml, String ip) {
		String resp;
		System.out.println("Entre a DataPoliciesEJB.setCreateTXT");
		try {
			
			TbBalancesInformation tbBI=new TbBalancesInformation();
			tbBI.setUserId(em.find(TbSystemUser.class, userId));
			tbBI.setBalancesInformationDate(new Timestamp(System.currentTimeMillis()));
			tbBI.setBalancesInformationStateId(1L);
			tbBI.setBalancesInformationTxt(textHtml);
			
			objectEM = new ObjectEM(em);	
			if(objectEM.create(tbBI)){
				
				System.out.println("Entre a Realizar Update setCreateTXT");
				System.out.println("IdInformationId: " + tbBI.getBalancesInformationId());
				
				Query q = em.createNativeQuery("update TB_BALANCES_INFORMATION set BALANCES_INFORMATION_STATE_ID = 2 where BALANCES_INFORMATION_ID <> ?1 and BALANCES_INFORMATION_STATE_ID=1");		
				q.setParameter(1, tbBI.getBalancesInformationId());
				int count = q.executeUpdate();
				
				System.out.println("La cantidad de mensajes inctivados es de: " + count);
			
				log.insertLog("Información Saldos | Se ha configurado un nuevo aviso de Infromación saldos : " + userId, 
						LogReference.INFORMATIONBALANCE,
				LogAction.CREATE, ip, userId);
				
			}
			resp = "Se han guardado los cambios con éxito";		
			
			
		} catch (Exception e) {
			System.out.println("Error en setCreateTXT");
			resp = "Error en la transaccion";
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public boolean getNotExistsPermission(Long userId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setCreatesPermission(Long creationUser, Long idPolitica,
			Long state, String ip, boolean inside, Long userId) {
		// TODO Auto-generated method stub

	}

	@Override
	public Long getIdHTML(long roleId) {
		// TODO Auto-generated method stub
		return null;
	}

}
