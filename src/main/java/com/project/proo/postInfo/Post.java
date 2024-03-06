package com.project.proo.postInfo;

import lombok.Data;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.proo.usreInfo.User;

@Entity
@Data
public class Post {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime date;
    String caption;
    
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
     @JsonManagedReference
    private List <imagevideo> imageVideo;
    
   // boolean isShared=false;
   @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
     @JsonBackReference
    private User user;

 
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<PostLike> likers;

   
 @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
 @JsonManagedReference
    private List<Comment> comments = new ArrayList<>();

 
    public void addComment(User user, String commentContent) {
        Comment comment = new Comment();
       
        comment.setDate(LocalDateTime.now());
        comment.setPost(this); 
        comment.setUser(user);   
        this.comments.add(comment);
    }
    public void sharePost(User user) {
        if (user != null) {
            // Create a new shared post
            Post sharedPost = new Post();
            sharedPost.setDate(LocalDateTime.now());
            sharedPost.setUser(user);
            sharedPost.setLikers(new ArrayList<>()); 
            user.getSharedPosts().add(sharedPost);
        }

}

}