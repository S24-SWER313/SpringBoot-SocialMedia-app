package com.project.proo.postInfo;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
@Entity
@Table(name = "comment_like")
@DiscriminatorValue("comment")
public class CommentLike extends Like {
    @ManyToOne
    @JoinColumn(name = "comment_id")
       //@JsonBackReference
    private Comment comment;
}
