package com.mb.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mb.Entity.Comment;
import com.mb.Entity.Post;
import com.mb.Entity.User;

public interface CommentRepo extends JpaRepository<Comment,Long> 
{
	public  List<Comment> findByPost(Post post);

	  public   List<Comment> findByUser(User user);
}
