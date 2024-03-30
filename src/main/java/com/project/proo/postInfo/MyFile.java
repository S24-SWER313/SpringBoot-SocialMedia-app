package com.project.proo.postInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class MyFile {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @JsonIgnore
    private Post post;

    private String type;

    private String name;
   
    
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    @Size(max = 10485760, message = "Image or video size must not exceed 10 MB")
    private byte[] size;  // Byte array to store image or video content

    public MyFile() {
    }

    public MyFile(String name ,Post post, String type,@Size(max = 10485760, message = "Image or video size must not exceed 10 MB") byte[] size) {
        this.post = post;
        this.type = type;
        this.size = size;
        this.name = name;
    }


  
}

