package com.example.channel.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.channel.R;
import com.example.channel.adapter.TaskFragmentAdapter;
import com.example.channel.fragment.TaskdelFragment;
import com.example.channel.fragment.TaskdingFragment;
import com.example.channel.fragment.TaskedFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

/**
 * Created by Administrator on 2019/10/10 0010.
 */

public class TaskActivity extends BaseActivity{

    ArrayList<Fragment> fragments;//Save each of the Page View
    @BindView(R.id.vp_task)
    public ViewPager mViewPager;
    @BindViews({R.id.tv_tasking, R.id.tv_tasked, R.id.tv_taskdel})
    public List<TextView> tvs;
    @BindView(R.id.iv_bottom_line)
    public ImageView iv_bottom_line;

    private int currIndex = 0;
    private int bottomLineWidth;
    private int offset = 0;
    private int position_one;
    private int position_two;
    private Resources resources;

    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addView(R.layout.activity_task, true);
        position = getIntent().getExtras().getInt("position");
        resources = getResources();
        tv_submit.setText("添加");
        tv_title.setText("任务列表");
        InitWidth();
        InitViewPager();
        mViewPager.setCurrentItem(position);
    }


    private void InitViewPager() {
        fragments = new ArrayList<>();

        fragments.add(new TaskdingFragment());
        fragments.add(new TaskedFragment());
        fragments.add(new TaskdelFragment());
        mViewPager.setAdapter(new TaskFragmentAdapter(getSupportFragmentManager(), fragments));
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    private void InitWidth() {
        int bili = 2;
        int yekaNum = 3;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        offset = (int)(screenW/yekaNum - screenW / (yekaNum*bili))/2;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenW / (yekaNum*bili), 5);
        params.leftMargin = offset;
        iv_bottom_line.setLayoutParams(params);
        bottomLineWidth = iv_bottom_line.getLayoutParams().width;
        position_one = (int) (screenW / yekaNum);
        position_two = (int)(screenW / yekaNum * 2);

    }

    @OnClick(R.id.tv_submit)
    void OnAddSite(){
        Bundle bundle = new Bundle();
        bundle.putInt("rod_number", 0);
        bundle.putInt("state", -1);
        bundle.putString("rod_number_parent", "");
        gotoActivity(AddSiteActivity.class, false, bundle);
    }

    @OnClick({R.id.tv_tasking, R.id.tv_tasked, R.id.tv_taskdel})
    void OnTask(View v){
        int index = 0;
        if (v.getId() == R.id.tv_tasking)
            index = 0;
        else if (v.getId() == R.id.tv_tasked)
            index = 1;
        else
            index = 2;
        mViewPager.setCurrentItem(index);
    }

    public void gotoDetail(String task_id, int state){
        Bundle bundle = new Bundle();
        bundle.putString("task_id", task_id);
        bundle.putInt("state", state);
        bundle.putString("rod_number_parent", "");
        gotoActivity(SiteDetailActivity.class, false, bundle);
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
                case 0:
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(position_one, 0, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(position_two, 0, 0, 0);
                    }
                    iv_bottom_line.setImageDrawable(resources.getDrawable(R.color.text_green));
                    break;
                case 1:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, position_one, 0,0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(position_two, position_one, 0, 0);
                    }
                    iv_bottom_line.setImageDrawable(resources.getDrawable(R.color.text_blue));
                    break;
                case 2:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, position_two, 0,0);
                    } else if (currIndex == 1) {
                        animation = new TranslateAnimation(position_one, position_two, 0, 0);
                    }
                    iv_bottom_line.setImageDrawable(resources.getDrawable(R.color.text_red));
                    break;
            }
            currIndex = arg0;
            animation.setFillAfter(true);
            animation.setDuration(300);
            iv_bottom_line.startAnimation(animation);
        }



        @Override

        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }



        @Override

        public void onPageScrollStateChanged(int arg0) {

        }

    }




}
