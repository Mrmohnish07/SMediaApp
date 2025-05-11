package com.mb.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mb.Entity.Conversation;
import com.mb.Entity.User;

public interface ConversationRepo extends JpaRepository<Conversation, Long> 
{
	   @Query("SELECT c FROM Conversation c WHERE c.participant1 = :participant1 OR c.participant2 = :participant2")
	    List<Conversation> findByParticipant1OrParticipant2(@Param("participant1") User participant1, @Param("participant2") User participant2);

	    /**
	     * Retrieves a conversation between two participants, if it exists.
	     */
	    @Query("SELECT c FROM Conversation c WHERE " +
	            "(c.participant1 = :participant1 AND c.participant2 = :participant2) " +
	            "OR (c.participant1 = :participant2 AND c.participant2 = :participant1)")
	    Optional<Conversation> findByParticipants(@Param("participant1") User participant1, @Param("participant2") User participant2);

	    /**
	     * Finds a conversation between two users by their user IDs.
	     */
	    Optional<Conversation> findByParticipant1_IdAndParticipant2_Id(Long userId1, Long userId2);
}
