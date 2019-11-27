package com.example.channel.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.channel.App;
import com.example.channel.R;

import butterknife.BindView;
import butterknife.OnClick;

public class SelectActivity extends BaseActivity{

    @BindView(R.id.lv_select)
    public ListView lv_select;

    private int position = -1;
    private String[] contents;
    private String content;
    private String title;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addView(R.layout.list, true);
        position = getIntent().getExtras().getInt("position");
        content = getIntent().getExtras().getString("content");
        title = getIntent().getExtras().getString("title");
        getList();
        tv_title.setText(title);
        adapter = new ListAdapter();
        lv_select.setAdapter(adapter);
        lv_select.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (position == 6)
                    content += ";" + contents[i];
                else
                    content = contents[i];
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void getList(){
        Resources res = getResources();
        switch (position){
            case 1://点类型（可选：普通点、终止点）
                contents = res.getStringArray(R.array.list1);
                break;
            case 2://电压等级-（选择10Kv、220V、400V）
                contents = res.getStringArray(R.array.list3);
                break;
            case 3://杆号
                if (content.equals("0"))
                    contents = res.getStringArray(R.array.list4_1);
                else
                    contents = res.getStringArray(R.array.list4);
                break;
            case 4://是否有同杆-（选择是、否-400V、否-220V）
                contents = res.getStringArray(R.array.list5);
                break;
            case 5://杆型：转角耐张、直线耐张、直线、转角、分支（400V分400V、220V分220V）、转换（400V转220V）、终端、门杆
                contents = res.getStringArray(R.array.list6);
                break;
            case 6://跨越/穿越/带电（选择公路、通讯、河流、电力）
                contents = res.getStringArray(R.array.list7);
                break;
        }
    }

    @OnClick(R.id.tv_submit)
    void onSubmit(){
        Intent in = new Intent();
        in.putExtra("content", content);
        setResult(App.SITE_LIST, in);
        back();
    }

    public class ListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return contents.length;
        }

        @Override
        public Object getItem(int i) {
            return contents[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view == null){
                view = LayoutInflater.from(SelectActivity.this).inflate(R.layout.adapter_select, null);
                holder = new ViewHolder();
                holder.tv_name = view.findViewById(R.id.tv_name);
                holder.img_select = view.findViewById(R.id.img_select);
                view.setTag(holder);
            }else
                holder = (ViewHolder) view.getTag();

            holder.tv_name.setText(contents[i]);
            if (position != 6){
                if (contents[i].equals(content))
                    holder.img_select.setVisibility(View.VISIBLE);
                else
                    holder.img_select.setVisibility(View.GONE);
            }else {
                String[] c = content.split(";");
                holder.img_select.setVisibility(View.GONE);
                for (String a : c){
                    if (contents[i].equals(a)){
                        holder.img_select.setVisibility(View.VISIBLE);
                        break;
                    }
                }
            }
            return view;
        }

        class ViewHolder{
            TextView tv_name;
            ImageView img_select;
        }
    }
}
