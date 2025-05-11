package com.mb.Service;

import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService  extends DefaultOAuth2UserService
{
	 @Override
	    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
	        OAuth2User user = super.loadUser(userRequest);

	        // Extract details (like name, email) from user.getAttributes()
	        Map<String, Object> attributes = user.getAttributes();

	        // Optionally save user details to the database

	        return new DefaultOAuth2User(
	                user.getAuthorities(),
	                attributes,
	                "name" // Replace with the key for the name attribute
	        );
	    }
}
