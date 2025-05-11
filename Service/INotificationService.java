package com.mb.Service;

import java.util.List;

import com.mb.Entity.Notification;
import com.mb.Entity.User;

public interface INotificationService 
{

 public	Notification sendFollowNotification(User sender, User receiver);

   public Long getCountOfNotifications(User loggedInUser);

 public List<Notification> getAllUnreadNotificationOfUser(User user);

 public void setAllNotificationAsRead(List<Notification> notification);

}
