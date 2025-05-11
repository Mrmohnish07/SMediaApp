package com.mb.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mb.Entity.Conversation;
import com.mb.Entity.User;
import com.mb.Repository.ConversationRepo;

import jakarta.transaction.Transactional;

@Service
public class ConversationServiceImpl implements IConversationService
{
	
	@Autowired
	private ConversationRepo crepo;
	  
	@Autowired
	private UserRegisterService service;
	
	
	
	    @Override
	    public Optional<Conversation> getConversation(User participant1, User participant2) {
	        return crepo.findByParticipants(participant1, participant2);
	    }

	    /**
	     * Creates a new conversation between two users if one doesn't already exist.
	    
	     */
	   
	    public Conversation createConversation(User participant1, User participant2) {
	        Optional<Conversation> existingConversation = getConversation(participant1, participant2);
	        if (existingConversation.isPresent()) {
	            return existingConversation.get();
	        }

	        // Create a new conversation if none exists
	        Conversation conversation = new Conversation();
	        conversation.setParticipant1(participant1);
	        conversation.setParticipant2(participant2);
	        return crepo.save(conversation);
	    }

	    /**
	     * Retrieves all conversations for a specific user.
	     
	     */
	    @Override
	    public List<Conversation> getConversationsForUser(User user) {
	        return crepo.findByParticipant1OrParticipant2(user, user);
	    }

		@Override
		public Conversation getConversationsForUser(Long conversationId, User loggedInUser,User targetuser) {
			  Optional<Conversation> conversation = crepo.findById(conversationId);
	        if (conversation.isPresent()) {
	            // Check if the user is a participant of this conversation
	            Conversation conv = conversation.get();
	            if (conv.getParticipant1().equals(conversation) || conv.getParticipant2().equals(conversation)) {
	                return conv;
	            }
	        }
	        else {
				Conversation newConvo= new Conversation();
				   newConvo.setParticipant1(loggedInUser);
				   newConvo.setParticipant2(targetuser);
				 crepo.save(newConvo);
				 
				 return newConvo; 
			}
	         // If no such conversation exists or the user is not a participant
	    return null;
		}

		@Override
		public Conversation findConversationBetweenUsers(User loggedInUser, User targetUser) {
			return crepo
		            .findByParticipant1_IdAndParticipant2_Id(loggedInUser.getId(), targetUser.getId())
		            .orElse(null);  // Return null if no conversation is found
		}

	@Override
	@Transactional
	public Conversation createConversation(Long loggedInUserId, Long userId) {
		 User participant1 = service.findById(loggedInUserId).get();
		 User participant2 = service.findById(userId).get(); 
		 
 Optional<Conversation> findByParticipants = crepo.findByParticipants(participant1, participant2);

	 if(findByParticipants.isPresent()) {
		 findByParticipants.get().getMessages().size();
		 return findByParticipants.get();
	 }
	 
	 Conversation  newconvo= new Conversation();
	 newconvo.setParticipant1(participant1);
	 newconvo.setParticipant2(participant2);
	 return crepo.save(newconvo);
	 
	}
		
}