package com.project.proo.postInfo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class MyFileNotFoundException extends RuntimeException {

    public MyFileNotFoundException(Integer fileId) {
        super("Could not find file with ID: " + fileId);
    }

 
    public MyFileNotFoundException(String message) {
        super(message);
    }

}

