package com.mb.Service;

import java.util.ArrayList;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mb.Repository.UserRepo;

@Service
public class CustomUserDetails implements UserDetailsService
{
	
	@Autowired
	private  UserRepo repo;
	
	 
	    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		 
		  com.mb.Entity.User user =  repo.findByUsername(username);
		 if (user == null) {
	            throw new UsernameNotFoundException("User not found with username: " + username);
	        }

	      return new User(user.getUsername(),user.getPassword(),new ArrayList<>());
	 }
}
 