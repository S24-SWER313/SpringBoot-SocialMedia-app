package com.project.proo;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class imagevideo {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "post_id")
   private Post post;
   
    private String caption;
      @Lob
    private byte[] size; // Byte array to store image or video content
    private String contentType; // You can use this to store content type (e.g., "image" or "video")
   public static long getContentSize(imagevideo imageVideo) {
    if (imageVideo != null && imageVideo.getSize() != null) {
    
        return imageVideo.getSize().length;
    }
    return 0;
}
//102,400bytes. II
//209,715,200bytes. VV

}
