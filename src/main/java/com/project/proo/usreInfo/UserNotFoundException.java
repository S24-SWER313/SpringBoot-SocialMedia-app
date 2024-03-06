package com.project.proo.usreInfo;

public class UserNotFoundException extends RuntimeException {

    UserNotFoundException(Integer id) {
      super("Could not find user " + id);
    }
  }