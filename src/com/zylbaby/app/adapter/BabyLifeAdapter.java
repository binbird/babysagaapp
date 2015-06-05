package com.zylbaby.app.adapter;

import java.util.ArrayList;
import java.util.List;

import com.loveplusplus.demo.image.ImagePagerActivity;
import com.zylbaby.app.R;
import com.zylbaby.app.adapter.BabyDiaryAdapter.Holder;
import com.zylbaby.app.bean.DiaryDto;
import com.zylbaby.app.bean.LifePic;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BabyLifeAdapter extends BaseAdapter{

	private List<DiaryDto> arrayDiary;

	
	private LayoutInflater inflater;
	private AdpterOnItemClick diaryAdpterOnclick;
	
	Button bt =null;
	int p = 0;
	private Context context;
	public BabyLifeAdapter(Context context,List<DiaryDto> arrayDiary) {
		this.context=context;
		this.arrayDiary = arrayDiary;
		inflater = LayoutInflater.from(context);
	}
	public void onListener(AdpterOnItemClick listener){
		 this.diaryAdpterOnclick  = listener;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrayDiary.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return arrayDiary.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView; 
		DiaryDto dto = arrayDiary.get(position);
		Holder holder = null;
		
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT, 300);
		//convertView.setLayoutParams(lp);
		
		if (view == null) {
			holder = new Holder();
			int piccount=arrayDiary.get(position).getPiccount();
			if(piccount==0){
				view = inflater.inflate(R.layout.main_fragment_life_item0, null);
				
			}else if(piccount==1){
				view = inflater.inflate(R.layout.main_fragment_life_item1, null);
			}else if(piccount==2){
				view = inflater.inflate(R.layout.main_fragment_life_item2, null);
			}else if(piccount>=3){
				view = inflater.inflate(R.layout.main_fragment_life_item3, null);
			}
			view.setFocusable(false);
			//view.setLayoutParams(lp);
			holder.Id=new Integer(position);
			holder.txTime = (TextView) view.findViewById(R.id.lift_time);
			//holder.txTitle = (TextView) view.findViewById(R.id.txt_diarytile);
			holder.txContext=(TextView)view.findViewById(R.id.lift_content);
			//holder.img=(ImageView)view.findViewById(R.id.diarytype_ioc);
			holder.gv=(GridView) view.findViewById(R.id.lift_grid_pic);
			view.setTag(holder);
			holder.gv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					
					int k=	((com.zylbaby.app.adapter.PicListAdapter.Holder)arg1.getTag()).position;
					
					Toast.makeText(context, String.valueOf(k), Toast.LENGTH_SHORT).show();
					LoadPic(k);
					
					
				}
			});
			
			holder.gv.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Toast.makeText(context, "长按事件", Toast.LENGTH_SHORT).show();
					return false;
				}
			});
			
		}else {
			holder = (Holder) view.getTag();
			
		}
		System.out.print(arrayDiary);
		int piccount=arrayDiary.get(position).getPiccount();
		List<String> piclist=arrayDiary.get(position).getPicList();
		holder.txTime.setText(String.valueOf(arrayDiary.get(position).getDiaryTitle()));		
		//holder.txTitle.setText(String.valueOf(arrayDiary.get(position).getDiaryTitle()));
		holder.txContext.setText(String.valueOf(arrayDiary.get(position).getDiarycontext()));
		
		
		
		ArrayList<LifePic> l= new ArrayList<LifePic>();
		for(int m=0;m<piclist.size();m++){
			LifePic pic= new LifePic();
			pic.set_position(position);
			pic.setPicPath(piclist.get(m).toString());
			l.add(pic);
		}
		
		
		PicListAdapter myAdpter= new PicListAdapter(context, l);
		//holder.gv.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1, l));
          holder.gv.setAdapter(myAdpter);
       if(piccount==1){
  			holder.gv.setNumColumns(1);
  			
  		}
  		if(piccount==2 && piccount==4 ){
  			holder.gv.setNumColumns(2);
  		}
  		if(piccount==3 || piccount>4){
  			holder.gv.setNumColumns(3);
  		}
  		holder.gv.refreshDrawableState();
		int k=arrayDiary.get(position).getDiarytypeId();
		int m=0;

		final int fpostion = position;	
		final Holder fholder = holder;
		
		return view;
	}
	
	
	private void LoadPic(int id){
		Intent intent = new Intent(context, ImagePagerActivity.class);
		//图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		List<String> piclist= this.arrayDiary.get(id).getPicList();
		String[] picdata=new String[piclist.size()];
		piclist.toArray(picdata);
		intent.putExtra("images", picdata);
		context.startActivity(intent);
	}


	
	
	
	
	
	static class Holder{
		Integer Id;
		TextView txTime;
		TextView txTitle;	
		TextView txContext;
		Button btPic  ;
		Button btDel ;
		ImageView img;
		GridView gv;
		
	}

	

}
