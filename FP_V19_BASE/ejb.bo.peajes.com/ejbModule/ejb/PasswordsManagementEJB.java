package ejb;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import constant.EmailProcess;
import constant.LogAction;
import constant.LogReference;
import constant.ProcessTrackDetailType;
import constant.ProcessTrackType;

import crud.ObjectEM;
import ejb.email.Outbox;

import util.Encryptor;
import util.ReGroupPassQ;

import jpa.ReGroupPassQuestion;
import jpa.TbPassGroup;
import jpa.TbPassQuestion;
import jpa.TbPassState;
import jpa.TbProcessTrack;
import jpa.TbQuestionType;
import jpa.TbSystemUser;


/**
 * Session Bean implementation class PasswordsManagementEJB
 * 
 * @author psanchez
 */
@Stateless(mappedName = "ejb/PasswordsManagement")
public class PasswordsManagementEJB implements PasswordsManagement{
	
	
	@PersistenceContext(unitName = "bo") 
	EntityManager em;
	
	@EJB(mappedName = "ejb/User")
	private User user;
	
	@EJB(mappedName ="ejb/email/Outbox")
	private Outbox outbox;
	
	@EJB(mappedName="ejb/Process")
	private Process process;
	
	@EJB(mappedName = "ejb/Log")
	private Log log;
	
	private ObjectEM objectEM;
	
	private TbSystemUser usr;
	public PasswordsManagementEJB(){
		super();
	}
	
	/**@author psanchez
	 * Método encargado de Obtener lista de todos los grupos para la Administración de Password
	 **/	
	@Override
	public List<TbPassGroup>  getAllPassGroup() {
		List<TbPassGroup> groups = new ArrayList<TbPassGroup>();
		
		try {
			for (Object object : em.createQuery("select tpg from TbPassGroup tpg  where tpg.passGroupState = 0 and tpg.passGroupId <> 5").getResultList()){
				if (object != null && object instanceof TbPassGroup) {
					TbPassGroup tpg = (TbPassGroup) object;
					groups.add(tpg);
				}
			}
		} catch (NoResultException e) {
			System.out.println(" [ Error en PasswordsManagementEJB.getAllPassGroup().NoResultException ] ");
			e.printStackTrace();
			groups = null;
		} catch (Exception e) {
			System.out.println(" [ Error en PasswordsManagementEJB.getAllPassGroup().Exception ] ");
			e.printStackTrace();
			groups = null;
		}
		return groups;		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TbSystemUser> getUserCreate() {
		List<TbSystemUser> listuserCreate = new ArrayList<TbSystemUser>();
		
		try {
			Query query = em
			.createNativeQuery("select DISTINCT tu.USER_ID,tu.USER_EMAIL from tb_system_user tu " +
							   " inner join re_group_pass_question rer on tu.USER_ID=rer.USER_ID " +
							   " where rer.PASS_GROUP_ID!=5");
			List<Object> lis = (List<Object>) query.getResultList();
			for (int i = 0; i < lis.size(); i++) {
				Object[] o = (Object[]) lis.get(i);
				TbSystemUser tsu = new TbSystemUser();
				if (tsu != null) {
					tsu.setUserId(Long.valueOf(o[0].toString()));
					tsu.setUserEmail(o[1].toString());
					listuserCreate.add(tsu);
				}
			}
		} catch (NoResultException e) {
			System.out.println(" [ Error en PasswordsManagementEJB.getUserCreate.NoResultException ]");
			e.printStackTrace();
			listuserCreate = null;
		}
		return listuserCreate;
	}
	
	
	/**@author psanchez
	 * Método encargado de Obtener lista de todos los estados para la Administración de Password
	 **/
	@Override
	public List<TbPassState>  getAllPassEst() {
		List<TbPassState> est = new ArrayList<TbPassState>();
		
		try {
			for (Object object : em.createQuery("select tps from TbPassState tps  where tps.passStateAvailable = 'S'").getResultList()){
				if (object != null && object instanceof TbPassState) {
					TbPassState tps = (TbPassState) object;
					est.add(tps);
				}
			}
		} catch (NoResultException e) {
			System.out.println(" [ Error en PasswordsManagementEJB.getAllPassEst().NoResultException ] ");
			e.printStackTrace();
			est = null;
		} catch (Exception e) {
			System.out.println(" [ Error en PasswordsManagementEJB.getAllPassGroup.Exception ] ");
			e.printStackTrace();
			est = null;
		}
		return est;		
	}
		
	/**
	 * @author psanchez Método encargado de crear la pregunta en el módulo
	 *         Administración de Password
	 **/
	@Override
	public boolean createQuestion(Long userId, Long groupId, Long stateId, String descriptionQuestions, String ip, Long creationUser){
		boolean result=false;
		try {

			TbPassQuestion tpq = new TbPassQuestion();
			TbQuestionType tqt = em.find(TbQuestionType.class, 1L);
			
			tpq.setQuestionTypeId(tqt);
			tpq.setPassQuestDate(new Timestamp(System.currentTimeMillis()));
			tpq.setPassQuestDesc(descriptionQuestions);
			tpq.setQuestionTypeId(tqt);
			tpq.setPassQuestDate(new Timestamp(System.currentTimeMillis()));
			em.persist(tpq);
			em.flush();

			ReGroupPassQuestion rgpq = new ReGroupPassQuestion();
			rgpq.setPassGroupId(em.find(TbPassGroup.class, groupId));
			rgpq.setPassQuestionId(tpq);
			if (stateId == null) {
				rgpq.setPassStateId(em.find(TbPassState.class, 1L));
			} else {
				rgpq.setPassStateId(em.find(TbPassState.class, stateId));
			}
			rgpq.setUserId(em.find(TbSystemUser.class, userId));
			rgpq.setRelationDate(new Timestamp(System.currentTimeMillis()));
			em.persist(rgpq);
			em.flush();
			result=true;

			log.insertLog("Se creó la pregunta exitosamente.",
					LogReference.CREATEQUESTION, LogAction.CREATE, ip,
					creationUser);
		} catch (NullPointerException e) {
			result=false;
	    	log.insertLog("Se ha presentado un error al momento de registrar la pregunta.",
			LogReference.CREATEQUESTION, LogAction.CREATEFAIL, ip, creationUser);
		} catch (Exception e) {
			 e.printStackTrace();
			 System.out.println(" [ Error en PasswordsManagementEJB.createQuestion() ] ");
		}
		return result;
	}
	
	/**
	 * @author psanchez Método encargado de retornar la lista de la relación
	 *         entre el grupo y la pregunta.
	 **/
	@Override
	@SuppressWarnings("unchecked")
	public List<ReGroupPassQ> getListAllGrupPassQ() {
		List<ReGroupPassQ> listReGrPassQuestion = new ArrayList<ReGroupPassQ>();

		try {
			Query listResult = em
			.createNativeQuery("select tpg.pass_group_name, "
								+ "tpg.pass_group_id, "
								+ "tpq.pass_question_description, "
								+ "decode(tpq.pass_question_date,null,'-',TO_CHAR(tpq.pass_question_date, 'DD/MM/YYYY HH:MI AM')), "
								+ "tsu.user_names ||' '|| tsu.user_second_names, " 
								+ "tps.pass_state_description, "
								+ "tps.pass_state_id, "
								+ "rgpq.group_pass_question_id, "
								+ "tpq.pass_question_id, "
								+ "count(rqr.pass_question_id) "
								+ "from re_group_pass_question rgpq "
								+ "inner join tb_pass_group tpg on rgpq.pass_group_id = tpg.pass_group_id "
								+ "inner join tb_pass_question tpq on rgpq.pass_question_id = tpq.pass_question_id "
								+ "inner join tb_system_user tsu on tsu.user_id = rgpq.user_id "
								+ "inner join tb_pass_state tps on tps.pass_state_id = rgpq.pass_state_id "
								+ "left join re_question_response rqr on rqr.pass_question_id = tpq.pass_question_id "
								+ "where rgpq.pass_state_id !=3 and rgpq.pass_state_id !=4 "
								+ "and tpg.pass_group_id <> 5 "
								+ "group by tpg.pass_group_name,tpg.pass_group_id, tpq.pass_question_description, "
								+ "tpq.pass_question_date, tsu.user_names, tsu.user_second_names, tps.pass_state_description,tps.pass_state_id, "
								+ "rgpq.group_pass_question_id, tpq.pass_question_id " 							
								+ "order by tpg.pass_group_id, tpq.pass_question_description asc ");
				List<Object> result = (List<Object>) listResult.getResultList();
				for (int i = 0; i < result.size(); i++) {
					Object[] obj = (Object[]) result.get(i);
					ReGroupPassQ reGrPaQu = new ReGroupPassQ();
					reGrPaQu.setNameGroupQuestionU(obj[0].toString());
					reGrPaQu.setGroupQuestionIdU(Long.parseLong(obj[1].toString()));
					reGrPaQu.setQuestionU(obj[2].toString());
					reGrPaQu.setCreationDateQuestioneU(obj[3].toString());
					reGrPaQu.setUserEmailU(obj[4].toString());
					reGrPaQu.setStateQuestionU(obj[5].toString());
					reGrPaQu.setStateQuestionIdU(Long.parseLong(obj[6].toString()));
					reGrPaQu.setReQuestionResponseIdU(Long.parseLong(obj[7].toString()));
					reGrPaQu.setQuestionIdU(Long.parseLong(obj[8].toString()));
					reGrPaQu.setModifiedQuestionIdU(Long.parseLong(obj[9] != null ? obj[9].toString() : "0"));
					listReGrPassQuestion.add(reGrPaQu);
				}
		} catch (Exception e) {
			System.out.println(" [ Error en PasswordsManagementEJB.getListAllGrupPassQ() ] ");
			e.printStackTrace();
		}
		return listReGrPassQuestion;
	}
	
	/**
	 * @author psanchez Método encargado de retornar la lista la busqueda
	 *         dependiendo del filtro ingresado por el Adminstrador.
	 **/
	@SuppressWarnings("unchecked")
	@Override
	public List<ReGroupPassQ> getListGrupPassByFilters(Long idGrupo,
			Long idEstado, Date fechaIni, Date fechaFin, String usuaCrea) {

		DateFormat fechaHora = new SimpleDateFormat("yyyy/MM/dd");
		List<ReGroupPassQ> listReGrPassQuest = new ArrayList<ReGroupPassQ>();
		String query = "";
		String order = "order by tpq.pass_question_date desc ";
		String group = "group by tpg.pass_group_name, tpg.pass_group_id, tpq.pass_question_description, tpq.pass_question_date, " +
				       "tsu.user_names, tsu.user_second_names, tps.pass_state_description, tps.pass_state_id, rgpq.group_pass_question_id, tpq.pass_question_id  ";
		try {
				query = "select tpg.pass_group_name, "
						+ "tpg.pass_group_id, "
						+ "tpq.pass_question_description, "
						+ "decode(tpq.pass_question_date,null,'-',TO_CHAR(tpq.pass_question_date, 'DD/MM/YYYY HH:MI AM')), " 
						+ "tsu.user_names ||' '|| tsu.user_second_names, "
						+ "tps.pass_state_description, "
						+ "tps.pass_state_id, "
						+ "rgpq.group_pass_question_id, "
						+ "tpq.pass_question_id, "
						+ "count(rqr.pass_question_id) "
						+ "from re_group_pass_question rgpq "
						+ "inner join tb_pass_group tpg on rgpq.pass_group_id = tpg.pass_group_id "
						+ "inner join tb_pass_question tpq on rgpq.pass_question_id = tpq.pass_question_id "
						+ "inner join tb_system_user tsu on tsu.user_id = rgpq.user_id "
						+ "inner join tb_pass_state tps on tps.pass_state_id = rgpq.pass_state_id "
						+ "left join re_question_response rqr on rqr.pass_question_id = tpq.pass_question_id "
						+ "where rgpq.pass_state_id!=3 " 
						+ "and rgpq.pass_state_id !=4 "
						+ "and tpg.pass_group_id <> 5 ";

				if (idGrupo != -1L) {
					query = query + " and rgpq.pass_group_id= " + idGrupo + " ";
				}
				if (idEstado != -1L) {
					query = query + " and tps.pass_state_id= " + idEstado + " ";
				}
				if (!usuaCrea.equals("-1")){
					if (!usuaCrea.equals("-99") && usuaCrea.contains("_")) {					
						query = query +"AND Upper(tsu.user_email) like '%\\_%' ESCAPE '\\' ";
					}
					if(!usuaCrea.equals("-99") && !usuaCrea.contains("_")){
						query = query +"AND lower(tsu.user_email) like '%"+usuaCrea.toLowerCase()+"%' ";
					}	
				}
				if (fechaIni != null && fechaFin != null) {
					String fIni = fechaHora.format(fechaIni);
					String fFin = fechaHora.format(fechaFin);
					query = query + 
					" AND rgpq.RELATION_DATE BETWEEN TO_DATE('"+ fIni + "','yyyy/mm/dd') AND TO_DATE('" + fFin + "','yyyy/mm/dd')+1";
				}

				Query listResult = em.createNativeQuery(query + group + order);
				List<Object> result = (List<Object>) listResult.getResultList();
				for (int i = 0; i < result.size(); i++) {
					Object[] obj = (Object[]) result.get(i);

					ReGroupPassQ reGrPaQu = new ReGroupPassQ();
					if (reGrPaQu != null) {
						reGrPaQu.setNameGroupQuestionU(obj[0].toString());
						reGrPaQu.setGroupQuestionIdU(Long.parseLong(obj[1].toString()));
						reGrPaQu.setQuestionU(obj[2].toString());
						reGrPaQu.setCreationDateQuestioneU(obj[3].toString());
						reGrPaQu.setUserEmailU(obj[4].toString());
						reGrPaQu.setStateQuestionU(obj[5].toString());
						reGrPaQu.setStateQuestionIdU(Long.parseLong(obj[6].toString()));
						reGrPaQu.setReQuestionResponseIdU(Long.parseLong(obj[7].toString()));
						reGrPaQu.setQuestionIdU(Long.parseLong(obj[8].toString()));
						reGrPaQu.setModifiedQuestionIdU(Long.parseLong(obj[9] != null ? obj[9].toString() : "0"));
						listReGrPassQuest.add(reGrPaQu);
					}
				}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error en PasswordsManagementEJB.getListGrupPassByFilters() ] ");
		}
		return listReGrPassQuest;
	}
	
	/**
	 * @author psanchez Método encargado de modificar la pregunta de seguridad
	 *         creada por el Adminstrador.
	 **/
	@Override
	public boolean updateQuestion(Long userId, Long groupId, Long stateId, Long questionId,
			String question, Long questionResponseId, Long questionType, String ip, Long creationUser) {
		boolean result=false;
		try {
			ReGroupPassQuestion rgpq = em.find(ReGroupPassQuestion.class,questionResponseId);
			TbQuestionType tqt = em.find(TbQuestionType.class, questionType);
			TbPassGroup tpg = em.find(TbPassGroup.class, groupId);
			TbPassState tps = em.find(TbPassState.class, stateId);
			TbSystemUser tsu = em.find(TbSystemUser.class, userId);
			TbPassQuestion tpq = em.find(TbPassQuestion.class, questionId);
			tpq.setPassQuestDesc(question);
			tpq.setQuestionTypeId(tqt);
			rgpq.setPassGroupId(tpg);
			rgpq.setPassStateId(tps);
			rgpq.setModificationDate(new Timestamp(System.currentTimeMillis()));
			rgpq.setUserId(tsu);
			      
			objectEM = new ObjectEM(em);
			if (objectEM.update(tpq)) {
				objectEM.create(rgpq);
				result=true;
				log.insertLog("Se ha modificado la pregunta exitosamente.",
				LogReference.MODIFYQUESTION, LogAction.UPDATE, ip,creationUser);
			} else {
				result=false;
				log.insertLog("Se ha presentado un error al momento de modificar la pregunta.",
				LogReference.MODIFYQUESTION, LogAction.UPDATEFAIL, ip, creationUser);
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error en PasswordsManagementEJB.getModificar() ] ");
			result=false;
		}
		return result;

	}
	
	/**
	 * @author psanchez Método encargado de eliminar la pregunta de seguridad
	 *         creada por el Adminstrador.
	 **/

	@Override
	public boolean deleteQuestion(Long userId, Long questionResponseId, String ip, Long creationUser) {
		boolean result=false;
		try {

			ReGroupPassQuestion rgpq = em.find(ReGroupPassQuestion.class,questionResponseId);
			TbPassState tps = em.find(TbPassState.class, 3L);
			TbSystemUser tsu = em.find(TbSystemUser.class, userId);
			
			rgpq.setPassStateId(tps);
			rgpq.setModificationDate(new Timestamp(System.currentTimeMillis()));
			rgpq.setUserId(tsu);
			
			objectEM = new ObjectEM(em);
			if (objectEM.update(rgpq)) {
				result=true;
				log.insertLog("Se ha eliminado la pregunta exitosamente.",
				LogReference.DELETEQUESTION, LogAction.DELETE, ip,creationUser);
			} else {
				result=false;				
				log.insertLog("Se ha presentado un error al momento de eliminar la pregunta.",
				LogReference.DELETEQUESTION, LogAction.DELETEFAIL, ip, creationUser);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(" [ Error en PasswordsManagementEJB.getEliminar() ] ");
			result=false;
		}
		return result;
	}
	/**
	 * @author psanchez Método encargado de verificar si existe o no el usuario
	 *         ingresado para la recuperación de la contraseña.
	 **/
	@SuppressWarnings("unchecked")
	@Override
	public Long validateUser(String userEmail) {
		Long userId=null;
		
		List<TbSystemUser> list = new ArrayList<TbSystemUser>();
		try {

			Query q = em
					.createQuery("select tsu from TbSystemUser tsu where tsu.userEmail = ?1 ");
			q.setParameter(1, userEmail);
			list = q.getResultList();
			if (list.size()>0){
				for (int i = 0; i < list.size(); i++) {
					TbSystemUser tsu = list.get(i);
					userId=tsu.getUserId();
				}
			} else {
				userId = null;
			}
		} catch (Exception e) {
			System.out.println(" [ Error en PasswordsManagementEJB.validateUser()] ");
			e.printStackTrace();
			userId = null;
		}
		return userId;
	}

	
	
	/**@author psanchez
	 * Método encargado de generar código OTP para la recuperación de contraseña, dejandolo como password del usuario, y se envía correo
	 **/
	@Override
	public boolean generarOtpRestart(Long userId, String ip){
		
		Long newOtp = null;
		usr = em.find(TbSystemUser.class, userId);
		boolean valid = true;
		
		newOtp = user.numRad(userId);
		if(newOtp!= null){
			try{
				ArrayList<String> parameters=new ArrayList<String>();
				parameters.add("#OTP="+newOtp);
				
				outbox.insertMessageOutbox(usr.getUserId(), 
		                   EmailProcess.PASSWORD_RESET,
		                   null,
		                   null,
		                   null, 
		                   null,
		                   null,
		                   null,
		                   null,
		                   null,
		                   usr.getUserId(),
			               null,
			               null,
			               null,
		                   true,
		                   parameters);
				
				usr.setUserPassword(Encryptor.getEncrypt(newOtp.toString()));
				usr.setUserPwdDate(new Timestamp(System.currentTimeMillis()));
				usr.setUserCountIntent(0);
				this.processNewOtp(userId, ip);
				valid = true;
			}catch (Exception e) {
				System.out.println(" [ Error en PasswordsManagementEJB.generarOtpRestart Error Al Enviar Correo ] ");
				e.printStackTrace();
			}
		}else{
			valid = false;
		}		
		return valid;
	}
	
	@SuppressWarnings("unused")
	public void processNewOtp(Long userId, String ip){		
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
				   "Se generó un nuevo código de seguridad para restablecer la contraseña del usuario " + 
				   user.getUserNames()+" "+ user.getUserSecondNames()+".", user.getUserId(), ip, 1,"",0,"","",null,null,null);
		//proceso cliente
		TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,user.getUserId());
		Long idPTC;
			if(ptc==null){
				idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, user.getUserId(), ip, user.getUserId());
			}else{
				idPTC=ptc.getProcessTrackId();
			}
		Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.PASSWORD_MANAGEMENT, 
				"Se generó un nuevo código de seguridad para restablecer la contraseña del usuario " + 
				user.getUserNames()+" "+ user.getUserSecondNames()+".", user.getUserId(), ip, 1, "", 0,"","",null,null,null);
	}
	
	/**@author psanchez
	 * Método encargado de registrar en ver procesos y se envía notificación, que el usuario no tiene las preguntas configuradas para la recuperación de la contraseña
	 **/
	@SuppressWarnings("unused")
	@Override
	public String registrarVerProcesos(Long user, String ip){
		usr = em.find(TbSystemUser.class, user);
		outbox.insertMessageOutbox(usr.getUserId(), 
                EmailProcess.RESTORE_PASSWORD_WITHOUT_SQ,
                null,
                null,
                null, 
                null,
                null,
                null,
                null,
                null,
                usr.getUserId(),
	            null,
	            null,
	            null,
                true,
                null);
		
		//proceso administrador
        TbProcessTrack pt = process.searchProcess(ProcessTrackType.CLIENT, usr.getUserId() );
		Long idPTA;
			if(pt==null){
				idPTA=process.createProcessTrack(ProcessTrackType.CLIENT,  usr.getUserId(), ip, usr.getUserId());
			}else{
				idPTA=pt.getProcessTrackId();
			}
		Long detailA=process.createProcessDetail(idPTA,ProcessTrackDetailType.PASSWORD_MANAGEMENT, 
				   "El usuario " + usr.getUserNames()+ " "+ usr.getUserSecondNames() + 
				   " intento restaurar contraseña sin preguntas de seguridad seleccionadas.",
				   usr.getUserId(), ip, 1,"",0, "", "",null,null,null);
		//proceso cliente
		TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,usr.getUserId());
		Long idPTC;
			if(ptc==null){
				idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, usr.getUserId(), ip, usr.getUserId());
			}else{
				idPTC=ptc.getProcessTrackId();
			}
		Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.PASSWORD_MANAGEMENT, 
				 "El usuario " + usr.getUserNames()+ " "+usr.getUserSecondNames() + 
				 " intento restaurar contraseña sin preguntas de seguridad seleccionadas.", 
				 usr.getUserId(), ip, 1, "", 0,"","",null,null,null);		
		return "Señor usuario, nuestros asesores se comunicarán con usted para atender su solicitud.";	
	}
	
	/**@author psanchez
	 * Método encargado de extraer el id del usuario por medio del correo electrónico.
	 **/
	@Override
	public Long getUsrId(String userEmail){	
		Long usrId = 0L;
		try{
			Query q = em.createQuery("select userId from TbSystemUser tsu where tsu.userEmail = ?1 ");
			q.setParameter(1, userEmail);
			usrId = (Long) q.getSingleResult();						
		}catch (Exception e) {
			e.printStackTrace();
		}				
		return usrId;		
	}

	
	/**@author psanchez
	 * Método encargado de registrar en Ver Procesos y seguimiento de procesos la cancelación del restablecimiento de la contraseña.
	 **/
	@SuppressWarnings("unused")
	@Override	
	public void regVerProcesosRC(Long userId, String ip){		
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
				   "El usuario canceló la validación de restablecimiento de contraseña.", user.getUserId(), ip, 1,"",0,"","",null,null,null);
		//proceso cliente
		TbProcessTrack ptc = process.searchProcess(ProcessTrackType.MY_CLIENT_PROCESS,user.getUserId());
		Long idPTC;
			if(ptc==null){
				idPTC=process.createProcessTrack(ProcessTrackType.MY_CLIENT_PROCESS, user.getUserId(), ip, user.getUserId());
			}else{
				idPTC=ptc.getProcessTrackId();
			}
		Long detailC=process.createProcessDetail(idPTC,ProcessTrackDetailType.PASSWORD_MANAGEMENT, 
				"El usuario canceló la validación de restablecimiento de contraseña", user.getUserId(), ip, 1, "", 0,"","",null,null,null);
	}
	
	/**@author psanchez
	 * Método encargado de validar si la respuesta de la pregunta 5 es correcta.
	 **/
	@Override
	public String validateResp5(String resp5){
		String msj = "";
		BigDecimal idQuestResp;
		try{
			Query q = em.createNativeQuery("select rqr.question_response_id from re_question_response rqr " +
					" inner join tb_response tr on tr.response_id = rqr.response_id " +
					" where upper(tr.response_description) = upper(?1) ");
			q.setParameter(1, resp5.toUpperCase());
			idQuestResp = (BigDecimal) q.getSingleResult();
			if(idQuestResp == null){
				msj="1";
			}			
		}catch (Exception e) {
			System.out.println(" [ Error en PasswordsManagementEJB.validateResp5() ] ");
			e.printStackTrace();
		}	
		return msj;
	}
	
	
	/**@author psanchez
	 * Método encargado de mostrar las preguntas configuradas por el usuario.
	 */
	@Override
	public String returnPgr(Long usrsId, int i){
		String pregunta = "";		
		try{
			Query q = em.createNativeQuery(" select tpq.pass_question_description from re_question_response rqr " +
					" inner join tb_pass_question tpq on tpq.pass_question_id  = rqr.pass_question_id " +
					" inner join re_group_pass_question rgpq on rgpq.pass_question_id = rqr.pass_question_id " +
					" where rgpq.pass_group_id = ?2 " +
					" and rqr.user_id = ?1 " +
					" and rqr.state_relation = 0 ");
			q.setParameter(1, usrsId);
			q.setParameter(2, i);
			pregunta = (String) q.getSingleResult();
		}catch (NullPointerException e) {
			System.out.println(" [ Error en PasswordsManagementEJB.returnPgr.NullPointerException) ] "+e.getLocalizedMessage());
		}catch(NoResultException ex){
			System.out.println(" [ Error en PasswordsManagementEJB.returnPgr.NoResultException) ] "+ex.getLocalizedMessage());
		}catch (Exception e) {
			System.out.println(" [ Error en PasswordsManagementEJB.returnPgr.Exception ] "+e.getLocalizedMessage());
			e.printStackTrace();
		}						
		return pregunta;
	}
	
	/**@author psanchez
	 * Método encargado de validar las preguntas configuradas por el admon.
	 */
	@Override
	public String validateQuestion(String question){
		String passGroupName = null;
		try {
			String sqlGroup = "select count(tbpg.PASS_GROUP_NAME) "
				+ "from  TB_PASS_QUESTION tpq "
				+ "inner join RE_GROUP_PASS_QUESTION rgpq on rgpq.PASS_QUESTION_ID=tpq.PASS_QUESTION_ID "
				+ "inner join TB_PASS_STATE tps on tps.PASS_STATE_ID=rgpq.PASS_STATE_ID "
				+ "inner join TB_PASS_GROUP tbpg on tbpg.PASS_GROUP_ID=rgpq.PASS_GROUP_ID "
				+ "where (tps.PASS_STATE_ID=1 or tps.PASS_STATE_ID=2) "
				+ "and tpq.PASS_QUESTION_DESCRIPTION  = '"
				+ question
				+ "'";

			Query query = em.createNativeQuery(sqlGroup);
			BigDecimal groupCount = (BigDecimal) query.getSingleResult();
			
			if(groupCount.intValue()>0){			
				String sqlString = "select MIN(tbpg.PASS_GROUP_NAME) "
						+ "from TB_PASS_QUESTION tpq "
						+ "inner join RE_GROUP_PASS_QUESTION rgpq on rgpq.PASS_QUESTION_ID=tpq.PASS_QUESTION_ID "
						+ "inner join TB_PASS_STATE tps on tps.PASS_STATE_ID=rgpq.PASS_STATE_ID "
						+ "inner join TB_PASS_GROUP tbpg on tbpg.PASS_GROUP_ID=rgpq.PASS_GROUP_ID "
						+ "where (tps.PASS_STATE_ID=1 or tps.PASS_STATE_ID=2) "
						+ "and tpq.PASS_QUESTION_DESCRIPTION  = '"
						+ question
						+ "'";
	
				Query query1 = em.createNativeQuery(sqlString);
				passGroupName = (String) query1.getSingleResult();		
				
			}else if(groupCount.intValue()== 1){			
				passGroupName = groupCount.toString();
				
			}
			
		} catch (Exception e) {
			System.out.println(" [ Error en validateQuestion()] ");
			e.printStackTrace();
		}
		return passGroupName;
	}

	/**@author psanchez
	 * Método encargado de validar la pregunta a modificar por el admon.
	 */
	@Override
	public String validateQuestionUpdate(String question, Long groupId, Long groupIdTemp) {
		String passGroupName = null;
		try {
			String sqlGroup = "select count(tbpg.PASS_GROUP_NAME) "
				+ "from  TB_PASS_QUESTION tpq "
				+ "inner join RE_GROUP_PASS_QUESTION rgpq on rgpq.PASS_QUESTION_ID=tpq.PASS_QUESTION_ID "
				+ "inner join TB_PASS_STATE tps on tps.PASS_STATE_ID=rgpq.PASS_STATE_ID "
				+ "inner join TB_PASS_GROUP tbpg on tbpg.PASS_GROUP_ID=rgpq.PASS_GROUP_ID "
				+ "where tbpg.PASS_GROUP_ID <> "+groupId 
				+ " and (tps.PASS_STATE_ID=1 or tps.PASS_STATE_ID=2) "
				+ " and tpq.PASS_QUESTION_DESCRIPTION  = '"
				+ question
				+ "'";

			Query query = em.createNativeQuery(sqlGroup);
			BigDecimal groupCount = (BigDecimal) query.getSingleResult();
			if(groupCount.intValue()>0){			
				String sqlString = "select MIN(tbpg.PASS_GROUP_NAME) "
						+ "from TB_PASS_QUESTION tpq "
						+ "inner join RE_GROUP_PASS_QUESTION rgpq on rgpq.PASS_QUESTION_ID=tpq.PASS_QUESTION_ID "
						+ "inner join TB_PASS_STATE tps on tps.PASS_STATE_ID=rgpq.PASS_STATE_ID "
						+ "inner join TB_PASS_GROUP tbpg on tbpg.PASS_GROUP_ID=rgpq.PASS_GROUP_ID "
						+ "where (tps.PASS_STATE_ID=1 or tps.PASS_STATE_ID=2) "
						+ "and tpq.PASS_QUESTION_DESCRIPTION  = '"
						+ question
						+ "'";
	
				Query query1 = em.createNativeQuery(sqlString);
				passGroupName = (String) query1.getSingleResult();		
			
			}else if(groupCount.intValue()== 1){			
				return passGroupName;
				
			}
			
		} catch (Exception e) {
			System.out.println(" [ Error en validateQuestionUpdate()] ");
			e.printStackTrace();
		}
		return passGroupName;
	}
	
}
