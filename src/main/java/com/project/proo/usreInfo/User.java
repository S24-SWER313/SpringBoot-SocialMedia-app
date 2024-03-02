package com.project.proo.usreInfo;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.proo.postInfo.Comment;
import com.project.proo.postInfo.Like;
import com.project.proo.postInfo.Post;
import com.project.proo.profileInfo.Profile;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;
    private String password;
    private String email;

    @ManyToMany
    @JoinTable(
        name = "user_friends",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    @JsonIgnoreProperties("friends") // Ignore friends to prevent recursion
    private List<User> friends = new ArrayList<>();


    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
   @JsonBackReference
    private Profile profile;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Post> posts;
 
    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<Post> sharedPosts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Like> likes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Comment> comments;
  
}
