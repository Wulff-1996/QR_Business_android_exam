package com.example.qrbusiness.model;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qrbusiness.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GridViewAdapter extends ArrayAdapter<GridItem>
{
    private Context mContext;
    private int layoutResourceId;
    private ArrayList<GridItem> mGridData;

    public GridViewAdapter(Context mContext, int layoutResourceId, ArrayList<GridItem> mGridData)
    {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
    }


    /**
     * Updates grid data and refresh grid items.
     * @param mGridData
     */
    public void setGridData(ArrayList<GridItem> mGridData)
    {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }

    public void reset()
    {
        this.mGridData.clear();
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        ViewHolder holder;

        if (row == null)
        {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageView = row.findViewById(R.id.gird_item_qr_image);
            holder.name = row.findViewById(R.id.gird_item_name);
            holder.icon = row.findViewById(R.id.gird_item_qr_icon);
            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) row.getTag();
        }

        GridItem item = mGridData.get(position);
        if (item.getImage() != null)
        {
            Picasso.with(mContext).load(item.getImage()).into(holder.imageView);
        }
        if (item.getName() != null)
        {
            holder.name.setText(item.getName());
        }
        if (item.getIcon() != null)
        {
           holder.icon.setImageDrawable(item.getIcon());
        }

        return row;
    }

    static class ViewHolder {
        ImageView imageView;
        ImageView icon;
        TextView name;
    }
}
