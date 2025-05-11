package com.mb.Service;

import java.util.List;

import com.mb.Entity.Post;
import com.mb.Entity.User;

public interface ILikeService 
{

	public void toggleLike(Long postId, User user);

	public Long getLikeCount(long postId);

	public boolean isPostLikedByUser(Long postId, User user);

	List<User> getUsersWhoLikedPost(Post post);

 

}
