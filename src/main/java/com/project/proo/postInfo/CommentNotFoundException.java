package com.project.proo.postInfo;

public class CommentNotFoundException extends RuntimeException {

    CommentNotFoundException(Integer id) {
      super("Could not find post " + id);
    }
  }