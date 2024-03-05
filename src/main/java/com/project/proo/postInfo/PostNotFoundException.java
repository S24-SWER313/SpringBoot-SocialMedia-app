package com.project.proo.postInfo;

public class PostNotFoundException extends RuntimeException {

    PostNotFoundException(Integer id) {
      super("Could not find post " + id);
    }
  }