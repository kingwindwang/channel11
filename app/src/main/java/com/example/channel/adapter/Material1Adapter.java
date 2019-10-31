package com.example.channel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.channel.R;
import com.example.channel.model.impl.MaterialModelImpl;

import java.util.List;

public class Material1Adapter extends BaseAdapter {

    private Context context;
    private List<MaterialModelImpl> materialModels;
    private LayoutInflater inflater;
    private int position = 0;

    public Material1Adapter(Context context, List<MaterialModelImpl> materialModels){
        this.context = context;
        this.materialModels = materialModels;
        inflater = LayoutInflater.from(context);
    }

    public void selectPosition(int position){
        this.position = position;
    }

    @Override
    public int getCount() {
        return materialModels.size();
    }

    @Override
    public Object getItem(int i) {
        return materialModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null){
            view = inflater.inflate(R.layout.adapter_material1, null);
            holder = new ViewHolder();
            holder.materialName = view.findViewById(R.id.tv_material1_name);
            view.setTag(holder);
        }else
            holder = (ViewHolder) view.getTag();
        holder.materialName.setText(materialModels.get(i).getName());
        if (position == i)
            view.setBackgroundColor(context.getResources().getColor(R.color.white));
        else
            view.setBackgroundColor(context.getResources().getColor(R.color.bg));
        return view;
    }

    class ViewHolder{
        TextView materialName;
    }
}
