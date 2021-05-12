package com.ufps.web.repository.dao;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ufps.web.entities.Usuario;

@Repository
public interface IUsuarioDao extends CrudRepository<Usuario, String>{
	public Usuario findByCodigo(String codigo);
	
	@Query("select u from Usuario u where u.rol.nombre = 'ROLE_DOCENTE'")
	public List<Usuario> findAllDocentes();
	
	@Query("select u from Usuario u where u.email =?1")
	public Usuario findByEmail(String email);
	
	public Usuario findByResetPasswordToken(String token);
	
}
