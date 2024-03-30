// package com.project.proo.fileInfo;


// import org.hibernate.annotations.GenericGenerator;

// import com.fasterxml.jackson.annotation.JsonBackReference;
// import com.fasterxml.jackson.annotation.JsonIgnore;
// import com.project.proo.postInfo.Post;

// import jakarta.persistence.*;
// import jakarta.validation.constraints.Size;
// import lombok.Data;

// @Entity
// @Data
// public class FileDB {


  
//     // @GeneratedValue(strategy = GenerationType.IDENTITY)
//     // private Integer id;

//     @Id
//     @GeneratedValue(generator = "uuid")
//     @GenericGenerator(name = "uuid", strategy = "uuid2")
//     private String id;

//     @ManyToOne
//     @JoinColumn(name = "post_id")
//     @JsonIgnore
//     private Post post;

//     private String type;

//     private String name;
   
    
//     @Lob
//     private byte[] size;  // Byte array to store image or video content


//     public FileDB() {
//     }


//     public FileDB(String name ,Post post, String type,@Size(max = 10485760, message = "Image or video size must not exceed 10 MB") byte[] size) {
//         this.post = post;
//         this.type = type;
//         this.size = size;
//         this.name = name;
//     }


  
// }


