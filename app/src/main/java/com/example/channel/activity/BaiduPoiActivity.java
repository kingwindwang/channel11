package com.example.channel.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.DotOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.channel.R;
import com.example.channel.utils.NotificationUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2019/12/9 0009.
 */

public class BaiduPoiActivity extends Activity{

    private LocationClient mClient;
    private MyLocationListener myLocationListener = new MyLocationListener();

    @BindView(R.id.mv_foreground)
    public MapView mMapView;
    private BaiduMap mBaiduMap;

    private NotificationUtils mNotificationUtils;
    private Notification notification;

    private boolean isFirstLoc = true;
    private List<Poi> pois = new ArrayList<>();
    private PoiAdapter adapter;

    @BindView(R.id.lv_address)
    public ListView lv_address;
    @BindView(R.id.tv_lat_lnt)
    public TextView tv_lat_lnt;

    @BindView(R.id.tv_title)
    public TextView tv_title;

    @BindView(R.id.img_back)
    public ImageView img_back;

    @BindView(R.id.tv_submit)
    public TextView tv_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baidu_poi);
        ButterKnife.bind(this);
        initViews();

        tv_title.setText("定位");
        tv_submit.setText("重新定位");

        // 定位初始化
        mClient = new LocationClient(this);
        LocationClientOption mOption = new LocationClientOption();
        mOption.setScanSpan(5000);
        mOption.setCoorType("bd09ll");
        mOption.setIsNeedAddress(true);
        mOption.setOpenGps(true);
        mOption.setIsNeedLocationPoiList(true);
        mClient.setLocOption(mOption);
        mClient.registerLocationListener(myLocationListener);

        //设置后台定位
        //android8.0及以上使用NotificationUtils
        if (Build.VERSION.SDK_INT >= 26) {
            mNotificationUtils = new NotificationUtils(this);
            Notification.Builder builder2 = mNotificationUtils.getAndroidChannelNotification
                    ("适配android 8限制后台定位功能", "正在后台定位");
            notification = builder2.build();
        } else {
            //获取一个Notification构造器
            Notification.Builder builder = new Notification.Builder(BaiduPoiActivity.this);
            Intent nfIntent = new Intent(BaiduPoiActivity.this, BaiduPoiActivity.class);

            builder.setContentIntent(PendingIntent.
                    getActivity(BaiduPoiActivity.this, 0, nfIntent, 0)) // 设置PendingIntent
                    .setContentTitle("适配android 8限制后台定位功能") // 设置下拉列表里的标题
                    .setSmallIcon(R.mipmap.ic_launcher) // 设置状态栏内的小图标
                    .setContentText("正在后台定位") // 设置上下文内容
                    .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间

            notification = builder.build(); // 获取构建好的Notification
        }
        notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
        startBaidu();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mMapView = null;
        // 关闭前台定位服务
        mClient.disableLocInForeground(true);
        // 取消之前注册的 BDAbstractLocationListener 定位监听函数
        mClient.unRegisterLocationListener(myLocationListener);
        // 停止定位sdk
        mClient.stop();
    }

    private void initViews(){
        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBaidu();
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
    }

    private void startBaidu(){
        //开启后台定位
        mClient.enableLocInForeground(1, notification);
        mClient.start();
    }

    private void stopBaidu(){
        mClient.disableLocInForeground(true);
        mClient.stop();
    }

    private String lat, lon;

    class  MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder().accuracy(bdLocation.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(bdLocation.getDirection()).latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude()).build();
            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);
            //地图SDK处理
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(bdLocation.getLatitude(),
                        bdLocation.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
            LatLng point = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            OverlayOptions dotOption = new DotOptions().center(point).color(0xAAA9A9A9);
            mBaiduMap.addOverlay(dotOption);
            StringBuffer sb = new StringBuffer(256);
            sb.append("纬度:");
            lat = bdLocation.getLatitude() + "";
            sb.append(lat+"    ；    ");
            sb.append("经度");
            lon = bdLocation.getLongitude() + "";
            sb.append(lon);
            tv_lat_lnt.setText(sb.toString());
            pois = bdLocation.getPoiList();
            setAdapter();
            stopBaidu();

        }
    }

    private void setAdapter(){
        if (pois == null)
            pois = new ArrayList<>();
        adapter = new PoiAdapter();
        lv_address.setAdapter(adapter);

        lv_address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in = new Intent();
                in.putExtra("lat", lat);
                in.putExtra("lon", lon);
                in.putExtra("address", pois.get(position).getAddr() + pois.get(position).getName());
                setResult(100, in);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    class PoiAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return pois.size();
        }

        @Override
        public Object getItem(int position) {
            return pois.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null){
                convertView = LayoutInflater.from(BaiduPoiActivity.this).inflate(R.layout.adapter_address, null);
                holder = new ViewHolder();
                holder.tv_poi_name = convertView.findViewById(R.id.tv_poi_name);
                holder.tv_poi_address = convertView.findViewById(R.id.tv_poi_address);
                convertView.setTag(holder);
            }else
                holder = (ViewHolder) convertView.getTag();
            holder.tv_poi_name.setText(pois.get(position).getName());
            holder.tv_poi_address.setText(pois.get(position).getAddr());
            return convertView;
        }

        class ViewHolder{
            TextView tv_poi_name, tv_poi_address;
        }
    }
}
