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
                System.out.println("시작합니다 " + firebaseSdkPath);
                FileInputStream serviceAccount = new FileInputStream(firebaseSdkPath);
                System.out.println("파일 : " + serviceAccount);
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
