package com.mb.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mb.Entity.Conversation;
import com.mb.Entity.Message;

public interface MessageRepo extends JpaRepository<Message,Long> 
{
   public List<Message> findByConversationOrderByTimestampAsc(Conversation convo);
   
   public  Message findTopByConversationOrderByTimestampDesc(Conversation conversation);
}
