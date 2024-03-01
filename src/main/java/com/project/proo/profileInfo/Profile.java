package com.project.proo.profileInfo;

import java.time.LocalDate;

import com.project.proo.usreInfo.User;

import jakarta.persistence.*;
@Entity
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String bio;
    private LocalDate dob;
    private String city;
    private Gender gender;
    

    // pic field could be added here
    
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    // getters and setters
}
