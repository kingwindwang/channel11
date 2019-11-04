package com.example.channel.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.channel.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/11/4.
 */

public class ImageAdapter extends BaseAdapter{

    private ArrayList<String> imgs;
    private LayoutInflater inflater;
    private Activity context;

    public ImageAdapter(Activity context, ArrayList<String> imgs){
        this.imgs = imgs;
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return imgs.size();
    }

    @Override
    public Object getItem(int position) {
        return imgs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.adapter_img, null);
            holder = new ViewHolder();
            holder.img = convertView.findViewById(R.id.focus);
            convertView.setTag(holder);
        }else
            holder = (ViewHolder) convertView.getTag();
        show(imgs.get(position), holder.img);
        return convertView;
    }

    class ViewHolder{
        ImageView img;
    }

    private void show(String url, ImageView img){
        try {
            if (!url.contains("http"))
                url = "file://" + url;
            Picasso.with(context).load(url).into(img);
        }catch (Exception e){
            Log.d("s", e.getMessage());
        }
    }
}
