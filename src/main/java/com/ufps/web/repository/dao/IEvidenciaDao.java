package com.ufps.web.repository.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ufps.web.entities.Evidencia;

@Repository
public interface IEvidenciaDao extends JpaRepository<Evidencia, Integer>{
	
	@Query("select e from Evidencia e where e.actividad.idActividad = ?1")
	public List<Evidencia> findByActividad(int actividad);

}
