package com.mb.Service;

import java.security.Principal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mb.Entity.Post;
import com.mb.Entity.User;
import com.mb.Repository.PostRepo;
import com.mb.Repository.UserRepo;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserRegisterServiceImpl implements UserRegisterService {

	@Autowired
	private  UserRepo repo;
	
	@Autowired
	private PostRepo prepo;
	
 
	
	@Autowired
	private  PasswordEncoder passwordEncoder;
	
	@Override
	public User addUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return repo.save(user);
	}

	@Override
	public User findByUsername(String username) { 
		return repo.findByUsername(username);
	}

	public User getLoggedInUser(Principal principal) {
		 
		String username = principal.getName(); // This returns the username of the logged-in user

        // Fetch and return the user from the repository using the username
        return  repo.findByUsername(username); // Assumes you have a findByUsername method in UserRepository
	}

	@Override
	public void save(User user) {
		 repo.save(user);
		
	}
	@Override
	public void followUser(User loggedInUser ,String username) {
	User wantTofollow=  repo.findByUsername(username);
	
	loggedInUser.getFollowing().add(wantTofollow.getId());
   wantTofollow.getFollowers().add(loggedInUser.getId());
    
   repo.save(wantTofollow);
   repo.save(loggedInUser);

    }
	
	@Override
	public void unfollowUser(User loggedInUser ,String username) {
		User wantTounfollow=  repo.findByUsername(username);
		
		loggedInUser.getFollowing().remove(wantTounfollow.getId());
	   wantTounfollow.getFollowers().remove(loggedInUser.getId());
	    
	   repo.save(wantTounfollow);
	   repo.save(loggedInUser);

	    }
	@Override
	  public Set<User> getFollowing(User loggedInUser) {
		  return loggedInUser.getFollowing().stream().map((followingId)-> repo.findById(followingId).orElse(null)).collect( Collectors.toSet());
	            // Return the set of following users
	    }
 
	     // Get the list of users who are following the logged-in user. 
	@Override
	    public Set<User> getFollowers(User loggedInUser) {
	    	 return loggedInUser.getFollowers().stream().map((followersId)-> repo.findById(followersId).orElse(null)).collect( Collectors.toSet()); // Return the set of followers
	    }
	 @Override
	    public User getUserById(Long userId) {
	        return  repo.findById(userId)
	                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
	    }

	    @Override
	    public User getUserByUsername(String username) {
	        return  repo.findByUsername(username);
	                  
	    }

	    @Override
	    public Set<User> getFollowing1(User user) {
	        
	        Set<User> following = user.getFollowing().stream()
	        .map(userId -> repo.findById(userId).orElse(null))  // Get User by ID or null if not found
	        .filter(Objects::nonNull)  // Ensure nulls are filtered out
	        .collect(Collectors.toSet());  // Collect to a Set
	        System.out.println("Total Following Users: " + following.size()); // Check size of the set

	        return following;
	    }

	    @Override
	    public Set<User> getFollowers1(User user) {
	        Set<User> follower= new HashSet<>();
	        for (Long followerUsername : user.getFollowers()) { // assuming getFollowers returns a Set of usernames
	            follower.add(getUserById(followerUsername)); // Retrieve the user by username
	        }
	        return follower;
	    }
	    @Override
	    public Set<Post> getPostByFOllowing(User loggedInUser){
	    	Set<User> following = getFollowing(loggedInUser);
	    	Set<Post> posts = following.stream()
	    		    .map(User::getPosts) // Map each user to their set of posts
	    		    .flatMap(Set::stream) // Flatten the sets of posts into a single stream
	    		    .collect(Collectors.toSet()); // Collect the flattened stream into a set

	    	return posts;
 
	    }

		@Override
		public Optional<User> findById(Long recipientId) {
			 
			return  repo.findById(recipientId);
		}

		@Override
		public User findById1(Long recipientId) {
			 return repo.findById(recipientId).orElse(null);
		}
  
}    