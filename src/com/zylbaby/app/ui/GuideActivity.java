package com.zylbaby.app.ui;

import org.taptwo.android.widget.CircleFlowIndicator;
import org.taptwo.android.widget.ViewFlow;
import org.taptwo.android.widget.ViewFlow.ViewSwitchListener;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.zylbaby.app.R;
import com.zylbaby.app.global.AppConstant;



import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

public class GuideActivity extends Activity{
	private ViewFlow viewFlow;
	
//	private DisplayImageOptions  options = new DisplayImageOptions.Builder()
//	.showImageForEmptyUri(R.drawable.ic_empty)
//	.showImageOnFail(R.drawable.ic_error)
//	.resetViewBeforeLoading(true)
//	.cacheOnDisc(true)
//	.delayBeforeLoading(0)
//	.imageScaleType(ImageScaleType.EXACTLY)
//	.bitmapConfig(Bitmap.Config.ARGB_8888)
//	.build();
	
	//protected ImageLoader imageLoader = ImageLoader.getInstance();
	public int[] ids = { R.drawable.first_time_post_1,
			R.drawable.first_time_post_2, 
			R.drawable.first_time_post_3, 
			R.drawable.first_time_post_4 };

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// requestWindowFeature ??
		setContentView(R.layout.guide_activity);
		//initImageLoader(this);
		viewFlow = (ViewFlow) findViewById(R.id.viewflow);
		viewFlow.setAdapter(new DemoPic());
		viewFlow.setmSideBuffer(ids.length); // 实际图片张数，
		// 我的ImageAdapter实际图片张数为3
		
		CircleFlowIndicator indic = (CircleFlowIndicator) findViewById(R.id.viewflowindic);

		viewFlow.setFlowIndicator(indic);
		viewFlow.setSelection(0); // 设置初始位置
		viewFlow.setOnViewSwitchListener(new ViewSwitchListener() {
			
			@Override
			public void onSwitched(View view, int position) {
				Toast.makeText(GuideActivity.this, String.valueOf(position), Toast.LENGTH_LONG).show();
				
			}
		});
		
		
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(RESULT_OK);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	
	
	

	class DemoPic extends BaseAdapter {
		private LayoutInflater mInflater;

		public DemoPic() {
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return ids.length; 
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			
			View view = convertView; 
			Holder holder = null;
			
//			if (convertView == null) {
//				convertView = mInflater.inflate(R.layout.image_item, null);
//			}

			
			if (view == null) {
				holder = new Holder();
				view = mInflater.inflate(R.layout.image_item, null);
				holder.img=(ImageView)view.findViewById(R.id.imgView);
				view.setTag(holder);
			}else {
				holder = (Holder) view.getTag();
				
			}

			//String drawableUrl = Scheme.DRAWABLE.wrap(String.valueOf(ids[position % ids.length]));
			String drawableUrl = "drawable://" +(ids[position % ids.length]);
			
			
			//图片显示类


			ImageLoader.getInstance().displayImage(drawableUrl, holder.img,AppConstant.options,new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String arg0, View arg1) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
					Log.v("MyImageLoader", arg2.getCause().toString());
					
				}
				
				@Override
				public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
					// TODO Auto-generated method stub
					
				}
			});
			//ImageLoader.getInstance().clearMemoryCache();
			final int fpostion = position;	
			final Holder fholder = holder;
			
			
			if (position == 3) {
				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
					
						ImageLoader.getInstance().clearMemoryCache();
						finish();
					}
				});
			}
			
			
			return view;
			
			
			

		}
	
		
		
	
	}
	
	static class Holder{
		ImageView img;
		Integer position;
		
	}
	
	
}
