package com.example.qrbusiness.model.ORModels;

import android.os.Bundle;

public class QRWiFi extends QR
{
    private String wifiName, password, netType;

    public static final String WIFI_NAME = "wifiName";
    public static final String PASSWORD = "password";
    public static final String NET_TYPE = "netType";

    public QRWiFi()
    {
        super();
        this.setQrType("wifi");
    }

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

    //  create to bundle
    public QRWiFi(Bundle b)
    {
        if (b!= null)
        {
            this.wifiName = b.getString(WIFI_NAME);
            this.password = b.getString(PASSWORD);
            this.netType = b.getString(NET_TYPE);

            this.setId(b.getString(QR.ID));
            this.setName(b.getString(QR.NAME));
            this.setQrType(b.getString(QR.QR_TYPE));
            this.setImage(b.getString(QR.IMAGE));
        }
    }

    //	Package data for transfer between activities
    public Bundle toBundle()
    {
        Bundle b = new Bundle();
        b.putString(WIFI_NAME, this.wifiName);
        b.putString(PASSWORD, this.password);
        b.putString(NET_TYPE, this.netType);
        b.putString(QR.ID, this.getId());
        b.putString(QR.NAME, this.getName());
        b.putString(QR.QR_TYPE, this.getQrType());
        b.putString(QR.IMAGE, this.getImage());
        return b;
    }
}
