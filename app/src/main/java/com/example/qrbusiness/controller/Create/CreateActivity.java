package com.example.qrbusiness.controller.Create;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.qrbusiness.R;
import com.example.qrbusiness.model.ORModels.QRVcard;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateActivity extends AppCompatActivity implements CreateVcardFragment.Callbacks
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        //  get bundle from intent
        Intent i = getIntent();

        String type = i.getStringExtra("TYPE");

        switch (type)
        {
            case "vcard":
                Fragment fragVcard = new CreateVcardFragment();

                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.createContainer, fragVcard)
                        .commit();
                break;

            case "wifi":
                Fragment fragWifi = new CreateWifiFragment();

                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.createContainer, fragWifi)
                        .commit();
                break;

            case "web":
                Fragment fragWeb = new CreateWebFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.createContainer, fragWeb)
                        .commit();
                break;
        }
    }

    @Override
    public void onBtnCreateVcard(QRVcard vcard)
    {
        Toast.makeText(getApplicationContext(), "created vcard bla", Toast.LENGTH_SHORT).show();

        /*

        this.firstName = b.getString(FIRST_NAME);
            this.lastName = b.getString(LAST_NAME);
            this.email = b.getString(EMAIL);
            this.phoneNum = b.getString(PHONE);

            this.setId(b.getString(QR.ID));
            this.setName(b.getString(QR.NAME));
            this.setQrType(b.getString(QR.QR_TYPE));
            this.setImagePath(b.getString(QR.IMAGE_PATH));
         */

        Map<String, Object> v = new HashMap<>();
        v.put("Name", vcard.getName());
        v.put("Type", vcard.getQrType());
        v.put("First Name", vcard.getFirstName());
        v.put("Last Name", vcard.getLastName());
        v.put("Email", vcard.getEmail());
        v.put("Phone Number", vcard.getPhoneNum());

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("QR_IDs").document()
                .set(v)
                .addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        Toast.makeText(getApplicationContext(), "Vcard added to Library", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(getApplicationContext(), "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}