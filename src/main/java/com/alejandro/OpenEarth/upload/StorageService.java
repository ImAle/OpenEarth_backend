package com.alejandro.OpenEarth.upload;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

	void init();
	String store(MultipartFile file, Long id);
	Stream<Path> loadAll();
	Path load(String filename);
	Resource loadAsResource(String filename);
	void deleteAll();
	void delete(String filename) throws IOException;
}
