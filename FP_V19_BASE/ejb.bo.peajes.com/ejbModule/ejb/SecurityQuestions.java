package ejb;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import jpa.ReGroupPassQuestion;

/**
 * 
 * @author psanchez
 *  
 */
@Remote
public interface SecurityQuestions {

	public List<ReGroupPassQuestion> getAllQuestion(Long passGroupId);
	public boolean saveResponse(Long idUsrs, ArrayList<String> resp, ArrayList<Long> idPreg, String p5);
	public boolean validateQuestionsResponse(Long userId);
	public void InicioConfPregVP(Long userId, String ip);
	public int validateAnswer(Long userId, String resp1, String resp2, String resp3, String resp4);
	public void rememberQuestionsAfter(Long userId, String ip);
	public void validateResponseReset(Long userId, String ip);
	public boolean validateAnswerFive(Long userId, String resp5);
	public boolean validateAnswerIntent(Long userId, int count, String ip);
	public boolean userResponseRelation(Long userId);
	public void userCountQuestions(Long userId);
	public boolean validateQuestionsResponseOld(Long userId);
	public void cancelConfPregVP(Long userId, String ip);
	
}
