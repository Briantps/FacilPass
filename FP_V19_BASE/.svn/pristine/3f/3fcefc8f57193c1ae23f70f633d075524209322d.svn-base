package ejb;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;


import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import util.Encryptor;
import constant.EmailProcess;
import constant.ProcessTrackDetailType;
import constant.ProcessTrackType;

import jpa.ReGroupPassQuestion;
import jpa.ReQuestionResponse;
import jpa.TbPassGroup;
import jpa.TbPassQuestion;
import jpa.TbPassState;
import jpa.TbProcessTrack;
import jpa.TbQuestionType;
import jpa.TbResponse;
import jpa.TbSystemParameter;
import jpa.TbSystemUser;

import ejb.email.Outbox;

/**
 * Session Bean implementation class SecurityQuestionsEjb
 * 
 * @author psanchez
 */

@Stateless(mappedName = "ejb/SecurityQuestions")
public class SecurityQuestionsEjb implements SecurityQuestions{
	
	@PersistenceContext(unitName = "bo") 
	EntityManager em;
	
	@EJB(mappedName="ejb/Process")
	private Process process;
		
	@EJB(mappedName ="ejb/email/Outbox")
	private Outbox outbox;
	
	@EJB(mappedName="ejb/SystemParameters")
	private SystemParameters SystemParametersEJB;
		
	
	public SecurityQuestionsEjb(){
		super();
	}
	
	/**
	 * @author psanchez
	 * MÈtodo encargado de Obtener las lista de todas las preguntas activas pertenecientes al grupo seleccionado	 
	 */		
	@Override
	public List<ReGroupPassQuestion>  getAllQuestion(Long passGroupId) {
		List<ReGroupPassQuestion> listG1 = new ArrayList<ReGroupPassQuestion>();
		TbPassGroup tpg = em.find(TbPassGroup.class, passGroupId);
		try {
			for (Object object : em.createQuery("select rgpq " +
					"from ReGroupPassQuestion rgpq " +
					"where rgpq.passGroupId = ?1 " +
					"and rgpq.passStateId = 1 " +
					"and rgpq.passQuestionId.passQuestDesc is not null " +
					"and rgpq.passStateId is not null " +
					"and rgpq.userId is not null ").setParameter(1, tpg).getResultList()){
				if (object != null && object instanceof ReGroupPassQuestion) {
					ReGroupPassQuestion rgpq = (ReGroupPassQuestion) object;
					listG1.add(rgpq);
				}
			}
		} catch (NoResultException e) {
			System.out.println(" [ Error en SecurityQuestionsEjb.getAllQuestionG1 ] ");
			e.printStackTrace();
			listG1 = null;
		} catch (Exception e) {
			System.out.println(" [ Error en SecurityQuestionsEjb.getAllQuestionG1 ] ");
			e.printStackTrace();
			listG1 = null;
		}
		return listG1;		
	}
	
	
	/**@author psanchez
	 * MÈtodo encargado de guardar  las respuestas de las preguntas de seguridad
	 * Long idUsrs: usuario que responde las preguntas de seguridad
	 * ArrayList<String> resp: Array de respuestas ingresadas por el usuario
	 * ArrayList<Long> idPreg: preguntas de seguridad seleccionadas por el usuario
	 * String p5: pregunta de seguridad ingresada por el usuario**/
	@SuppressWarnings("unused")
	@Override
	public boolean saveResponse(Long idUsrs, ArrayList<String> resp, ArrayList<Long> idPreg, String p5){
		boolean result=false;		
		
		TbSystemUser user = em.find(TbSystemUser.class, idUsrs);
		TbSystemUser tsu = em.find(TbSystemUser.class, idUsrs);
		TbQuestionType tqt = em.find(TbQuestionType.class, 2L);
		try{
			
			Query query= em.createNativeQuery("UPDATE re_group_pass_question SET pass_state_id=3, modification_date=?2 WHERE user_id=?1 AND pass_group_id=5 ");
			query.setParameter(1, idUsrs);
			query.setParameter(2, new Timestamp(System.currentTimeMillis()));
			query.executeUpdate();
			
			Query query1= em.createNativeQuery("UPDATE re_question_response SET state_relation=1 WHERE user_id=?1 ");
			query1.setParameter(1, idUsrs);
			query1.executeUpdate();
			
			/**Se ingresa la pregunta 5 */
			TbPassQuestion tpq5 = new TbPassQuestion(); 
			tpq5.setPassQuestDesc(p5);
			tpq5.setPassQuestDate(new Timestamp(System.currentTimeMillis()));
			tpq5.setQuestionTypeId(tqt);
			em.persist(tpq5); 
			em.flush();
			
			TbPassGroup tpg = em.find(TbPassGroup.class, 5L);
			TbPassState tps = em.find(TbPassState.class, 1L);
			ReGroupPassQuestion rgpq = new ReGroupPassQuestion();
			rgpq.setPassGroupId(tpg);
			rgpq.setPassQuestionId(tpq5);
			rgpq.setPassStateId(tps);
			rgpq.setUserId(tsu);
			rgpq.setRelationDate(new Timestamp(System.currentTimeMillis()));
			em.persist(rgpq);
			em.flush();
			
			
			/**se ingresa la respuesta y la relaciÛn con la pregunta de seguridad, recorriendo la lista de respuestas*/			
			for(int i=0;i<resp.size();i++){					
				if(i != 4){
					TbResponse tr = new TbResponse(); 
					tr.setResponseDescription(Encryptor.getEncrypt(quitaEspacios(resp.get(i).toLowerCase())));
					tr.setDateResponse(new Timestamp(System.currentTimeMillis()));					
					em.persist(tr);
					em.flush();
					
					ReQuestionResponse rqr = new ReQuestionResponse();				
					TbPassQuestion tpq = em.find(TbPassQuestion.class,idPreg.get(i)); 					
					rqr.setResponseId(tr);
					rqr.setPassQuestionId(tpq);
					rqr.setUserId(tsu);
					rqr.setDateRelation(new Timestamp(System.currentTimeMillis()));
					rqr.setStateRelation(0L);
					em.persist(rqr);
					em.flush();
					
				}else{
					TbResponse tr = new TbResponse(); 
					tr.setResponseDescription(Encryptor.getEncrypt(quitaEspacios(resp.get(i).toLowerCase())));
					tr.setDateResponse(new Timestamp(System.currentTimeMillis()));
					em.persist(tr);
					em.flush();
					
					ReQuestionResponse rqr = new ReQuestionResponse();				
					rqr.setResponseId(tr);
					rqr.setPassQuestionId(tpq5);
					rqr.setUserId(tsu);
					rqr.setDateRelation(new Timestamp(System.currentTimeMillis()));
					rqr.setStateRelation(0L);
					em.persist(rqr);
					em.flush();

				}
			}
			
			//proceso administrador
	        TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, user.getUserId() );
			Long idPTA;
				if(pt==null){
					idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  user.getUserId(), null, user.getUserId());
				}else{
					idPTA=pt.getProcessTrackId();
				}
			Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.PASSWORD_MANAGEMENT, 
					   "El usuario " + user.getUserNames()+" "+ user.getUserSecondNames() + 
					   " configurÛ correctamente las preguntas de seguridad. ", user.getUserId(), null, 1,"",0,"","",null,null,null);
			//proceso cliente
			TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,user.getUserId());
			Long idPTC;
				if(ptc==null){
					idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, user.getUserId(), null, user.getUserId());
				}else{
					idPTC=ptc.getProcessTrackId();
				}
			Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.PASSWORD_MANAGEMENT, 
					"El usuario " + user.getUserNames()+" "+ user.getUserSecondNames() +
					" configurÛ correctamente las preguntas de seguridad. ", user.getUserId(), null, 1, "", 0,"","",null,null,null);
			result = true;	
		}catch (Exception e) {
			System.out.println(" [ Error en SecurityQuestionsEjb.saveResponse()] ");
			e.printStackTrace();
			result = false;		
		}
		return result;
		
	}
	/** @author psanchez
	 * MÈtod encargado de validar si existe o no preguntas configuradas por el usuario.
	 */
	@Override
	public boolean validateQuestionsResponse(Long userId){	
		boolean validate = false;
		try{
			Query query = 
				em.createNativeQuery("select count(rqr.question_response_id) " +
									 "from re_question_response rqr " +
									 "where rqr.user_id = ?1 " +
									 "and rqr.state_relation = 0 ");
			query.setParameter(1, userId); 
			BigDecimal reQuestionResponseId = (BigDecimal) query.getSingleResult();

			if(reQuestionResponseId.longValue()>=1){
				validate = true;// tiene preguntas configuradas
			}else{
				validate = false;//no tiene preguntas configuradas
			}
		}catch (Exception e) {
			System.out.println(" [ Error en SecurityQuestionsEjb.validateQuestionsResponse()] ");
			e.printStackTrace();
			validate = false;
		}
		return validate;
	}
	
	
	/** @author psanchez
	 * MÈtodo encargado de validar si el usuario antiguo ha tenido preguntas configuradas
	 */
	@Override
	public boolean validateQuestionsResponseOld(Long userId){	
		boolean validate = false;
		try{
			Query query = 
				em.createNativeQuery("select count(rqr.question_response_id) " +
									 "from re_question_response rqr " +
									 "where rqr.user_id = ?1 " +
									 "and rqr.state_relation = 1 ");
			query.setParameter(1, userId); 
			BigDecimal reQuestionResponseId = (BigDecimal) query.getSingleResult();
			System.out.println("RQO-"+reQuestionResponseId);

			if(reQuestionResponseId.longValue()>=1){
				validate = true;// ha configurado preguntas
			}else{
				validate = false;//no ha configurado preguntas
			}
		}catch (Exception e) {
			System.out.println(" [ Error en SecurityQuestionsEjb.validateQuestionsResponse()] ");
			e.printStackTrace();
			validate = false;
		}
		return validate;
	}
	
	/**@author psanchez
	 * MÈtodo encargado de realizar las validaciones pertenecientes al usuario existente, relacionado con la configuraciÛn de preguntas de seguridad.
	 **/
	@Override
	public boolean userResponseRelation(Long userId){
		boolean result = false;
		
		try{			
			TbSystemParameter tsp2 = em.find(TbSystemParameter.class, 48L);
			String valueP2 = tsp2.getSystemParameterValue();
			
			Query valor = em.createNativeQuery("select nvl(user_questions_date,(select user_pwd_date from tb_system_user where user_id = ?1 ) )+ " + valueP2 + " from tb_system_user where user_id = ?1 ");
			valor.setParameter(1, userId);
			Date fecCalculada = (Date) valor.getSingleResult();

			if(fecCalculada.getTime() > new Date(System.currentTimeMillis()).getTime()){//No ha superado el tiempo para definir las pregurnas de seguirdad
				result=false;
			}else{//SuperÛ el tiempo para definir las preguntas de seguridad
				result=true;
			}
		}catch (Exception e) {
			System.out.println(" [ Error en SecurityQuestionsEjb.userResponseRelation] ");
			e.printStackTrace();
			result = false;	
		}
		return result;
	}
	
	/**@author psanchez
	 * MÈtodo encargado de registrar en Ver Procesos y seguimiento de procesos el inicio de la configuraciÛn de las preguntas.
	 **/
	@SuppressWarnings("unused")
	@Override	
	public void InicioConfPregVP(Long userId, String ip){		
		TbSystemUser user = em.find(TbSystemUser.class, userId);
		
		//proceso administrador
        TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, user.getUserId() );
		Long idPTA;
			if(pt==null){
				idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  user.getUserId(), ip, user.getUserId());
			}else{
				idPTA=pt.getProcessTrackId();
			}
		Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.PASSWORD_MANAGEMENT,
				   "El usuario " + user.getUserNames()+ " " + user.getUserSecondNames() + 
				   " iniciÛ el proceso de configuraciÛn de las preguntas de seguridad.", user.getUserId(), ip, 1,"",0,"","",null,null,null);
		//proceso cliente
		TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,user.getUserId());
		Long idPTC;
			if(ptc==null){
				idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, user.getUserId(), ip, user.getUserId());
			}else{
				idPTC=ptc.getProcessTrackId();
			}
		Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.PASSWORD_MANAGEMENT, 
				"El usuario " + user.getUserNames()+" "+ user.getUserSecondNames()+ 
				" iniciÛ el proceso de configuraciÛn de las preguntas de seguridad.", 
				user.getUserId(), ip, 1, "", 0,"","",null,null,null);
	}

	/**MÈtodo encargado de obtener las respuestas de seguridad del cliente
	 * @return return las respuestas de seguridad
	 * @author psanchez
	 */
	public String returnResp(Long userId, Long group){		
		String response = "";		
		try{
			Query q = em.createNativeQuery("select tr.response_description from re_question_response rqr " +
					" inner join tb_response tr on tr.response_id = rqr.response_id " +
					" inner join re_group_pass_question rgpq on rgpq.pass_question_id = rqr.pass_question_id "+
					" where rgpq.pass_group_id = ?1 " +
					" and rqr.user_id = ?2 "+
					" and rqr.state_relation=0");
			q.setParameter(1, group);
			q.setParameter(2, userId);
			response = (String) q.getSingleResult();
		}catch (Exception e) {
			System.out.println(" [ Error en SecurityQuestionsEjb.returnResp() ] ");
			e.printStackTrace();
		}						
		return response;
	}
	
	/**@author psanchez
	 * MÈtodo encargado de registrar en Ver Procesos y seguimiento de procesos el aplazamiento del diligenciamiento de las preguntas de seguridad.
	 **/	
	@SuppressWarnings("unused")
	@Override	
	public void rememberQuestionsAfter(Long userId, String ip){		
		TbSystemUser user = em.find(TbSystemUser.class, userId);
		
		//proceso administrador
        TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, user.getUserId() );
		Long idPTA;
			if(pt==null){
				idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  user.getUserId(), ip, user.getUserId());
			}else{
				idPTA=pt.getProcessTrackId();
			}
		Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.PASSWORD_MANAGEMENT, 
				   "El usuario " + user.getUserNames()+" "+user.getUserSecondNames() + 
				   " aplazÛ el diligenciamiento de las preguntas de seguridad.", user.getUserId(), ip, 1,"",0,"","",null,null,null);
		//proceso cliente
		TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,user.getUserId());
		Long idPTC;
			if(ptc==null){
				idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, user.getUserId(), ip, user.getUserId());
			}else{
				idPTC=ptc.getProcessTrackId();
			}
		Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.PASSWORD_MANAGEMENT, 
				"El usuario " + user.getUserNames()+" "+user.getUserSecondNames()+ 
				" aplazÛ el diligenciamiento de las preguntas de seguridad.", user.getUserId(), ip, 1, "", 0,"","",null,null,null);
	}

	
	/**MÈtodo encargado de validar si la respuesta de seguridad 1-4 es igual a las registradas en BD
	 * @return return true si la respuestas de seguridad 1-4 son iguales a las registradas en BD o false en caso contrario
	 * @author psanchez
	 */	
	@Override
	public int validateAnswer(Long userId, String resp1, String resp2, String resp3, String resp4) {

		int count=0;
		String inputAnswer1=this.reemplazarCaracteresRaros(quitaEspacios(resp1.toLowerCase()));
		String inputAnswer2=this.reemplazarCaracteresRaros(quitaEspacios(resp2.toLowerCase()));
		String inputAnswer3=this.reemplazarCaracteresRaros(quitaEspacios(resp3.toLowerCase()));
		String inputAnswer4=this.reemplazarCaracteresRaros(quitaEspacios(resp4.toLowerCase()));
		
		String answerBD1=this.reemplazarCaracteresRaros(returnResp(userId, 1L));
		String answerBD2=this.reemplazarCaracteresRaros(returnResp(userId, 2L));
		String answerBD3=this.reemplazarCaracteresRaros(returnResp(userId, 3L));
		String answerBD4=this.reemplazarCaracteresRaros(returnResp(userId, 4L));
		
		if(Encryptor.getEncrypt(inputAnswer1).equalsIgnoreCase(answerBD1)){
			count++;
		}        
		if(Encryptor.getEncrypt(inputAnswer2).equalsIgnoreCase(answerBD2)){
			count++;
		}
		if(Encryptor.getEncrypt(inputAnswer3).equalsIgnoreCase(answerBD3)){
			count++;
		}
		if(Encryptor.getEncrypt(inputAnswer4).equalsIgnoreCase(answerBD4)){
			count++;
		}
	  return count;
   }
	
	/**
	 * MÈtodo encargado de validar si la respuesta de seguridad N˙mero 5 es igual a la registrada en BD
	 * @return return true si la respuesta de seguridad N˙mero 5 es igual a la registrada en BD o false en caso contrario
	 * @author psanchez
	 */	
	@Override
	public boolean validateAnswerFive(Long userId, String resp5) {
		boolean result=false;
		try{
		String inputAnswer5=this.reemplazarCaracteresRaros(quitaEspacios(resp5.toLowerCase()));	
		String answerBD5=this.reemplazarCaracteresRaros(returnResp(userId, 5L));
		
			if(Encryptor.getEncrypt(inputAnswer5).equalsIgnoreCase(answerBD5)){
				result=true;
			}
			else{
				result=false;
			}
		}catch(Exception ex){
			System.out.println(" [ Error en SecurityQuestionsEjb.validateResponseFive ] ");
			result=false;
		}
		return result;
   }
	
	
	@SuppressWarnings("unused")
	@Override
	public void validateResponseReset(Long userId, String ip){
		TbSystemUser user = em.find(TbSystemUser.class, userId);
		//proceso administrador
	    TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, user.getUserId() );
		Long idPTA;
			if(pt==null){
				idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  user.getUserId(), ip, user.getUserId());
			}else{
				idPTA=pt.getProcessTrackId();
			}
		Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.PASSWORD_MANAGEMENT, 
				"El usuario " + user.getUserNames()+" "+user.getUserSecondNames() + 
				" ingresÛ incorrectamente las respuestas a las preguntas de seguridad.", user.getUserId(), ip, 1,"",0, "", "",null,null,null);
		//proceso cliente
		TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,user.getUserId());
		Long idPTC;
			if(ptc==null){
				idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, user.getUserId(), ip, user.getUserId());
			}else{
				idPTC=ptc.getProcessTrackId();
			}
		Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.PASSWORD_MANAGEMENT, 
				 "El usuario " + user.getUserNames()+" "+user.getUserSecondNames() +
				 " ingresÛ incorrectamente las respuestas a las preguntas de seguridad.", user.getUserId(), ip, 1, "", 0,"","",null,null,null);
	 }
	
	
	 /**
	 * MÈtodo creado para validar el n˙mero de intentos de respuesta de seguridad. 
	 * @return return true si el usuario sobrepasÛ el n˙mero de intentos de respuestas de preguntas de seguridad o false el caso contario.
	 * @author psanchez
	 */
	@SuppressWarnings("unused")
	@Override
	public boolean validateAnswerIntent(Long userId, int count, String ip){
		boolean result=false;
		try{	
			Query query = em.createNativeQuery("UPDATE tb_system_user tsu SET tsu.user_count_questions=?1, tsu.user_last_intent_date=?2 WHERE tsu.user_id=?3 ");
		    query.setParameter(1,count);
		    query.setParameter(2,new Timestamp(System.currentTimeMillis()));
		    query.setParameter(3,userId);
		    query.executeUpdate();

			Query q3= em.createNativeQuery("SELECT tsu.user_count_questions FROM tb_system_user tsu WHERE tsu.user_id=?1 "); 
            q3.setParameter(1,userId);
            Long countValidateAnswer = Long.parseLong(q3.getSingleResult().toString());

			String maxFailedLogin = SystemParametersEJB.getParameterValueById(46L);
	         if(countValidateAnswer>= Long.parseLong(maxFailedLogin)){
	             	Query quer= em.createNativeQuery("SELECT tsu.user_state FROM tb_system_user tsu WHERE tsu.user_id=?1 ");
	                quer.setParameter(1,userId);
	                int userState =((BigDecimal) quer.getSingleResult()).intValue();
	             	
		 			Query q1= em.createNativeQuery("UPDATE tb_system_user tsu SET tsu.user_lock_date=?1, tsu.user_state_old=?3, " +
		 					                       "tsu.user_state=1 WHERE tsu.user_id=?2 ");
		 			q1.setParameter(1,new Timestamp(System.currentTimeMillis()));
	    			q1.setParameter(2,userId);
	    			q1.setParameter(3,userState);
	    			q1.executeUpdate();
	             	
	             	TbSystemUser user   = em.find(TbSystemUser.class, userId);
					//proceso administrador
			        TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, user.getUserId() );
					Long idPTA;
						if(pt==null){
							idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  user.getUserId(), ip, user.getUserId());
						}else{
							idPTA=pt.getProcessTrackId();
						}
					Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.PASSWORD_MANAGEMENT, 
							   "El usuario " + user.getUserNames()+" "+user.getUserSecondNames() +
							   " sobrepasÛ el n˙mero intentos de respuesta de preguntas de seguridad.",
							   user.getUserId(), ip, 1,"",0, "", "",null,null,null);
					//proceso cliente
					TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,user.getUserId());
					Long idPTC;
						if(ptc==null){
							idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, user.getUserId(), ip, user.getUserId());
						}else{
							idPTC=ptc.getProcessTrackId();
						}
					Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.PASSWORD_MANAGEMENT, 
							 "El usuario " + user.getUserNames()+" "+user.getUserSecondNames() +
							 " sobrepasÛ el n˙mero intentos de respuesta de preguntas de seguridad.", 
							 user.getUserId(), ip, 1, "", 0,"","",null,null,null);
					
	    			outbox.insertMessageOutbox(userId,
			                   EmailProcess.FAILED_PASSWORD_ATTEMPTS,
			                   null,
			                   null, 
			                   null, 
			                   null,
			                   null,
			                   null,
			                   null,
			                   null,
			                   userId,
				               null,
				               null,
				               null,
			                   true,
			                   null);
	    			result=true;
	    		 }else {
	    			result=false;	                 
	    		 }
		}catch(Exception ex){
			System.out.println("[ Error en LoginEJB.answerIntent-->"+ex.getLocalizedMessage());
			result=false;
		}
		return result;
	}
	
	/**
	 * FunciÛn que elimina acentos y caracteres especiales de
	 * una cadena de texto.
	 * @param input
	 * @return cadena de texto limpia de acentos y caracteres especiales.
	 */
	public String reemplazarCaracteresRaros(String input) {
	    // Cadena de caracteres original a sustituir.
	    String original = "·‡‰ÈËÎÌÏÔÛÚˆ˙˘u¡¿ƒ…»ÀÕÃœ”“÷⁄Ÿ‹Á«";
	    // Cadena de caracteres ASCII que reemplazar·n los originales.
	    String ascii = "aaaeeeiiiooouuuAAAEEEIIIOOOUUUcC";
	    String output = input;
	    for (int i=0; i<original.length(); i++) {
	        // Reemplazamos los caracteres especiales.
	        output = output.replace(original.charAt(i), ascii.charAt(i));
	    }//for i
	    return output;
	}//reemplazarCaracteresRaros
	
	
	public String quitaEspacios(String texto)
	{
		// Cadena de texto sin blancos
        String sCadenaSinBlancos = "";
        // 2. Mediante un StringTokenizer cuyos delimitadores sean
        // espacios en blanco
        StringTokenizer stTexto = new StringTokenizer(texto);
        while (stTexto.hasMoreElements())
          sCadenaSinBlancos += stTexto.nextElement();
	return sCadenaSinBlancos;
	}

	/**MÈtodo encargado de poner en cero el contador de preguntas de seguridad
	 * @author psanchez
	 */
	@Override
	public void userCountQuestions(Long userId) {
		TbSystemUser user = em.find(TbSystemUser.class, userId);
		user.setUserCountQuestions(0);
	}
	
	/**@author psanchez
	 * MÈtodo encargado de registrar en Ver Procesos y seguimiento de procesos la cancelaciÛn de las preguntas de seguridad.
	 **/
	@SuppressWarnings("unused")
	public void cancelConfPregVP(Long userId, String ip){		
		TbSystemUser user = em.find(TbSystemUser.class, userId);
		
		//proceso administrador
        TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, user.getUserId() );
		Long idPTA;
			if(pt==null){
				idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  user.getUserId(), ip, user.getUserId());
			}else{
				idPTA=pt.getProcessTrackId();
			}
		Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.PASSWORD_MANAGEMENT, 
				   "El usuario cancelÛ el restablecimiento de las preguntas de seguridad.", user.getUserId(), ip, 1,"",0,"","",null,null,null);
		//proceso cliente
		TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,user.getUserId());
		Long idPTC;
			if(ptc==null){
				idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, user.getUserId(), ip, user.getUserId());
			}else{
				idPTC=ptc.getProcessTrackId();
			}
		Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.PASSWORD_MANAGEMENT, 
				"El usuario cancelÛ el restablecimiento de las preguntas de seguridad.", user.getUserId(), ip, 1, "", 0,"","",null,null,null);
	}
}
