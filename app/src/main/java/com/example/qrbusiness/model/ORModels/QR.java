package com.example.qrbusiness.model.ORModels;

import android.os.Bundle;

public abstract class QR
{
    private String id;
    private String name, qrType;
    private String imagePath;

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String QR_TYPE = "qrType";
    public static final String IMAGE_PATH = "imagePath";

    public QR() {}

    public QR(String id, String name, String qrType, String imagePath)
    {
        this.id = id;
        this.name = name;
        this.qrType = qrType;
        this.imagePath = imagePath;
    }

    public QR(String imagePath)
    {
        this.imagePath = imagePath;
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

    public void setQrType(String qrType)
    {
        this.qrType = qrType;
    }

    public String getImagePath()
    {
        return imagePath;
    }

    public void setImagePath(String imagePath)
    {
        this.imagePath = imagePath;
    }

    @Override
    public String toString()
    {
        return "QR{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", qrType='" + qrType + '\'' +
                ", imagePath='" + imagePath + '\'' +
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
            this.imagePath = b.getString(IMAGE_PATH);
        }
    }

    //	Package data for transfer between activities
    public Bundle toBundle()
    {
        Bundle b = new Bundle();
        b.putString(ID, this.id);
        b.putString(NAME, this.name);
        b.putString(QR_TYPE, this.qrType);
        b.putString(IMAGE_PATH, this.imagePath);
        return b;
    }
}
