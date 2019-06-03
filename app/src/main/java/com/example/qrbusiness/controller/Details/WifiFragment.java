package com.example.qrbusiness.controller.Details;

import android.app.Activity;
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
import com.example.qrbusiness.model.ORModels.QRWiFi;

public class WifiFragment extends Fragment
{
    private WifiFragment.Callbacks detailsActivity;
    private QRWiFi wiFi;

    private TextView qrName;
    private TextView wifiName;
    private TextView password;
    private TextView nettype;
    private ImageView poster;
    private ImageButton deleteBtn;

    public WifiFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Bundle b = getArguments();
        if (b != null && b.containsKey(wiFi.WIFI_NAME))
        {
            wiFi = new QRWiFi(b);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_details_wifi, container, false);

        if (wiFi != null)
        {
            this.qrName = view.findViewById(R.id.details_wifi_qr_name);
            this.wifiName = view.findViewById(R.id.details_wifi_wifi_name_result);
            this.password = view.findViewById(R.id.details_wifi_password_result);
            this.nettype = view.findViewById(R.id.details_wifi_nettype_result);
            this.poster = view.findViewById(R.id.details_wifi_poster);
            this.deleteBtn = view.findViewById(R.id.fragment_wifi_details_delete_btn);
            deleteBtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    DialogBuilder.deleteDialog(wiFi.getId(), getActivity()).show();
                }
            });

            qrName.setText(wiFi.getName());
            wifiName.setText(wiFi.getWifiName());
            password.setText(wiFi.getPassword());
            nettype.setText(wiFi.getNetType());

            Bitmap bitmap = ImageUtils.base64ToBitmap(wiFi.getImage());
            poster.setImageBitmap(bitmap);
        }

        return view;
    }

    public interface Callbacks
    {
        public void onAddToListClicked(QRWiFi wiFi);
    }

    public void btnOnQRAdded()
    {
        detailsActivity.onAddToListClicked(this.wiFi);
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        this.detailsActivity = (WifiFragment.Callbacks) activity;
    }
}
