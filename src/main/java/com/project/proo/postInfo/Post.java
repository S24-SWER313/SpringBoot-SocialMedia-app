package com.project.proo.postInfo;

import lombok.Data;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.proo.Hashtagee.Hashtag;
import com.project.proo.usreInfo.User;


@Entity
@Data
public class Post {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
   // private List<Post> sharedPosts;
   
   @OneToMany(mappedBy = "originalPost", cascade = CascadeType.ALL)
   @JsonIgnore
   private List<Post> sharedPosts;

   @ManyToOne
   @JoinColumn(name = "original_post_id")
   @JsonIgnore
   private Post originalPost;

    private Privacy audiance;
    private LocalDateTime date;
    private boolean isShared;
    @Size(max = 750,message = "The caption is too long")
    private String caption;
     @Transient // This annotation ensures that the field is not persisted to the database
    private MultipartFile image; // MultipartFile field to handle image upload

    private String imageUrl;

    @Transient
    private MultipartFile video; // Field to handle video uploads

    private String videoUrl; 

    public Post(Privacy audiance, LocalDateTime date, String caption) {
      this.audiance = audiance;
        this.date = date;
        this.caption = caption;
        this.isShared = false;
    }

    public Post(@Size(max = 750, message = "The caption is too long") String caption,Privacy audiance, String imageUrl, String videoUrl) {
        this.caption = caption;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
        this.date = LocalDateTime.now();
        this.audiance = audiance;
    }


    public Post() {
    }
    

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @JsonIgnore
    private List <MyFile> files;


   // boolean isShared=false;
    @ManyToOne(optional = false)
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

    
    @ManyToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinTable(name = "post_hashtags",
               joinColumns = @JoinColumn(name = "post_id"),
               inverseJoinColumns = @JoinColumn(name = "hashtag_id"))
    private Set<Hashtag> hashtags = new HashSet<>();
 
    public void addComment(User user, String commentContent) {
        Comment comment = new Comment();
       
        comment.setDate(LocalDateTime.now());
        comment.setPost(this); 
        comment.setUser(user);   
        this.comments.add(comment);
    }

    

}