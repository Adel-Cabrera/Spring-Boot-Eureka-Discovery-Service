package com.darkonnen.photoapp.api.users.ui.models;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreateUserRequestModel {
	
	@NotNull(message="Cannot be null")
	@Size(min=2, message="Greater than 2")
	private String firstName;
	
	@NotNull(message="Cannot be null")
	@Size(min=2, message="Greater than 2")
	private String lastName;
	
	@NotNull(message="Cannot be null")
	@Email
	private String email;
	
	@NotNull(message="Cannot be null")
	@Size(min=8, max=16, message="Greater than 8, less than 16")
	private String password;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
