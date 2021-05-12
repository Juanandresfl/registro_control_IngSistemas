package com.ufps.web.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ufps.web.auth.service.IUploadFileService;
import com.ufps.web.entities.Actividad;
import com.ufps.web.entities.Convenio;
import com.ufps.web.entities.Evidencia;
import com.ufps.web.entities.TipoActividad;
import com.ufps.web.repository.dao.IActividadDao;
import com.ufps.web.repository.dao.IConvenioDao;
import com.ufps.web.repository.dao.IEvidenciaDao;
import com.ufps.web.repository.dao.ITipoActividadDao;
import com.ufps.web.repository.dao.IUsuarioDao;

@Controller
@RequestMapping("/actividad")
@SessionAttributes("actividad")
public class ActividadController {

	@Autowired
	IActividadDao actividadDao;

	@Autowired
	IUsuarioDao usuarioDao;

	@Autowired
	IConvenioDao convenioDao;

	@Autowired
	ITipoActividadDao tipoActividadDao;

	@Autowired
	IEvidenciaDao evidenciaDao;

	@Autowired
	IUploadFileService uploadService;
	
	@Secured({"ROLE_ADMIN", "ROLE_DOCENTE"})
	@GetMapping("/")
	public String actividad(Model model) {
		model.addAttribute("actividad", new Actividad());
		return "actividad";
	}

	@Secured({"ROLE_ADMIN", "ROLE_DOCENTE"})
	@GetMapping("/{id}")
	public String evidencias(@PathVariable(value = "id") int id,Model model,RedirectAttributes flash) {
		if(actividadDao.findById(id).isEmpty()) {
			System.out.println("ENTRO ACA");
			flash.addFlashAttribute("error","Actividad buscada inexistente");
			return "redirect:/";
		}
		model.addAttribute("actividad", actividadDao.findById(id).orElse(null));
		model.addAttribute("evidencias",evidenciaDao.findByActividad(id));
		model.addAttribute("titulo", " Evidencias de la actividad");
		return "/evidencias";
	}
	
	@Secured({"ROLE_ADMIN", "ROLE_DOCENTE"})
	@PostMapping("/filtrar")
	public String listarActividades( @RequestParam  @DateTimeFormat(pattern = "yyyy-MM-dd") Date desde,
		        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date hasta, RedirectAttributes flash){
		
		flash.addFlashAttribute("actividades", actividadDao.filtrarActividades(desde, hasta));
		
		for (Actividad a : actividadDao.filtrarActividades(desde, hasta)) {
			
			System.out.println("ACTIVIDADES : "+ a.getIdActividad());
		}
		
			
		return  "redirect:/listar/1";
	}

	@PostMapping("/")
	public String registrarActividad(@Valid Actividad actividad, BindingResult result, @RequestParam String tipo,
			@RequestParam String convenio, @RequestParam("evidencia") MultipartFile evidencia, Model model,
			RedirectAttributes flash, SessionStatus status, Authentication auth) {

		if (result.hasErrors()) {
			model.addAttribute("titulo", "Registrar actividad");
			model.addAttribute("actividad", actividad);
			return "actividad";
		}

//			evidencia	
		String uniqueFilename = null;
		if (!evidencia.isEmpty()) {
			try {
				uniqueFilename = uploadService.copy(evidencia);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Evidencia e = new Evidencia();
		e.setFecha(new Date());
		e.setUrl(uniqueFilename);
		ArrayList<Evidencia> ev = new ArrayList<>();

		Convenio c = new Convenio();
		if (convenioDao.findByNumConvenio(convenio) == null) {
			model.addAttribute("error", "Numero de convenio inexistente");
			model.addAttribute("actividad", actividad);
			return "actividad";
		}

		c = convenioDao.findByNumConvenio(convenio);

		TipoActividad ta = new TipoActividad();

		if (tipoActividadDao.findByNombre(tipo) == null) {

			model.addAttribute("error", "Tipo de actividad inexistente");
			model.addAttribute("actividad", actividad);
			return "actividad";
		}

		ta = tipoActividadDao.findByNombre(tipo);

		ev.add(e);
		Actividad activity = actividad;
		activity.setFecha(new Date());
		activity.setUsuario(usuarioDao.findByCodigo(auth.getName()));
		activity.setEvidencias(ev);
		activity.setTipoActividad(ta);
		activity.setConvenio(c);
		activity.setEvidencias(ev);

//			model.addAttribute("actividad",actividad);
		e.setActividad(activity);
		actividadDao.save(activity);
		evidenciaDao.save(e);
		status.setComplete();
		flash.addFlashAttribute("success", "Actividad creada exitosamente!");

		return "redirect:/";
	}
}
