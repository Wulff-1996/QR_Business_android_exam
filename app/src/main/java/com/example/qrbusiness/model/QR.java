package com.example.qrbusiness.model;

public class QR
{

    int id;
    String name, qrType;
    String imagePath;

    public QR()
    {
    }

    public QR(int id, String name, String qrType, String imagePath)
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

    public int getId()
    {
        return id;
    }

    public void setId(int id)
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
}
