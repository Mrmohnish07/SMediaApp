package com.mb.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mb.Entity.Like;
import com.mb.Entity.Post;
import com.mb.Entity.User;
import com.mb.Repository.PostRepo;
import com.mb.Repository.UserRepo;

@Service
public class PostServiceImpl implements IPostService {

	 private static final String UPLOAD_DIRECTORY = "src/main/resources/static/images/posts/";
	
	@Autowired
    private PostRepo prepo;
	
	@Autowired
	private UserRepo repo;
	
	  // Save a post image
	@Override
    public String savePostImage(MultipartFile postImage) throws IOException {
        // Generate a unique file name for the post image
        String fileName = System.currentTimeMillis() + "-" + postImage.getOriginalFilename();
        
        // Define the target location to save the file
        Path targetLocation = Paths.get(UPLOAD_DIRECTORY, fileName);
        
        // Create the directory if it doesn't exist
        File directory = new File(UPLOAD_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Save the file to the target location
        try {
            Files.copy(postImage.getInputStream(), targetLocation);
        } catch (IOException e) {
            throw new IOException("Failed to save image: " + e.getMessage());
        }

        // Return the URL path to be stored in the database
        return "/images/posts/" + fileName;
    }
	
	@Override
	public Post savePost(Post post) {
	 
		return prepo.save(post);
	}
	 @Override
	    public List<Post> getPostsByUser(User user) {
	        return prepo.findByUser(user);  // This assumes you have a method in the PostRepository to find posts by user
	    }

	@SuppressWarnings("deprecation")
	@Override
	public Post getPostById(Long postId) {
		 return prepo.getById(postId);
	}

//	@Override
//	public List<User> getUsersWhoLikedPost(Long postId) {
//		 return prepo.findLikedByUsersByPostId(postId);
//	}
	@Override
	public Set<User> getUsersWhoLikedPost(Set<Long> likes) {
		return likes.stream()
	            .map(userId -> repo.findById(userId)) // Find user by ID using the repository
	            .filter(Optional::isPresent) // Filter out empty Optional values
	            .map(Optional::get) // Extract the User object from Optional
	            .collect(Collectors.toSet()); // Collect into a Set
	   
	}

}
