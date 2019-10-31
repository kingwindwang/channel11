package com.example.channel.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.channel.R;
import com.example.channel.activity.MainActivity;
import com.example.channel.activity.SiteDetailActivity;
import com.example.channel.activity.TaskActivity;
import com.example.channel.adapter.SiteAdapter;
import com.example.channel.model.impl.SiteModelImpl;
import com.example.channel.present.TaskPresent;
import com.example.channel.present.impl.TaskPresentImpl;
import com.example.channel.utils.LoadDialog;
import com.example.channel.view.TaskView;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2019/10/10 0010.
 */

public class TaskdingFragment extends Fragment implements TaskView{

    private Unbinder unbinder;

    @BindView(R.id.lv_task)
    public PullLoadMoreRecyclerView lv;

    private List<SiteModelImpl> siteModels;

    private int state=0, page=0;
    private LoadDialog loadDialog;

    private SiteAdapter siteAdapter;
    private TaskPresent taskPresent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_task_list, null);
        unbinder = ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadDialog = new LoadDialog(getActivity());
        lv.setIsLoadMore(true);
        taskPresent = new TaskPresentImpl(this);
        page = 0;
        siteModels = new ArrayList<>();
        taskPresent.showTask(state, page, true);
    }

    @Override
    public void findTask(List<SiteModelImpl> siteModelList) {
        if (siteModelList.size() < 20){
            lv.setIsLoadMore(false);
        }
        siteModels.addAll(siteModelList);
        lv.setPullLoadMoreCompleted();
        setAdapter();
    }

    private void setAdapter(){
        lv.setLinearLayout();
        siteAdapter = new SiteAdapter((TaskActivity) getActivity(), siteModels);
        lv.setAdapter(siteAdapter);
        lv.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                siteModels.clear();
                page = 0;
                lv.setIsLoadMore(true);
                taskPresent.showTask(state, page, false);
            }

            @Override
            public void onLoadMore() {
                page++;
                taskPresent.showTask(state, page, false);
            }
        });
    }

    @Override
    public void fail(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        lv.setPullLoadMoreCompleted();
    }

    @Override
    public void onDestroyView() {
        if (unbinder != null)
            unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void showProgress() {
        if (loadDialog != null)
            loadDialog.builder();
    }

    @Override
    public void hideProgress() {
        if (loadDialog != null)
            loadDialog.dismiss();
    }
}
