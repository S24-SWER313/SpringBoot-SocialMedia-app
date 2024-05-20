package com.project.proo.postInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Integer> {

    void deleteByComment_Id(Integer commentId);

    void deleteByIdAndComment_Id(Integer likeId, Integer commentId);
    // Define custom queries or methods if needed
    @Modifying
    @Transactional
    @Query("DELETE FROM CommentLike cl WHERE cl.user.id = :userId AND cl.comment.id = :commentId")
    void deleteByUserIdAndCommentId(@Param("userId") Integer userId, @Param("commentId") Integer commentId);
    
    
}


