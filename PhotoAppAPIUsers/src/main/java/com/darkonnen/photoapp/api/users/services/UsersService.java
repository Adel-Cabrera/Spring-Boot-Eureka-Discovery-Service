package com.darkonnen.photoapp.api.users.services;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.darkonnen.photoapp.api.users.shared.UserDto;

public interface UsersService extends UserDetailsService{
	
	UserDto createUser(UserDto userDetails);
	
	UserDto getUserDetailsByEmail(String email);

}
