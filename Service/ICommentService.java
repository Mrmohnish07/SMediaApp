package com.mb.Service;

import java.util.List;

import com.mb.Entity.Comment;
import com.mb.Entity.Post;
import com.mb.Entity.User;

public interface ICommentService 
{

 public 	void saveComment(Post post, User user, String content);

public 	List<Comment> getCommentsForPost(Post post);

	public void deleteComment(Long commentId);

}
