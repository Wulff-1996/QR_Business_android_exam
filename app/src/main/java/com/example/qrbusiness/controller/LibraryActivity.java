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
import java.util.List;

public class LibraryActivity extends AppCompatActivity
        implements
        AbsListView.OnScrollListener,
        AdapterView.OnItemClickListener
{

    private GridView mGridView;
    private GridViewAdapter mGridViewAdapter;
    private ArrayList<GridItem> mGridData;
    List<String> QRIds;

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
        this.QRIds = new ArrayList<>();
        getImages();
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

    private void getImages()
    {
        // Create a storage reference from our app
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        //For testing purposes
        List<String> imageIds = new ArrayList<>();
        fetchQrIds();

        imageIds.add("wifi123");
        imageIds.add("web123");

        for (String id:imageIds)
        {
            storageRef.child("QR_Images/" + id + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors add logger error
                }
            });
        }
    }
    private void fetchQrIds()
    {
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        fs.collection("users").document("mikkel@mail.com").collection("QR_IDs")
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
                        }
                    }
                });
    }
}
