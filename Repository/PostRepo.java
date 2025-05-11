package com.mb.Repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mb.Entity.Post;
import com.mb.Entity.User;

public interface PostRepo extends JpaRepository<Post,Long>
{
   public List<Post> findByUser(User user); 
// @Query("SELECT p.likedByUser FROM Post p WHERE p.id = :postId")
//   
//   public List<User> findLikedByUsersByPostId(@Param("postId") Long postId);
//  
}
