package com.project.proo.security;

import java.util.Set;

import jakarta.validation.constraints.*;

public class SignupRequest {
  public SignupRequest() {
  }

  @NotBlank
  @Size(min = 3, max = 20,message = "The Size of username mustbe between 3 and 20")
  private String username;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  public SignupRequest(@NotBlank @Size(min = 3, max = 20) String username,
      @NotBlank @Size(max = 50) @Email String email, @NotBlank @Size(min = 6, max = 40) String password) {
    this.username = username;
    this.email = email;
    this.password = password;
  }


  @Size(min = 6, max = 40, message = "The size must be between 6 and 40!")
  @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{6,40}$",
          message = "Password must be strong ")
  private String password;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
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
