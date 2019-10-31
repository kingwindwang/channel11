package com.example.channel.adapter;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.channel.R;
import com.example.channel.activity.MainActivity;
import com.example.channel.activity.TaskActivity;
import com.example.channel.model.impl.SiteModelImpl;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class SiteAdapter extends RecyclerView.Adapter<SiteAdapter.HeadleView> {

    private MainActivity context;
    private TaskActivity context1;
    private List<SiteModelImpl>  siteModels;
    private LayoutInflater inflater;
    private Resources resource;

    public SiteAdapter(MainActivity context, List<SiteModelImpl>  siteModels){
        this.context = context;
        this.siteModels = siteModels;
        inflater = LayoutInflater.from(context);
        resource = context.getResources();
    }

    public SiteAdapter(TaskActivity context, List<SiteModelImpl>  siteModels){
        this.context1 = context;
        this.siteModels = siteModels;
        inflater = LayoutInflater.from(context1);
        resource = context.getResources();
    }

    @Override
    public HeadleView onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_site, parent, false);
        return new HeadleView(v);
    }

    @Override
    public void onBindViewHolder(HeadleView headleView, int position) {
        headleView.tv_site_num.setText(siteModels.get(position).getCreate_time());
        if (siteModels.get(position).getState() == -1)
            headleView.tv_site_name.setTextColor(resource.getColor(R.color.text_red));
        else if (siteModels.get(position).getState() == 1)
            headleView.tv_site_name.setTextColor(resource.getColor(R.color.text_blue));
        else
            headleView.tv_site_name.setTextColor(resource.getColor(R.color.text_green));
        headleView.tv_site_name.setText(siteModels.get(position).getLine_name());
        headleView.tv_site_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context != null)
                    context.gotoDetail(siteModels.get(position).getTask_id(), siteModels.get(position).getState());
                else
                    context1.gotoDetail(siteModels.get(position).getTask_id(), siteModels.get(position).getState());
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return siteModels.size();
    }

    static class HeadleView extends RecyclerView.ViewHolder{
        ImageView img_stie_logo;
        TextView tv_site_name, tv_site_num, tv_site_detail;

        public HeadleView(View convertView){
            super(convertView);
            img_stie_logo = convertView.findViewById(R.id.img_stie_logo);
            tv_site_num = convertView.findViewById(R.id.tv_site_num);
            tv_site_name = convertView.findViewById(R.id.tv_site_name);
            tv_site_detail = convertView.findViewById(R.id.tv_site_detail);
        }
    }
}
