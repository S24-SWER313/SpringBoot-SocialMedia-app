package com.project.proo.postInfo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PostNotFoundException extends RuntimeException {

    PostNotFoundException(Integer id) {
      super("Could not find post " + id);
    }
  }