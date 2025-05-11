package com.mb.Entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="Conversations")
public class Conversation 
{
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @ManyToOne
	    @JoinColumn(name = "participant1_id")
	    private User participant1;

	    @ManyToOne
	    @JoinColumn(name = "participant2_id")
	    private User participant2;

	    private String lastMessage;
	    
	    private LocalDateTime lastMessageTime;
	    
	    @OneToMany(mappedBy="conversation")
	    private List<Message> messages;

		public Conversation() {
			super();
		}
 
		public Conversation(Long id, User participant1, User participant2, String lastMessage,
				LocalDateTime lastMessageTime) {
			super();
			this.id = id;
			this.participant1 = participant1;
			this.participant2 = participant2;
			this.lastMessage = lastMessage;
			this.lastMessageTime = lastMessageTime;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public User getParticipant1() {
			return participant1;
		}

		public void setParticipant1(User participant1) {
			this.participant1 = participant1;
		}

		public User getParticipant2() {
			return participant2;
		}

		public void setParticipant2(User participant2) {
			this.participant2 = participant2;
		}

		public String getLastMessage() {
			return lastMessage;
		}

		public void setLastMessage(String lastMessage) {
			this.lastMessage = lastMessage;
		}

		public LocalDateTime getLastMessageTime() {
			return lastMessageTime;
		}

		public void setLastMessageTime(LocalDateTime lastMessageTime) {
			this.lastMessageTime = lastMessageTime;
		}

		@Override
		public String toString() {
			return "Conversation [id=" + id + ","
					+ " participant1=" + participant1 + ","
							+ " participant2=" + participant2 + "]";
		}

		public List<Message> getMessages() {
			return messages;
		}

		public void setMessages(List<Message> messages) {
			this.messages = messages;
		}
	    
		public User getOtherParticipant(User user) {
		    if (this.participant1.equals(user)) {
		        return this.participant2;
		    } else if (this.participant2.equals(user)) {
		        return this.participant1;
		    }
		    return null;
		}
}
