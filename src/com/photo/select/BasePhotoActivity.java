package com.photo.select;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.zylbaby.app.R;
import com.zylbaby.app.global.AppConstant;





import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class BasePhotoActivity extends Activity{
	
	public GridAdapter adapter;
	public Handler handler;
	//protected ImageLoader imageLoader = ImageLoader.getInstance();
	private String fromactivity;
	private Context context;
	protected static String localTempImageFileName = ""; 
	
	
	public static final File FILE_PIC_SCREENSHOT = new File(AppConstant.FILE_LOCAL,
            "images/screenshots");
	    /*** 
      * 使用照相机拍照获取图片 
      */  
  public static final int SELECT_PIC_BY_TACK_PHOTO = 1;  
  /*** 
     * 使用相册中的图片 
     */  
  public static final int SELECT_PIC_BY_PICK_PHOTO = 2;

  //定义一个ProgressDialog
	public ProgressDialog pd;
	// 设置标记
	public final int SIGN = 0x11;
  
  
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

//				convertView = inflater.inflate(R.layout.photo_item_published_grida,
//						parent, false);
				
				convertView=LayoutInflater.from(context).inflate(
						R.layout.photo_item_published_grida, null);
				
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

				String imgurl="drawable://" + R.drawable.photo_icon_addpic_focused;
				ImageLoader.getInstance().displayImage(imgurl, holder.image,AppConstant.options);   
		        
				
				
				
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

	
	public  void ClearBimp(){
		for(int i=0;i<Bimp.bmp.size();i++){
			 if(Bimp.bmp.get(i)!=null){
				 try{
				 Bimp.bmp.get(i).recycle();
				 }
				 catch(Exception e){
				 
				 }
			 
			 }
		 }
		 System.gc();
	}
	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}
	
	
	public class PopupWindows extends PopupWindow {

		public PopupWindows(Context mContext, View parent,String fromname) {
			fromactivity=fromname;
			View view = getLayoutInflater().inflate(R.layout.photo_item_popupwindows, null);
					
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
					photo();
					dismiss();
				}
			});
			bt2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					
					Intent intent = new Intent(context,
							TestPicActivity.class); //图片选择器
					intent.putExtra("fromactivity", fromactivity);
					startActivity(intent);
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

	public static final int TAKE_PICTURE = 0x000000;
	public String path = "";

	public void photo() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			try {
				localTempImageFileName = "";
				localTempImageFileName = String.valueOf((new Date())
						.getTime()) + ".png";
				File filePath = FILE_PIC_SCREENSHOT;
				if (!filePath.exists()) {
					filePath.mkdirs();
				}
				Intent intent = new Intent(
						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				File f = new File(filePath, localTempImageFileName);
				// localTempImgDir和localTempImageFileName是自己定义的名字
				Uri u = Uri.fromFile(f);
				intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
				
				startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
			} catch (ActivityNotFoundException e) {
				//
			}
		}
		
		
	}

	// 加载界面
	// 调用onPrepareDialog()方法之后
	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		pd = new ProgressDialog(this);
		pd.setMessage("正在加载......");
		pd.setProgressStyle(ProgressDialog.THEME_HOLO_DARK);
		pd.setCancelable(false);
		return pd;
	}
	// 调用showDialog()之后和调用onCreateDialog之前
	@Override
	protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
		switch (id) {
		case SIGN:
			break;
		}
	}


	public void ShowPd(){
		pd.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				boolean onkey = false;
				int keyCode1 = event.getKeyCode();
				if (keyCode1 == KeyEvent.KEYCODE_BACK
						&& event.getAction() != KeyEvent.ACTION_UP) {
					if (event.getRepeatCount() == 0) {
						pd.dismiss();
						pd.cancel();
						onkey = true;
					}
				}
				return onkey;
			}
		});
	}
}
