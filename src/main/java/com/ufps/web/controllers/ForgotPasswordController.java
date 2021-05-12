package com.ufps.web.controllers;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ufps.web.entities.Usuario;
import com.ufps.web.repository.dao.IUsuarioDao;
import com.ufps.web.services.UsuarioServices;
import com.ufps.web.util.Utility;

import net.bytebuddy.utility.RandomString;

@Controller
public class ForgotPasswordController {

	@Autowired
    private JavaMailSender mailSender;
	
	@Autowired
	private UsuarioServices usuarioService;
	
	@Autowired
	private IUsuarioDao usuarioDao;
	
	
	 @GetMapping("/forgot_password")
	    public String showForgotPasswordForm() {
		 return "forgotPassword";
	    }
	 
	 @PostMapping("/forgot_password")
	 public String processForgotPassword(HttpServletRequest request, Model model) {
	     String codigo = request.getParameter("codigo");
	     String token = RandomString.make(30);
	     
	      
	     try {
	         usuarioService.updateResetPasswordToken(token, codigo);
	         String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token=" + token;
	         Usuario u = usuarioDao.findByCodigo(codigo);
	         sendEmail(u.getEmail(), resetPasswordLink);
	         model.addAttribute("message", "Te hemos enviado un link al correo asociado al codigo para recuperar la contraseña. Por favor revisa.");
	         
	     } catch (UsernameNotFoundException ex) {
	         model.addAttribute("error", ex.getMessage());
	     } catch (UnsupportedEncodingException | MessagingException e) {
	         model.addAttribute("error", "Error mientras se enviaba el email");
	     }
	          
	     return "forgotPassword";
	 }
	     
	    public void sendEmail(String recipientEmail, String link)
	    	throws MessagingException, UnsupportedEncodingException {
	    	    MimeMessage message = mailSender.createMimeMessage();              
	    	    MimeMessageHelper helper = new MimeMessageHelper(message);
	    	     
	    	    helper.setFrom("contact@ufps.com", "Registro y control Ingeniería de sistemas UFPS");
	    	    helper.setTo(recipientEmail);
	    	     
	    	    String subject = "Recuperar contraseña";
	    	     
	    	    String content= "  <!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\r\n" + 
	    	    		"<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" style=\"width:100%;font-family:helvetica, 'helvetica neue', arial, verdana, sans-serif;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%;padding:0;Margin:0\">\r\n" + 
	    	    		" <head> \r\n" + 
	    	    		"  <meta charset=\"UTF-8\"> \r\n" + 
	    	    		"  <meta content=\"width=device-width, initial-scale=1\" name=\"viewport\"> \r\n" + 
	    	    		"  <meta name=\"x-apple-disable-message-reformatting\"> \r\n" + 
	    	    		"  <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"> \r\n" + 
	    	    		"  <meta content=\"telephone=no\" name=\"format-detection\"> \r\n" + 
	    	    		"  <style type=\"text/css\">\r\n" + 
	    	    		"#outlook a {\r\n" + 
	    	    		"	padding:0;\r\n" + 
	    	    		"}\r\n" + 
	    	    		".ExternalClass {\r\n" + 
	    	    		"	width:100%;\r\n" + 
	    	    		"}\r\n" + 
	    	    		".ExternalClass,\r\n" + 
	    	    		".ExternalClass p,\r\n" + 
	    	    		".ExternalClass span,\r\n" + 
	    	    		".ExternalClass font,\r\n" + 
	    	    		".ExternalClass td,\r\n" + 
	    	    		".ExternalClass div {\r\n" + 
	    	    		"	line-height:100%;\r\n" + 
	    	    		"}\r\n" + 
	    	    		".es-button {\r\n" + 
	    	    		"	mso-style-priority:100!important;\r\n" + 
	    	    		"	text-decoration:none!important;\r\n" + 
	    	    		"}\r\n" + 
	    	    		"a[x-apple-data-detectors] {\r\n" + 
	    	    		"	color:inherit!important;\r\n" + 
	    	    		"	text-decoration:none!important;\r\n" + 
	    	    		"	font-size:inherit!important;\r\n" + 
	    	    		"	font-family:inherit!important;\r\n" + 
	    	    		"	font-weight:inherit!important;\r\n" + 
	    	    		"	line-height:inherit!important;\r\n" + 
	    	    		"}\r\n" + 
	    	    		".es-desk-hidden {\r\n" + 
	    	    		"	display:none;\r\n" + 
	    	    		"	float:left;\r\n" + 
	    	    		"	overflow:hidden;\r\n" + 
	    	    		"	width:0;\r\n" + 
	    	    		"	max-height:0;\r\n" + 
	    	    		"	line-height:0;\r\n" + 
	    	    		"	mso-hide:all;\r\n" + 
	    	    		"}\r\n" + 
	    	    		".es-button-border:hover a.es-button, .es-button-border:hover button.es-button {\r\n" + 
	    	    		"	background:#ffffff!important;\r\n" + 
	    	    		"	border-color:#ffffff!important;\r\n" + 
	    	    		"}\r\n" + 
	    	    		".es-button-border:hover {\r\n" + 
	    	    		"	background:#ffffff!important;\r\n" + 
	    	    		"	border-style:solid solid solid solid!important;\r\n" + 
	    	    		"	border-color:#3d5ca3 #3d5ca3 #3d5ca3 #3d5ca3!important;\r\n" + 
	    	    		"}\r\n" + 
	    	    		"[data-ogsb] .es-button {\r\n" + 
	    	    		"	border-width:0!important;\r\n" + 
	    	    		"	padding:15px 20px 15px 20px!important;\r\n" + 
	    	    		"}\r\n" + 
	    	    		"@media only screen and (max-width:600px) {p, ul li, ol li, a { line-height:150%!important } h1 { font-size:20px!important; text-align:center; line-height:120%!important } h2 { font-size:16px!important; text-align:left; line-height:120%!important } h3 { font-size:20px!important; text-align:center; line-height:120%!important } .es-header-body h1 a, .es-content-body h1 a, .es-footer-body h1 a { font-size:20px!important } h2 a { text-align:left } .es-header-body h2 a, .es-content-body h2 a, .es-footer-body h2 a { font-size:16px!important } .es-header-body h3 a, .es-content-body h3 a, .es-footer-body h3 a { font-size:20px!important } .es-menu td a { font-size:14px!important } .es-header-body p, .es-header-body ul li, .es-header-body ol li, .es-header-body a { font-size:10px!important } .es-content-body p, .es-content-body ul li, .es-content-body ol li, .es-content-body a { font-size:16px!important } .es-footer-body p, .es-footer-body ul li, .es-footer-body ol li, .es-footer-body a { font-size:12px!important } .es-infoblock p, .es-infoblock ul li, .es-infoblock ol li, .es-infoblock a { font-size:12px!important } *[class=\"gmail-fix\"] { display:none!important } .es-m-txt-c, .es-m-txt-c h1, .es-m-txt-c h2, .es-m-txt-c h3 { text-align:center!important } .es-m-txt-r, .es-m-txt-r h1, .es-m-txt-r h2, .es-m-txt-r h3 { text-align:right!important } .es-m-txt-l, .es-m-txt-l h1, .es-m-txt-l h2, .es-m-txt-l h3 { text-align:left!important } .es-m-txt-r img, .es-m-txt-c img, .es-m-txt-l img { display:inline!important } .es-button-border { display:block!important } a.es-button, button.es-button { font-size:14px!important; display:block!important; border-left-width:0px!important; border-right-width:0px!important } .es-btn-fw { border-width:10px 0px!important; text-align:center!important } .es-adaptive table, .es-btn-fw, .es-btn-fw-brdr, .es-left, .es-right { width:100%!important } .es-content table, .es-header table, .es-footer table, .es-content, .es-footer, .es-header { width:100%!important; max-width:600px!important } .es-adapt-td { display:block!important; width:100%!important } .adapt-img { width:100%!important; height:auto!important } .es-m-p0 { padding:0px!important } .es-m-p0r { padding-right:0px!important } .es-m-p0l { padding-left:0px!important } .es-m-p0t { padding-top:0px!important } .es-m-p0b { padding-bottom:0!important } .es-m-p20b { padding-bottom:20px!important } .es-mobile-hidden, .es-hidden { display:none!important } tr.es-desk-hidden, td.es-desk-hidden, table.es-desk-hidden { width:auto!important; overflow:visible!important; float:none!important; max-height:inherit!important; line-height:inherit!important } tr.es-desk-hidden { display:table-row!important } table.es-desk-hidden { display:table!important } td.es-desk-menu-hidden { display:table-cell!important } .es-menu td { width:1%!important } table.es-table-not-adapt, .esd-block-html table { width:auto!important } table.es-social { display:inline-block!important } table.es-social td { display:inline-block!important } }\r\n" + 
	    	    		"</style> \r\n" + 
	    	    		" </head> \r\n" + 
	    	    		" <body style=\"width:100%;font-family:helvetica, 'helvetica neue', arial, verdana, sans-serif;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%;padding:0;Margin:0\"> \r\n" + 
	    	    		"  <div class=\"es-wrapper-color\" style=\"background-color:#FAFAFA\">            \r\n" + 
	    	    		"       <table class=\"es-content\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%\"> \r\n" + 
	    	    		"         <tr style=\"border-collapse:collapse\"> \r\n" + 
	    	    		"          <td style=\"padding:0;Margin:0;background-color:#FAFAFA\" bgcolor=\"#fafafa\" align=\"center\"> \r\n" + 
	    	    		"           <table class=\"es-content-body\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:#FFFFFF;width:600px\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#ffffff\" align=\"center\"> \r\n" + 
	    	    		"             <tr style=\"border-collapse:collapse\"> \r\n" + 
	    	    		"              <td style=\"padding:0;Margin:0;padding-left:20px;padding-right:20px;padding-top:40px;background-color:transparent;background-position:left top\" bgcolor=\"transparent\" align=\"left\"> \r\n" + 
	    	    		"               <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"> \r\n" + 
	    	    		"                 <tr style=\"border-collapse:collapse\"> \r\n" + 
	    	    		"                  <td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;width:560px\"> \r\n" + 
	    	    		"                   <table style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-position:left top\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" role=\"presentation\"> \r\n" + 
	    	    		"                     <tr style=\"border-collapse:collapse\"> \r\n" + 
	    	    		"                      <td align=\"center\" style=\"padding:0;Margin:0;padding-top:5px;padding-bottom:5px;font-size:0\"><img src=\"https://roecjf.stripocdn.email/content/guids/CABINET_dd354a98a803b60e2f0411e893c82f56/images/23891556799905703.png\" alt style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic\" width=\"175\" height=\"208\"></td> \r\n" + 
	    	    		"                     </tr> \r\n" + 
	    	    		"                     <tr style=\"border-collapse:collapse\"> \r\n" + 
	    	    		"                      <td align=\"center\" style=\"padding:0;Margin:0;padding-top:15px;padding-bottom:15px\"><h1 style=\"Margin:0;line-height:24px;mso-line-height-rule:exactly;font-family:arial, 'helvetica neue', helvetica, sans-serif;font-size:20px;font-style:normal;font-weight:normal;color:#333333\"><strong>OLVIDASTE TU </strong></h1><h1 style=\"Margin:0;line-height:24px;mso-line-height-rule:exactly;font-family:arial, 'helvetica neue', helvetica, sans-serif;font-size:20px;font-style:normal;font-weight:normal;color:#333333\"><strong>&nbsp;CONTRASEÑA?</strong></h1></td> \r\n" + 
	    	    		"                     </tr>  \r\n" + 
	    	    		"                     <tr style=\"border-collapse:collapse\"> \r\n" + 
	    	    		"                      <td align=\"left\" style=\"padding:0;Margin:0;padding-right:35px;padding-left:40px\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:helvetica, 'helvetica neue', arial, verdana, sans-serif;line-height:24px;color:#666666;font-size:16px;text-align:center\">Esta es una petición para cambiar tu contraseña!</p></td> \r\n" + 
	    	    		"                     </tr> \r\n" + 
	    	    		"                     <tr style=\"border-collapse:collapse\"> \r\n" + 
	    	    		"                      <td align=\"center\" style=\"padding:0;Margin:0;padding-top:25px;padding-left:40px;padding-right:40px\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:helvetica, 'helvetica neue', arial, verdana, sans-serif;line-height:24px;color:#666666;font-size:16px\">Si tu no hiciste esta petición, ignora este mensaje. Sino, por favor dale click al boton para cambiar la contraseña:</p></td> \r\n" + 
	    	    		"                     </tr> \r\n" + 
	    	    		"                     <tr style=\"border-collapse:collapse\"> \r\n" + 
	    	    		"                      <td align=\"center\" style=\"Margin:0;padding-left:10px;padding-right:10px;padding-top:40px;padding-bottom:40px\"><span class=\"es-button-border\" style=\"border-style:solid;border-color:#D42B17;background:#FFFFFF;border-width:2px;display:inline-block;border-radius:10px;width:auto\"><a href=\"" + link + "\" class=\"es-button\" target=\"_blank\" style=\"mso-style-priority:100 !important;text-decoration:none;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;color:#D42B17;font-size:14px;border-style:solid;border-color:#FFFFFF;border-width:15px 20px 15px 20px;display:inline-block;background:#FFFFFF;border-radius:10px;font-family:arial, 'helvetica neue', helvetica, sans-serif;font-weight:bold;font-style:normal;line-height:17px;width:auto;text-align:center\">Cambiar contraseña</a></span></td> \r\n" + 
	    	    		"                     </tr> \r\n" + 
	    	    		"                   </table></td> \r\n" + 
	    	    		"                 </tr> \r\n" + 
	    	    		"               </table></td> \r\n" + 
	    	    		"             </tr> \r\n" + 
	    	    		"             <tr style=\"border-collapse:collapse\"> \r\n" + 
	    	    		"              <td style=\"Margin:0;padding-top:5px;padding-bottom:20px;padding-left:20px;padding-right:20px;background-position:left top\" align=\"left\"> \r\n" + 
	    	    		"               <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"> \r\n" + 
	    	    		"                 <tr style=\"border-collapse:collapse\"> \r\n" + 
	    	    		"                  <td valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;width:560px\"> \r\n" + 
	    	    		"                   <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" role=\"presentation\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\"> \r\n" + 
	    	    		"                     <tr style=\"border-collapse:collapse\"> \r\n" + 
	    	    		"                      <td align=\"center\" style=\"padding:0;Margin:0\"><p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:helvetica, 'helvetica neue', arial, verdana, sans-serif;line-height:21px;color:#666666;font-size:14px\">Contactenos <a target=\"_blank\" style=\"-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;text-decoration:none;color:#666666;font-size:14px\" href=\"tel:123456789\">575-2381</a> | <a target=\"_blank\" href=\"mailto:your@mail.com\" style=\"-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;text-decoration:none;color:#666666;font-size:14px\">contact@ufps.edu.co</a></p></td> \r\n" + 
	    	    		"                     </tr> \r\n" + 
	    	    		"                   </table></td> \r\n" + 
	    	    		"                 </tr> \r\n" + 
	    	    		"               </table></td> \r\n" + 
	    	    		"             </tr> \r\n" + 
	    	    		"           </table></td> \r\n" + 
	    	    		"         </tr> \r\n" + 
	    	    		"       </table> \r\n" + 
	    	    		"     </tr> \r\n" + 
	    	    		"   </table> \r\n" + 
	    	    		"  </div>  \r\n" + 
	    	    		" </body>\r\n" + 
	    	    		"</html>";
	    	     
	    	    helper.setSubject(subject);
	    	     
	    	    helper.setText(content, true);
	    	     
	    	    mailSender.send(message);
	    }  
	     
	     
	    @GetMapping("/reset_password")
	    public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
	        Usuario usuario = usuarioService.getByResetPasswordToken(token);
	        model.addAttribute("token", token);
	         
	        if (usuario == null) {
	            model.addAttribute("message", "Invalid Token");
	            return "resetPassword";
	        }
	         
	        return "resetPassword";
	    }
	     
	    @PostMapping("/reset_password")
	    public String processResetPassword(@RequestParam("password") String pass, @RequestParam("passwordRepeat") String passRepeat,
	    		@RequestParam("token")String token,Model model, RedirectAttributes flash) {
	    	
	        if(!pass.equals(passRepeat)) {
	        	model.addAttribute("token", token);
	        	model.addAttribute("error", "Contraseñas no coinciden!");
	        	return "resetPassword";
	        }
	         
	        Usuario usuario = usuarioService.getByResetPasswordToken(token);
	         
	        if (usuario == null) {
	            model.addAttribute("message", "Invalid Token");
	            return "resetPassword";
	        } else {           
	            usuarioService.updatePassword(usuario, pass);
	             
	            flash.addAttribute("success", "Contraseña cambiada con exito!");
	        }
	         
	        return "redirect:/login";
	    }
}
