package com.example.qrbusiness.controller.Create;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.qrbusiness.R;
import com.example.qrbusiness.controller.Details.DetailsActivity;
import com.example.qrbusiness.model.ORModels.QRWiFi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class CreateWifiFragment extends Fragment implements View.OnClickListener, TextWatcher
{
    private QRWiFi wifi;
    private EditText nameResult, wifiNameResult, passwordResult;
    private Spinner dropdown;
    private Button createBtn;


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

        this.createBtn.setOnClickListener(this);
        this.createBtn.setEnabled(false);
        this.nameResult.addTextChangedListener(this);
        this.wifiNameResult.addTextChangedListener(this);
        this.passwordResult.addTextChangedListener(this);

        String[] authTypes = new String[]{"None", "WEP", "WPA"};
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, authTypes);

        this.dropdown.setAdapter(adapter);
        //Set default starting position of dropdown
        this.dropdown.setSelection(2);
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.create_wifi_create_btn)
        {
            this.wifi = new QRWiFi();
            this.wifi.setName(nameResult.getText().toString());
            this.wifi.setWifiName(wifiNameResult.getText().toString());
            this.wifi.setPassword(passwordResult.getText().toString());
            this.wifi.setNetType(dropdown.getSelectedItem().toString());
            uploadImage();
        }
    }

    private void uploadQRModel(Uri uri)
    {
        this.wifi.setImagePath(uri.toString());

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

    private void uploadImage()
    {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();

        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + getResources().getResourcePackageName(R.drawable.qr_test)
                + '/' + getResources().getResourceTypeName(R.drawable.qr_test) + '/' + getResources().getResourceEntryName(R.drawable.qr_test) );

        final StorageReference ref = storageReference.child("QR_Images/" + UUID.randomUUID().toString());
        ref.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                    {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();

                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                        {
                            @Override
                            public void onSuccess(Uri uri)
                            {
                                // upload the model
                                uploadQRModel(uri);
                            }
                        });


                    }
                })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>()
                {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
                    {
                        double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                .getTotalByteCount());
                        progressDialog.setMessage("Uploaded "+(int)progress+"%");
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
        String netType = dropdown.getSelectedItem().toString();

        boolean isValidName = false;
        boolean isValidWifiName = false;
        boolean isValidPassword = false;
        boolean isValidNetType = false;

        if (QRName != null && !QRName.isEmpty() && QRName.length() >= 3)
        {
            isValidName = true;
            this.nameResult.setBackgroundColor(Color.RED);
        }
        else
        {
            this.nameResult.setBackgroundColor(Color.WHITE);
        }

        if (wifiName != null && !wifiName.isEmpty() && wifiName.length() >= 3)
        {
            isValidWifiName = true;
            this.wifiNameResult.setBackgroundColor(Color.RED);
        }
        else
        {
            this.wifiNameResult.setBackgroundColor(Color.WHITE);
        }

        if (password != null && !password.isEmpty())
        {
            isValidPassword = true;
            this.passwordResult.setBackgroundColor(Color.RED);
        }
        else
        {
            this.passwordResult.setBackgroundColor(Color.WHITE);
        }

        if (netType != null && !netType.isEmpty() && !password.equals("None"))
        {
            isValidNetType = true;
        }

        if (isValidName && isValidWifiName && isValidPassword && isValidNetType)
        {
            this.createBtn.setEnabled(true);
        }
        else
        {
            this.createBtn.setEnabled(false);
        }
    }
}
