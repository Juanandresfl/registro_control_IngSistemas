package com.ufps.web.entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import java.util.List;


/**
 * The persistent class for the tipo_actividad database table.
 * 
 */
@Entity
@Table(name="tipo_actividad")
@NamedQuery(name="TipoActividad.findAll", query="SELECT t FROM TipoActividad t")
public class TipoActividad implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_tipo_actividad")
	private int idTipoActividad;

	@NotBlank(message = "El campo no debe estar vacio")
	private String nombre;
	
	@NotBlank(message = "El campo no debe estar vacio")
	private String descripcion;

	//bi-directional many-to-one association to Actividad
	@OneToMany(mappedBy="tipoActividad")
	private List<Actividad> actividads;

	public TipoActividad() {
	}

	public int getIdTipoActividad() {
		return this.idTipoActividad;
	}

	public void setIdTipoActividad(int idTipoActividad) {
		this.idTipoActividad = idTipoActividad;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<Actividad> getActividads() {
		return this.actividads;
	}

	public void setActividads(List<Actividad> actividads) {
		this.actividads = actividads;
	}

	public Actividad addActividad(Actividad actividad) {
		getActividads().add(actividad);
		actividad.setTipoActividad(this);

		return actividad;
	}

	public Actividad removeActividad(Actividad actividad) {
		getActividads().remove(actividad);
		actividad.setTipoActividad(null);

		return actividad;
	}

}