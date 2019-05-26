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
import com.example.qrbusiness.model.ORModels.QRVcard;
import com.squareup.picasso.Picasso;

public class VcardFragment extends Fragment
{
    private Callbacks detailsActivity;
    private QRVcard vcard;

    private TextView qrName;
    private TextView fullName;
    private TextView email;
    private TextView phone;
    private ImageView poster;


    public VcardFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Bundle b = getArguments();
        if (b != null && b.containsKey(vcard.FIRST_NAME))
        {
            vcard = new QRVcard(b);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_details_vcard, container, false);

        if (vcard != null)
        {
            this.qrName = view.findViewById(R.id.details_vcard_qr_name);
            this.fullName = view.findViewById(R.id.details_vcard_fullName_result);
            this.email = view.findViewById(R.id.details_vcard_email_result);
            this.phone = view.findViewById(R.id.details_vcard_phone_result);
            this.poster = view.findViewById(R.id.details_vcard_poster);

            qrName.setText(vcard.getName());
            fullName.setText(vcard.getFirstName() + "" + vcard.getLastName());
            email.setText(vcard.getEmail());
            phone.setText(vcard.getPhoneNum());


            Picasso.with(getContext()).load(vcard.getImagePath()).into(poster);
        }

        return view;
    }

    public interface Callbacks
    {
        public void onAddToListClicked(QRVcard vcard);
    }

    public void btnOnQRAdded()
    {
        detailsActivity.onAddToListClicked(this.vcard);
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        this.detailsActivity = (Callbacks) activity;
    }
}
