package com.example.channel.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
    private String title;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addView(R.layout.list, true);
        position = getIntent().getExtras().getInt("position");
        contents = getIntent().getExtras().getStringArray("content");
        title = getIntent().getExtras().getString("title");
        tv_title.setText(title);
        adapter = new ListAdapter();
        lv_select.setAdapter(adapter);
        lv_select.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                position = i;
                if (title.equals("跨越/穿越/带电")){
                    if (i == 0)
                        dialog(contents[position], getResources().getStringArray(R.array.list7_0));
                    else if (i == 3)
                        dialog(contents[position], getResources().getStringArray(R.array.list7_3));
                    else
                        adapter.notifyDataSetChanged();
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick(R.id.tv_submit)
    void onSubmit(){
        Intent in = new Intent();
        in.putExtra("position", position);
        setResult(App.SITE_LIST, in);
        back();
    }

    private void dialog(String name, String[] items4){

        AlertDialog alertDialog4 = new AlertDialog.Builder(this)
                .setTitle("选择您喜欢的老湿")
                .setIcon(R.mipmap.ic_launcher)
                .setSingleChoiceItems(items4, 0, new DialogInterface.OnClickListener() {//添加单选框
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        contents[position] = items4[i];
                        adapter.notifyDataSetChanged();
                        dialogInterface.dismiss();
                    }
                })
                .create();
        alertDialog4.show();
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
            if (position == i)
                holder.img_select.setVisibility(View.VISIBLE);
            else
                holder.img_select.setVisibility(View.GONE);
            return view;
        }

        class ViewHolder{
            TextView tv_name;
            ImageView img_select;
        }
    }
}
