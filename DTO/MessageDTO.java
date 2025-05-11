package com.mb.DTO;

import lombok.Data;

@Data
public class MessageDTO 
{
   private Long id;
   private String content;
   private Long senderId;
   private String senderUsername;
   private String senderProfilePictureUrl;
   private String timestamp;
   
   
public MessageDTO() {
	super();
}
public MessageDTO(Long id, String content, Long senderId, String senderUsername, String senderProfilePictureUrl,
		String timestamp) {
	super();
	this.id = id;
	this.content = content;
	this.senderId = senderId;
	this.senderUsername = senderUsername;
	this.senderProfilePictureUrl = senderProfilePictureUrl;
	this.timestamp = timestamp  ;
}
public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public String getContent() {
	return content;
}
public void setContent(String content) {
	this.content = content;
}
public Long getSenderId() {
	return senderId;
}
public void setSenderId(Long senderId) {
	this.senderId = senderId;
}
public String getSenderUsername() {
	return senderUsername;
}
public void setSenderUsername(String senderUsername) {
	this.senderUsername = senderUsername;
}
public String getSenderProfilePictureUrl() {
	return senderProfilePictureUrl;
}
public void setSenderProfilePictureUrl(String senderProfilePictureUrl) {
	this.senderProfilePictureUrl = senderProfilePictureUrl;
}
public String getTimestamp() {
	return timestamp;
}
public void setTimestamp(String timestamp) {
	this.timestamp = timestamp;
}
   
   
   
}
