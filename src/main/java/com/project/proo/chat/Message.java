package com.project.proo.chat;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;



import lombok.Data;
import java.util.Date;


@Data

public class Message {
    private String senderName;
    private String receiverName;
    private String message;
    private String date;
    private Status status;
}

