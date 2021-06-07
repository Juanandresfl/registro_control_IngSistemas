package com.ufps.web.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ufps.web.entities.Actividad;
import com.ufps.web.entities.Rol;
import com.ufps.web.entities.Usuario;
import com.ufps.web.repository.dao.IActividadDao;
import com.ufps.web.repository.dao.IUsuarioDao;

@Controller
public class UsuarioController {

	protected final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private IActividadDao actividadDao;

	@Autowired
	private IUsuarioDao usuarioDao;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Secured({ "ROLE_ADMIN", "ROLE_DOCENTE" })
	@GetMapping({ "/", "/listar/{filtradas}" })
	public String inicio(Model model, @PathVariable(required = false) String filtradas, Principal principal) {
		model.addAttribute("titulo", "Listado de actividades");
		Usuario u = usuarioDao.findByCodigo(principal.getName());
		List<Actividad> actividades = new ArrayList<>();
		
		if (u.getRol().getNombre().equals("ROLE_ADMIN")) {
              actividades = actividadDao.findAll();
		}
		if(u.getRol().getNombre().equals("ROLE_DOCENTE")) {
			 actividades = actividadDao.actividadesDocente(principal.getName());
		}
		if (filtradas == null) {
			model.addAttribute("actividades", actividades);
		}
		return "dashboard";
	}

	@GetMapping(value = "/login")
	public String login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout, Model model, Principal principal,
			RedirectAttributes flash) {

		if (principal != null) {
			return "redirect:/";
		}

		if (error != null) {
			model.addAttribute("error", "Nombre de usuario o contraseña incorrecta! "
					+ "(Si esta correcto todo comuniquese con el administrador del sistema)");
		}

		if (logout != null) {
			model.addAttribute("success", "Ha cerrado sesión con éxito!");
		}

		return "login";
	}

	@Secured("ROLE_ADMIN")
	@PostMapping("/registrar-docente")
	public String registrarDocente(@Valid Usuario usuario, BindingResult result, Model model, RedirectAttributes flash,
			SessionStatus status, @RequestParam String pass) {

		if (result.hasErrors()) {
			model.addAttribute("titulo", "Registrar Docente");
			model.addAttribute("usuario", usuario);
			return "registrarDocente";
		}

		if (!usuario.getPassword().equalsIgnoreCase(pass)) {
			System.out.println("ENTRO ACA");
			model.addAttribute("error", "Contraseñas no coinciden");
			model.addAttribute("usuario", usuario);
			return "registrarDocente";
		}

		Rol rol = new Rol();
		rol.setIdRol(2);

		usuario.setEstado("activo");
		usuario.setRol(rol);
		String bcryptPassword = passwordEncoder.encode(usuario.getPassword());
		usuario.setPassword(bcryptPassword);
		usuarioDao.save(usuario);

		status.setComplete();
		model.addAttribute("success", "Docente registrado con exito!");

		return "registrarDocente";
	}

	@Secured("ROLE_ADMIN")
	@PostMapping("/update-state")
	public String updateState(@RequestParam String codigo, Model model) {

		Usuario u = usuarioDao.findByCodigo(codigo);

		if (u.getEstado().equalsIgnoreCase("activo")) {
			u.setEstado("inactivo");
		} else {
			u.setEstado("activo");
		}

		usuarioDao.save(u);

		return "redirect:/docentes";
	}

	@PostMapping("actualizar-perfil")
	public String actualizarPerfil(@Valid Usuario usuario, BindingResult result, Model flash) {
		if (result.hasErrors()) {
			flash.addAttribute("titulo", "Perfil del usuario");
			return "profile";
		}

		Usuario u = usuarioDao.findByCodigo(usuario.getCodigo());
		usuario.setRol(u.getRol());
		usuarioDao.save(usuario);
		System.out.println("LLEGO ACA");
		flash.addAttribute("success", "Perfil actualizado con exito!");
		return "profile";
	}

	@GetMapping(value = "/perfil")
	public String profile(Model model, Principal principal) {
		model.addAttribute("titulo", "Perfil del usuario");
		model.addAttribute("usuario", usuarioDao.findByCodigo(principal.getName()));
		return "profile";
	}

	@Secured("ROLE_ADMIN")
	@GetMapping(value = "/docentes")
	public String docentes(Model model) {
		model.addAttribute("docentes", usuarioDao.findAllDocentes());
		model.addAttribute("titulo", "Listado de docentes");
		return "docente";
	}

	@GetMapping(value = "/registrar-docente")
	public String addDocente(Model model) {
		model.addAttribute("usuario", new Usuario());
		model.addAttribute("titulo", "Registrar docente");
		return "registrarDocente";
	}

}
