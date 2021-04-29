package com.ufps.web.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the convenio database table.
 * 
 */
@Entity
@Table(name="convenio")
@NamedQuery(name="Convenio.findAll", query="SELECT c FROM Convenio c")
public class Convenio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_convenio")
	private int idConvenio;

	private String descripcion;

	@Temporal(TemporalType.DATE)
	private Date fecha;

	private String institucion;

	@Column(name="num_convenio")
	private String numConvenio;

	//bi-directional many-to-one association to Actividad
	@OneToMany(mappedBy="convenio")
	private List<Actividad> actividads;

	public Convenio() {
	}

	public int getIdConvenio() {
		return this.idConvenio;
	}

	public void setIdConvenio(int idConvenio) {
		this.idConvenio = idConvenio;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getInstitucion() {
		return this.institucion;
	}

	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}

	public String getNumConvenio() {
		return this.numConvenio;
	}

	public void setNumConvenio(String numConvenio) {
		this.numConvenio = numConvenio;
	}

	public List<Actividad> getActividads() {
		return this.actividads;
	}

	public void setActividads(List<Actividad> actividads) {
		this.actividads = actividads;
	}

	public Actividad addActividad(Actividad actividad) {
		getActividads().add(actividad);
		actividad.setConvenio(this);

		return actividad;
	}

	public Actividad removeActividad(Actividad actividad) {
		getActividads().remove(actividad);
		actividad.setConvenio(null);

		return actividad;
	}

}