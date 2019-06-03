package com.example.qrbusiness.model.ORModels;

import android.os.Bundle;

public class QRWeb extends  QR
{
    private String url;

    //  constants to bundle
    public static final String URL = "url";

    public QRWeb()
    {
        super();
        this.setQrType("web");
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }


    //  create to bundle
    public QRWeb(Bundle b)
    {
        if (b!= null)
        {
            this.url = b.getString(URL);
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
        b.putString(URL, this.url);
        b.putString(QR.ID, this.getId());
        b.putString(QR.NAME, this.getName());
        b.putString(QR.QR_TYPE, this.getQrType());
        b.putString(QR.IMAGE, this.getImage());
        return b;
    }
}
