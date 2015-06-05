package com.zylbaby.app.global;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Environment;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.zylbaby.app.R;
public class AppConstant {
	
	
	public static final String IMAGE_PATH = "babysaga";
	public static final File FILE_SDCARD = Environment
            .getExternalStorageDirectory();
	public static final File FILE_LOCAL = 
			new File(AppConstant.FILE_SDCARD,AppConstant.IMAGE_PATH);
	

	public static int[] babystateioc_array={
		R.drawable.babystate_good,R.drawable.babystate_naughty
	};
	public static String[] babystatetext_array={
		"好","淘气"
	};
	public static int[] babtstateid_array={
		1,2
	};
	
	
	
	
	public static int[] starsioc_array={
		R.drawable.stars_1,R.drawable.stars_2,
		R.drawable.stars_3,R.drawable.stars_4,
		R.drawable.stars_5
	};
	public static int[] starsid_array={
		1,2,3,4,5
	};
	
	
	public static String[] diarytype_array=new String[]{
			"大便","小便",
			"睡觉","午睡",
			"吃饭","零食",
			"室内个人活动","室内集体活动",
			"室外个人活动","室外集体活动",
			"洗漱","其他","起床","洗澡","生病","到校","离校"
	};
	public static int[] diaryioc_array1=new int[]{
		R.drawable.diary_bianbian,R.drawable.diary_xiaobian,
		R.drawable.diary_wanshui,R.drawable.diary_wushui,
		R.drawable.diary_chifan,R.drawable.diary_lingshi,
		R.drawable.diary_shineigeren,R.drawable.diary_shineijiti,
		R.drawable.diary_shiwaigeren,R.drawable.diary_shiwaijiti,
		R.drawable.diary_xishu,R.drawable.diary_other,
		R.drawable.diary_schoolin,R.drawable.diary_schoolout
		
	};
	public static int[] diaryioc_array=new int[]{
		R.drawable.diary_bianbian_1,R.drawable.diary_xiaobian_1,
		R.drawable.diary_wanshui_1,R.drawable.diary_wushui_1,
		R.drawable.diary_chifan_1,R.drawable.diary_lingshi_1,
		R.drawable.diary_shineigeren_1,R.drawable.diary_shineijiti_1,
		R.drawable.diary_shiwaigeren_1,R.drawable.diary_shiwaijiti_1,
		R.drawable.diary_xishu_1,R.drawable.diary_other_1,
		R.drawable.diary_qichuang_1,R.drawable.diary_xizao_1,
		R.drawable.diary_shengbing_1,
		R.drawable.diary_schoolin,R.drawable.diary_schoolout
		
	};
	public static int[] diarytypeid_array=new int[]{
			1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,20,30
			
	};
	
	
	public static String[] help_title=new String[]{
		"急救第一步",
		"噎食(3岁以下)",
		"噎食(3岁以上)",
		"心肺复苏CPR",
		"止血",
		"烫伤",
		"溺水"
	};
	public static String[] help_pic=new String[]{
		"1_First.jpg",
		"2_Chocking 3D.jpg",
		"3_Chocking 3U.jpg",
		"4_Heart.jpg",
		"5_Blood.jpg",
		"6_Burn.jpg",
		"7_Water.jpg"
	};
	
	
	
	public static DisplayImageOptions  options = new DisplayImageOptions.Builder()
	.showImageForEmptyUri(R.drawable.ic_empty)
	.showImageOnFail(R.drawable.ic_error)
	.resetViewBeforeLoading(true)
	
	.cacheOnDisc(true)
	.delayBeforeLoading(0)
	.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
	.bitmapConfig(Bitmap.Config.RGB_565)
	
	.build();
	
	
	
	
	
	

	
	
	
	
	
}
