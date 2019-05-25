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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrbusiness.R;
import com.example.qrbusiness.controller.Details.DetailsActivity;
import com.example.qrbusiness.model.ORModels.QRWeb;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreateWebFragment extends Fragment implements View.OnClickListener, TextWatcher
{
    private QRWeb web;
    private TextView titletext;
    private EditText nameResult, urlResult;
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
        View view = inflater.inflate(R.layout.create_web_fragment, container, false);

        init(view);

        return view;
    }

    private void init(View view)
    {
        this.titletext = view.findViewById(R.id.TextviewTitle);
        this.nameResult = view.findViewById(R.id.urlQRName);
        this.urlResult = view.findViewById(R.id.urlResult);
        this.createBtn = view.findViewById(R.id.urlCreateBtn);
        this.createBtn.setOnClickListener(this);
        this.createBtn.setEnabled(false);

        this.nameResult.addTextChangedListener(this);
        this.urlResult.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.urlCreateBtn)
        {
            this.web = new QRWeb();
            this.web.setName(nameResult.getText().toString());
            this.web.setQrType("web");
            this.web.setUrl(urlResult.getText().toString());

            uploadImage();
        }

    }

    private void uploadQRModel(Uri uri)
    {
        Map<String, Object> web = new HashMap<>();
        web.put("Name", this.web.getName());
        web.put("Type", this.web.getQrType());
        web.put("URL", this.web.getUrl());
        web.put("Image Location", uri.toString());

        this.web.setImagePath(uri.toString());

        final QRWeb v = this.web;

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final DocumentReference documentReference = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("QR_IDs").document();

        documentReference
                .set(web)
                .addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        Toast.makeText(getActivity(), "Web added to Library", Toast.LENGTH_SHORT).show();

                        v.setId(documentReference.toString());


                        Intent i = new Intent(getActivity(), DetailsActivity.class);
                        i.putExtra("BUNDLE", v.toBundle());
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
        String url = urlResult.getText().toString();

        boolean isValidName = false;
        boolean isValidUrl = false;

        if (QRName != null && !QRName.isEmpty() && QRName.length() >= 3)
        {
            isValidName = true;
            this.nameResult.setBackgroundColor(Color.RED);
        }
        else
        {
            this.nameResult.setBackgroundColor(Color.WHITE);
        }

        if (url != null && !url.isEmpty() && url.length() >= 3)
        {
            isValidUrl = true;
            this.urlResult.setBackgroundColor(Color.RED);
        }
        else
        {
            this.urlResult.setBackgroundColor(Color.WHITE);
        }



        if (isValidName && isValidUrl)
        {
            this.createBtn.setEnabled(true);
        }
        else
        {
            this.createBtn.setEnabled(false);
        }
    }
}
