package com.example.channel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.channel.R;
import com.example.channel.model.impl.SiteContentModelImpl;

import java.util.List;

public class AddSiteAdapter extends BaseAdapter {

    private Context context;
    private List<SiteContentModelImpl> siteContentModels;
    private LayoutInflater inflater;
    private int rod_number;

    public AddSiteAdapter(Context context, List<SiteContentModelImpl> siteContentModels, int rod_number){
        this.context = context;
        this.siteContentModels = siteContentModels;
        this.rod_number = rod_number;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return siteContentModels.size();
    }

    @Override
    public Object getItem(int i) {
        return siteContentModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null){
            view = inflater.inflate(R.layout.layout_site_content, null);
            holder = new ViewHolder();
            holder.tv_name = view.findViewById(R.id.tv_name);
            holder.tv_content = view.findViewById(R.id.tv_content);
            holder.ll_select_site = view.findViewById(R.id.ll_select_site);
            holder.img_arrow = view.findViewById(R.id.img_arrow);
            view.setTag(holder);
        }else
            holder = (ViewHolder) view.getTag();
        holder.tv_name.setText(siteContentModels.get(i).getName());
        holder.tv_content.setText(siteContentModels.get(i).getContents());
        if (i == 0)
            holder.img_arrow.setVisibility(View.INVISIBLE);
        if ((i == 1 && rod_number == 0) || i == 10)
            holder.tv_content.setHint("请输入");

        return view;
    }

    static class ViewHolder{
        TextView tv_name, tv_content;
        LinearLayout ll_select_site;
        ImageView img_arrow;
    }
}
