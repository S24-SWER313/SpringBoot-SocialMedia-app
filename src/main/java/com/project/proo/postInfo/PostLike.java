package com.project.proo.postInfo;


import jakarta.persistence.*;

@Entity
@Table(name = "post_like")
@DiscriminatorValue("post")
public class PostLike extends Like {
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
