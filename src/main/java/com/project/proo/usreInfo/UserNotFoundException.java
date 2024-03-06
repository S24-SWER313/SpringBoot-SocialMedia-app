package com.project.proo.usreInfo;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Integer id) {
      super("Could not find user " + id);
    }
  }