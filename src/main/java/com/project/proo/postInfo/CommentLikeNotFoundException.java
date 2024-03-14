package com.project.proo.postInfo;

public class CommentLikeNotFoundException extends RuntimeException {

    CommentLikeNotFoundException(Integer id) {
      super("Could not find Like " + id);
    }

}