package com.tasksApi.requests;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UsersRequest implements Serializable {
    private static final long serialVersionUID = 5926468583005150707L;

	@Email(message = "INVALID_EMAIL", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
	@NotEmpty(message = "EMAIL_NOT_STRING")
	@NotNull(message = "MISSING_EMAIL")
	@NotBlank(message = "EMPTY_EMAIL")
	private String email;

	@NotEmpty(message = "NAME_NOT_STRING")
	@NotNull(message = "MISSING_NAME")
	@NotBlank( message = "EMPTY_NAME")
	private String name;

	@NotEmpty(message = "PASSWORD_NOT_STRING")
	@NotNull(message = "MISSING_PASSWORD")
	@NotBlank(message = "EMPTY_PASSWORD")
	private String password;

	public UsersRequest()
	{
	}

	public UsersRequest(String name, String email, String password)
	{
		this.setName(name);
		this.setEmail(email);
		this.setPassword(password);
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getEmail()
	{
		return this.email;
	}

	public void setEmail(String email)
	{
		this.email = email;
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
