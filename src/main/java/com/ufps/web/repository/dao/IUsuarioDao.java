package com.ufps.web.repository.dao;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ufps.web.entities.Usuario;

@Repository
public interface IUsuarioDao extends CrudRepository<Usuario, String>{
	public Usuario findByCodigo(String codigo);
}
