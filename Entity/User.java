package com.mb.Entity;

 
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@Table(name = "users")
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class User
{
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
   
   @Column(nullable = false, unique = true)
   private String username;
   
   @Column(nullable = false)
   private String password;
   
   @Column(nullable = false, unique = true)
   private String email;
 
   private String bio;
   
   private String profilePictureUrl;
   
   private String Location;
   
   private String phone;
   
   private String website;
   
   private String gender;
   
   private String coverPhotoUrl;
   
   @Column(name = "dob")
   private LocalDate dob;

@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
   private  Set<Post> posts;

 
@ElementCollection
private Set<Long> followers;  // Followers - Users who are following this user

@ElementCollection
private Set<Long> following;  // Following - Users this user is following
 
@OneToMany(mappedBy = "participant1")
private List<Conversation> initiatedConversations;

@OneToMany(mappedBy = "participant2")
private List<Conversation> receivedConversations;
 
public User() {
	super();
}
 
public User(String username, String password, String email, String bio, String profilePictureUrl, String location,
		String phone, String website, String gender, String coverPhotoUrl, LocalDate dob, Set<Post> posts,
		Set<Long> followers, Set<Long> following,List<Conversation> initiatedConversations,List<Conversation> receivedConversations) {
	super();
	this.username = username;
	this.password = password;
	this.email = email;
	this.bio = bio;
	this.profilePictureUrl = profilePictureUrl;
	Location = location;
	this.phone = phone;
	this.website = website;
	this.gender = gender;
	this.coverPhotoUrl = coverPhotoUrl;
	this.dob = dob;
	this.posts = posts;
	this.followers = followers;
	this.following = following; 
	this.initiatedConversations=initiatedConversations;
	this.receivedConversations=receivedConversations;
}

public Long getId() {
	return Id;
}

public void setId(Long id) {
	Id = id;
}

public String getUsername() {
	return username;
}

public void setUsername(String username) {
	this.username = username;
}

public String getPassword() {
	return password;
}

public void setPassword(String password) {
	this.password = password;
}

public String getEmail() {
	return email;
}

public void setEmail(String email) {
	this.email = email;
}

public String getBio() {
	return bio;
}

public void setBio(String bio) {
	this.bio = bio;
}

public String getProfilePictureUrl() {
    return profilePictureUrl;
}

public void setProfilePictureUrl(String profilePictureUrl) {
    this.profilePictureUrl = profilePictureUrl;
}
  
public String getLocation() {
	return Location;
}

public void setLocation(String location) {
	Location = location;
}
public Set<Post> getPosts() {
    return posts;
}

public void setPosts(Set<Post> posts) {
    this.posts = posts;
}

// Optionally, you can add helper methods to add/remove posts from the set
public void addPost(Post post) {
    posts.add(post);
    post.setUser(this); // Set the current user as the post's owner
}

public void removePost(Post post) {
    posts.remove(post);
    post.setUser(null); // Set the user to null when removing the post
}
public String getCoverPhotoUrl() {
	return coverPhotoUrl;
}

public void setCoverPhotoUrl(String coverPhotoUrl) {
	this.coverPhotoUrl = coverPhotoUrl;
}

public String getPhone() {
	return phone;
}

public void setPhone(String phone) {
	this.phone = phone;
}

public String getWebsite() {
	return website;
}

public void setWebsite(String website) {
	this.website = website;
}
public LocalDate getDob() {
    return dob;
}

public void setDob(LocalDate dob) {
    this.dob = dob;
}


public String getGender() {
	return gender;
}


public void setGender(String gender) {
	this.gender = gender;
}

public Set<Long> getFollowers() {
	return followers;
}

public void setFollowers(Set<Long> followers) {
	this.followers = followers;
}

public Set<Long> getFollowing() {
	return following;
}

public void setFollowing(Set<Long> following) {
	this.following = following;
}

@Override
public String toString() {
	return "User [username=" + username + "]";
}

public List<Conversation> getInitiatedConversations() {
	return initiatedConversations;
}

public void setInitiatedConversations(List<Conversation> initiatedConversations) {
	this.initiatedConversations = initiatedConversations;
}

public List<Conversation> getReceivedConversations() {
	return receivedConversations;
}

public void setReceivedConversations(List<Conversation> receivedConversations) {
	this.receivedConversations = receivedConversations;
}

 
 
 
}
