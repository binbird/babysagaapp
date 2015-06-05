package com.zylbaby.app.adapter;

import com.zylbaby.app.R;
import com.zylbaby.app.ui.AddWandrActivity;
import com.zylbaby.app.util.Bimp;
import com.zylbaby.app.util.DimensionUtility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;


@SuppressLint("HandlerLeak")
public class GridAdapter extends BaseAdapter {
	
	private GridView noScrollgridview;
	private Context context;
	
	private LayoutInflater inflater;
	private int selectedPosition = -1;
	private boolean shape;

	public boolean isShape() {
		return shape;
	}

	public void setShape(boolean shape) {
		this.shape = shape;
	}

	public GridAdapter(Context context,GridView gridview) {
		inflater = LayoutInflater.from(context);
		noScrollgridview=gridview;
		context=context;
	}

	public void update() {
		//loading();
	}

	public int getCount() {
		if(Bimp.tempSelectBitmap.size() == 9){
			return 9;
		}
		return (Bimp.tempSelectBitmap.size() + 1);
	}

	public Object getItem(int arg0) {
		return null;
	}

	public long getItemId(int arg0) {
		return 0;
	}

	public void setSelectedPosition(int position) {
		selectedPosition = position;
	}

	public int getSelectedPosition() {
		return selectedPosition;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_published_grida,
					parent, false);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView
					.findViewById(R.id.item_grida_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (position ==Bimp.tempSelectBitmap.size()) {
			holder.image.setImageBitmap(BitmapFactory.decodeResource(
				context.getResources(), R.drawable.icon_addpic_unfocused));
			if (position == 9) {
				holder.image.setVisibility(View.GONE);
			}
		} else {
			holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
		}

		return convertView;
	}

	public class ViewHolder {
		public ImageView image;
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				setListViewHeightBasedOnChildren(context,noScrollgridview);
				GridAdapter.this.notifyDataSetChanged();
				break;
			}
			super.handleMessage(msg);
		}
	};

	public void loading() {
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					if (Bimp.max == Bimp.tempSelectBitmap.size()) {
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
						break;
					} else {
						Bimp.max += 1;
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
					}
				}
			}
		}).start();
	}

	public void setListViewHeightBasedOnChildren(Context context,GridView gridview) {
		ListAdapter gridAdapter = gridview.getAdapter();

		if (gridAdapter == null) {
			return;
		}
		int totalHeight = 0;
		double num = gridAdapter.getCount();
		double size=4;
		double bug = num/size;
		double line = Math.ceil(bug); 
		int totalline = (int) line;
		for (int i = 0; i <totalline; i++) {
			totalHeight += DimensionUtility.dip2px(context, 55); // 统计所有子项的总高度
		}
		ViewGroup.LayoutParams params = gridview.getLayoutParams();
//		if(totalline == 1){
			params.height = totalHeight;
//		}else{
//			params.height = totalHeight-30*(totalline);
//		}
		// listView.getDividerHeight() 获取子项间分隔符占用的高度
		gridview.setLayoutParams(params);
	}
}

