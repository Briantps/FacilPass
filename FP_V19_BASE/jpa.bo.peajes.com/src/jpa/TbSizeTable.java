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
@Table(name = "TB_SIZE_TABLE")
public class TbSizeTable implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id	
	@Column(name="SIZE_TABLE_ID")
	private Long sizeTableId;
		
	@Column(name="SIZE_TABLE_SIZE")
	private long size;
    
		
	/**
	 * Constructor
	 */
    public TbSizeTable() {
    	super();
    }


	public void setSizeTableId(Long sizeTableId) {
		this.sizeTableId = sizeTableId;
	}


	public Long getSizeTableId() {
		return sizeTableId;
	}


	public void setSize(long size) {
		this.size = size;
	}


	public long getSize() {
		return size;
	}
 }