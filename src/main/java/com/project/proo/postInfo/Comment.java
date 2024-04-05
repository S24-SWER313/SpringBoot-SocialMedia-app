package com.project.proo.postInfo;

import java.time.LocalDateTime;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonIgnore;

import com.project.proo.usreInfo.User;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    LocalDateTime date;
    @Size(max = 300,message = "The comment is too long")
    String commentContent;

    public Comment(String commentContent) {
        this.commentContent = commentContent;
    }

    public Comment() {
    }

    @ManyToOne(optional = false) // Make the association mandatory
    @JoinColumn(name = "post_id")
     @JsonIgnore
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
    
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CommentLike> likes;

    
}




 
