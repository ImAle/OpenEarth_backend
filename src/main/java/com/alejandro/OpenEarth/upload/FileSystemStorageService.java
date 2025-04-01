package com.alejandro.OpenEarth.upload;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service("fileService")
public class FileSystemStorageService implements StorageService{

	private final Path location;
	
	public FileSystemStorageService(StorageProperties properties) {
		
		if(properties.getLocation().trim().isEmpty())
			throw new StorageException("Upload directory location cannot be empty.");
		
		this.location = Paths.get(properties.getLocation());
	}
	
	
	@Override
	public void init() {
		try {
			Files.createDirectories(location);
		} catch (Exception e) {
			throw new StorageException("Uploading could not be initialized:", e);
		}
		
	}

	@Override
	public String store(MultipartFile file, Long idFile) {
		
		try {
			// It checks if file is empty
			if(file.isEmpty())
				throw new StorageException("The file is empty");

			// Resolve file's location
			Path allocation = this.location.resolve(Paths.get(file.getOriginalFilename()))
					.normalize().toAbsolutePath();

			// It checks the file is not been uploading out of the directory
			if (!allocation.getParent().equals(this.location.toAbsolutePath())) {
			    throw new StorageException("The picture can not be upload out of the directory.");
			}

			// It gets the original name of the file as well as its extension
			String originalFile = file.getOriginalFilename();
		        if (originalFile == null || !originalFile.contains(".")) {
		            throw new StorageException("Can not resolve file's extension.");
		        }
			// Get file's extension
			String extension = originalFile.substring(originalFile.lastIndexOf("."));

	        // New name based on ID's file
	        String newName = originalFile.substring(0, originalFile.lastIndexOf(".")) + "_" + idFile + extension;

	        // Resolves allocation
			allocation = this.location.resolve(Paths.get(newName)).normalize().toAbsolutePath();

			// Copy file with the new name
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, allocation, StandardCopyOption.REPLACE_EXISTING);
			}

	        return newName;

		} catch (Exception e) {
			throw new StorageException("Uploading Error", e);
		}
		
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.location, 1)
				.filter(path -> !path.equals(this.location))
				.map(this.location::relativize);
		}
		catch (IOException e) {
			throw new StorageException("Reading Error on files: ", e);
		}

	}


	@Override
	public Path load(String filename) {
		return location.resolve(filename);
	}

	@Override
	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new StorageFileNotFoundException(
						"File could not be read: " + filename);
			}
		}
		catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("File could not be read: " + filename, e);
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(location.toFile());
	}

	@Override
	public void delete(String filename) throws IOException {
		Path file = location.resolve(filename);
		Files.deleteIfExists(file);
	}

}
