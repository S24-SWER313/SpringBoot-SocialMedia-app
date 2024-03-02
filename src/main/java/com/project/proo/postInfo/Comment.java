package com.project.proo.postInfo;

import java.time.LocalDateTime;
import java.util.List;

import com.project.proo.usreInfo.User;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    LocalDateTime date;
    String commentContent;

    @ManyToOne(optional = false) // Make the association mandatory
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private List<CommentLike> likes;

    
}




 
