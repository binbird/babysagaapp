package com.zylbaby.app.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.zylbaby.app.R;
import com.zylbaby.app.adapter.BanjiBabyAdapter.Holder;
import com.zylbaby.app.bean.BabyBean;
import com.zylbaby.app.bean.LifePic;
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

public class PicListAdapter extends BaseAdapter {

	protected static Object Holder;


	private List<LifePic> piclist;

	
	private LayoutInflater inflater;
	//private AdpterOnItemClick diaryAdpterOnclick;
	
	private Context context;
	public PicListAdapter(Context context,List<LifePic> piclist) {
		this.context=context;
		this.piclist = piclist;
		inflater = LayoutInflater.from(context);
	}
	
	
	@Override
	public int getCount() {
		return piclist.size();
	}

	@Override
	public Object getItem(int position) {
		return piclist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	
//	private DisplayImageOptions  options = new DisplayImageOptions.Builder()
//	.showImageForEmptyUri(R.drawable.ic_empty)
//	.showImageOnFail(R.drawable.ic_error)
//	.resetViewBeforeLoading(true)
//	.cacheOnDisc(true)
//	.imageScaleType(ImageScaleType.EXACTLY)
//	.bitmapConfig(Bitmap.Config.ARGB_8888)
//	.displayer(new FadeInBitmapDisplayer(300))
//	.build();
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView; 
		String dto = piclist.get(position).getPicPath();
		Integer id=piclist.get(position).get_position();
		Holder holder = null;
		
		if (view == null) {
			holder = new Holder();
			view = inflater.inflate(R.layout.global_pic_list_item, null);
			holder.img=(ImageView)view.findViewById(R.id.pic_show);
			holder.position=new Integer(id);
			view.setTag(holder);
		}else {
			holder = (Holder) view.getTag();
			
		}

		String imgurl=String.valueOf(piclist.get(position).getPicPath().toString());
		//图片显示类
		ImageLoader.getInstance().displayImage(imgurl, holder.img, AppConstant.options); 
        
		
		

		final int fpostion = position;	
		final Holder fholder = holder;
		
		return view;
	}
	
	
	static class Holder{
		ImageView img;
		Integer position;
		
	}
}
