package com.zylbaby.app.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.zylbaby.app.R;
import com.zylbaby.app.adapter.InfoMainAdapter.AdpterOnItemClick;
import com.zylbaby.app.adapter.InfoMainAdapter.Holder;
import com.zylbaby.app.bean.InfoMainBean;
import com.zylbaby.app.bean.TeacherInfoMainBean;
import com.zylbaby.app.global.AppConstant;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TeacherInfoMainAdapter  extends BaseAdapter{

	private Context context;
	private List<TeacherInfoMainBean> infomainlist;
	LayoutInflater inflater;
	

	
	private AdpterOnItemClick infoAdpterOnclick;
	interface AdpterOnItemClick{
		void onAdpterClick(int which,int postion);
	}
	
	public TeacherInfoMainAdapter(Context context,List<TeacherInfoMainBean> arraymaininfo){
		this.context=context;
		this.infomainlist=arraymaininfo;
		inflater = LayoutInflater.from(context);
	}
	
	public void onListener(AdpterOnItemClick listener){
		 this.infoAdpterOnclick  = listener;
	}
	@Override
	public int getCount() {

		return infomainlist.size();
	}

	@Override
	public Object getItem(int position) {

		return infomainlist.get(position);
	}

	@Override
	public long getItemId(int position) {
	
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView; 
		TeacherInfoMainBean dto = infomainlist.get(position);
		Holder holder = null;
		
		if (view == null) {
			holder = new Holder();
			//替换视图
			view = inflater.inflate(R.layout.teacher_main_fragment_info_item, null);
			holder.txtName = (TextView) view.findViewById(R.id.banjiName);
			holder.TxtCount = (TextView) view.findViewById(R.id.infocount);
			holder.txContext=(TextView)view.findViewById(R.id.InfoContent);
			holder.time=(TextView) view.findViewById(R.id.InfoTime);
			holder.img=(ImageView) view.findViewById(R.id.babyimg);
			view.setTag(holder);
		}else {
			holder = (Holder) view.getTag();
			
		}
		holder.txtName.setText(infomainlist.get(position).getName());		
		holder.TxtCount.setText(String.valueOf(infomainlist.get(position).getCount()));
		holder.txContext.setText(String.valueOf(infomainlist.get(position).getContent()));
		holder.time.setText(String.valueOf(infomainlist.get(position).getInfotime()));
		
		String imgurl=infomainlist.get(position).getBabyimg();
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
