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
 * @author psanchez
 *
 */
@Entity
@Table(name="TB_TYPE_DOCUMENT")
public class TbTypeDocument implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "TB_TYPE_DOCUMENT_SEQ", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "TB_TYPE_DOCUMENT_SEQ", sequenceName = "TB_TYPE_DOCUMENT_SEQ",allocationSize=1)
	
	@Column(name="TYPE_DOCUMENT_ID")
	private Long typeDocumentId;
	
	@Column(name="TYPE_DOCUMENT_DESCRIPTION")
	private String typeDocumentDescription;
	
	@Column(name="TYPE_DOCUMENT_STATE")
	private Integer typeDocumentState;
	
	public TbTypeDocument(){
		super();
	}

	/**
	 * @param typeDocumentId the typeDocumentId to set
	 */
	public void setTypeDocumentId(Long typeDocumentId) {
		this.typeDocumentId = typeDocumentId;
	}

	/**
	 * @return the typeDocumentId
	 */
	public Long getTypeDocumentId() {
		return typeDocumentId;
	}

	/**
	 * @param typeDocumentDescription the typeDocumentDescription to set
	 */
	public void setTypeDocumentDescription(String typeDocumentDescription) {
		this.typeDocumentDescription = typeDocumentDescription;
	}

	/**
	 * @return the typeDocumentDescription
	 */
	public String getTypeDocumentDescription() {
		return typeDocumentDescription;
	}

	/**
	 * @param typeDocumentState the typeDocumentState to set
	 */
	public void setTypeDocumentState(Integer typeDocumentState) {
		this.typeDocumentState = typeDocumentState;
	}

	/**
	 * @return the typeDocumentState
	 */
	public Integer getTypeDocumentState() {
		return typeDocumentState;
	}

	
	
}