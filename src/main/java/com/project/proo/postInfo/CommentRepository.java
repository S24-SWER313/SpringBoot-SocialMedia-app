package com.project.proo.postInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

import com.project.proo.postInfo.Post;


public interface CommentRepository extends JpaRepository<Comment, Long> {


    
}
