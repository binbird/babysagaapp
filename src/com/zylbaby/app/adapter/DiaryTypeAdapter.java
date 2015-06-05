package com.zylbaby.app.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.zylbaby.app.R;
import com.zylbaby.app.adapter.BanjiBabyAdapter.Holder;
import com.zylbaby.app.bean.BanjiBabyAndInOutBean;
import com.zylbaby.app.global.AppConstant;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DiaryTypeAdapter extends BaseAdapter{

	
	private ArrayList<HashMap<String, Object>> diarytypelist;
	private Context context;
	private LayoutInflater inflater;
	
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	
	
//	private DisplayImageOptions  options = new DisplayImageOptions.Builder()
//	.showImageForEmptyUri(R.drawable.baby_default)
//	.showImageOnFail(R.drawable.baby_default)
//	.resetViewBeforeLoading(true)
//	.cacheOnDisc(true)
//	.imageScaleType(ImageScaleType.EXACTLY)
//	.bitmapConfig(Bitmap.Config.ARGB_8888)
//	.build();
	
	public DiaryTypeAdapter(Context context,ArrayList<HashMap<String, Object>> list){
		this.context=context;
		this.diarytypelist=list;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {

		return diarytypelist.size();
	}

	@Override
	public Object getItem(int position) {

		return diarytypelist.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
//		R.layout.diarytype_griditem
		
		View view = convertView; 
		HashMap<String,Object> dto = diarytypelist.get(position);
		
		Holder holder = null;
		
		if (view == null) {
			holder = new Holder();
			view = inflater.inflate(R.layout.diarytype_griditem, null);
			holder.txtname = (TextView) view.findViewById(R.id.ItemText);
			holder.Typeimg=(ImageView)view.findViewById(R.id.ItemImage);

			view.setTag(holder);
		}else {
			holder = (Holder) view.getTag();
			
		}
		
		holder.txtname.setText(String.valueOf(dto.get("ItemText")));		

		//String imgurl = Scheme.DRAWABLE.wrap(String.valueOf(dto.get("ItemImage")));
		String imgurl = "drawable://" +dto.get("ItemImage");
		imageLoader  
        .displayImage(imgurl, holder.Typeimg, AppConstant.options); 
		
		

		final int fpostion = position;	
		final Holder fholder = holder;

		
		
		return view;
	}
	
	
	static class Holder{
		TextView txtname;
		ImageView Typeimg;
		
		
	}

}
