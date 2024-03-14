package com.project.proo.postInfo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Integer> {

    void deleteByComment_Id(Integer commentId);
    // Define custom queries or methods if needed
}
