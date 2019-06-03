package com.example.qrbusiness.controller.Details;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.example.qrbusiness.R;
import com.example.qrbusiness.model.ORModels.QR;

public class DetailsActivity extends AppCompatActivity
{
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //  get bundle from intent
        Intent i = getIntent();
        this.bundle = i.getBundleExtra("BUNDLE");

        String type = bundle.getString(QR.QR_TYPE);

        switch (type)
        {
            case "vcard":
                Fragment fragVcard = new VcardFragment();

                fragVcard.setArguments(bundle);
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.detailsContainer, fragVcard)
                        .commit();
                break;

            case "wifi":
                Fragment fragWifi = new WifiFragment();

                fragWifi.setArguments(bundle);
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.detailsContainer, fragWifi)
                        .commit();
                break;

            case "web":
                Fragment fragWeb = new WebFragment();
                fragWeb.setArguments(bundle);
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.detailsContainer, fragWeb)
                        .commit();
                break;
        }
    }
}