package jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * The persistent class for the TB_ROLE database table.
 * 
 */
@Entity
@Table(name = "TB_TYPE_MESSAGE_WYSIWYG")
public class TbTypeMessageWysiwyg implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id	
	@Column(name="TYPE_MESSAGE_WYSIWYG_ID")
	private Long typeMessageWysiwygId;

		
	@Column(name="TYPE_MESSAGE_WYSIWYG_DESC")
	private String typeMessageWysiwygDesc;
		
	/**
	 * Constructor
	 */
    public TbTypeMessageWysiwyg() {
    	super();
    }


	public void setTypeMessageWysiwygDesc(String typeMessageWysiwygDesc) {
		this.typeMessageWysiwygDesc = typeMessageWysiwygDesc;
	}


	public String getTypeMessageWysiwygDesc() {
		return typeMessageWysiwygDesc;
	}


	public void setTypeMessageWysiwygId(Long typeMessageWysiwygId) {
		this.typeMessageWysiwygId = typeMessageWysiwygId;
	}


	public Long getTypeMessageWysiwygId() {
		return typeMessageWysiwygId;
	}
    
}