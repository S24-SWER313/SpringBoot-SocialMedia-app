package com.project.proo.profileInfo;

import java.time.LocalDate;
import java.time.Period;

import com.project.proo.usreInfo.User;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;
@Data
@Entity
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Bio must not be blank")
    @Size(max = 300, message = "Bio must be at most 300 characters")
    private String bio;

    @NotNull(message = "Date of birth must not be null")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;


    @NotBlank(message = "City must not be blank")
    private String city;

    @NotNull(message = "Gender must not be null")
    private Gender gender;

    @Min(value = 18) 
    @NotNull(message = "Age must not be null")
    private Integer age;
    

    // pic field could be added here
    
    public Profile() {
    }

    

    public Profile(String bio, LocalDate dob, String city, Gender gender,  Integer age) {
        this.bio = bio;
        this.dob = dob;
        this.city = city;
        this.gender = gender;
        this.age = age;
    
    }



    @OneToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;


}
