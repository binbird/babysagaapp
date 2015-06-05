package com.zylbaby.app.adapter;

import java.util.List;


import com.zylbaby.app.R;
import com.zylbaby.app.adapter.BabyDiaryAdapter.Holder;
import com.zylbaby.app.bean.BanjiDto;
import com.zylbaby.app.bean.DiaryDto;
import com.zylbaby.app.util.UIHelper;
import com.zylbaby.app.view.BanjiAdpterOnItemClick;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;



public class BanjiAdapter extends BaseAdapter{

	List<BanjiDto> arrayBanji;

	BanjiAdpterOnItemClick myAdpterOnclick;
	LayoutInflater inflater;
	
	Context context;
	public BanjiAdapter(Context context,List<BanjiDto> arrayBanji) {
		this.context=context;
		this.arrayBanji = arrayBanji;
		inflater = LayoutInflater.from(context);
	}
	
	public void onListener(BanjiAdpterOnItemClick listener){
		 this.myAdpterOnclick  = listener;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrayBanji.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return arrayBanji.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView; 
		BanjiDto dto = arrayBanji.get(position);
		Holder holder = null;
		
		if (view == null) {
			holder = new Holder();
			view = inflater.inflate(R.layout.return_searchbanji_item, null);
			holder.txName = (TextView) view.findViewById(R.id.banji_name);
			holder.txDescribe = (TextView) view.findViewById(R.id.banji_describe);
			holder.btJoin=(Button) view.findViewById(R.id.banji_joinbtn);
			//holder.txContext=(TextView)view.findViewById(R.id.diarycontent);

			view.setTag(holder);
		}else {
			holder = (Holder) view.getTag();
			
		}
		holder.txName.setText(arrayBanji.get(position).getName());		
		holder.txDescribe.setText(String.valueOf(arrayBanji.get(position).getDescribe()));
		//holder.txContext.setText(String.valueOf(arrayBanji.get(position).getDiarycontext()));
		holder.btJoin.setTag(position);
		
		
		final int fpostion = position;	
		final Holder fholder = holder;
		
		holder.btJoin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (myAdpterOnclick != null) {
					
					int which = v.getId();
					myAdpterOnclick.onAdpterClick(which, fpostion);
					//UIHelper.ToastMessage(context, v.getId().toString());
					showBanjiId(fpostion, fholder);
					
				}
			}
		});
		
		
		
		return view;
	}
	
	static class Holder{
		TextView txName;
		TextView txDescribe;	
		//TextView txContext;
		Button btJoin  ;
		Button btPic  ;
		Button btDel ;
		
	}

	public void showBanjiId(int index ,Holder holder){
		if (holder!=null) {
			Integer i = new Integer(arrayBanji.get(index).getId());
			//UIHelper.ToastMessage(context, i.toString());

//			holder.txAge.setText(String.valueOf(age));
		}
		
	}

}
