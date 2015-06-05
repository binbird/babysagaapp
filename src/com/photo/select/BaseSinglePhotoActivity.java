package com.photo.select;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.zylbaby.app.R;
import com.zylbaby.app.global.AppConstant;
import com.zylbaby.app.util.ImgLoader;
import com.zylbaby.app.view.BootstrapCircleThumbnail;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

public class BaseSinglePhotoActivity  extends Activity{
	
	
	public static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
	public static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	public static final int PHOTO_REQUEST_CUT = 3;// 结果
	public BootstrapCircleThumbnail add_baby_image;
	public File tempFile ;
	public Bitmap bitmaptemp = null;
	public ProgressDialog pd;
	// 设置标记
	public final int SIGN = 0x11;
	// 提示对话框方法
	public void showDialog() {
				new AlertDialog.Builder(this)
						.setTitle("头像设置")
						.setPositiveButton("拍照", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
								// 调用系统的拍照功能
								Intent intent = new Intent(
										MediaStore.ACTION_IMAGE_CAPTURE);
								// 指定调用相机拍照后照片的储存路径
								intent.putExtra(MediaStore.EXTRA_OUTPUT,
										Uri.fromFile(tempFile));
								startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
							}
						})
						.setNegativeButton("相册", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
								Intent intent = new Intent(Intent.ACTION_PICK, null);
								intent.setDataAndType(
										MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
										"image/*");
								startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
							}
						}).show();
			}

	public void startPhotoZoom(Uri uri, int size) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop为true是设置在开启的intent中设置显示的view可以剪裁
		intent.putExtra("crop", "true");

		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);

		// outputX,outputY 是剪裁图片的宽高
		intent.putExtra("outputX", size);
		intent.putExtra("outputY", size);
		intent.putExtra("return-data", true);

		startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}

	// 将进行剪裁后的图片显示到UI界面上
	public void setPicToView(Intent picdata) {
		Bundle bundle = picdata.getExtras();
		if (bundle != null) {
			Bitmap photo = bundle.getParcelable("data");
			if(photo != null){
				bitmaptemp = photo;
				Drawable drawable = new BitmapDrawable(photo);
				add_baby_image.setImage(drawable);
			}
		}
	}
	
	public void setPicToView(String url) {
//		Drawable drawable=null;
//		try {
//			drawable = Drawable.createFromStream(new URL(url).openStream(), "image.jpg");
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		if(drawable!=null){
//		add_baby_image.image.setImage(drawable);
//		}
		try{
			ImageLoader.getInstance().displayImage(url, add_baby_image.image,AppConstant.options);
		}catch(Exception err){
			
		}
		
//		Drawable cacheImage = imageLoader.loadDrawable(url,
//				new ImgLoader.ImageCallback() {
//					@Override
//					public void imageLoaded(Drawable imageDrawable,
//							String imageUrl) {
//						// TODO Auto-generated method stub
//						add_baby_image.setImageDrawable(imageDrawable);
//					}
//				});
//		if (cacheImage != null) {
//			add_baby_image.setImageDrawable(cacheImage);
//		}
		
		
	}
	
	// 加载界面
		// 调用onPrepareDialog()方法之后
		@Override
		protected Dialog onCreateDialog(int id, Bundle args) {
			pd = new ProgressDialog(this);
			pd.setMessage("正在加载......");
			pd.setProgressStyle(ProgressDialog.THEME_HOLO_DARK);
			pd.setCancelable(false);
			return pd;
		}

		// 调用showDialog()之后和调用onCreateDialog之前
		@Override
		protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
			switch (id) {
			case SIGN:
				break;
			}
		}

}
