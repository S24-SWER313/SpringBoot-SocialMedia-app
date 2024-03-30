package com.project.proo.postInfo;

import lombok.Data;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.proo.usreInfo.User;


@Entity
@Data
public class Post {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Privacy audiance;
    private LocalDateTime date;

    @Size(max = 750,message = "The caption is too long")
    private String caption;

    public Post(Privacy audiance, LocalDateTime date, String caption) {
      this.audiance = audiance;
        this.date = date;
        this.caption = caption;
    }


    public Post() {
    }
    

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @JsonIgnore
    private List <MyFile> files;


   // boolean isShared=false;
    @ManyToOne(optional = false,cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

 
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    //@JsonManagedReference
    @JsonIgnore
    private List<PostLike> likers;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @JsonIgnore
   // @JsonManagedReference
    private List<Comment> comments = new ArrayList<>();

    
 
    public void addComment(User user, String commentContent) {
        Comment comment = new Comment();
       
        comment.setDate(LocalDateTime.now());
        comment.setPost(this); 
        comment.setUser(user);   
        this.comments.add(comment);
    }

    

}