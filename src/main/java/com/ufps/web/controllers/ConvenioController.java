package com.ufps.web.controllers;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ufps.web.auth.service.IDocumentFileService;
import com.ufps.web.entities.Convenio;
import com.ufps.web.entities.Evidencia;
import com.ufps.web.repository.dao.IConvenioDao;
import com.ufps.web.repository.dao.IEvidenciaDao;

@Secured("ROLE_ADMIN")
@Controller
@SessionAttributes("convenio")
public class ConvenioController {

	@Autowired
	IDocumentFileService documentService;

	@Autowired
	IConvenioDao convenioDao;
	
	@Autowired
	IEvidenciaDao evidenciaDao;

	@GetMapping("convenio")
	public String addConvenio(Model model) {
		model.addAttribute("titulo", "Registrar Convenio");
		model.addAttribute("convenio", new Convenio());
		return "convenio";
	}

	@GetMapping({"listar-convenios","convenios-filtrados/{filtradas}"})
	public String listarConvenios(Model model, @PathVariable(required = false) String filtradas) {
		model.addAttribute("titulo", "Listado de convenios");
		if (filtradas == null) {
			List<Convenio> convenios = convenioDao.findAll();
			model.addAttribute("convenios", convenios);
		}
		return "listaConvenios";
	}

	@PostMapping("add-convenio")
	public String registrarConvenio(@Valid Convenio convenio, BindingResult result,
			@RequestParam("archivo") MultipartFile archivo, Model model, SessionStatus status,
			Authentication auth) {

		if (result.hasErrors()) {
			model.addAttribute("titulo", "Registrar convenio");
			model.addAttribute("convenio", convenio);
			return "convenio";
		}

//		evidencia	
		String uniqueFilename = null;
		if (!archivo.isEmpty()) {
			try {
				uniqueFilename = documentService.copy(archivo);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Evidencia e = new Evidencia();
		e.setFecha(new Date());
		e.setUrl(uniqueFilename);
		ArrayList<Evidencia> ev = new ArrayList<>();
		ev.add(e);
		
		Convenio c = convenio;
		c.setEvidencias(ev);
		c.setFecha(new Date());
		convenioDao.save(c);
		e.setConvenio(c);
		evidenciaDao.save(e);
		
		status.setComplete();
		model.addAttribute("success", "Convenio creado exitosamente!");

		return "convenio";
	}
	
	@RequestMapping("/download/{filename}")
	 @ResponseBody
	 public void show(@PathVariable String filename, HttpServletResponse response) {
		 
		 response.setContentType("application/pdf");
		 response.setHeader("Content-Transfer-Encoding", "quoted-printable");
		 
		 try {
			 BufferedOutputStream bos= new BufferedOutputStream(response.getOutputStream());
			 FileInputStream file =new FileInputStream(this.documentService.getPath(filename).toString());
			 byte [] buf=new byte[1024];
			 int len;
			 while((len=file.read(buf))>0) {
				 bos.write(buf, 0, len);
			 }
			 
			 bos.close();
			 file.close();
			 response.flushBuffer();
			
		} catch (Exception e) {
			System.out.println("Ha ocurrido un error");
			e.printStackTrace();
		}
	 }
	
	@PostMapping("filtrar-convenios")
	public String listarConvenios( @RequestParam  @DateTimeFormat(pattern = "yyyy-MM-dd") Date desde,
	        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date hasta, RedirectAttributes flash){
	
	try {
		flash.addFlashAttribute("convenios", convenioDao.filtrarConvenios(desde, hasta));
		return  "redirect:/convenios-filtrados/1";
	} catch (Exception e) {
		// TODO: handle exception
		flash.addFlashAttribute("error", e.getMessage());
	}
	
	return "listaConvenios";	
}
}
