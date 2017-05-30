package ejb;

import java.sql.Clob;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import util.BalanceAccount;

@Stateless(mappedName = "ejb/InformationBalanceAccountI")
public class InformationBalanceAccountEJB implements InformationBalanceAccountI {

	@PersistenceContext(unitName = "bo")
	EntityManager em;

	/**
	 * Lista de Cuentas FacilPass con estado diferente a canceladas o disociadas.
	 * @author psanchez 
	 **/
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<BalanceAccount> getInformationBalanceAccount(long user_Id) {
		ArrayList<BalanceAccount> list = new ArrayList<BalanceAccount>();

		try {
			
			StringBuilder builder=new StringBuilder();
			
			builder.append("SELECT ta.account_id,ta.account_balance,tf.frequency_description as frecuencia,ta.account_opening_date,decode(rab.payment_method_id,1,ta.nure,2,null,0) "); 
			builder.append("FROM tb_account ta ");
			builder.append("INNER JOIN tb_system_user tsu ON tsu.user_id = ta.user_id "); 
			builder.append("INNER JOIN re_account_bank rab ON ta.account_id = rab.account_id ");
			builder.append("INNER JOIN tb_automatic_recharge tar ON tar.account_id=ta.account_id AND tar.estado=0 ");
			builder.append("INNER JOIN re_account_bank rab ON ta.account_id = rab.account_id AND rab.state_account_bank IN (1,2,4) ");
			builder.append("LEFT JOIN tb_frequency tf ON tar.frecuency_id=tf.frequency_id ");
			builder.append("WHERE tsu.user_id=:user_id AND ta.account_state_type_id <> 4");
			
			
			Query q = em
			.createNativeQuery(builder.toString());
			q.setParameter("user_id", user_Id);

			List<Object> listQ = (List<Object>) q.getResultList();

			if (listQ.size() > 0) {
				for (int i = 0; i < listQ.size(); i++) {
					Object[] o = (Object[]) listQ.get(i);
					BalanceAccount b = new BalanceAccount();
					b.setDateCreationAccount(o[3] != null ? o[3].toString(): "-");
					b.setAccount(o[0] != null ? (o[0].toString()): "-");
					b.setNure(o[4] != null ? o[4].toString() : "-");
					b.setBalanceAccount(o[1] != null ?Long.parseLong( o[1].toString()) : 0);
					b.setDetailFrecuence(o[2] != null? o[2].toString():"No Aplica");
					list.add(b);
				}
			} else {
				System.out.print("No se obtuvo información");
			}
		} catch (NullPointerException e) {
			System.out.print("Genero Error getInformationBalanceAccount"+ e.getMessage());
		} catch (Exception e) {
			System.out.print("Genero Exepción getInformationBalanceAccount"+ e.getMessage());
		}

		return list;
	}

	/*
	 * 
	 * (non-Javadoc)
	 * 
	 * @see ejb.InformationBalanceAccountI#getHtmlEditor() Metodo para realizar
	 * la consulta de Editor text
	 */
	@Override
	public String getHtmlEditor() {
		String textHTML = "";
		try {
			Query q = em
					.createNativeQuery("select BALANCES_INFORMATION_TXT from TB_BALANCES_INFORMATION where BALANCES_INFORMATION_STATE_ID=1");

			Clob clobresult = (Clob) q.getSingleResult();
			textHTML = clobresult.getSubString(1, (int) clobresult.length());
		} catch (NoResultException e) {
			// TODO: handle exception
			textHTML = "";
		} catch (Exception e) {
			// TODO: handle exception
			System.out
					.println("Error en InformationBalanceAccountI.getHtmlEditor:  "
							+ e.getMessage());
		}

		return textHTML;
	}

	@Override
	public String getNameNure() {
		String textHTML = "";
		try {
			Query q = em
					.createNativeQuery("select SYSTEM_PARAMETER_VALUE from TB_SYSTEM_PARAMETER where SYSTEM_PARAMETER_ID=70");

			Clob clobresult = (Clob) q.getSingleResult();
			textHTML = clobresult.getSubString(1, (int) clobresult.length());
		} catch (NoResultException e) {
			// TODO: handle exception
			textHTML = "";
		} catch (Exception e) {
			// TODO: handle exception
			System.out
					.println("Error en InformationBalanceAccountI.getHtmlEditor:  "
							+ e.getMessage());
		}

		return textHTML;

	}
	
	
}
