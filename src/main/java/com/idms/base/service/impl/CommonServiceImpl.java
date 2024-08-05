package com.idms.base.service.impl;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import com.idms.base.dao.entity.Document;
import com.idms.base.dao.repository.DocumentRepository;
import com.idms.base.service.CommonService;

@Service
public class CommonServiceImpl implements CommonService {
	
	@Autowired
	DocumentRepository docRepo;

	@Override
	public Resource download(Integer id) {
		Resource resource;
		Document doc = docRepo.getOne(id);
		Path file = Paths.get(doc.getDocumentPath());
		try {
			resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		} catch (Exception e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}

}
