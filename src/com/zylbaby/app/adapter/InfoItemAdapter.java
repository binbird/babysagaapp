package com.zylbaby.app.adapter;

import java.util.List;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zylbaby.app.R;
import com.zylbaby.app.adapter.InfoMainAdapter.Holder;
import com.zylbaby.app.bean.InfoItem;
import com.zylbaby.app.bean.InfoMainBean;
import com.zylbaby.app.global.AppConstant;
import com.zylbaby.app.global.AppContext;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class InfoItemAdapter extends BaseAdapter {

	private Context context;
	private List<InfoItem> infoitemlist;
	LayoutInflater inflater;
	private int touser;
	
	private int self;
	

	
	
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
	
	public static interface IMsgViewType {
		int LAYOUT_LEFT = 0;
		int LAYOUT_RIGHT = 1;
	}
	
	public InfoItemAdapter(Context context,List<InfoItem> arrayinfoitem,int self){
		this.context=context;
		this.infoitemlist=arrayinfoitem;
		inflater = LayoutInflater.from(context);
		this.touser=self;
		
		
	}
	@Override
	public int getItemViewType(int position) {
		InfoItem entity = infoitemlist.get(position);

		if (entity.isMyself()) {
			return IMsgViewType.LAYOUT_RIGHT;
		} else {
			return IMsgViewType.LAYOUT_LEFT;
		}
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return infoitemlist.size();
	}
	@Override
	public int getViewTypeCount() {
        return 2;
    }

	@Override
	public Object getItem(int position) {
		
		return infoitemlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View view = convertView; 
		InfoItem dto = infoitemlist.get(position);
		Holder holder = null;
		int l=getItemViewType(position);
		if (view == null) {
			holder = new Holder();
			//int k=dto.getByuser();
			String ss=dto.getContent();
//			view = inflater.inflate(R.layout.add_info_activity_listitem, null);
			boolean k=dto.isMyself();
			if(!k){
				view = LayoutInflater.from(context).inflate(R.layout.add_info_activity_listitem, null);
				
			}else 
			{
				view=LayoutInflater.from(context).inflate(R.layout.add_info_activity_listitem_right, null);
				
			}
			holder.time=(TextView) view.findViewById(R.id.infotime);
			holder.txContext=(TextView)view.findViewById(R.id.infoitem_content);
			
			holder.img=(ImageView) view.findViewById(R.id.infoitem_pic);
			view.setTag(holder);
		}else {
			holder = (Holder) view.getTag();
			
		}
		holder.txContext.setText(String.valueOf(dto.getContent()));
		holder.time.setText(String.valueOf(dto.getTime()));
		//显示图片路径
		String imgurl=dto.getImgpath();
		///图片显示类
//		if(infoitemlist.get(position).getInfotype()==0){
		ImageLoader.getInstance().displayImage(imgurl, holder.img, AppConstant.options);
//		}
		
		final int fpostion = position;	
		final Holder fholder = holder;
		
		
		return view;
	}
	
	
	static class Holder{
		Integer Id;
		TextView txContext;
		ImageView img;
		TextView time;
		
	}

}
