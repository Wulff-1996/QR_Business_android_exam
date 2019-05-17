package com.example.qrbusiness.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.qrbusiness.GridViewAdapter;
import com.example.qrbusiness.R;
import com.example.qrbusiness.model.GridItem;
import com.example.qrbusiness.model.QR;
import com.example.qrbusiness.model.QRVcard;
import com.example.qrbusiness.model.QRWeb;
import com.example.qrbusiness.model.QRWiFi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class LibraryActivity extends AppCompatActivity
        implements
        AbsListView.OnScrollListener,
        AdapterView.OnItemClickListener
{

    private GridView mGridView;
    private GridViewAdapter mGridViewAdapter;
    private ArrayList<GridItem> mGridData;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        init();

        fetchData();
    }

    private void init()
    {
        this.mGridView = findViewById(R.id.gridView);
        this.mGridData = new ArrayList<>();
        this.mGridViewAdapter = new GridViewAdapter(this, R.layout.grid_item, this.mGridData);

        mGridView.setAdapter(mGridViewAdapter);
        mGridView.setOnScrollListener(this);
        mGridView.setOnItemClickListener(this);
        getQrCodes();
    }


    private void fetchData()
    {

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState)
    {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {

    }

    public void signOut(View v)
    {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void getQrCodes()
    {
        // Create a storage reference from our app
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        final List<String> QRIds = new ArrayList<>();
        final HashSet<QR> QRList = new HashSet<>();

        fetchQRImages(fs, QRIds);
        fetchQRObjects(fs, QRIds, QRList);
    }

    private void fetchQRImages(FirebaseFirestore fs, final List<String> QRIds)
    {
        final StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        fs.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("QR_IDs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if (task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                QRIds.add(document.getString("QRID"));
                            }

                            //Gets all the images from database with relevant assosiation
                            for (String id:QRIds)
                            {
                                try
                                {
                                    storageRef.child("QR_Images/" + id + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                                    {
                                        @Override
                                        public void onSuccess(Uri uri)
                                        {
                                            GridItem gridItem = new GridItem();
                                            gridItem.setImage(uri.toString());
                                            mGridData.add(gridItem);

                                            mGridViewAdapter.setGridData(mGridData);
                                            //Picasso.with(getApplicationContext()).load(uri.toString()).into();
                                        }
                                    }).addOnFailureListener(new OnFailureListener()
                                    {
                                        @Override
                                        public void onFailure(@NonNull Exception exception)
                                        {
                                            // Handle any errors add logger error
                                        }
                                    });
                                }
                                //Bad practice, but android studio does not recognize correct error: StorageException
                                catch (Exception e)
                                {
                                    //Tilføj til log
                                }
                            }
                        }
                        if (!task.isSuccessful())
                        {
                            //Indsæt log error
                        }
                    }
                });
    }

    private void fetchQRObjects(FirebaseFirestore fs, List<String> qrIds, final HashSet<QR> QRList)
    {
        fs.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("QR_IDs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                if (task.isSuccessful())
                {
                    for (QueryDocumentSnapshot document : task.getResult())
                    {
                        try
                        {
                            switch (document.getString("Type"))
                            {
                                case "web":
                                    QRList.add(new QRWeb(document.getString("QRID"),
                                            document.getString("Name"), document.getString("URL")));
                                    break;
                                case "wifi":
                                    QRList.add(new QRWiFi(document.getString("QRID"),
                                            document.getString("Name"), document.getString("WiFi Name"),
                                            document.getString("Password"), document.getString("Network Type")));
                                    break;
                                case "vcard":
                                    QRList.add(new QRVcard(document.getString("QRID"),
                                            document.getString("Name"), document.getString("First Name"),
                                            document.getString("Last Name"), document.getString("Phone Number"), document.getString("Email")));
                                    break;
                            }
                        }
                        catch (NullPointerException NPE)
                        {
                        }

                    }
                }
                if (!task.isSuccessful()) {
                    //Indsæt log error
                }
            }
        });
    }
}


