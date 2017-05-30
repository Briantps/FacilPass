package ejb.taskeng.email.impl;

import java.util.HashMap;
import java.util.Map;

import ejb.taskeng.email.FromField;
import ejb.taskeng.email.Header;
import ejb.taskeng.email.SubjectField;



public class HeaderBase implements Header {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private FromField fromField;
	private Map<Integer, SubjectField> subjectFields = new HashMap<Integer, SubjectField>(); 
	private String priority;
	
	public HeaderBase(){
	}
	
	/* (non-Javadoc)
	 * @see email.Header#setFromFields(java.util.List)
	 */
	public void setFromField(FromField fromField) {
		this.fromField = fromField;
	}

	/* (non-Javadoc)
	 * @see email.Header#getFromFields()
	 */
	public FromField getFromField() {
		return fromField;
	}

	/* (non-Javadoc)
	 * @see email.Header#setSubjectFields(java.util.Map)
	 */
	public void setSubjectFields(Map<Integer, SubjectField> subjectFields) {
		this.subjectFields = subjectFields;
	}

	/* (non-Javadoc)
	 * @see email.Header#getSubjectFields()
	 */
	public Map<Integer, SubjectField> getSubjectFields() {
		return subjectFields;
	}

	/* (non-Javadoc)
	 * @see email.Header#addSubjectField(email.SubjectField)
	 */
	public void addSubjectField(SubjectField e){
		if(!subjectFields.containsKey(e.getId())){
			subjectFields.put(e.getId(), e);
		}
	}
	
	/* (non-Javadoc)
	 * @see email.Header#getSubjectField(java.lang.Integer)
	 */
	public SubjectField getSubjectField(int id){
		return subjectFields.get(id);
	}
	
	/* (non-Javadoc)
	 * @see email.Header#setPriority(java.lang.String)
	 */
	public void setPriority(String priority) {
		this.priority = priority;
	}

	/* (non-Javadoc)
	 * @see email.Header#getPriority()
	 */
	public String getPriority() {
		return priority;
	}
}
