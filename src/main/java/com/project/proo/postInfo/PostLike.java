package com.project.proo.postInfo;


import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
@Table(name = "post_like")
@DiscriminatorValue("post")
public class PostLike extends Like {
    @ManyToOne
    @JoinColumn(name = "post_id")
       //@JsonBackReference
    private Post post;
}
