package com.baby.common.ui;

import uk.co.senab.photoview.PhotoView;

import com.loveplusplus.demo.image.ImagePagerActivity;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.HackyViewPager;
import com.zylbaby.app.R;
import com.zylbaby.app.global.AppConstant;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class HelpPicShowActivity extends Activity {
	
	private final static String PATH="http://www.babysaga.cn/AidPic/";
//	protected ImageLoader imageLoader = ImageLoader.getInstance();
	//DisplayImageOptions options;
	private static final String STATE_POSITION = "STATE_POSITION";

	private static final String IMAGES = "images";

	private static final String IMAGE_POSITION = "image_index";
	
	

	HackyViewPager pager;
	private String picpath;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_pic_show_activity);
		
		
		
		Bundle bundle = getIntent().getExtras();
		picpath=bundle.getString("images");
		picpath=PATH+picpath;
		String[] imageUrls=new String[]{picpath};
		
		

//		int pagerPosition = bundle.getInt(IMAGE_POSITION, 0);
//
//		if (savedInstanceState != null) {
//			pagerPosition = savedInstanceState.getInt(STATE_POSITION);
//		}

//		options = new DisplayImageOptions.Builder()
//			.showImageForEmptyUri(R.drawable.ic_empty)
//			.showImageOnFail(R.drawable.ic_error)
//			.resetViewBeforeLoading(true)
//			.cacheOnDisc(true)
//			.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
//			.bitmapConfig(Bitmap.Config.ARGB_8888)
//			.displayer(new FadeInBitmapDisplayer(300))
//			.build();

		pager = (HackyViewPager) findViewById(R.id.pager);
		pager.setAdapter(new ImagePagerAdapter(imageUrls,this));
		pager.setCurrentItem(0);

	}

	

	
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, pager.getCurrentItem());
	}

	
	private class ImagePagerAdapter extends PagerAdapter {

		private String[] images;
		private LayoutInflater inflater;
		private Context mContext;

		ImagePagerAdapter(String[] images,Context context) {
			this.images = images;
			this.mContext=context;
			inflater = getLayoutInflater();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}

		@Override
		public void finishUpdate(View container) {
		}

		@Override
		public int getCount() {
			return images.length;
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			View imageLayout = inflater.inflate(R.layout.item_pager_image, view, false);
			
			PhotoView imageView = (PhotoView) imageLayout.findViewById(R.id.image);
			final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);

			ImageLoader.getInstance().displayImage(images[position], imageView, AppConstant.options, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					spinner.setVisibility(View.VISIBLE);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
					String message = null;
					switch (failReason.getType()) {
						case IO_ERROR:
							message = "Input/Output error";
							break;
						case DECODING_ERROR:
							message = "Image can't be decoded";
							break;
						case NETWORK_DENIED:
							message = "Downloads are denied";
							break;
						case OUT_OF_MEMORY:
							message = "Out Of Memory error";
							break;
						case UNKNOWN:
							message = "Unknown error";
							break;
					}
					//Toast.makeText(ImagePagerActivity.this, message, Toast.LENGTH_SHORT).show();

					spinner.setVisibility(View.GONE);
				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					spinner.setVisibility(View.GONE);
				}
			});

			((ViewPager) view).addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View container) {
		}
	}



	
}
