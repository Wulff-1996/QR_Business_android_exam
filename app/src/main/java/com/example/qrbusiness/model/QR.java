package com.example.qrbusiness.model;

import android.media.Image;

public abstract class QR
{

    int id;
    String name, QrType;
    Image QRImage;

    public QR()
    {
    }

    public QR(int id, String name, String qrType, Image QRImage)
    {
        this.id = id;
        this.name = name;
        QrType = qrType;
        this.QRImage = QRImage;
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
        return QrType;
    }

    public void setQrType(String qrType)
    {
        QrType = qrType;
    }

    public Image getQRImage()
    {
        return QRImage;
    }

    public void setQRImage(Image QRImage)
    {
        this.QRImage = QRImage;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    @Override
    public String toString()
    {
        return "QR{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", QrType='" + QrType + '\'' +
                ", QRImage=" + QRImage +
                '}';
    }
}
