package com.example.channel.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

/**
 * Created by lingcheng on 2014/7/16.
 */
public class ImageUtil {

	/**
	 * 获取文件指定文件的指定单位的大小
	 *
	 * @param filePath
	 *            文件路径
	 * @return double值的大小
	 */
	public static String getFileOrFilesSize(String filePath) {
		File file = new File(filePath);
		long blockSize = 0;
		try {
			if (file.isDirectory()) {
				blockSize = getFileSizes(file);
			} else {
				blockSize = getFileSize(file);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("获取文件大小", "获取失败!");
		}
		return formatFileSize(blockSize);
	}


	/**
	 * Try to return the absolute file path from the given Uri
	 *
	 * @param context
	 * @param uri
	 * @return the file path or null
	 */
	public static String getRealFilePath( final Context context, final Uri uri ) {
		if (null == uri) return null;
		final String scheme = uri.getScheme();
		String data = null;
		if (scheme == null)
			data = uri.getPath();
		else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
			data = uri.getPath();
		} else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
			Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
			if (null != cursor) {
				if (cursor.moveToFirst()) {
					int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
					if (index > -1) {
						data = cursor.getString(index);
					}
				}
				cursor.close();
			}
		}
		return data;
	}

	/**
	 * 获取指定文件大小
	 *
	 * @param
	 * @return
	 * @throws Exception
	 */
	private static long getFileSize(File file) throws Exception {
		long size = 0;
		if (file.exists()) {
			FileInputStream fis = null;
			fis = new FileInputStream(file);
			size = fis.available();
		} else {
			file.createNewFile();
			Log.e("获取文件大小", "文件不存在!");
		}
		return size;
	}
	/**
	 * 获取指定文件夹
	 *
	 * @param f
	 * @return
	 * @throws Exception
	 */
	private static long getFileSizes(File f) throws Exception {
		long size = 0;
		File flist[] = f.listFiles();
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getFileSizes(flist[i]);
			} else {
				size = size + getFileSize(flist[i]);
			}
		}
		return size;
	}

	/**
	 * 转换文件大小,指定转换的类型
	 *
	 * @param fileS
	 * @return
	 */
	private static String formatFileSize(long fileS) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		String wrongSize = "0B";
		if (fileS == 0) {
			return wrongSize;
		}
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "KB";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "MB";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "GB";
		}

		if (fileS < 1048576 * 2) { // 限制图片大小不能大于2MB
			return fileSizeString;
		}
		return null;

	}

	public static String getImgName(String url){
		String temp[] = url.replaceAll("\\\\","/").split("/");
		String fileName = "";
		if(temp.length > 1){
			fileName = temp[temp.length - 1];
		}
		return fileName;
	}

	/**
	 * 得到一个可用的缓存目录(如果外部可用使用外部,否则内部)。
	 *
	 * @param context
	 *            上下文信息
	 * @param uniqueName
	 *            目录名字
	 * @return 返回目录名字
	 */
	public static File getDiskCacheDir(Context context, String uniqueName) {
		// 检查是否安装或存储媒体是内置的,如果是这样,试着使用
		// 外部缓存 目录
		// 否则使用内部缓存 目录
		final String cachePath = Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState()) ? getExternalCacheDir(context)
				.getPath() : context.getCacheDir().getPath();
		File appCacheDir = new File(cachePath + File.separator + uniqueName);
		if (!appCacheDir.exists()) {
			if (!appCacheDir.mkdirs()) {
				return null;
			}
			try {
				new File(appCacheDir, ".nomedia").createNewFile();
			} catch (IOException e) {
			}
		}
		return appCacheDir;
	}

	/**
	 * 获得外部应用程序缓存目录
	 *
	 * @param context
	 *            上下文信息
	 * @return 外部缓存目录
	 */
	public static File getExternalCacheDir(Context context) {
		if (hasFroyo()) {
			return context.getExternalCacheDir();
		}
		final String cacheDir = "/Android/data/" + context.getPackageName()
				+ "/cache/";
		File appCacheDir = null;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			appCacheDir = new File(Environment.getExternalStorageDirectory()
					.getPath() + cacheDir);
			if (!appCacheDir.exists()) {
				if (!appCacheDir.mkdirs()) {
					return null;
				}
				try {
					new File(appCacheDir, ".nomedia").createNewFile();
				} catch (IOException e) {
				}
			}
		}
		return appCacheDir;
	}

	/**
	 * 当前Android系统版本是否在（ Froyo） Android 2.2或 Android 2.2以上
	 *
	 * @return
	 */
	public static boolean hasFroyo() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	/**
	 * 柔化效果(高斯模糊)(优化后比上面快三倍)
	 *
	 * @param bmp
	 * @return
	 */
	public static Bitmap blurImageAmeliorate(Bitmap bmp) {
		long start = System.currentTimeMillis();
		// 高斯矩阵
		int[] gauss = new int[]{1, 2, 1, 2, 4, 2, 1, 2, 1};

		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

		int pixR = 0;
		int pixG = 0;
		int pixB = 0;

		int pixColor = 0;

		int newR = 0;
		int newG = 0;
		int newB = 0;

		int delta = 36; // 值越小图片会越亮，越大则越暗

		int idx = 0;
		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		for (int i = 1, length = height - 1; i < length; i++) {
			for (int k = 1, len = width - 1; k < len; k++) {
				idx = 0;
				for (int m = -1; m <= 1; m++) {
					for (int n = -1; n <= 1; n++) {
						pixColor = pixels[(i + m) * width + k + n];
						pixR = Color.red(pixColor);
						pixG = Color.green(pixColor);
						pixB = Color.blue(pixColor);

						newR = newR + (int) (pixR * gauss[idx]);
						newG = newG + (int) (pixG * gauss[idx]);
						newB = newB + (int) (pixB * gauss[idx]);
						idx++;
					}
				}

				newR /= delta;
				newG /= delta;
				newB /= delta;

				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));

				pixels[i * width + k] = Color.argb(255, newR, newG, newB);

				newR = 0;
				newG = 0;
				newB = 0;
			}
		}

		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		long end = System.currentTimeMillis();
		Log.d("may", "used time=" + (end - start));
		return bitmap;
	}
	
	/**
	 * 保存图片
	 * @throws IOException
	 */
	public static void saveBitmap(Bitmap bm, File file) throws IOException{
		bm = resizeImage(bm, 800, 1024);
		if (file.exists()) {
			file.delete();
		}
		try {
			FileOutputStream out = new FileOutputStream(file);
			bm.compress(Bitmap.CompressFormat.JPEG, 80, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public class SaveBitmap extends AsyncTask<Void, Void, Void>{
		private Bitmap bm; 
		private File file;
		
		public SaveBitmap(Bitmap bm, File file){
			this.bm = bm;
			this.file = file;
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			if (file.exists()) {
				file.delete();
			}
			try {
				FileOutputStream out = new FileOutputStream(file);
				bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
				out.flush();
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	/**
	 * 根据指定的图像路径和大小来获取缩略图
	 * 此方法有两点好处：
	 * 1. 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
	 * 第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。
	 * 2. 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使
	 * 用这个工具生成的图像不会被拉伸。
	 *
	 * @param imagePath 图像的路径
	 * @param width     指定输出图像的宽度
	 * @param height    指定输出图像的高度
	 * @return 生成的缩略图
	 */
	public static Bitmap getImageThumbnail(String imagePath, int width, int height) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高，注意此处的bitmap为null
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		options.inJustDecodeBounds = false; // 设为 false
		// 计算缩放比
		int h = options.outHeight;
		int w = options.outWidth;
		int beWidth = w / width;
		int beHeight = h / height;
		int be = 1;
		if (beWidth < beHeight) {
			be = beWidth;
		} else {
			be = beHeight;
		}
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = be;
		// 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		// 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

	public static Bitmap getImageThumbnail(String imagePath, int width) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高，注意此处的bitmap为null
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		options.inJustDecodeBounds = false; // 设为 false
		// 计算缩放比
		int h = options.outHeight;
		int w = options.outWidth;
		int beWidth = w / width;
		int be = beWidth;
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = be;
		// 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		// 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, width * h / w,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

	/**
	 * 按照比例
	 */
	public static Bitmap getImageThumbnailAuto(String imagePath, int width) throws Exception {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高，注意此处的bitmap为null
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		options.inJustDecodeBounds = false; // 设为 false
		// 计算缩放比
		int h = options.outHeight;
		int w = options.outWidth;
		int beWidth = w / width;
		int height = width * h / w;
		int beHeight = h / height;
		int be = 1;
		if (beWidth < beHeight) {
			be = beWidth;
		} else {
			be = beHeight;
		}
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = be;
		// 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		// 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
		Bitmap thumb = null;
		if (be != 1) {
			thumb = ThumbnailUtils.extractThumbnail(bitmap, width, height,
					ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
			bitmap.recycle();
			return thumb;
		}
		return bitmap;
	}

	/**
	 * 获取视频的缩略图
	 * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
	 * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
	 *
	 * @param videoPath 视频的路径
	 * @param width     指定输出视频缩略图的宽度
	 * @param height    指定输出视频缩略图的高度度
	 * @param kind      参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
	 *                  其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
	 * @return 指定大小的视频缩略图
	 */
	private Bitmap getVideoThumbnail(String videoPath, int width, int height,
	                                 int kind) {
		Bitmap bitmap = null;
		// 获取视频的缩略图
		bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
		System.out.println("w" + bitmap.getWidth());
		System.out.println("h" + bitmap.getHeight());
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

	//递归删除文件及文件夹
	public static void delete(File file) {
		if (file == null)
			return;
		if (file.isFile()) {
			file.delete();
			return;
		}

		if(file.isDirectory()){
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				file.delete();
				return;
			}

			for (int i = 0; i < childFiles.length; i++) {
				delete(childFiles[i]);
			}
			file.delete();
		}
	}
	
	/**
	 * 读取图片属性：旋转的角度
	 * 
	 * @param path 图片绝对路径
	 * @return degree 旋转的角度
	 * @throws IOException
	 */
	public static int gainPictureDegree(String path) throws Exception {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		} catch (Exception e) {
			throw(e);
		}
		
		return degree;
	}
	
	/**
     * 旋转图片 
     * @param angle 角度
     * @param bitmap 源bitmap
     * @return Bitmap 旋转角度之后的bitmap
     */  
    public static Bitmap rotaingBitmap(int angle,Bitmap bitmap) {  
        //旋转图片 动作   
        Matrix matrix = new Matrix();;  
        matrix.postRotate(angle);  
        //重新构建Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix, true);  
        return resizedBitmap;  
    }
    
    /**
	 * 矫正旋转角度后的图像
	 * @param filePath 图片文件
	 * @return 矫正角度后的图像
	 */
	public static Bitmap getDiskBitmap(String filePath) {
		Bitmap bitmap = null;
		try {
			File file = new File(filePath);
			if (file.exists())
				bitmap = BitmapFactory.decodeFile(filePath);

			// 获取图片的旋转角度，有些系统把拍照的图片旋转了，有的没有旋转
			int degree = ImageUtil.gainPictureDegree(filePath);
			if (degree != 0)
				bitmap = ImageUtil.rotaingBitmap(degree, bitmap);
		} catch (Exception e) {
			Log.e("img", e.getMessage());
		}

		return bitmap;
	}
	
	/**
	 * 图片压缩方法实现
	 * 
	 * @param srcPath
	 * @returnc
	 */
	public static Bitmap getimage(String srcPath)
	{
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		int hh = 1024;// 这里设置高度为800f
		int ww = 800;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww)
		{// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh)
		{// 如果高度高的话根据高度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return bitmap;// 压缩好比例大小后再进行质量压缩
	}
	
	public static Bitmap resizeImage(Bitmap bitmap, int w, int h) 
    {  
        Bitmap BitmapOrg = bitmap;  
        int width = BitmapOrg.getWidth();  
        int height = BitmapOrg.getHeight();  
        int newWidth = w;  
        int newHeight = h;  
 
        float scaleWidth = ((float) newWidth) / width;  
        float scaleHeight = ((float) newHeight) / height;  
 
        Matrix matrix = new Matrix();  
        matrix.postScale(scaleWidth, scaleHeight);  
        // if you want to rotate the Bitmap   
        // matrix.postRotate(45);   
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,  
                        height, matrix, true);  
        return resizedBitmap;  
    }

	/**
	 * 质量压缩
	 * 
	 * @param image
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image , String path)
	{

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 200)
		{ // 循环判断如果压缩后图片是否大于200kb,大于继续压缩
			baos.reset(); // 重置baos即清空baos
			options -= 10; // 每次都减少10
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中

		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
			int height = bitmap.getHeight();
			int width = bitmap.getWidth();
			long size = baos.toByteArray().length;
//		try
//		{
//			File file = new File(path);
//			FileOutputStream out = new FileOutputStream(file);
//			bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
//			out.flush();
//			out.close();
//		} catch (Exception e){
//			System.out.println(e.getMessage());
//		}
		
		return bitmap;
	}

	public static String getImageType(String path){
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		return options.outMimeType;
	}
}
