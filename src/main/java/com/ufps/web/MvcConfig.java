package com.ufps.web;


import java.nio.file.Paths;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer{
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// TODO Auto-generated method stub
//		super.addResourceHandlers(registry);
		
		String resourcePath = Paths.get("evidencias").toAbsolutePath().toUri().toString();
		String resource = Paths.get("documentos").toAbsolutePath().toUri().toString();
		
		registry.addResourceHandler("/evidencias/**")
		.addResourceLocations(resourcePath);
		
		registry.addResourceHandler("/documentos/**")
		.addResourceLocations(resource);
		
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
