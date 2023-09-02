package com.tasksApi.requests;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

public class UsersRequest implements Serializable {
    private static final long serialVersionUID = 5926468583005150707L;

	@NotBlank(message = "Username is mandatory")
	private String username;

	@NotBlank(message = "Password is mandatory")
	private String password;

	public UsersRequest()
	{
	}

	public UsersRequest(String username, String password) {
		this.setUsername(username);
		this.setPassword(password);
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
