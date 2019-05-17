package com.example.qrbusiness.model;

import android.os.Parcel;
import android.os.Parcelable;

public class QRWeb extends  QR implements Parcelable
{
    private String url;

    public QRWeb(int id, String name, String qrType, String imagePath, String url)
    {
        super(id, name, qrType, imagePath);
        this.url = url;
    }

    public QRWeb()
    {
        super();
    }

    protected QRWeb(Parcel in)
    {
        url = in.readString();
    }

    public static final Creator<QRWeb> CREATOR = new Creator<QRWeb>()
    {
        @Override
        public QRWeb createFromParcel(Parcel in)
        {
            return new QRWeb(in);
        }

        @Override
        public QRWeb[] newArray(int size)
        {
            return new QRWeb[size];
        }
    };

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(url);
    }
}
