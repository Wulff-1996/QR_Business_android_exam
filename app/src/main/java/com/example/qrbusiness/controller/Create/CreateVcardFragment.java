package com.example.qrbusiness.controller.Create;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.qrbusiness.R;
import com.example.qrbusiness.Service.DialogBuilder;
import com.example.qrbusiness.controller.Details.DetailsActivity;
import com.example.qrbusiness.model.ORModels.QRVcard;
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

public class CreateVcardFragment extends Fragment implements View.OnClickListener, TextWatcher
{

    private QRVcard vcard;
    private EditText nameResult, firstNameResult, lastNameResult, phoneNumberResult, emailResult;
    private Button createBtn;
    private ImageButton isvalidQrnameBtn, isvalidFirstnameBtn, isvalidLastnameBtn, isvalidPhoneBtn, isvalidEmailBtn;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_create_vcard, container, false);

        init(view);

        return view;
    }

    private void init(View view)
    {
        this.nameResult = view.findViewById(R.id.create_vcard_qr_name);
        this.firstNameResult = view.findViewById(R.id.create_vcard_firstname);
        this.lastNameResult = view.findViewById(R.id.create_vcard_lastname);
        this.phoneNumberResult = view.findViewById(R.id.create_vcard_phone);
        this.emailResult = view.findViewById(R.id.create_vcard_email);
        this.createBtn = view.findViewById(R.id.create_vcard_create_btn);
        this.createBtn.setOnClickListener(this);
        this.createBtn.setEnabled(false);

        this.isvalidQrnameBtn = view.findViewById(R.id.fragment_create_vcard_qrname_isvalid_btn);
        this.isvalidFirstnameBtn = view.findViewById(R.id.fragment_create_vcard_firstname_isvalid_btn);
        this.isvalidLastnameBtn = view.findViewById(R.id.fragment_create_vcard_lastname_isvalid_btn);
        this.isvalidPhoneBtn = view.findViewById(R.id.fragment_create_vcard_phone_isvalid_btn);
        this.isvalidEmailBtn = view.findViewById(R.id.fragment_create_vcard_email_isvalid_btn);

        this.isvalidQrnameBtn.setOnClickListener(this);
        this.isvalidFirstnameBtn.setOnClickListener(this);
        this.isvalidLastnameBtn.setOnClickListener(this);
        this.isvalidPhoneBtn.setOnClickListener(this);
        this.isvalidEmailBtn.setOnClickListener(this);


        nameResult.addTextChangedListener(this);
        firstNameResult.addTextChangedListener(this);
        lastNameResult.addTextChangedListener(this);
        phoneNumberResult.addTextChangedListener(this);
        emailResult.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.create_vcard_create_btn:
                this.vcard = new QRVcard();
                this.vcard.setName(nameResult.getText().toString());
                this.vcard.setFirstName(firstNameResult.getText().toString());
                this.vcard.setLastName(lastNameResult.getText().toString());
                this.vcard.setPhoneNum(phoneNumberResult.getText().toString());
                this.vcard.setEmail(emailResult.getText().toString());
                uploadImage();
                break;

            case R.id.fragment_create_vcard_qrname_isvalid_btn:
                DialogBuilder.createDialog("QR name must be over 3 letters long.", getContext()).show();
                break;

            case R.id.fragment_create_vcard_firstname_isvalid_btn:
                DialogBuilder.createDialog("First name must be over 3 letters long, and cant contain spaces.", getContext()).show();
                break;

            case R.id.fragment_create_vcard_lastname_isvalid_btn:
                DialogBuilder.createDialog("Last name must be over 3 letters long, and cant contain spaces.", getContext()).show();
                break;

            case R.id.fragment_create_vcard_phone_isvalid_btn:
                DialogBuilder.createDialog("Phone number must only contain numbers and be 8 numbers long.", getContext()).show();
                break;

            case R.id.fragment_create_vcard_email_isvalid_btn:
                DialogBuilder.createDialog("Email must contain '@' and '.' and be over 3 characters long.", getContext()).show();
                break;
        }
    }

    private void uploadQRModel(Uri uri)
    {
        this.vcard.setImagePath(uri.toString());

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final DocumentReference documentReference = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("QR_IDs").document();

        this.vcard.setId(documentReference.getId());
        final QRVcard vcard = this.vcard;

        documentReference
                .set(vcard)
                .addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        Toast.makeText(getActivity(), "Vcard added to Library", Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(getActivity(), DetailsActivity.class);
                        i.putExtra("BUNDLE", vcard.toBundle());
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

                        //  get download path to the image
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
                        Toast.makeText(getActivity(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
        String firstName = firstNameResult.getText().toString();
        String lastName = lastNameResult.getText().toString();
        String phoneNumber = phoneNumberResult.getText().toString();
        String email = emailResult.getText().toString();

        boolean isValidName = false;
        boolean isValidFirstName = false;
        boolean isValidLastName = false;
        boolean isValidPhoneNumber = false;
        boolean isValidEmail = false;

        if (!QRName.isEmpty() && QRName.length() >= 3)
        {
            isValidName = true;
            isvalidQrnameBtn.setImageResource(R.drawable.ic_correct);
        }
        else
        {
            isvalidQrnameBtn.setImageResource(R.drawable.ic_incorrect);
        }

        if (!firstName.isEmpty() && firstName.length() >= 3 && !firstName.contains(" "))
        {
            isValidFirstName = true;
            isvalidFirstnameBtn.setImageResource(R.drawable.ic_correct);
        }
        else
        {
            isvalidFirstnameBtn.setImageResource(R.drawable.ic_incorrect);
        }

        if (!lastName.isEmpty() && lastName.length() >= 3 && !lastName.contains(" "))
        {
            isValidLastName = true;
            isvalidLastnameBtn.setImageResource(R.drawable.ic_correct);
        }
        else
        {
            isvalidLastnameBtn.setImageResource(R.drawable.ic_incorrect);
        }

        if (!phoneNumber.isEmpty() && phoneNumber.length() == 8)
        {
            isValidPhoneNumber = true;
            isvalidPhoneBtn.setImageResource(R.drawable.ic_correct);
        }
        else
        {
            isvalidPhoneBtn.setImageResource(R.drawable.ic_incorrect);
        }

        if (!email.isEmpty() && email.length() > 3 && email.contains("@") && email.contains("."))
        {
            isValidEmail = true;
            isvalidEmailBtn.setImageResource(R.drawable.ic_correct);
        }
        else
        {
            isvalidEmailBtn.setImageResource(R.drawable.ic_incorrect);
        }

        if (isValidName && isValidFirstName && isValidLastName && isValidPhoneNumber && isValidEmail)
        {
            this.createBtn.setEnabled(true);
        }
        else
        {
            this.createBtn.setEnabled(false);
        }
    }
}
