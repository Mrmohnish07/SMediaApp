package com.mb.Service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.mb.Entity.User;

public interface ProfilePhotoService 
{
   public  String saveProfilePhoto(MultipartFile file) throws IOException;

public String uploadCoverPhoto(MultipartFile coverPhoto, User user);
}
