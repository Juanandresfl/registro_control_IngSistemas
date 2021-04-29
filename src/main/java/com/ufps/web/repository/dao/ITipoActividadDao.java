package com.ufps.web.repository.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ufps.web.entities.TipoActividad;

@Repository
public interface ITipoActividadDao extends JpaRepository<TipoActividad, Integer> {
	
	public TipoActividad findByNombre(String nombre);

}
