package com.example.channel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.channel.R;
import com.example.channel.activity.SiteDetailActivity;
import com.example.channel.model.impl.SiteDetailModelImpl;

import java.util.List;

public class SiteDetailAdapter extends BaseAdapter {

    private List<SiteDetailModelImpl> siteDetailModels;
    private LayoutInflater inflater;
    private SiteDetailActivity context;
    private boolean isAdd;

    public SiteDetailAdapter(SiteDetailActivity context, List<SiteDetailModelImpl> siteDetailModels, boolean isAdd){
        this.siteDetailModels = siteDetailModels;
        this.context = context;
        this.isAdd = isAdd;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return siteDetailModels.size();
    }

    @Override
    public Object getItem(int i) {
        return siteDetailModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null){
            view = inflater.inflate(R.layout.adapter_site_detail, null);
            holder = new ViewHolder();
            holder.img_circle = view.findViewById(R.id.img_circle);
            holder.img_dian1 = view.findViewById(R.id.img_dian1);
            holder.img_dian2 = view.findViewById(R.id.img_dian2);
            holder.img_dian3 = view.findViewById(R.id.img_dian3);
            holder.img_dian4 = view.findViewById(R.id.img_dian4);
            holder.tv_site_pos = view.findViewById(R.id.tv_site_pos);
            holder.tv_site_edit = view.findViewById(R.id.tv_site_edit);
            holder.tv_name1 = view.findViewById(R.id.tv_name1);
            holder.tv_name2 = view.findViewById(R.id.tv_name2);
            holder.tv_name3 = view.findViewById(R.id.tv_name3);
            holder.tv_name4 = view.findViewById(R.id.tv_name4);
            view.setTag(holder);
        }else
            holder = (ViewHolder) view.getTag();
        SiteDetailModelImpl site = siteDetailModels.get(i);
        holder.tv_name1.setText(site.getLine1());
        holder.tv_name2.setText(site.getLine2());
        holder.tv_name3.setText(site.getLine3());
        holder.tv_name4.setText(site.getLine4());
        holder.tv_site_pos.setText(site.getPoint_type());
        if (site.getPoint_type().equals("起始点")){
            holder.img_circle.setImageDrawable(context.getResources().getDrawable(R.drawable.circle_green));
            holder.img_dian1.setImageDrawable(context.getResources().getDrawable(R.drawable.dian_green));
            holder.img_dian2.setImageDrawable(context.getResources().getDrawable(R.drawable.dian_gray));
            holder.img_dian3.setImageDrawable(context.getResources().getDrawable(R.drawable.dian_gray));
            holder.img_dian4.setImageDrawable(context.getResources().getDrawable(R.drawable.dian_gray));
            holder.tv_site_pos.setTextColor(context.getResources().getColor(R.color.text_green));
        }else if (site.getPoint_type().equals("终止点")){
            holder.tv_site_pos.setTextColor(context.getResources().getColor(R.color.text_red));
            holder.img_circle.setImageDrawable(context.getResources().getDrawable(R.drawable.circle_red));
            holder.img_dian1.setVisibility(View.INVISIBLE);
            holder.img_dian2.setVisibility(View.INVISIBLE);
            holder.img_dian3.setVisibility(View.INVISIBLE);
            holder.img_dian4.setVisibility(View.INVISIBLE);
        }else {
            holder.img_circle.setImageDrawable(context.getResources().getDrawable(R.drawable.circle_blue));
            holder.img_dian1.setImageDrawable(context.getResources().getDrawable(R.drawable.dian_blue));
            holder.img_dian2.setImageDrawable(context.getResources().getDrawable(R.drawable.dian_blue));
            holder.img_dian3.setImageDrawable(context.getResources().getDrawable(R.drawable.dian_blue));
            holder.img_dian4.setImageDrawable(context.getResources().getDrawable(R.drawable.dian_blue));
            holder.tv_site_pos.setTextColor(context.getResources().getColor(R.color.text_blue));
        }
        if (!isAdd)
            holder.tv_site_edit.setVisibility(View.INVISIBLE);
        holder.tv_site_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.edit(i);
            }
        });
        return view;
    }

    class ViewHolder{
        ImageView img_circle, img_dian1, img_dian2, img_dian3, img_dian4;
        TextView tv_site_pos, tv_site_edit, tv_name1, tv_name2, tv_name3, tv_name4;
    }
}
