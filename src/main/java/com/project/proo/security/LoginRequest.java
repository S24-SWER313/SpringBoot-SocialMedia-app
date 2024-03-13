package com.project.proo.security;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
	@NotBlank
  private String username;

	@NotBlank
	private String password;

	public String getUsername() {
		return username;
	}

	public LoginRequest(@NotBlank String username, @NotBlank String password) {
		this.username = username;
		this.password = password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}