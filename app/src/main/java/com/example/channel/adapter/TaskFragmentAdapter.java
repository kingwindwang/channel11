package com.example.channel.adapter;

import android.view.View;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

/**
 * Created by Administrator on 2019/10/11 0011.
 */

public class TaskFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;

    public TaskFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList){
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

}
