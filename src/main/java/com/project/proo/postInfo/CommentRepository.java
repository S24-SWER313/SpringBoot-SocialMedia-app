package com.project.proo.postInfo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByPost_Id(Integer postId);



    
}
