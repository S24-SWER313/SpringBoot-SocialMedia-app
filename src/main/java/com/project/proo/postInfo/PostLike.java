package com.project.proo.postInfo;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
@Table(name = "post_like")
@DiscriminatorValue("post")
public class PostLike extends Like {
    @ManyToOne(optional = false) 
    @JoinColumn(name = "post_id")
       //@JsonBackReference
        @JsonIgnore
    private Post post;
}
