package com.example.qrbusiness.controller.Details;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qrbusiness.R;
import com.example.qrbusiness.Service.DialogBuilder;
import com.example.qrbusiness.Utils.ImageUtils;
import com.example.qrbusiness.model.ORModels.QRWeb;

public class WebFragment extends Fragment
{
    private QRWeb web;

    private TextView tvName;
    private TextView tvUrl;
    private ImageView poster;
    private ImageButton deleteBtn;

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
        View view = inflater.inflate(R.layout.fragment_details_web, container, false);

        if (web != null)
        {
            this.tvName = view.findViewById(R.id.details_web_qr_name);
            this.tvUrl = view.findViewById(R.id.details_web_url_result);
            this.poster = view.findViewById(R.id.details_web_poster);
            this.deleteBtn = view.findViewById(R.id.fragment_web_details_delete_btn);
            deleteBtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    DialogBuilder.deleteDialog(web.getId(), getActivity()).show();
                }
            });

            tvName.setText(web.getName());
            tvUrl.setText(web.getUrl());

            Bitmap bitmap = ImageUtils.base64ToBitmap(web.getImage());
            poster.setImageBitmap(bitmap);
        }

        return view;
    }
}
