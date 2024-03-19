package com.project.proo.profileInfo;

public class ProfileNotFoundException extends RuntimeException {

    ProfileNotFoundException(Integer id) {
      super("Could not find profile " + id);
    }
  }
