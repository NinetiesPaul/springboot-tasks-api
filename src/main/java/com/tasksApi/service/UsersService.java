package com.tasksApi.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tasksApi.model.Users;
import com.tasksApi.repositories.UsersRepository;
import com.tasksApi.requests.UsersRequest;

@Service
public class UsersService implements UserDetailsService {
	
	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private PasswordEncoder bcryptEncoder;

    @Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users user = usersRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				new ArrayList<>());
	}

	public Integer findByName(String username)
	{
		Users user = usersRepository.findByUsername(username);
		return user.getId();
	}
    
	public Users save(UsersRequest user) {
		Users newUser = new Users();
		newUser.setUsername(user.getUsername());
		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		return usersRepository.save(newUser);
	}
}
