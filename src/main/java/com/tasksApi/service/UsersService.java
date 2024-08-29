package com.tasksApi.service;

import java.util.ArrayList;
import java.util.Optional;

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
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException
	{
		Users user = usersRepository.findByEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + email);
		}
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				new ArrayList<>());
	}

	public Users findByName(String username)
	{
		Users user = usersRepository.findByEmail(username);
		return user;
	}

	public Optional<Users> findById(Integer userId)
	{
		return usersRepository.findById(userId);
	}
    
	public Users save(UsersRequest user) {
		Users newUser = new Users();
		newUser.setName(user.getName());
		newUser.setEmail(user.getEmail());
		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		return usersRepository.save(newUser);
	}

	public Iterable<Users> findAll()
    {
        return usersRepository.findAll();
	}
}
