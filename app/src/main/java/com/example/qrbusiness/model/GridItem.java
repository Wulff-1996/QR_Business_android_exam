package com.example.qrbusiness.model;

import android.graphics.drawable.Drawable;

public class GridItem
{
    private String image;
    private String name;
    private Drawable icon;


    public GridItem() {
        super();
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Drawable getIcon()
    {
        return icon;
    }

    public void setIcon(Drawable icon)
    {
        this.icon = icon;
    }
}
