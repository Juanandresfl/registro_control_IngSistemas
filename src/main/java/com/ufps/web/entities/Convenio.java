package com.ufps.web.entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;

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

	@NotBlank(message = "El campo no debe estar vacio")
	private String descripcion;

	@Temporal(TemporalType.DATE)
	private Date fecha;

	@NotBlank(message = "El campo no debe estar vacio")
	private String institucion;

	@NotBlank(message = "El campo no debe estar vacio")
	@Column(name="num_convenio")
	private String numConvenio;

	//bi-directional many-to-one association to Actividad
	@OneToMany(mappedBy="convenio")
	private List<Actividad> actividads;
	
	//bi-directional many-to-one association to Evidencia
//	se coloca el fetch EAGER porque en la vista traigo directamente todas las evidencias
	@OneToMany(mappedBy="convenio",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
	private List<Evidencia> evidencias;

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

	public List<Evidencia> getEvidencias() {
		return evidencias;
	}

	public void setEvidencias(List<Evidencia> evidencias) {
		this.evidencias = evidencias;
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