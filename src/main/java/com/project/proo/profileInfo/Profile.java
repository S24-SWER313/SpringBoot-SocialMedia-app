package com.project.proo.profileInfo;

import java.time.LocalDate;

import com.project.proo.usreInfo.User;

import jakarta.persistence.*;
import lombok.Data;
@Data
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
    
    public Profile() {
    }

    

    public Profile(String bio, LocalDate dob, String city, Gender gender) {
        this.bio = bio;
        this.dob = dob;
        this.city = city;
        this.gender = gender;
    }



    @OneToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    // getters and setters
}
