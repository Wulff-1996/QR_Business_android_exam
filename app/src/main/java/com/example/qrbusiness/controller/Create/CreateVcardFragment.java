package com.example.qrbusiness.controller.Create;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.qrbusiness.R;

public class CreateVcardFragment extends Fragment
{

    TextView titleText;
    EditText nameResult, firstNameResult, lastNameResult, phoneNumberResult, emailResult;
    Button createBtn;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_create_vcard_fragment, container, false);

        init(view);

        return view;
    }

    private void init(View view)
    {
        this.titleText = view.findViewById(R.id.TextviewTitle);
        this.nameResult = view.findViewById(R.id.EditTextName);
        this.firstNameResult = view.findViewById(R.id.EditTextFirstName);
        this.lastNameResult = view.findViewById(R.id.EditTextLastName);
        this.phoneNumberResult = view.findViewById(R.id.EditTextNumber);
        this.emailResult = view.findViewById(R.id.EditTextEmail);
        this.createBtn = view.findViewById(R.id.CreateBtn);
    }
}
