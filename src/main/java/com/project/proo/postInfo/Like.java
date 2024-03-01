package com.project.proo.postInfo;

import java.util.List;

import com.project.proo.usreInfo.User;

import jakarta.persistence.*;

@Entity
@Table(name = "user_like")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "like_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    Boolean liked;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private  User user; 
}
