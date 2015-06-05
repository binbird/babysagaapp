package com.photo.select;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;


public class BaseSinglePhotoSelect {

	public static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
	public static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	public static final int PHOTO_REQUEST_CUT = 3;// 结果
	
	private Context context;
	
	public BaseSinglePhotoSelect(Context context){
		this.context=context;
	}
	
	
	// 使用系统当前日期加以调整作为照片的名称
		public String getPhotoFileName() {
			Date date = new Date(System.currentTimeMillis());
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"'IMG'_yyyyMMdd_HHmmss");
			return dateFormat.format(date) + ".jpg";
		}
		
		


	
}
