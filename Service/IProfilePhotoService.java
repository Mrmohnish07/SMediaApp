package com.mb.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mb.Entity.User;

@Service
public class IProfilePhotoService implements ProfilePhotoService{
	
	 private static final String UPLOAD_DIR = "src/main/resources/static/images/profile/";
	 private static final String UPLOAD_DIR_COVER="src/main/resources/static/images/cover/";
	 
	@Override
	public String saveProfilePhoto(MultipartFile file ) throws IOException {
	 
		 Path directoryPath = Paths.get(UPLOAD_DIR);
		    if (!Files.exists(directoryPath)) {
		        Files.createDirectories(directoryPath);
		    }

		
		  Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
	        Files.write(path, file.getBytes());

	        // Return the URL path to be stored in the database
	        return "/images/profile/" + file.getOriginalFilename();
	}



	@Override
	public String uploadCoverPhoto(MultipartFile coverPhoto, User user) {
		 String fileName = user.getUsername() + "-cover-photo-" + System.currentTimeMillis() + ".jpg";
	        Path targetLocation = Paths.get(UPLOAD_DIR_COVER, fileName);

	        // Create the directory if it doesn't exist
	        File directory = new File(UPLOAD_DIR);
	        if (!directory.exists()) {
	            directory.mkdirs();
	        }

	        // Copy the file to the target location
	        try {
				Files.copy(coverPhoto.getInputStream(), targetLocation);
			} catch (IOException e) {
				e.printStackTrace();
			}

	        // Return the URL or path of the uploaded file (you can adjust the URL based on your application's needs)
	        return "/images/cover/" + fileName;  // Adjust this as per your resource mapping
	    }
	
}
 
 
