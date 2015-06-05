package com.zylbaby.app.adapter;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.zylbaby.app.R;

import com.zylbaby.app.bean.BanjiBabyAndInOutBean;
import com.zylbaby.app.global.AppConstant;
import com.zylbaby.app.global.AppContext;

import com.zylbaby.app.util.AppException;
import com.zylbaby.app.util.StringUtils;
import com.zylbaby.app.view.BanjiAdpterOnItemClick;
import com.zylbaby.app.view.BanjiAdpterOnTimeSelect;

import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
/**
 * 
 * @author binbird
 *
 */
public class BanjiBabyAdapter extends BaseAdapter {

	BanjiAdpterOnItemClick myAdpterOnclick;
	BanjiAdpterOnTimeSelect myAdpterOnTimeSelect;
	
	private List<BanjiBabyAndInOutBean> arraybaby;

	private Context context;
	private LayoutInflater inflater;
	//private AdpterOnItemClick diaryAdpterOnclick;
	
	
	public BanjiBabyAdapter(Context context,List<BanjiBabyAndInOutBean> arrayBaby) {
		this.arraybaby = arrayBaby;
		this.context=context;
		inflater = LayoutInflater.from(context);
	}
	public void onListener(BanjiAdpterOnItemClick listener){
		 this.myAdpterOnclick  = listener;
	}
	//时间选择
	public void onTimeListener(BanjiAdpterOnTimeSelect listener){
		this.myAdpterOnTimeSelect=listener;
	}
	
	@Override
	public int getCount() {
		return arraybaby.size();
	}

	@Override
	public Object getItem(int position) {
		return arraybaby.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	
	
//	private DisplayImageOptions  options = new DisplayImageOptions.Builder()
//	.showImageForEmptyUri(R.drawable.baby_default)
//	.showImageOnFail(R.drawable.baby_default)
//	.resetViewBeforeLoading(true)
//	.cacheOnDisc(true)
//	.imageScaleType(ImageScaleType.EXACTLY)
//	.bitmapConfig(Bitmap.Config.ARGB_8888)
//	.displayer(new FadeInBitmapDisplayer(300))
//	.build();
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView; 
		BanjiBabyAndInOutBean dto = arraybaby.get(position);
		Holder holder = null;
		
		if (view == null) {
			holder = new Holder();
			view = inflater.inflate(R.layout.teacher_main_class_item, null);
			holder.txtname = (TextView) view.findViewById(R.id.baby_name);

			holder.babyimg=(ImageView)view.findViewById(R.id.baby_img);
			holder.btdaoxiao=(Button) view.findViewById(R.id.btn_daoxiao);
			holder.btlixiao=(Button) view.findViewById(R.id.btn_lixiao);
			view.setTag(holder);
		}else {
			holder = (Holder) view.getTag();
			
		}
		holder.txtname.setText(arraybaby.get(position).getName());		
		String imgurl=String.valueOf(arraybaby.get(position).getImg());
		holder.btdaoxiao.setText(arraybaby.get(position).getIntime());
		holder.btlixiao.setText(arraybaby.get(position).getOuttime());
		imageLoader  
        .displayImage(imgurl, holder.babyimg, AppConstant.options); 
		
		

		final int fpostion = position;	
		final Holder fholder = holder;
		final int babyid=arraybaby.get(fpostion).getId();
		holder.btdaoxiao.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (myAdpterOnTimeSelect != null) {
					
					int which = v.getId();
					
					//holder.btdaoxiao.setText(StringUtils.GetTime());
					String s=((Button)v).getText().toString();
					if("到校时间".trim().equals(s.toString().trim()) || "".trim().equals(s.trim())){ //没有判断出来
						((Button)v).setText(StringUtils.GetTime());
						arraybaby.get(fpostion).setIntime(StringUtils.GetTime());
						AddInOutSchoolTime(babyid,((Button)v).getText().toString(),1);
					}else{
						
						showTime(1,(Button)v,babyid,fpostion);
					}
					myAdpterOnTimeSelect.onTimeSelect(which, fpostion,((Button)v).getText().toString());
					//UIHelper.ToastMessage(context, v.getId().toString());
					showBanjiId(fpostion, fholder);
					
					
					
				}
			}
		});
		
		holder.btlixiao.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (myAdpterOnTimeSelect!= null) {
					
					int which = v.getId();
					String s=((Button)v).getText().toString();
					if("离校时间".trim().equals(s.toString().trim()) || "".trim().equals(s.trim())){
						
						((Button)v).setText(StringUtils.GetTime());
						arraybaby.get(fpostion).setOuttime(StringUtils.GetTime());
						AddInOutSchoolTime(babyid,((Button)v).getText().toString(),2);
						
					}else{
						
						showTime(2,(Button)v,babyid,fpostion);
					}
					myAdpterOnTimeSelect.onTimeSelect(which, fpostion,((Button)v).getText().toString());
					showBanjiId(fpostion, fholder);
				}
			}
		});
		
		holder.babyimg.setOnClickListener(new OnClickListener() {
			
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
		TextView txtname;
		Button btdaoxiao  ;
		Button btlixiao ;
		ImageView babyimg;
		
	}
	public void showBanjiId(int index ,Holder holder){
		if (holder!=null) {
			Integer i = new Integer(arraybaby.get(index).getId());

		}
		
	}
	
	private TimePickerDialog timePickerDialog;
	private Calendar calendar;
	private void showTime(final int sign,final Button v,final int babyid,final int postion) {

		/**
		 * 内部匿名类，实现TimePickerDialog.OnTimeSetListener接口，重写onTimeSet()方法
		 * 当弹出TimePickerDialog并设置完Time以后，左下方有个“Set”按钮，表示确定设置。当这个
		 * 按钮被点击的时候，就执行这里的onTimeSet()方法。
		 */
		TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker arg0, int arg1, int arg2) {
				v.setText(arg1 + ":" + arg2);
				
				AddInOutSchoolTime(babyid,((Button)v).getText().toString(),sign);
				arraybaby.get(postion).setOuttime(((Button)v).getText().toString());
			}

		};
		calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		timePickerDialog = new TimePickerDialog(context, onTimeSetListener,
				calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE), true);
		timePickerDialog.show();
	}
	private void AddInOutSchoolTime(final int babyid,final String time,final int type) {
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					Toast.makeText(context, "记录成功", Toast.LENGTH_SHORT).show();
					
				} else if (msg.what == 0) {
					Toast.makeText(context, String.valueOf(msg.obj) , Toast.LENGTH_SHORT).show();
				} else if (msg.what == -1) {
					Toast.makeText(context, String.valueOf(msg.obj) , Toast.LENGTH_SHORT).show();
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					AppContext ac = (AppContext)context.getApplicationContext();
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("BabyId",babyid);
					paramMap.put("Time", time);
					paramMap.put("InputType", type);


					int k = ac.InOutSchoolTime(paramMap); //增加日程
					//String res = baby.getStatusCode();
					if (k==0) {
						msg.what = 1;// 成功
						
					} else {
						//ac.cleanLoginInfo();// 清除登录信息
						msg.what = 0;// 失败
						msg.obj ="新增错误";
					}
				} catch (AppException e) {
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
				}
				handler.sendMessage(msg);
			}
		}.start();
	}
	
}
