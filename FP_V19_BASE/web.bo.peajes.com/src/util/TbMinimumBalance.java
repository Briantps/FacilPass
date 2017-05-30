package util;

import java.io.Serializable;


public class TbMinimumBalance implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String category;
	private String categoryvalue;
	private String minimumvalue;
	private String date;
	private String user;
	private String obser;
	private String state;
	private String editar;
	private String idminimum;

	
	
	public TbMinimumBalance(){
    	super();
    }




	public String getCategory() {
		return category;
	}
	public String getIdminimum() {
		return idminimum;
	}
	public void setIdminimum(String idminimum) {
		this.idminimum = idminimum;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getCategoryvalue() {
		return categoryvalue;
	}
	public void setCategoryvalue(String categoryvalue) {
		this.categoryvalue = categoryvalue;
	}
	public String getMinimumvalue() {
		return minimumvalue;
	}
	public void setMinimumvalue(String minimumvalue) {
		this.minimumvalue = minimumvalue;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getObser() {
		return obser;
	}
	public void setObser(String obser) {
		this.obser = obser;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getEditar() {
		return editar;
	}
	public void setEditar(String editar) {
		this.editar = editar;
	}
	
	
}