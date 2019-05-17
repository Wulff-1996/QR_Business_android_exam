package com.example.qrbusiness.model;

import android.os.Parcel;
import android.os.Parcelable;

public class QRWiFi extends QR implements Parcelable
{
    private String wifiName, password, netType;

    public QRWiFi(int id, String name, String qrType, String imagePath, String wifiName, String password, String netType)
    {
        super(id, name, qrType, imagePath);
        this.wifiName = wifiName;
        this.password = password;
        this.netType = netType;
    }

    public QRWiFi()
    {
        super();
    }

    protected QRWiFi(Parcel in)
    {
        wifiName = in.readString();
        password = in.readString();
        netType = in.readString();
    }

    public static final Creator<QRWiFi> CREATOR = new Creator<QRWiFi>()
    {
        @Override
        public QRWiFi createFromParcel(Parcel in)
        {
            return new QRWiFi(in);
        }

        @Override
        public QRWiFi[] newArray(int size)
        {
            return new QRWiFi[size];
        }
    };

    public String getWifiName()
    {
        return wifiName;
    }

    public void setWifiName(String wifiName)
    {
        this.wifiName = wifiName;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getNetType()
    {
        return netType;
    }

    public void setNetType(String netType)
    {
        this.netType = netType;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(wifiName);
        dest.writeString(password);
        dest.writeString(netType);
    }
}
