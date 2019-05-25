package com.example.qrbusiness.controller.Details;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qrbusiness.R;
import com.example.qrbusiness.model.ORModels.QRWeb;
import com.squareup.picasso.Picasso;

public class WebFragment extends Fragment
{
    private WebFragment.Callbacks detailsActivity;
    private QRWeb web;

    private TextView tvName;
    private TextView tvType;
    private TextView tvUrl;
    private ImageView poster;

    public WebFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Bundle b = getArguments();
        if (b != null && b.containsKey(web.URL))
        {
            web = new QRWeb(b);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.details_web, container, false);

        if (web != null)
        {
            this.tvName = view.findViewById(R.id.nameResult);
            this.tvUrl = view.findViewById(R.id.url);
            this.poster = view.findViewById(R.id.poster);

            tvName.setText(web.getName());
            tvUrl.setText(web.getUrl());

            Picasso.with(getContext()).load(web.getImagePath()).into(poster);
        }

        return view;
    }

    public interface Callbacks
    {
        public void onAddToListClicked(QRWeb web);
    }

    public void btnOnQRAdded()
    {
        detailsActivity.onAddToListClicked(this.web);
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        this.detailsActivity = (WebFragment.Callbacks) activity;
    }
}
