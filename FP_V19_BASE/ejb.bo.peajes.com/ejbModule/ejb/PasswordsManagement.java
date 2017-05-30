package ejb;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import util.ReGroupPassQ;

import jpa.TbPassGroup;
import jpa.TbPassState;
import jpa.TbSystemUser;

/**
 * 
 * @author psanchez
 *  
 */
@Remote

public interface PasswordsManagement {
	
	public List<TbPassGroup>  getAllPassGroup();
	public List<TbPassState>  getAllPassEst();
	public boolean createQuestion(Long userId, Long groupId, Long stateId, 
			String descriptionQuestions, String ip, Long creationUser);
	public boolean updateQuestion(Long userId, Long groupId, Long stateId, Long questionId,
			String question, Long questionResponseId, Long questionType, String ip, Long creationUser);	
	public boolean deleteQuestion(Long userId, Long questionResponseId, String ip, Long creationUser);
	public List<ReGroupPassQ> getListAllGrupPassQ();
	public List<ReGroupPassQ> getListGrupPassByFilters(Long idGrupo, Long idEstado, Date fechaIni, Date fechaFin, String usuaCrea);
	public Long validateUser(String userEmail);
	public boolean generarOtpRestart(Long userId, String ip);
	public String registrarVerProcesos(Long user, String ip);
	public List<TbSystemUser> getUserCreate();
	public String validateQuestion(String question);
	public Long getUsrId(String userEmail);
	public void regVerProcesosRC(Long userId, String ip);
	public String validateResp5(String resp5);
	public String returnPgr(Long userId, int i);
	public String validateQuestionUpdate(String question, Long groupId, Long groupIdTemp);
	public void processNewOtp(Long userId, String ip);

}
