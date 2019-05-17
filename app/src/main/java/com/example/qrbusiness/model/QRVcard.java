package com.example.qrbusiness.model;

import android.os.Parcel;
import android.os.Parcelable;

public class QRVcard extends QR implements Parcelable
{
    private String firstName, lastName, phoneNum, email;

    public QRVcard(int id, String name, String qrType, String imagePath, String firstName, String lastName, String phoneNum, String email)
    {
        super(id, name, qrType, imagePath);
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNum = phoneNum;
        this.email = email;
    }

    public QRVcard()
    {
        super();
    }

    protected QRVcard(Parcel in)
    {
        firstName = in.readString();
        lastName = in.readString();
        phoneNum = in.readString();
        email = in.readString();
    }

    public static final Creator<QRVcard> CREATOR = new Creator<QRVcard>()
    {
        @Override
        public QRVcard createFromParcel(Parcel in)
        {
            return new QRVcard(in);
        }

        @Override
        public QRVcard[] newArray(int size)
        {
            return new QRVcard[size];
        }
    };

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

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(phoneNum);
        dest.writeString(email);
    }
}
