package com.project.proo.postInfo;

import jakarta.persistence.*;
@Entity
@Table(name = "comment_like")
@DiscriminatorValue("comment")
public class CommentLike extends Like {
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;
}
