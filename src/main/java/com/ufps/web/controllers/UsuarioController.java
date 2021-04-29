package com.ufps.web.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ufps.web.entities.Actividad;
import com.ufps.web.repository.dao.IActividadDao;
import com.ufps.web.repository.dao.IUsuarioDao;


@Controller
public class UsuarioController {
	
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	IActividadDao actividadDao;
	
	@Autowired
	IUsuarioDao usuarioDao;
	
	@Secured({"ROLE_ADMIN", "ROLE_DOCENTE"})
	@GetMapping({"/","/listar/{filtradas}"})
	public String inicio(Model model,  @PathVariable(required=false) String filtradas) {
		model.addAttribute("titulo", "Listado de actividades");
		List<Actividad> actividades = actividadDao.findAll();
		if(filtradas==null) {
		model.addAttribute("actividades", actividades);
		}
		return "dashboard";
	}
	
	@GetMapping(value="/login")
	public String login(@RequestParam(value="error", required=false) String error,
			@RequestParam(value="logout", required = false) String logout,
			Model model, Principal principal, RedirectAttributes flash) {

		if(principal != null) {
			return "redirect:/";
		}	
		
		if(error != null) {
			model.addAttribute("error","Nombre de usuario o contraseña incorrecta!");
		}
		
		if(logout != null) {
			model.addAttribute("success", "Ha cerrado sesión con éxito!");
		}
		
		return "login";
	}
	
	@GetMapping(value="/profile")
	public String profile(Model model,Principal principal) {
		model.addAttribute("titulo", "Perfil del usuario");
		model.addAttribute("usuario", usuarioDao.findByCodigo(principal.getName()));
		return "profile";
	}
	
	@GetMapping(value="/docentes")
	public String docentes(Map<String, Object> model) {
		model.put("titulo", "Listado de docentes");
		return "docente";
	}
	
}
