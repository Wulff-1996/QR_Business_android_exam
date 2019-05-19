package com.example.qrbusiness.model.ORModels;

import android.os.Bundle;

public class QRVcard extends QR
{
    private String firstName, lastName, phoneNum, email;

    //  constants
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String EMAIL = "email";
    public static final String PHONE = "phone";

    public QRVcard()
    {
        super();
        this.setQrType("vcard");
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getPhoneNum()
    {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum)
    {
        this.phoneNum = phoneNum;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    //  create to bundle
    public QRVcard(Bundle b)
    {
        if (b!= null)
        {
            this.firstName = b.getString(FIRST_NAME);
            this.lastName = b.getString(LAST_NAME);
            this.email = b.getString(EMAIL);
            this.phoneNum = b.getString(PHONE);

            this.setId(b.getString(QR.ID));
            this.setName(b.getString(QR.NAME));
            this.setQrType(b.getString(QR.QR_TYPE));
            this.setImagePath(b.getString(QR.IMAGE_PATH));
        }
    }

    //	Package data for transfer between activities
    public Bundle toBundle()
    {
        Bundle b = new Bundle();
        b.putString(FIRST_NAME, this.firstName);
        b.putString(LAST_NAME, this.lastName);
        b.putString(EMAIL, this.email);
        b.putString(PHONE, this.phoneNum);
        b.putString(QR.ID, this.getId());
        b.putString(QR.NAME, this.getName());
        b.putString(QR.QR_TYPE, this.getQrType());
        b.putString(QR.IMAGE_PATH, this.getImagePath());
        return b;
    }
}
