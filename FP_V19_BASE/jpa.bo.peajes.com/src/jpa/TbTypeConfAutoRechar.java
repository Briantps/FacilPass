package jpa;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * The persistent class for the TB_ROLE database table.
 * 
 */
@Entity
@Table(name = "TB_TYPE_CONF_AUTO_RECHAR")
public class TbTypeConfAutoRechar implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id	
	@Column(name="TYPE_CONF_AUTO_RECHAR_ID")
	private Long typeConfAutoRecharId;
		
	@Column(name="TYPE_CONF_AUTO_RECHAR_NAME")
	private String typeConfAutoRecharName;
    	
	@ManyToOne
	@JoinColumn (name="SIZE_TABLE_ID")
	private TbSizeTable tbSizeTable;
	
	@ManyToOne
	@JoinColumn (name="STATE_ID")
	private TbState tbState;
	
	@ManyToOne
	@JoinColumn (name="TYPE_MESSAGE_WYSIWYG_ID")
	private TbTypeMessageWysiwyg tbTypeMessageWysiwyg;
		
	/**
	 * Constructor
	 */
    public TbTypeConfAutoRechar() {
    	super();
    }

	public Long getTypeConfAutoRecharId() {
		return typeConfAutoRecharId;
	}

	public void setTypeConfAutoRecharId(Long typeConfAutoRecharId) {
		this.typeConfAutoRecharId = typeConfAutoRecharId;
	}

	public String getTypeConfAutoRecharName() {
		return typeConfAutoRecharName;
	}

	public void setTypeConfAutoRecharName(String typeConfAutoRecharName) {
		this.typeConfAutoRecharName = typeConfAutoRecharName;
	}

	public TbSizeTable getTbSizeTable() {
		return tbSizeTable;
	}

	public void setTbSizeTable(TbSizeTable tbSizeTable) {
		this.tbSizeTable = tbSizeTable;
	}

	public TbState getTbState() {
		return tbState;
	}

	public void setTbState(TbState tbState) {
		this.tbState = tbState;
	}

	public void setTbTypeMessageWysiwyg(TbTypeMessageWysiwyg tbTypeMessageWysiwyg) {
		this.tbTypeMessageWysiwyg = tbTypeMessageWysiwyg;
	}

	public TbTypeMessageWysiwyg getTbTypeMessageWysiwyg() {
		return tbTypeMessageWysiwyg;
	}	
 }