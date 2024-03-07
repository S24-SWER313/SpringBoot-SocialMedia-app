package com.project.proo.postInfo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findByUserId(Integer userId);
    List<Post> findByUser_SharedPosts_User_Id(Integer userId);
}
    

