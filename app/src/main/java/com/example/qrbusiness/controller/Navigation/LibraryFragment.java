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
import com.example.qrbusiness.model.Firestore;
import com.example.qrbusiness.model.GridItem;
import com.example.qrbusiness.model.GridViewAdapter;
import com.example.qrbusiness.model.ORModels.QR;
import com.example.qrbusiness.model.ORModels.QRVcard;
import com.example.qrbusiness.model.ORModels.QRWeb;
import com.example.qrbusiness.model.ORModels.QRWiFi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class LibraryFragment extends Fragment implements
        AbsListView.OnScrollListener,
        AdapterView.OnItemClickListener
{
    private List<QR> QRList;
    private GridView mGridView;
    private GridViewAdapter mGridViewAdapter;
    private ArrayList<GridItem> mGridData;

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

        this.mGridView = view.findViewById(R.id.library_gridView);
        this.mGridData = new ArrayList<>();
        this.mGridViewAdapter = new GridViewAdapter(getActivity(), R.layout.grid_item, this.mGridData);
        this.QRList = new ArrayList<>();

        mGridView.setAdapter(mGridViewAdapter);
        mGridView.setOnScrollListener(this);
        mGridView.setOnItemClickListener(this);
        fetchQRObjects();

        return view;
    }

    private void fetchQRObjects()
    {
        Firestore.getCurrentUsersQrCollection()
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
                                    switch (document.getString("qrType"))
                                    {
                                        case "web":
                                            QRWeb web = document.toObject(QRWeb.class);
                                            QRList.add(web);

                                            //  map to griditem
                                            GridItem gridItem = new GridItem();
                                            gridItem.setImage(web.getImagePath());
                                            gridItem.setName(web.getName());
                                            gridItem.setIcon(getResources().getDrawable(R.drawable.ic_url));
                                            mGridData.add(gridItem);

                                            mGridViewAdapter.setGridData(mGridData);
                                            break;

                                        case "wifi":
                                            QRWiFi wifi = document.toObject(QRWiFi.class);
                                            QRList.add(wifi);

                                            //  map to griditem
                                            gridItem = new GridItem();
                                            gridItem.setImage(wifi.getImagePath());
                                            gridItem.setName(wifi.getName());
                                            gridItem.setIcon(getResources().getDrawable(R.drawable.ic_wifi));
                                            mGridData.add(gridItem);
                                            mGridViewAdapter.setGridData(mGridData);
                                            break;

                                        case "vcard":
                                            QRVcard vcard = document.toObject(QRVcard.class);
                                            QRList.add(vcard);

                                            //  map to griditem
                                            gridItem = new GridItem();
                                            gridItem.setImage(vcard.getImagePath());
                                            gridItem.setName(vcard.getName());
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
