package jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name="TAREA_TRANS")
public class TbTareaTrans implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TAREA_TRANS_GENERATOR")
	@SequenceGenerator(name = "TAREA_TRANS_GENERATOR", sequenceName = "TAREA_TRANS_SEQ",allocationSize=1)
	@Column(name="ID_TAREA_TRANS")
	private Long idTareaTrans;
		
	@Column(name="NOMBRE_TABLA")
	private String nombreTabla;
	
	@Column(name="ID_PK_TABLA")
	private String idPkTabla;
	
	@Column(name="ESTADO")
	private int estado;

	public void setIdTareaTrans(Long idTareaTrans) {
		this.idTareaTrans = idTareaTrans;
	}

	public Long getIdTareaTrans() {
		return idTareaTrans;
	}

	public void setNombreTabla(String nombreTabla) {
		this.nombreTabla = nombreTabla;
	}

	public String getNombreTabla() {
		return nombreTabla;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public int getEstado() {
		return estado;
	}

	public void setIdPkTabla(String idPkTabla) {
		this.idPkTabla = idPkTabla;
	}

	public String getIdPkTabla() {
		return idPkTabla;
	}
	
}
