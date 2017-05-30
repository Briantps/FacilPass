package ejb.taskeng.email;

import java.io.Serializable;
import java.util.Map;

/**
 * Contains header data of email definition
 * @author Mauricio Gil 
 */
public interface Header extends Serializable {

	/**
	 * Sets data of the from field
	 * @param fromField
	 */
	public void setFromField(FromField fromField);

	/**
	 * Gets data of the from field
	 * @return
	 */
	public FromField getFromField();

	/**
	 * Sets subject map
	 * @param subjectFields
	 */
	public void setSubjectFields(Map<Integer, SubjectField> subjectFields);

	/**
	 * Gets subject map
	 * @return
	 */
	public Map<Integer, SubjectField> getSubjectFields();

	/**
	 * 
	 * @param e
	 */
	public void addSubjectField(SubjectField e);

	/**
	 * 
	 * @param priority
	 */
	public void setPriority(String priority);

	/**
	 * 
	 * @return
	 */
	public String getPriority();

	/**
	 * 
	 * @param id
	 * @return
	 */
	public SubjectField getSubjectField(int id);

}