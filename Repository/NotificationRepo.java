package com.mb.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mb.Entity.Notification;
import com.mb.Entity.User;

public interface NotificationRepo extends JpaRepository<Notification, Long>
{

	 public long countByReceiverAndIsReadFalse(User receiver);

	 public List<Notification> findByReceiverAndIsReadFalse(User receiver);
	 
}
