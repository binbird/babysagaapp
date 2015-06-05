package com.zylbaby.app.adapter;

import java.util.List;

import com.zylbaby.app.R;
import com.zylbaby.app.adapter.BanjiAdapter.Holder;
import com.zylbaby.app.bean.BanjiDto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class BabyClassListAdapter extends BaseAdapter {

	List<BanjiDto> arrayBanji;
	LayoutInflater inflater;
	Context context;

	public BabyClassListAdapter(Context context,List<BanjiDto> arrayBanji) {
		this.context=context;
		this.arrayBanji = arrayBanji;
		inflater = LayoutInflater.from(context);
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
			view = inflater.inflate(R.layout.baby_inclass_listitem, null);
			holder.txName = (TextView) view.findViewById(R.id.class_name);
			holder.txDescribe = (TextView) view.findViewById(R.id.class_describe);
			
			//holder.txContext=(TextView)view.findViewById(R.id.diarycontent);

			view.setTag(holder);
		}else {
			holder = (Holder) view.getTag();
			
		}
		holder.txName.setText(arrayBanji.get(position).getName());		
		holder.txDescribe.setText(String.valueOf(arrayBanji.get(position).getDescribe()));

		final int fpostion = position;	
		final Holder fholder = holder;
				
		return view;
	}
	
	
	static class Holder{
		TextView txName;
		TextView txDescribe;	

		
	}



}
