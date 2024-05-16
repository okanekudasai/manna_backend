package com.example.manna.configurer;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.FileNotFoundException;
import java.io.IOException;

@Configuration
public class FirebaseConfig {
    @Configuration
    public class FirebaseConfiguration {

        private String firebaseSdkPath = "mannayo-6dcdb-firebase-adminsdk-3pf5g-6230376f83.json";

        private FirebaseApp firebaseApp;

        @PostConstruct
        public void initializeFCM() throws IOException {
            try {
                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(
                                new ClassPathResource(firebaseSdkPath).getInputStream()))
                        .build();
                if (FirebaseApp.getApps().isEmpty()) {
                    FirebaseApp.initializeApp(options);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
