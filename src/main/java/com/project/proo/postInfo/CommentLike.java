package com.project.proo.postInfo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
@Table(name = "comment_like")
@DiscriminatorValue("comment")
public class CommentLike extends Like {
    @ManyToOne
    @JoinColumn(name = "comment_id")
       //@JsonBackReference
        @JsonIgnore
    private Comment comment;
}
