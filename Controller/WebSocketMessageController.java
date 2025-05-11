package com.mb.Controller;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.mb.DTO.MessageDTO;
import com.mb.Entity.Conversation;
import com.mb.Entity.Message;
import com.mb.Entity.User;
import com.mb.Repository.UserRepo;
import com.mb.Service.IConversationService;
import com.mb.Service.IMessageService;

@Controller
public class WebSocketMessageController
{
	  @Autowired
	    private IConversationService conversationService;

	    @Autowired
	    private IMessageService messageService;
	    
	    @Autowired
	    private UserRepo userRepo;

	    @Autowired
	    private SimpMessagingTemplate messagingTemplate;
	    
	    private static final Logger logger = LoggerFactory.getLogger(WebSocketMessageController.class);


	    @MessageMapping("/chat")
	    public void sendMessage(Message incomingMessage) {
	        // Validate sender and receiver
	        if (incomingMessage.getSender() == null || incomingMessage.getReceiver() == null) {
	            logger.error("Sender or Receiver is null in the incoming message");
	            throw new IllegalArgumentException("Sender and Receiver must not be null");
	        }

	        Long senderId = incomingMessage.getSender().getId();
	        Long receiverId = incomingMessage.getReceiver().getId();

	        // Fetch or create the conversation
	        Conversation conversation = conversationService.createConversation(senderId, receiverId);

	        // Assign conversation and timestamp
	        if (incomingMessage.getTimestamp() == null) {
	            incomingMessage.setTimestamp(LocalDateTime.now());
	        }

	        // Log the timestamp for debugging purposes
	        logger.info("Timestamp assigned: {}", incomingMessage.getTimestamp());

	        // Save the message
	        Message savedMessage = messageService.saveMessage(incomingMessage);

	        // Log saved message with timestamp
	        logger.info("Message saved with timestamp: {}", savedMessage.getTimestamp());

	        // Prepare MessageDTO for WebSocket broadcasting
	        MessageDTO messageDTO = new MessageDTO();
	        messageDTO.setId(savedMessage.getId());
	        messageDTO.setContent(savedMessage.getContent());
	        messageDTO.setSenderId(savedMessage.getSender().getId());
	        messageDTO.setSenderUsername(savedMessage.getSender().getUsername());
	        messageDTO.setSenderProfilePictureUrl(savedMessage.getSender().getProfilePictureUrl());
	        messageDTO.setTimestamp(savedMessage.getTimestamp() != null
	                    ? savedMessage.getTimestamp().toString() // Ensuring timestamp is a string
	                    : "Unknown");

	        // Broadcast message
	        logger.info("Broadcasting message: {}", messageDTO);
	        messagingTemplate.convertAndSend("/topic/conversation/" + conversation.getId() + "/info", messageDTO);
	    }


}
