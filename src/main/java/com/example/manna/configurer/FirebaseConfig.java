package com.example.manna.configurer;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Configuration
public class FirebaseConfig {
    @Configuration
    public class FirebaseConfiguration {

        private String firebaseSdkPath = "src/main/resources/mannayo-6dcdb-firebase-adminsdk-3pf5g-6230376f83.json";

        private FirebaseApp firebaseApp;

        @PostConstruct
        public void initializeFCM() throws IOException {
            try {
                FileInputStream serviceAccount = new FileInputStream(firebaseSdkPath);
                FirebaseOptions options = FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();
                FirebaseApp.initializeApp(options);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
