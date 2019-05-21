package com.example.qrbusiness.controller.Details;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.example.qrbusiness.R;
import com.example.qrbusiness.model.ORModels.QR;
import com.example.qrbusiness.model.ORModels.QRVcard;
import com.example.qrbusiness.model.ORModels.QRWeb;
import com.example.qrbusiness.model.ORModels.QRWiFi;

public class DetailsActivity extends AppCompatActivity
        implements
        VcardFragment.Callbacks,
        WebFragment.Callbacks,
        WifiFragment.Callbacks
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

    @Override
    public void onAddToListClicked(QRVcard vcard)
    {

    }

    @Override
    public void onAddToListClicked(QRWeb web)
    {

    }

    @Override
    public void onAddToListClicked(QRWiFi wiFi)
    {

    }
}