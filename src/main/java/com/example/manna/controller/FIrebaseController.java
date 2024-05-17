package com.example.manna.controller;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.ExportedUserRecord;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ListUsersPage;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/firebase")
public class FIrebaseController {
    @GetMapping("/deleteAllFirebaseUser")
    public String getAllFirebaseUser() throws Exception {
        Firestore db = FirestoreClient.getFirestore();
        ListUsersPage page = FirebaseAuth.getInstance().listUsers(null);
        while (page != null) {
            for (ExportedUserRecord user : page.getValues()) {
                String uid = user.getUid();
                ApiFuture<WriteResult> writeResult = db.collection("user").document(uid).delete();
                FirebaseAuth.getInstance().deleteUser(uid);
            }
            page = page.getNextPage();
        }
        return "Success";
    }
}
