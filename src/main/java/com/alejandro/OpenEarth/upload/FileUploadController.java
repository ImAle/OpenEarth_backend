package com.alejandro.OpenEarth.upload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
public class FileUploadController {

	private final StorageService storageService;
	
	@Autowired
	public FileUploadController(StorageService storageService) {
		this.storageService = storageService;
	}
	
	@GetMapping("/api/picture/{filename}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

		Resource file = storageService.loadAsResource(filename);

		if (file == null)
			return ResponseEntity.notFound().build();

		MediaType mediaType;
		if (filename.endsWith(".avif")) {
			mediaType = MediaType.parseMediaType("image/avif");
		} else if (filename.endsWith(".jpeg") || filename.endsWith(".jpg")) {
			mediaType = MediaType.IMAGE_JPEG;
		} else if (filename.endsWith(".png")) {
			mediaType = MediaType.IMAGE_PNG;
		} else {
			mediaType = MediaType.APPLICATION_OCTET_STREAM;
		}

		return ResponseEntity.ok().contentType(mediaType).body(file);
	}

	@PostMapping("/api/")
	public String handleFileUpload(@RequestParam("file") MultipartFile file, Long idFile,
								   RedirectAttributes redirectAttributes) {

		storageService.store(file, idFile);
		redirectAttributes.addFlashAttribute("message",
				"You successfully uploaded " + file.getOriginalFilename() + "!");

		return "redirect:/";
	}

	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}

}
