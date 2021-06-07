package com.ufps.web.repository.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ufps.web.entities.Convenio;

@Repository
public interface IConvenioDao extends JpaRepository<Convenio, Integer>{
	
	@Query("select c from Convenio c where c.numConvenio=?1")
	public Convenio findByNumConvenio(String convenio);
	
	@Query("select c from Convenio c where c.fecha between ?1 and ?2")
	public List<Convenio> filtrarConvenios(Date desde,Date hasta);

}
