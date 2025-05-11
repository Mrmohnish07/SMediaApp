package com.mb.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mb.Entity.Notification;
import com.mb.Entity.User;
import com.mb.Repository.NotificationRepo;

@Service
public class NotificationServiceImpl implements INotificationService 
{
	@Autowired
   private NotificationRepo nrepo;
	
	@Override
	public Notification sendFollowNotification(User sender, User receiver)
	{
		// Validate users (optional)
        if (sender == null || receiver == null) {
            throw new IllegalArgumentException("Sender and Receiver must be provided");
        }

        // Create a new notification instance
        Notification notification = new Notification();
        notification.setSender(sender);
        notification.setReceiver(receiver);
        notification.setType("FOLLOW");  // Type of notification (you can use other types like 'LIKE', etc.)
        notification.setCreatedAt(LocalDateTime.now());  // Timestamp of when the notification is created
        notification.setRead(false);  // Initially, the notification is unread

        // Save the notification to the database
        return nrepo.save(notification);
	}
	@Override 
  public Long getCountOfNotifications(User loggedInUser) {
	  return nrepo.countByReceiverAndIsReadFalse(loggedInUser);
  }
	@Override
	public List<Notification> getAllUnreadNotificationOfUser(User user){
		return  nrepo.findByReceiverAndIsReadFalse(user);
	}
	@Override
	public void setAllNotificationAsRead(List<Notification> notifications)
	{
		 notifications.forEach(notification -> notification.setRead(true));
          nrepo.saveAll(notifications);
	}
}
