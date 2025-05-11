package com.mb.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mb.Entity.Conversation;
import com.mb.Entity.Message;
import com.mb.Entity.User;
import com.mb.Repository.ConversationRepo;
import com.mb.Repository.MessageRepo;
import com.mb.Repository.UserRepo;

@Service
public class MessageServiceImpl implements IMessageService {
    
	@Autowired
	private ConversationRepo crepo;
	
	@Autowired 
	private MessageRepo mrepo;
	
	  @Autowired
	    private SimpMessagingTemplate messagingTemplate;

	    /**
	     * Saves a new message and sends it to the WebSocket channel for real-time updates.
	     
	     */
	  @Override
	    public void sendMessage(User sender, User receiver, Conversation conversation, String content) {
	        Message message = new Message();
	        message.setSender(sender);
	        message.setReceiver(receiver);
	        message.setConversation(conversation);
	        message.setContent(content);
	        message.setTimestamp(LocalDateTime.now());
	        message.setRead(false);  // Set to false initially

	        // Save the message to the database
	        mrepo.save(message);

	        // Update the conversation with the last message and timestamp
	        conversation.setLastMessage(content);
	        conversation.setLastMessageTime(message.getTimestamp());
	        crepo.save(conversation);

	        // Send the message to the WebSocket channel for real-time updates
	        messagingTemplate.convertAndSend("/topic/conversation/" + conversation.getId(), message);
	    }

	    /**
	     * Retrieves all messages for a specific conversation.
	     *
	     * @param conversation The conversation whose messages are to be retrieved.
	     * @return A list of messages in the conversation.
	     */
	  @Override
	    public List<Message> getMessages(Conversation conversation) {
	        return mrepo.findByConversationOrderByTimestampAsc(conversation);
	    }
	 

	    // Save a message to the database
	  @Override
	    public Message saveMessage(Message message) {
	        return mrepo.save(message);
	    }

	@Override
	public Message getLastMessageForConversation(Conversation conversation) {
		 return mrepo.findTopByConversationOrderByTimestampDesc(conversation);
	}

	 
	 
}
