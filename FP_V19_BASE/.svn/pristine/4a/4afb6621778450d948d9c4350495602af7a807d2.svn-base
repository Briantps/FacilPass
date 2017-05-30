/**
 * 
 */
package jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author tmolina
 *
 */
@Entity
@Table(name="TB_DEPARTMENT")
public class TbDepartment implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="DEPARTMENT_ID")
	private Long departmentId;
	
	@Column(name="DEPARTMENT_NAME")
	private String departmentName;
	
	/**
	 * Constructor
	 */
	public TbDepartment(){
		super();
	}

	/**
	 * @param departmentId the departmentId to set
	 */
	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	/**
	 * @return the departmentId
	 */
	public Long getDepartmentId() {
		return departmentId;
	}

	/**
	 * @param departmentName the departmentName to set
	 */
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	/**
	 * @return the departmentName
	 */
	public String getDepartmentName() {
		return departmentName;
	}
}