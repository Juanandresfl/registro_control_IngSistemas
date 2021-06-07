package com.ufps.web.repository.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ufps.web.entities.Actividad;

@Repository
public interface IActividadDao extends JpaRepository<Actividad, Integer> {

	@Query("select a from Actividad a where a.fecha between ?1 and ?2")
	public List<Actividad> filtrarActividades(Date desde,Date hasta);
	
	@Query("select a from Actividad a where a.usuario.codigo =?1")
	public List<Actividad>actividadesDocente(String codigo);
}
