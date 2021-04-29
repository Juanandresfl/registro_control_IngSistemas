package com.ufps.web.entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the actividad database table.
 * 
 */
@Entity
@Table(name="actividad")
@NamedQuery(name="Actividad.findAll", query="SELECT a FROM Actividad a")
public class Actividad implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_actividad")
	private int idActividad;
	
	@NotBlank(message = "El campo no debe estar vacio")
	private String ciudad;

	@NotBlank(message = "El campo no debe estar vacio")
	private String descripcion;
	
	@Min(value = 1,message = "El campo no debe estar vacio")
	private int duracion;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fecha;

	@Min(value = 1,message = "El campo no debe estar vacio")
	@Column(name="num_participantes")
	private int numParticipantes;

	@NotBlank(message = "El campo no debe estar vacio")
	private String pais;

	//bi-directional many-to-one association to Convenio
	@ManyToOne
	@JoinColumn(name="id_convenio")
	private Convenio convenio;

	//bi-directional many-to-one association to TipoActividad
	@ManyToOne
	@JoinColumn(name="id_tipo")
	private TipoActividad tipoActividad;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="codigo_usuario")
	private Usuario usuario;

	//bi-directional many-to-one association to Evidencia
	@OneToMany(mappedBy="actividad")
	private List<Evidencia> evidencias;

	public Actividad() {
	}

	public int getIdActividad() {
		return this.idActividad;
	}

	public void setIdActividad(int idActividad) {
		this.idActividad = idActividad;
	}

	public String getCiudad() {
		return this.ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getDuracion() {
		return this.duracion;
	}

	public void setDuracion(int duracion) {
		this.duracion = duracion;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public int getNumParticipantes() {
		return this.numParticipantes;
	}

	public void setNumParticipantes(int numParticipantes) {
		this.numParticipantes = numParticipantes;
	}

	public String getPais() {
		return this.pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public Convenio getConvenio() {
		return this.convenio;
	}

	public void setConvenio(Convenio convenio) {
		this.convenio = convenio;
	}

	public TipoActividad getTipoActividad() {
		return this.tipoActividad;
	}

	public void setTipoActividad(TipoActividad tipoActividad) {
		this.tipoActividad = tipoActividad;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Evidencia> getEvidencias() {
		return this.evidencias;
	}

	public void setEvidencias(List<Evidencia> evidencias) {
		this.evidencias = evidencias;
	}

	public Evidencia addEvidencia(Evidencia evidencia) {
		getEvidencias().add(evidencia);
		evidencia.setActividad(this);

		return evidencia;
	}

	public Evidencia removeEvidencia(Evidencia evidencia) {
		getEvidencias().remove(evidencia);
		evidencia.setActividad(null);

		return evidencia;
	}

}