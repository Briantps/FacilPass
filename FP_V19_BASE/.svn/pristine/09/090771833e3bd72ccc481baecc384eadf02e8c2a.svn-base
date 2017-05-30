/**
 * 
 */
package mBeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

/**
 * @author choyos
 * 
 */
public class TestMBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private String nombre;
	private String apellido;
	private String identificacion;
	private Integer tamPaquete;
	
	public List<SelectItem> getOpcTamPaquete(){
		List<SelectItem> list = new ArrayList<SelectItem>();
		list.add(new SelectItem(1, "Pequeño"));
		list.add(new SelectItem(2, "Mediano"));
		list.add(new SelectItem(3, "Grande"));
		return list;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getNombre() {
		return nombre;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getApellido() {
		return apellido;
	}
	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}
	public String getIdentificacion() {
		return identificacion;
	}
	public void setTamPaquete(Integer tamPaquete) {
		this.tamPaquete = tamPaquete;
	}
	public Integer getTamPaquete() {
		return tamPaquete;
	}
	
}
