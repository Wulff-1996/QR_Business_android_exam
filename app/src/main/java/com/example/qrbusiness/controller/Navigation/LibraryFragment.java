package com.example.qrbusiness.controller.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LibraryFragment extends Fragment implements
        AbsListView.OnScrollListener,
        AdapterView.OnItemClickListener
{
    private List<QR> QRList;

    private GridView mGridView;
    private GridViewAdapter mGridViewAdapter;
    private ArrayList<GridItem> mGridData;
    private Map<String, String> QRids;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_library, container, false);

        this.mGridView = view.findViewById(R.id.gridView);
        this.mGridData = new ArrayList<>();
        this.mGridViewAdapter = new GridViewAdapter(getActivity(), R.layout.grid_item, this.mGridData);
        this.QRList = new ArrayList<>();
        this.QRids = new HashMap<>();

        mGridView.setAdapter(mGridViewAdapter);
        mGridView.setOnScrollListener(this);
        mGridView.setOnItemClickListener(this);
        getQrCodes();

        return view;
    }

    private void getQrCodes()
    {
        // Create a storage reference from our app
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        final HashMap<String, String> QRIds = new HashMap<>();

        fetchQRObjects(fs);
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
                                            String name = document.getString("Name");
                                            String imageLocation = document.getString("Image Location");

                                            //  map to object
                                            QRWeb web = new QRWeb();
                                            web.setId(id);
                                            web.setImagePath(imageLocation);
                                            web.setName(name);
                                            web.setUrl(document.getString("URL"));
                                            QRList.add(web);

                                            //  map to griditem
                                            GridItem gridItem = new GridItem();
                                            gridItem.setImage(imageLocation);
                                            gridItem.setName(name);
                                            gridItem.setIcon(getResources().getDrawable(R.drawable.ic_url));
                                            mGridData.add(gridItem);

                                            mGridViewAdapter.setGridData(mGridData);
                                            break;

                                        case "wifi":
                                            id = document.getString("QRID");
                                            name = document.getString("Name");
                                            imageLocation = document.getString("Image Location");

                                            //  map to object
                                            QRWiFi wifi = new QRWiFi();
                                            wifi.setId(document.getString("QRID"));
                                            wifi.setImagePath(imageLocation);
                                            wifi.setName(document.getString("Name"));
                                            wifi.setWifiName(document.getString("WiFi Name"));
                                            wifi.setPassword(document.getString("Password"));
                                            wifi.setNetType(document.getString("Network Type"));
                                            QRList.add(wifi);

                                            //  map to griditem
                                            gridItem = new GridItem();
                                            gridItem.setImage(imageLocation);
                                            gridItem.setName(name);
                                            gridItem.setIcon(getResources().getDrawable(R.drawable.ic_wifi));
                                            mGridData.add(gridItem);
                                            mGridViewAdapter.setGridData(mGridData);
                                            break;

                                        case "vcard":
                                            id = document.getString("QRID");
                                            name = document.getString("Name");
                                            imageLocation = document.getString("Image Location");

                                            //  map to object
                                            QRVcard vcard = new QRVcard();
                                            vcard.setId(document.getString("QRID"));
                                            vcard.setImagePath(imageLocation);
                                            vcard.setName(document.getString("Name"));
                                            vcard.setFirstName(document.getString("First Name"));
                                            vcard.setLastName(document.getString("Last Name"));
                                            vcard.setPhoneNum(document.getString("Phone Number"));
                                            vcard.setEmail(document.getString("Email"));
                                            QRList.add(vcard);

                                            //  map to griditem
                                            gridItem = new GridItem();
                                            gridItem.setImage(imageLocation);
                                            gridItem.setName(name);
                                            gridItem.setIcon(getResources().getDrawable(R.drawable.ic_vcard));
                                            mGridData.add(gridItem);
                                            mGridViewAdapter.setGridData(mGridData);
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

        Intent i = new Intent(getActivity(), DetailsActivity.class);
        i.putExtra("BUNDLE", QRList.get(position).toBundle());
        startActivityForResult(i, 1);
    }
}
