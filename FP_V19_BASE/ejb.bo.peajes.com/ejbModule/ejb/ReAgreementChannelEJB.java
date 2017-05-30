package ejb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import constant.LogAction;
import constant.LogReference;
import constant.ProcessTrackDetailType;
import constant.ProcessTrackType;

import jpa.TbAgreement;
import jpa.TbChanel;
import jpa.TbProcessTrack;
import jpa.reAgreementChannel;


import util.ReAgreementChannel;

import ejb.Log;

@Stateless(mappedName = "ejb/ReAgreementChanelI")
public class ReAgreementChannelEJB implements ReAgreementChanelI {
	
	@PersistenceContext(unitName="bo")
	EntityManager em;
	
	@EJB(mappedName = "ejb/Log")
	private Log log;
	

	@EJB(mappedName="ejb/Process")
	private Process process;

	

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<ReAgreementChannel> getAllRelationAgreementChannel() {

    	ArrayList<ReAgreementChannel> listChannel = null;
		System.out.print("" + "entro A EJB");
		try {

			Query query = em
					.createNativeQuery("select distinct rac.AGREEMENT_CHANEL_ID , tbc.COD_CHANEL,tbc.CHANEL_TYPE,tba.CODE_AGREEMENT,tba.NAME_AGREEMENT,decode(rac.STATE_ID,1,'Inactivar',0,'Activar') " +
							", decode(tbu.AGREEMENT_CHANEL_ID,null,1,2) exitsRelation" +
							" from RE_AGREEMENT_CHANEL rac"
								             +" left join TB_CHANEL tbc on tbc.CHANEL_ID= rac.CHANEL_ID"
								             +" left join TB_AGREEMENT tba on tba.AGREEMENT_ID=rac.AGREEMENT_ID"
								             +" left join TB_UMBRAL tbu on (rac.AGREEMENT_CHANEL_ID=tbu.AGREEMENT_CHANEL_ID )" +
								             		" inner join TB_STATE ts on (ts.STATE_ID=rac.STATE_ID)" +
								             		" and ts.STATE_ID<>2" 
								             + " order by 1");

			List<Object> list = (List<Object>) query.getResultList();

			if (list.size() > 0) {
				listChannel = new ArrayList<ReAgreementChannel>();
				for (int i = 0; i < list.size(); i++) {
					Object[] o = (Object[]) list.get(i);
					ReAgreementChannel reAC = new ReAgreementChannel();
					reAC.setAgreementChannelId(o[0].toString());
					reAC.setCodeChannel(o[1].toString());
					reAC.setNameChannel(o[2].toString());
					reAC.setCodeAgreement(o[3].toString());
					reAC.setNameAgreement(o[4].toString());
					reAC.setStateReAgreementChannel(o[5].toString());
					reAC.setHaveTransactions(o[6].toString());
					
			

					listChannel.add(reAC);
				}
			} else {
				System.out.print("No se Obtuvo iformación");

			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.print("Error getAllRelationAgreementChannel()" + e.getMessage()
					+ "<------>" + e.getStackTrace());
		}

		return listChannel;
		
	}
	
	@Override
	public List<TbChanel> getChannels() {
		List<TbChanel> list = new ArrayList<TbChanel>();
		try {
			String query = "SELECT ac FROM TbChanel ac where ac.chanelState=1 order by cod_channel ";	
			Query q = em.createQuery(query);
					
			for (Object obj : q.getResultList()) {
				list.add((TbChanel) obj);
			}
		} catch (Exception e) {
			System.out.println("[ Error en getChannels() ]");
			e.printStackTrace();
		}
		return list;
	}
	
	
	
	@Override
	public List<TbAgreement> getAgreements(){
		List<TbAgreement> list = new ArrayList<TbAgreement>();
		try {
			String query = "SELECT ac FROM TbAgreement ac where ac.tbState.stateId=1 order by codeAgreement ";
			Query q = em.createQuery(query);
					
			for (Object obj : q.getResultList()) {
				list.add((TbAgreement) obj);
			}
		} catch (Exception e) {
			System.out.println("[ Error en getAgreements(). ]");
			e.printStackTrace();
		}
		return list;
		
	}

	@Override
	public boolean saveReAgreementChannel(Long idChannel, Long idagreement,String ip,Long userCreator) {
		
		boolean result = false;

		Query qu = em
				.createNativeQuery(" select count(*) from RE_AGREEMENT_CHANEL where AGREEMENT_ID= ?1 and CHANEL_ID= ?2");
		qu.setParameter(1, idagreement);
		qu.setParameter(2, idChannel);
		BigDecimal countR = (BigDecimal) qu.getSingleResult();

		System.out.println("INFO: idChannel" + idChannel + " idAgreement "+idagreement);
		BigDecimal big = new BigDecimal(0);
		if (!countR.equals(big)) {
			result = false;
		} else {
			try {
				Calendar calendar = Calendar.getInstance();
				java.util.Date now = calendar.getTime();
				java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
				reAgreementChannel reAgreementChannel = new reAgreementChannel();
				reAgreementChannel.setAgreementId(em.find(TbAgreement.class, idagreement));
				reAgreementChannel.setChannelId(em.find(TbChanel.class,idChannel));
				reAgreementChannel.setStateId(1L);
				reAgreementChannel.setDateCreateRelation(currentTimestamp);	
				em.persist(reAgreementChannel);
				em.flush();
				

				TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, userCreator);
				Long idPTA;
				if(pt==null){
					idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  userCreator, ip, userCreator);
			
				}
				else{
					idPTA=pt.getProcessTrackId();
				}
				Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.RE_CHANNEL_AGREEMENT,"Se ha creado la Relación entre canal "+idChannel+" y convenio "+idagreement+"",
						userCreator, ip,1,".",null,null,null,null);
				
				log.insertLog("Se ha creado la relación Canal- convenio ("+idChannel+"-"+idagreement+")",LogReference.RECHANNELAGREEMENT, LogAction.CREATE, ip,
						userCreator);
				result = true;
			
			
				
				
			} catch (NullPointerException e) {
				result = false;
			} catch (Exception e) {
				e.printStackTrace();
				System.out
						.println(" [ Error en PasswordsManagementEJB.createQuestion() ] ");
				return false;
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ReAgreementChannel getFindChannelAgreement(Long idChanel, Long idAgreement) {
		
		ReAgreementChannel reAC=new ReAgreementChannel();
		
		try{
		
		Query que=em.createNativeQuery(" select b.CODE_AGREEMENT,a.CHANEL_TYPE,b.NAME_AGREEMENT from TB_CHANEL a,TB_AGREEMENT b"
										+" where a.CHANEL_ID= ?1 and b.AGREEMENT_ID= ?2");
		
		que.setParameter(1, idChanel);
		que.setParameter(2, idAgreement);
		
		List<Object> list = (List<Object>) que.getResultList();

		if (list.size() > 0) {
			
			for (int i = 0; i < list.size(); i++) {
				Object[] o = (Object[]) list.get(i);
				
				reAC.setCodeAgreement(o[0].toString());
				reAC.setNameChannel(o[1].toString());
				reAC.setNameAgreement(o[2].toString());
				
				

			}
		} else {
			System.out.print("No se Obtuvo iformación");
			reAC=null;
		}
	} catch (Exception e) {
		// TODO: handle exception
		System.out.print("Error getFindChannelAgreement()" + e.getMessage()
				+ "<------>" + e.getStackTrace());
		reAC=null;
	}
		
		
		return reAC;
	}

	@Override
	public boolean deleteRelationChannelAgremeent(Long id_agreementChannel) {
		
		return false;
	}

	@Override
	public boolean ActiveInactiveRelationChannelAgremeent(
			Long id_agreementChannel,String ip,Long userCreator) {
		
		reAgreementChannel reag=em.find(reAgreementChannel.class, id_agreementChannel);
		TbChanel tbc=em.find(TbChanel.class, reag.getChannelId().getChanelId());
		TbAgreement tba=em.find(TbAgreement.class, reag.getAgreementId().getAgreementId());
		
		Query qe = em
		.createNativeQuery("" +
				" update RE_AGREEMENT_CHANEL set state_id=(select decode(state_id,1,0,0,1)"
               +" from RE_AGREEMENT_CHANEL where agreement_chanel_id= ?1 ) where agreement_chanel_id= ?2 ");
         qe.setParameter(1, id_agreementChannel);
         qe.setParameter(2, id_agreementChannel);
         	int b = qe.executeUpdate();
         	System.out.print("resultado de la eliminacion de dato :" + b);	
         	if(b==1){

				TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, userCreator);
				Long idPTA;
				if(pt==null){
					idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  userCreator, ip, userCreator);
			
				}
				else{
					idPTA=pt.getProcessTrackId();
				}
				Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.RE_CHANNEL_AGREEMENT,"" +
						"Se cambia de estado la relación entre el convenio "+tba.getNameAgreement() +" código " +
						""+tba.getCodeAgreement()+" y en canal "+tbc.getChanelType()+" ",
						userCreator, ip,1,".",null,null,null,null);
         		
         		
         		log.insertLog("Se ha actualizado el estado de la relación canal-convenio",LogReference.RECHANNELAGREEMENT, LogAction.UPDATE, ip,
						userCreator);
         	
         		
         		
         		return true;
         		
         	}else{
         		log.insertLog("Genero Error al intentar crear la relacion entre conveio y canal ",LogReference.RECHANNELAGREEMENT, LogAction.CREATE, ip,
						userCreator);
         		return false;         		
         	}
	
	}
	
	

}

	

