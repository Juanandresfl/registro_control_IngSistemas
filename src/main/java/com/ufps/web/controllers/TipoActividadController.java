package com.ufps.web.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ufps.web.entities.TipoActividad;
import com.ufps.web.repository.dao.ITipoActividadDao;

@Controller
@RequestMapping("/tipoActividad")
public class TipoActividadController {
	
	@Autowired
	private ITipoActividadDao tipoActividadDao;

	@GetMapping("")
	public String tipoActividad(Model model) {
		model.addAttribute("titulo","Tipo de actividad");
		model.addAttribute("tipoActividad", new TipoActividad());
		return "tipoActividad";
	}
	
	@PostMapping("/add-tipo")
	public String tipoActividad(@Valid TipoActividad tipo, BindingResult result, RedirectAttributes flash ) {
		if (result.hasErrors()) {
			flash.addFlashAttribute("titulo", "Registrar tipo de actividad");
			flash.addFlashAttribute("tipo", tipo);
			return "tipoActividad";
		}
		
		tipoActividadDao.save(tipo);
		flash.addFlashAttribute("success","tipo de actividad registrada exitosamente!");
		return "redirect:/";
	}
}
