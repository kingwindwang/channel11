package com.example.channel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.channel.R;
import com.example.channel.activity.MaterialAddActivity;
import com.example.channel.model.impl.MaterialModelImpl;

import java.util.List;

public class MaterialAdapter extends BaseAdapter {

    private List<MaterialModelImpl.Material1> material1s;
    private LayoutInflater inflater;

    public MaterialAdapter(Context context, List<MaterialModelImpl.Material1> material1s){
        this.material1s = material1s;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return material1s.size();
    }

    @Override
    public Object getItem(int i) {
        return material1s.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null){
            view = inflater.inflate(R.layout.adpater_material, null);
            holder = new ViewHolder();
            holder.materialName = view.findViewById(R.id.tv_material_name);
            holder.materialNum = view.findViewById(R.id.tv_material_num);
            view.setTag(holder);
        }else
            holder = (ViewHolder) view.getTag();
        holder.materialName.setText(material1s.get(i).getName());
        holder.materialNum.setText(material1s.get(i).getMaterialNum()+"");
        return view;
    }

    class ViewHolder{
        TextView materialName, materialNum;
    }
}
