package com.example.qrbusiness.model;

public abstract class QR
{
    String id, name, qrType;

    public QR()
    {
    }

    public QR(String id, String name, String qrType)
    {
        this.id = id;
        this.name = name;
        this.qrType = qrType;
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


    @Override
    public String toString()
    {
        return "QR{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", qrType='" + qrType + '\'' +
                '}';
    }
}
