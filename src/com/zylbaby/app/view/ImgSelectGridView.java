package com.zylbaby.app.view;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.AdapterView.OnItemClickListener;

import com.photo.select.Bimp;
import com.photo.select.FileUtils;
import com.photo.select.PhotoActivity;
import com.photo.select.TestPicActivity;
import com.photo.select.BasePhotoActivity.GridAdapter;
import com.photo.select.BasePhotoActivity.PopupWindows;
import com.photo.select.BasePhotoActivity.GridAdapter.ViewHolder;
import com.zylbaby.app.R;
import com.zylbaby.app.ui.AddLifeActivity;

public class ImgSelectGridView {
	
	private GridAdapter adapter;
	private Context context;
	private String fromactivity;
	@SuppressLint("HandlerLeak") Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				adapter.notifyDataSetChanged();
				break;
			}
			super.handleMessage(msg);
		}
	};

	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater; // 视图容器
		private int selectedPosition = -1;// 选中的位置
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context cx,Handler hd) {
			inflater = LayoutInflater.from(cx);
			handler=hd;
			context=cx;
			
			
		}

		public void update() {
			loading();
		}

		public int getCount() {
			return (Bimp.bmp.size() + 1);
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

		/**
		 * ListView Item设置
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			final int coord = position;
			ViewHolder holder = null;
			if (convertView == null) {

				convertView = inflater.inflate(R.layout.photo_item_published_grida,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == Bimp.bmp.size()) {
//				holder.image.setImageBitmap(BitmapFactory.decodeResource(
//						getResources(), R.drawable.photo_icon_addpic_unfocused));
				if (position == 9) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp.bmp.get(position));
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

		

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp.max == Bimp.drr.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							try {
								String path = Bimp.drr.get(Bimp.max);
								System.out.println(path);
								Bitmap bm = Bimp.revitionImageSize(path);
								Bimp.bmp.add(bm);
								String newStr = path.substring(
										path.lastIndexOf("/") + 1,
										path.lastIndexOf("."));
								FileUtils.saveBitmap(bm, "" + newStr);
								Bimp.max += 1;
								Message message = new Message();
								message.what = 1;
								handler.sendMessage(message);
							} catch (IOException e) {

								e.printStackTrace();
							}
						}
					}
				}
			}).start();
		}
	}

	public class PopupWindows extends PopupWindow {

		public PopupWindows(Context mContext, View parent,String fromname) {
			fromactivity=fromname;
			View view = View
					.inflate(mContext, R.layout.photo_item_popupwindows, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.photo_fade_ins));
			LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.photo_push_bottom_in_2));

			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();

			Button bt1 = (Button) view
					.findViewById(R.id.item_popupwindows_camera);
			Button bt2 = (Button) view
					.findViewById(R.id.item_popupwindows_Photo);
			Button bt3 = (Button) view
					.findViewById(R.id.item_popupwindows_cancel);
			bt1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
//					photo();
					dismiss();
				}
			});
			bt2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					
					Intent intent = new Intent(context,
							TestPicActivity.class); //图片选择器
					intent.putExtra("fromactivity", fromactivity);
//					startActivity(intent);
					dismiss();
				}
			});
			bt3.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});

		}
	}

	
	public void Init(final Context context,View view,final GridView noScrollgridview){
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		//adapter = new GridAdapter(this);
		adapter=new GridAdapter(context,handler);
		adapter.update();
		noScrollgridview.setAdapter(adapter);
		
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == Bimp.bmp.size()) {
					new PopupWindows(context, noScrollgridview,"life");
				} else {
					Intent intent = new Intent(context,
							PhotoActivity.class);
					intent.putExtra("ID", arg2);
					//intent.putExtra("activityname", "life");
					context.startActivity(intent);
				}
			}
		});
	}
}
