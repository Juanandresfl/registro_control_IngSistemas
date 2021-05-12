package com.ufps.web.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ufps.web.entities.Usuario;
import com.ufps.web.repository.dao.IUsuarioDao;

@Service
@Transactional
public class UsuarioServices {

	@Autowired
	private IUsuarioDao usuarioDao;
	
	public void updateResetPasswordToken(String token, String codigo) throws UsernameNotFoundException {
        Usuario usuario = usuarioDao.findByCodigo(codigo);
        if (usuario != null) {
            usuario.setResetPasswordToken(token);
            usuarioDao.save(usuario);
        } else {
            throw new UsernameNotFoundException("La consulta no encontro ningun usuario con el codigo: " + codigo);
        }
    }
	
	public Usuario getByResetPasswordToken(String token) {
		return usuarioDao.findByResetPasswordToken(token);
	}
	
	public void updatePassword(Usuario usuario, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        usuario.setPassword(encodedPassword);
         
        usuario.setResetPasswordToken(null);
        usuarioDao.save(usuario);
    }
}
