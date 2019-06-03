package com.example.qrbusiness.controller.Create;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.qrbusiness.R;
import com.example.qrbusiness.Service.DialogBuilder;
import com.example.qrbusiness.Utils.ImageUtils;
import com.example.qrbusiness.controller.Details.DetailsActivity;
import com.example.qrbusiness.model.ORModels.QRWiFi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import net.glxn.qrgen.android.QRCode;

import it.auron.library.wifi.WifiCard;

public class CreateWifiFragment extends Fragment implements View.OnClickListener, TextWatcher
{
    private QRWiFi wifi;
    private EditText nameResult, wifiNameResult, passwordResult;
    private Spinner dropdown;
    private Button createBtn;
    private ImageButton isvalidQrnameBtn, isvalidWifiNameBtn, isvalidPasswordBtn;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_create_wifi, container, false);

        init(view);

        return view;
    }

    private void init(View view)
    {
        this.nameResult = view.findViewById(R.id.create_wifi_qr_name);
        this.wifiNameResult = view.findViewById(R.id.create_wifi_wifi_name);
        this.passwordResult = view.findViewById(R.id.create_wifi_password);
        this.dropdown = view.findViewById(R.id.create_wifi_nettypes);
        this.createBtn = view.findViewById(R.id.create_wifi_create_btn);
        this.isvalidQrnameBtn = view.findViewById(R.id.fragment_create_wifi_qrname_isvalid_btn);
        this.isvalidWifiNameBtn = view.findViewById(R.id.fragment_create_wifi_wifi_name_isvalid_btn);
        this.isvalidPasswordBtn = view.findViewById(R.id.fragment_create_wifi_password_isvalid_btn);

        isvalidQrnameBtn.setOnClickListener(this);
        isvalidWifiNameBtn.setOnClickListener(this);
        isvalidPasswordBtn.setOnClickListener(this);

        this.createBtn.setOnClickListener(this);
        this.createBtn.setEnabled(false);
        this.nameResult.addTextChangedListener(this);
        this.wifiNameResult.addTextChangedListener(this);
        this.passwordResult.addTextChangedListener(this);

        String[] authTypes = new String[]{"None", "WEP", "WPA"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, authTypes);

        this.dropdown.setAdapter(adapter);
        //Set default starting position of dropdown
        this.dropdown.setSelection(2);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.create_wifi_create_btn:
                this.wifi = new QRWiFi();
                this.wifi.setName(nameResult.getText().toString());
                this.wifi.setWifiName(wifiNameResult.getText().toString());
                this.wifi.setPassword(passwordResult.getText().toString());
                this.wifi.setNetType(dropdown.getSelectedItem().toString());

                WifiCard wifiCard = new WifiCard();
                wifiCard.setSid(wifi.getWifiName());
                wifiCard.setPassword(wifi.getPassword());
                wifiCard.setType(wifi.getNetType());
                Bitmap bitmap = QRCode.from(wifiCard.buildString()).bitmap();
                this.wifi.setImage(ImageUtils.bitmapToBase64(bitmap));
                uploadQRModel();
                break;

            case R.id.fragment_create_wifi_qrname_isvalid_btn:
                DialogBuilder.createDialog("QR name must be 3 letters long or more.", getContext()).show();
                break;

            case R.id.fragment_create_wifi_wifi_name_isvalid_btn:
                DialogBuilder.createDialog("Wifi name must be 1 letters long or more and cant contain spaces.", getContext()).show();
                break;

            case R.id.fragment_create_wifi_password_isvalid_btn:
                DialogBuilder.createDialog("Password cannot be empty.", getContext()).show();
                break;
        }
    }

    private void uploadQRModel()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final DocumentReference documentReference = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("QR_IDs").document();

        this.wifi.setId(documentReference.getId());
        final QRWiFi wifi = this.wifi;

        documentReference
                .set(wifi)
                .addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        Toast.makeText(getActivity(), "Wifi added to Library", Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(getActivity(), DetailsActivity.class);
                        i.putExtra("BUNDLE", wifi.toBundle());
                        startActivity(i);
                        getActivity().finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(getActivity(), "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {

    }

    @Override
    public void afterTextChanged(Editable s)
    {
        String QRName = nameResult.getText().toString();
        String wifiName = wifiNameResult.getText().toString();
        String password = passwordResult.getText().toString();

        boolean isValidName = false;
        boolean isValidWifiName = false;
        boolean isValidPassword = false;

        if (QRName.length() >= 3)
        {
            isValidName = true;
            isvalidQrnameBtn.setImageResource(R.drawable.ic_correct);
        }
        else
        {
            isvalidQrnameBtn.setImageResource(R.drawable.ic_incorrect);
        }

        if (wifiName.length() >= 1 && !wifiName.contains(" "))
        {
            isValidWifiName = true;
            isvalidWifiNameBtn.setImageResource(R.drawable.ic_correct);
        }
        else
        {
            isvalidWifiNameBtn.setImageResource(R.drawable.ic_incorrect);
        }

        if (!password.isEmpty())
        {
            isValidPassword = true;
            isvalidPasswordBtn.setImageResource(R.drawable.ic_correct);
        }
        else
        {
            isvalidPasswordBtn.setImageResource(R.drawable.ic_incorrect);
        }

        if (isValidName && isValidWifiName && isValidPassword)
        {
            createBtn.setEnabled(true);
            createBtn.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.style_rounded_highlighted));
        }
        else
        {
            this.createBtn.setEnabled(false);
            createBtn.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.style_rounded_unhighligted));
        }
    }
}
