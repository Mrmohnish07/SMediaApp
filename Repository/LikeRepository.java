package com.mb.Repository;
 

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mb.Entity.Like;
import com.mb.Entity.Post;
import com.mb.Entity.User;

public interface LikeRepository extends JpaRepository<Like,Long>
{
   public Like findByPostAndUser(Post post, User user);
    
   public Long countByPost(Post post);

public void deleteByPostAndUser(Post post, User user);

@Query("SELECT l.user.id FROM Like l WHERE l.post = :post")
public List<Long> findUserIdsByPost(@Param("post") Post post);
 
}
 
