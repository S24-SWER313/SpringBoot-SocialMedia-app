package com.project.proo.usreInfo;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

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
    private List<User> friends;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Profile profile;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts;
 
    @OneToMany(mappedBy = "user")
    private List<Post> sharedPosts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Like> likes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> comments;
  
}
