package com.zylbaby.app.adapter;

import android.content.Context;
import java.util.List;

import com.zylbaby.app.R;
import com.zylbaby.app.bean.DiaryDto;
import com.zylbaby.app.global.AppConstant;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

interface AdpterOnItemClick{
	void onAdpterClick(int which,int postion);
}
interface AdpterOnItemClickDelete{
	void onAdpterClickDelete(int which,int postion);
}
/**
 * 宝贝作息列表适配器
 * @author jiangbin
 *
 */
public class BabyDiaryAdapter extends BaseAdapter {


	
	
	private List<DiaryDto> arrayDiary;

	
	private LayoutInflater inflater;
	private AdpterOnItemClick diaryAdpterOnclick;
	private AdpterOnItemClickDelete diaryAdpterOnclickDelete;
	
	
	
	
	Button bt =null;
	int p = 0;
	
	public BabyDiaryAdapter(Context context,List<DiaryDto> arrayDiary) {
		this.arrayDiary = arrayDiary;
		inflater = LayoutInflater.from(context);
	}
	public void onListener(AdpterOnItemClick listener){
		 this.diaryAdpterOnclick  = listener;
	}
	
	public void OnDeleteListener(AdpterOnItemClickDelete listener){
		this.diaryAdpterOnclickDelete=listener;
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
		
		if (view == null) {
			holder = new Holder();
			view = inflater.inflate(R.layout.new_diarylist_item_3, null);
			holder.txTime = (TextView) view.findViewById(R.id.diaryitem_time);
			holder.txTitle = (TextView) view.findViewById(R.id.diaryitem_title);
			holder.txContext=(TextView)view.findViewById(R.id.diaryitem_content);
			holder.btDel=(Button) view.findViewById(R.id.btn_del);
			holder.img=(ImageView)view.findViewById(R.id.diaryitem_mainpic);
			holder.stateimg=(ImageView) view.findViewById(R.id.dairyitem_star);
			holder.piccount=(TextView) view.findViewById(R.id.pic_count);
			view.setTag(holder);
		}else {
			holder = (Holder) view.getTag();
			
		}
		holder.txTime.setText(arrayDiary.get(position).getEntrytime());		
		holder.txTitle.setText(String.valueOf(arrayDiary.get(position).getDiaryTitle()));
		holder.txContext.setText(String.valueOf(arrayDiary.get(position).getDiarycontext()));
		int k1=arrayDiary.get(position).getRank();
		int n=0;
		for(int i=0;i<AppConstant.starsid_array.length;i++){
			if(k1==AppConstant.starsid_array[i]){
				n=i;
				break;
			}
		}
		holder.stateimg.setImageResource(AppConstant.starsioc_array[n]); //baby 表现
		int k=arrayDiary.get(position).getDiarytypeId();
		int m=0;
		for(int i=0;i<AppConstant.diarytypeid_array.length;i++){
			if(k==AppConstant.diarytypeid_array[i]){
				m=i;
				break;
			}
		}
		holder.img.setImageResource(AppConstant.diaryioc_array[m]);
		holder.img.setMaxWidth(100);
		holder.piccount.setText(String.valueOf(arrayDiary.get(position).getPiccount()));
		final int fpostion = position;	
		final Holder fholder = holder;
		
		return view;
	}
	
	static class Holder{
		TextView txTime;
		TextView txTitle;	
		TextView txContext;
		Button btPic  ;
		Button btDel ;
		ImageView img;
		ImageView stateimg;
		TextView piccount;
		
	}

}
