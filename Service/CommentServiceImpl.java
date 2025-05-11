package com.mb.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mb.Entity.Comment;
import com.mb.Entity.Post;
import com.mb.Entity.User;
import com.mb.Repository.CommentRepo;

@Service
public class CommentServiceImpl implements ICommentService 
{
	@Autowired
   private CommentRepo crepo;
	
	@Override
    public void saveComment(Post post, User user, String content) {
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(content);
        crepo.save(comment);
    }
	@Override
    public List<Comment> getCommentsForPost(Post post) {
        return crepo.findByPost(post);
    }
	@Override
    public void deleteComment(Long commentId) {
        crepo.deleteById(commentId);
    }
}
