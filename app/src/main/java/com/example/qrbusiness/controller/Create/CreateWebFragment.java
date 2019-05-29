package com.example.qrbusiness.controller.Create;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.qrbusiness.R;
import com.example.qrbusiness.Service.DialogBuilder;
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

import java.util.UUID;

public class CreateWebFragment extends Fragment implements View.OnClickListener, TextWatcher
{
    private QRWeb web;
    private EditText nameResult, urlResult;
    private Button createBtn;
    private ImageButton isvalidQrnameBtn, isvalidUrlBtn;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_create_web, container, false);

        init(view);

        return view;
    }

    private void init(View view)
    {
        this.nameResult = view.findViewById(R.id.create_web_qr_name);
        this.urlResult = view.findViewById(R.id.create_web_url);
        this.createBtn = view.findViewById(R.id.create_web_create_btn);
        this.createBtn.setOnClickListener(this);
        this.createBtn.setEnabled(false);
        this.isvalidQrnameBtn = view.findViewById(R.id.fragment_create_web_isvalid_qrname_btn);
        this.isvalidUrlBtn = view.findViewById(R.id.fragment_create_web_isvalid_url_btn);
        isvalidQrnameBtn.setOnClickListener(this);
        isvalidUrlBtn.setOnClickListener(this);

        this.nameResult.addTextChangedListener(this);
        this.urlResult.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.create_web_create_btn:
                this.web = new QRWeb();
                this.web.setName(nameResult.getText().toString());
                this.web.setUrl(urlResult.getText().toString());
                uploadImage();
                break;

            case R.id.fragment_create_web_isvalid_qrname_btn:
                DialogBuilder.createDialog("QR name must be 3 letters long or more.", getContext()).show();
                break;

            case R.id.fragment_create_web_isvalid_url_btn:
                DialogBuilder.createDialog("Url must contain 'www' and a top level domain fx '.com'", getContext()).show();
                break;
        }
    }

    private void uploadQRModel(Uri uri)
    {
        this.web.setImagePath(uri.toString());

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final DocumentReference documentReference = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("QR_IDs").document();

        this.web.setId(documentReference.getId());
        final QRWeb web = this.web;
        documentReference
                .set(web)
                .addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        Toast.makeText(getActivity(), "Web added to Library", Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(getActivity(), DetailsActivity.class);
                        i.putExtra("BUNDLE", web.toBundle());
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

        if (!QRName.isEmpty() && QRName.length() >= 3)
        {
            isValidName = true;
            isvalidQrnameBtn.setImageResource(R.drawable.ic_correct);
        }
        else
        {
            isvalidQrnameBtn.setImageResource(R.drawable.ic_incorrect);
        }

        if (!url.isEmpty() && url.length() >= 5 && url.contains("www."))
        {
            isValidUrl = true;
            isvalidUrlBtn.setImageResource(R.drawable.ic_correct);
        }
        else
        {
            isvalidUrlBtn.setImageResource(R.drawable.ic_incorrect);
        }



        if (isValidName && isValidUrl)
        {
            createBtn.setEnabled(true);
            createBtn.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.style_rounded_green));
        }
        else
        {
            this.createBtn.setEnabled(false);
            createBtn.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.style_rounded_red));
        }
    }
}
