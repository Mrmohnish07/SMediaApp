package com.mb.Service;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.mb.Entity.Post;
import com.mb.Entity.User;

public interface IPostService 
{
    public Post savePost(Post post);

    public List<Post> getPostsByUser(User user);

   public String savePostImage(MultipartFile postImage) throws IOException;

   public Post getPostById(Long postId);

   //public 	List<User> getUsersWhoLikedPost(Long postId);

Set<User> getUsersWhoLikedPost(Set<Long> likes);
}
