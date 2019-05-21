package com.example.qrbusiness.controller.Create;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.qrbusiness.R;

public class CreateWebFragment extends Fragment
{
    TextView titletext;
    EditText nameResult, urlResult;
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
        View view = inflater.inflate(R.layout.activity_create_web_fragment, container, false);

        init(view);

        return view;
    }

    private void init(View view)
    {
        this.titletext = view.findViewById(R.id.TextviewTitle);
        this.nameResult = view.findViewById(R.id.EditTextName);
        this.urlResult = view.findViewById(R.id.EditTextUrl);
        this.createBtn = view.findViewById(R.id.CreateBtn);
    }
}
