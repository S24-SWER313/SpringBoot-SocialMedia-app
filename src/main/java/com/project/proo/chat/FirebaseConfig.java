package com.project.proo.chat;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp initializeFirebase() throws IOException {
        ClassPathResource serviceAccountResource = new ClassPathResource("serviceAccountKey.json");

        if (!serviceAccountResource.exists()) {
            throw new IllegalArgumentException("Service account key file not found");
        }

        try (InputStream serviceAccount = serviceAccountResource.getInputStream()) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://trek-link-default-rtdb.firebaseio.com")
                    .build();

            return FirebaseApp.initializeApp(options);
        }
    }
}
