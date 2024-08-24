package com.tasksApi.requests;

import java.io.Serializable;
public class UsersRequest implements Serializable {
    private static final long serialVersionUID = 5926468583005150707L;

	private String email;

	private String name;

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
