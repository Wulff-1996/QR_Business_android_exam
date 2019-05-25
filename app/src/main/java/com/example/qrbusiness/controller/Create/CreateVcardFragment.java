package com.example.qrbusiness.controller.Create;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreateVcardFragment extends Fragment implements View.OnClickListener, TextWatcher
{

    private QRVcard vcard;
    private Image image;
    private TextView titleText;
    private EditText nameResult, firstNameResult, lastNameResult, phoneNumberResult, emailResult;
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
        View view = inflater.inflate(R.layout.create_vcard_fragment, container, false);

        init(view);

        return view;
    }

    private void init(View view)
    {
        this.titleText = view.findViewById(R.id.TextviewTitle);
        this.nameResult = view.findViewById(R.id.urlQRName);
        this.firstNameResult = view.findViewById(R.id.EditTextFirstName);
        this.lastNameResult = view.findViewById(R.id.EditTextLastName);
        this.phoneNumberResult = view.findViewById(R.id.EditTextNumber);
        this.emailResult = view.findViewById(R.id.EditTextEmail);
        this.createBtn = view.findViewById(R.id.CreateBtn);
        this.createBtn.setOnClickListener(this);
        this.createBtn.setEnabled(false);

        nameResult.addTextChangedListener(this);
        firstNameResult.addTextChangedListener(this);
        lastNameResult.addTextChangedListener(this);
        phoneNumberResult.addTextChangedListener(this);
        emailResult.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.CreateBtn)
        {
            this.vcard = new QRVcard();
            this.vcard.setName(nameResult.getText().toString());
            this.vcard.setQrType("vcard");
            this.vcard.setFirstName(firstNameResult.getText().toString());
            this.vcard.setLastName(lastNameResult.getText().toString());
            this.vcard.setPhoneNum(phoneNumberResult.getText().toString());
            this.vcard.setEmail(emailResult.getText().toString());

            uploadImage();
        }

    }

    private void uploadQRModel(Uri uri)
    {
        Map<String, Object> vcard = new HashMap<>();
        vcard.put("Name", this.vcard.getName());
        vcard.put("Type", this.vcard.getQrType());
        vcard.put("First Name", this.vcard.getFirstName());
        vcard.put("Last Name", this.vcard.getLastName());
        vcard.put("Email", this.vcard.getEmail());
        vcard.put("Phone Number", this.vcard.getPhoneNum());
        vcard.put("Image Location", uri.toString());

        this.vcard.setImagePath(uri.toString());

        final QRVcard v = this.vcard;

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final DocumentReference documentReference = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("QR_IDs").document();

        documentReference
                .set(vcard)
                .addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        Toast.makeText(getActivity(), "Vcard added to Library", Toast.LENGTH_SHORT).show();

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
        String firstName = firstNameResult.getText().toString();
        String lastName = lastNameResult.getText().toString();
        String phoneNumber = phoneNumberResult.getText().toString();
        String email = emailResult.getText().toString();

        boolean isValidName = false;
        boolean isValidFirstName = false;
        boolean isValidLastName = false;
        boolean isValidPhoneNumber = false;
        boolean isValidEmail = false;

        if (QRName != null && !QRName.isEmpty() && QRName.length() >= 3)
        {
            isValidName = true;
            this.nameResult.setBackgroundColor(Color.RED);
            System.out.println("name valid");
        }
        else
        {
            this.nameResult.setBackgroundColor(Color.WHITE);
        }

        if (firstName != null && !firstName.isEmpty() && firstName.length() >= 3 && !firstName.contains(" "))
        {
            isValidFirstName = true;
            this.firstNameResult.setBackgroundColor(Color.RED);
            System.out.println("firstname valid");
        }
        else
        {
            this.firstNameResult.setBackgroundColor(Color.WHITE);
        }

        if (lastName != null && !lastName.isEmpty() && lastName.length() >= 3 && !lastName.contains(" "))
        {
            isValidLastName = true;
            this.lastNameResult.setBackgroundColor(Color.RED);
            System.out.println("lastname valid");
        }
        else
        {
            this.lastNameResult.setBackgroundColor(Color.WHITE);
        }

        if (phoneNumber != null && !phoneNumber.isEmpty() && phoneNumber.length() == 8)
        {
            isValidPhoneNumber = true;
            this.phoneNumberResult.setBackgroundColor(Color.RED);
            System.out.println("phone valid");
        }
        else
        {
            this.phoneNumberResult.setBackgroundColor(Color.WHITE);
        }

        if (email != null && !email.isEmpty() && email.length() > 3 && email.contains("@") && email.contains("."))
        {
            isValidEmail = true;
            this.emailResult.setBackgroundColor(Color.RED);
            System.out.println("email valid");
        }
        else
        {
            this.emailResult.setBackgroundColor(Color.WHITE);
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

    public interface Callbacks
    {
        void onBtnCreateVcard(QRVcard vcard);
    }
}
