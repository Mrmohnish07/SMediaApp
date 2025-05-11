package com.mb.Controller;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mb.Entity.Comment;
import com.mb.Entity.Conversation;
import com.mb.Entity.Message;
import com.mb.Entity.Notification;
import com.mb.Entity.Post;
import com.mb.Entity.User;
import com.mb.Repository.ConversationRepo;
import com.mb.Repository.UserRepo;
import com.mb.Service.CommentServiceImpl;
import com.mb.Service.IConversationService;
import com.mb.Service.IMessageService;
import com.mb.Service.IPostService;
import com.mb.Service.LikeServiceImpl;
import com.mb.Service.NotificationServiceImpl;
import com.mb.Service.ProfilePhotoService;
import com.mb.Service.UserRegisterService;

import jakarta.servlet.http.HttpServletRequest;
 

@Controller
public class SocialMediaController
{
	@Autowired
	private UserRegisterService service;
	
	 @Autowired
	    private ProfilePhotoService profilePhotoService;
	 
	 @Autowired
	 private NotificationServiceImpl nservice;
	 
	 @Autowired
	 private UserRepo repo;
 
	 @Autowired 
 private CommentServiceImpl cservice;
	 
	 @Autowired
	 private IPostService  pservice;
	 
	  @Autowired
	    private LikeServiceImpl lservice;
	  
	    @Autowired
	    private IMessageService mservice;
	    
	    @Autowired
	    private IConversationService  csservice;
	    
	    @Autowired
	    private ConversationRepo crepo;
	    
	    @Autowired
	    private SimpMessagingTemplate simpMessagingTemplate;

	  private static final Logger logger = LoggerFactory.getLogger(SocialMediaController.class);
	 
	
	@GetMapping("/signup")
       public String showSignUp(Model model) {
    	   User user=new User();
    	   model.addAttribute("user", user);
    	   return "SignUp";
       }
	
	@PostMapping("/signup")
	public String addUser(@ModelAttribute("user")User user) {
		service.addUser(user);
		return "redirect:/login";
	}
	
	@GetMapping("/login")
	public String getLoginPage(Model model) {
		User user=new User();
		return "login";
	}
	
	@GetMapping("/home")
	 public String homePage(@AuthenticationPrincipal UserDetails userDetails, Model model)
	{
		 User user = service.findByUsername(userDetails.getUsername());
		 model.addAttribute("username", user.getUsername());
		 model.addAttribute("profilePhoto",user.getProfilePictureUrl());
		 model.addAttribute("bio", user.getBio());
	 
		// Fetch all users and filter out the logged-in user
        List<User> suggestedUsers =   repo.findAll().stream()
                .filter(enter -> !enter.getUsername().equals(user.getUsername()))
                .collect(Collectors.toList());

        // Add attributes to the model
        model.addAttribute("username",  user.getUsername());
        model.addAttribute("profilePhoto",  user.getProfilePictureUrl());
        model.addAttribute("bio",  user.getBio());
        model.addAttribute("suggestedUsers", suggestedUsers);
        model.addAttribute("loggedInUser", user);

        
        // Fetch posts created by the logged-in user
        List<Post> posts = pservice.getPostsByUser(user);
        Set<Post> postByFOllowing = service.getPostByFOllowing(user);
        
        // Add the posts and user info to the model
      //  model.addAttribute("posts", posts);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("profilePhoto", user.getProfilePictureUrl());
        model.addAttribute("bio", user.getBio());
        model.addAttribute("followingPosts", postByFOllowing);
        Long countN= nservice.getCountOfNotifications(user);
        model.addAttribute("Ncount",countN);
     
        // Prepare a map to track whether the logged-in user has liked each post
        Map<Long, Boolean> likedPostsMap = new HashMap<>();
        for (Post post : postByFOllowing) {
            boolean isLiked = lservice.isPostLikedByUser(post.getId(), user);
            likedPostsMap.put(post.getId(), isLiked);
        }
 
        // Add posts and like-related attributes to the model
        model.addAttribute("followingPosts", postByFOllowing);
        model.addAttribute("likedPostsMap", likedPostsMap); // Post ID -> Liked status
        model.addAttribute("loggedInUser", user);

        // Fetch comments for each post
        for (Post post : postByFOllowing) {
            List<Comment> comments = cservice.getCommentsForPost(post);
            post.setComments(comments);
        }
        model.addAttribute("loggedInUser", user);
        Map<Long, Long> likedPerPost = postByFOllowing.stream().
        collect(Collectors.toMap(Post::getId,
        		           (post)-> lservice.getLikeCount(post.getId())));
        model.addAttribute("likeCount", likedPerPost);
        
        User loggedInUser =  service.findByUsername(userDetails.getUsername()); // Get logged-in user
        
        List<Conversation> conversations = crepo.findByParticipant1OrParticipant2(loggedInUser, loggedInUser);

        // Add the conversations to the model to display on the homepage
        model.addAttribute("conversations", conversations);
        
		 return "home";
	}
	@PostMapping("/uploadProfilePhoto")
    public String uploadProfilePhoto(
            @RequestParam("profilePhoto") MultipartFile file,
            Principal principal,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Please select a file to upload.");
            return "redirect:/home";
        }

        try {
 
            // Update the user's profile photo URL in the database
            String username = principal.getName();
            String path = profilePhotoService.saveProfilePhoto(file);
            User user = repo.findByUsername(username);
            user.setProfilePictureUrl(path);
            repo.save(user);
            redirectAttributes.addFlashAttribute("message", "Photo uploaded successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to upload photo. Try again.");
        }

        return "redirect:/home";
    }
	   @GetMapping("/profile/{username}")
	    public String viewProfile(@PathVariable String username , Principal principal ,Model model) {
	        User user = service.findByUsername(username);
	     User loggedInUser=service.findByUsername(principal.getName()); 
	        model.addAttribute("user", user);
	        model.addAttribute("loggedInUser", loggedInUser);
	        
	        // Fetch followers and following for the user
            Set<User> followers = service.getFollowers1(user);
            Set<User> following = service.getFollowing1(user);
            
	        model.addAttribute("following ", following );
	        model.addAttribute("followers", followers);
	        Set<Post> postsByUser = user.getPosts();
	        
	        // Prepare a map to track whether the logged-in user has liked each post
	        Map<Long, Boolean> likedPostsMap = new HashMap<>();
	        for (Post post : postsByUser) {
	            Boolean isLiked = lservice.isPostLikedByUser(post.getId(), loggedInUser);
	            likedPostsMap.put(post.getId(), isLiked != null ? isLiked : false); // Default to 'false' if null
	        }
	        model.addAttribute("likedPostsMap", likedPostsMap);
	        
	        
	        Map<Long, Long> likedPerPost = postsByUser.stream().
	        collect(Collectors.toMap(Post::getId,
	        		           (post)-> lservice.getLikeCount(post.getId())));
	        model.addAttribute("likeCount", likedPerPost);
	        
	        User profileUser = service.findByUsername(username);
	        // Add necessary attributes to the model
	        List<Conversation> conversation = csservice.getConversationsForUser(loggedInUser);
	        model.addAttribute("profileUser", profileUser);
	        model.addAttribute("conversation", conversation);
	        
	        
	        return "profile";
	    }
	   @PostMapping("/uploadCoverPhoto")
	    public String uploadCoverPhoto(@RequestParam("coverPhoto") MultipartFile coverPhoto, 
	                                   
	                                   Principal principal, Model model) throws IOException {

	        if (coverPhoto.isEmpty()) {
	            model.addAttribute("message", "No file selected for upload");
	            return "profile"; // Return the profile page with error message
	        }

	        try {
	            // Use service to handle file storage and update user's cover photo URL
	        	String username = principal.getName();
	        	User user = repo.findByUsername(username);
	            String coverPhotoUrl = profilePhotoService.uploadCoverPhoto(coverPhoto, user);
	            user.setCoverPhotoUrl(coverPhotoUrl);
	            repo.save(user);
	            model.addAttribute("user", user);
	            model.addAttribute("message", "Cover photo uploaded successfully");

	        } catch (Exception e) {
	            model.addAttribute("message", "Error uploading cover photo");
	            e.printStackTrace();
	        }

	        return "redirect:/profile/"+principal.getName(); // Return the profile page after upload
	    }
	   @PostMapping("/updateBio")
	   public String updateBio(@RequestParam("bio") String bio,Principal principal) {
		   User user = repo.findByUsername(principal.getName());
		   user.setBio(bio);
		   repo.save(user);
		   return "redirect:/profile"+principal.getName();
	   }
	   @PostMapping("/createPost")
	   public String createPost(@RequestParam("postContent") String postContent,
	                            @RequestParam(value = "postImage", required = false) MultipartFile postImage,
	                            Principal principal,
	                            Model model) {
		   User user = repo.findByUsername(principal.getName());
	       Post newPost = new Post();
	       newPost.setContent(postContent);
	       newPost.setUser(user);

	       if (postImage != null && !postImage.isEmpty()) {
	           try {
	               // Save the post image and get the URL to store in the database
	               String imageUrl = pservice.savePostImage(postImage);
	               newPost.setImageUrl(imageUrl);
	           } catch (IOException e) {
	               model.addAttribute("error", "Image upload failed: " + e.getMessage());
	               return "home";
	           }
	       }
	       pservice.savePost(newPost);

	       return "redirect:/home";
	   }
	   @PostMapping("/updatePersonalInfo")
	    public String updatePersonalInfo(@RequestParam("dob") String dob, 
	                                     @RequestParam("gender") String gender,
	                                     @RequestParam("website") String website,
	                                     @RequestParam("email") String email,
	                                     @RequestParam("phone") String phone,
	                                     @RequestParam("location") String location,
	                                     Principal principal, Model model) {

	        // Convert dob string to LocalDate
	        LocalDate localDateDob = LocalDate.parse(dob);
	        
	        // Get the logged-in user using Principal
	        User user = service.getLoggedInUser(principal);
	        
	        // Update user details
	        user.setDob(localDateDob);
	        user.setGender(gender);
	        user.setEmail(email);
	        user.setWebsite(website);
	        user.setPhone(phone);
	        user.setLocation(location);
	        
	        // Save updated user
	        service.save(user);

	        // Add user to model and redirect to profile page
	        model.addAttribute("user", user);
	        return "redirect:/profile"; // Redirect to the profile page to show updated info
	    }
	   @PostMapping("/user/follow/{username}")
	    public String followUser(Principal principal, @PathVariable String username) {
	        User loggedInUser = repo.findByUsername(principal.getName());
	         service.followUser(loggedInUser, username);
	         nservice.sendFollowNotification(loggedInUser,repo.findByUsername(username));
	        return "redirect:/profile/" + username;
	    }    
	    @PostMapping("/user/unfollow/{username}")
	    public String unfollowUser(Principal principal, @PathVariable String username) {
	        User loggedInUser = repo.findByUsername(principal.getName());
	      service.unfollowUser(loggedInUser, username);
	        return "redirect:/profile/" + username;
	    }   
	    @GetMapping("/user/following")
	    public String getFollowingList(Principal principal, Model model) {
	        User loggedInUser = repo.findByUsername(principal.getName());
	        Set<User> followingList = service.getFollowing(loggedInUser);
	        model.addAttribute("followingList", followingList);
	        return "followingList";
	    } 
	    @GetMapping("/user/followers")
	    public String getFollowersList(Principal principal, Model model) {
	        User loggedInUser = repo.findByUsername(principal.getName());
	        Set<User> followersList =  service.getFollowers(loggedInUser);
	        model.addAttribute("followersList", followersList);
	        return "followersList";
	    }
	 // View the list of users the current user is following
	    @GetMapping("/user/{username}/following")
	    public String viewFollowing(@PathVariable String username, Principal principal, Model model) {
	        if (principal != null) {
	            String  Username = principal.getName(); // Get the logged-in user's username

	            // Optionally, verify if the username in the URL matches the logged-in user's username
	           
	                User user = service.getUserByUsername(username); // Get the user by username
	                Set<User> following  = service.getFollowing1(user); // Get the users the current user is following
	                model.addAttribute("user", user);
	                model.addAttribute("following", following );
	             
	        }
	        return "user/following"; // Thymeleaf template for following users
	    }
	    @GetMapping("/user/{username}/followers")
	    public String viewFollowers(@PathVariable String username, Principal principal, Model model) {
	        if (principal != null) {
	            String  Username = principal.getName(); // Get the logged-in user's username

	            
	                User user = service.getUserByUsername(username); // Get the user by username
	                Set<User> followers = service.getFollowers1(user); // Get users who are following this user
	                model.addAttribute("user", user);
	                model.addAttribute("followers", followers);
	            } 
	        
	        return "user/followers"; // Thymeleaf template for followers
	    }
	    @GetMapping("/notifications")
	    public String viewNotifications( Principal principal,Model model) {
	     User  loggedInUser = service.findByUsername(principal.getName());
	        List<Notification> allnotifications = nservice.getAllUnreadNotificationOfUser(loggedInUser);
	        model.addAttribute("notifications", allnotifications);
	        nservice.setAllNotificationAsRead(allnotifications);
	        model.addAttribute("username", loggedInUser.getUsername());
	        return "notifications";
	    }
	    @PostMapping("/user/follow/feed/{username}")
	    public String followUseronHome(Principal principal, @PathVariable String username) {
	        User loggedInUser = repo.findByUsername(principal.getName());
	         service.followUser(loggedInUser, username);
	         nservice.sendFollowNotification(loggedInUser,repo.findByUsername(username));
	        return "redirect:/home";
	    }   
	    @PostMapping("/user/unfollow/feed/{username}")
	    public String unfollowUseronHome(Principal principal, @PathVariable String username) {
	        User loggedInUser = repo.findByUsername(principal.getName());
	      service.unfollowUser(loggedInUser, username);
	        return "redirect:/home";
	    }
	    @PostMapping("/like/{postId}/toggle")
	    public String toggleLike(@PathVariable Long postId, Principal principal,Model model 
	    		                               ,HttpServletRequest request) {
	        // Get the logged-in user
	        User loggedInUser = service.getUserByUsername(principal.getName());
	        
	        // Toggle the like for the given post
	        lservice.toggleLike(postId, loggedInUser);
	        
	        model.addAttribute("loggedInUser", loggedInUser);
	        String referer = request.getHeader("Referer");
	        // Redirect back to the home page
	        return "redirect:"+referer;
	    }  
	    @PostMapping("/comments/add/{postId}")
	    public String addComment(@PathVariable Long postId, @RequestParam String content, Principal principal) {
	        // Get the logged-in user
	        User loggedInUser = service.getUserByUsername(principal.getName());

	        // Get the post by ID
	        Post post = pservice.getPostById(postId);

	        // Save the comment
	        cservice.saveComment(post, loggedInUser, content);

	        // Redirect to the home or post view page
	        return "redirect:/home"; // Modify to redirect to the specific post page if needed
	    }
	    @GetMapping("/{postId}")
	    public String getComments(@PathVariable Long postId, Model model) {
	        // Get the post
	        Post post = pservice.getPostById(postId);

	        // Get the comments for the post
	        List<Comment> comments = cservice.getCommentsForPost(post);

	        // Add attributes to the model
	        model.addAttribute("post", post);
	        model.addAttribute("comments", comments);

	        return "postDetails"; // Change to the appropriate view for displaying comments
	    }
	    @PostMapping("/comments/delete/{commentId}")
	    public String deleteComment(@PathVariable Long commentId) {
	        // Delete the comment
	        cservice.deleteComment(commentId);
	        // Redirect to the home or post view page
	        return "redirect:/home"; // Modify to redirect to the specific post page if needed
	    }
	    @GetMapping("/{postId}/likes")
	    public String getPostLikes(@PathVariable Long postId, Model model) {
	        logger.info("Fetching details for post ID: {}", postId);

	        // Retrieve the post by ID
	        Post post = pservice.getPostById(postId);
	        logger.debug("Post retrieved: {}", post);

	        // Fetch the users who liked the post
	        List<User> usersWhoLiked = lservice.getUsersWhoLikedPost(post);
	        logger.debug("Users who liked the post: {}", usersWhoLiked);

	        // Add the data to the model
	        model.addAttribute("post", post);
	        model.addAttribute("likeCount", usersWhoLiked.size());
	        model.addAttribute("likedUsers", usersWhoLiked);

	        logger.info("Returning post-like details to the view.");
	        return "post-likes"; // Return the name of the Thymeleaf template to display
	    }
	   
   

	    // Show a specific conversation
	    @GetMapping("/conversation")
	    public String getConversation(@RequestParam Long conversationId, Model model, Principal principal) {
	        String loggedInUsername = principal.getName();
	        User loggedInUser = service.findByUsername(loggedInUsername);
	        User targetUser=service.findById1(conversationId);
	        Conversation conversation = csservice.findConversationBetweenUsers(loggedInUser, targetUser);
	        List<Message> messages = mservice.getMessages(conversation);
	        model.addAttribute("conversation", conversation);
	        model.addAttribute("messages", messages);
	        model.addAttribute("loggedInUsername", loggedInUsername);
	        return "messages";
	    }

}
