package com.mb.Service;

import java.util.List;
import java.util.Optional;

import com.mb.Entity.Conversation;
import com.mb.Entity.User;

public interface IConversationService 
{

	/**
	 * Creates a new conversation between two users if one doesn't already exist.
	
	 */
	Conversation createConversation(Long loggedInUserId, Long userId);

	/**
	 * Retrieves all conversations for a specific user.
	 
	 */
	public List<Conversation> getConversationsForUser(User user);

	public Optional<Conversation> getConversation(User participant1, User participant2);

	 
	 public Conversation findConversationBetweenUsers(User loggedInUser, User targetUser);

	Conversation getConversationsForUser(Long conversationId, User loggedInUser, User targetuser);
	
 

}
