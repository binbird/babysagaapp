package com.zylbaby.app.adapter;

import java.util.ArrayList;


import com.zylbaby.app.R;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HelpListAdapter extends BaseAdapter {

	private ArrayList<String> array;
	private LayoutInflater inflater;
	
	public HelpListAdapter(Context context,ArrayList<String> arraytitle){
		this.array = arraytitle;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return array.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return array.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView; 
		String dto = array.get(position);
		Holder holder = null;
		
		if (view == null) {
			holder = new Holder();
			view = inflater.inflate(R.layout.help_list_item_activity, null);
			holder.txTitle = (TextView) view.findViewById(R.id.help_title);
			view.setTag(holder);
		}else {
			holder = (Holder) view.getTag();
			
		}
		
		holder.txTitle.setText(String.valueOf(array.get(position)));
		final int fpostion = position;	
		final Holder fholder = holder;
		return view;
	}
	
	
	static class Holder{
		TextView txTitle;	
	}

}
