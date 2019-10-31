package com.example.channel.utils;

import android.app.Activity;


import com.example.channel.R;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Admin on 2019/8/9.
 * 编写人：li
 * 功能描述：
 */
public class CacheActivity {

    public static List<Activity> activityList = new LinkedList<Activity>();

    public CacheActivity() {

    }

    /**
     * 添加到Activity容器中
     */
    public static void addActivity(Activity activity) {
        if (!activityList.contains(activity)) {
            activityList.add(activity);
        }
    }

    /**
     * 便利所有Activigty并finish
     */
    public static void finishActivity() {
        for (Activity activity : activityList) {
            activity.finish();
        }
    }

    public static void finishActivityNum(int num){
        for (int i = 0; i < num; i++){
            activityList.get(activityList.size()-i-1).finish();
        }
    }

    /**
     * 便利所有Activigty并finish
     */
//    public static void backMainActivity() {
//        for (Activity activity : activityList) {
//            if (!activity.getClass().equals(MainActivity.class))
//                activity.finish();
//        }
//    }

    /**
     * 结束指定的Activity
     */
    public static void finishSingleActivity(Activity activity) {
        if (activity != null) {
            if (activityList.contains(activity)) {
                activityList.remove(activity);
            }
            activity = null;
        }
    }

    /**
     * 结束指定的Activity
     */
    public static void backActivity(Activity activity) {
        if (activity != null) {
            if (activityList.contains(activity)) {
                activityList.remove(activity);
            }
            activity.finish();
            activity.overridePendingTransition(R.anim.anim_left_in,R.anim.anim_right_out);
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity 在遍历一个列表的时候不能执行删除操作，所有我们先记住要删除的对象，遍历之后才去删除。
     */
    public static void finishSingleActivityByClass(Class<?> cls) {
        Activity tempActivity = null;
        for (Activity activity : activityList) {
            if (activity.getClass().equals(cls)) {
                tempActivity = activity;
            }
        }

        finishSingleActivity(tempActivity);
    }
}
