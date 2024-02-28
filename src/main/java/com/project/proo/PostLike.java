package com.project.proo;


import jakarta.persistence.*;

@Entity
@Table(name = "post_like")
@DiscriminatorValue("post")
public class PostLike extends Like {
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
