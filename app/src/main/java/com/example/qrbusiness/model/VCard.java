package com.example.qrbusiness.model;

import android.media.Image;

public class VCard extends QR
{
    private String contactName, phoneNumber, email;

    public VCard() {
    }

    public VCard(int id, String name, String qrType, Image QRImage, String contactName, String phoneNumber, String email)
    {
        super(id, name, qrType, QRImage);
        this.contactName = contactName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getContactName()
    {
        return contactName;
    }

    public void setContactName(String contactName)
    {
        this.contactName = contactName;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
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
    public String toString()
    {
        return "VCard{" +
                "contactName='" + contactName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", QrType='" + QrType + '\'' +
                ", QRImage=" + QRImage +
                '}';
    }
}
