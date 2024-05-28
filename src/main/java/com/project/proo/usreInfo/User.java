package com.project.proo.usreInfo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @NotBlank
    @Size(min = 3, max = 20,message = "The Size of username mustbe between 3 and 20")
    private String username;
    
//     @Size(min = 6, max = 40, message = "The size must be between 6 and 40!")
//   @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{6,40}$",
//           message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, one special character, and be 6-40 characters long!")
  private String password;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

    public User(){}

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @ManyToMany
    @JoinTable(
        name = "user_friends",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    @JsonIgnore
    private List<User> friends = new ArrayList<>();


    @OneToOne( mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Profile profile;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Post> posts;
 
    // @OneToMany(mappedBy = "user")
    // @JsonIgnore
    // private List<Post> sharedPosts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Like> likes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Comment> comments;
  
}

