package com.zylbaby.app.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.zylbaby.app.R;
import com.zylbaby.app.adapter.BanjiAdapter.Holder;
import com.zylbaby.app.bean.BanjiDto;
import com.zylbaby.app.bean.DiaryDto;
import com.zylbaby.app.bean.InfoMainBean;
import com.zylbaby.app.global.AppConstant;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class InfoMainAdapter extends BaseAdapter{

	private Context context;
	private List<InfoMainBean> infomainlist;
	LayoutInflater inflater;
	

	
	
//	private DisplayImageOptions  options = new DisplayImageOptions.Builder()
//	.showImageForEmptyUri(R.drawable.ic_empty)
//	.showImageOnFail(R.drawable.ic_error)
//	.resetViewBeforeLoading(true)
//	.cacheInMemory(true)
//	.cacheOnDisc(true)
//	.imageScaleType(ImageScaleType.EXACTLY)
//	.bitmapConfig(Bitmap.Config.ARGB_8888)
//	.displayer(new FadeInBitmapDisplayer(300))
//	.build();
	
	
	private AdpterOnItemClick infoAdpterOnclick;
	interface AdpterOnItemClick{
		void onAdpterClick(int which,int postion);
	}
	
	public InfoMainAdapter(Context context,List<InfoMainBean> arraymaininfo){
		this.context=context;
		this.infomainlist=arraymaininfo;
		inflater = LayoutInflater.from(context);
	}
	
	public void onListener(AdpterOnItemClick listener){
		 this.infoAdpterOnclick  = listener;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return infomainlist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return infomainlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView; 
		InfoMainBean dto = infomainlist.get(position);
		Holder holder = null;
		
		if (view == null) {
			holder = new Holder();
			view = inflater.inflate(R.layout.main_fragment_info_item, null);
			holder.txtName = (TextView) view.findViewById(R.id.banjiName);
			holder.TxtCount = (TextView) view.findViewById(R.id.infocount);
			holder.txContext=(TextView)view.findViewById(R.id.InfoContent);
			holder.time=(TextView) view.findViewById(R.id.InfoTime);
			holder.img=(ImageView) view.findViewById(R.id.BanjiIco);
			view.setTag(holder);
		}else {
			holder = (Holder) view.getTag();
			
		}
		holder.txtName.setText(infomainlist.get(position).getName());		
		holder.TxtCount.setText(String.valueOf(infomainlist.get(position).getCount()));
		holder.txContext.setText(String.valueOf(infomainlist.get(position).getContent()));
		holder.time.setText(String.valueOf(infomainlist.get(position).getInfotime()));
		
		String imgurl=infomainlist.get(position).getMainpic();
		//图片显示类
		ImageLoader.getInstance().displayImage(imgurl, holder.img, AppConstant.options);
		
		final int fpostion = position;	
		final Holder fholder = holder;
		
		
		return view;
	}
	
	
	
	static class Holder{
		Integer Id;
		TextView txtName;	
		TextView txContext;
		ImageView img;
		TextView TxtCount;
		TextView time;
		
	}

}
