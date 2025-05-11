package com.mb.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="Messages")
public class Message 
{
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @ManyToOne
	    @JoinColumn(name = "conversation_id")
	    private Conversation conversation;

	    @ManyToOne
	    @JoinColumn(name = "sender_id")
	    private User sender;

	    public User getReceiver() {
			return receiver;
		}

		@ManyToOne
	    @JoinColumn(name = "receiver_id")
	    private User receiver;
		
	    private String content;
	    
	    @Column(nullable = false)
	    private LocalDateTime timestamp =LocalDateTime.now();
	    
	    private boolean isRead;

		public Message() {
			super();
			 
		}

		public Message(Long id, Conversation conversation, User sender,User receiver, String content, LocalDateTime timestamp,
				boolean isRead) {
			super();
			this.id = id;
			this.conversation = conversation;
			this.sender = sender;
			this.receiver=receiver;
			this.content = content;
			this.timestamp = timestamp;
			this.isRead = isRead;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Conversation getConversation() {
			return conversation;
		}

		public void setConversation(Conversation conversation) {
			this.conversation = conversation;
		}

		public User getSender() {
			return sender;
		}

		public void setSender(User sender) {
			this.sender = sender;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public LocalDateTime getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(LocalDateTime timestamp) {
			this.timestamp = LocalDateTime.now();
		}

		public boolean isRead() {
			return isRead;
		}

		public void setRead(boolean isRead) {
			this.isRead = isRead;
		}

		public void setReceiver(User receiver) {
		    this.receiver=receiver;
		}

		
}
