package com.mb.Service;

import java.util.List;
import java.util.Optional;

import com.mb.Entity.Conversation;
import com.mb.Entity.Message;
import com.mb.Entity.User;

public interface IMessageService 
{

	/**
	 * Saves a new message and sends it to the WebSocket channel for real-time updates.
	 */
	  public void sendMessage(User sender, User receiver, Conversation conversation, String content);

	/**
	 * Retrieves all messages for a specific conversation.
	 */
  public List<Message> getMessages(Conversation conversation);

  public Message saveMessage(Message message);
	 
  public Message getLastMessageForConversation(Conversation conversation);
} 
