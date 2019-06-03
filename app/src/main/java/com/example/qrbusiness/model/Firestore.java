package com.example.qrbusiness.model;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

    public static void deleteEntry(String id, final Activity activity)
    {
        Firestore.getCurrentUsersQrCollection().document(id)
                .delete().
                addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        activity.finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(activity, "Delete unsuccessful", Toast.LENGTH_SHORT);
                    }
                });

    }
}
