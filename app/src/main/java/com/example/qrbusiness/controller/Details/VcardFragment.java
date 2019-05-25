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

    private TextView tvName;
    private TextView tvType;
    private TextView tvFulldname;
    private TextView tvEmail;
    private TextView tvPhone;
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
        View view = inflater.inflate(R.layout.details_vcard, container, false);

        if (vcard != null)
        {
            this.tvName = view.findViewById(R.id.nameResult);
            this.tvFulldname = view.findViewById(R.id.fullNameResult);
            this.tvEmail = view.findViewById(R.id.emailResult);
            this.tvPhone = view.findViewById(R.id.phoneResult);
            this.poster = view.findViewById(R.id.poster);

            tvName.setText(vcard.getName());
            tvFulldname.setText(vcard.getFirstName() + "" + vcard.getLastName());
            tvEmail.setText(vcard.getEmail());
            tvPhone.setText(vcard.getPhoneNum());


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
