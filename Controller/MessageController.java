package com.mb.Controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.mb.DTO.MessageDTO;
import com.mb.Entity.Conversation;
import com.mb.Entity.Message;
import com.mb.Entity.User;
import com.mb.Repository.ConversationRepo;
import com.mb.Repository.UserRepo;
import com.mb.Service.IConversationService;
import com.mb.Service.IMessageService;
import com.mb.Service.UserRegisterService;

 
@Controller
public class MessageController 
{
	@Autowired 
	private IConversationService csservice;
	@Autowired 
	private  UserRegisterService service;
	@Autowired 
	private UserRepo repo;
	@Autowired 
	private IMessageService mservice;
	@Autowired
	private ConversationRepo crepo;
	
	private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
	
	@GetMapping("/messages/{userId}")
	 public String openMessagePage(@PathVariable Long userId, Model model, Principal principal) {
	     Long loggedInUserId = service.findByUsername(principal.getName()).getId();
	     
	     User receiver = repo.findById(userId).orElseThrow(()-> new RuntimeException("user not found"));
	     
	     // Fetch the conversation between the two users or create a new one
	     Conversation conversation = csservice.createConversation(loggedInUserId, userId);
	     
	     model.addAttribute("conversation", conversation);
	     model.addAttribute("receiver", receiver);
	     model.addAttribute("loggedInUserId", loggedInUserId);
	     model.addAttribute("receiverId", userId);  // Or whatever value you want to pass
        List<Message> messages = mservice.getMessages(conversation);
        model.addAttribute("messages", messages);
 
	     return "chat"; // Return the view for the chat page
	 }
	
	@GetMapping("/conversation/{conversationId}")
    public ResponseEntity<List<MessageDTO>> getMessages(@PathVariable Long conversationId) {
	 Conversation findById = crepo.findById(conversationId).get();
         List<Message> messages = mservice.getMessages(findById);
      // Log messages before returning
         messages.forEach(message -> logger.info("Fetched message: " + message.getContent()));
         List<MessageDTO> messageDTOs = messages.stream().map(message -> {
             MessageDTO dto = new MessageDTO();
             
             dto.setId(message.getId());
             dto.setContent(message.getContent());
             dto.setSenderId(message.getSender().getId());
             dto.setSenderUsername(message.getSender().getUsername());
             dto.setSenderProfilePictureUrl(message.getSender().getProfilePictureUrl());
             dto.setTimestamp(message.getTimestamp() != null ? message.getTimestamp().toString() : "Unknown");
             return dto;
         }).collect(Collectors.toList());
         return ResponseEntity.ok(messageDTOs);

    }
	
	@GetMapping("/messages")
	public String getMessagesPage(Principal principal, Model model) {
	    Logger logger = LoggerFactory.getLogger(this.getClass());

	    if (principal == null) {
	        logger.warn("Principal is null. Redirecting to login page.");
	        return "redirect:/login";
	    }

	    // Fetch the authenticated user's username
	    String username = principal.getName();
	    logger.info("Fetching messages page for user: {}", username);

	    // Fetch the logged-in User entity from the database
	    User loggedInUser = service.findByUsername(username);
	    if (loggedInUser == null) {
	        logger.error("Logged-in user not found in the database. Username: {}", username);
	        return "redirect:/login"; // Handle missing user case appropriately
	    }
	    logger.debug("Logged-in user fetched: {}", loggedInUser);

	    // Get all conversations for the logged-in user
	    List<Conversation> conversations = csservice.getConversationsForUser(loggedInUser);
	    logger.info("Fetched {} conversations for user: {}", conversations.size(), username);

	    List<User> recentChatUsers = new ArrayList<>();
	    List<Message> lastMessages = new ArrayList<>();

	    for (Conversation conversation : conversations) {
	        User otherUser = conversation.getOtherParticipant(loggedInUser);
	        if (otherUser != null) {
	            recentChatUsers.add(otherUser);
	            logger.debug("Added recent chat user: {}", otherUser);

	            // Fetch the last message for this conversation
	            Message lastMessage = mservice.getLastMessageForConversation(conversation);
	            if (lastMessage != null) {
	                lastMessages.add(lastMessage);
	                logger.debug("Added last message: {}", lastMessage);
	            } else {
	                logger.warn("No last message found for conversation: {}", conversation.getId());
	            }
	        } else {
	            logger.warn("No other participant found for conversation: {}", conversation.getId());
	        }
	    }

	    // Add attributes to the model
	    model.addAttribute("recentChatUsers", recentChatUsers);
	    model.addAttribute("lastMessages", lastMessages);
	    model.addAttribute("loggedInUserId", loggedInUser.getId());
	    model.addAttribute("username", loggedInUser.getUsername());
	    model.addAttribute("loggedInUser", loggedInUser);
	    logger.info("Successfully prepared model for messages page for user: {}", username);
	    return "messages";
	}


	  
	 
}