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

import com.example.qrbusiness.R;
import com.example.qrbusiness.controller.Details.DetailsActivity;
import com.example.qrbusiness.model.GridItem;
import com.example.qrbusiness.model.GridViewAdapter;
import com.example.qrbusiness.model.ORModels.QR;
import com.example.qrbusiness.model.ORModels.QRVcard;
import com.example.qrbusiness.model.ORModels.QRWeb;
import com.example.qrbusiness.model.ORModels.QRWiFi;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LibraryActivity extends AppCompatActivity
        implements
        AbsListView.OnScrollListener,
        AdapterView.OnItemClickListener
{

    private GridView mGridView;
    private GridViewAdapter mGridViewAdapter;
    private ArrayList<GridItem> mGridData;
    private List<QR> QRList;
    private Map<String, String> QRids;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        init();
    }

    private void init()
    {
        this.mGridView = findViewById(R.id.gridView);
        this.mGridData = new ArrayList<>();
        this.mGridViewAdapter = new GridViewAdapter(this, R.layout.grid_item, this.mGridData);
        this.QRList = new ArrayList<>();
        this.QRids = new HashMap<>();

        mGridView.setAdapter(mGridViewAdapter);
        mGridView.setOnScrollListener(this);
        mGridView.setOnItemClickListener(this);
        getQrCodes();
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
        Intent i = new Intent(this, DetailsActivity.class);
        i.putExtra("BUNDLE", QRList.get(position).toBundle());
        startActivityForResult(i, 1);
    }

    public void signOut(View v)
    {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void createQR(View v)
    {
        /*
    }
        AlertDialog.Builder alertBox = new AlertDialog.Builder(this);
        alertBox.setTitle("Create QR");
        alertBox.setMessage("Select QR type");

        alertBox.setNeutralButton("Contact", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Intent i = new Intent(getApplicationContext(), CreateQRActivity.class);
                        i.putExtra("TYPE", "vcard");
                        startActivity(i);
                    }
                }).show();

        alertBox.setNeutralButton("Url", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Intent i = new Intent(getApplicationContext(), CreateQRActivity.class);
                        i.putExtra("TYPE", "web");
                        startActivity(i);
                    }
                }).create().show();

        alertBox.setNeutralButton("WiFi", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Intent i = new Intent(getApplicationContext(), CreateQRActivity.class);
                        i.putExtra("TYPE", "wifi");
                        startActivity(i);
                    }
                }).create().show();
        alertBox.setNegativeButton("Cancel", null);

        alertBox.setIcon(android.R.drawable.ic_dialog_alert);
        alertBox.show();
        */
    }

    private void getQrCodes()
    {
        // Create a storage reference from our app
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        final HashMap<String, String> QRIds = new HashMap<>();

        fetchQRImages(fs);
    }

    private void fetchQRImages(final FirebaseFirestore fs)
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
                                QRids.put(document.getString("QRID"), "");
                            }

                            final int[] i = {0};
                            //Gets all the images from database with relevant assosiation
                            for (String id: QRids.keySet())
                            {
                                final String QRID = id;
                                try
                                {
                                    storageRef.child("QR_Images/" + id + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                                    {
                                        @Override
                                        public void onSuccess(Uri uri)
                                        {
                                            QRids.put(QRID, uri.toString());
                                            GridItem gridItem = new GridItem();
                                            gridItem.setImage(uri.toString());
                                            QRids.put(QRID, uri.toString());
                                            mGridData.add(gridItem);

                                            mGridViewAdapter.setGridData(mGridData);

                                            if (i[0] == QRids.size() -1)
                                            {
                                                fetchQRObjects(fs);
                                            }
                                            i[0]++;
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

    private void fetchQRObjects(FirebaseFirestore fs)
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
                                            String id = document.getString("QRID");
                                            QRWeb web = new QRWeb();
                                            web.setId(id);
                                            web.setImagePath(QRids.get(id));
                                            web.setName(document.getString("Name"));
                                            web.setUrl(document.getString("URL"));
                                            QRList.add(web);
                                            break;

                                        case "wifi":
                                            QRWiFi wifi = new QRWiFi();
                                            wifi.setId(document.getString("QRID"));
                                            wifi.setImagePath(QRids.get(document.getString("QRID")));
                                            wifi.setName(document.getString("Name"));
                                            wifi.setWifiName(document.getString("WiFi Name"));
                                            wifi.setPassword(document.getString("Password"));
                                            wifi.setNetType(document.getString("Network Type"));
                                            QRList.add(wifi);
                                            break;

                                        case "vcard":
                                            QRVcard vcard = new QRVcard();
                                            vcard.setId(document.getString("QRID"));
                                            vcard.setImagePath(QRids.get(document.getString("QRID")));
                                            vcard.setName(document.getString("Name"));
                                            vcard.setFirstName(document.getString("First Name"));
                                            vcard.setLastName(document.getString("Last Name"));
                                            vcard.setPhoneNum(document.getString("Phone Number"));
                                            vcard.setEmail(document.getString("Email"));
                                            QRList.add(vcard);
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


