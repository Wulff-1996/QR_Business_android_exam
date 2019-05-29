package com.example.qrbusiness.controller.Create;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.example.qrbusiness.R;

public class CreateActivity extends AppCompatActivity
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
}