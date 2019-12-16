package com.example.channel.activity;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.example.channel.App;
import com.example.channel.R;
import com.example.channel.adapter.ImageAdapter;
import com.example.channel.utils.ImageUtil;
import com.example.channel.utils.Location;
import com.foamtrace.photopicker.ImageCaptureManager;
import com.foamtrace.photopicker.ImageConfig;
import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.OnClick;

public class CameraActivity extends BaseActivity{

	@BindView(R.id.gv_img)
	public GridView gv_img;
	@BindView(R.id.tv)
	public TextView tv;

	private ImageCaptureManager captureManager; // 相机拍照处理类
	private Location mLocationService;

	private String latitude,longtitude,addr;//维度，经度，地址，图片路径
	private ArrayList<String> urls;
	private int rod_number;
	private ImageAdapter imageAdapter;
	private int select = -1;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		addView(R.layout.activity_camera, true);
		if (!hasPermission(Manifest.permission.CAMERA,
				Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION,
				Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE)){

			requestPermission(1, Manifest.permission.CAMERA,
					Manifest.permission.READ_EXTERNAL_STORAGE,
					Manifest.permission.ACCESS_FINE_LOCATION,
					Manifest.permission.ACCESS_COARSE_LOCATION,
					Manifest.permission.WRITE_EXTERNAL_STORAGE);
		}
		initView();
	}

	private void initView(){
		rod_number = getIntent().getIntExtra("rod_number", 0);
		if (rod_number == -2){
			tv_submit.setVisibility(View.GONE);
		}
		tv_submit.setText("上传");
		setAddView();
		tv_add.setText("提交");

		urls = getIntent().getExtras().getStringArrayList("urls");
		if (urls != null && urls.size() > 0){
			addr = getIntent().getExtras().getString("address");
			longtitude = getIntent().getExtras().getString("lont");
			latitude = getIntent().getExtras().getString("lat");
			show(2);
			showTv();
		}



	}

	private void setAddView(){
		if (TextUtils.isEmpty(addr) || urls.size() == 0)
			tv_add.setVisibility(View.GONE);
		else
			tv_add.setVisibility(View.VISIBLE);
	}

	@OnClick({R.id.tv_submit, R.id.tv_add, R.id.tv})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.tv_submit:
				select = -1;
				showDialog("上传图片", "拍照", "图库", 1);
				break;
			case R.id.tv_add:
				Intent in = new Intent();
				in.putExtra("address", addr);
				in.putExtra("lont", longtitude);
				in.putExtra("lat", latitude);
				in.putStringArrayListExtra("urls", urls);
				setResult(App.SITE_PHONE, in);
				back();
				break;
			case R.id.tv:
				Intent in1 = new Intent(CameraActivity.this, BaiduPoiActivity.class);
				startActivityForResult(in1, 100);
				break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		String url = "";
		if (resultCode == RESULT_OK && requestCode == PHOTO_REQUEST_CAREMA){
			url = ImageUtil.getRealFilePath(this, imageUri);
		} else if (resultCode == RESULT_OK && requestCode == 2){
			if (data == null)
				return;
			url = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT).get(0);
		}else if (resultCode == 100){
			if (data == null)
				return;
			latitude = data.getStringExtra("lat");
			longtitude = data.getStringExtra("lon");
			addr = data.getStringExtra("address");
			setAddView();
			showTv();
			return;
		}
		if (!TextUtils.isEmpty(url))
			if (select == -1)
				urls.add(url);
			else{
				urls.remove(select);
				urls.add(select, url);
			}
		show(1);
	}

	//type=1添加修改；tpye=2查看，不启动定位
	private void show(int type){
		try {
			if (imageAdapter == null){
				imageAdapter = new ImageAdapter(CameraActivity.this, urls);
				gv_img.setAdapter(imageAdapter);
			}else
				imageAdapter.notifyDataSetChanged();
			if (rod_number != -2 && type == 1){
				setAddView();
				if (!mLocationService.isStart())
					mLocationService.start();
			}
			if (rod_number != -2){
				gv_img.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
						select = position;
						showDialog("是否删除", "取消", "删除", 3);
						return true;
					}
				});
				gv_img.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						select = position;
						showDialog("替换图片", "拍照", "图库", 2);
					}
				});
			}


		}catch (Exception e){

		}
	}

	//type 1：上传拍照；2：修改拍照；3：删除
	private void showDialog(String title, String left, String right, int type){
		AlertDialog alertDialog2 = new AlertDialog.Builder(CameraActivity.this)
				.setTitle(title)
				.setPositiveButton(right, new DialogInterface.OnClickListener() {//添加"Yes"按钮
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						if (type == 3){
							urls.remove(select);
							show(1);
						}else {
							PhotoPickerIntent intent = new PhotoPickerIntent(CameraActivity.this);
							intent.setSelectModel(SelectModel.SINGLE);
							startActivityForResult(intent, 2);
						}
					}
				})

				.setNegativeButton(left, new DialogInterface.OnClickListener() {//添加取消
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						if (type != 3)
							openCamera();
					}
				})
				.create();
		alertDialog2.show();

	}

	private Uri imageUri;

	public static File tempFile;
	public static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
	public void openCamera() {
		//獲取系統版本
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		// 激活相机
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// 判断存储卡是否可以用，可用进行存储
		if (hasSdcard()) {
			SimpleDateFormat timeStampFormat = new SimpleDateFormat(
					"yyyy_MM_dd_HH_mm_ss");
			String filename = timeStampFormat.format(new Date());
			tempFile = new File(Environment.getExternalStorageDirectory(),
					filename + ".jpg");
			if (currentapiVersion < 24) {
				// 从文件中创建uri
				imageUri = Uri.fromFile(tempFile);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			} else {
				//兼容android7.0 使用共享文件的形式
				ContentValues contentValues = new ContentValues(1);
				contentValues.put(MediaStore.Images.Media.DATA, tempFile.getAbsolutePath());
				//检查是否有存储权限，以免崩溃
				if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
						!= PackageManager.PERMISSION_GRANTED) {
					//申请WRITE_EXTERNAL_STORAGE权限
					Toast.makeText(this,"请开启存储权限",Toast.LENGTH_SHORT).show();
					return;
				}
				imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			}
		}
		// 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
		startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
	}

	/*
* 判断sdcard是否被挂载
*/
	public static boolean hasSdcard() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	private void showTv(){
		StringBuffer sb = new StringBuffer(256);
		sb.append("纬度 : ");// 纬度
		sb.append(latitude);
		sb.append("\n经度 : ");// 经度
		sb.append(longtitude);
		sb.append("\n地址 : ");// 地址信息
		sb.append(addr);
		tv.setText(sb.toString());
	}

}
