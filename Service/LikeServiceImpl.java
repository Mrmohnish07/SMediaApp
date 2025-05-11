package com.mb.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mb.Entity.Like;
import com.mb.Entity.Post;
import com.mb.Entity.User;
import com.mb.Repository.LikeRepository;
import com.mb.Repository.PostRepo;
import com.mb.Repository.UserRepo;

@Service
public class LikeServiceImpl implements ILikeService 
{
	@Autowired
    private LikeRepository lrepo;
    
	@Autowired
    private PostRepo prepo;
    
	@Autowired
	private UserRepo repo;
	
	@Override 
	public void toggleLike(Long postId, User user)
	{
		 Post post = prepo.findById(postId)
	                .orElseThrow(() -> new RuntimeException("Post not found"));
		  // Check if a like already exists
		   Like  l1 = lrepo.findByPostAndUser(post, user);
		   if (l1 != null) {
	            // If like exists, remove it (unlike)
	            lrepo.delete(l1);
	        } 
		   else {
		   
		        // Otherwise, add a new like
		        Like like = new Like();
		        like.setPost(post);
		        like.setUser(user);
		        lrepo.save(like);
		        Set<Long> likes = post.getLikes();
		        likes.add(user.getId());
		   }
	}
	@Override
	public Long getLikeCount(long postId) {
		Post post = prepo.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
		return lrepo.countByPost(post);
	}
	@Override
	public boolean isPostLikedByUser(Long postId, User user) {
		 // Fetch the post from the database
	    Post post = prepo.findById(postId)
	            .orElseThrow(() -> new RuntimeException("Post not found"));

	    // Check if a like exists for the given post and user
	    Like like = lrepo.findByPostAndUser(post, user);

	    // Return true if the like exists, false otherwise
	    return like != null;
	}
	@Override
	public List<User> getUsersWhoLikedPost(Post post){
		 List<Long> userIds = lrepo.findUserIdsByPost(post);
		    
		    // Fetch all users in one go using findAllById
		    List<User> users = repo.findAllById(userIds);
		    
		    // Return the list of users
		    return users;
	}
	 
}
