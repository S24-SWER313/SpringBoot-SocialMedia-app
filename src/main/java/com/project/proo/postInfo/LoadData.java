package com.project.proo.postInfo;

import com.project.proo.postInfo.Post;
import com.project.proo.postInfo.PostRepository;
import com.project.proo.usreInfo.User;
import com.project.proo.usreInfo.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
class LoadData {

    private static final Logger log = LoggerFactory.getLogger(LoadData.class);

    @Bean
    CommandLineRunner initDatabase(PostRepository postRepository, UserRepository userRepository) {
        return args -> {
            // Create some dummy users
            User user1 = new User();
            user1.setUsername("user1");
            user1.setPassword("password1");
            user1.setEmail("user1@example.com");

            User user2 = new User();
            user2.setUsername("user2");
            user2.setPassword("password2");
            user2.setEmail("user2@example.com");

            // Establish friendship between user1 and user2
           
            user1.getFriends().add(user2);
            userRepository.save(user1);
            user2.getFriends().add(user1);

        
            userRepository.save(user2);

            // Create a post and associate it with user1
            Post post = new Post();
            post.setDate(LocalDateTime.now());
            post.setCaption("Sample caption");
            post.setUser(user1);

            // Save the post to the database
            postRepository.save(post);
            log.info("Preloading users and establishing friendship: user1, user2");
         log.info("Preloading post into the database: " + post);
        };
    }
}
