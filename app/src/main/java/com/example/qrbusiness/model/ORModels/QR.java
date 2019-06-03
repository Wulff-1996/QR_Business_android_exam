package com.example.qrbusiness.model.ORModels;

import android.os.Bundle;

public abstract class QR
{
    private String id;
    private String name, qrType;
    private String image;

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String QR_TYPE = "qrType";
    public static final String IMAGE = "image";

    public QR() {}

    public QR(String id, String name, String qrType, String image)
    {
        this.id = id;
        this.name = name;
        this.qrType = qrType;
        this.image = image;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getQrType()
    {
        return qrType;
    }

    public static String getIMAGE()
    {
        return IMAGE;
    }

    public void setQrType(String qrType)
    {
        this.qrType = qrType;
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public static String getID()
    {
        return ID;
    }

    public static String getNAME()
    {
        return NAME;
    }

    @Override
    public String toString()
    {
        return "QR{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", qrType='" + qrType + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    //  create to bundle
    public QR(Bundle b)
    {
        if (b!= null)
        {
            this.id = b.getString(ID);
            this.name = b.getString(NAME);
            this.qrType = b.getString(QR_TYPE);
            this.image = b.getString(IMAGE);

        }
    }

    //	Package data for transfer between activities
    public Bundle toBundle()
    {
        Bundle b = new Bundle();
        b.putString(ID, this.id);
        b.putString(NAME, this.name);
        b.putString(QR_TYPE, this.qrType);
        b.putString(IMAGE, this.image);
        return b;
    }
}
