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
import com.example.qrbusiness.model.ORModels.QRWiFi;
import com.squareup.picasso.Picasso;

public class WifiFragment extends Fragment
{
    private WifiFragment.Callbacks detailsActivity;
    private QRWiFi wiFi;

    private TextView tvName;
    private TextView tvType;
    private TextView tvWifiName;
    private TextView tvPassword;
    private TextView tvNetType;
    private ImageView poster;

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
        View view = inflater.inflate(R.layout.details_wifi, container, false);

        if (wiFi != null)
        {
            this.tvName = view.findViewById(R.id.nameResult);
            this.tvWifiName = view.findViewById(R.id.wifiNameResult);
            this.tvPassword = view.findViewById(R.id.passwordResult);
            this.tvNetType = view.findViewById(R.id.netTypeResult);
            this.poster = view.findViewById(R.id.poster);

            tvName.setText(wiFi.getName());
            tvWifiName.setText(wiFi.getWifiName());
            tvPassword.setText(wiFi.getPassword());
            tvNetType.setText(wiFi.getNetType());

            String image = wiFi.getImagePath();

            Picasso.with(getContext()).load(wiFi.getImagePath()).into(poster);
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
