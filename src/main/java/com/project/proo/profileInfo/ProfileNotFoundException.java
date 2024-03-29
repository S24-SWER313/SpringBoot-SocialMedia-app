package com.project.proo.profileInfo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProfileNotFoundException extends RuntimeException {

    ProfileNotFoundException(Integer id) {
      super("Could not find profile " + id);
    }
  }
