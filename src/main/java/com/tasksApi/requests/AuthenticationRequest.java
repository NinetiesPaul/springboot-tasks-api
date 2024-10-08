package com.tasksApi.requests;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class AuthenticationRequest implements Serializable {
    private static final long serialVersionUID = 5926468583005150707L;

	@Email(message = "Email is invalid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
	@NotBlank(message = "Username is mandatory")
	private String username;

	@NotBlank(message = "Password is mandatory")
	private String password;

	public AuthenticationRequest()
	{
	}

	public AuthenticationRequest(String username, String password)
	{
		this.setUsername(username);
		this.setPassword(password);
	}

	public String getUserName()
	{
		return this.username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return this.password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}
}
