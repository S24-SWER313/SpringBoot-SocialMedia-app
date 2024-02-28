package com.project.proo;

import jakarta.persistence.*;
@Entity
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String username;
    private String bio;

    // pic field could be added here
    
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    // getters and setters
}
