package com.project.proo.postInfo;

public class PostLikeNotFoundException extends RuntimeException {

    PostLikeNotFoundException(Integer id) {
      super("Could not find Like " + id);
    }
}