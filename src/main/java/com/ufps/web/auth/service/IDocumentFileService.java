package com.ufps.web.auth.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IDocumentFileService {

	public Resource load(String filename) throws MalformedURLException;

	public String copy(MultipartFile file) throws IOException;
	
	public Path getPath(String filename);

	public boolean delete(String filename);

	public void deleteAll();

	public void init() throws IOException;
}
