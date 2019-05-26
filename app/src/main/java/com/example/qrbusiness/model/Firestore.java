package com.example.qrbusiness.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Firestore
{
    public static FirebaseFirestore getInstance()
    {
        return FirebaseFirestore.getInstance();
    }

    public static FirebaseUser getCurrentUser()
    {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static CollectionReference getCurrentUsersQrCollection()
    {
        return getInstance().collection("users").document(getCurrentUser().getEmail()).collection("QR_IDs");
    }

    public static FirebaseAuth getFirebaseAuth()
    {
        return FirebaseAuth.getInstance();
    }
}
