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

public class CreateWifiFragment extends Fragment
{
    TextView titleText;
    EditText nameResult, wifiNameResult, passwordResult;
    Spinner dropdown;
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
        View view = inflater.inflate(R.layout.activity_create_wifi_fragment, container, false);

        init(view);

        return view;
    }

    private void init(View view)
    {
        this.titleText = view.findViewById(R.id.TextviewTitle);
        this.nameResult = view.findViewById(R.id.EditTextName);
        this.wifiNameResult = view.findViewById(R.id.EditTextWifiName);
        this.passwordResult = view.findViewById(R.id.EditTextPassword);
        this.dropdown = view.findViewById(R.id.authTypeDropdown);
        this.createBtn = view.findViewById(R.id.CreateBtn);

        String[] authTypes = new String[]{"None", "WEP", "WPA"};
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, authTypes);

        this.dropdown.setAdapter(adapter);
        //Set default starting position of dropdown
        this.dropdown.setSelection(2);
    }
}
