package ejb;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.exception.SQLGrammarException;

import constant.LogAction;
import constant.LogReference;
import constant.ProcessTrackDetailType;
import constant.ProcessTrackType;
import crud.ObjectEM;

import jpa.TbChanel;
import jpa.TbProcessTrack;

import util.Channel;

@Stateless(mappedName = "ejb/ChannelI")
public class ChannelEJB implements ChannelI {

	@PersistenceContext(unitName = "bo")
	EntityManager em;

	@EJB(mappedName = "ejb/Log")
	private Log log;

	@EJB(mappedName = "ejb/Process")
	private Process process;

	/**
	 * Metodo que carga los canales en la grilla de visualización
	 * 
	 * @author ARivera
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<util.Channel> getAllChannel() {
		ArrayList<Channel> listChannel = null;
			Query query = em
					.createNativeQuery("select DISTINCT ch.COD_CHANEL,ch.CHANEL_TYPE,ch.CHANEL_DESCRIPTION,ch.CHANEL_MINIMUM_ACTIVE,"
							        +" ch.CHANEL_MINIMUM_ALLOCATION, decode(tbs.STATE_ID,1,'Inactivar',0,'Activar') stateChannel,ch.CHANEL_ID,count(tbu.UMBRAL_ID) exitsRelation from TB_CHANEL ch"
									+" inner join TB_STATE tbs on (tbs.STATE_ID=ch.CHANEL_STATE)"
						   			+" left join RE_AGREEMENT_CHANEL rac on (ch.CHANEL_ID=rac.CHANEL_ID)"
									+" left join TB_UMBRAL tbu on (rac.AGREEMENT_CHANEL_ID=tbu.AGREEMENT_CHANEL_ID)"
									+" where ch.CHANEL_STATE  in (1,0)"
									+" group by ch.COD_CHANEL,ch.CHANEL_TYPE,ch.CHANEL_DESCRIPTION,ch.CHANEL_MINIMUM_ACTIVE,"
									+" ch.CHANEL_MINIMUM_ALLOCATION, tbs.STATE_ID,ch.CHANEL_ID"
									+" order by 1");

			List<Object> list = (List<Object>) query.getResultList();
			if (list.size() > 0) {
				listChannel = new ArrayList<Channel>();
				for (int i = 0; i < list.size(); i++) {
					Object[] o = (Object[]) list.get(i);
					Channel channel = new Channel();
					//System.out.println("DATA: " + o[0].toString());
					channel.setCodeChannel(o[0].toString());
					channel.setNameChannel(o[1]!=null?o[1].toString():"");
					channel.setDescription(o[2]!=null?o[2].toString():"");
					channel.setActiveRecharge(o[3]!=null?Long.valueOf(o[3].toString()):0);
					channel.setRechargeValue(formateador((o[4]!=null?((BigDecimal)o[4]):new BigDecimal(0))));
					channel.setStateChanel(o[5] != null ? o[5].toString() : "2");
					channel.setIdChannel(o[6]!=null?o[6].toString():"");
					channel.setFindRealtionUmbral(o[7]!=null?o[7].toString():"");
					listChannel.add(channel);
				}
			} else {
				System.out.print("No se Obtuvo iformación");
			}
		return listChannel;
	}

	@SuppressWarnings("unused")
	@Override
	public boolean createChannel(Long codeChannel, String nameChannel,
			String description, Long active, Long rechargeValue, String ip, Long creationUser) {
			try {
				TbChanel tbChannel = new TbChanel();			
				tbChannel.setChanelState(1L);
				tbChannel.setChanelDescription(description);
				tbChannel.setCod_channel(codeChannel);
				tbChannel.setChanelState(1L);
				tbChannel.setChanelType(nameChannel);
				tbChannel.setMinimumActive(active);
				tbChannel.setMinimumValue(rechargeValue);

				if(new ObjectEM(em).create(tbChannel)){
					log.insertLog("Se ha creado el Canal ." + tbChannel.getChanelType()+ " con el código: "+ tbChannel.getCod_channel() + ".",
							LogReference.CHANNEL, LogAction.CREATE, ip,creationUser);
					
					TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, creationUser);
					Long idPTA;
					if (pt == null) {
						idPTA = process.createProcessTrack(ProcessTrackType.CLIENT,creationUser, ip, creationUser);
					} else {
						idPTA = pt.getProcessTrackId();
					}
					Long detailA = process.createProcessDetail(idPTA,ProcessTrackDetailType.CHANNEL," Se ha creado el canal" + " " + nameChannel
									+ " con el código del canal: " + codeChannel+ "  ", creationUser, ip, 1, ".", null, null,null, null);
					return true;
				}else{
					log.insertLog("No se ha creado el Canal ." + tbChannel.getChanelType()+ " con el código: "+ tbChannel.getCod_channel() + ".",
							LogReference.CHANNEL, LogAction.CREATE, ip,creationUser);
					return false;
				}
			}catch (Exception e) {
				e.printStackTrace();
				System.out.println(" [ Error en ChannelEJB.createChannel() ] "+e.getMessage());
			}
			return false;	
		}

	@SuppressWarnings("unused")
	@Override
	public boolean updateChannel(Long channelId, Long codeChannel,
			String nameChannel, String Description, Long activeRecharge, Long rechargeValue, String ip, Long creationUser) {
		try {				
				TbChanel tbChanel = em.find(TbChanel.class, Long.valueOf(channelId));
				tbChanel.setChanelType(nameChannel);
				tbChanel.setChanelDescription(Description);
				tbChanel.setCod_channel(codeChannel);
				tbChanel.setMinimumActive(activeRecharge);
				tbChanel.setMinimumValue(rechargeValue);
				
				if(new ObjectEM(em).update(tbChanel)){
					log.insertLog("Se ha modificado el código de canal: "+ codeChannel, LogReference.CHANNEL, LogAction.UPDATE,ip, creationUser);
	
					TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, creationUser);
					Long idPTA;
					if (pt == null) {
						idPTA = process.createProcessTrack(ProcessTrackType.CLIENT,creationUser, ip, creationUser);
	
					} else {
						idPTA = pt.getProcessTrackId();
					}
					Long detailA = process.createProcessDetail(idPTA,ProcessTrackDetailType.CHANNEL," Se ha modificado el canal "
									+ tbChanel.getChanelType() + " con el código "+ tbChanel.getCod_channel() + "",
									creationUser, ip, 1,"Se ha realizado una Actualización", null, null, null,null);
					return true;
				}else {
					log.insertLog("No se ha modificado el código de canal: "+ codeChannel, LogReference.CHANNEL, LogAction.UPDATE,ip, creationUser);
					return false;
				}
			} catch (Exception e) {
				System.out.println(" [ Error en ChannelEJB.updateChannel ] "+e.getMessage());
				e.printStackTrace();
			}
		return false;
	}

	@SuppressWarnings("unused")
	@Override
	public boolean inactiveActiveChannel(Long id_channel, Long codeChannel,
			String ip, Long creationUser) throws SQLException {

		TbChanel tbChannel = em.find(TbChanel.class, id_channel);
		try {

			Query q = em
					.createNativeQuery("select count(*) from RE_AGREEMENT_CHANEL rac"
							+ "  left join TB_CHANEL tbc on tbc.CHANEL_ID= rac.CHANEL_ID"
							+ " left join TB_AGREEMENT tba on tba.AGREEMENT_ID=rac.AGREEMENT_ID"
							+ "  where tbc.COD_CHANEL= ?1");
			q.setParameter(1, codeChannel);
			BigDecimal countR = (BigDecimal) q.getSingleResult();

			BigDecimal big = new BigDecimal(0);
			if (countR.equals(big)) {
				try {
					System.out
							.println("No hay relacion con Re_agreementChannel");
					Query qe = em
							.createNativeQuery("update TB_CHANEL set CHANEL_STATE=(select decode(CHANEL_STATE,1,0,0,1) from TB_CHANEL where COD_CHANEL= ?1 ) where COD_CHANEL= ?2 ");
					qe.setParameter(1, codeChannel);
					qe.setParameter(2, codeChannel);
					int a = qe.executeUpdate();

					TbProcessTrack pt = process.searchProcess(
							ProcessTrackType.CLIENT, creationUser);
					Long idPTA;
					if (pt == null) {
						idPTA = process.createProcessTrack(
								ProcessTrackType.CLIENT, creationUser, ip,
								creationUser);

					} else {
						idPTA = pt.getProcessTrackId();
					}
					Long detailA = process.createProcessDetail(
							idPTA,
							ProcessTrackDetailType.CHANNEL,
							" Se ha cambiado el estado "
									+ tbChannel.getChanelState()
									+ " del canal " + tbChannel.getChanelType()
									+ " con el código " + codeChannel + "  ",
							creationUser, ip, 1, ".", null, null, null, null);

					System.out.println("valor de resultado a: " + a);
					return true;
				} catch (Exception e) {
					System.out.println(e.getMessage());
					return false;
				}

			} else {
				System.out.println("Si hay relacion");
				Query find = em
						.createNativeQuery("select CHANEL_STATE from TB_CHANEL where COD_CHANEL= ?1  and CHANEL_STATE<>2");
				find.setParameter(1, codeChannel);
				Long state = ((BigDecimal) find.getSingleResult()).longValue();
				Query qe = em
						.createNativeQuery("update TB_CHANEL set CHANEL_STATE=(select decode(CHANEL_STATE,1,0,0,1) from TB_CHANEL where COD_CHANEL= ?1 and CHANEL_STATE in  (0,1) ) where COD_CHANEL= ?2 and CHANEL_STATE in  (0,1)");
				qe.setParameter(1, codeChannel);
				qe.setParameter(2, codeChannel);
				int b = qe.executeUpdate();
				Long estateActive = 1L;
				TbProcessTrack pt = process.searchProcess(
						ProcessTrackType.CLIENT, creationUser);
				Long idPTA;
				if (pt == null) {
					idPTA = process.createProcessTrack(ProcessTrackType.CLIENT,
							creationUser, ip, creationUser);

				} else {
					idPTA = pt.getProcessTrackId();
				}
				Long detailA = process.createProcessDetail(
						idPTA,
						ProcessTrackDetailType.CHANNEL,
						" Se ha cambiado de estado "
								+ tbChannel.getChanelState()
								+ " del código de canal " + codeChannel + "  ",
						creationUser, ip, 1, ".", null, null, null, null);
				// BigDecimal estateInactive = new BigDecimal(0);

				System.out.println("valor de b: " + b);
				if (state.equals(estateActive)) {
					Query qre = em
							.createNativeQuery("update RE_AGREEMENT_CHANEL set state_id=0 , DATE_INACTIVE_RELATION= systimestamp where CHANEL_ID in (select tc.CHANEL_ID from TB_CHANEL tc where tc.COD_CHANEL= ?1 ) and state_id in (0,1)");
					qre.setParameter(1, codeChannel);
					int c = qre.executeUpdate();

					process.createProcessDetail(idPTA,
							ProcessTrackDetailType.CHANNEL,
							" Se ha cambiado el estado a la relación entre canal "
									+ tbChannel.getCod_channel()
									+ " y convenio", creationUser, ip, 1, ".",
							null, null, null, null);
					System.out.println("valor de c: " + c);
				}

				log.insertLog(
						"Se ha realizado un cambio de estado en el canal y en la relacion Canal con Convenio: "
								+ codeChannel, LogReference.CHANNEL,
						LogAction.CREATE, ip, creationUser);
				return true;
			}
		} catch (SQLGrammarException e) {
			System.out.println("1" + e.getMessage());
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			System.out.println("2" + e.getMessage());
			e.printStackTrace();
			return false;
		}

	}

	@SuppressWarnings("unused")
	@Override
	public boolean deleteChannel(Long id_channel, Long codeChannel, String ip,
			Long creationUser) throws SQLException {

		
		TbChanel tbChannel = em.find(TbChanel.class, id_channel);
		try {

			Query q = em
					.createNativeQuery("select count(*) from RE_AGREEMENT_CHANEL rac"
							+ "  left join TB_CHANEL tbc on tbc.CHANEL_ID= rac.CHANEL_ID"
							+ " left join TB_AGREEMENT tba on tba.AGREEMENT_ID=rac.AGREEMENT_ID"
							+ "  where tbc.COD_CHANEL= ?1 and tbc.CHANEL_STATE in (0,1) ");
			q.setParameter(1, codeChannel);
			BigDecimal countR = (BigDecimal) q.getSingleResult();

			BigDecimal big = new BigDecimal(0);
			if (countR.equals(big)) {
				try {
					System.out
							.println("No hay relacion con Re_agreementChannel");
					Query qe = em
							.createNativeQuery("update TB_CHANEL set CHANEL_STATE=2 where COD_CHANEL= ?1 and CHANEL_STATE in (0,1)");
					qe.setParameter(1, codeChannel);

					int a = qe.executeUpdate();

					TbProcessTrack pt = process.searchProcess(
							ProcessTrackType.CLIENT, creationUser);
					Long idPTA;
					if (pt == null) {
						idPTA = process.createProcessTrack(
								ProcessTrackType.CLIENT, creationUser, ip,
								creationUser);

					} else {
						idPTA = pt.getProcessTrackId();
					}
					Long detailA = process.createProcessDetail(
							idPTA,
							ProcessTrackDetailType.CHANNEL,
							" Se ha eliminado el estado "
									+ tbChannel.getChanelState()
									+ " del canal " + tbChannel.getChanelType()
									+ " con el código " + codeChannel + "  ",
							creationUser, ip, 1, ".", null, null, null, null);

					System.out.println("valor de resultado a: " + a);
					return true;
				} catch (Exception e) {
					System.out.println(e.getMessage());
					return false;
				}

			} else {
				System.out.println("Si hay relacion");
				Query qe = em
						.createNativeQuery("update TB_CHANEL set CHANEL_STATE=2 where "
								+ " COD_CHANEL= ?1 and CHANEL_STATE in (0,1)");
				qe.setParameter(1, codeChannel);

				int b = qe.executeUpdate();

				Query qre = em
						.createNativeQuery("update RE_AGREEMENT_CHANEL set state_id=2 , DATE_INACTIVE_RELATION= systimestamp "
								+ " where CHANEL_ID in (select tc.CHANEL_ID from TB_CHANEL tc where tc.COD_CHANEL= ?1 )");
				qre.setParameter(1, codeChannel);
				int c = qre.executeUpdate();

				TbProcessTrack pt = process.searchProcess(
						ProcessTrackType.CLIENT, creationUser);
				Long idPTA;
				if (pt == null) {
					idPTA = process.createProcessTrack(ProcessTrackType.CLIENT,
							creationUser, ip, creationUser);

				} else {
					idPTA = pt.getProcessTrackId();
				}
				Long detailA = process.createProcessDetail(
						idPTA,
						ProcessTrackDetailType.CHANNEL,
						" Se ha eliminado de estado "
								+ tbChannel.getChanelState()
								+ " del código de canal " + codeChannel + "  ",
						creationUser, ip, 1, ".", null, null, null, null);
				// BigDecimal estateInactive = new BigDecimal(0);

				System.out.println("valor de b: " + b);

				process.createProcessDetail(
						idPTA,
						ProcessTrackDetailType.CHANNEL,
						" Se ha eliminado la relación entre canal "
								+ tbChannel.getCod_channel() + " y convenio",
						creationUser, ip, 1, ".", null, null, null, null);
				System.out.println("valor de c: " + c);

				log.insertLog(
						"Se ha eliminado la relacion Canal con Convenio: "
								+ codeChannel, LogReference.CHANNEL,
						LogAction.DELETE, ip, creationUser);
				return true;
			}
		} catch (SQLGrammarException e) {
			System.out.println("1" + e.getMessage());
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			System.out.println("2" + e.getMessage());
			e.printStackTrace();
			return false;
		}

	}
	
	/**
	 * Método valida la existencia del convenio antes de crear o modificar el convenio
	 * @author psanchez
	 */
	@Override
	public Long existChannel(Long channelId, Long codeChannel, int i) {
		Long existCodeChannel=-1L;
		Long count=0L;
		try {
			if(i==1){
				Query query = em.createNativeQuery("select count(CHANEL_ID) from TB_CHANEL where COD_CHANEL=?1 and CHANEL_STATE in (0,1)");					
				query.setParameter(1, codeChannel);
				count = ((BigDecimal) query.getSingleResult()).longValue();						
			}else if (i==2){//modificar 
				Query query = em.createNativeQuery("select count(CHANEL_ID) from TB_CHANEL where COD_CHANEL=?1 and CHANEL_ID<> ?2 " +
						"and CHANEL_STATE in (0,1) ");
				query.setParameter(1, codeChannel);
				query.setParameter(2, channelId);
				
				count = ((BigDecimal) query.getSingleResult()).longValue();
				}
			if(count>0){
				existCodeChannel=1L;
			}
		} catch (Exception e) {
			existCodeChannel = 0L;
			System.out.println(" [ Error en ChannelEJB.existChannel] "+e.getMessage());
			e.printStackTrace();
		}
		return existCodeChannel;
	}
	
	private String formateador (BigDecimal number){		 
		 String valueChargeTxt = "";		 
		 if(number != null && number.compareTo(BigDecimal.ZERO) > 0){
			 DecimalFormat formateador = new DecimalFormat("###,###.##");
			 valueChargeTxt = formateador.format(number);
			 valueChargeTxt = "$"+valueChargeTxt.replaceAll(",", ".");
		 }			 
		return valueChargeTxt;		 
	 }

}
