package com.ufps.web.auth.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ufps.web.entities.Usuario;
import com.ufps.web.repository.dao.IUsuarioDao;

@Service("userDetailService")
public class UserDetailService implements UserDetailsService {
	
	@Autowired
	private IUsuarioDao usuarioDao;
	
	private Logger logger = LoggerFactory.getLogger(UserDetailService.class);

	@Override
	@Transactional(readOnly=true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		
//		se utiliza el parametro llamado username, ya que Spring maneja automatico este nombre
		Usuario usuario = usuarioDao.findByCodigo(username);
		logger.info("CODIGO USER" + username);
		
		if (usuario == null) {
			throw new UsernameNotFoundException("Usuario: " + username + "No existe en el sistema");
		}
		
		List<GrantedAuthority> rol = new ArrayList<GrantedAuthority>();
        rol.add(new SimpleGrantedAuthority(usuario.getRol().getNombre()));
        
        if(rol.isEmpty()) {
        	throw new UsernameNotFoundException("Error en el Login: usuario '" + username + "' no tiene roles asignados!");
        }
        
        if(usuario.getEstado().equalsIgnoreCase("inactivo")) {
        	throw new UsernameNotFoundException("Error en el Login: usuario '" + username + "' esta desactivado!, comuniquese con el administrador del sistema");
        }
        
		return new User(usuario.getCodigo(),usuario.getPassword(),rol);
	}

	
}
