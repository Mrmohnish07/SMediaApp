package com.mb.Service;

import java.security.Principal;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.mb.Entity.Conversation;
import com.mb.Entity.Post;
import com.mb.Entity.User;

public interface UserRegisterService 
{
   public User addUser(User user);

 public User findByUsername(String username);

public User getLoggedInUser(Principal principal);
 
public void save(User user);

public void followUser(User loggedInUser, String username);

public void unfollowUser(User loggedInUser, String username);

public Set<User> getFollowing(User loggedInUser);

 public Set<User> getFollowers(User loggedInUser);

public User getUserById(Long userId);

 public User getUserByUsername(String username);

public Set<User> getFollowers1(User user);

public Set<User> getFollowing1(User user);

Set<Post> getPostByFOllowing(User loggedInUser);

public Optional<User> findById(Long recipientId);

  
public User findById1(Long recipientId);

 
}
