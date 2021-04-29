package com.ufps.web.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the evidencia database table.
 * 
 */
@Entity
@Table(name="evidencia")
@NamedQuery(name="Evidencia.findAll", query="SELECT e FROM Evidencia e")
public class Evidencia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_evidencia")
	private int idEvidencia;

	@Temporal(TemporalType.DATE)
	private Date fecha;

	private String url;

	//bi-directional many-to-one association to Actividad
	@ManyToOne
	@JoinColumn(name="id_actividad")
	private Actividad actividad;

	public Evidencia() {
	}

	public int getIdEvidencia() {
		return this.idEvidencia;
	}

	public void setIdEvidencia(int idEvidencia) {
		this.idEvidencia = idEvidencia;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Actividad getActividad() {
		return this.actividad;
	}

	public void setActividad(Actividad actividad) {
		this.actividad = actividad;
	}

}