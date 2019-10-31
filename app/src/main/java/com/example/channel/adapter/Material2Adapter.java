package com.example.channel.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.channel.R;
import com.example.channel.activity.MaterialAddActivity;
import com.example.channel.model.impl.MaterialModelImpl;

import java.util.List;

public class Material2Adapter extends BaseAdapter {

    private List<MaterialModelImpl.Material1> material1s;
    private LayoutInflater inflater;
    private MaterialAddActivity context;

    public Material2Adapter(MaterialAddActivity context, List<MaterialModelImpl.Material1> material1s){
        this.material1s = material1s;
        this.context = context;
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
            view = inflater.inflate(R.layout.adapter_material2, null);
            holder = new ViewHolder();
            holder.materialName = view.findViewById(R.id.tv_material2_name);
            holder.jian = view.findViewById(R.id.img_jian);
            holder.materialNum = view.findViewById(R.id.tv_material_num);
            holder.jia = view.findViewById(R.id.img_jia);
            view.setTag(holder);
        }else
            holder = (ViewHolder) view.getTag();
        holder.materialName.setText(material1s.get(i).getName());
        holder.materialNum.setText(material1s.get(i).getMaterialNum()+"");
        holder.jian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (material1s.get(i).getMaterialNum() > 0)
                    context.jiajian(i, material1s.get(i).getMaterialNum()-1);
            }
        });
        holder.jia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.jiajian(i, material1s.get(i).getMaterialNum()+1);
            }
        });
        return view;
    }

    class ViewHolder{
        TextView materialName, materialNum;
        ImageView jian, jia;
    }
}
