/**
 * 
 */
package jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the TB_REPLACEMENT_CAUSE database table.
 * @author tmolina
 */
 @Entity
 @Table(name="TB_REPLACEMENT_CAUSE")
public class TbReplacementCause implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TB_REPLACEMENT_CAUSE_REPLACEMENTCAUSEID_GENERATOR")
	@SequenceGenerator(name="TB_REPLACEMENT_CAUSE_REPLACEMENTCAUSEID_GENERATOR",  sequenceName = "TB_REPLACEMENT_CAUSE_SEQ",allocationSize=1)
	@Column(name="REPLACEMENT_CAUSE_ID")
	private Long replacementCauseId;
	
	@Column(name="REPLACEMENT_CAUSE_NAME")
	private String replacementCauseName;
		
	public TbReplacementCause(){
		super();
	}

	/**
	 * @param replacementCauseId the replacementCauseId to set
	 */
	public void setReplacementCauseId(Long replacementCauseId) {
		this.replacementCauseId = replacementCauseId;
	}

	/**
	 * @return the replacementCauseId
	 */
	public Long getReplacementCauseId() {
		return replacementCauseId;
	}

	/**
	 * @param replacementCauseName the replacementCauseName to set
	 */
	public void setReplacementCauseName(String replacementCauseName) {
		this.replacementCauseName = replacementCauseName;
	}

	/**
	 * @return the replacementCauseName
	 */
	public String getReplacementCauseName() {
		return replacementCauseName;
	}
}