package com.zylbaby.app.adapter;

import java.util.List;

import com.zylbaby.app.R;
import com.zylbaby.app.bean.ScheduleDto;
import com.zylbaby.app.global.AppConstant;
import com.zylbaby.app.view.BanjiAdpterOnItemClick;
import com.zylbaby.app.view.BanjiScheduleOnItemDeleteClick;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;



public class ScheduleAdapter extends BaseAdapter{


	private List<ScheduleDto> arraySchedule;

	BanjiAdpterOnItemClick myAdpterOnclick; //作息完成操作
	BanjiScheduleOnItemDeleteClick myAdpterDelOnclick; //删除操作
	
	private LayoutInflater inflater;
	//private AdpterOnItemClick scheduleAdpterOnclick;
	int FormId;
	
	public ScheduleAdapter(Context context,List<ScheduleDto> arraySchedule,int formid) {
		this.arraySchedule = arraySchedule;
		inflater = LayoutInflater.from(context);
		FormId=formid;
	}
	//完成
	public void onListener(BanjiAdpterOnItemClick listener){
		 this.myAdpterOnclick  = listener;
	}
	//删除
	public void onDelListener(BanjiScheduleOnItemDeleteClick listener){
		this.myAdpterDelOnclick=listener;
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arraySchedule.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return arraySchedule.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView; 
		ScheduleDto dto = arraySchedule.get(position);
		Holder holder = null;
		
		if (view == null) {
			holder = new Holder();
			view = inflater.inflate(R.layout.teacher_schedule_item, null);//加载日程列表Item
			holder.txTime = (TextView) view.findViewById(R.id.diaryitem_time);
			holder.txTitle = (TextView) view.findViewById(R.id.diaryitem_title);
			holder.txContext=(TextView)view.findViewById(R.id.diaryitem_content);
			holder.img=(ImageView)view.findViewById(R.id.diaryitem_mainpic);
			holder.btover=(Button) view.findViewById(R.id.over_btn);
			
			holder.btDel=(Button) view.findViewById(R.id.del_btn);
			if(FormId==1){
				holder.btover.setVisibility(View.GONE);
				holder.btDel.setVisibility(View.GONE);
			}
			
			
			view.setTag(holder);
		}else {
			holder = (Holder) view.getTag();
			
		}
		holder.txTime.setText(arraySchedule.get(position).getEntrytime());		
		//holder.txTitle.setText(String.valueOf(arraySchedule.get(position).getScheduletitle()));
		holder.txContext.setText(String.valueOf(arraySchedule.get(position).getSchedulecontext()));
		String k=arraySchedule.get(position).getScheduletypeid();
		int m=0;
		for(int i=0;i<AppConstant.diarytypeid_array.length;i++){
			if(k==String.valueOf(AppConstant.diarytypeid_array[i])){
				m=i;
				break;
			}
		}
		holder.img.setImageResource(AppConstant.diaryioc_array[m]);
		holder.txTitle.setText(AppConstant.diarytype_array[m]);
		final int fpostion = position;	
		final Holder fholder = holder;
		
		holder.btover.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (myAdpterOnclick != null) {
					
					int which = v.getId();
					myAdpterOnclick.onAdpterClick(which, fpostion);
					//UIHelper.ToastMessage(context, v.getId().toString());
					//showBanjiId(fpostion, fholder);
					
				}
			}
		});
		
		holder.btDel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(myAdpterDelOnclick!=null){
					int which = v.getId();
					myAdpterDelOnclick.onAdpterDeleteClick(which, fpostion);
					//UIHelper.ToastMessage(context, v.getId().toString());
					//showBanjiId(fpostion, fholder);
				}
				
			}
		});
		
		
		
		
		return view;
	}
	
	public void showBanjiId(int index ,Holder holder){
		if (holder!=null) {
			Integer i = new Integer(arraySchedule.get(index).getId());

		}
		
	}
	
	static class Holder{
		TextView txTime;
		TextView txTitle;	
		TextView txContext;
		Button btPic  ;
		Button btDel ;
		ImageView img;
		Button btover;
		
	}

}
